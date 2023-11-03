import com.sap.gateway.ip.core.customdev.util.Message;

//Code by OutOfTheBoxEA
//https://www.youtube.com/channel/UCct88_6add0aUiEOwODUZmA
//Method to generate a file path for a DropBox server

def Message processData(Message message) {
    //get the Properties from previous script
    def extension = message.getProperty("extension");
    def fileName = message.getProperty("attachmentName");
    //Generate Random text
    def randText = new Random().with {(1..9).collect {(('a'..'z')).join()[ nextInt((('a'..'z')).join().length())]}.join()}
    //concatenate the values for final path
    def filePath = '/OutOfTheBoxEADemo/' + fileName +'_'+ randText + extension;
    //set the final path name
    message.setProperty("dropboxFileName",filePath);
    return message;
}