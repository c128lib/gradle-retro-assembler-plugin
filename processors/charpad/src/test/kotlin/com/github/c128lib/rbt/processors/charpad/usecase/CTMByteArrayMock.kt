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
package com.github.c128lib.rbt.processors.charpad.usecase

import com.github.c128lib.rbt.processors.charpad.domain.CTMHeader
import com.github.c128lib.rbt.processors.charpad.domain.ColouringMethod
import com.github.c128lib.rbt.processors.charpad.domain.Dimensions
import com.github.c128lib.rbt.processors.charpad.domain.ScreenMode

internal class CTMByteArrayMock(
    version: Int,
    header: CTMHeader =
        CTMHeader(
            version.toByte(),
            0.toByte(),
            1.toByte(),
            2.toByte(),
            3.toByte(),
            3.toByte(),
            ColouringMethod.Global,
            ScreenMode.TextHires,
            Dimensions(2.toByte(), 2.toByte()),
            Dimensions(0, 0)),
    charset: ByteArray = ByteArray(0),
    charAttributes: ByteArray = ByteArray(0),
    tiles: ByteArray = ByteArray(0),
    tileColours: ByteArray = ByteArray(0),
    map: ByteArray = ByteArray(0)
) {
  val bytes =
      when (version) {
        5 ->
            CTM5ByteArrayMock(
                    header = header,
                    charset = charset,
                    charAttributes = charAttributes,
                    tiles = tiles,
                    tileColours = tileColours,
                    map = map)
                .bytes
        else -> throw IllegalArgumentException("Unhandled version: $version")
      }
}
