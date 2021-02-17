package com.rdc.androidinterview.ui.menu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rdc.androidinterview.R
import com.rdc.androidinterview.ui.BaseActivity
import com.rdc.androidinterview.ui.auth.AuthActivity
import com.rdc.androidinterview.ui.menu.account.BaseAccountFragment
import com.rdc.androidinterview.ui.menu.menu.BaseMenuFragment
import com.rdc.androidinterview.util.BottomNavController
import com.rdc.androidinterview.util.BottomNavController.*
import com.rdc.androidinterview.util.setUpNavigation
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : BaseActivity(),
    NavGraphProvider,
    OnNavigationGraphChanged,
    OnNavigationReselectedListener
{
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private lateinit var bottomNavigationView: BottomNavigationView

    private val bottomNavController by lazy(LazyThreadSafetyMode.NONE) {
        BottomNavController(
            this,
            R.id.menu_nav_host_fragment,
            R.id.nav_menu,
            this,
            this)
    }

    override fun getNavGraphId(itemId: Int) = when(itemId){
        R.id.nav_menu -> {
            R.navigation.nav_menu
        }
        R.id.nav_account -> {
            R.navigation.nav_account
        }
        else -> {
            R.navigation.nav_menu
        }
    }

    override fun onGraphChange() {
        expandAppBar()
    }

    private fun cancelActiveJobs() {
        val fragments = bottomNavController.fragmentManager
            .findFragmentById(bottomNavController.containerId)
            ?.childFragmentManager
            ?.fragments
        if (fragments != null) {
            for (fragment in fragments) {
                when(fragment){
                    is BaseAccountFragment -> fragment.cancelActiveJobs()
                    is BaseMenuFragment -> fragment.cancelActiveJobs()
                }
            }
        }
        displayProgressBar(false)
    }

    override fun onBackPressed() = bottomNavController.onBackPressed()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setUpNavigation(bottomNavController, this)
        if (savedInstanceState == null) {
            bottomNavController.onNavigationItemSelected()
        }

        subscribeObservers()
        setupActionBar()
    }

    override fun expandAppBar() {
        findViewById<AppBarLayout>(R.id.app_bar).setExpanded(true)
    }

    private fun setupActionBar(){
        setSupportActionBar(tool_bar)
    }

    fun subscribeObservers() {
        sessionManager.cachedToken.observe(this, { authToken ->
            Log.d(TAG, "MainActivity, subscribeObservers: ViewState: ${authToken}")
            if (authToken == null || authToken.account_pk == -1 || authToken.access_token == null) {
                navAuthActivity()
                finish()
            }
        })
    }

    private fun navAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun displayProgressBar(bool: Boolean) {
        if(bool){
            progress_bar.visibility = View.VISIBLE
        }
        else{
            progress_bar.visibility = View.GONE
        }
    }

    override fun onReselectNavItem(navController: NavController, fragment: Fragment) {
        //ignore
    }
}