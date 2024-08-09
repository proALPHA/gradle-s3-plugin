package com.proalpha.s3

import groovy.transform.CompileDynamic
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

/**
 * The S3Upload class defines a custom Gradle task for uploading files or directories to an S3 bucket.
 */
@CompileDynamic
class S3Upload extends DefaultTask {

    @Input
    String region

    @Input
    String bucket

    @Input
    String path

    @Optional
    @InputFile
    final RegularFileProperty inputFile = project.objects.fileProperty()

    @Optional
    @InputDirectory
    final DirectoryProperty inputDirectory = project.objects.directoryProperty()

    @TaskAction
    void execute() {
        if (inputFile.present) {
            new S3Api(region, bucket).uploadFile(inputFile.get().asFile, new S3Path(path))
        } else if (inputDirectory.present) {
            new S3Api(region, bucket).uploadDirectory(inputDirectory.get().asFile, new S3Path(path))
        } else {
            throw new IllegalArgumentException('Neither inputFile nor inputDirectory is specified.')
        }
    }

}
