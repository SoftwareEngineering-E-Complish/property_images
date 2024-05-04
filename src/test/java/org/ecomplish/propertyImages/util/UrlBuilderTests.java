package org.ecomplish.propertyImages.util;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UrlBuilderTests {
    @Test
    public void testBuildUrl() {
        // Arrange
        String test_key = "13/123e4567-e89b-12d3-a456-426614174000.key";
        // Act
        String result = UrlBuilder.buildUrl(test_key);
        // Assert
        assertEquals("https://test-image-storage.test-url/" + test_key, result);
    }
    @Test
    public void testBuildPrimaryUrl() {
        // Arrange
        String propertyId = "15";
        // Act
        String result = UrlBuilder.buildPrimaryUrl(propertyId);
        // Assert
        assertEquals("https://test-image-storage.test-url/primary/15.jpeg", result);
    }
}
