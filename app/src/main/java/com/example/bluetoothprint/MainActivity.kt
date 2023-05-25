package com.example.bluetoothprint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mazenrashed.printooth.utilities.Printing
import com.mazenrashed.printooth.utilities.PrintingCallback

class MainActivity : AppCompatActivity(), PrintingCallback {

    internal var printing:Printing ?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initview()

    }

    private fun initview() {
        if (printing != null)
            printing!!.printingCallback = this


    }

    override fun connectingWithPrinter() {
        TODO("Not yet implemented")
    }

    override fun connectionFailed(error: String) {
        TODO("Not yet implemented")
    }

    override fun onError(error: String) {
        TODO("Not yet implemented")
    }

    override fun onMessage(message: String) {
        TODO("Not yet implemented")
    }

    override fun printingOrderSentSuccessfully() {
        TODO("Not yet implemented")
    }
}