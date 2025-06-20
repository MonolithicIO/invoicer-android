package buildLogic.extensions

import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.gradle.api.Project

fun Project.getDetektExtension(): DetektExtension =
    extensions.findByType(DetektExtension::class.java)
        ?: throw IllegalStateException("Detekt extension not found")

fun Project.configureDetekt() {
    val extension = getDetektExtension()

    extension.apply {
        val configFileDir = "${this@configureDetekt.rootDir}/detekt/detekt-rules.yaml"

        config.setFrom(listOf(configFileDir))
        parallel = true
        this.source = files(
            "src/commonMain/kotlin",
            "src/commonTest/kotlin",
            "src/androidMain/kotlin",
            "src/androidTest/kotlin",
            "src/iosMain/kotlin",
            "src/iosTest/kotlin",
        )
    }
}