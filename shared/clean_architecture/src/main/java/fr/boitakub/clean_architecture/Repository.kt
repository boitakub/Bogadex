package fr.boitakub.clean_architecture

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
