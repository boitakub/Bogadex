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
 * The Contract regroup all the elements that your are responsible of, because this is your business
 *
 * Example {Parcel Delivery}
 * - As a Parcel Delivery, I'm in charge of deliver your parcels from outside
 * - As a Parcel Delivery, I'm in charge to deliver your parcels at outside
 * - As a Parcel Delivery, I'm in charge of keep your parcels safety
 * - As a Parcel Delivery, I'm in charge of deliver your parcels in many ways
 */
interface Contract<P : Presenter, R : Repository>
