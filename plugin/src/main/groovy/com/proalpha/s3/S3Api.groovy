package com.proalpha.s3

import groovy.transform.CompileDynamic
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.model.GetObjectRequest
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.core.ResponseInputStream

/**
 * The S3Api class provides methods to upload files and directories to an S3 bucket.
 */
@CompileDynamic
class S3Api {

    private final S3Client s3Client
    private final String bucket

    S3Api(String region, String bucket) {
        this.s3Client = S3Client.builder()
            .region(Region.of(region))
            .build()
        this.bucket = bucket
    }

    void uploadFile(File source, S3Path destination) {
        s3Client.putObject(
            PutObjectRequest.builder()
                .bucket(this.bucket)
                .key(destination.toString())
                .build(),
            RequestBody.fromFile(source)
        )
        println "Uploaded: ${source.absolutePath} to ${destination}"
    }

    void uploadDirectory(File source, S3Path destination) {
        if (!source.directory) {
            throw new IllegalArgumentException("The provided file is not a directory: ${source}")
        }

        source.eachFileRecurse { file ->
            if (file.file) {
                this.uploadFile(file, destination.join((new S3Path(source)).relativize(file)))
            }
        }
    }

    void downloadFile(S3Path source, File destination) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
            .bucket(this.bucket)
            .key(source.toString())
            .build()
        ResponseInputStream responseInputStream = s3Client.getObject(getObjectRequest)
        Files.copy(responseInputStream, destination.toPath(), StandardCopyOption.REPLACE_EXISTING)
        responseInputStream.close()
        println "Downloaded: ${source} to ${destination.absolutePath}"
    }

    void downloadDirectory(S3Path source, File destination) {
        if (!destination.exists()) {
            destination.mkdirs()
        } else if (!destination.directory) {
            throw new IllegalArgumentException("The provided destination is not a directory: ${destination}")
        }

        ListObjectsV2Request listObjectsRequest = ListObjectsV2Request.builder()
            .bucket(this.bucket)
            .prefix("${source}/")
            .build()

        ListObjectsV2Response listObjectsResponse
        do {
            listObjectsResponse = this.s3Client.listObjectsV2(listObjectsRequest)
            listObjectsResponse.contents().each { s3Object ->
                S3Path sourceFile = new S3Path(s3Object.key())
                this.downloadFile(
                    sourceFile,
                    new File(destination, source.relativize(sourceFile).toString())
                )
            }
            listObjectsRequest = listObjectsRequest.toBuilder()
                .continuationToken(listObjectsResponse.nextContinuationToken())
                .build()
        } while (listObjectsResponse.truncated)
    }

}
