package com.yannickpulver.plans.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlaceDto(
    @SerialName("result") val place: Place,
    val status: String
)

@Serializable
data class Place(
    val address_components: List<AddressComponent> = listOf(),
    val formatted_address: String,
    val geometry: Geometry,
    val icon: String,
    val icon_background_color: String,
    val icon_mask_base_uri: String,
    val name: String,
    val opening_hours: OpeningHours? = null,
    val photos: List<Photo>? = null,
    val photoUrls: List<String> = listOf(),
    @SerialName("place_id") val id: String,
    val plus_code: PlusCode? = null,
    val rating: Double? = null,
    val reference: String,
    val types: List<String>? = null,
    val url: String,
    val website: String? = null
) {
    val city = address_components.find { it.types.contains("locality") }

    companion object {
        val Preview = Place(
            formatted_address = "Zürich, Schweiz",
            geometry = Geometry(
                location = Location(
                    lat = 47.3768866,
                    lng = 8.541694
                ),
                viewport = Viewport(
                    northeast = Location(
                        lat = 47.606186,
                        lng = 8.573863
                    ),
                    southwest = Location(
                        lat = 47.320229,
                        lng = 8.466199
                    )
                )
            ),
            icon = "https://maps.gstatic.com/mapfiles/place_api/icons/v1/png_71/geocode-71.png",
            icon_background_color = "#7B9EB0",
            icon_mask_base_uri = "https://maps.gstatic.com/mapfiles/place_api/icons/v2/generic_pinlet",
            name = "Zürich",
            photos = listOf(),
            id = "ChIJGaK-SZcLkEcRA9wf5_GNbuY",
            plus_code = PlusCode(
                compound_code = "9F2J+XH Zürich, Schweiz",
                global_code = "8FVC9F2J+XH"
            ),
            rating = 4.5,
            reference = "ChIJGaK-SZcLkEcRA9wf5_GNbuY",
            types = listOf("locality", "political"),
            url = "https://maps.google.com/?q=Z%C3%BCrich,+Schweiz&ftid=0x47900b2b000e5e5b:0x421200e0c7b6b70",
            address_components = listOf()
        )
    }
}

@Serializable
data class AddressComponent(
    val long_name: String,
    val short_name: String,
    val types: List<String>
)

@Serializable
data class Geometry(
    val location: Location,
    val viewport: Viewport
)

@Serializable
data class Location(
    val lat: Double,
    val lng: Double
)

@Serializable
data class Viewport(
    val northeast: Location,
    val southwest: Location
)

@Serializable
data class OpeningHours(
    val open_now: Boolean,
    val periods: List<Period>,
    val weekday_text: List<String>
)

@Serializable
data class Period(
    val close: CloseOpen,
    val open: CloseOpen
)

@Serializable
data class CloseOpen(
    val day: Int,
    val time: String
)

@Serializable
data class Photo(
    val height: Int,
    val html_attributions: List<String>,
    val photo_reference: String,
    val width: Int
)

@Serializable
data class PlusCode(
    val compound_code: String,
    val global_code: String
)
