import com.sap.gateway.ip.core.customdev.util.Message
import java.util.Map
import java.util.Iterator
import javax.activation.DataHandler

//Code by OutOfTheBoxEA
//https://www.youtube.com/channel/UCct88_6add0aUiEOwODUZmA
//Method to extract attachments from a message in SAP CPI


//Method to process chage
def Message processData(Message message) {

    //extract attachements
  Map < String, DataHandler > attachments = message.getAttachments()

//check if the email has any attachments
 if(attachments.isEmpty()){
    //no attachments
     message.setProperty("hasAttachmentsFlag", false);
 }else{
    //has some attachements
     message.setProperty("hasAttachmentsFlag", true);
     //loop through all attachments using lamda function
       attachments.values().each {
    attachment ->
    //get the attachment file extension and attachment name and set the properties
    if (attachment.getContentType().contains("pdf")) {
        message.setBody(attachment.getContent());
        message.setProperty("extension", ".pdf");
        message.setProperty("attachmentName", attachment.getName());
    }
    else if (attachment.getContentType().contains("xml")) {
      message.setBody(attachment.getContent());
      message.setProperty("extension", ".xml");
      message.setProperty("attachmentName", attachment.getName());
    } else if (attachment.getContentType().contains("zip")) {
      message.setBody(attachment.getContent());
      message.setProperty("extension", ".zip");
      message.setProperty("attachmentName", attachment.getName());
    }
  }
 }    

return message
}
