/**
 * SAP CPI - Retrieve PDF from Azure Blob Storage
 * Author: OutOfTheBoxEA
 * YouTube: https://www.youtube.com/@OutOfTheBoxEA
 */

import com.sap.gateway.ip.core.customdev.util.Message
import java.util.HashMap
import java.net.HttpURLConnection
import java.net.URL
import java.io.InputStream
import java.io.ByteArrayOutputStream

def Message processData(Message message) {
    def headers = message.getHeaders()
    def properties = message.getProperties()

    // Get Azure Blob URL from headers or properties (Adjust as needed)
    def azureBlobUrl = headers.get("BlobURL") ?: properties.get("BlobURL")

    if (!azureBlobUrl) {
        message.setBody("Error: Missing Blob URL")
        message.setHeader("Content-Type", "text/plain")
        return message
    }

    try {
        // Open connection to Azure Blob Storage
        URL url = new URL(azureBlobUrl)
        HttpURLConnection connection = (HttpURLConnection) url.openConnection()
        connection.setRequestMethod("GET")
        connection.setRequestProperty("x-ms-version", "2021-04-10")  // Adjust version as per Azure API
        connection.setRequestProperty("Content-Type", "application/pdf")
        connection.setDoInput(true)

        int responseCode = connection.getResponseCode()
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read the InputStream
            InputStream inputStream = connection.getInputStream()
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
            byte[] buffer = new byte[4096]
            int bytesRead
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead)
            }
            inputStream.close()

            // Set binary PDF data as response body
            message.setBody(outputStream.toByteArray())

            // Set Content-Type header for PDF
            message.setHeader("Content-Type", "application/pdf")
        } else {
            message.setBody("Error: Unable to fetch PDF, HTTP Response Code: " + responseCode)
            message.setHeader("Content-Type", "text/plain")
        }
        connection.disconnect()
    } catch (Exception e) {
        message.setBody("Exception: " + e.getMessage())
        message.setHeader("Content-Type", "text/plain")
    }

    return message
}
