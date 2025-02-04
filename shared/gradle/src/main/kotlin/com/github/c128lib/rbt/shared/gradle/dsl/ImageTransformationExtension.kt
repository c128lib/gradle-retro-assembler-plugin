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
import org.gradle.api.GradleException
import org.gradle.api.model.ObjectFactory

abstract class ImageTransformationExtension
@Inject
constructor(protected val objectFactory: ObjectFactory) {
  var spriteWriter: ImageWriterExtension? = null

  var bitmapWriter: ImageWriterExtension? = null

  var cut: ImageCutExtension? = null

  var split: ImageSplitExtension? = null

  var extend: ImageExtendExtension? = null

  var flip: ImageFlipExtension? = null

  var reduceResolution: ImageReduceResolutionExtension? = null

  fun sprite(action: Action<ImageWriterExtension>) {
    spriteWriter = execute(spriteWriter, action, ImageWriterExtension::class.java)
  }

  fun bitmap(action: Action<ImageWriterExtension>) {
    bitmapWriter = execute(bitmapWriter, action, ImageWriterExtension::class.java)
  }

  fun cut(action: Action<ImageCutExtension>) {
    cut = execute(cut, action, ImageCutExtension::class.java)
  }

  fun split(action: Action<ImageSplitExtension>) {
    split = execute(split, action, ImageSplitExtension::class.java)
  }

  fun extend(action: Action<ImageExtendExtension>) {
    extend = execute(extend, action, ImageExtendExtension::class.java)
  }

  fun flip(action: Action<ImageFlipExtension>) {
    flip = execute(flip, action, ImageFlipExtension::class.java)
  }

  fun reduceResolution(action: Action<ImageReduceResolutionExtension>) {
    reduceResolution = execute(reduceResolution, action, ImageReduceResolutionExtension::class.java)
  }

  private fun <T> execute(value: T?, action: Action<T>, clazz: Class<T>): T {
    if (value != null) {
      throw GradleException("This action can be called only once (${clazz.name}).")
    }
    val ex = requireNotNull(objectFactory.newInstance(clazz))
    action.execute(ex)
    return ex
  }
}
