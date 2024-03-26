package org.ecomplish.propertyImages.repo;

import jakarta.annotation.PostConstruct;
import org.ecomplish.propertyImages.util.ImageFileHandler;
import org.ecomplish.propertyImages.util.UrlBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.core.sync.RequestBody;

import java.io.IOException;
import java.lang.StringBuilder;
import java.net.URI;
import java.util.List;

@Repository
public class S3Handler {
    private S3Client s3Client;

    @Value(value = "${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.url}")
    private String endpointUrl;

    @Value("${aws.s3.region}")
    private String region;

    @PostConstruct
    public void init() {
        System.out.println("URL: " + this.endpointUrl);
        System.out.println("bucketName: " + this.bucketName);
        System.out.println("region: " + this.region);

        URI url = URI.create("https://" + this.endpointUrl);

        this.s3Client = S3Client.builder()
                .region(Region.of(this.region))
                .endpointOverride(url)
                .build();
    }

    public String getBuckets() {
        ListBucketsResponse buckets = s3Client.listBuckets();
        StringBuilder bucketString = new StringBuilder("Available buckets:\n");
        buckets.buckets().forEach(bucket -> bucketString.append(bucket.name()).append("\n") );
        return bucketString.toString();
    }

    public String uploadImage(ImageFileHandler fileHandler, String propertyId) {
        String key = propertyId + "/" + fileHandler.getFileName();
        return this.uploadFile(fileHandler, key);
    }

    public String uploadPrimaryImage(ImageFileHandler fileHandler, String propertyId) {
        String key = "primary/" + propertyId + ".jpeg";
        return this.uploadFile(fileHandler, key);
    }

    private String uploadFile(ImageFileHandler fileHandler, String key) {
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        try {
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromBytes(fileHandler.getBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return UrlBuilder.buildUrl(key);
    }

    public List<String> getImageUrls(String propertyId) {
        ListObjectsV2Request listObjectsReqManual = ListObjectsV2Request.builder()
                .bucket(this.bucketName)
                .prefix(propertyId + "/")
                .build();

        ListObjectsV2Response listObjResponse = s3Client.listObjectsV2(listObjectsReqManual);
        return listObjResponse.contents().stream().map(obj -> UrlBuilder.buildUrl(obj.key())).toList();
    }
}
