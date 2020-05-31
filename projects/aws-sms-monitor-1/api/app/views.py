import os
import logging
from typing import Set, List, Optional
import boto3

from flask import request, jsonify
from twilio.rest import Client 

from app import application as app
from app.exceptions import AWSMonitorException
from app.handlers.resource_handler import ResourceHandler
from app.db import Database
from app.services.twilio_message_service import TwilioMessageService
from app.services.assume_role_service import assumed_role_session
from app.settings import (
    DEBUG,
    TEST_PHONE_TO,
    TWILIO_ACCOUNT_SID,
    TWILIO_AUTH_TOKEN,
    DATABASE_PATH,
)
from app.utils import (
    get_message_from_resource,
    get_resource,
    get_subscriber_role,
    tokenize_message,
    
)


LOG = logging.getLogger(__name__)

db = Database(DATABASE_PATH)
client = Client(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN)
message_handler = TwilioMessageService(client)


# TODO remove this endpoint
@app.route('/')
def hello_world():
    return 'Your App is running!'


@app.route('/receive-events', methods=['POST'])
def receive_events():
    """
        Recieve SMS Events from the Twilio Webhook :)  
        TODO: ensure endpoint is only Twilio!

        Expected Json Body
        {
            "Body": "modified message text",
            "Attributes": "{\"key\" : \"value\"}"
        }
    """
    webhook_body: Dict[str, str] = request.values
    message_body = webhook_body.get('Body', None)
    from_phone = webhook_body.get('From', None)
    LOG.info(f'Receiving incoming sms message from {from_phone}')
    try:
        subscriber_role = get_subscriber_role(db, from_phone)
        aws_session = assumed_role_session(subscriber_role)

        webhook_body: Dict[str, str] = request.values
        message_body = webhook_body.get('Body', None)
        tokenized_message: List[str] = tokenize_message(message_body)
        resource_handler_class: ResourceHandler = get_resource(tokenized_message)
        resource_handler = resource_handler_class(aws_session, app.cache)
        message_response: str = get_message_from_resource(resource_handler, tokenized_message)    
    except AWSMonitorException as err:
        message_response: str = err.message
    message_sid = message_handler.send_message(TEST_PHONE_TO, message_response)
    return jsonify(body=message_response)


# TODO: Move To Adding Slack
@app.route('/slack-events', methods=['POST'])
def notification_events():
    LOG.info('Receiving incoming webhook message')
    message_body = "sqs test_sqs_queue"
    message_sid = message_handler.handle_send_message(TEST_PHONE_TO, message_body)
    return message_sid
