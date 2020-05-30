const axios = require('axios')

exports.handler = async function (event, context, callback) {
    const url = "https://www.goodreads.com/search/index.xml"
    let params = event.queryStringParameters
    const response = await axios.get(url, {params}).then((result)=>{
        return result.data
    })
    return {
        statusCode: 200,
        body: response
    }
}