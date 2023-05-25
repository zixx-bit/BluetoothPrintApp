package com.example.bluetoothprint

import android.app.Application
import com.mazenrashed.printooth.Printooth

class ApplicationClass: Application(){
    override fun onCreate() {
        super.onCreate()
        Printooth.init(this)
    }
}