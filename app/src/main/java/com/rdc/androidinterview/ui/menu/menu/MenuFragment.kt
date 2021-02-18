package com.rdc.androidinterview.ui.menu.menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.RequestManager
import com.rdc.androidinterview.R
import com.rdc.androidinterview.models.CategorySection
import com.rdc.androidinterview.models.MenuItem
import com.rdc.androidinterview.models.StoreInfo
import com.rdc.androidinterview.session.SessionManager
import com.rdc.androidinterview.ui.menu.menu.state.MenuStateEvent
import com.rdc.androidinterview.util.SpacingItemDecoration
import kotlinx.android.synthetic.main.fragment_menu.*
import javax.inject.Inject

class MenuFragment : BaseMenuFragment(),
    CategoryListAdapter.Interaction,
    SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var requestManager: RequestManager

    private lateinit var recyclerAdapter: CategoryListAdapter
    private var currentCategoryList: MutableList<CategorySection> = mutableListOf()
    private var updatingPosition: Int = -1

    @Inject
    lateinit var sessionManager: SessionManager
    private var initialLoad: Boolean = true

    private var myStoreInfo: StoreInfo? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipe_refresh.setOnRefreshListener(this)

        initRecyclerView()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.dataState.observe(viewLifecycleOwner, { dataState ->
            stateChangeListener.onDataStateChange(dataState)
            if (dataState != null) {
                dataState.data?.let { data ->
                    data.data?.let { event ->
                        event.getContentIfNotHandled()?.let { viewState ->
                            viewState.accountProperties?.let { accountProperties ->
                                Log.d(TAG, "MenuFragment, DataState: $accountProperties")
                                viewModel.setAccountPropertiesData(accountProperties)
                            }
                            if (viewState.menuList.isNotEmpty()) {
                                viewModel.setMenuListData(viewState.menuList)
                            }
                            viewState.updatedMenuItem?.let { menuItem ->
                                viewModel.setUpdatedMenuItemData(menuItem)
                            }
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, { viewState ->
            if (viewState != null) {
                viewState.accountProperties?.let {
                    Log.d(TAG, "MenuFragment, ViewState: $it")
                    it.stores?.let { store ->
                        if (store.isEmpty().not()) {
                            val storeId = store[0].uuid
                            if (storeId.isEmpty().not().and(initialLoad)) {
                                myStoreInfo = store[0]
                                viewModel.setStateEvent(MenuStateEvent.SearchMenuItemsEvent(storeId))
                                initialLoad = false
                            }
                        }
                    }

                }
                viewState.menuList.let { menuList ->
                    val groupedList = menuList.groupBy { it.category }
                    val categoryList: MutableList<CategorySection> = mutableListOf()
                    groupedList.forEach { mapEntry ->
                        val categorySection = CategorySection(
                            uuid = mapEntry.key.uuid,
                            name = mapEntry.key.name,
                            menuItems = mapEntry.value
                        )
                        categoryList.add(categorySection)
                    }
                    Log.d(TAG, "MenuFragment, ViewState: $menuList")
                    recyclerAdapter.submitList(categoryList)
                    currentCategoryList = categoryList
                }
                viewState.updatedMenuItem?.let { updatedItem ->
                    currentCategoryList.forEachIndexed { index, categorySection ->
                        if (categorySection.uuid.contentEquals(updatedItem.category.uuid)) {
                            categorySection.menuItems[updatingPosition].availability =
                                updatedItem.availability
                            recyclerAdapter.notifyItemChanged(index, categorySection)
                        }
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.setStateEvent(MenuStateEvent.GetAccountPropertiesEvent())
    }

    private fun initRecyclerView() {

        menu_recyclerview.apply {

            recyclerAdapter = CategoryListAdapter(this@MenuFragment, requestManager)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    if (lastPosition == recyclerAdapter.itemCount.minus(1)) {
                        Log.d(
                            TAG,
                            "Menu Fragment: end of scroll, if pagination should be implemented this is the place..."
                        )
                    }
                }
            })
            adapter = recyclerAdapter
        }

    }

    override fun onItemSelected(position: Int, item: CategorySection, visibility: Boolean) {
        Log.d(TAG, "onItemSelected: position, CategorySection: $position, $item")
        when (visibility) {
            true -> item.itemsVisible = false
            false -> item.itemsVisible = true
        }
        recyclerAdapter.notifyItemChanged(position, item)
    }

    override fun onMenuItemIntent(position: Int, item: MenuItem) {
        updatingPosition = position
        when (item.availability) {
            AVAILABLE_STATUS -> {
                viewModel.setStateEvent(
                    MenuStateEvent.UpdateMenuItemsEvent(
                        item.uuid,
                        UNAVAILABLE_STATUS
                    )
                )
            }
            UNAVAILABLE_STATUS -> {
                viewModel.setStateEvent(
                    MenuStateEvent.UpdateMenuItemsEvent(
                        item.uuid,
                        AVAILABLE_STATUS
                    )
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // clear references (can leak memory)
        menu_recyclerview.adapter = null
    }

    companion object {
        private const val AVAILABLE_STATUS = "AVAILABLE"
        private const val UNAVAILABLE_STATUS = "UNAVAILABLE"
    }

    override fun onRefresh() {
        myStoreInfo?.let { store ->
            viewModel.setStateEvent(MenuStateEvent.SearchMenuItemsEvent(store.uuid))
        }
        swipe_refresh.isRefreshing = false
    }
}