"""
D365API.Entity
~~~~~~~~~~~~~~
"""

import json
from urllib.parse import urlparse

from D365API.Rest import Rest
from D365API.Constant import HTTP_GET
from D365API.Constant import HTTP_POST
from D365API.Constant import HTTP_PATCH
from D365API.Constant import HTTP_DELETE

class Entity(Rest):
    """Entity.
    """

    def __getattr__(self, label):
        """Get Attribute Passed In.

        Args:
            label (str): The attribute passed in.

        Returns:
            A instance of the Entity class.
        """
        # Set the name / label
        self._label = label

        # Return the self instance
        return self


    def create(self, payload):
        """Create Entity.

        Args:
            payload (dict): The payload (message body) passed in.

        Returns:
            A string for the unique identifier (ID) of the Entity.
        """

        # Create the relative (request) URL
        relative_url = "/{name}".format(name=self._label)

        # Send the request for a response
        r = self.send(HTTP_POST, relative_url, payload)

        # Check the status code
        if r.status_code == 204:
            # Parse the unique identifier (ID) of the entity
            entity_url = r.headers["OData-EntityId"]
            begin_id = entity_url.find("(") + 1
            end_id = entity_url.find(")")
            entity_id = entity_url[begin_id:end_id]
            # Return the unique identifier (ID) of the entity
            return entity_id

        # There was an error
        return None


    def read(self, id=None):
        """Read Entity.

        Args:
            id (str): The unique identifier (ID) of the entity.

        Returns:
            A string formatted JSON for the read request
        """

        # Create a read result list to store all the read result
        read_result_list = []

        # Create the relative (request) URL
        if id is not None:
            relative_url = "/{name}({id})".format(name=self._label, id=id)
        else:
            relative_url = "/{name}".format(name=self._label)

        # Send the request for a response
        r = self.send(HTTP_GET, relative_url, None)

        # Parse the read result
        read_result = json.loads(r.text)

        # Check the status code
        if r.status_code == 200:
            # Add the `value` to read result list
            read_result_list.extend(read_result["value"])

        # Check if there are more result
        while "@odata.nextLink" in read_result:
            # If there are more result
            # Parse the `nextLink` URL
            u = urlparse(read_result["@odata.nextLink"])
            # Rebuild the relative URL using the path and query
            relative_url = u.path + u.query
            # Send the request for a response
            r = self.send(HTTP_GET, relative_url, None)

            # Check the status code
            if r.status_code == 200:
                # Parse the read result
                read_result = json.loads(r.text)
                # Add the `value` to read result list
                read_result_list.append(read_result["value"])

        # Return all the read result
        return read_result_list


    def update(self, id, payload):
        """Update Entity.

        Args:
            id (str): The unique identifier (ID) of the entity.
            payload (dict): The payload (message body) passed in.

        Returns:
            An integer for the status code of the update request.
        """

        # Create the relative (request) URL
        relative_url = "/{name}({id})".format(name=self._label, id=id)

        # Send the request for a response
        r = self.send(HTTP_PATCH, relative_url, payload)

        # Check the status code
        if r.status_code == 204:
            # Return the status code
            return r.status_code

        # There was an error
        return None


    def delete(self, id):
        """Delete Entity.

        Args:
            id (str): The unique identifier (ID) of the entity.

        Returns:
            An integer for the status code of the delete request.
        """

        # Create the relative (request) URL
        relative_url = "/{name}({id})".format(name=self._label, id=id)

        # Send the request for a response
        r = self.send(HTTP_DELETE, relative_url, None)

        # Check the status code
        if r.status_code == 204:
            # Return the status code
            return r.status_code

        # There was an error
        return None


    def query(self, **kwargs):
        """Query Entity.

        .. _Query Data using the Web API:
        https://docs.microsoft.com/en-us/powerapps/developer/common-data-service/webapi/query-data-web-api

        .. _Web API Query Data Sample:
        https://docs.microsoft.com/en-us/powerapps/developer/common-data-service/webapi/web-api-query-data-sample

        Args:
            kwargs (dict): The keyword arguments for the query.

        Returns:
            A string formatted JSON for the result of the query.
        """
        
        # Build the query
        query = ""

        # If the `select` system query option is specified
        if "select" in kwargs:
            query += "$select={}".format(kwargs["select"])
        # If the `top` system query option is specified
        if "top" in kwargs:
            query += "$top={}".format(kwargs["top"])

        # Create the relative (request) URL
        relative_url = "/{name}?{query}".format(name=self._label, query=query)

        # Send the request for a response
        r = self.send(HTTP_GET, relative_url, None)

        # Check the status code
        if r.status_code == 200:
            # Return the response text (message body)
            return r.text

        # There was an error
        return None