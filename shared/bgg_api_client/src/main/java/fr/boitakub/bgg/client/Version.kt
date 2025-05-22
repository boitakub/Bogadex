/*
 * Copyright (c) 2022-2025, Boitakub
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
package fr.boitakub.bgg.client

import fr.boitakub.bgg.client.BoardGame.FloatHolder
import fr.boitakub.bgg.client.BoardGame.IntHolder
import fr.boitakub.bgg.client.BoardGame.StringHolder
import kotlinx.serialization.Serializable
import nl.adaptivity.xmlutil.serialization.XmlElement
import nl.adaptivity.xmlutil.serialization.XmlSerialName

@Serializable
class Version {
    @XmlElement(false)
    @XmlSerialName
    var id = 0

    @XmlElement(false)
    @XmlSerialName
    var type: String? = null

    @XmlElement(true)
    @XmlSerialName
    var image: String = ""

    @XmlElement(true)
    @XmlSerialName
    var thumbnail: String = ""

    @XmlElement(true)
    @XmlSerialName
    var name: List<Name> = mutableListOf()

    @XmlElement(true)
    @XmlSerialName("yearpublished")
    var yearPublished: IntHolder = IntHolder(0)

    @XmlElement(true)
    @XmlSerialName("link", "", "")
    var link: List<Link>? = null

    @XmlElement(true)
    @XmlSerialName("productcode")
    var productCode: StringHolder = StringHolder("")

    @XmlElement(true)
    @XmlSerialName
    var width: FloatHolder = FloatHolder(0.0f)

    @XmlElement(true)
    @XmlSerialName
    var length: FloatHolder = FloatHolder(0.0f)

    @XmlElement(true)
    @XmlSerialName
    var depth: FloatHolder = FloatHolder(0.0f)

    @XmlElement(true)
    @XmlSerialName
    var weight: FloatHolder = FloatHolder(0.0f)
}
