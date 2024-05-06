# property_images
Microservice for handling s3 image hosting

## Endpoints
In this microservice, there are 4 endpoints:
* POST /upload
  * Accepts an image parsed in base64 format and uploads it to the s3 bucket
  * Requires a flag if the image is the primary image of the property or not
  * Requires the property id
  * Returns the url of the uploaded image
* GET /getImageUrls
  * Accepts a property id
  * Returns a list of urls of images associated with the property
* GET /getPrimaryImageUrl
  * Accepts a property id
  * Returns the url of the primary image associated with the property
* GET /listbuckets
  * Returns a list of all the buckets in the s3 bucket
  * Used as a health check of the service

## Properties - Environment Variables
In application.properties file are set the following properties regarding the AWS S3 bucket:
* aws.s3.bucketName
* aws.s3.url
* aws.s3.region
Additionally, the `AWS_ACCESS_KEY_ID` and `AWS_SECRET_ACCESS_KEY` are set as environment variables,
so the service can connect to the pre-configured s3 bucket.

## Testing
You can manually execute the unit tests by executing `gradle test`.
Tests are also automatically invoked for code commited or merged in the main branch.

[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=SoftwareEngineering-E-Complish_property_images&metric=coverage)](https://sonarcloud.io/summary/new_code?id=SoftwareEngineering-E-Complish_property_images)
