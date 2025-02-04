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

import javax.inject.Inject
import org.gradle.api.Action
import org.gradle.api.model.ObjectFactory

const val PREPROCESSING_EXTENSION_DSL_NAME = "preprocess"

abstract class PreprocessingExtension
@Inject
constructor(private val objectFactory: ObjectFactory) {

  val charpadPipelines = ArrayList<CharpadPipelineExtension>()

  val spritepadPipelines = ArrayList<SpritepadPipelineExtension>()

  val goattrackerPipelines = ArrayList<GoattrackerPipelineExtension>()

  val imagePipelines = ArrayList<ImagePipelineExtension>()

  fun charpad(action: Action<CharpadPipelineExtension>) {
    val ex = objectFactory.newInstance(CharpadPipelineExtension::class.java)
    action.execute(ex)
    charpadPipelines.add(ex)
  }

  fun spritepad(action: Action<SpritepadPipelineExtension>) {
    val ex = objectFactory.newInstance(SpritepadPipelineExtension::class.java)
    action.execute(ex)
    spritepadPipelines.add(ex)
  }

  fun goattracker(action: Action<GoattrackerPipelineExtension>) {
    val ex = objectFactory.newInstance(GoattrackerPipelineExtension::class.java)
    action.execute(ex)
    goattrackerPipelines.add(ex)
  }

  fun image(action: Action<ImagePipelineExtension>) {
    val ex = objectFactory.newInstance(ImagePipelineExtension::class.java)
    action.execute(ex)
    imagePipelines.add(ex)
  }
}
