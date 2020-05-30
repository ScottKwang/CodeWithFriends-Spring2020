

class DynamoLimitHandler:

    def __init__(self, attribute):
        self.attribute = attribute

    def handle(self, client, stream_name):
        response = client.describe_limits()
        return attributes.get(self.attribute)

    def handle_response(self, name, value):
        return f'The {self.attribute} for the account is {value}'
