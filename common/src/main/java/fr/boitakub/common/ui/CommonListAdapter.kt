package fr.boitakub.common.ui

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

abstract class CommonListAdapter<T : CommonListViewHolder, E>(val layoutManager: GridLayoutManager) : RecyclerView.Adapter<T>() {

    var itemList = mutableListOf<E>()

    fun setItems(items: List<E>) {
        this.itemList = items.toMutableList()
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
