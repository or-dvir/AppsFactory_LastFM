package com.hotmail.or_dvir.appsfactory_lastfm.vvm.adapters

import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.hotmail.or_dvir.dxadapter.IDxBaseItem

abstract class BaseAdapter<T : IDxBaseItem, VH : RecyclerView.ViewHolder>(
    val items: MutableList<T>
) : DxAdapter<T, VH>()
{
    override fun getDxAdapterItems() = items

    fun setData(data: List<T>)
    {
        items.apply {
            clear()
            addAll(data)
        }
        //note that there is no need to notify the adapter because we are using DiffUtil
    }

    fun isEmpty() = itemCount == 0
}
