import logging
from typing import List, Optional, Tuple

import boto3
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

from app import application as app
from app.db import Database
from app.resources import message_resources

from app.handlers.resource_handler import ResourceHandler

from app.resources import message_resources
from app.exceptions import AWSMonitorException, AWSUnauthorized

LOG = logging.getLogger(__name__)



def get_message_from_resource(resource_handler: Optional[ResourceHandler], tokenized_message: List[str]) -> str:
    if not resource_handler:
        raise AWSMonitorException
    return resource_handler.handle(tokenized_message)


def get_resource(tokenized_message: List[str]) -> ResourceHandler:
    resource_names = set(message_resources.keys())
    desired_resources = list(resource_names.intersection(tokenized_message))
    if len(desired_resources) != 1:
        raise AWSMonitorException
    
    resource = message_resources[desired_resources[0]]
    return resource


def get_subscriber_role(db: Database, from_phone: str)-> str:
    subscriber_role: Tuple[str] = db.query_db(f'SELECT role_arn FROM subscriber where phone={from_phone};', one=True)
    if not subscriber_role:
        raise AWSUnauthorized
    return subscriber_role[0]


def tokenize_message(message: str) -> List[str]:
    """
    TODO: see if I can use nltk tokenize
    https://stackoverflow.com/questions/743806/how-to-split-a-string-into-a-list
    """
    return list(set(message.split(' ')))
