/*
MIT License

Copyright (c) 2018-2023 c128lib: The ultimate library for Commodore 128 projects development
Copyright (c) 2018-2023 Maciej Małecki

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.github.c128lib.rbt.dependencies.adapters.`in`.gradle

import com.github.c128lib.rbt.dependencies.usecase.ResolveGitHubDependencyCommand
import com.github.c128lib.rbt.dependencies.usecase.ResolveGitHubDependencyUseCase
import com.github.c128lib.rbt.shared.gradle.GROUP_BUILD
import com.github.c128lib.rbt.shared.gradle.dsl.RetroAssemblerPluginExtension
import com.github.c128lib.rbt.shared.gradle.getBooleanProperty
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

open class DownloadDependencies : DefaultTask() {

  init {
    description = "Downloads and unzips library dependency"
    group = GROUP_BUILD
  }

  @Input lateinit var extension: RetroAssemblerPluginExtension

  @Internal lateinit var resolveGitHubDependencyUseCase: ResolveGitHubDependencyUseCase

  @TaskAction
  fun downloadAndUnzip() {
    extension.dependencies.forEach { dependency ->
      resolveGitHubDependencyUseCase.apply(
          ResolveGitHubDependencyCommand(
              dependencyName = dependency.name,
              dependencyVersion = dependency.version,
              force = project.getBooleanProperty("forceDownload") ?: false,
              workDir = extension.workDir))
    }
  }
}
