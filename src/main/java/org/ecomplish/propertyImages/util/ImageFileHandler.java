package org.ecomplish.propertyImages.util;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.UUID;

public class ImageFileHandler {
    private MultipartFile tmpFile;
    private String filename;
    public ImageFileHandler(MultipartFile file){
        setFile(file);
    }
    public void setFile(MultipartFile file){
        this.tmpFile = file;
        UUID uuid = UUID.randomUUID();
        String fileType = file.getContentType().split("/")[1];
        this.filename = uuid.toString() + "." + fileType;
    }
    public byte[] getBytes() throws IOException {
        return tmpFile.getBytes();
    }
    public String getFileName() {
        return filename;
    }
}
