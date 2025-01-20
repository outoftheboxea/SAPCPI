import com.sap.gateway.ip.core.customdev.util.Message
import groovy.json.JsonSlurper

Message processData(Message message) {
    // Get the body from the message as a string
    def body = message.getBody(String)
    
    // Parse the JSON payload
    def jsonSlurper = new JsonSlurper()
    def jsonObject = jsonSlurper.parseText(body)
    
    // Access the "Payload" value inside the "Data" object
    def payloadData = jsonObject.Data?.Payload

    // Log the extracted data for debugging (optional)
    messageLog = messageLogFactory.getMessageLog(message)
    if (messageLog != null) {
        messageLog.addAttachmentAsString("Extracted Payload", payloadData.toString(), "text/plain")
    }

    // Set the extracted payload as the message body
    message.setBody(payloadData)
    
    return message
}
