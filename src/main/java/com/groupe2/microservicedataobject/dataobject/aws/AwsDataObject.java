package com.groupe2.microservicedataobject.dataobject.aws;

import com.groupe2.microservicedataobject.dataobject.DataObjectAlreadyExists;
import com.groupe2.microservicedataobject.dataobject.DataObjectNotFoundException;
import com.groupe2.microservicedataobject.dataobject.IDataObject;
import com.groupe2.microservicedataobject.dataobject.PathContainsOtherObjectsException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.core.waiters.WaiterResponse;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.paginators.ListObjectsV2Iterable;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.waiters.S3Waiter;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class AwsDataObject implements IDataObject {
    private static final AwsCloudClient AWS_CLIENT = AwsCloudClient.getInstance();
    private final String path;
    private final String bucketName;

    public AwsDataObject(String path) {
        if(!path.contains("/")){
            throw new DataObjectNotFoundException();
        }
        this.bucketName = path.substring(0, path.indexOf("/"));
        this.path = path.substring(path.indexOf("/") + 1);
    }

    /**
     * Upload data to a bucket from base64 encoded string
     * @param dataBase64 the data to upload as base64 String
     */
    public void upload(String dataBase64) {
        byte[] bytes = java.util.Base64.getDecoder().decode(dataBase64);
        upload(bytes);
    }

    /**
     * Upload data to a bucket from byte array
     * @param dataBytes the data to upload as bytes
     */
    public void upload(byte[] dataBytes) {
        if (exists()){
            throw new DataObjectAlreadyExists();
        }

        if (!AwsBucketHelper.bucketExists(bucketName)) {
            AwsBucketHelper.createBucket(bucketName);
        }

        try (S3Client s3 = S3Client.builder().credentialsProvider(AWS_CLIENT.getCredentialsProvider()).region(AWS_CLIENT.getRegion()).build()) {

            InputStream fis = new ByteArrayInputStream(dataBytes);

            s3.putObject(PutObjectRequest.builder().bucket(bucketName).key(path)
                            .contentLength((long) dataBytes.length)
                            .build(),
                    RequestBody.fromInputStream(fis, dataBytes.length));

            S3Waiter waiter = s3.waiter();
            HeadObjectRequest requestWait = HeadObjectRequest.builder()
                    .bucket(bucketName).key(path).build();

            WaiterResponse<HeadObjectResponse> waiterResponse = waiter.waitUntilObjectExists(requestWait);
            waiterResponse.matched().response().ifPresent(System.out::println);
        }
    }

    /**
     * Downloads the current object into a byte array
     * @return byte array
     * @throws DataObjectNotFoundException if the file does not exist
     */
    public byte[] download() throws IOException {
        if (!AwsBucketHelper.bucketExists(bucketName) || !exists()) {
            throw new DataObjectNotFoundException();
        }

        try (S3Client s3 = S3Client.builder().credentialsProvider(AWS_CLIENT.getCredentialsProvider()).region(AWS_CLIENT.getRegion()).build()) {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(path)
                    .build();
            return s3.getObject(getObjectRequest).readAllBytes();
        }
    }

    public void delete(){
        delete(false);
    }

    /**
     * Deletes the current object
     * @throws DataObjectNotFoundException if the file does not exist
     */
    public void delete(boolean recursive) {
        if (!AwsBucketHelper.bucketExists(bucketName) || !exists()) {
            throw new DataObjectNotFoundException();
        }
        try (S3Client s3 = S3Client.builder().credentialsProvider(AWS_CLIENT.getCredentialsProvider()).region(AWS_CLIENT.getRegion()).build()) {
            if (!recursive) {
                ListObjectsRequest listObjects = ListObjectsRequest
                        .builder()
                        .bucket(bucketName)
                        .prefix(path)
                        .build();

                ListObjectsResponse res = s3.listObjects(listObjects);
                if (res.contents().size() > 1){
                    throw new PathContainsOtherObjectsException();
                }

                DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                        .bucket(bucketName)
                        .key(path)
                        .build();
                s3.deleteObject(deleteObjectRequest);
                return;
            }

            ListObjectsV2Request request = ListObjectsV2Request.builder().bucket(bucketName).prefix(path).build();
            ListObjectsV2Iterable list = s3.listObjectsV2Paginator(request);
            for (ListObjectsV2Response response : list) {
                List<S3Object> objects = response.contents();
                List<ObjectIdentifier> objectIdentifiers = objects.stream().map(o -> ObjectIdentifier.builder().key(o.key()).build()).collect(Collectors.toList());
                DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder().bucket(bucketName).delete(Delete.builder().objects(objectIdentifiers).build()).build();
                s3.deleteObjects(deleteObjectsRequest);
            }
        }

    }

    /**
     * Creates a URL for the file that lasts for 60 minutes.
     * @return the URL as a String.
     */
    public String getUrl() {
        final int EXPIRATION_TIME = 60;

        if (!AwsBucketHelper.bucketExists(bucketName) || !exists()) {
            throw new DataObjectNotFoundException();
        }

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(path)
                .build();

        GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(EXPIRATION_TIME))
                .getObjectRequest(getObjectRequest)
                .build();

        S3Presigner presigner = S3Presigner.builder()
                .credentialsProvider(AWS_CLIENT.getCredentialsProvider())
                .region(AWS_CLIENT.getRegion())
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(getObjectPresignRequest);
        return presignedGetObjectRequest.url().toString();
    }

    /**
     * Checks if the current object exists
     * @return true if the object exists, false otherwise
     */
    public boolean exists() {
        try (S3Client s3 = S3Client.builder().credentialsProvider(AWS_CLIENT.getCredentialsProvider()).region(AWS_CLIENT.getRegion()).build()) {
            if (!AwsBucketHelper.bucketExists(bucketName)){
                return false;
            }

            ListObjectsRequest listObjects = ListObjectsRequest
                    .builder()
                    .bucket(bucketName)
                    .prefix(path)
                    .build();

            ListObjectsResponse res = s3.listObjects(listObjects);
            return res.contents().size() != 0;

        } catch (NoSuchKeyException e) {
            return false;
        }
        // TODO créer une exception custom pour chaque type d'erreur qui peut survenir.
        //return true;
    }
}
