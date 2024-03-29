package org.ecomplish.propertyImages.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ImageFileHandlerTests {
    @Mock
    private MultipartFile file;

    @Test
    public void setFileTest() throws IOException {
        Mockito.when(file.getContentType()).thenReturn("image/jpeg");
        byte[] bytes = new byte[1];
        bytes[0] = 127;
        Mockito.when(file.getBytes()).thenReturn(bytes);
        ImageFileHandler imageFileHandler = new ImageFileHandler(file);
        String filename = imageFileHandler.getFileName();
        String UUID = filename.split("\\.")[0];
        // Assert that the filename is a UUID followed by the file extension
        assertEquals(UUID.length(), 36);
        assertEquals(filename.split("\\.")[1], "jpeg");
        // Assert that the file bytes returned from Handler are the same as the bytes in the file
        assertEquals(bytes[0], imageFileHandler.getBytes()[0]);
    }
}
