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
import com.tickaroo.tikxml.annotation.Xml

@Xml
class Statistics {
    @Attribute
    var page = 0

    @Path("ratings/usersrated")
    @Attribute(name = "value")
    var usersrated: String? = null

    @Path("ratings/average")
    @Attribute(name = "value")
    var average: String? = null

    @Path("ratings/bayesaverage")
    @Attribute(name = "value")
    var bayesaverage: String? = null

    @Path("ratings/stddev")
    @Attribute(name = "value")
    var stddev: String? = null

    @Path("ratings/median")
    @Attribute(name = "value")
    var median: String? = null

    @Path("ratings/owned")
    @Attribute(name = "value")
    var owned = 0

    @Path("ratings/trading")
    @Attribute(name = "value")
    var trading = 0

    @Path("ratings/wanting")
    @Attribute(name = "value")
    var wanting = 0

    @Path("ratings/wishing")
    @Attribute(name = "value")
    var wishing = 0

    @Path("ratings/numcomments")
    @Attribute(name = "value")
    var numcomments = 0

    @Path("ratings/numweights")
    @Attribute(name = "value")
    var numweights = 0

    @Path("ratings/averageweight")
    @Attribute(name = "value")
    var averageweight = 0.0

    @Path("ratings/ranks")
    @Element
    var ranks: List<Rank>? = null
}
