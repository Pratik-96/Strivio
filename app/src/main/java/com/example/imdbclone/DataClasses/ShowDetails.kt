package com.example.imdbclone.DataClasses

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
@Parcelize
data class Show(
    val shows:List<ShowDetails>
):Parcelable

@Serializable
@Parcelize
data class ShowDetails(
    val itemType: String,
    val showType: String,
    val id: String,
    val title: String,
    val overview: String,
    val firstAirYear:Int?,
    val releaseYear:Int?,
    val genres:List<GenreDetails?>,
    val creators:List<String?>,
    val directors:List<String?>,
    val cast: List<String>,
    val rating: Int,
    val runtime:Int,
    val seasonCount: Int?,
    val episodeCount: Int?,
    val imageSet: VerticalPoster,
    val streamingOptions: StreamingOptionsInIndia?
):Parcelable

@Serializable
@Parcelize
data class GenreDetails(
    val id:String,
    val name: String
):Parcelable

@Serializable
@Parcelize
data class VerticalPoster(
    val verticalPoster: VerticalPosterDetails?,
    val verticalBackdrop: VerticalBackdropPosterDetails?,
    val horizontalPoster : HorizontalPosterDetails?,
    val horizontalBackdrop: HorizontalBackDropPosterDetails?
):Parcelable


@Serializable
@Parcelize
data class HorizontalBackDropPosterDetails(
    val w360:String,
    val w480:String,
    val w720:String,
):Parcelable

@Serializable
@Parcelize
data class VerticalPosterDetails(
    val w240:String,
    val w360:String,
    val w480:String,
    val w600:String,
    val w720:String

):Parcelable

@Serializable
@Parcelize
data class VerticalBackdropPosterDetails(
    val w240:String,
    val w360:String,
    val w480:String,
    val w600:String,
    val w720:String

):Parcelable

@Serializable
@Parcelize
data class HorizontalPosterDetails(
    val w360:String,
    val w480:String,
    val w720:String,
    val w1080:String,
    val w1440:String


):Parcelable

@Serializable
@Parcelize
data class StreamingOptionsInIndia(
    val `in`:List<ServiceMetaData?>
):Parcelable

@Serializable
@Parcelize
data class ServiceMetaData(
    val service:Service?=null,
    val type:String?=null,
    val link:String?=null,
    val videoLink:String?=null,
    val quality:String?=null
):Parcelable{
    constructor():this(null,null,null,null,null)
}

@Serializable
@Parcelize
data class Service(
    val id:String?=null,
    val name:String?=null,
    val homePage:String?=null,
    val imageSet:ServiceImageAsset?=null
):Parcelable{
    constructor():this(null,null,null,null)
}

@Serializable
@Parcelize
data class ServiceImageAsset(
    val lightThemeImage:String?=null,
    val darkThemeImage:String?=null
):Parcelable{
    constructor():this(null,null)
}


data class ShowResponse(val shows:List<ShowDetails>)

