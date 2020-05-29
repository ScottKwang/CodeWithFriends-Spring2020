# Python Microsoft Dynamic 365 Application Programming Interface
Python Microsoft Dynamic 365 Application Programming Interface

[![Python Version](https://img.shields.io/badge/python-3.7-blue.svg)][python-version]

[python-version]: https://www.python.org/

A basic Python REST API client built for Python 3.0+.
This framework provide an integration to the [Microsoft Dynamics 365 Web API](https://docs.microsoft.com/en-us/powerapps/developer/common-data-service/webapi/overview) resources.

## Table of Contents
* [Quick Start](#quick-start)
* [Authentication](#authentication)
* [Usage](#usage)

## Quick Start

The framework contain different modules, currently there are:
* Access
* Rest
* Entity

```python
# Import each module individually as needed
from D365API.Access import Access
from D365API.Entity import Entity
```

## Authentication

The `Access` module allows the user to authenticate with the system using [OAuth](https://en.wikipedia.org/wiki/OAuth).
It accepts a fix list of valid credentials and returns an access (bearer) token.
* **hostname:** the unique organization name for the environment
* **client_id:** the client (application) ID of the Azure registered application
* **client_secret:** the client secret (key) of the Azure registered application
* **tenant_id:** the tenant (directory) ID of the environment

```python
# Create an instance of Access object
access = Access(hostname=hostname,
                client_id=client_id,
                client_secret=client_secret,
                tenant_id=tenant_id)

# Use the Access object to login
access_token = access.login()

# You can also do it all in one step
# Create an instance of Access object and login
access = Access(hostname=hostname,
                client_id=client_id,
                client_secret=client_secret,
                tenant_id=tenant_id).login()
```

## Usage

First and foremost is to create an instance of Entity, passing in the authenticated access token and hostname.

```python
# Create an instance of Entity
entity = Entity(access_token, hostname)
```

### Create

```python
# Create the payload
payload = {
    # Generate a random Account Name
    "name": "Account-{}".format(random.randrange(10000, 99999))
}

# Make a request to create the Account
# Get the return unique identifier (ID)
# The payload need to be serialized to JSON formatted str (json.dumps)
account_id = self.entity.accounts.create(json.dumps(payload))
```

### Read

```python
# Make a request to read the Account
result = self.entity.accounts.read(account_id)
```

### Update

```python
# Create the payload
payload = {
    # Generate a random Account Name
    "name": "Account-{}".format(random.randrange(10000, 99999))
}

# Make a request to update the Account with unique identifier (ID)
# Update the Account Name with the newly generated Account Name
result = self.entity.accounts.update(account_id, json.dumps(payload))
```

### Delete

```python
# Make a request to delete the Account with unique identifier (ID)
result = self.entity.accounts.delete(account_id)
```

### Query

```python
# Define the query property
query = {
    "select": "accountid,name"
}

# Make a request to query the Account
result = self.entity.accounts.query(**query)
```