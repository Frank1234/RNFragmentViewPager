package com.rnviewpager

import com.facebook.FacebookSdk
import com.facebook.CallbackManager
import com.facebook.appevents.AppEventsLogger

import android.app.Application

import com.facebook.react.ReactApplication
import com.facebook.reactnative.androidsdk.FBSDKPackage
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.shell.MainReactPackage
import com.facebook.soloader.SoLoader

import java.util.Arrays

class MainApplication : Application(), ReactApplication {

    companion object {

        val callbackManager = CallbackManager.Factory.create()
    }

    private val mReactNativeHost = object : ReactNativeHost(this) {
        override fun getUseDeveloperSupport(): Boolean {
            return BuildConfig.DEBUG
        }

        override fun getPackages(): List<ReactPackage> {
            return Arrays.asList(
                    MainReactPackage(),
                    FBSDKPackage(callbackManager)
            )
        }

        override fun getJSMainModuleName(): String {
            return "index"
        }
    }

    override fun getReactNativeHost(): ReactNativeHost {
        return mReactNativeHost
    }

    override fun onCreate() {
        super.onCreate()
        SoLoader.init(this, /* native exopackage */ false)
    }
}
