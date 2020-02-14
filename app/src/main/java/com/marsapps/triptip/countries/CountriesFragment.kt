package com.marsapps.triptip.countries

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.marsapps.triptip.R
import com.marsapps.triptip.country.TravelCountryActivity
import com.marsapps.triptip.main.*
import com.marsapps.triptip.main.Constants.Companion.Extras.COUNTRY_EXTRA
import com.marsapps.triptip.main.Constants.Companion.Preferences.TRAVEL_LIST_CHANGED
import com.marsapps.triptip.main.interfaces.OnCountryClickListener
import com.marsapps.triptip.model.CountryModel
import kotlinx.android.synthetic.main.fragment_travel_countries.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [CountriesFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [CountriesFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class CountriesFragment : Fragment(),
    OnCountryClickListener {

    private lateinit var adapter: CountriesAdapter
    private lateinit var countryViewModel: CountryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_travel_countries, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeRecyclerView()

        countryViewModel = ViewModelProviders.of(activity!!).get(CountryViewModel::class.java)
        countryViewModel.getTravelListCountries()?.observe(this, Observer<List<CountryModel>> { countries ->
            val travelListChanged = TRAVEL_LIST_CHANGED.getBooleanFromCache(context, false)

            if (travelListChanged) {
                TRAVEL_LIST_CHANGED.saveBooleanToCache(context, false)
                adapter.updateTravelListCountries(countries)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu?.clear()
        inflater?.inflate(R.menu.menu_search, menu)

        val searchView = menu?.findItem(R.id.action_filter_countries)?.actionView as SearchView
        searchView.isIconified = true
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                adapter.filter(s)
                return false
            }
        })
    }

    override fun onCountryClick(view: View?, position: Int) {
        val intent = Intent(activity, TravelCountryActivity::class.java)
        intent.putExtra(COUNTRY_EXTRA, adapter.getItem(position))
        startActivity(intent)
    }

    private fun initializeRecyclerView() {
        val contentRowPixels: Int = (resources.getDimension(R.dimen.ic_width).toInt() + resources.getDimensionPixelSize(
            R.dimen.recycler_view_grid_spacing).pxToDP(resources))
        val mNoOfColumns: Int = Utils.calculateNoOfColumns(resources, contentRowPixels)

        rvCountries.layoutManager = GridLayoutManager(context, mNoOfColumns)
        rvCountries.addItemDecoration(Utils.Companion.GridSpacingItemDecoration(mNoOfColumns, resources.getDimensionPixelSize(
            R.dimen.recycler_view_grid_spacing).pxToDP(resources), true))

        adapter = CountriesAdapter(context!!, CountriesContent.COUNTRIES, this)
        rvCountries.adapter = adapter
    }
}
