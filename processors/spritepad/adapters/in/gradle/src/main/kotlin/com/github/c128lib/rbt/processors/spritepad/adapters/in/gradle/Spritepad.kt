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
package com.github.c128lib.rbt.processors.spritepad.adapters.`in`.gradle

import com.c128lib.rbt.processors.spritepad.domain.SpriteProducer
import com.c128lib.rbt.processors.spritepad.usecase.ProcessSpritepadUseCase
import com.github.c128lib.rbt.shared.gradle.GROUP_BUILD
import com.github.c128lib.rbt.shared.gradle.dsl.PreprocessingExtension
import com.github.c128lib.rbt.shared.gradle.dsl.SpritepadPipelineExtension
import com.github.c128lib.rbt.shared.gradle.dsl.SpritesOutputsExtension
import com.github.c128lib.rbt.shared.processor.BinaryOutputBuffer
import com.github.c128lib.rbt.shared.processor.FisInput
import java.io.FileInputStream
import java.util.*
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

open class Spritepad : DefaultTask() {

  init {
    description = "Process Spritepad data files (SPD) into a bunch of binary output files."
    group = GROUP_BUILD
  }

  @Input lateinit var preprocessingExtension: PreprocessingExtension

  @TaskAction
  fun process() {
    preprocessingExtension.spritepadPipelines.forEach { pipeline ->
      val inputFile = pipeline.getInput().get()
      logger.debug("Found pipeline: input=${pipeline.getInput().get()}.")
      val fis = FileInputStream(inputFile)
      fis.use { pipeline.outputs.forEach { output -> processInput(fis, output, pipeline) } }
    }
  }

  private fun processInput(
      fis: FileInputStream,
      output: SpritesOutputsExtension,
      pipeline: SpritepadPipelineExtension
  ) {
    val buffers: MutableList<BinaryOutputBuffer> = LinkedList()
    val processor = ProcessSpritepadUseCase(producers(output, buffers, pipeline))
    processor.apply(FisInput(fis))
    buffers.forEach { it.flush() }
  }

  private fun producers(
      output: SpritesOutputsExtension,
      buffers: MutableList<BinaryOutputBuffer>,
      pipeline: SpritepadPipelineExtension
  ) =
      output.sprites.map { sprites ->
        SpriteProducer(
            start = sprites.start,
            end = sprites.end,
            output = sprites.resolveOutput(buffers, useBuildDir(pipeline)))
      }

  private fun useBuildDir(pipeline: SpritepadPipelineExtension): Boolean =
      pipeline.getUseBuildDir().getOrElse(true)
}
