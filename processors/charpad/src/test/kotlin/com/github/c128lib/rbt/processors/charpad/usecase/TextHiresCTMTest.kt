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
import com.github.c128lib.rbt.processors.charpad.domain.CharAttributesProducer
import com.github.c128lib.rbt.processors.charpad.domain.CharColoursProducer
import com.github.c128lib.rbt.processors.charpad.domain.CharMaterialsProducer
import com.github.c128lib.rbt.processors.charpad.domain.CharsetProducer
import com.github.c128lib.rbt.processors.charpad.domain.HeaderProducer
import com.github.c128lib.rbt.processors.charpad.domain.MapCoord
import com.github.c128lib.rbt.processors.charpad.domain.MapProducer
import com.github.c128lib.rbt.processors.charpad.domain.TileColoursProducer
import com.github.c128lib.rbt.processors.charpad.domain.TileProducer
import com.github.c128lib.rbt.shared.testutils.BinaryOutputMock
import com.github.c128lib.rbt.shared.testutils.InputByteStreamAdapter
import com.github.c128lib.rbt.shared.testutils.OutputMock
import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class TextHiresCTMTest :
    BehaviorSpec({
      isolationMode = IsolationMode.InstancePerTest
      val supportedVersions = listOf(5, 6, 7, 8, 82)

      supportedVersions.forEach { ctmVersion ->
        val input =
            InputByteStreamAdapter(
                this.javaClass.getResourceAsStream(
                    "/text-hires/text-hi-per-char-notiles-ctm$ctmVersion.ctm")!!)

        Given("[CTM v$ctmVersion] with per char colouring method and no tile set") {
          val charsetOutput = BinaryOutputMock()
          val charsetColourOutput = BinaryOutputMock()
          val charsetAttributesOutput = BinaryOutputMock()
          val charsetMaterialOutput = BinaryOutputMock()
          val tilesetOutput = BinaryOutputMock()
          val mapOutput = BinaryOutputMock()
          val headerOutput = OutputMock<CTMHeader>()

          val processor =
              ProcessCharpadUseCase(
                  listOf(
                      HeaderProducer(output = headerOutput),
                      CharsetProducer(output = charsetOutput),
                      CharColoursProducer(output = charsetColourOutput),
                      CharAttributesProducer(output = charsetAttributesOutput),
                      CharMaterialsProducer(output = charsetMaterialOutput),
                      TileProducer(output = tilesetOutput),
                      MapProducer(
                          leftTop = MapCoord(0, 0),
                          rightBottom = MapCoord(40, 25),
                          output = mapOutput)),
                  false)

          When("process is called") {
            processor.apply(input)

            Then("CTM version matches") { headerOutput.data?.version?.toInt() shouldBe ctmVersion }
            Then("charset content is read") {
              charsetOutput.bytes shouldBe
                  byteArrayOf(0x00, 0x7E, 0x42, 0x00, 0x00, 0x42, 0x7E, 0x00) +
                      byteArrayOf(0x00, 0x18, 0x18, 0x24, 0x24, 0x42, 0x42, 0x00) +
                      byteArrayOf(0x00, 0x7E, 0x42, 0x40, 0x40, 0x42, 0x7E, 0x00) +
                      byteArrayOf(0x00, 0x66, 0x42, 0x42, 0x42, 0x42, 0x66, 0x00)
            }
            Then("char colours are read") {
              charsetColourOutput.bytes shouldBe byteArrayOf(6, 3, 2, 1)
            }
            Then("char attributes are read") {
              charsetAttributesOutput.bytes shouldBe
                  byteArrayOf(0x06, 0x43, 0x82.toByte(), 0xC1.toByte())
            }
            Then("char materials are read") {
              charsetMaterialOutput.bytes shouldBe byteArrayOf(0x0, 0x4, 0x8, 0xC)
            }
            Then("no tiles are produced") { tilesetOutput.bytes.size shouldBe 0 }
            Then("map is read") {
              mapOutput.bytes.copyOfRange(0, 8) shouldBe
                  byteArrayOf(0x00, 0x00, 0x01, 0x00, 0x03, 0x00, 0x02, 0x00)
              mapOutput.bytes.copyOfRange(2000 - 6, 2000) shouldBe
                  byteArrayOf(0x03, 0x00, 0x02, 0x00, 0x01, 0x00)
              mapOutput.bytes.copyOfRange(8, 2000 - 6).filter { it != 0x00.toByte() }.size shouldBe
                  0
            }
          }
        }
      }

      supportedVersions.forEach { ctmVersion ->
        val input =
            InputByteStreamAdapter(
                this.javaClass.getResourceAsStream(
                    "/text-hires/text-hi-per-char-tiles-ctm$ctmVersion.ctm")!!)

        Given("[CTM v$ctmVersion] with per char colouring method and with tile set") {
          val charsetOutput = BinaryOutputMock()
          val charsetColourOutput = BinaryOutputMock()
          val charsetAttributesOutput = BinaryOutputMock()
          val charsetMaterialOutput = BinaryOutputMock()
          val tilesetOutput = BinaryOutputMock()
          val tileColoursOutput = BinaryOutputMock()
          val mapOutput = BinaryOutputMock()

          val processor =
              ProcessCharpadUseCase(
                  listOf(
                      CharsetProducer(output = charsetOutput),
                      CharColoursProducer(output = charsetColourOutput),
                      CharAttributesProducer(output = charsetAttributesOutput),
                      CharMaterialsProducer(output = charsetMaterialOutput),
                      TileProducer(output = tilesetOutput),
                      TileColoursProducer(output = tileColoursOutput),
                      MapProducer(
                          leftTop = MapCoord(0, 0),
                          rightBottom = MapCoord(40, 25),
                          output = mapOutput)),
                  false)

          When("process is called") {
            processor.apply(input)

            Then("charset content is read") {
              charsetOutput.bytes shouldBe
                  byteArrayOf(0x00, 0x7E, 0x42, 0x00, 0x00, 0x42, 0x7E, 0x00) +
                      byteArrayOf(0x00, 0x18, 0x18, 0x24, 0x24, 0x42, 0x42, 0x00) +
                      byteArrayOf(0x00, 0x7E, 0x42, 0x40, 0x40, 0x42, 0x7E, 0x00) +
                      byteArrayOf(0x00, 0x66, 0x42, 0x42, 0x42, 0x42, 0x66, 0x00)
            }
            Then("char colours are read") {
              charsetColourOutput.bytes shouldBe byteArrayOf(6, 3, 2, 1)
            }
            Then("char attributes are read") {
              charsetAttributesOutput.bytes shouldBe
                  byteArrayOf(0x06, 0x43, 0x82.toByte(), 0xC1.toByte())
            }
            Then("char materials are read") {
              charsetMaterialOutput.bytes shouldBe byteArrayOf(0x0, 0x4, 0x8, 0xC)
            }
            Then("tiles are read") {
              tilesetOutput.bytes shouldBe
                  byteArrayOf(0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00) +
                      byteArrayOf(0x03, 0x00, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00) +
                      byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)
            }
            Then("tile colours are empty") { tileColoursOutput.bytes.size shouldBe 0 }
            Then("map is read") {
              mapOutput.bytes.copyOfRange(0, 6) shouldBe
                  byteArrayOf(0x00, 0x00, 0x01, 0x00, 0x02, 0x00)
              mapOutput.bytes.copyOfRange(480 - 6, 480) shouldBe
                  byteArrayOf(0x02, 0x00, 0x01, 0x00, 0x00, 0x00)
            }
          }
        }
      }

      supportedVersions.forEach { ctmVersion ->
        val input =
            InputByteStreamAdapter(
                this.javaClass.getResourceAsStream(
                    "/text-hires/text-hi-per-tile-ctm$ctmVersion.ctm")!!)

        Given("[CTM v$ctmVersion] with per tile colouring method and with tile set") {
          val charsetOutput = BinaryOutputMock()
          val charsetColourOutput = BinaryOutputMock()
          val charsetAttributesOutput = BinaryOutputMock()
          val charsetMaterialOutput = BinaryOutputMock()
          val tilesetOutput = BinaryOutputMock()
          val tilesColoursOutput = BinaryOutputMock()
          val mapOutput = BinaryOutputMock()

          val processor =
              ProcessCharpadUseCase(
                  listOf(
                      CharsetProducer(output = charsetOutput),
                      CharColoursProducer(output = charsetColourOutput),
                      CharAttributesProducer(output = charsetAttributesOutput),
                      CharMaterialsProducer(output = charsetMaterialOutput),
                      TileProducer(output = tilesetOutput),
                      TileColoursProducer(output = tilesColoursOutput),
                      MapProducer(
                          leftTop = MapCoord(0, 0),
                          rightBottom = MapCoord(40, 25),
                          output = mapOutput)),
                  false)

          When("process is called") {
            processor.apply(input)

            Then("charset content is read") {
              charsetOutput.bytes shouldBe
                  byteArrayOf(0x00, 0x7E, 0x42, 0x00, 0x00, 0x42, 0x7E, 0x00) +
                      byteArrayOf(0x00, 0x18, 0x18, 0x24, 0x24, 0x42, 0x42, 0x00) +
                      byteArrayOf(0x00, 0x7E, 0x42, 0x40, 0x40, 0x42, 0x7E, 0x00) +
                      byteArrayOf(0x00, 0x66, 0x42, 0x42, 0x42, 0x42, 0x66, 0x00)
            }
            Then("char colours are empty") { charsetColourOutput.bytes.size shouldBe 0 }
            Then("char attributes are read") {
              charsetAttributesOutput.bytes shouldBe
                  byteArrayOf(0x00, 0x40, 0x80.toByte(), 0xC0.toByte())
            }
            Then("char materials are read") {
              charsetMaterialOutput.bytes shouldBe byteArrayOf(0x0, 0x4, 0x8, 0xC)
            }
            Then("tiles are read") {
              tilesetOutput.bytes shouldBe
                  byteArrayOf(0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x00, 0x00) +
                      byteArrayOf(0x03, 0x00, 0x02, 0x00, 0x00, 0x00, 0x00, 0x00) +
                      byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)
            }
            Then("tiles colours are read") {
              tilesColoursOutput.bytes shouldBe byteArrayOf(0x01, 0x02, 0x06)
            }
            Then("map is read") {
              mapOutput.bytes.copyOfRange(0, 6) shouldBe
                  byteArrayOf(0x00, 0x00, 0x01, 0x00, 0x02, 0x00)
              mapOutput.bytes.copyOfRange(480 - 6, 480) shouldBe
                  byteArrayOf(0x02, 0x00, 0x01, 0x00, 0x00, 0x00)
            }
          }
        }
      }
    })
