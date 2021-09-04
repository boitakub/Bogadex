package fr.boitakub.clean_architecture

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
