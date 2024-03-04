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
package com.github.c128lib.rbt.compilers.glassz80.adapters.out.gradle

import com.github.c128lib.rbt.compilers.glassz80.domain.GlassZ80Settings
import com.github.c128lib.rbt.compilers.glassz80.usecase.port.GlassZ80SpecPort
import java.io.File
import java.nio.file.Path
import org.gradle.api.Project

class GlassZ80SpecAdapter(private val project: Project, private val settings: GlassZ80Settings) :
    GlassZ80SpecPort {
  override fun assemble(
      libDirs: List<File>,
      defines: List<String>,
      resultFileName: String,
      source: File
  ) {
    project.javaexec {
      it.classpath = project.files(settings.pathToExecutable)
      val args =
          CommandLineBuilder(settings)
              .libDirs(libDirs.map { file -> file.toPath() })
              .defines(defines)
              .output(Path.of(prgFile(source)))
              .viceSymbols()
              .variable("on_exit", "jam")
              .variable("write_final_results_to_file", "true")
              .variable("change_character_set", "true")
              .variable("result_file_name", resultFileName)
              .source(source.toPath())
              .build()
      it.args = args

      printArgs(args)
    }
  }

  private fun printArgs(args: List<String>) {
    println(args.joinToString(" "))
  }
}
