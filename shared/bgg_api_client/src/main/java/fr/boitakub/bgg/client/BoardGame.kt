/*
 * Copyright (c) 2022, Boitakub
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

import com.tickaroo.tikxml.annotation.Attribute
import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.Path
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml
import com.tickaroo.tikxml.converter.htmlescape.HtmlEscapeStringConverter

@Xml
class BoardGame : Publishable() {

    @JvmField
    @PropertyElement
    var thumbnail: String? = null

    @JvmField
    @PropertyElement
    var image: String? = null

    @JvmField
    @PropertyElement(converter = HtmlEscapeStringConverter::class)
    var description: String? = null

    @JvmField
    @Path("minplayers")
    @Attribute(name = "value")
    var minplayers = 0

    @JvmField
    @Path("maxplayers")
    @Attribute(name = "value")
    var maxplayers = 0

    @JvmField
    @Path("playingtime")
    @Attribute(name = "value")
    var playingtime = 0

    @JvmField
    @Path("minplaytime")
    @Attribute(name = "value")
    var minplaytime = 0

    @JvmField
    @Path("maxplaytime")
    @Attribute(name = "value")
    var maxplaytime = 0

    @JvmField
    @Path("minage")
    @Attribute(name = "value")
    var minage = 0

    @JvmField
    @Element(name = "poll")
    var polls: List<Poll>? = null

    @JvmField
    @Element(name = "link")
    var links: List<Link>? = null

    @JvmField
    @Element
    var statistics: Statistics = Statistics()

    @JvmField
    @Path("versions")
    @Element
    var versions: List<Version>? = null
}
