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
 * The repository is responsible of delivering valuable object or store them
 *
 * Example {Parcel Delivery}
 * - Get a parcel from my warehouse
 * - Get a list of parcel from my warehouse
 * - Put a parcel in my warehouse
 * - Put a parcel collection un my warehouse
 */
interface Repository {

    val local: Any

    val remote: Any
}
