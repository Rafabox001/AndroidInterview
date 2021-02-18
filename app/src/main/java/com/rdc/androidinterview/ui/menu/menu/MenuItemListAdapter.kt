package com.rdc.androidinterview.ui.menu.menu

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.rdc.androidinterview.R
import com.rdc.androidinterview.models.Category
import com.rdc.androidinterview.models.MenuItem
import com.rdc.androidinterview.util.GenericViewHolder
import kotlinx.android.synthetic.main.layout_category_list_item.view.*
import kotlinx.android.synthetic.main.layout_menu_list_item.view.*
import kotlinx.android.synthetic.main.layout_menu_list_item.view.menu_image

class MenuItemListAdapter(
    private val interaction: Interaction? = null,
    private val requestManager: RequestManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MenuItem>() {

        override fun areItemsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: MenuItem, newItem: MenuItem): Boolean {
            return oldItem == newItem
        }

    }

    private val differ =
        AsyncListDiffer(
            MenuItemRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
        )

    internal inner class MenuItemRecyclerChangeCallback(
        private val adapter: MenuItemListAdapter
    ) : ListUpdateCallback {

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyDataSetChanged()
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return MenuItemViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_menu_list_item, parent, false),
            interaction = interaction,
            requestManager = requestManager
        )

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is MenuItemViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<MenuItem>) {
        differ.submitList(list)
    }

    class MenuItemViewHolder
    constructor(
        itemView: View,
        val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: MenuItem) = with(itemView) {
            requestManager
                .load(item.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(itemView.menu_image)
            itemView.name.text = item.name
            itemView.description.text = item.description
            itemView.price.text = itemView.context.getString(R.string.product_price, item.price)
            when (item.availability) {
                ITEM_AVAILABLE -> {
                    itemView.availability_switch.isChecked = true
                    itemView.availability_switch.text =
                        itemView.context.getString(R.string.product_available)
                    itemView.availability_switch.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.tealDark
                        )
                    )
                }
                ITEM_UNAVAILABLE -> {
                    itemView.availability_switch.isChecked = false
                    itemView.availability_switch.text =
                        itemView.context.getString(R.string.product_unavailable)
                    itemView.availability_switch.setTextColor(
                        ContextCompat.getColor(
                            itemView.context,
                            R.color.red2
                        )
                    )
                }
            }

            itemView.availability_switch.setOnCheckedChangeListener { buttonView, _ ->
                if (buttonView.isPressed) {
                    interaction?.onItemSelected(adapterPosition, item)
                }
            }
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: MenuItem)
    }

    companion object {
        private const val ITEM_AVAILABLE = "AVAILABLE"
        private const val ITEM_UNAVAILABLE = "UNAVAILABLE"
    }
}