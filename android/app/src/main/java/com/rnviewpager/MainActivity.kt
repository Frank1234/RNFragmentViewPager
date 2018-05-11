package com.rnviewpager

import android.app.Activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentActivity
import android.support.v4.view.ViewPager
import android.view.KeyEvent
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactRootView
import com.facebook.react.common.LifecycleState
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.facebook.react.shell.MainReactPackage
import android.support.v4.view.accessibility.AccessibilityEventCompat.setAction
import android.view.View
import java.util.*


const val OVERLAY_PERMISSION_REQ_CODE = 12345

class MainActivity : FragmentActivity(), DefaultHardwareBackBtnHandler {

    lateinit var mReactInstanceManager: ReactInstanceManager
    private lateinit var viewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName"))
                startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE)
            }
        }

        mReactInstanceManager = ReactInstanceManager.builder()
                .setApplication(application)
                .setBundleAssetName("index.android.bundle")
                .setJSMainModulePath("index")
                .addPackage(MainReactPackage())
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build()

        setContentView(R.layout.main)

        viewPager = findViewById(R.id.viewpager) as ViewPager
        viewPager.setAdapter(MyViewPagerAdapter(supportFragmentManager))

        initButton()
    }

    private fun initButton() {

        findViewById<FloatingActionButton>(R.id.tryCrashButton).setOnClickListener {
            Snackbar.make(it, "Going to switch pages fast...", Snackbar.LENGTH_LONG).show()
            switchPagesFast()
        }
    }

    private fun switchPagesFast() {
        val count = viewPager.adapter?.count
        val random = Random()
        count?.let {
            for (i in 1..count - 1 step 2) {
                switchToPage(i, i * (random.nextInt(60).toLong()))
                switchToPage(count - i, (i + 1) * (random.nextInt(60).toLong()))
            }
        }
    }

    private fun switchToPage(pageNr: Int, delayMs: Long) {
        viewPager.postDelayed({
            viewPager.setCurrentItem(pageNr, false)
        }, delayMs)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        MainApplication.callbackManager.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPause() {
        super.onPause()

        mReactInstanceManager.onHostPause(this)
    }

    override fun onResume() {
        super.onResume()

        mReactInstanceManager.onHostResume(this, this)
    }

    override fun onDestroy() {
        super.onDestroy()

        mReactInstanceManager.onHostDestroy(this)
    }

    override fun invokeDefaultOnBackPressed() {
        super.onBackPressed()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            mReactInstanceManager.showDevOptionsDialog()
            return true
        }
        return super.onKeyUp(keyCode, event)
    }
}
