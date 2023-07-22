package com.tt.eggs.classes

class NewApps {
    var appsInGooglePlay = 0
    var appsSavedINMemory = 0

    fun setAppsInGooglePlayInt(numberOfApps: GooglePlayApps){
        this.appsInGooglePlay = numberOfApps.numberOfApps
    }

    fun setAppsInMemoryInt(numberOfAppsInMemory:Int){
        this.appsSavedINMemory = numberOfAppsInMemory
    }

    fun isNewApp():Boolean{
        return appsInGooglePlay>appsSavedINMemory
    }
}