package fr.boitakub.clean_architecture

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
