import json, sys
import os
import unittest

# import twilio
from fixtures.twilio_fixture import TwilioTestClient
import boto3
from moto import mock_dynamodb2
from mock import patch

from dotenv import load_dotenv
load_dotenv(verbose=True)

from app import views, application as app
from app.exceptions import AWSResourceMissing, AWSInvalidCommand
from app.handlers.dynamo.dynamo_table_attribute_handler import DynamoTableAttributeHandler
from app.handlers.dynamo.dynamo_limit_handler import DynamoLimitHandler

from app.handlers.dynamo.dynamo_handler import DynamoHandler
from app.services.twilio_message_service import TwilioMessageService
from testing.fixtures.cache_fixture import Cache
from testing.receive_event_test import ReceiveEventTest

# set our application to testing mode
app.testing = True
views.message_handler = TwilioMessageService(TwilioTestClient(
    os.getenv('TEST_ACCOUNT_SID'),
    os.getenv('TEST_AUTH_TOKEN')
))

# TODO: switch to use metadata class
@patch('app.views.assumed_role_session')
@patch('app.views.db')
class ReceiveEventDynamoApi(unittest.TestCase, ReceiveEventTest):
    table_name = 'test_dynamo_table'

    @mock_dynamodb2
    def test_dynamo_message_handler_with_count_type_message_returns_count(self, db, role_service):
        self._setup_mock_values(db, role_service)
        with app.test_client() as client:
            # Arrange
            client.application.cache = Cache()
            self._create_test_table(self.table_name)

            sent = self._create_webhook_payload(
                self.valid_phone,
                'what is the dynamodb test_dynamo_table count'
            )
            expected = DynamoTableAttributeHandler('ItemCount').handle_response(self.table_name, '0')

            # Act
            result = client.post(
                '/receive-events',
                data=sent
            )
            result_payload = result.data.decode("utf-8")

            # assert
            self.assertEqual(expected, json.loads(result_payload)['body'])


    @staticmethod
    def _create_test_table(table_name):
        client = boto3.client('dynamodb')
        client.create_table(
            AttributeDefinitions=[
                {
                    'AttributeName': 'name',
                    'AttributeType': 'S'
                },
            ],
            TableName=table_name,
            KeySchema=[
                {
                    'AttributeName': 'name',
                    'KeyType': 'HASH'
                }
            ],
        )

    def _populate_test_table(table_name):
        client = boto3.client('dynamo')

        client.put_item(
            TableName=table_name,
            Item={
            'name': {
                'S': 'hello',
            },
        })
    