import com.sap.gateway.ip.core.customdev.util.Message
import com.sap.it.api.ITApiFactory
import com.sap.it.api.integration.FileService
import com.sap.it.api.integration.FileEntry

def Message processData(Message message) {
    // Get FileService API
    def fileService = ITApiFactory.getService(FileService.class)
    if (fileService == null) {
        throw new Exception("FileService is not available in CPI.")
    }

    // Define the base SFTP directory (change as needed)
    def baseDirectory = "/sftp-root-folder/"
    
    // List of subdirectories to fetch files from (can be dynamic)
    def directories = [ "inbound", "outbound", "archive" ] // Example directories

    def allFiles = []

    directories.each { dir ->
        def fullPath = baseDirectory + dir // Construct full directory path
        def fileEntries = fileService.listFiles(fullPath)

        if (fileEntries != null) {
            fileEntries.each { FileEntry file ->
                allFiles << "File: ${file.getName()} | Path: ${fullPath}" // Collect file details
            }
        }
    }

    // Convert list to string and set it as the message body
    message.setBody(allFiles.join("\n"))

    return message
}
