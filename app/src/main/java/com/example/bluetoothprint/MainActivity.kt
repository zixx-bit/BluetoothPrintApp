package com.example.bluetoothprint

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.ui.ScanningActivity
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

            btnPairUnpair.setOnClickListener{
                if (Printooth.hasPairedPrinter())
                    Printooth.removeCurrentPrinter()
                else{
                    startActivityForResult(Intent(this@MainActivity, ScanningActivity::class.java),
                        ScanningActivity.SCANNING_FOR_PRINTER)
                    changePairAndUnpair()
                }
            }

    }

    private fun changePairAndUnpair() {
        if (Printooth.hasPairedPrinter())
            btnPairUnPair.text = "Unpair ${Printooth.getPairedPrinter()!!.name}"
        else
            btnPairUnpair.text = "Pair with Printer"
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