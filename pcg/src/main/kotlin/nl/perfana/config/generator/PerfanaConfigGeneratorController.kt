package nl.perfana.config.generator

import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.InputStreamReader
import java.nio.file.Path
import java.time.format.DateTimeFormatter
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import java.util.stream.Collectors


@RestController
class PerfanaConfigGeneratorController(val storage: FileStorage) {

    companion object {
        val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd-HHmmss")
    }

    val zipFiles: ConcurrentHashMap<String, Path> = ConcurrentHashMap()

    @PostMapping("/upload")
    fun upload(@RequestParam file: MultipartFile): FileUploadResponse {
        val projectId = "perfana-starter-${DATE_TIME_FORMATTER.format(java.time.LocalDateTime.now())}"
        val workDir = storage.createProjectDirectory(file, projectId)

        // this should be done in a co routine
        file.originalFilename?.let { generateAndZipAsync(workDir, it, projectId) }

        // now return zip download url
        return FileUploadResponse(projectId, "/download/${projectId}")
    }

    private fun generateAndZipAsync(workDir: Path, zipFileName: String, projectId: String) {

        val generatedFilesDir = "generated-files"

        // now call generator, source bashrc to use sdkman and installed kotlin/kscript
        executeCommand(
            workDir,
            "source /root/.bashrc && /files/generate-config.sh $zipFileName $generatedFilesDir"
        )

        // now zip generated files
        val zipFile = storage.zip(workDir.resolve(generatedFilesDir), workDir, projectId)
        // keep in mapping for future download
        zipFiles[projectId] = zipFile
    }

    private fun executeCommand(workDir: Path, command: String) {
        val process = ProcessBuilder("/bin/bash", "-c", command)
            .directory(workDir.toFile())
            .redirectErrorStream(true)
            .start()

        try {
            val hasRunToExit = process.waitFor(5, TimeUnit.MINUTES)

            if (!hasRunToExit) throw PerfanaConfigGeneratorException("Timeout for process '$command'")

            val exitValue = process.exitValue()

            if (exitValue != 0) {
                throw PerfanaConfigGeneratorException("Process '$command' failed with exit code $exitValue")
            }
        } finally {
            val output = extractOutput(process)
            print(output)
            process.destroy()
        }
    }

    private fun extractOutput(process: Process): String {
        val reader = BufferedReader(InputStreamReader(process.inputStream))
        val output = reader.lines().collect(Collectors.joining(System.getProperty("line.separator")))
        return output
    }

    @GetMapping("/download/{projectId:.+}")
    fun download(@PathVariable projectId: String, request: HttpServletRequest): ResponseEntity<Resource> {

        val zipFile = zipFiles[projectId] ?: throw RuntimeException("Zip file for $projectId unknown or not ready.")

        val resource = storage.fileAsResource(zipFile)

        return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=$projectId.zip")
            .body(resource)
    }
    /*

     */
}
