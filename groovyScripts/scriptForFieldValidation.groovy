import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

Message processData(Message message) {
    def body = message.getBody(String)
    def jsonParser = new JsonSlurper()
    def inputPayload = jsonParser.parseText(body)
    
    def errors = []

    // Validate "name" field
    if (!inputPayload.name || inputPayload.name.trim().isEmpty()) {
        errors << [field: "name", error: "Name is required."]
    }

    // Validate "age" field
    if (inputPayload.age == null || !(inputPayload.age instanceof Integer) || inputPayload.age < 0) {
        errors << [field: "age", error: "Age must be a non-negative integer."]
    }

    // Add more field validations as needed
    if (!inputPayload.email || !inputPayload.email.contains("@")) {
        errors << [field: "email", error: "Email must be a valid email address."]
    }

    // If errors exist, generate error response
    if (!errors.isEmpty()) {
        def errorResponse = [errors: errors]
        def errorResponseJson = JsonOutput.toJson(errorResponse)

        message.setProperty("http_status_code", 400) // Set HTTP status code
        message.setBody(errorResponseJson)
    } else {
        // Proceed with valid payload
        message.setBody(body)
    }

    return message
}
