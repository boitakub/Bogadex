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
 * The presenter is the way i present my valuable object.
 *
 * Example {Parcel Delivery}
 * - Consume by a delivery man : Deliver the parcel
 * - Consumed by a mobile application : Show parcel state
 * - Consumed by an assistant : Say 'Your parcel is in the truck'
 *
 * Keep in mind that the output is not only Interface this is now an Interaction
 *
 * In Android MVVM pattern we can say that Presenter is ViewModel
 */
interface Presenter
