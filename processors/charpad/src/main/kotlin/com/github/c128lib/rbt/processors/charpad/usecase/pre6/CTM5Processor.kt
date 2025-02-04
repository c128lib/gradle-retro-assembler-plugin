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
package com.github.c128lib.rbt.processors.charpad.usecase.pre6

import com.github.c128lib.rbt.processors.charpad.domain.CTMHeader
import com.github.c128lib.rbt.processors.charpad.domain.ColouringMethod
import com.github.c128lib.rbt.processors.charpad.domain.Dimensions
import com.github.c128lib.rbt.processors.charpad.domain.ScreenMode
import com.github.c128lib.rbt.processors.charpad.domain.colouringMethodFrom
import com.github.c128lib.rbt.processors.charpad.usecase.CTMProcessor
import com.github.c128lib.rbt.processors.charpad.usecase.ProcessCharpadUseCase
import com.github.c128lib.rbt.shared.binutils.isolateHiNybbles
import com.github.c128lib.rbt.shared.binutils.isolateLoNybbles
import com.github.c128lib.rbt.shared.binutils.toUnsignedByte
import com.github.c128lib.rbt.shared.binutils.toWord
import com.github.c128lib.rbt.shared.processor.InputByteStream
import kotlin.experimental.and
import kotlin.experimental.or

internal class CTM5Processor(private val processCharpadUseCase: ProcessCharpadUseCase) :
    CTMProcessor {
  override fun process(inputByteStream: InputByteStream) {
    val rawHeader = readHeader(inputByteStream)
    val header = rawHeader.toHeader()

    processCharpadUseCase.processHeader { it.write(header) }

    if (rawHeader.numChars > 0) {
      val charData = inputByteStream.read(rawHeader.numChars * 8)
      processCharpadUseCase.processCharset { it.write(charData) }

      val charAttributeData = inputByteStream.read(rawHeader.numChars)
      processCharpadUseCase.processCharAttributes { it.write(charAttributeData) }
      if (header.colouringMethod == ColouringMethod.PerChar) {
        processCharpadUseCase.processCharColours { it.write(isolateLoNybbles(charAttributeData)) }
      }
      processCharpadUseCase.processCharMaterials { it.write(isolateHiNybbles(charAttributeData)) }
    }

    if (rawHeader.flags and CTM5Flags.TileSys.bit != 0.toByte()) {
      if (rawHeader.flags and CTM5Flags.CharEx.bit == 0.toByte()) {
        val tileData =
            inputByteStream.read(
                rawHeader.numTiles * rawHeader.tileWidth.toInt() * rawHeader.tileHeight.toInt() * 2)
        processCharpadUseCase.processTiles { it.write(tileData) }
      }

      if (header.colouringMethod == ColouringMethod.PerTile) {
        val tileColoursData = inputByteStream.read(rawHeader.numTiles)
        processCharpadUseCase.processTileColours { it.write(tileColoursData) }
      }
    }

    if (rawHeader.mapHeight > 0 && rawHeader.mapWidth > 0) {
      val mapData = inputByteStream.read(rawHeader.mapWidth * rawHeader.mapHeight * 2)
      processCharpadUseCase.processMap {
        it.write(rawHeader.mapWidth, rawHeader.mapHeight, mapData)
      }
    }
  }

  private fun readHeader(inputByteStream: InputByteStream): CTM5Header {
    val screenColor = inputByteStream.readByte()
    val multicolor1 = inputByteStream.readByte()
    val multicolor2 = inputByteStream.readByte()
    val charColor = inputByteStream.readByte()
    val colouringMethod = inputByteStream.readByte()
    val flags = inputByteStream.readByte()
    val numChars = inputByteStream.read(2).toWord().value + 1
    val numTiles = inputByteStream.read(2).toWord().value + 1
    val tileWidth = inputByteStream.readByte()
    val tileHeight = inputByteStream.readByte()
    val mapWidth = inputByteStream.read(2).toWord().value
    val mapHeight = inputByteStream.read(2).toWord().value
    return CTM5Header(
        screenColour = screenColor,
        multicolour1 = multicolor1,
        multicolour2 = multicolor2,
        charColour = charColor,
        colouringMethod = colouringMethod,
        flags = flags,
        numChars = numChars,
        numTiles = numTiles,
        tileWidth = tileWidth,
        tileHeight = tileHeight,
        mapWidth = mapWidth,
        mapHeight = mapHeight)
  }
}

internal enum class CTM5Flags(val bit: Byte) {
  TileSys(0x01),
  CharEx(0x02),
  MCM(0x04)
}

fun CTMHeader.toCTM5Flags(): Byte {
  var result = 0.toByte()
  if (screenMode == ScreenMode.TextMulticolor) {
    result = result or CTM5Flags.MCM.bit
  }
  if (useTiles) {
    result = result or CTM5Flags.TileSys.bit
  }
  return result
}

internal data class CTM5Header(
    val screenColour: Byte,
    val multicolour1: Byte,
    val multicolour2: Byte,
    val charColour: Byte,
    val colouringMethod: Byte,
    val flags: Byte,
    val numChars: Int,
    val numTiles: Int,
    val tileWidth: Byte,
    val tileHeight: Byte,
    val mapWidth: Int,
    val mapHeight: Int
) {
  fun toHeader(): CTMHeader =
      CTMHeader(
          version = 5,
          backgroundColour0 = screenColour,
          backgroundColour1 = multicolour1,
          backgroundColour2 = multicolour2,
          backgroundColour3 = 0,
          charColour = charColour,
          colouringMethod = colouringMethodFrom(colouringMethod),
          screenMode =
              if (flags and CTM5Flags.MCM.bit != 0.toUnsignedByte()) {
                ScreenMode.TextMulticolor
              } else {
                ScreenMode.TextHires
              },
          tileDimensions =
              if (flags and CTM5Flags.TileSys.bit != 0.toUnsignedByte()) {
                Dimensions(tileWidth, tileHeight)
              } else {
                null
              },
          mapDimensions = Dimensions(mapWidth, mapHeight))
}
