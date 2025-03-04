/**
 * =====================================================================================
 *  SAP Integration Helper - ChatGPT by OutOfTheBoxEA
 *  Solution: Read XLSX file from Email, Convert to XML, and Send to SFTP
 *  Author: SAP Integration Helper (ChatGPT)
 *  YouTube: https://www.youtube.com/@OutOfTheBoxEA
 * =====================================================================================
 */

import com.sap.gateway.ip.core.customdev.util.Message
import org.apache.poi.xssf.usermodel.*
import java.io.InputStream
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.*
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import org.w3c.dom.Document
import org.w3c.dom.Element

Message processData(Message message) {
    // Read XLSX File from attachment
    def attachments = message.getAttachments()
    if (attachments.isEmpty()) {
        throw new Exception("No attachment found in the email!")
    }

    // Get the first attachment (Assuming it's an XLSX file)
    def attachmentEntry = attachments.entrySet().iterator().next()
    InputStream inputStream = attachmentEntry.value.getInputStream()

    // Parse XLSX File
    XSSFWorkbook workbook = new XSSFWorkbook(inputStream)
    XSSFSheet sheet = workbook.getSheetAt(0) // Read first sheet

    // Create XML Document
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance()
    Document doc = factory.newDocumentBuilder().newDocument()
    Element rootElement = doc.createElement("Workbook")
    doc.appendChild(rootElement)

    // Iterate over rows
    sheet.iterator().each { row ->
        Element rowElement = doc.createElement("Row")
        rootElement.appendChild(rowElement)

        // Iterate over cells
        row.cellIterator().each { cell ->
            Element cellElement = doc.createElement("Cell")
            cellElement.setTextContent(cell.toString()) // Convert cell value to String
            rowElement.appendChild(cellElement)
        }
    }

    // Convert XML Document to String
    TransformerFactory transformerFactory = TransformerFactory.newInstance()
    Transformer transformer = transformerFactory.newTransformer()
    transformer.setOutputProperty(OutputKeys.INDENT, "yes")
    
    StringWriter writer = new StringWriter()
    transformer.transform(new DOMSource(doc), new StreamResult(writer))
    
    // Set XML as message body
    message.setBody(writer.toString())

    return message
}
