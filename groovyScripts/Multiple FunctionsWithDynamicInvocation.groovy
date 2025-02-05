import com.sap.gateway.ip.core.customdev.util.Message

// Main script execution
def Message processData(Message message) {
    // Retrieve function name from a property or header
    def functionName = message.getProperty("FunctionName") ?: message.getHeader("FunctionName")
    
    if (functionName) {
        switch (functionName) {
            case "logIdoc":
                logIdoc(message)
                break
            case "logFilename":
                logFilename(message)
                break
            default:
                message.setProperty("Error", "Invalid function name: " + functionName)
        }
    } else {
        message.setProperty("Error", "FunctionName not provided")
    }
    
    return message
}

// Function to log IDoc details
def void logIdoc(Message message) {
    def idocPayload = message.getBody(String)
    message.setProperty("LogIdoc", "Logging IDoc: " + idocPayload)
}

// Function to log filename
def void logFilename(Message message) {
    def filename = message.getHeader("Filename")
    message.setProperty("LogFilename", "Processing file: " + (filename ?: "Unknown"))
}
