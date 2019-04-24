package com.github.c64lib.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class KickAssemblerPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val extension = project.extensions.findByType(KickAssemblerPluginExtension::class.java)
        project.tasks.create("assemble", Assemble::class.java) { task ->
            task.kaJar = extension?.kaJar!!.absolutePath;
            task.libDir = extension.libDir.absolutePath;
        }
        project.tasks.create("clean", CleanKickFiles::class.java)
    }
}

