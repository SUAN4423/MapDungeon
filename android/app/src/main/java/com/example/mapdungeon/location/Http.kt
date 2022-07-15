package com.example.mapdungeon.location

import android.os.AsyncTask
import android.util.Log
import android.util.Xml
import androidx.appcompat.app.AppCompatActivity
import com.example.mapdungeon.cityname.AddressMap
import com.example.mapdungeon.cityname.Hiragana
import com.example.mapdungeon.databinding.ActivityJudgeBinding
import org.xmlpull.v1.XmlPullParser
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class Http : AsyncTask<HttpRequesetDataset, Void, HttpRequesetDataset>() {
    public fun getAddressName(dataset: HttpRequesetDataset): HttpRequesetDataset? {
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
            val address: AddressMap? = parseResXml(input)
            Hiragana.addressMap = address
//            Log.d("debug", address + " debug")
            input.close()
            if (address != null) {
                dataset.setCityName(address.prefecture + address.city + address.town)
//                return address["prefecture"] + address["city"] + address["town"]
            }
            return dataset
        } catch (e: Exception) {
            Log.d("error", e.stackTraceToString())
        }
        return null
    }

    private fun parseResXml(input: InputStream): AddressMap? {
        try {
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false)
            parser.setInput(input, "UTF-8")

            var isLast: Boolean = false

            var eventType: Int = parser.eventType;
//            Log.d("debug", "${eventType != XmlPullParser.END_DOCUMENT}")

            var prefecture = ""; var city = ""; var city_kana = ""; var town = ""; var town_kana = ""; var postal = ""
            while (eventType != XmlPullParser.END_DOCUMENT) {
                val name: String? = parser.name
//                Log.d("debug", "${name} ${eventType}")
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        if ("city" == name) {
                            city = parser.nextText()
                        } else if ("city-kana" == name) {
                            city_kana = parser.nextText()
                        } else if ("town" == name) {
                            town = parser.nextText()
                        } else if ("town-kana" == name) {
                            town_kana = parser.nextText()
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
            val address = AddressMap(prefecture, city, city_kana, town, town_kana, postal)
            return address //address["prefecture"] + address["city"] + address["town"]
        } catch (e: Exception) {
            Log.d("error", e.stackTraceToString())
        }
        return null
    }

    override fun doInBackground(vararg params: HttpRequesetDataset?): HttpRequesetDataset? {
        return getAddressName(params[0]!!)
    }

    override fun onPostExecute(result: HttpRequesetDataset?) {
        if (result!!.getBinding() != null) { // NOTE: 画面更新処理
            if (result.getBinding() is ActivityJudgeBinding) { // NOTE: JudgeActivityの画面更新処理
                val successCity: Boolean = Hiragana.checkLocation(result.getActivity() as AppCompatActivity)
                if (successCity)
                    (result.getBinding() as ActivityJudgeBinding).judgeText.text =
                        "「${Hiragana.getNowMission()}」の付く市区町村に\n到着しました！"
                else if (Hiragana.getFirstKana(result.getActivity() as AppCompatActivity) != null)
                    (result.getBinding() as ActivityJudgeBinding).judgeText.text =
                        "「${Hiragana.getNowMission()}」の付く市区町村に\n到着していません\n現在の頭文字: ${Hiragana.getFirstKana(result.getActivity() as AppCompatActivity)!!}"
                (result.getBinding() as ActivityJudgeBinding).cityText.text = Hiragana.getCityName()
            }
        }
    }
}