package com.tt.eggs

import android.app.Application
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.tt.eggs.classes.UpdateHelper
import java.util.*
import kotlin.collections.HashMap

class Eggs:Application() {
    override fun onCreate() {
        super.onCreate()

        val firebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
       val remoteConfigDefaults = HashMap<String,Any>()
        remoteConfigDefaults.put(UpdateHelper.KEY_UPDATE_ENABLE,false)
        remoteConfigDefaults.put(UpdateHelper.KEY_UPDATE_VERSION,"1.0.0")
        remoteConfigDefaults.put(UpdateHelper.KEY_UPDATE_URL,
            "https://play.google.com/store/apps/details?id=com.tt.eggs")


        firebaseRemoteConfig.setDefaultsAsync(remoteConfigDefaults)
        firebaseRemoteConfig.fetch(300).addOnCompleteListener {
            if(it.isSuccessful){
                firebaseRemoteConfig.activate()
            }
        }

    }

}