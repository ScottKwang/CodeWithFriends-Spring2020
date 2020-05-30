
class SQSAttributeHandler:

    def __init__(self, attribute):
        self.attribute = attribute

    def handle(self, client, queue_url):
        response = client.get_queue_attributes(QueueUrl=queue_url)
        attributes = response['Attributes']
        return attributes.get(self.attribute)

    def handle_response(self, name, value):
        return f'The {self.attribute} for queue {name} is {value}'
