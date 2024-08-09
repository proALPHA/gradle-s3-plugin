package com.proalpha.s3

import groovy.transform.CompileDynamic
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.TaskAction

/**
 * The S3Download class defines a custom Gradle task for downloading files or directories from an S3 bucket.
 */
@CompileDynamic
class S3Download extends DefaultTask {

    @Input
    String region

    @Input
    String bucket

    @Input
    String path

    @Optional
    @OutputFile
    final RegularFileProperty outputFile = project.objects.fileProperty()

    @Optional
    @OutputDirectory
    final DirectoryProperty outputDirectory = project.objects.directoryProperty()

    @TaskAction
    void execute() {
        if (outputFile.present) {
            new S3Api(region, bucket).downloadFile(new S3Path(path), outputFile.get().asFile)
        } else if (outputDirectory.present) {
            new S3Api(region, bucket).downloadDirectory(new S3Path(path), outputDirectory.get().asFile)
        } else {
            throw new IllegalArgumentException('Neither inputFile nor inputDirectory is specified.')
        }
    }

}
