import logging
import re
from typing import Dict, List, Tuple, Set, Optional

from app.handlers.resource_handler import ResourceHandler
from app.handlers.dynamo.dynamo_limit_handler import DynamoLimitHandler 
from app.handlers.dynamo.dynamo_table_attribute_handler import DynamoTableAttributeHandler 
from app.exceptions import AWSResourceMissing, AWSInvalidCommand, AWSMultipleResources 

LOG = logging.getLogger(__name__)


class DynamoHandler(ResourceHandler):
    resource = 'dynamodb'
    cache_key = 'dynamo_tables'
    intents = {
        'count': DynamoTableAttributeHandler('ItemCount'),
        'TableSizeBytes': DynamoTableAttributeHandler('TableSizeBytes'),
        'status': DynamoTableAttributeHandler('TableStatus'),
        'account_read': DynamoLimitHandler('AccountMaxReadCapacityUnits'),
        'account_write': DynamoLimitHandler('AccountMaxWriteCapacityUnits'),
        'table_read': DynamoLimitHandler('TableMaxReadCapacityUnits'),
        'table_write': DynamoLimitHandler('TableMaxWriteCapacityUnits'),
    }

    def handle(self, tokenized_message: List[str]) -> str:
        message_intent: str = self.get_intent(tokenized_message)
        if not message_intent:
            queue_commands = ', '.join(intents.keys())
            raise AWSInvalidCommand(self.resource, queue_commands)

        name = self.get_name(tokenized_message)
        if not name and message_intent not in self.account_level_intents:
            raise AWSResourceMissing(self.resource)
        
        handler = self.intents[message_intent]
        value = handler.handle(self.client, name)
        return handler.handle_response(name, value)

    def get_intent(self, tokenized_message: List[str])-> str:
        intent_tokens: Set[str] = set(self.intents.keys())
        message_intent = list(intent_tokens.intersection(tokenized_message))

        if len(message_intent) != 1:
            raise AWSInvalidCommand(self.resource, intent_tokens)
        return message_intent[0]

    def get_name(self, tokenized_message: List[str]) -> Optional[str]:
        """
            {
                'TableNames': [
                    'test_table_name',
                ]
            }
        """
        intended_tables = self._get_intended_dynamno_table_name(tokenized_message)
        if len(intended_tables) == 0:
            return None
        if len(intended_tables) > 1:
            raise AWSMultipleResources(self.resource)

        table_name = intended_tables[0]
        return table_name

    def _get_intended_dynamno_table_name(self, tokenized_message: List[str]) -> List[str]:
        tables: List[str]
        if self.cache.get(self.cache_key):
            tables = self.cache.get(self.cache_key)
        else:
            tables = self._refresh_resources()

        table_names = set(tables)
        return list(table_names.intersection(tokenized_message))

    def _refresh_resources(self):
        response: Dict[str, str] = self.client.list_tables()
        tables = [name for name in response['TableNames']]

        self.cache.set(self.cache_key, tables, ex=3600)
        return tables
