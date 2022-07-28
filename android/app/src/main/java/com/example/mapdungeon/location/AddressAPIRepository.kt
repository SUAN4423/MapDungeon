package com.example.mapdungeon.location

import android.util.Log
import android.util.Xml
import com.example.mapdungeon.cityname.AddressMap
import com.example.mapdungeon.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.xmlpull.v1.XmlPullParser
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.stream.Collectors


class AddressAPIRepository {
    suspend fun getAddress(
        latitude: Double,
        longitude: Double
    ): Result<AddressMap> {
        return withContext(Dispatchers.IO) {
            val url =
                URL("http://geoapi.heartrails.com/api/xml?method=searchByGeoLocation&x=${longitude}&y=${latitude}")
            val con = url.openConnection() as HttpURLConnection
            con.requestMethod = "GET"
            con.instanceFollowRedirects = false
            con.doInput = true
            con.connectTimeout = 100000

            try {
                con.connect()
                val input = con.inputStream

                val ret = parseResXml(input)
                input.close()
                Log.d("debug", "addressAPIRepository: " + ret.str)
                return@withContext Result.Success(ret)
            } catch (e: java.lang.Exception) {
                return@withContext Result.Error(e)
            }
        }
    }

    private fun parseResXml(input: InputStream): AddressMap {
        try {
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(input, "UTF-8")

            var isLast = false

            var eventType: Int = parser.eventType;

            var prefecture = "";
            var city = "";
            var cityKana = "";
            var town = "";
            var townKana = "";
            var postal = ""
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val name: String? = parser.name
                Log.d("debug", "${name} ${eventType}")
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if ("city" == name) {
                            city = parser.nextText()
                        } else if ("city-kana" == name) {
                            cityKana = parser.nextText()
                        } else if ("town" == name) {
                            town = parser.nextText()
                        } else if ("town-kana" == name) {
                            townKana = parser.nextText()
                        } else if ("prefecture" == name) {
                            prefecture = parser.nextText()
                        } else if ("postal" == name) {
                            postal = parser.nextText()
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if ("location" == name) {
                            isLast = true
                        }
                    }
                }
                if (isLast) {
                    break
                }
                eventType = parser.next()
            }
            isUsed = false

            return AddressMap(prefecture, city, cityKana, town, townKana, postal)
        } catch (e: Exception) {
            Log.d("error", e.stackTraceToString())
            throw e
        }
    }
}