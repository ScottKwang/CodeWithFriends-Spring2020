import os

import redis
import boto3

from flask import Flask, request, render_template
from werkzeug.debug import DebuggedApplication

from flask_debugtoolbar import DebugToolbarExtension

from app import settings

# Cache for heavily accessed values
cache = redis.Redis(host='0.0.0.0', port=6379)

application = Flask(__name__)

import app.views

if settings.DEBUG:
    application.wsgi_app = DebuggedApplication(application.wsgi_app, evalex=True)
