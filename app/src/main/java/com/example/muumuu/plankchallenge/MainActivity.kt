package com.example.muumuu.plankchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

interface Host {
    fun showRecordScreen()
}
class MainActivity : AppCompatActivity(), Host {

    private val bottomNavigationView: BottomNavigationView
        get() = findViewById(R.id.bottom_navigation)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showFragment(R.id.nav_record)
        bottomNavigationView.setOnNavigationItemSelectedListener {
            showFragment(it.itemId)
            true
        }
    }

    private fun showFragment(itemId: Int) {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, getFragment(itemId), itemId.toString())
            .commitAllowingStateLoss()
    }

    private fun getFragment(itemId: Int) =
        supportFragmentManager.findFragmentByTag(itemId.toString()) ?: when (itemId) {
            R.id.nav_record -> RecordFragment()
            R.id.nav_exercise -> ExerciseFragment()
            else -> throw Throwable(IllegalArgumentException())
        }

    override fun showRecordScreen() {
        showFragment(R.id.nav_record)
    }
}
