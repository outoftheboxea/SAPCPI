import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper
import groovy.json.JsonOutput

def Message processData(Message message) {
    // Step 1: Read input headers or properties for value mapping details
    def headers = message.getHeaders()
    def properties = message.getProperties()

    // Example input parameters (adjust as needed)
    def sourceAgency = properties.get("sourceAgency") ?: "MySourceAgency"
    def sourceValue = properties.get("sourceValue") ?: "MySourceValue"
    def targetAgency = properties.get("targetAgency") ?: "MyTargetAgency"
    def targetSchema = properties.get("targetSchema") ?: "MyTargetSchema"
    
    // Step 2: Set the API URL
    def baseUrl = "https://<your-cpi-tenant-url>/cpi/api/v1/ValueMappingService" // Update with your CPI tenant URL
    def url = "${baseUrl}/ValueMappings"

    // Step 3: Set up the HTTP client
    def connection = new URL(url).openConnection() as HttpURLConnection
    connection.setRequestMethod("GET")
    connection.setRequestProperty("Authorization", "Bearer " + properties.get("authToken")) // Pass OAuth token or Basic Auth token
    connection.setRequestProperty("Accept", "application/json")

    // Step 4: Handle response
    def responseCode = connection.responseCode
    if (responseCode == 200) {
        // Read the response
        def responseBody = connection.inputStream.text
        def jsonResponse = new JsonSlurper().parseText(responseBody)
        
        // Find the mapping
        def valueMapping = jsonResponse.find {
            it.sourceAgency == sourceAgency &&
            it.sourceValue == sourceValue &&
            it.targetAgency == targetAgency &&
            it.targetSchema == targetSchema
        }
        
        if (valueMapping) {
            // Add the found mapping to message properties or body
            message.setProperty("valueMappingResult", JsonOutput.toJson(valueMapping))
            message.setBody("Mapping Found: " + JsonOutput.toJson(valueMapping))
        } else {
            message.setBody("No mapping found for the given keys")
        }
    } else {
        // Handle error
        def errorResponse = connection.errorStream?.text ?: "No error details"
        message.setBody("Error: ${responseCode}, Details: ${errorResponse}")
    }

    return message
}
