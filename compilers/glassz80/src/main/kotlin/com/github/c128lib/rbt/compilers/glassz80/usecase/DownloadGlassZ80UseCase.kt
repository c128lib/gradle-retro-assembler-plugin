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
package com.github.c128lib.rbt.compilers.glassz80.usecase

import com.github.c128lib.rbt.compilers.glassz80.usecase.port.DownloadGlassZ80Port
import com.github.c128lib.rbt.compilers.glassz80.usecase.port.ReadVersionPort
import com.github.c128lib.rbt.compilers.glassz80.usecase.port.SaveVersionPort
import com.github.c128lib.rbt.shared.domain.SemVer
import java.net.URL

class DownloadGlassZ80UseCase(
    private val downloadGlassZ80Port: DownloadGlassZ80Port,
    private val readVersionPort: ReadVersionPort,
    private val saveVersionPort: SaveVersionPort
) {

  fun apply(command: DownloadGlassZ80Command) {
    val url =
        URL("https://github.com/c128lib/asm-ka/releases/download/${command.version}/glassz80.jar")
    val target = "${command.workDir}/asms/ka/${command.version}/glassz80.jar"
    val versionFile = "${command.workDir}/asms/ka/version"
    if (command.force || command.version != readVersionPort.readVersion(versionFile)) {
      downloadGlassZ80Port.download(url, target)
      saveVersionPort.saveVersion(versionFile, command.version)
    }
  }
}

data class DownloadGlassZ80Command(
    val version: SemVer,
    val workDir: String,
    val force: Boolean = false
)
