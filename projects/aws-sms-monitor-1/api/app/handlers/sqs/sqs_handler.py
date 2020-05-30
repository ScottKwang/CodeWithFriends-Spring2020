import logging
import re
from typing import Dict, List, Tuple, Set, Optional

from app.handlers.resource_handler import ResourceHandler
from app.handlers.sqs.sqs_attribute_handler import SQSAttributeHandler
from app.exceptions import AWSResourceMissing, AWSInvalidCommand

LOG = logging.getLogger(__name__)


class SQSHandler(ResourceHandler):
    resource = 'sqs'
    cache_key = 'sqs_queues'
    queue_url_regex = r'https:\/\/.+[1-9]\/(.+)' 
    intents = {
        'size': SQSAttributeHandler('ApproximateNumberOfMessages'),
        'created': SQSAttributeHandler('CreatedTimestamp'),
        'retention': SQSAttributeHandler('MessageRetentionPeriod')
    }

    def handle(self, tokenized_message: List[str]) -> str:
        name, url = self.get_name(tokenized_message)
        if not name:
            raise AWSResourceMissing(self.resource)
        message_intent: str = self.get_intent(tokenized_message)
        if not message_intent:
            queue_commands = ', '.join(intents.keys())
            raise AWSInvalidCommand(self.resource, queue_commands)

        handler = self.intents[message_intent]
        value = handler.handle(self.client, url)
        return handler.handle_response(name, value)

    def get_intent(self, tokenized_message: List[str])-> str:
        intent_tokens: Set[str] = set(self.intents.keys())
        message_intent = list(intent_tokens.intersection(tokenized_message))

        if len(message_intent) != 1:
            raise AWSInvalidCommand(self.resource, intent_tokens)
        return message_intent[0]

    def get_name(self, tokenized_message: List[str]) -> Optional[Tuple[str, str]]:
        """
            {
                'QueueUrls': [
                    'https://sqs.us-east-2.amazonaws.com/838802343873/test-queue-monitor',
                ]
            }
        """
        # TODO refactor out more of this
        intended_queues, queues = self._get_queue_url_mapping(tokenized_message)        
        if len(intended_queues) != 1:
            raise AWSResourceMissing(self.resource)
        queue_name = intended_queues[0]
        return queue_name, queues[queue_name]

    def _get_queue_url_mapping(self, tokenized_message):
        queues: Dict[str, str]
        if self.cache.get(self.cache_key):
            queues = self.cache.get(self.cache_key)
        else:
            queues = self._refresh_resources()
        queue_names = set(queues.keys())
        intended_queues: List = list(queue_names.intersection(tokenized_message))
        return intended_queues, queues
    
    def _refresh_resources(self):
        response: Dict[str, str] = self.client.list_queues()
        queues = {re.match(self.queue_url_regex, url).group(1): url for url in response['QueueUrls']}
        self.cache.set(self.cache_key, queues, ex=3600)
        return queues
