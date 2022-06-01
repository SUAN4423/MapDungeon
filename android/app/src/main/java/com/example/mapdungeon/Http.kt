package com.example.mapdungeon

import android.os.AsyncTask
import android.util.Log
import android.util.Xml
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class Http : AsyncTask<Double, Void, String>() {
    public fun getAddressName(y: Double, x: Double): String? {
        var con: HttpURLConnection;
        try {
            val url: URL =
                URL("http://geoapi.heartrails.com/api/xml?method=searchByGeoLocation&x=$x&y=$y")
//            Log.d("debug", "http://geoapi.heartrails.com/api/xml?method=searchByGeoLocation&x=$x&y=$y")
            con = url.openConnection() as HttpURLConnection
            con.requestMethod = "GET"
            con.instanceFollowRedirects = false
            con.doInput = true
            con.connect()

            val input: InputStream = con.inputStream
            val address: HashMap<String, String>? = parseResXml(input)
            addressMap = address
//            Log.d("debug", address + " debug")
            input.close()
            if (address != null)
                return address["prefecture"] + address["city"] + address["town"]
            else
                return null
        } catch (e: Exception) {
            Log.d("error", e.stackTraceToString())
        }
        return null
    }

    private fun parseResXml(input: InputStream): HashMap<String, String>? {
        try {
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(input, "UTF-8")

            var address: HashMap<String, String> = HashMap()
            var isLast: Boolean = false

            var eventType: Int = parser.eventType;
//            Log.d("debug", "${eventType != XmlPullParser.END_DOCUMENT}")
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val name: String? = parser.name
//                Log.d("debug", "${name} ${eventType}")
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if ("city" == name) {
                            address["city"] = parser.nextText()
                        } else if ("city-kana" == name) {
                            address["city-kana"] = parser.nextText()
                        } else if ("town" == name) {
                            address["town"] = parser.nextText()
                        } else if ("town-kana" == name) {
                            address["town-kana"] = parser.nextText()
                        } else if ("prefecture" == name) {
                            address["prefecture"] = parser.nextText()
                        } else if ("postal" == name) {
                            address["postal"] = parser.nextText()
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
            return address //address["prefecture"] + address["city"] + address["town"]
        } catch (e: Exception) {
            Log.d("error", e.stackTraceToString())
        }
        return null
    }

    override fun doInBackground(vararg params: Double?): String? {
        return getAddressName(params[0]!!, params[1]!!)
    }
}