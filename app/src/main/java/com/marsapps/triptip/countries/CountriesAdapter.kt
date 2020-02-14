package com.marsapps.triptip.countries

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.gms.common.util.Predicate
import com.marsapps.triptip.R
import com.marsapps.triptip.main.Utils
import com.marsapps.triptip.main.interfaces.OnCountryClickListener
import com.marsapps.triptip.model.CountryModel
import kotlinx.android.synthetic.main.item_country.view.*
import java.util.*

class CountriesAdapter(val context: Context, items: MutableList<CountryModel>, val countryClickListener: OnCountryClickListener) :
    RecyclerView.Adapter<CountriesAdapter.ViewHolder>() {

    private val countries: MutableList<CountryModel> = items.map{ it.copy() }.toMutableList()
    private val originList: MutableList<CountryModel> = items.map{ it.copy() }.toMutableList() // This is for filter

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        private val ivFlag: ImageView = view.ivCountryFlagItem
        private val tvName: TextView = view.tvCountryName

        init {
            view.setOnClickListener(this)
        }

        fun onBindViewHolder(countryModel: CountryModel) {

            Glide.with(context).load(countryModel.flag).error(R.drawable.travel_default)
                .listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        Log.e("Glide failed", "Failed load flag for " + countryModel.name + " " + e?.message)
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?,
                                                 dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        Log.i("Glide success", countryModel.name + " downloaded flag succeeded")
                        return false
                    }
                })
                .into(ivFlag)

            tvName.text = countryModel.name
        }

        override fun onClick(view: View) {
            countryClickListener.onCountryClick(view, adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindViewHolder(countries[position])
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    fun setData(items: List<CountryModel>) {
        countries.clear()
        countries.addAll(items)
        notifyDataSetChanged()
    }

    fun getCountries(): List<CountryModel> {
        return countries
    }

    fun getItem(position: Int): CountryModel {
        return countries[position]
    }

    // Filter countries list according to searched text.
    fun filter(str: String) {
        countries.clear()
        countries.addAll(originList.filter { it.name.toLowerCase(Locale.getDefault()).startsWith(str) })
        notifyDataSetChanged()
    }

    @SuppressLint("StaticFieldLeak")
    fun updateTravelListCountries(travelListCountries: List<CountryModel>) {
        object : AsyncTask<Void, Void?, Void?>() {
            override fun onPreExecute() {}

            override fun doInBackground(vararg voids: Void): Void? {
                for (adapterCountry in countries) {

                    val isInList = Predicate<CountryModel> { countryModel ->
                            // binds a boolean method in User to a reference
                            countryModel.id == adapterCountry.id
                        }

                    val sameCountry: List<CountryModel> = Utils.filter(travelListCountries, isInList)

                    if (sameCountry.isNotEmpty()) {
                        adapterCountry.isInTravelList = true
                        adapterCountry.image = sameCountry[0].image
                    }
                    else {
                        adapterCountry.beenThere = false
                        adapterCountry.isInTravelList = false
                    }
                }
                Log.d("Background Task", "Finished update countries adapter!")
                return null
            }
        }.execute()
    }
}
