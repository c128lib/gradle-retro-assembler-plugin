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
package com.c128lib.rbt.processors.spritepad.domain

import com.github.c128lib.rbt.shared.domain.OutOfDataException
import com.github.c128lib.rbt.shared.processor.BinaryProducer
import com.github.c128lib.rbt.shared.processor.Output

class SpriteProducer(val start: Int = 0, private val end: Int = 65536, output: Output<ByteArray>) :
    BinaryProducer(output) {

  private val scaledStart = scale(start)
  private val scaledEnd = scale(end)

  override fun write(data: ByteArray) =
      super.write(
          when {
            scaledStart >= data.size ->
                throw OutOfDataException("Not enough characters to support start=$start")
            scaledEnd < data.size -> data.copyOfRange(scaledStart, scaledEnd)
            else -> data.copyOfRange(scaledStart, data.size)
          })

  private fun scale(value: Int) = value * 64
}
