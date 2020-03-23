package com.marsapps.triptip.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.ViewPagerOnTabSelectedListener
import com.marsapps.triptip.R
import com.marsapps.triptip.countries.CountriesFragment
import com.marsapps.triptip.country.TravelCountryActivity
import com.marsapps.triptip.db.DB
import com.marsapps.triptip.main.Constants.Companion.Extras.COUNTRY_NOTIFICATION_EXTRA
import com.marsapps.triptip.main.Constants.REST.BASE_API_URL
import com.marsapps.triptip.model.Country
import com.marsapps.triptip.model.CountryModel
import com.marsapps.triptip.rest.CountryModelConverter
import com.marsapps.triptip.rest.CountryService
import com.marsapps.triptip.rest.RestClientManager
import com.marsapps.triptip.travel.TravelListFragment
import com.marsapps.triptip.travel.TravelMapFragment
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private lateinit var sectionsPagerAdapter: SectionsPagerAdapter

    /**
     * The [ViewPager] that will host the section contents.
     */

    private var call: Call<List<Country>>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MobileAds.initialize(this) {}

        val adRequest = AdRequest.Builder().build()
        main_ad_view.loadAd(adRequest)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        pbTravel.visibility = View.VISIBLE
        window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        sectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        loadCountries(this)

        // If app opened from a notification, the app will move to country screen.
        intent.getStringExtra(COUNTRY_NOTIFICATION_EXTRA)?.let { countryName ->
            val country = CountriesContent.COUNTRIES.filter { it.name.toLowerCase() == countryName.toLowerCase() }

            val intent = Intent(this, TravelCountryActivity::class.java)
            intent.putExtra(Constants.Companion.Extras.COUNTRY_EXTRA, country[0])
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        call?.cancel()
        DB.closeConnection()
        super.onDestroy()
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_search, menu)
        super.onCreateOptionsMenu(menu)

        return true
    }



    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.getItem(0)?.isVisible = tabs.selectedTabPosition == 1
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadViewPager() {
        vpTravelContainer.adapter = sectionsPagerAdapter
        vpTravelContainer.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(ViewPagerOnTabSelectedListener(vpTravelContainer))
        pbTravel.visibility = View.GONE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    class SectionsPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        override fun getItem(position: Int): Fragment {

            var fragment: Fragment? = null
            when (position) {
                0 -> fragment = TravelListFragment()
                1 -> fragment = CountriesFragment()
                2 -> fragment = TravelMapFragment()
            }
            return fragment!!
        }

        // Show 3 total pages.
        override fun getCount(): Int {
            return 3
        }
    }

    private fun loadCountries(context: Context) {

        CountriesContent.clear()
        val cachedMovies: List<CountryModel> = DB.getInstance(context)?.countryDao()?.getCountries()!!
        CountriesContent.COUNTRIES.addAll(cachedMovies)

        if (CountriesContent.COUNTRIES.size == 0) {
            val countryService: CountryService? = RestClientManager.getCountryServiceInstance(BASE_API_URL)
            call = countryService?.getCountries()

            call?.enqueue(object : Callback<List<Country>?> {
                override fun onResponse(call: Call<List<Country>?>, response: Response<List<Country>?>) {
                    Log.i("response", "response")

                    if (response.code() == 200 && response.body() != null) {
                        DB.getInstance(context)?.countryDao()?.insertAll(CountryModelConverter.convertResult(response.body()!!))
                        CountriesContent.COUNTRIES.addAll(DB.getInstance(context)?.countryDao()?.getCountries()!!)
                        loadViewPager()
                    }
                }

                override fun onFailure(call: Call<List<Country>?>, t: Throwable) {
                    Log.e("failure", t.message)
                }
            })
        } else loadViewPager()
    }
}
