package com.hotmail.or_dvir.appsfactory_lastfm.vvm.adapters

import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseItem

/**
 * a base class used to hold some shared functionality of all the adapters in this app
 */
abstract class BaseAdapter<T : IDxBaseItem, VH : RecyclerView.ViewHolder>(
    val items: MutableList<T>
) : DxAdapter<T, VH>()
{
    override fun getDxAdapterItems() = items

    /**
     * sets the value of [items] to the given [data].
     * This completely overrides the current value of [items]
     */
    fun setData(data: List<T>)
    {
        items.apply {
            clear()
            addAll(data)
        }

        //note that there is no need to notify the adapter because we are using DiffUtil
    }

    /**
     * returns whether or not this adapter is empty
     */
    fun isEmpty() = itemCount == 0
}
