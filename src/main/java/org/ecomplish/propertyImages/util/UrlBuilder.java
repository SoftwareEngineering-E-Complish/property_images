package org.ecomplish.propertyImages.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class UrlBuilder {
    private static String bucketName;
    private static String url;

    @Value("${aws.s3.bucketName}")
    public void setBucketName(String bucketName) {
        UrlBuilder.bucketName = bucketName;
    }

    @Value("${aws.s3.url}")
    public void setUrl(String url) {
        UrlBuilder.url = url;
    }
    public static String buildUrl(String key) {
        return "https://" + bucketName + "." + url + "/" + key;
    }

    public static String buildPrimaryUrl(String propertyId) {
        return "https://" + bucketName + "." + url + "/primary/" + propertyId + ".jpeg";
    }
}
