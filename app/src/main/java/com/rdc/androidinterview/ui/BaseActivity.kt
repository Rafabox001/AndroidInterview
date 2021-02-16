package com.rdc.androidinterview.ui

import com.rdc.androidinterview.session.SessionManager
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity: DaggerAppCompatActivity(){

    val TAG: String = "AppDebug"

    @Inject
    lateinit var sessionManager: SessionManager

}











