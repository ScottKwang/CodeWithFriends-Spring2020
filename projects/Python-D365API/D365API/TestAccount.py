"""
D365API.TestAccount
~~~~~~~~~~~~~~~~~~~
"""

import json
import os
import random
import unittest

from D365API.Access import Access
from D365API.Entity import Entity
from D365API.Constant import HTTP_GET

def setUpModule():
    """Set Up Module"""
    pass


def tearDownModule():
    """Tear Down Module"""
    pass


class TestAccountCreate(unittest.TestCase):
    """Test the Entity module with Create Account."""
    
    @classmethod
    def setUpClass(cls):
        """Prepare test set up class.

        Get the data from JSON (JavaScript Object Notation) file and
        login.
        """

        # Get the current directory of the file
        current_directory = os.path.dirname(os.path.abspath(__file__))
        # Get the path of the Test Data file
        cls.test_data_file = os.path.join(current_directory, "TestData.json")

        # Open the file for reading
        with open(cls.test_data_file, "r") as f:
            cls.data = json.load(f)

        # Get the hostname from the Test Data
        cls.hostname = cls.data["organizations"]["name"]

        # Get the user data for success login
        user_rest_v1_success = cls.data["systemusers"]["user_rest_v1_success"]

        # Create an instance of Access object and login
        cls.access = Access(hostname=cls.hostname,
                            client_id=user_rest_v1_success["client_id"],
                            client_secret=user_rest_v1_success["client_secret"],
                            tenant_id=user_rest_v1_success["tenant_id"]).login()

        # Create an instance of Entity
        cls.entity = Entity(cls.access, cls.hostname)


    def test_account_create_random_failure(self):
        """Test a random failure for Account create.

        Get the hostname from the Test Data and the generated a random
        Account field to make a request for create. Should result in
        response with status code 400 Bad Request.
        """
        
        # Create the payload
        payload = {
            # Generate a random Account field
            "random": "Random-{}".format(random.randrange(10000, 99999))
        }

        # Make a request to create the Account with random field
        # Create the Account with the newly generated Account field
        create_account_id = self.entity.accounts.create(json.dumps(payload))

        # Test to ensure Account information is None
        self.assertIsNone(create_account_id)


    @unittest.expectedFailure
    def test_account_create_description_failure(self):
        """Test a description failure for Account create.

        Get the hostname from the Test Data and the generated a random
        Account Description to make a request for create. Should result
        in response with status code 400 Bad Request.
        """
        
        # Create the payload
        payload = {
            # Generate a random Account description
            "description": "Description-{}".format(random.randrange(10000, 99999))
        }

        # Make a request to create the Account with random description
        # Create the Account with the newly generated Account description
        create_account_id = self.entity.accounts.create(json.dumps(payload))

        # Test to ensure Account information is None
        self.assertIsNone(create_account_id)


    def test_account_create_success(self):
        """Test a success for Account create.

        Get the hostname from the Test Data and the generated Account
        Name to make a request for create. Should result in response
        with status code 204 No Content.
        """

        # Create the payload
        payload = {
            # Generate a random Account Name
            "name": "Account-{}".format(random.randrange(10000, 99999))
        }

        # Make a request to create the Account
        # Get the return unique identifier (ID)
        # The payload need to be serialized to JSON formatted str (json.dumps)
        create_account_id = self.entity.accounts.create(json.dumps(payload))

        # Create the dictionary
        self.data["accounts"]["create_account_success"] = {}
        # Create or update the Account ID in the Test Data
        self.data["accounts"]["create_account_success"]["id"] = create_account_id

        # Write the new Test Data to file
        with open(self.test_data_file, "w") as f:
            json.dump(self.data, f)

        # Test to ensure Account ID is a string
        self.assertEqual(type(create_account_id), str)


    @classmethod
    def tearDownClass(cls):
        """Prepare test tear down class.

        Clean up Test Data.
        """

        # Get the create Account success unique identifier (ID)
        create_account_id = cls.data["accounts"]["create_account_success"]["id"]
        # Make a request to delete the Account
        create_account = cls.entity.accounts.delete(create_account_id)
        # Check if the delete was successful
        if create_account == 204:
            # Delete the Account entry from the Test Data
            if "create_account_success" in cls.data["accounts"]:
                del cls.data["accounts"]["create_account_success"]

        # Write the new Test Data to file
        with open(cls.test_data_file, "w") as f:
            json.dump(cls.data, f)


class TestAccountRead(unittest.TestCase):
    """Test the Entity module with Read Account."""
    
    @classmethod
    def setUpClass(cls):
        """Prepare test set up class.

        Get the data from JSON (JavaScript Object Notation) file and
        login.
        """

        # Get the current directory of the file
        current_directory = os.path.dirname(os.path.abspath(__file__))
        # Get the path of the Test Data file
        cls.test_data_file = os.path.join(current_directory, "TestData.json")

        # Open the file for reading
        with open(cls.test_data_file, "r") as f:
            cls.data = json.load(f)

        # Get the hostname from the Test Data
        cls.hostname = cls.data["organizations"]["name"]

        # Get the user data for success login
        user_rest_v1_success = cls.data["systemusers"]["user_rest_v1_success"]

        # Create an instance of Access object and login
        cls.access = Access(hostname=cls.hostname,
                            client_id=user_rest_v1_success["client_id"],
                            client_secret=user_rest_v1_success["client_secret"],
                            tenant_id=user_rest_v1_success["tenant_id"]).login()

        # Create an instance of Entity
        cls.entity = Entity(cls.access, cls.hostname)

        # Create the payload
        payload = {
            # Generate a random Account Name
            "name": "Account-{}".format(random.randrange(10000, 99999))
        }

        # Make a request to create the Account
        # Get the return unique identifier (ID)
        # The payload need to be serialized to JSON formatted str (json.dumps)
        account_id = cls.entity.accounts.create(json.dumps(payload))

        # Create the dictionary
        cls.data["accounts"]["read_account_success"] = {}
        # Create or update the Account ID in the Test Data
        cls.data["accounts"]["read_account_success"]["id"] = account_id

        # Write the new Test Data to file
        with open(cls.test_data_file, "w") as f:
            json.dump(cls.data, f)


    def test_account_read_failure(self):
        """Test a failure of Account read.

        Get the hostname from the Test Data to make a request for read.
        Should result in response with None.
        """

        # Get the read Account failure unique identifier (ID)
        read_account_id = self.data["accounts"]["read_account_failure"]["id"]

        # Make a request to read the Account with unique identifier (ID)
        account = self.entity.accounts.read(read_account_id)

        # Test to ensure read Account information is None
        self.assertIsNone(account)


    def test_account_read_success(self):
        """Test a success for Account read.

        Get the hostname from the Test Data to make a request for read.
        Should result in response with status code 200 OK and a string
        formatted JSON for the result of the read.
        """

        # Get the Account unique identifier (ID) from Test Data
        read_account_id = self.data["accounts"]["read_account_success"]["id"]

        # Make a request to read the Account
        read_account = self.entity.accounts.read(read_account_id)

        # Test to ensure Account information is a string
        self.assertEqual(type(read_account), str)


    def test_account_read_count_success(self):
        """Test a success for Account read count.

        Get the hostname from the Test Data to make a request for read.
        Should result in response with a list, the count of the read
        result list should be 1 greater than the count of the function
        call. This is due to Account create from `setUpClass`.
        """

        # Get the Account count using the `RetrieveTotalRecordCount` function
        account_count_result = self.entity.send(HTTP_GET, "RetrieveTotalRecordCount(EntityNames=['account'])", None)
        # Parse the Account count result
        account_count = json.loads(account_count_result.text)["EntityRecordCountCollection"]["Values"][0]

        # Make a request to read the Account
        read_account = self.entity.accounts.read()

        # Test to ensure Account count (+ 1) is the same as Account read
        self.assertEqual(account_count + 1, len(read_account))


    @classmethod
    def tearDownClass(cls):
        """Prepare test tear down class.

        Clean up Test Data.
        """

        # Get the read Account success unique identifier (ID)
        read_account_id = cls.data["accounts"]["read_account_success"]["id"]
        # Make a request to delete the Account
        read_account = cls.entity.accounts.delete(read_account_id)
        # Check if the delete was successful
        if read_account == 204:
            # Delete the Account entry from the Test Data
            if "read_account_success" in cls.data["accounts"]:
                del cls.data["accounts"]["read_account_success"]

        # Write the new Test Data to file
        with open(cls.test_data_file, "w") as f:
            json.dump(cls.data, f)


class TestAccountUpdate(unittest.TestCase):
    """Test the Entity module with Update Account."""
    
    @classmethod
    def setUpClass(cls):
        """Prepare test set up class.

        Get the data from JSON (JavaScript Object Notation) file and
        login.
        """

        # Get the current directory of the file
        current_directory = os.path.dirname(os.path.abspath(__file__))
        # Get the path of the Test Data file
        cls.test_data_file = os.path.join(current_directory, "TestData.json")

        # Open the file for reading
        with open(cls.test_data_file, "r") as f:
            cls.data = json.load(f)

        # Get the hostname from the Test Data
        cls.hostname = cls.data["organizations"]["name"]

        # Get the user data for success login
        user_rest_v1_success = cls.data["systemusers"]["user_rest_v1_success"]

        # Create an instance of Access object and login
        cls.access = Access(hostname=cls.hostname,
                            client_id=user_rest_v1_success["client_id"],
                            client_secret=user_rest_v1_success["client_secret"],
                            tenant_id=user_rest_v1_success["tenant_id"]).login()

        # Create an instance of Entity
        cls.entity = Entity(cls.access, cls.hostname)

        # Create the payload
        payload = {
            # Generate a random Account Name
            "name": "Account-{}".format(random.randrange(10000, 99999))
        }

        # Make a request to create the Account
        # Get the return unique identifier (ID)
        # The payload need to be serialized to JSON formatted str (json.dumps)
        account_id = cls.entity.accounts.create(json.dumps(payload))

        # Create the dictionary
        cls.data["accounts"]["update_account_success"] = {}
        # Create or update the Account ID in the Test Data
        cls.data["accounts"]["update_account_success"]["id"] = account_id

        # Write the new Test Data to file
        with open(cls.test_data_file, "w") as f:
            json.dump(cls.data, f)


    def test_account_update_failure(self):
        """Test a failure for Account update.

        Get the hostname from the Test Data and generate an incorrect
        Account Name to make a request for update. Should result in
        response with None.
        """

        # Get the read Account failure unique identifier (ID)
        update_account_id = self.data["accounts"]["update_account_failure"]["id"]

        # Create the payload
        payload = {
            # Generate a random Account Name
            "name": "Account-{}".format(random.randrange(10000, 99999))
        }

        # Make a request to update the Account with the incorrect unique identifier (ID)
        # Update the Account Name with the newly generated Account Name
        update_account = self.entity.accounts.update(update_account_id, json.dumps(payload))

        # Test to ensure update Account information is None
        self.assertIsNone(update_account)


    def test_account_update_success(self):
        """Test a success for Account update.

        Get the hostname from the Test Data and the generated a new
        Account Name to make a request for update. Should result in
        response with status code 204 No Content.
        """

        # Get the read Account success data
        update_account_id = self.data["accounts"]["update_account_success"]["id"]

        # Create the payload
        payload = {
            # Generate a random Account Name
            "name": "Account-{}".format(random.randrange(10000, 99999))
        }

        # Make a request to update the Account with unique identifier (ID)
        # Update the Account Name with the newly generated Account Name
        update_account = self.entity.accounts.update(update_account_id, json.dumps(payload))

        # Test to ensure HTTP status code is 204 No Content
        self.assertEqual(update_account, 204)


    @classmethod
    def tearDownClass(cls):
        """Prepare test tear down class.

        Clean up Test Data.
        """

        # Get the read Account success unique identifier (ID)
        update_account_id = cls.data["accounts"]["update_account_success"]["id"]
        # Make a request to delete the Account
        update_account = cls.entity.accounts.delete(update_account_id)
        # Check if the delete was successful
        if update_account == 204:
            # Delete the Account entry from the Test Data
            if "update_account_success" in cls.data["accounts"]:
                del cls.data["accounts"]["update_account_success"]

        # Write the new Test Data to file
        with open(cls.test_data_file, "w") as f:
            json.dump(cls.data, f)


class TestAccountDelete(unittest.TestCase):
    """Test the Entity module with Delete Account."""
    
    @classmethod
    def setUpClass(cls):
        """Prepare test set up class.

        Get the data from JSON (JavaScript Object Notation) file and
        login.
        """

        # Get the current directory of the file
        current_directory = os.path.dirname(os.path.abspath(__file__))
        # Get the path of the Test Data file
        cls.test_data_file = os.path.join(current_directory, "TestData.json")

        # Open the file for reading
        with open(cls.test_data_file, "r") as f:
            cls.data = json.load(f)

        # Get the hostname from the Test Data
        cls.hostname = cls.data["organizations"]["name"]

        # Get the user data for success login
        user_rest_v1_success = cls.data["systemusers"]["user_rest_v1_success"]

        # Create an instance of Access object and login
        cls.access = Access(hostname=cls.hostname,
                            client_id=user_rest_v1_success["client_id"],
                            client_secret=user_rest_v1_success["client_secret"],
                            tenant_id=user_rest_v1_success["tenant_id"]).login()

        # Create an instance of Entity
        cls.entity = Entity(cls.access, cls.hostname)

        # Create the payload
        payload = {
            # Generate a random Account Name
            "name": "Account-{}".format(random.randrange(10000, 99999))
        }

        # Make a request to create the Account
        # Get the return unique identifier (ID)
        # The payload need to be serialized to JSON formatted str (json.dumps)
        account_id = cls.entity.accounts.create(json.dumps(payload))

        # Create the dictionary
        cls.data["accounts"]["delete_account_success"] = {}
        # Create or update the Account ID in the Test Data
        cls.data["accounts"]["delete_account_success"]["id"] = account_id

        # Write the new Test Data to file
        with open(cls.test_data_file, "w") as f:
            json.dump(cls.data, f)


    def test_account_delete_failure(self):
        """Test a failure for Account delete.

        Get the hostname and the unique identifier (ID) from the Test
        Data to make a request for delete. Should result in response
        with None.
        """

        # Get the delete Account failure unique identifier (ID)
        delete_account_id = self.data["accounts"]["delete_account_failure"]["id"]

        # Make a request to delete the Account with unique identifier (ID)
        # Delete the Account
        delete_account = self.entity.accounts.delete(delete_account_id)

        # Test to ensure delete Account information is None
        self.assertIsNone(delete_account)


    def test_account_delete_success(self):
        """Test a success for Account delete.

        Get the hostname and the unique identifier (ID) from the Test
        Data to make a request for delete. Should result in response
        with status code 204 No Content.
        """

        # Get the delete Account success unique identifier (ID)
        delete_account_id = self.data["accounts"]["delete_account_success"]["id"]

        # Make a request to delete the Account with unique identifier (ID)
        # Delete the Account
        delete_account = self.entity.accounts.delete(delete_account_id)

        # Delete the Account entry from the Test Data
        if "delete_account_success" in self.data["accounts"]:
            del self.data["accounts"]["delete_account_success"]

        # Write the new Test Data to file
        with open(self.test_data_file, "w") as f:
            json.dump(self.data, f)

        # Test to ensure HTTP status code is 204 No Content
        self.assertEqual(delete_account, 204)


    @classmethod
    def tearDownClass(cls):
        """Prepare test tear down class.

        Clean up Test Data.
        """
        pass


class TestAccountQuery(unittest.TestCase):
    """Test the Entity module with Query Account."""

    @classmethod
    def setUpClass(cls):
        """Prepare test set up class.

        Get the data from JSON (JavaScript Object Notation) file and
        login.
        """

        # Get the current directory of the file
        current_directory = os.path.dirname(os.path.abspath(__file__))
        # Get the path of the Test Data file
        cls.test_data_file = os.path.join(current_directory, "TestData.json")

        # Open the file for reading
        with open(cls.test_data_file, "r") as f:
            cls.data = json.load(f)

        # Get the hostname from the Test Data
        cls.hostname = cls.data["organizations"]["name"]

        # Get the user data for success login
        user_rest_v1_success = cls.data["systemusers"]["user_rest_v1_success"]

        # Create an instance of Access object and login
        cls.access = Access(hostname=cls.hostname,
                            client_id=user_rest_v1_success["client_id"],
                            client_secret=user_rest_v1_success["client_secret"],
                            tenant_id=user_rest_v1_success["tenant_id"]).login()

        # Create an instance of Entity
        cls.entity = Entity(cls.access, cls.hostname)


    def test_account_query_select_success(self):
        """Test a success for Account query `select`.

        Get the hostname from the Test Data to make a request for query.
        Test for the `select` system query option. Should result in
        response with status code 200 OK and a string formatted JSON for
        the result of the query.
        """

        # Define the query property
        query = {
            "select": "accountid,name"
        }

        # Make a request to query the Account
        query_account = self.entity.accounts.query(**query)

        # Test to ensure Account information is a string
        self.assertEqual(type(query_account), str)


    def test_account_query_top_success(self):
        """Test a success for Account query `top`.

        Get the hostname from the Test Data to make a request for query.
        Test for the `top` system query option. Should result in
        response with status code 200 OK and a string formatted JSON for
        the result of the query.
        """

        # Set the `top` system query option count
        top_count = 3
        
        # Define the query property
        query = {
            "top": top_count
        }

        # Make a request to query the Account
        query_account = self.entity.accounts.query(**query)

        # Count the number of result
        query_account_count = len(json.loads(query_account)["value"])

        # Test to ensure the `top` count is the same as the result count
        self.assertEqual(top_count, query_account_count)


def suite():
    """Test Suite"""

    # Create the Unit Test Suite
    suite = unittest.TestSuite()

    # Add the Unit Test
    suite.addTest(TestAccountCreate())
    suite.addTest(TestAccountRead())
    suite.addTest(TestAccountUpdate())
    suite.addTest(TestAccountDelete())
    suite.addTest(TestAccountQuery())

    # Return the Test Suite
    return suite


if __name__ == "__main__":
    runner = unittest.TextTestRunner()
    runner.run(suite())