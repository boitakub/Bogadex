/*
 * Copyright (c) 2023-2025, Boitakub
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of mosquitto nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package fr.boitakub.bogadex.tests

import androidx.test.platform.app.InstrumentationRegistry
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.RecordedRequest
import java.io.InputStreamReader

class MockedWebResponseDispatcher : Dispatcher() {
    private fun readStringFromFile(fileName: String): String {
        val inputStream = InstrumentationRegistry.getInstrumentation().context.assets.open("data/$fileName")
        val builder = StringBuilder()
        val reader = InputStreamReader(inputStream, "UTF-8")
        reader.readLines().forEach {
            builder.append(it).append("\n")
        }
        val content = builder.toString()
        require(content.isNotBlank()) { "Fichier $fileName vide ou non trouvé !" }
        return content
    }

    override fun dispatch(request: RecordedRequest): MockResponse {
        println("Received request: ${request.path}")
        return when {
            request.path!!.contains("/xmlapi2/collection") -> {
                MockResponse()
                    .setResponseCode(200)
                    .setHeader("Content-Type", "application/xml; charset=utf-8")
                    .setBody(readStringFromFile("Cubenbois.xml"))
                    .setHeader("Content-Length", readStringFromFile("Cubenbois.xml").toByteArray().size.toString())
            }
            request.path!!.contains("/xmlapi2/thing") -> {
                MockResponse()
                    .setResponseCode(200)
                    .setHeader("Content-Type", "application/xml; charset=utf-8")
                    .setBody(readStringFromFile("86246.xml"))
                    .setHeader("Content-Length", readStringFromFile("86246.xml").toByteArray().size.toString())
            }
            else -> {
                MockResponse()
                    .setResponseCode(200)
                    .setHeader("Content-Type", "text/plain")
                    .setBody("Mock default response")
            }
        }
    }
}
