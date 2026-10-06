package com.example.a25172012060_p7

import java.io.Serializable
import org.json.JSONObject

class Person(
    var id: String = "",
    var name: String = "",
    var emailId: String = "",
    var phoneNo: String = "",
    var address: String = "",
    var latitude: Double = 0.0,
    var longitute: Double = 0.0
) : Serializable {

    val longitude: Double
        get() = longitute

    constructor(jsonObject: JSONObject) : this(
        id = jsonObject.optString("id", jsonObject.optString("_id", "")),
        emailId = jsonObject.optString("email", jsonObject.optString("emailId", "")),
        phoneNo = jsonObject.optString("phone", jsonObject.optString("phoneNo", ""))
            .replace("}", "").trim(),
        name = if (jsonObject.has("profile")) {
            val profile = jsonObject.optJSONObject("profile")
            profile?.optString("name", "") ?: ""
        } else if (jsonObject.has("name") && jsonObject.get("name") is JSONObject) {
            val nameObj = jsonObject.getJSONObject("name")
            "${nameObj.optString("first", "")} ${nameObj.optString("last", "")}".trim()
        } else {
            jsonObject.optString("name", "")
        },
        address = if (jsonObject.has("profile")) {
            val profile = jsonObject.optJSONObject("profile")
            profile?.optString("address", "") ?: ""
        } else {
            jsonObject.optString("address", "")
        },
        latitude = if (jsonObject.has("profile")) {
            val location = jsonObject.optJSONObject("profile")?.optJSONObject("location")
            location?.optDouble("lat", 0.0) ?: 0.0
        } else {
            jsonObject.optDouble("latitude", jsonObject.optDouble("lat", 0.0))
        },
        longitute = if (jsonObject.has("profile")) {
            val location = jsonObject.optJSONObject("profile")?.optJSONObject("location")
            location?.optDouble("long", 0.0) ?: 0.0
        } else {
            jsonObject.optDouble("longitute", jsonObject.optDouble("longitude", jsonObject.optDouble("long", 0.0)))
        }
    )
}
