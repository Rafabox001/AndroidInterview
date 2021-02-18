package com.rdc.androidinterview.ui.menu.menu

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.rdc.androidinterview.R
import com.rdc.androidinterview.models.CategorySection
import com.rdc.androidinterview.models.MenuItem
import kotlinx.android.synthetic.main.fragment_menu.view.*
import kotlinx.android.synthetic.main.layout_category_list_item.view.*

class CategoryListAdapter(
    private val interaction: Interaction? = null,
    private val requestManager: RequestManager
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    MenuItemListAdapter.Interaction{

    private val TAG: String = "AppDebug"

    private lateinit var recyclerAdapter: MenuItemListAdapter

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CategorySection>() {

        override fun areItemsTheSame(oldItem: CategorySection, newItem: CategorySection): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: CategorySection, newItem: CategorySection): Boolean {
            return oldItem == newItem
        }

    }

    private val differ =
        AsyncListDiffer(
            GategoryRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
        )

    internal inner class GategoryRecyclerChangeCallback(
        private val adapter: CategoryListAdapter
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

        return CagegoryViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_category_list_item,
                parent,
                false
            ),
            interaction
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        holder.itemView.menu_items_recycler.apply {
            recyclerAdapter = MenuItemListAdapter(this@CategoryListAdapter, requestManager)
            addOnScrollListener(object: RecyclerView.OnScrollListener(){

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == recyclerAdapter.itemCount.minus(1)) {
                        Log.d(TAG, "Menu Fragment: end of scroll, if pagination should be implemented this is the place...")
//
                    }
                }
            })
            adapter = recyclerAdapter
            recyclerAdapter.submitList(differ.currentList[position].menuItems)
        }
        when (holder) {
            is CagegoryViewHolder -> {
                holder.bind(differ.currentList[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    fun submitList(list: List<CategorySection>) {
        differ.submitList(list)
    }

    class CagegoryViewHolder
    constructor(
        itemView: View,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: CategorySection) = with(itemView) {
            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item, item.itemsVisible)
            }

            itemView.category.text = item.name
            itemView.menu_items_recycler.isVisible = item.itemsVisible
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: CategorySection, visibility: Boolean)
        fun onMenuItemIntent(position: Int, item: MenuItem)
    }

    override fun onItemSelected(position: Int, item: MenuItem) {
        Log.d(TAG, "onMenuItemIntent: position, MenuItem: $position, $item")
        interaction?.onMenuItemIntent(position, item)
    }
}