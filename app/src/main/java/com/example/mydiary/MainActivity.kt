package com.example.mydiary

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.mydiary.databinding.ActivityMainBinding
import com.example.mydiary.ui.homeFragment.HomeFragment
import com.example.mydiary.utils.ProgressDialogUtil
import com.example.mydiary.utils.toast
import dagger.hilt.android.AndroidEntryPoint

import java.io.File


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        ProgressDialogUtil.createprogressdialog(this)
        supportFragmentManager.beginTransaction().add(R.id.navhostfragment, HomeFragment(), "home")
            .commit()


    }


    override fun onBackPressed() {
        val fragment: Fragment? = supportFragmentManager.findFragmentByTag("home")
        val fragment2: Fragment? = supportFragmentManager.findFragmentByTag("details")
        val fragment3: Fragment? = supportFragmentManager.findFragmentByTag("details2")

        if (fragment2 != null && fragment2.isHidden) {
            supportActionBar?.setTitle("Update Memory")
            supportFragmentManager.beginTransaction().show(fragment2)
        } else if (fragment3 != null && fragment3.isHidden) {
            supportActionBar?.setTitle("Add Memory")
            supportFragmentManager.beginTransaction().show(fragment3)
        } else if (fragment != null && fragment.isHidden) {

            supportActionBar?.setDisplayHomeAsUpEnabled(false)
            supportActionBar?.setTitle("My Diary")
            supportFragmentManager.beginTransaction().show(fragment)
        }

        super.onBackPressed()
    }




}