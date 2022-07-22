package com.example.mapdungeon.location

import android.os.AsyncTask
import android.util.Log
import android.util.Xml
import com.example.mapdungeon.cityname.Address
import com.example.mapdungeon.cityname.missionFirstKana
import com.example.mapdungeon.databinding.ActivityJudgeBinding
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class Http : AsyncTask<HttpRequestDataset, Void, HttpRequestDataset>() {
    var address: Address? = null
    fun getAddressName(dataset: HttpRequestDataset): HttpRequestDataset? {
        var con: HttpURLConnection;
        try {
            val url: URL =
                URL("http://geoapi.heartrails.com/api/xml?method=searchByGeoLocation&x=${dataset.getY()}&y=${dataset.getX()}")
//            Log.d("debug", "http://geoapi.heartrails.com/api/xml?method=searchByGeoLocation&x=$x&y=$y")
            con = url.openConnection() as HttpURLConnection
            con.requestMethod = "GET"
            con.instanceFollowRedirects = false
            con.doInput = true
            con.connectTimeout = 100000
            con.connect()

            val input: InputStream = con.inputStream
            val address = parseResXml(input)
            input.close()

            this.address = address

            if (address != null) {
                dataset.setCityName(address.prefecture + address.city + address.town)
            }
            return dataset
        } catch (e: Exception) {
            Log.d("error", e.stackTraceToString())
        }
        return null
    }

    private fun parseResXml(input: InputStream): Address? {
        try {
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(input, "UTF-8")

            var isLast: Boolean = false

            var eventType: Int = parser.eventType;
//            Log.d("debug", "${eventType != XmlPullParser.END_DOCUMENT}")

            var prefecture = "";
            var city = "";
            var cityKana = "";
            var town = "";
            var townKana = "";
            var postal = ""
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val name: String? = parser.name
//                Log.d("debug", "${name} ${eventType}")
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

            return Address(prefecture, city, cityKana, town, townKana, postal)
        } catch (e: Exception) {
            Log.d("error", e.stackTraceToString())
        }
        return null
    }

    override fun doInBackground(vararg params: HttpRequestDataset?): HttpRequestDataset? {
        return getAddressName(params[0]!!)
    }

    override fun onPostExecute(result: HttpRequestDataset?) {
        if (result!!.getBinding() != null) { // NOTE: 画面更新処理
            if (result.getBinding() is ActivityJudgeBinding) { // NOTE: JudgeActivityの画面更新処理
                (result.getBinding() as ActivityJudgeBinding).judgeText.text =
                    if (address?.firstKana == missionFirstKana) {
                        // TODO: use ViewModel
                        "「${missionFirstKana}」から始まる\n市区町村に\n到着しました！"
                    } else {
                        // TODO: use ViewModel
                        "「${missionFirstKana}」から始まる\n市区町村に\n到着していません\n現在の頭文字: ${address?.firstKana ?: '?'}"
                    }

                (result.getBinding() as ActivityJudgeBinding).cityText.text = address?.str ?: "住所不明"
            }
        }
    }
}