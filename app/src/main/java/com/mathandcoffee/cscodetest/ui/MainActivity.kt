package com.mathandcoffee.cscodetest.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mathandcoffee.cscodetest.R
import com.mathandcoffee.cscodetest.databinding.ActivityMainBinding
import com.mathandcoffee.cscodetest.ui.login.LoginFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideNavigationBar()

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.navigation_host, LoginFragment())
        transaction.disallowAddToBackStack()
        transaction.commit()
    }

    private fun hideNavigationBar() {
        supportActionBar?.hide()
    }
}