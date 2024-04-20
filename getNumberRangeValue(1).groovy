import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.XmlUtil

//Code by OutOfTheBoxEA
//https://www.youtube.com/channel/UCct88_6add0aUiEOwODUZmA
//Method to extract number range value from header and map it to target structure
//www.OutOfTheBoxEA.com

//Method to process change
def Message processData(Message message) {
    // Accessing the header fields
    def headers = message.getHeaders()

    // Get the input XML payload
    def xmlPayload = message.getBody(java.lang.String) as String

    // Parse the XML payload
    def xml = new XmlSlurper().parseText(xmlPayload)

    // Find and update the element UniqueIdentifier
    xml.'**'.findAll { it.name() == 'UniqueIdentifier' }.each { it.replaceBody(headers.get('UniqueOrderNumber')) }

    // Convert the updated XML back to a string
    def updatedXmlPayload = XmlUtil.serialize(xml)

    // Set the updated XML payload back to the message
    message.setBody(updatedXmlPayload)

    return message
}
