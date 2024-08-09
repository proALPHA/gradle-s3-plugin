# Gradle S3 Plugin

This plugin adds the two tasks `S3Download` and `S3Upload` which can be used to download/upload files/folders from/to s3.

## Usage

You can use the `S3Download` task to download a file:

```groovy
task download(type: com.proalpha.s3.S3Download) {
    region     = '<region>'
    bucket     = '<bucket>'
    path       = '<s3PathToTheFile>'
    outputFile = projectDir.toPath().resolve('output.txt').toFile()
}
```

Or you can use the `S3Download` task to download a folder:

```groovy
task download(type: com.proalpha.s3.S3Download) {
    region          = '<region>'
    bucket          = '<bucket>'
    path            = '<s3PathToTheFolder>'
    outputDirectory = projectDir.toPath().resolve('output').toFile()
}
```

Or you can use the `S3Upload` task to upload a file:

```groovy
task upload(type: com.proalpha.s3.S3Upload) {
    region    = '<region>'
    bucket    = '<bucket>'
    path      = '<s3PathToTheFile>'
    inputFile = projectDir.toPath().resolve('input.txt').toFile()
}
```

Or you can use the `S3Upload` task to upload a folder:

```groovy
task upload(type: com.proalpha.s3.S3Upload) {
    region         = '<region>'
    bucket         = '<bucket>'
    path           = '<s3PathToTheFolder>'
    inputDirectory = projectDir.toPath().resolve('input').toFile()
}
```

## License

[![Apache License 2.0](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](LICENSE)
