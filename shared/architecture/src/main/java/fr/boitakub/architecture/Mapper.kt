/*
 * Copyright 2021 Boitakub
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.boitakub.architecture

/**
 * Represent the way i take own of the raw i get as an input
 *
 * Example {Parcel Delivery}
 * - The Parcel from another Parcel Delivery is not handled like i handle mine
 * (I don't care of his contract but I care about mine)
 */
interface Mapper<D : BusinessModel, S> {

    fun map(source: S): D
}
