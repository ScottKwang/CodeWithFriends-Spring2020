from abc import ABC, abstractmethod 


class ResourceHandler(ABC):

    def __init__(self, session, cache):
        self.client = session.client(self.resource)
        self.cache = cache

    @abstractmethod
    def handle(self, tokenized_message):
        pass

    @abstractmethod
    def _refresh_resources(self):
        pass

