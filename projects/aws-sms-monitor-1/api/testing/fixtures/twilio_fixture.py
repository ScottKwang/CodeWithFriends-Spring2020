class TwilioTestClient:

    def __init__(self, sid, token):
        self.sid = sid
        self.token = token
        self.messages = TwillioTestClientMessages()


class TwillioTestClientMessages:
    def __init__(self):
        self.created = []

    def create(self, to, from_, body):
        self.created.append({
            'to': to,
            'from_': from_,
            'body': body
        })
        return TestMessage


class TestMessage:
    sid = 'test_message_sid'