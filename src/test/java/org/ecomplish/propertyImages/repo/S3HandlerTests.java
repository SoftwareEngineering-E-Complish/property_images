package org.ecomplish.propertyImages.repo;

import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.core.sync.RequestBody;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.ecomplish.propertyImages.util.ImageFileHandler;

import java.io.IOException;
import java.util.List;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class S3HandlerTests {

    @Mock
    private S3Client s3Client;

    @Mock
    private ImageFileHandler imageFileHandler;

    @Test
    public void testGetBuckets() {
        // Arrange
        ListBucketsResponse listBucketsResponse = ListBucketsResponse.builder()
                .buckets(Bucket.builder().name("testBucket").build())
                .build();
        Mockito.when(s3Client.listBuckets()).thenReturn(listBucketsResponse);
        S3Handler s3Handler = new S3Handler();
        s3Handler.setS3client(s3Client);

        // Act
        String result = s3Handler.getBuckets();

        // Assert
        //Mockito.verify(s3Client).listBuckets();
        assertEquals("Available buckets:\ntestBucket\n", result);
    }

    @Test
    public void testUploadImage() throws IOException {
        // Arrange
        Mockito.when(s3Client.putObject(Mockito.any(PutObjectRequest.class), Mockito.any(RequestBody.class)))
                .thenReturn(null);
        S3Handler s3Handler = new S3Handler();
        s3Handler.setS3client(s3Client);
        Mockito.when(imageFileHandler.getFileName()).thenReturn("123e4567-e89b-12d3-a456-426614174000.jpeg");
        Mockito.when(imageFileHandler.getBytes()).thenReturn(new byte[0]);
        // Act
        String result = s3Handler.uploadImage(imageFileHandler, "13");

        // Assert
        //Mockito.verify(s3Client).putObject(Mockito.any(PutObjectRequest.class), Mockito.any(RequestBody.class));
        String resultUrl = "https://test-image-storage.test-url/13/123e4567-e89b-12d3-a456-426614174000.jpeg";
        assertEquals(resultUrl, result);
    }

    @Test
    public void testUploadImageException() throws IOException {
        // Arrange
        S3Handler s3Handler = new S3Handler();
        s3Handler.setS3client(s3Client);
        Mockito.when(imageFileHandler.getBytes()).thenThrow(new IOException());

        // Act - Assert
        assertThrows(RuntimeException.class, () -> {
            String result = s3Handler.uploadImage(imageFileHandler, "13");
        });
    }

    @Test
    public void testUploadPrimaryImage() throws IOException {
        // Arrange
        Mockito.when(s3Client.putObject(Mockito.any(PutObjectRequest.class), Mockito.any(RequestBody.class)))
                .thenReturn(null);
        S3Handler s3Handler = new S3Handler();
        s3Handler.setS3client(s3Client);
        Mockito.when(imageFileHandler.getBytes()).thenReturn(new byte[0]);
        // Act
        String result = s3Handler.uploadPrimaryImage(imageFileHandler, "13");

        // Assert
        Mockito.verify(s3Client).putObject(Mockito.any(PutObjectRequest.class), Mockito.any(RequestBody.class));
        String resultUrl = "https://test-image-storage.test-url/primary/13.jpeg";
        assertEquals(resultUrl, result);
    }

    @Test
    public void testGetImageUrls() {
        // Arrange
        ListObjectsV2Response listObjectsV2Response = ListObjectsV2Response.builder().contents(
                List.of(
                    S3Object.builder().key("13/123e4567-e89b-12d3-a456-426614174000.jpeg").build(),
                    S3Object.builder().key("13/e87f719b-f1e2-497d-a2d1-33ae4840b0f4.jpeg").build()
                ))
                .build();
        Mockito.when(s3Client.listObjectsV2(Mockito.any(ListObjectsV2Request.class)))
                .thenReturn(listObjectsV2Response);
        S3Handler s3Handler = new S3Handler();
        s3Handler.setS3client(s3Client);
        // Act
        List<String> result = s3Handler.getImageUrls("13");
        List<String> expected = List.of("https://test-image-storage.test-url/13/123e4567-e89b-12d3-a456-426614174000.jpeg",
                "https://test-image-storage.test-url/13/e87f719b-f1e2-497d-a2d1-33ae4840b0f4.jpeg");
        // Assert
        Mockito.verify(s3Client).listObjectsV2(Mockito.any(ListObjectsV2Request.class));
        assertEquals(expected, result);
    }
}