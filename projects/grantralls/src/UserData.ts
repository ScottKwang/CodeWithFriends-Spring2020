import aws from "aws-sdk";
import { userData } from "./interfaces"; 

/**
 * @returns The promise used to upload user data to the database.
 */
function postUserData(dataToPost: userData): Promise<any> {

	const docClient = new aws.DynamoDB.DocumentClient({
		region: process.env.region,
		endpoint: process.env.endpoint
	});

	const params = {
		TableName: process.env.tableName,
		Item: {...dataToPost}
	};

	return docClient.put(params).promise()
		.then((message: any) => {
			return message;
		})
		.catch((err: any) => {
			console.log("there was an error: " + err);
		});
}

function editUserData(phoneNumber: string, option: string, value: string): Promise<any> {

	const docClient = new aws.DynamoDB.DocumentClient({
		region: process.env.region,
		endpoint: process.env.endpoint
	});

	const params = {
		TableName: process.env.tableName,
		Key: {
			"phoneNumber": phoneNumber
		},
		UpdateExpression: `set #o.#v = :x`,
		ExpressionAttributeValues: {
			":x": value,
		},
		ExpressionAttributeNames: {
			"#o": "options",
			"#v": option
		},
		ReturnValues: "UPDATED_NEW"
	};

	return docClient.update(params).promise()
		.then(returnedData => {
			console.log(returnedData);
			return returnedData;
		})
		.catch(err => {
			console.log(`This is the error: ${err}`);
		});


}

/**
 * @returns The promise used to get user data from the database.
 */
function getUserData(phoneNumber: string): Promise<any> {

	const docClient = new aws.DynamoDB.DocumentClient({
		region: process.env.region,
		endpoint: process.env.endpoint
	});

	const params = {
		TableName: process.env.tableName,
		Key: {
			"phoneNumber": phoneNumber
		}
	};


	return docClient.get(params).promise()
		.then(data => {
			if(data.Item == undefined) {
				throw "User not found in db";
			}

			return data.Item;
		});
}

/**
 * @returns The promise used to delete user data from the database.
 */
function deleteUserData(phoneNumber: string): Promise<any> {
	const docClient = new aws.DynamoDB.DocumentClient({
		region: process.env.region,
		endpoint: process.env.endpoint
	});

	const params = {
		TableName: process.env.tableName,
		Key: {
			"phoneNumber": phoneNumber
		}
	};

	return docClient.delete(params).promise()
		.then((data: any) => {
			return data;
		})
		.catch((err: any) => {
			console.error("There was an error deleting the users data");
		});
}

export { postUserData, getUserData, deleteUserData, editUserData };
