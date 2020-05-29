"""
D365.Rest
~~~~~~~~~
"""

import re
from urllib.parse import urlparse
from requests import Request, Session

from D365API.Constant import D365_API_V

class Rest(object):
    """A REST (REpresentational State Transfer) object.

    General API (Application Programming Interface) for request and response.

    Basic Usage:

        >>> from D365API.Rest import Rest
        >>> rest = Rest(access)
        >>> rest.send("GET", "/accounts", None)
    """

    def __init__(self, access, hostname):
        """Constructor.

        Args:
            access (str): The Microsoft Dynamics 365 access token.
            hostname (str): The Hostname of the environment.
        """

        self._access_token = access
        self._url = "https://{hostname}.crm.dynamics.com/".format(hostname=hostname)

        # Create header
        self._header = {
            "Authorization": "Bearer " + self._access_token,
            "Content-Type": "application/json; charset=utf-8",
            "Accept": "application/json",
            "OData-Version": "4.0",
            "OData-MaxVersion": "4.0"
        }


    def send(self, method, relative_url, payload):
        """Send REST Request.

        Args:
            method (str): The HTTP method for the request.
            relative_url (str): The relative URL for the HTTP request, with or
                without the beginning slash ( / ).
                Example: /api/data/v9.1/ or /accounts
            payload (dict): The body of the HTTP request.

        Returns:
            A string formatted JSON for the HTTP response object.
        """

        # TODO: Expand method to take certificate and extra parameters.

        # Create the request URL
        request_url = self._url

        if "api/data" in relative_url:
            # If the complete relative URL is provided
            request_url += "/" + relative_url
        else:
            # Assume only a partial relative URL is provided
            request_url += "/api/data/v" + D365_API_V + "/" + relative_url

        # Clean up any extra slash ( / )
        request_url = re.sub(r"(?<=[^:\s])(\/+\/)", r"/", request_url)

        # Create a session
        session = Session()

        # Create the request
        request = Request(method=method,
                          url=request_url,
                          headers=self._header,
                          data=payload)
                          
        # Prepare the request
        preparation = request.prepare()

        # Send the prepared request for response
        response = session.send(preparation)

        return response