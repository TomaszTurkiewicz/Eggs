package com.tt.eggs.classes

class NewApps {
    var appsInGooglePlay = 0
    var appsSavedINMemory = 0

    fun setAppsInGooglePlayInt(numberOfApps: GooglePlayApps){
        this.appsInGooglePlay = numberOfApps.numberOfApps
    }

    fun saveNewNumberOfApps(){
        this.appsSavedINMemory = this.appsInGooglePlay
    }

    private fun setAppsInMemoryEqualToGoogle(){
        this.appsSavedINMemory = this.appsInGooglePlay
    }

    fun isNewApp():Boolean{
        if(appsInGooglePlay<appsSavedINMemory){
            setAppsInMemoryEqualToGoogle()
        }
        return appsInGooglePlay>appsSavedINMemory
    }
}