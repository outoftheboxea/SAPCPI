import com.sap.gateway.ip.core.customdev.util.Message
import com.sap.it.api.ITApiFactory
import com.sap.it.api.datastore.DataStoreService

def Message processData(Message message) {
    // Define Data Store Name and Key
    def dataStoreName = "YourDataStoreName" // Change this to your Data Store name
    def key = "YourKey" // Change this to the key used in Data Store
    
    // Get Data Store Service
    def dataStoreService = ITApiFactory.getService(DataStoreService.class)
    
    if (dataStoreService == null) {
        throw new Exception("Data Store Service is not available")
    }
    
    // Retrieve the stored Message ID from the Data Store
    def storedMessageId = dataStoreService.get(dataStoreName, key)
    
    if (storedMessageId == null) {
        throw new Exception("No Message ID found in Data Store for key: " + key)
    }
    
    // Set the retrieved Message ID as a property
    message.setProperty("UpdatedMessageID", storedMessageId)
    
    return message
}
