# AWS-resource-chatbot

The purpose of this project is to enable monitoring resources in AWS. It's just a fun personal project with external apis and AWS.


## Local

TBD


### Database Setup

TODO: add database setup

After adding the database, generate `api/app/models.py` via

```
sqlacodegen sqlite:///data/user.db
```

## Testing

Before running any unittests, I highly recommend adding a virtual environment and adding test requirements

```bash
# from ~/path/to/aws-sms-monitor/
python3 -m venv virtualenv
source virtualenv/bin activate
pip install -r requirements/test.txt
```

If you ever leave the project, running 

```bash
deactivate 
```

In order to run the tests, 

```bash
# from ~/path/api/aws-sms-monitor/
python -m unittest discover api/testing/ 
```

All tests must pass before merging.

## Contributing

Any help in testing, development, documentation and other tasks is highly appreciated and useful to the project. However, still in the design stage, so I'm a little less inclined for outside help until it's all sorted out. 
