package com.example.mapdungeon

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mapdungeon.location.AddressAPIRepository
import com.example.mapdungeon.model.Mission
import com.example.mapdungeon.util.Result

import kotlinx.coroutines.launch


class   MissionViewModel(private val addressAPIRepository: AddressAPIRepository) : ViewModel() {
    var mission = Mission()
    fun judge(
        latitude: Double,
        longitude: Double
    ): Boolean {
        var ret = false
        viewModelScope.launch {
            when (val res = addressAPIRepository.getAddress(latitude, longitude)) {
                is Result.Success -> {
                    ret = mission.updateIsCompleted(res.data)
                }
                else -> {

                }
            }
        }
        // TODO: LiveData or Kotlin Flow
        return ret
    }
}