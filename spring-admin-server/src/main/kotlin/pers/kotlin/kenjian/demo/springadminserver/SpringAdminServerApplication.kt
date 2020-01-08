package pers.kotlin.kenjian.demo.springadminserver

import de.codecentric.boot.admin.server.config.EnableAdminServer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableAdminServer
@SpringBootApplication
class SpringAdminServerApplication

fun main(args: Array<String>) {
    runApplication<SpringAdminServerApplication>(*args)
}
