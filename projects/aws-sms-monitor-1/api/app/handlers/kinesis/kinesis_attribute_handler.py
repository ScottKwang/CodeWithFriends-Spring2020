


class KinesisAttributeHandler:

    def __init__(self, attribute):
        self.attribute = attribute

    def handle(self, client, stream_name):
        response = client.describe_stream_summary(StreamName=stream_name)
        attributes = response['StreamDescriptionSummary']
        return attributes.get(self.attribute)

    def handle_response(self, name, value):
        return f'The {self.attribute} for stream {name} is {value}'
