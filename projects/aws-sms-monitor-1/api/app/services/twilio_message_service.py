import logging

from twilio.rest import Client 
from twilio.base.exceptions import TwilioRestException

from app.settings import TESTING, FROM_PHONE_NUMBER

LOG = logging.getLogger(__name__)


class TwilioMessageService:

    def __init__(self, client):
        self.client = client

    def send_message(self, to, message):
        if not TESTING and not is_valid_number(to):
            LOG.warning(f'Phone Number is an invalid number')
            return
        message = self.client.messages.create( 
            from_=FROM_PHONE_NUMBER,
            to=to,
            body=message,
        )
        return message.sid

    def is_valid_number(self, number: str) -> bool:
        try:
            response = self.client.lookups.phone_numbers(number).fetch(type="carrier")
            return True
        except TwilioRestException as e:
            if e.code == 20404:
                return False
            else:
                raise e
