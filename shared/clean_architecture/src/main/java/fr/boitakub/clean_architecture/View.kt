package fr.boitakub.clean_architecture

/**
 * The view is the way you deliver or pick-up something. (And nothing else)
 *
 * The View is always owned by a Interface (UI or other) it can be Mobile phone (Android or iOS),
 * a TV, a watch, a Fridge, an assistant ...
 *
 * Some Presenter don't need View, so View is no need in business part
 *
 * In Android MVVM pattern we can say that View is Fragment ou Activity (but can be other UI)
 */
interface View<P : Presenter> {

    val presenter: P
}
