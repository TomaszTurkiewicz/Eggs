package com.tt.eggs


import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.ump.ConsentDebugSettings
import com.google.android.ump.ConsentForm
import com.google.android.ump.ConsentInformation
import com.google.android.ump.ConsentRequestParameters
import com.google.android.ump.UserMessagingPlatform
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tt.eggs.classes.Functions
import com.tt.eggs.classes.GooglePlayApps
import com.tt.eggs.classes.NewApps
import com.tt.eggs.classes.User
import com.tt.eggs.databinding.ActivityMainBinding
import java.util.concurrent.atomic.AtomicBoolean


class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    private lateinit var userList: MutableList<User>
    private var listDownloaded = false
    private var mInterstitialAd: InterstitialAd? = null
    private var googlePlayApps: GooglePlayApps? = null
    private var apps:NewApps = NewApps()

    private lateinit var consentInformation: ConsentInformation
    // Use an atomic boolean to initialize the Google Mobile Ads SDK and load ads once.
    private var isMobileAdsInitializeCalled = AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // make full screen
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        fullScreen(view)
//        MobileAds.initialize(this)
        requestConsentForm()

        userList = mutableListOf()
        createUserListFromFirebase()

        apps.setAppsInMemoryInt(Functions.readNumberOfAppsFromMemory(this))
        checkAppsInGooglePlay()

    }

    private fun requestConsentForm(){



        // Set tag for under age of consent. false means users are not under age
        // of consent.
        val params = ConsentRequestParameters
            .Builder()
            .setTagForUnderAgeOfConsent(false)
            .build()


        consentInformation = UserMessagingPlatform.getConsentInformation(this)
//        consentInformation.reset()
        consentInformation.requestConsentInfoUpdate(
            this,
            params,
            {
                UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                    this@MainActivity,
                    ConsentForm.OnConsentFormDismissedListener {
                            loadAndShowError ->
                        // Consent gathering failed.
                        Log.w(
                            ContentValues.TAG, String.format("%s: %s",
                            loadAndShowError?.errorCode,
                            loadAndShowError?.message))

                        // Consent has been gathered.
                        if (consentInformation.canRequestAds()) {
                            initializeMobileAdsSdk()
                        }
                    }
                )
            },
            {
                    requestConsentError ->
                // Consent gathering failed.
                Log.w(
                    ContentValues.TAG, String.format("%s: %s",
                    requestConsentError.errorCode,
                    requestConsentError.message))
            })

        // Check if you can initialize the Google Mobile Ads SDK in parallel
        // while checking for new consent information. Consent obtained in
        // the previous session can be used to request ads.
        if (consentInformation.canRequestAds()) {
            initializeMobileAdsSdk()
        }
    }

    private fun initializeMobileAdsSdk() {
        if (isMobileAdsInitializeCalled.get()) {
            return
        }
        isMobileAdsInitializeCalled.set(true)

        // Initialize the Google Mobile Ads SDK.
        MobileAds.initialize(this)

    }

    private fun checkAppsInGooglePlay(){
        val dbRef = Firebase.database.getReference("GooglePlayApps")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                googlePlayApps = snapshot.getValue(GooglePlayApps::class.java)
                googlePlayApps?.let {
                    apps.setAppsInGooglePlayInt(it)
                    checkIfNewApp()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // do nothing
            }

        })
    }

    private fun checkIfNewApp(){
        val newApp = apps.isNewApp()
        Functions.saveNumberOfAppsFromMemory(this@MainActivity,apps.appsInGooglePlay)
        if(newApp){
            Functions.saveNewAppAvailable(this@MainActivity,true)
        }
    }


    private fun fullScreen(view:View){
        WindowCompat.setDecorFitsSystemWindows(window,false)
        WindowInsetsControllerCompat(window,view).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun createUserListFromFirebase() {
        val dbRef = Firebase.database.getReference("user")
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()){
                    for(user in p0.children){
                        val tUser = user.getValue(User::class.java)
                        userList.add(tUser!!)
                    }
                    listDownloaded = true
                }
            }
        })
    }

    private fun setFullScreenContent(){
        mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback(){
            override fun onAdClicked() {
                super.onAdClicked()
            }

            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
            }

            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
            }

            override fun onAdImpression() {
                super.onAdImpression()
            }

            override fun onAdShowedFullScreenContent() {
                mInterstitialAd = null
            }
        }
    }

    fun getDownloaded():Boolean{
        return this.listDownloaded
    }

    fun getList(): MutableList<User> {
        return this.userList
    }

    fun loadAdvert(){
        if(mInterstitialAd==null){
            val adRequest = AdRequest.Builder().build()
            val adId = getString(R.string.admob_big)
            InterstitialAd.load(this,adId,adRequest,object : InterstitialAdLoadCallback(){
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                    setFullScreenContent()
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    loadAdvert()
                }
            })
        }
    }

    fun showAdvert(){
        mInterstitialAd?.let {
            it?.show(this)
        }
    }

}









