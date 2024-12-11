import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonOutput

Message processData(Message message) {
    // Retrieve HTTP Status Code and Response Body
    def statusCode = message.getHeaders().get("CamelHttpResponseCode") as Integer
    def responseBody = message.getBody(String)
    
    // Initialize a response map
    def responseMap = [:]
    responseMap.statusCode = statusCode

    // Check for specific HTTP errors and log messages
    switch (statusCode) {
        case 200:
            responseMap.message = "Success"
            responseMap.details = responseBody
            break
        case 400:
            responseMap.message = "Bad Request: Check input payload or headers."
            responseMap.details = responseBody
            break
        case 403:
            responseMap.message = "Forbidden: Authentication or permissions issue."
            responseMap.details = responseBody
            break
        case 500:
            responseMap.message = "Internal Server Error: Server-side issue in Concur."
            responseMap.details = responseBody
            break
        case 503:
            responseMap.message = "Service Unavailable: Concur server might be down."
            responseMap.details = responseBody
            break
        default:
            responseMap.message = "Unexpected HTTP Error"
            responseMap.details = responseBody
    }
    
    // Log response for debugging
    message.setProperty("ResponseDetails", JsonOutput.toJson(responseMap))

    // If needed, set a custom response or re-throw errors for specific cases
    if (statusCode >= 400) {
        message.setBody(JsonOutput.toJson(responseMap)) // Return error details in response
        message.setProperty("http_status_code", statusCode) // Set HTTP status code
    } else {
        message.setBody(responseBody) // Return successful response body
    }

    return message
}
