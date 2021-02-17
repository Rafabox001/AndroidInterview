package com.rdc.androidinterview.ui.menu.menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rdc.androidinterview.R
import com.rdc.androidinterview.session.SessionManager
import com.rdc.androidinterview.ui.menu.menu.state.MenuStateEvent
import javax.inject.Inject

class MenuFragment : BaseMenuFragment(){

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeObservers()
    }

    private fun subscribeObservers(){
        viewModel.dataState.observe(viewLifecycleOwner, { dataState ->
            stateChangeListener.onDataStateChange(dataState)
            if(dataState != null){
                dataState.data?.let { data ->
                    data.data?.let{ event ->
                        event.getContentIfNotHandled()?.let{ viewState ->
                            viewState.accountProperties?.let{ accountProperties ->
                                Log.d(TAG, "MenuFragment, DataState: $accountProperties")
                                viewModel.setAccountPropertiesData(accountProperties)
                            }
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, { viewState->
            if(viewState != null){
                viewState.accountProperties?.let{
                    Log.d(TAG, "MenuFragment, ViewState: ${it}")
                    it.stores?.let { store ->
                        if (store.isEmpty().not()){
                            val storeId = store[0].uuid
                            if (storeId.isEmpty().not()){
                                viewModel.setStateEvent(MenuStateEvent.SearchMenuItemsEvent(storeId!!))
                            }
                        }
                    }

                }
                viewState.menuList?.let {
                    Log.d(TAG, "MenuFragment, ViewState: ${it}")

                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        viewModel.setStateEvent(MenuStateEvent.GetAccountPropertiesEvent())
    }
}