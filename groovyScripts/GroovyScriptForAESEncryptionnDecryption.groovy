import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import java.util.Base64
import com.sap.gateway.ip.core.customdev.util.Message

def Message processData(Message message) {
    def secretKey = "1234567890123456" // Must be 16, 24, or 32 characters long
    def inputText = "Hello, SAP CPI!" // Example plaintext message

    // Encrypt data
    def encryptedText = encryptAES(inputText, secretKey)
    message.setProperty("EncryptedText", encryptedText)

    // Decrypt data
    def decryptedText = decryptAES(encryptedText, secretKey)
    message.setProperty("DecryptedText", decryptedText)

    // Set final message body (encrypted text)
    message.setBody(encryptedText)
    
    return message
}

// AES Encryption Function
def encryptAES(String data, String key) {
    def cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    def secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES")
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec)
    return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes("UTF-8")))
}

// AES Decryption Function
def decryptAES(String encryptedData, String key) {
    def cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    def secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES")
    cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)
    return new String(cipher.doFinal(Base64.getDecoder().decode(encryptedData)), "UTF-8")
}
