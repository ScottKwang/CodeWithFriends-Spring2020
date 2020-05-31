"""

"""
import boto3


class ReceiveEventTest:
    valid_phone = '+15555555555'
    invalid_phone = '123+14213453'

    def _setup_mock_values(self, db, role_service):
        db.query_db.return_value = ('test_arn',)
        role_service.return_value = boto3.Session()

    @staticmethod
    def _create_webhook_payload(phone, message):
        return {         
            'From': phone,
            'Body': message
        }
