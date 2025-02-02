// OutOfTheBoxEA - Extract XSD from XML payload in SAP CPI
// This script reads an XML payload, identifies the embedded XSD, and extracts it

import com.sap.gateway.ip.core.customdev.util.Message
import javax.xml.parsers.DocumentBuilderFactory
import groovy.xml.XmlUtil

def Message processData(Message message) {
    try {
        // Retrieve XML payload from the message body
        def xmlPayload = message.getBody(String)
        
        // Parse the XML payload
        def factory = DocumentBuilderFactory.newInstance()
        factory.setNamespaceAware(true)
        def builder = factory.newDocumentBuilder()
        def inputStream = new ByteArrayInputStream(xmlPayload.bytes)
        def document = builder.parse(inputStream)
        
        // Get all elements with 'xs:schema' or 'xsd:schema' (handling namespace variations)
        def schemaNodes = document.getElementsByTagNameNS("http://www.w3.org/2001/XMLSchema", "schema")
        
        if (schemaNodes.length == 0) {
            schemaNodes = document.getElementsByTagName("xs:schema") // Alternate tag lookup
        }
        
        if (schemaNodes.length == 0) {
            schemaNodes = document.getElementsByTagName("xsd:schema") // Another possible variant
        }
        
        if (schemaNodes.length > 0) {
            // Convert first found schema node to a string format
            def schemaNode = schemaNodes.item(0)
            def transformer = javax.xml.transform.TransformerFactory.newInstance().newTransformer()
            def writer = new StringWriter()
            def source = new javax.xml.transform.dom.DOMSource(schemaNode)
            def result = new javax.xml.transform.stream.StreamResult(writer)
            transformer.transform(source, result)
            
            // Set extracted XSD as the new message body
            message.setBody(writer.toString())
        } else {
            // No schema found, setting a default response
            message.setBody("<error>No embedded XSD schema found in the XML payload</error>")
        }
        
    } catch (Exception e) {
        message.setBody("<error>Exception: ${e.message}</error>")
    }

    return message
}
