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
package com.github.c128lib.rbt.shared.gradle.dsl

import com.github.c128lib.rbt.shared.domain.AutostartPrgMode
import com.github.c128lib.rbt.shared.domain.Dependency
import com.github.c128lib.rbt.shared.domain.DependencyType
import com.github.c128lib.rbt.shared.domain.DependencyVersion
import com.github.c128lib.rbt.shared.domain.OutputFormat
import com.github.c128lib.retroassembler.domain.AssemblerType

const val EXTENSION_DSL_NAME = "retroProject"

const val DIALECT_VERSION_LATEST = "latest"

open class RetroAssemblerPluginExtension {

  var workDir = ".ra"
  var dialect = AssemblerType.None
  var dialectVersion = DIALECT_VERSION_LATEST
  var libDirs: Array<String> = emptyArray()
  var srcDirs = arrayOf(".")
  var includes: Array<String> = arrayOf("**/*.asm")
  var excludes: Array<String> = arrayOf("$workDir/**/*.asm")
  var defines: Array<String> = emptyArray()
  var verbose = false

  var viceExecutable = "x128"
  var viceHeadless = true
  var viceAutostartPrgMode = AutostartPrgMode.VIRTUAL_FS
  var specDirs = arrayOf("spec")
  var specIncludes: Array<String> = arrayOf("**/*.spec.asm")
  var values: Map<String, String> = mapOf()
  var outputFormat = OutputFormat.PRG

  val dependencies: List<Dependency>
    get() = _dependencies

  private var _dependencies: MutableList<Dependency> = ArrayList()

  fun libFromGitHub(name: String, version: String) {
    _dependencies.add(
        Dependency(type = DependencyType.GitHub, name = name, version = DependencyVersion(version)))
  }
}
