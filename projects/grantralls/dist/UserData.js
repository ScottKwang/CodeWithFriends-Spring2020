"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.editUserData = exports.deleteUserData = exports.getUserData = exports.postUserData = void 0;
const aws_sdk_1 = __importDefault(require("aws-sdk"));
/**
 * @returns The promise used to upload user data to the database.
 */
function postUserData(dataToPost) {
    const docClient = new aws_sdk_1.default.DynamoDB.DocumentClient({
        region: process.env.region,
        endpoint: process.env.endpoint
    });
    const params = {
        TableName: process.env.tableName,
        Item: Object.assign({}, dataToPost)
    };
    return docClient.put(params).promise()
        .then((message) => {
        return message;
    })
        .catch((err) => {
        console.log("there was an error: " + err);
    });
}
exports.postUserData = postUserData;
function editUserData(phoneNumber, option, value) {
    const docClient = new aws_sdk_1.default.DynamoDB.DocumentClient({
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
exports.editUserData = editUserData;
/**
 * @returns The promise used to get user data from the database.
 */
function getUserData(phoneNumber) {
    const docClient = new aws_sdk_1.default.DynamoDB.DocumentClient({
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
        if (data.Item == undefined) {
            throw "User not found in db";
        }
        return data.Item;
    });
}
exports.getUserData = getUserData;
/**
 * @returns The promise used to delete user data from the database.
 */
function deleteUserData(phoneNumber) {
    const docClient = new aws_sdk_1.default.DynamoDB.DocumentClient({
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
        .then((data) => {
        return data;
    })
        .catch((err) => {
        console.error("There was an error deleting the users data");
    });
}
exports.deleteUserData = deleteUserData;
