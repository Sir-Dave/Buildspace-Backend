package com.sirdave.buildspace

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BuildspaceApplication

fun main(args: Array<String>) {
	runApplication<BuildspaceApplication>(*args)
}
