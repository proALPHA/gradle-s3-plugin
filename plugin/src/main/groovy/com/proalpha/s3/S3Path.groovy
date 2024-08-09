package com.proalpha.s3

import groovy.transform.CompileDynamic
import java.nio.file.Paths

/**
 * The S3Path class represents a path in an S3 bucket, providing utility methods to manipulate and query the path.
 */
@CompileDynamic
class S3Path {

    private final List<String> pathSegments;

    S3Path(List<String> pathSegments) {
        this.pathSegments = Collections.unmodifiableList(pathSegments)
    }

    S3Path(String s) {
        this(
            s.replaceAll(/[\\\/]+/, '/')
             .replaceFirst(/^\/+/, '')
             .replaceFirst(/^\.\//, '')
             .split('/')
             .findAll { segment -> segment }
             .asImmutable()
        )
    }

    S3Path(File file) {
        this(file.absolutePath)
    }

    S3Path join(S3Path other) {
        return new S3Path(this.pathSegments + other.pathSegments)
    }

    S3Path join(String other) {
        return this.join(new S3Path(other))
    }

    S3Path relativize(S3Path other) {
        return new S3Path(
            Paths.get(this.toString()).relativize(Paths.get(other.toString())).toString()
        )
    }

    S3Path relativize(String other) {
        return this.relativize(new S3Path(other))
    }

    S3Path relativize(File other) {
        return this.relativize(new S3Path(other))
    }

    @Override
    String toString() {
        return pathSegments.join('/')
    }

}
