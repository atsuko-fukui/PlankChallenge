package com.example.muumuu.plankchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.muumuu.plankchallenge.databinding.ActivityMainBinding

interface Host {
    fun showRecordScreen()
}
class MainActivity : AppCompatActivity(), Host {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        showFragment(R.id.nav_record)
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
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
