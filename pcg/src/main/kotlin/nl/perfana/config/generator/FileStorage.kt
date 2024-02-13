package nl.perfana.config.generator

import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.IOException
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream
import kotlin.io.path.listDirectoryEntries

@Component
class FileStorage(val filesPath: Path = Path.of("/tmp")) {

    fun File.bufferedOutputStream(size: Int = 8192) = BufferedOutputStream(this.outputStream(), size)
    fun File.zipOutputStream(size: Int = 8192) = ZipOutputStream(this.bufferedOutputStream(size))
    fun File.bufferedInputStream(size: Int = 8192) = BufferedInputStream(this.inputStream(), size)
    fun File.asZipEntry(directory: String) = ZipEntry("${directory}/${this.name}")

    fun cleanup(workDir:Path){
        println("Cleanup: ${workDir}")
        val unzipDirectory = workDir.resolve("unzip")
        deleteDirectory(unzipDirectory)
    }

    fun deleteDirectory(path: Path) {
        try {
            Files.walkFileTree(path, object : SimpleFileVisitor<Path>() {
                @Throws(IOException::class)
                override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                    Files.delete(file)
                    return FileVisitResult.CONTINUE
                }

                @Throws(IOException::class)
                override fun postVisitDirectory(dir: Path, exc: IOException?): FileVisitResult {
                    if (exc == null) {
                        Files.delete(dir)
                        return FileVisitResult.CONTINUE
                    } else {
                        throw exc
                    }
                }
            })
        } catch (e: Exception) {
            println("An error occurred while deleting directory: $e")
        }
    }

    /**
     * Creates a work directory for the project and copies the uploaded file there.
     * Default location is /tmp/<name>. Name is typically a time stamp.
     */
    fun createProjectDirectory(file: MultipartFile, name: String): Path {
        val workDir = filesPath.resolve(name)
        Files.createDirectories(workDir)
        val uploadedFile = workDir.resolve(file.originalFilename!!)
        Files.copy(file.inputStream, uploadedFile)
        return workDir
    }

    /**
     * Zip all files in the directory, excluding subdirs and return the zip file.
     * Puts files inside the zip in a directory with the name of the project.
     */
    fun zip(directoryToZip: Path, zipFilePath: Path, projectId: String): Path {
        val list = directoryToZip.listDirectoryEntries()
        val zipFile = zipFilePath.resolve("$projectId.zip").toFile()

        zipFile.zipOutputStream().use {
            list.map { p -> p.toFile() }
                .filter { f -> !f.isDirectory }
                .forEach { file ->
                    it.putNextEntry(file.asZipEntry(projectId))
                    file.bufferedInputStream().use { bis -> bis.copyTo(it) }
                }
        }
        deleteDirectory(directoryToZip)
        return zipFile.toPath()
    }

    fun fileAsResource(file: Path): Resource {
        return UrlResource(file.toUri())
    }

}