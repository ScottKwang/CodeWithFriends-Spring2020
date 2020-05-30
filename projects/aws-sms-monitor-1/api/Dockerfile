# Regular Container
FROM python:3.7
ENV PYTHONUNBUFFERED 1

ADD . /var/www/api
WORKDIR /var/www/api

RUN pip install -r requirements/prod.txt

ENV FLASK_APP api
ENV FLASK_ENV development
ENV PORT 3000
ENV DEBUG True
EXPOSE 3000 3000
