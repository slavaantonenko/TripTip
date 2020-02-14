package com.marsapps.triptip.travel

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.marsapps.triptip.R
import com.marsapps.triptip.country.TravelCountryActivity
import com.marsapps.triptip.main.Constants.Companion.Bundles.ADDED_TO_TRAVEL_LIST
import com.marsapps.triptip.main.Constants.Companion.Extras.COUNTRY_EXTRA
import com.marsapps.triptip.main.Constants.Companion.Preferences.TRAVEL_LIST_CHANGED
import com.marsapps.triptip.main.CountryViewModel
import com.marsapps.triptip.main.interfaces.OnCountryClickListener
import com.marsapps.triptip.main.saveBooleanToCache
import com.marsapps.triptip.model.CountryModel
import kotlinx.android.synthetic.main.fragment_travel_list.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TravelListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TravelListFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class TravelListFragment : Fragment(), OnCountryClickListener {

    private lateinit var adapter: TravelListAdapter
    private lateinit var countryViewModel: CountryViewModel

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment TravelListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() = TravelListFragment().apply { arguments = Bundle().apply {} }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_travel_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvTravelList.layoutManager = LinearLayoutManager(context)

        adapter = TravelListAdapter(context!!, this)
        rvTravelList.adapter = adapter

        countryViewModel = ViewModelProviders.of(this).get(CountryViewModel::class.java)
        countryViewModel.getTravelListCountries()?.observe(this, Observer<List<CountryModel>> { countries ->
            
            if (countries != null && countries.isEmpty())
                tvEmptyList.visibility = View.VISIBLE
            else if (tvEmptyList.visibility != View.GONE)
                tvEmptyList.visibility = View.GONE
            
            adapter.setCountries(countries)
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_filter_countries).isVisible = false
    }

    override fun onCountryClick(view: View?, position: Int) {
        val country: CountryModel = adapter.getItem(position)

        when (view?.id) {
            R.id.ivBeenThere -> {
                country.beenThere = !country.beenThere
                countryViewModel.updateBeenThere(country)
                return
            }
            R.id.ivRemoveFromTravelList -> {
                TRAVEL_LIST_CHANGED.saveBooleanToCache(context, true)
                country.isInTravelList = false
                countryViewModel.updateIsInTravelList(country)
                return
            }
        }

        val intent = Intent(activity, TravelCountryActivity::class.java)
        intent.putExtra(COUNTRY_EXTRA, country)
        startActivityForResult(intent, ADDED_TO_TRAVEL_LIST)
    }
}
