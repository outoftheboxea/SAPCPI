import com.sap.gateway.ip.core.customdev.util.Message
import groovy.xml.XmlUtil
import groovy.json.JsonOutput

Message processData(Message message) {
    // Get the input WSDL payload
    def body = message.getBody(String)

    // Parse the XML payload
    def xml = new XmlSlurper().parseText(body)

    // Remove all occurrences of wsdl:Policy
    xml.depthFirst().findAll { it.name() == 'Policy' && it.namespaceURI().endsWith('wsdl') }.each { node ->
        node.replaceNode {}
    }

    // Convert the remaining XML to JSON
    def jsonOutput = JsonOutput.toJson(xml)

    // Set the modified JSON as the new message body
    message.setBody(jsonOutput)

    return message
}
