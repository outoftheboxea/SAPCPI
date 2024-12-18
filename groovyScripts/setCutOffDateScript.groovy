import com.sap.gateway.ip.core.customdev.util.Message
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

Message processData(Message message) {
    // Step 1: Retrieve the current cutoff date property
    def properties = message.getProperties()
    def cutoffDateStr = properties.get("CutoffDate") // Assuming the property is named "CutoffDate"

    // Step 2: Define date format
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd")
    
    // Step 3: Parse the cutoff date
    Date cutoffDate = cutoffDateStr ? dateFormat.parse(cutoffDateStr) : null

    // Step 4: Get the current date
    Date currentDate = new Date()

    // Step 5: Check if the current date is beyond the cutoff date
    if (cutoffDate == null || currentDate.after(cutoffDate)) {
        // If the cutoff date is null or expired, update it dynamically to a new date
        Calendar calendar = Calendar.getInstance()
        calendar.setTime(currentDate)
        calendar.add(Calendar.DAY_OF_MONTH, 30) // Example: Extend cutoff date by 30 days
        Date newCutoffDate = calendar.getTime()
        
        // Update the property
        String newCutoffDateStr = dateFormat.format(newCutoffDate)
        message.setProperty("CutoffDate", newCutoffDateStr)

        // Log the update for debugging
        def log = messageLogFactory.getMessageLog(message)
        if (log != null) {
            log.addAttachmentAsString("Updated Cutoff Date", "Old Date: $cutoffDateStr, New Date: $newCutoffDateStr", "text/plain")
        }
    }

    return message
}
