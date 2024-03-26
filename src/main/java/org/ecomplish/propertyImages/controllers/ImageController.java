package org.ecomplish.propertyImages.controllers;

import org.ecomplish.propertyImages.util.UrlBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.ecomplish.propertyImages.repo.S3Handler;
import org.ecomplish.propertyImages.util.ImageFileHandler;

import java.util.List;

@RestController
public class ImageController {
    @Autowired
    private final S3Handler s3Handler;

    public ImageController(S3Handler s3Handler) {
        this.s3Handler = s3Handler;
    }
    @GetMapping("/listBuckets")
    public String listBuckets() {
        return s3Handler.getBuckets();
    }
    @GetMapping("/getPrimaryImageUrl")
    public String getPrimaryImageUrl(@RequestParam("propertyId") String propertyId) {
        return UrlBuilder.buildPrimaryUrl(propertyId);
    }
    @GetMapping("/getImageUrls")
    public List<String> getImageUrls(@RequestParam("propertyId") String propertyId) {
        return s3Handler.getImageUrls(propertyId);
    }
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file,
                             @RequestParam("propertyId") String propertyId,
                            @RequestParam("primary") Boolean primary) {
        ImageFileHandler fileHandler = new ImageFileHandler(file);
        String url = s3Handler.uploadImage(fileHandler, propertyId);
        if (primary) {
            url = s3Handler.uploadPrimaryImage(fileHandler, propertyId);
        }
        return url;
    }
}