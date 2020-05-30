import json, sys
import os
import unittest

# import twilio
from fixtures.twilio_fixture import TwilioTestClient
import boto3
from moto import mock_kinesis, mock_sts
from mock import patch

from dotenv import load_dotenv
load_dotenv(verbose=True)

from app import views, application as app
from app.exceptions import AWSResourceMissing, AWSInvalidCommand
from app.handlers.kinesis.kinesis_attribute_handler import KinesisAttributeHandler
from app.handlers.kinesis.kinesis_limit_handler import KinesisLimitHandler

from app.exceptions import AWSUnauthorized
from app.handlers.kinesis.kinesis_handler import KinesisHandler
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
class ReceiveEventApi(unittest.TestCase, ReceiveEventTest):
    stream_name = 'test_kinesis_stream'
    valid_phone = '+15555555555'

    def test_message_handler_with_unauthorized_access(self, db, role_service):
        self.mock_patched_values_with_null(db, role_service)
        with app.test_client() as client:
            # Arrange
            client.application.cache = Cache()
            sent = self._create_webhook_payload(
                self.valid_phone,
                'placeholder text because I\'m unauthorized to see this'
            )
            expected = AWSUnauthorized().message

            # Act
            result = client.post(
                '/receive-events',
                data=sent
            )
            result_payload = result.data.decode("utf-8")

            # assert
            self.assertEqual(expected, json.loads(result_payload)['body'])

    def mock_patched_values_with_null(self, db, role_service):
        db.query_db.return_value = None
        role_service.return_value = None
