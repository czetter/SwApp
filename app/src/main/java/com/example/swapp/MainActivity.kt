package com.example.swapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.swapp.data.Park
import com.example.swapp.fragments.AddParkFragment
import com.example.swapp.fragments.InfoFragment
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        var init = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (init) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true)
            init = !init
        }

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        FirebaseApp.initializeApp(baseContext)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)


    }



    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_locations -> {
                var intent = Intent(this, LocationsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_info -> {
                showInfoDialog()
            }
            R.id.nav_add -> {
                showAddDialog()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun showInfoDialog() {
        var dialog = InfoFragment()
        dialog.show(supportFragmentManager, "Info")
    }

    private fun showAddDialog() {
        var dialog = AddParkFragment()
        dialog.show(supportFragmentManager, "Info")
    }
}
