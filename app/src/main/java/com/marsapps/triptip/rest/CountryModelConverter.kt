package com.marsapps.triptip.rest

import com.marsapps.triptip.main.Constants.REST.BASE_FLAGS_URL
import com.marsapps.triptip.model.Country
import com.marsapps.triptip.model.CountryModel
import com.marsapps.triptip.model.Currency
import com.marsapps.triptip.model.Language
import java.util.*

class CountryModelConverter {

    companion object {
        fun convertResult(countries: List<Country>): ArrayList<CountryModel>? {
            val result = ArrayList<CountryModel>()
            for (country in countries) {
                if (country.latlng.size == 2)
                    result.add(
                        CountryModel(
                            0,
                            removeBracketStringFromName(country.name),
                            country.nativeName,
                            country.alpha2Code,
                            country.alpha3Code,
                            country.callingCodes[0],
                            country.capital,
                            country.region,
                            country.population,
                            country.latlng[0],
                            country.latlng[1],
                            country.area,
                            country.timezones[0],
                            getCurrenciesFromList(country.currencies),
                            getLanguages(country.languages, false),
                            getLanguages(country.languages, true),
                            BASE_FLAGS_URL + country.alpha2Code + "/flat/64.png"))
            }

            return result
        }

        /**
         * This method builds a string of all country currency.
         * @param currenciesList
         * @return
         */
        private fun getCurrenciesFromList(currenciesList: List<Currency>): String {
            val currencies = StringBuilder()

            for (i in currenciesList.indices) {
                val currency = currenciesList[i]
                currencies.append(currency.name + " (" + currency.symbol + ")" + " (" + currency.code + ")")

                if (i < currenciesList.size - 1)
                    currencies.append(", ")
            }

            return currencies.toString()
        }

        /**
         * This method builds a string of all country language.
         * @param languagesList
         * @param nativeName if false build only language names otherwise only native names.
         * @return
         */
        private fun getLanguages(languagesList: List<Language>, nativeName: Boolean): String {
            val languages = StringBuilder()

            for (i in languagesList.indices) {
                val language = languagesList[i]

                if (!nativeName)
                    languages.append(language.name)
                else
                    languages.append(language.nativeName)

                if (i < languagesList.size - 1)
                    languages.append(", ")
            }

            return languages.toString()
        }

        /**
         * This method removes string which is between brackets in a name.
         * @param name full country name.
         * @return
         */
        private fun removeBracketStringFromName(name: String): String {
            return if (name.contains("(") && name.contains(")")) {
                val startIndex = name.indexOf("(")
                val endIndex = name.indexOf(")")
                val toBeReplaced = name.substring(startIndex, endIndex + 1)

                name.replace(toBeReplaced, "")
            } else
                name
        }
    }
}