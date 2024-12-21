import com.sap.gateway.ip.core.customdev.util.Message
import groovy.util.XmlParser
import groovy.xml.XmlUtil

Message processData(Message message) {
    // Parse the incoming XML payload
    def body = message.getBody(String)
    def xmlParser = new XmlParser()
    def payload = xmlParser.parseText(body)

    // Create a list to collect the result values
    def resultList = []

    // Process each E1MARAM node in the XML
    payload.E1MARAM.each { node ->
        // Extract the relevant fields
        def matkl = node.MATKL?.text()  // Material Group
        def matnr = node.MATNR?.text() // Material Number
        def wrkst = payload.WRKST?.text() // Assuming WRKST is at the root level

        // Initialize the result
        def result

        // Apply the conditions
        if (matkl in ['1', '2', '3', '4'] || 
            matnr in ['5', '6']) {
            result = "One-Time"
        } else if (wrkst == matnr) {
            result = "One-Time, 30, 60" 
        } else {
            result = "One-Time, 30, 60, 90" 
        }

        // Add the result to the list
        resultList << result
    }

    // Set the list of results as the message body
    message.setBody(resultList)

    return message
}
