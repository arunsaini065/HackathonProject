package com.serhat.autosub

import android.app.Activity
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class ResultLauncher(var fragment: Fragment?,var activity : Activity?) {

    init {
       registerLauncher()
    }

    private var resultLauncher: ActivityResultLauncher<Intent>? = null

    abstract fun onLauncherResult(result: Intent)

    fun launchByLauncher(intent: Intent){
            resultLauncher?.launch(intent)
    }

    private fun registerLauncher(){
        resultLauncher = fragment?.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.let { onLauncherResult(it) }
        }
        if(activity != null && activity is AppCompatActivity) {
            resultLauncher = (activity as AppCompatActivity).registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                result.data?.let { onLauncherResult(it) }
            }
        }
    }

}