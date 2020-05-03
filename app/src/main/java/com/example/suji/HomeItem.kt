package com.example.suji

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_home.view.*
import org.jetbrains.anko.layoutInflater

class HomeItem : Item {

    internal var builder: (ViewHolder.() -> Unit)? = null

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {
            val view = parent.context.layoutInflater.inflate(R.layout.item_home, parent, false)
            val dayView = view.tv_item_day
            val weekView = view.tv_item_week
            val contentView = view.tv_item_content
            return ViewHolder(view, dayView, weekView, contentView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as ViewHolder
            item as HomeItem
            item.builder?.invoke(holder)
        }

        internal class ViewHolder(
            itemView: View,
            val dayView: TextView,
            val weekView: TextView,
            val contentView: TextView
        ) : RecyclerView.ViewHolder(itemView)
    }

    override val controller: ItemController
        get() = HomeItem
}

internal fun MutableList<Item>.homeItem(builder: HomeItem.Controller.ViewHolder.() -> Unit) =
    add(HomeItem().apply { this.builder = builder })

