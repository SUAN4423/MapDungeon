package com.example.mapdungeon

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.OnRequestPermissionsResultCallback
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.example.mapdungeon.databinding.ActivityMapsBinding
import com.example.mapdungeon.global.GlobalData
import com.example.mapdungeon.judge.JudgeActivity
import com.example.mapdungeon.location.AddressAPIRepository
import com.example.mapdungeon.location.EXTRA_LATITUDE
import com.example.mapdungeon.location.EXTRA_LONGITUDE
import com.example.mapdungeon.location.Location
import com.example.mapdungeon.model.genBingo
import com.example.mapdungeon.util.Result

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.coroutines.launch
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, OnRequestPermissionsResultCallback {

    private lateinit var mapsBinding: ActivityMapsBinding
    private lateinit var mMap: GoogleMap
    private val LOCATION_PERMISSION_REQUEST_CODE: Int = 1001
    private lateinit var location: Location
    private var clearMissionNum = 0
    private val requestPermissions =
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mapsBinding = ActivityMapsBinding.inflate(layoutInflater)
        val view = mapsBinding.root
        setContentView(view)

        val missionTextViews: MutableList<TextView> = mutableListOf(
            mapsBinding.mission0,
            mapsBinding.mission1,
            mapsBinding.mission2,
            mapsBinding.mission3,
            mapsBinding.mission4,
            mapsBinding.mission5,
            mapsBinding.mission6,
            mapsBinding.mission7
        )

        fun updateScoreText () {
            mapsBinding.scoreText.text = "現在のスコア：${clearMissionNum * 100}点"
        }

        fun setMissionText () {
            for ((index, mission) in missionTextViews.withIndex()) {
                mission.text = GlobalData.bingo.missions[index].missionChar.toString()
            }
        }

        fun resetMission () {
            GlobalData.bingo = genBingo()
            setMissionText()

            for (mission in missionTextViews) {
                mission.setBackgroundColor(resources.getColor(R.color.unclear))
            }
            mapsBinding.missionCenter.setBackgroundColor(resources.getColor(R.color.clear))
        }

        val launcher: ActivityResultLauncher<Intent> =
            prepareCall(ActivityResultContracts.StartActivityForResult()) { activityResult ->
                val isClearList = GlobalData.bingo.getClearList() //Hiragana.getCurrentClear()

                for ((index, isClear) in isClearList.withIndex()) {
                    if (isClear) {
                        missionTextViews[index].setBackgroundColor(resources.getColor(R.color.clear))
                    }
                }

                val isBingo = bingoCheck(isClearList)

                if (isBingo) {
                    GlobalData.bingo.isClear = true
                    GlobalData.bingo.clearedAt = Date()

                    val builder: AlertDialog.Builder = AlertDialog.Builder(this)
                    builder.setMessage("ビンゴカードを取り替えますか？")
                        .setTitle("ビンゴしました！")
                        .setPositiveButton("はい", DialogInterface.OnClickListener { dialog, id ->
                            resetMission()
                            clearMissionNum++
                            updateScoreText()
                        })
                        .setNegativeButton("いいえ", DialogInterface.OnClickListener { dialog, id ->
                        })
                        .show()
                }
            }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        location = Location(this, this)

        mapsBinding.button4.setOnClickListener {
            Log.d("debug", "button clicked")
            toastLocation(latitude = location.latitude, longitude = location.longitude)
        }

        mapsBinding.judgeButton.setOnClickListener {
            val intent = Intent(this, JudgeActivity::class.java).apply {
                putExtra(EXTRA_LATITUDE, location.latitude)
                putExtra(EXTRA_LONGITUDE, location.longitude)
            }
            launcher.launch(intent)
        }

        mapsBinding.skipButton.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setMessage("クリア状況はリセットされます\nビンゴしていた場合、スコアが加算されます")
                .setTitle("ビンゴカードを取り替えますか？")
                .setPositiveButton("はい", DialogInterface.OnClickListener { dialog, id ->
                    if(GlobalData.bingo.isClear) {
                        clearMissionNum++
                        updateScoreText()
                    }
                    resetMission()
                })
                .setNegativeButton("いいえ", DialogInterface.OnClickListener { dialog, id ->
                })
                .show()
        }

        GlobalData.bingo = genBingo()
        setMissionText()

        updateScoreText()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun toastLocation(latitude: Double, longitude: Double) {
        lifecycleScope.launch {
            when (val res = AddressAPIRepository().getAddress(latitude, longitude)) {
                is Result.Success -> {
                    val address = res.data

                    Toast.makeText(
                        this@MapsActivity,
                        "緯度:$latitude, 経度:$longitude, ${res.data.str}",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> {
                    Toast.makeText(
                        this@MapsActivity,
                        "緯度:$latitude, 経度:$longitude, 住所取得失敗",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    fun bingoCheck(isClearList: List<Boolean>): Boolean {
        var isBingo: Boolean = false
        /*
         * * *
         - - -
         - - -
         */
        if (isClearList[0] && isClearList[1] && isClearList[2]) {
            mapsBinding.mission0.setBackgroundColor(resources.getColor(R.color.bingo))
            mapsBinding.mission1.setBackgroundColor(resources.getColor(R.color.bingo))
            mapsBinding.mission2.setBackgroundColor(resources.getColor(R.color.bingo))
            isBingo = true
        }
        /*
         - - -
         * * *
         - - -
         */
        if (isClearList[3] && isClearList[4]) {
            mapsBinding.mission3.setBackgroundColor(resources.getColor(R.color.bingo))
            mapsBinding.missionCenter.setBackgroundColor(resources.getColor(R.color.bingo))
            mapsBinding.mission4.setBackgroundColor(resources.getColor(R.color.bingo))
            isBingo = true
        }
        /*
         - - -
         - - -
         * * *
         */
        if (isClearList[5] && isClearList[6] && isClearList[7]) {
            mapsBinding.mission5.setBackgroundColor(resources.getColor(R.color.bingo))
            mapsBinding.mission6.setBackgroundColor(resources.getColor(R.color.bingo))
            mapsBinding.mission7.setBackgroundColor(resources.getColor(R.color.bingo))
            isBingo = true
        }
        /*
         * - -
         * - -
         * - -
         */
        if (isClearList[0] && isClearList[3] && isClearList[5]) {
            mapsBinding.mission0.setBackgroundColor(resources.getColor(R.color.bingo))
            mapsBinding.mission3.setBackgroundColor(resources.getColor(R.color.bingo))
            mapsBinding.mission5.setBackgroundColor(resources.getColor(R.color.bingo))
            isBingo = true
        }
        /*
         - * -
         - * -
         - * -
         */
        if (isClearList[1] && isClearList[6]) {
            mapsBinding.mission1.setBackgroundColor(resources.getColor(R.color.bingo))
            mapsBinding.missionCenter.setBackgroundColor(resources.getColor(R.color.bingo))
            mapsBinding.mission6.setBackgroundColor(resources.getColor(R.color.bingo))
            isBingo = true
        }
        /*
         - - *
         - - *
         - - *
         */
        if (isClearList[2] && isClearList[4] && isClearList[7]) {
            mapsBinding.mission2.setBackgroundColor(resources.getColor(R.color.bingo))
            mapsBinding.mission4.setBackgroundColor(resources.getColor(R.color.bingo))
            mapsBinding.mission7.setBackgroundColor(resources.getColor(R.color.bingo))
            isBingo = true
        }
        /*
         * - -
         - * -
         - - *
         */
        if (isClearList[0] && isClearList[7]) {
            mapsBinding.mission0.setBackgroundColor(resources.getColor(R.color.bingo))
            mapsBinding.missionCenter.setBackgroundColor(resources.getColor(R.color.bingo))
            mapsBinding.mission7.setBackgroundColor(resources.getColor(R.color.bingo))
            isBingo = true
        }
        /*
         - - *
         - * -
         * - -
         */
        if (isClearList[2] && isClearList[5]) {
            mapsBinding.mission2.setBackgroundColor(resources.getColor(R.color.bingo))
            mapsBinding.missionCenter.setBackgroundColor(resources.getColor(R.color.bingo))
            mapsBinding.mission5.setBackgroundColor(resources.getColor(R.color.bingo))
            isBingo = true
        }
        return isBingo
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= 23) {
                enableMyLocation()
            } else {
                showErrorDialog("位置情報権限エラー")
            }
        }
        mMap.isMyLocationEnabled = true
    }

    fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            return
        } else {
            requestPermissions(
                this,
                requestPermissions,
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return
        }
        grantResults.forEach {
            if (it != PackageManager.PERMISSION_GRANTED) {
                if (shouldShowRequestPermissionRationale(
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                ) {
                    Log.d("debug", "権限の再要求")
                    requestPermissions(
                        this,
                        requestPermissions,
                        LOCATION_PERMISSION_REQUEST_CODE
                    )
                } else {
                    Log.d("debug", "再要求不可、設定画面を開かせる")
                    showDialogToGetPermission()
                }
            }
        }
    }

    private fun showDialogToGetPermission() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("権限要求")
            .setMessage(
                "このアプリの動作には位置情報、インターネット接続権限の許可が必要です。"
            )

        builder.setPositiveButton("設定を開く") { dialogInterface, i ->
            val intent = Intent(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", packageName, null)
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        builder.setNegativeButton("アプリを閉じる") { dialogInterface, i ->
            this.finish()
        }
        val dialog = builder.create()
        dialog.show()
    }

    fun showErrorDialog(errorMessage: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("エラー")
            .setMessage(errorMessage)
        builder.setPositiveButton("OK") { dialogInterface, i ->
            this.finish()
        }
    }
}