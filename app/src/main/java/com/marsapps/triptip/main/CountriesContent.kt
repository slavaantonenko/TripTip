package com.marsapps.triptip.main

import com.marsapps.triptip.model.CountryModel

class CountriesContent {

    companion object {
        val COUNTRIES: MutableList<CountryModel> = arrayListOf()

        fun clear() { COUNTRIES.clear() }
    }

    //
//    public static List<CountryModel> getTravelListCountries()
//    {
//        //TODO Enable This when upgrade to Java 1.8 and API 24.
////        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
////            return COUNTRIES.stream().filter(CountryModel::isInTravelList).collect(Collectors.toList());
////        else
////        {
//            List<CountryModel> countries = new ArrayList<>();
//            for (CountryModel country : COUNTRIES)
//                if (country.isInTravelList())
//                    countries.add(country);
//
//            return countries;
////        }
//    }
}