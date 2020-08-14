package com.marsapps.triptip.travel

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.marsapps.triptip.R
import com.marsapps.triptip.main.interfaces.OnCountryClickListener
import com.marsapps.triptip.model.CountryModel
import kotlinx.android.synthetic.main.item_travel_list.view.*

class TravelListAdapter(val context: Context, val countryClickListener: OnCountryClickListener) :
    RecyclerView.Adapter<TravelListAdapter.ViewHolder>() {

    private var countries: List<CountryModel>? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private val ivCountryImage: ImageView = view.ivCountryImage
        val tvCountryName: TextView = view.tvTravelListCountry
        val ivRemoveFromTravelList: ImageView = view.ivRemoveFromTravelList
        val ivBeenThere: ImageView = view.ivBeenThere

        init {
            view.setOnClickListener(this)
            ivRemoveFromTravelList.setOnClickListener(this)
            ivBeenThere.setOnClickListener(this)
        }

        fun onBindViewHolder(country: CountryModel) {
            tvCountryName.text = country.name

            Glide.with(context).load(country.image).error(R.drawable.travel_default)
                .listener(object : RequestListener<Drawable> {

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                        tvCountryName.setTextColor(ContextCompat.getColor(context, R.color.atc_travel_been_there))
                        ivBeenThere.setImageDrawable(context.getDrawable(R.drawable.ic_flight_land))
                        ivRemoveFromTravelList.setImageDrawable(context.getDrawable(R.drawable.ic_delete_gray))
                        Log.e("Glide failed", "Failed load image for " + country.name + " " + e?.message)
                        return false
                    }

                    override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?,
                                                 dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        Log.i("Glide success", country.name + " downloaded image succeeded")
                        return false
                    }
                })
                .into(ivCountryImage)

            if (country.beenThere) {
                ivCountryImage.alpha = 0.5f
                tvCountryName.setTextColor(ContextCompat.getColor(context,
                    R.color.atc_travel_been_there
                ))
                ivBeenThere.setImageDrawable(context.getDrawable(R.drawable.ic_flight_land))
                ivRemoveFromTravelList.setImageDrawable(context.getDrawable(R.drawable.ic_delete_gray))
            }
            else {
                ivCountryImage.alpha = 1f
                tvCountryName.setTextColor(Color.WHITE)
                ivBeenThere.setImageDrawable(context.getDrawable(R.drawable.ic_flight_take_off))
                ivRemoveFromTravelList.setImageDrawable(context.getDrawable(R.drawable.ic_delete))
            }
        }

        override fun onClick(view: View?) {
            countryClickListener.onCountryClick(view, adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): ViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_travel_list, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBindViewHolder(countries!![position])
    }

    override fun getItemCount(): Int {
        return countries?.size ?: 0
    }

    fun setCountries(countries: List<CountryModel>) {
        this.countries = countries
        notifyDataSetChanged()
    }

    fun getItem(position: Int): CountryModel {
        return countries!![position]
    }
}