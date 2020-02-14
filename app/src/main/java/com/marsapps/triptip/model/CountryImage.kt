package com.marsapps.triptip.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CountryImage {
    @SerializedName("height")
    @Expose
    val height: Int = 0
    @SerializedName("html_attributions")
    @Expose
    val htmlAttributions: List<String> = arrayListOf()
    @SerializedName("photo_reference")
    @Expose
    val photoReference: String = ""
    @SerializedName("width")
    @Expose
    val width: Int = 0
//    @SerializedName("largeImageURL")
//    @Expose
//    val largeImageURL: String = ""
//    @SerializedName("webformatHeight")
//    @Expose
//    val webformatHeight: Int = 0
//    @SerializedName("webformatWidth")
//    @Expose
//    val webformatWidth: Int = 0
//    @SerializedName("likes")
//    @Expose
//    val likes: Int = 0
//    @SerializedName("imageWidth")
//    @Expose
//    val imageWidth: Int = 0
//    @SerializedName("id")
//    @Expose
//    val id: Int = 0
//    @SerializedName("user_id")
//    @Expose
//    val userId: Int = 0
//    @SerializedName("views")
//    @Expose
//    val views: Int = 0
//    @SerializedName("comments")
//    @Expose
//    val comments: Int = 0
//    @SerializedName("pageURL")
//    @Expose
//    val pageURL: String = ""
//    @SerializedName("imageHeight")
//    @Expose
//    val imageHeight: Int = 0
//    @SerializedName("webformatURL")
//    @Expose
//    val webformatURL: String = ""
//    @SerializedName("type")
//    @Expose
//    val type: String = ""
//    @SerializedName("previewHeight")
//    @Expose
//    val previewHeight: Int = 0
//    @SerializedName("tags")
//    @Expose
//    val tags: String = ""
//    @SerializedName("downloads")
//    @Expose
//    val downloads: Int = 0
//    @SerializedName("user")
//    @Expose
//    val user: String = ""
//    @SerializedName("favorites")
//    @Expose
//    val favorites: Int = 0
//    @SerializedName("imageSize")
//    @Expose
//    val imageSize: Int = 0
//    @SerializedName("previewWidth")
//    @Expose
//    val previewWidth: Int = 0
//    @SerializedName("userImageURL")
//    @Expose
//    val userImageURL: String = ""
//    @SerializedName("previewURL")
//    @Expose
//    val previewURL: String = ""
}