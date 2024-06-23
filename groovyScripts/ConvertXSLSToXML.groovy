import groovy.xml.MarkupBuilder
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook

// Function to convert XLSX to XML
def convertXlsxToXml(InputStream inputStream) {
    def workbook = new XSSFWorkbook(inputStream)
    def sheet = workbook.getSheetAt(0)
    
    def writer = new StringWriter()
    def xml = new MarkupBuilder(writer)
    
    xml.records {
        sheet.each { Row row ->
            record {
                row.each { Cell cell ->
                    def cellValue = getCellValue(cell)
                    "column${cell.getColumnIndex()}"(cellValue)
                }
            }
        }
    }
    
    return writer.toString()
}

// Helper function to get the cell value as a string
def getCellValue(Cell cell) {
    switch (cell.getCellType()) {
        case Cell.CELL_TYPE_STRING:
            return cell.getStringCellValue()
        case Cell.CELL_TYPE_NUMERIC:
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue().toString()
            } else {
                return cell.getNumericCellValue().toString()
            }
        case Cell.CELL_TYPE_BOOLEAN:
            return cell.getBooleanCellValue().toString()
        case Cell.CELL_TYPE_FORMULA:
            return cell.getCellFormula()
        default:
            return ""
    }
}

// Get the XLSX file from the message body
def xlsxFile = message.getBody(InputStream)
def xmlOutput = convertXlsxToXml(xlsxFile)

// Set the XML output as the message body
message.setBody(xmlOutput)
return message
