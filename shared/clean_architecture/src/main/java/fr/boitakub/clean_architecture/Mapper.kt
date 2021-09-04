package fr.boitakub.clean_architecture

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
