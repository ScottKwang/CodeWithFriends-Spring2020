

class DynamoTableAttributeHandler:

    def __init__(self, attribute):
        self.attribute = attribute

    def handle(self, client, table_name):
        response = client.describe_table(TableName=table_name)
        attributes = response['Table']
        return attributes.get(self.attribute)

    def handle_response(self, name, value):
        return f'The {self.attribute} for table {name} is {value}'
