"""
API & HTTP Clients exceptions.
"""


class AWSMonitorException(Exception):
    """
    Base class for exceptions.
    """
    def __init__(self):
        self.message = 'Sorry, did not understand that'


class AWSUnauthorized(AWSMonitorException):
    
    def __init__(self):
        self.message = 'You are not authorized to access this chatbot'


class AWSResourceMissing(AWSMonitorException):

    def __init__(self, resource):
        self.message = f'The {resource} you requested does not exist or not permissioned'


class AWSInvalidCommand(AWSMonitorException):
    
    def __init__(self, resource, commands):
        self.message = f'The Available Commands for {resource} are {commands}'


class AWSMultipleResources(AWSMonitorException):
    
    def __init__(self, resource, resource_names):
        self.message = f'Too many resources requested for {resource}, please select one of {resource_names}'
