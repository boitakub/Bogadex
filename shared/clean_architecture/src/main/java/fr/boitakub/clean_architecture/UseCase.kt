package fr.boitakub.clean_architecture

/**
 * Use cases are responsible of business rules
 *
 * Example {Parcel Delivery}
 * - Use Case : Re-direct the parcel which are not on my area
 * - Use Case : Destroy dangerous parcels (and notify the customer in a customer usecase)
 */
interface UseCase<O, I> {

    fun apply(input: I): O
}
