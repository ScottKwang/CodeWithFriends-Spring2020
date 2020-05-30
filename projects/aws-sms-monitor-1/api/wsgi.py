import os

from app import application
from app.settings import PORT


if __name__ == '__main__':
    if os.environ.get('DEBUG', False):
        application.debug = True

    application.run(
        host='0.0.0.0',
        port=PORT,
    )
