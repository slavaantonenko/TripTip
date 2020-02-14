package com.marsapps.triptip.country

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.work.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.data.geojson.GeoJsonLayer
import com.google.maps.android.data.geojson.GeoJsonMultiPolygon
import com.google.maps.android.data.geojson.GeoJsonPolygon
import com.marsapps.triptip.R
import com.marsapps.triptip.main.Constants.Companion.Data.COUNTRY_NAME_DATA
import com.marsapps.triptip.main.Constants.Companion.Data.NOTIFICATION_ID_DATA
import com.marsapps.triptip.main.Constants.Companion.Extras.COUNTRY_EXTRA
import com.marsapps.triptip.main.Constants.Companion.Ignore.DISABLE_COUNTRIES_HIGHLIGHT
import com.marsapps.triptip.main.Constants.Companion.Preferences.TRAVEL_LIST_CHANGED
import com.marsapps.triptip.main.Constants.Companion.RecommendedMonths.MONTHS
import com.marsapps.triptip.main.Constants.REST.BASE_GOOGLE_API_URL
import com.marsapps.triptip.main.Constants.REST.IMAGES_URL
import com.marsapps.triptip.main.CountryViewModel
import com.marsapps.triptip.main.MonthTipNotificationWorker
import com.marsapps.triptip.main.saveBooleanToCache
import com.marsapps.triptip.model.CountryCandidates
import com.marsapps.triptip.model.CountryModel
import com.marsapps.triptip.rest.RestClientManager
import kotlinx.android.synthetic.main.activity_travel_country.*
import kotlinx.android.synthetic.main.country_overview.*
import kotlinx.android.synthetic.main.country_when_to_travel.*
import org.json.JSONException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class TravelCountryActivity : AppCompatActivity(), OnMapReadyCallback, OnFloatingActionClickListener, OnExpandTextClickListener {

    private lateinit var countryViewModel: CountryViewModel
    private lateinit var country: CountryModel
    private var mapFragment: CustomMapFragment? = null
    private var call: Call<CountryCandidates>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_travel_country)

        country = intent.getParcelableExtra(COUNTRY_EXTRA)
        countryViewModel = ViewModelProviders.of(this).get(CountryViewModel::class.java)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        (supportFragmentManager.findFragmentById(R.id.mapCountry) as CustomMapFragment?)!!.getMapAsync(this)

        initializeView()
    }

    override fun onDestroy() {
        call?.cancel()
        super.onDestroy()
    }

    override fun onMapReady(googleMap: GoogleMap?) {

        try {
            val rawID = resources.getIdentifier(
                country.secondaryCode.toLowerCase() + "_geojson", "raw", packageName)
            val layer = GeoJsonLayer(googleMap, rawID, this)
            layer.addLayerToMap()

            if (layer.features.iterator().hasNext()) { // Waiting till layer will be added to the map.
                googleMap?.setOnMapLoadedCallback {
                    // Get the bounding box builder.
                    var builder = LatLngBounds.builder()
                    val geometry = layer.features.iterator().next().geometry

                    // Some countries have multiply borders like Greece (because of the many islands).
                    if (geometry.geometryType == "Polygon") {
                        val listOfCoordinates = (geometry as GeoJsonPolygon).coordinates
                        builder = getLatLngBoundsBuilder(listOfCoordinates, builder!!)
                    }
                    else if (geometry.geometryType == "MultiPolygon") {
                        val polygons = (geometry as GeoJsonMultiPolygon).polygons
                        if (polygons != null)
                            for (polygon in polygons)
                                builder = getLatLngBoundsBuilder(polygon.coordinates, builder!!)
                    }

                    val bounds = builder!!.build()
                    val padding = 100 // offset from edges of the map in pixels
                    val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)

                    googleMap.moveCamera(cameraUpdate)
                    googleMap.animateCamera(cameraUpdate)
                }
            }
            else {
                // Move the camera to location
                val latLng = LatLng(country.latitude, country.longitude)
                googleMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))

                // For zooming automatically to the location of the marker
                val cameraPosition = CameraPosition.Builder().target(latLng).zoom(8.0f).build()
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
            }

            // Enable scroll map up and down inside a ScrollView
            mapFragment?.setListener(object : CustomMapFragment.OnTouchListener {
                override fun onTouch() {
                    svCountry.requestDisallowInterceptTouchEvent(true)
                }
            })

            googleMap?.setOnCameraIdleListener {
                if (googleMap.cameraPosition.zoom > 7 || DISABLE_COUNTRIES_HIGHLIGHT.contains(country.name))
                    setPolygonStyle(layer, android.R.color.transparent, android.R.color.transparent, 0f)
                else
                    setPolygonStyle(layer,
                        R.color.highlightCountry,
                        R.color.highlightCountry, 2f)
            }
        }
        catch (e: IOException) {
            e.printStackTrace()
        }
        catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    override fun onExpandClick(viewId: Int): View.OnClickListener? {
        return View.OnClickListener {
            when (viewId) {
                clOverview.id -> {
                    if (elOverview.isExpanded) {
                        elOverview.collapse()
                        ivExpandOverview.setImageResource(R.drawable.ic_collapse)
                    } else {
                        elOverview.expand()
                        ivExpandOverview.setImageResource(R.drawable.ic_expand)
                    }
                }
                clWhenToTravel.id -> {
                    if (elWhenToTravel.isExpanded) {
                        elWhenToTravel.collapse()
                        ivExpandWhenToTravel.setImageResource(R.drawable.ic_collapse)
                    } else {
                        elWhenToTravel.expand()
                        ivExpandWhenToTravel.setImageResource(R.drawable.ic_expand)
                    }
                }
            }
        }
    }

    override fun onFabClick(): View.OnClickListener {
        return View.OnClickListener {
            if (!country.isInTravelList) {
                TRAVEL_LIST_CHANGED.saveBooleanToCache(baseContext, true)
                country.isInTravelList = true
                getImage(country.name)
                scheduleNotification()
            }
            else
                Toast.makeText(baseContext, getString(R.string.already_added_to_travel_list, country.name), Toast.LENGTH_LONG).show()
        }
    }

    private fun onCountryCloseClick(): View.OnClickListener? {
        return View.OnClickListener { onBackPressed() }
    }

    private fun initializeView() {
        clOverview.setOnClickListener(onExpandClick(clOverview.id))
        clWhenToTravel.setOnClickListener(onExpandClick(clWhenToTravel.id))
        fabCountry.setOnClickListener(onFabClick())
        ivCountryClose.setOnClickListener(onCountryCloseClick())

        Glide.with(this).load(country.flag).error(R.drawable.ic_flag_sample)
            .listener(object : RequestListener<Drawable> {

                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    Log.e("Glide failed", "Failed load flag for " + country.name + " " + e?.message)
                    return false
                }

                override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?,
                                             dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                    Log.i("Glide success", country.name + " downloaded flag succeeded")
                    return false
                }
            })
            .into(ivCountryFlag)


        tvCountryName.text = country.name
        tvNativeNameValue.text = country.nativeName
        tvCapitalCityName.text = country.capital
        tvLanguageName.text = country.language
        tvNativeLanguageName.text = country.nativeLanguage
        tvRegionName.text = country.region
        tvAreaSize.text = getString(R.string.area_size_proportion, country.area.toString())
        tvPopulationSize.text = country.population.toString()
        tvCurrencyValue.text = country.currency
        tvTimeZoneValue.text = country.timezone
        tvCallingCodeValue.text = getString(R.string.calling_code, country.callingCode)
        tvCountryCodeValues.text = getString(R.string.country_codes, country.code, country.secondaryCode)
        tvWhenToTravel.text = getString(resources.getIdentifier(
            "when_to_travel_to_" + country.code.toLowerCase(), "string", packageName))
    }

    private fun getImage(searchParameter: String) {

        if (country.image.isEmpty()) {
            val apiKey = packageManager.getApplicationInfo(packageName,
                PackageManager.GET_META_DATA).metaData.getString("com.google.android.geo.API_KEY")!!

            val countryService = RestClientManager.getCountryServiceInstance(BASE_GOOGLE_API_URL)
            call = countryService!!.getCountryImages(searchParameter, apiKey)

            call!!.enqueue(object : Callback<CountryCandidates> {
                override fun onResponse(call: Call<CountryCandidates>, response: Response<CountryCandidates>) {

                    response.body()?.candidates?.let { candidate ->
                        if (candidate.isNotEmpty()) {
                            candidate[0].photos?.let { photos ->
                                if (photos.isNotEmpty())
                                    country.image = "$IMAGES_URL${photos[0].photoReference}&key=$apiKey"
                                else
                                    getImage(country.capital)
                            }?: run {
                                getImage(country.capital)
                            }
                        }
                        else
                            getImage(country.capital)
                    }?: run {
                        getImage(country.capital)
                    }

                    updateTravelList()
                }

                override fun onFailure(call: Call<CountryCandidates>, t: Throwable) {
                    Log.e("failure", t.message)
                }
            })
        }

        updateTravelList()
    }

    /**
     * This method updates travel list data in all screens and shows a message that the country was added to that list.
     */
    private fun updateTravelList() {
        countryViewModel.updateIsInTravelList(country)
        Toast.makeText(baseContext, getString(R.string.added_to_travel_list, country.name), Toast.LENGTH_LONG).show()
    }

    /**
     * The method adds coordinates to existed builder.
     * @param listOfCoordinates border coordinates, could be multiply borders.
     * @param builder Latitude Longitude existed builder.
     * @return
     */
    private fun getLatLngBoundsBuilder(listOfCoordinates: List<List<LatLng>>?, builder: LatLngBounds.Builder): LatLngBounds.Builder? {
        listOfCoordinates.let {
            for (coordinates in it!!)
                for (latLng in coordinates)
                    builder.include(latLng)
        }

        return builder
    }

    /**
     * The method sets country layer style.
     * @param layer country layer.
     * @param fillColor
     * @param strokeColor
     * @param strokeWidth
     */
    private fun setPolygonStyle(layer: GeoJsonLayer, fillColor: Int, strokeColor: Int, strokeWidth: Float) {
        val style = layer.defaultPolygonStyle
        style.fillColor = ContextCompat.getColor(baseContext, fillColor)
        style.strokeColor = ContextCompat.getColor(baseContext, strokeColor)
        style.strokeWidth = strokeWidth
    }

    /**
     * This method will schedule future notification for travel recommended months for each county.
     */
    private fun scheduleNotification() {
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val data = Data.Builder()
        data.putString(COUNTRY_NAME_DATA, country.name)
        data.putInt(NOTIFICATION_ID_DATA, System.currentTimeMillis().toInt())

        MONTHS[country.code.toLowerCase()]!!.forEach {
            val delayedTime: Long = getDelayedTime(it.monthNumber) - System.currentTimeMillis()

            val travelMonthTipRequest = OneTimeWorkRequest.Builder(MonthTipNotificationWorker::class.java)
                .setConstraints(constraints)
                .setInputData(data.build())
                .setInitialDelay(delayedTime, TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(this).enqueue(travelMonthTipRequest) // Set notification in place.
        }
    }

    /**
     * This method calculates when to schedule the notification.
     */
    @SuppressLint("SimpleDateFormat")
    private fun getDelayedTime(monthNumber: Int): Long {
        var delayedTime: Long = 0

        try {
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            val notificationDate: String
            var notificationDay = 1
            val notificationMonth = SimpleDateFormat("MM").format(Date()).toInt()
            var notificationYear = Calendar.getInstance().get(Calendar.YEAR)

            if (notificationMonth > monthNumber)
                notificationYear = Calendar.getInstance().get(Calendar.YEAR).plus(1)
            else if (notificationMonth == monthNumber)
                notificationDay = SimpleDateFormat("dd").format(Date()).toInt().plus(1)

            notificationDate = "$notificationDay/$monthNumber/$notificationYear 12:00:00"
            delayedTime = simpleDateFormat.parse(notificationDate).time
        }
        catch (e: ParseException) {
            e.printStackTrace()
        }

        return delayedTime
    }
}
