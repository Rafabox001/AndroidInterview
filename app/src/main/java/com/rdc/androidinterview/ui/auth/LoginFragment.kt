package com.rdc.androidinterview.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.rdc.androidinterview.R
import com.rdc.androidinterview.util.ApiEmptyResponse
import com.rdc.androidinterview.util.ApiErrorResponse
import com.rdc.androidinterview.util.ApiSuccessResponse

class LoginFragment : BaseAuthFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "LoginFragment: ${viewModel.hashCode()}")

        viewModel.testLogin().observe(viewLifecycleOwner, { response ->

            when(response){
                is ApiSuccessResponse ->{
                    Log.d(TAG, "LOGIN RESPONSE: ${response.body}")
                }
                is ApiErrorResponse ->{
                    Log.d(TAG, "LOGIN RESPONSE: ${response.errorMessage}")
                }
                is ApiEmptyResponse ->{
                    Log.d(TAG, "LOGIN RESPONSE: Empty Response")
                }
            }
        })
    }

}