import logging
import re
from typing import Dict, List, Tuple, Set, Optional

from app.handlers.resource_handler import ResourceHandler
from app.handlers.kinesis.kinesis_limit_handler import KinesisLimitHandler 
from app.handlers.kinesis.kinesis_attribute_handler import KinesisAttributeHandler 
from app.exceptions import AWSResourceMissing, AWSInvalidCommand, AWSMultipleResources 

LOG = logging.getLogger(__name__)


class KinesisHandler(ResourceHandler):
    resource = 'kinesis'
    cache_key = 'kinesis_queues'
    stream_url_regex = r'https:\/\/.+[1-9]\/(.+)'
    account_level_intents = ['limit', 'count']
    intents = {
        'arn': KinesisAttributeHandler('StreamARN'),
        'encryption': KinesisAttributeHandler('EncryptionType'),
        'retention': KinesisAttributeHandler('RetentionPeriodHours'),
        'created': KinesisAttributeHandler('StreamCreationTimestamp'),
        'limit': KinesisLimitHandler('ShardLimit'),
        'count': KinesisLimitHandler('OpenShardCount')
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
                'StreamNames': [
                    'test-queue',
                    'test-queue2',
                ]
            }
        """
        intended_streams = self._get_intended_stream_name(tokenized_message)
        if len(intended_streams) == 0:
            return None
        if len(intended_streams) > 1:
            raise AWSMultipleResources(self.resource)

        stream_name = intended_streams[0]
        return stream_name

    def _get_intended_stream_name(self, tokenized_message: List[str]) -> List[str]:
        streams: List[str]
        if self.cache.get(self.cache_key):
            streams = self.cache.get(self.cache_key)
        else:
            streams = self._refresh_resources()

        stream_names = set(streams)
        return list(stream_names.intersection(tokenized_message))

    def _refresh_resources(self):
        response: Dict[str, str] = self.client.list_streams()
        streams = [url for url in response['StreamNames']]

        self.cache.set(self.cache_key, streams, ex=3600)
        return streams
