package nl.perfana.config.generator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PerfanaConfigGenerator

fun main(args: Array<String>) {
    runApplication<PerfanaConfigGenerator>(*args)
}
