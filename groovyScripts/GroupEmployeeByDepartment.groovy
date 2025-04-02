// SAP Integration Helper by OutOfTheBoxEA - https://www.youtube.com/@OutOfTheBoxEA

import groovy.xml.*
import java.util.stream.Collectors
import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    def body = message.getBody(String) as String
    def inputXml = new XmlSlurper().parseText(body)

    // Group employees by department
    def deptMap = [:].withDefault { [] }

    inputXml.Employee.each { emp ->
        def dept = emp.Department.text()
        def name = emp.Name.text()
        def id = emp.ID.text()
        deptMap[dept] << [Name: name, ID: id]
    }

    // Build output XML
    def writer = new StringWriter()
    def builder = new MarkupBuilder(writer)
    builder.Departments {
        deptMap.each { deptName, empList ->
            Department {
                Name(deptName)
                empList.each { emp ->
                    Employee {
                        Name(emp.Name)
                        ID(emp.ID)
                    }
                }
            }
        }
    }

    message.setBody(writer.toString())
    return message
}
