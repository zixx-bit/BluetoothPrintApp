package com.example.bluetoothprint

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.data.printable.Printable
import com.mazenrashed.printooth.data.printable.RawPrintable
import com.mazenrashed.printooth.data.printable.TextPrintable
import com.mazenrashed.printooth.data.printer.DefaultPrinter
import com.mazenrashed.printooth.ui.ScanningActivity
import com.mazenrashed.printooth.utilities.Printing
import com.mazenrashed.printooth.utilities.PrintingCallback

class MainActivity : AppCompatActivity(), PrintingCallback {

    internal var printing:Printing ?=null
    private val btnPairUnpair: Button = findViewById(R.id.btnPairUnpair)
    private val btnPrintImages: Button = findViewById(R.id.btnPrintImage)
    private val btnPrint: Button = findViewById(R.id.btnPrint)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initview()

    }

    private fun initview() {
        if (printing != null) printing!!.printingCallback = this

            btnPrintImages.setOnClickListener(){
                if (!Printooth.hasPairedPrinter())
                    startActivityForResult(Intent(this@MainActivity,ScanningActivity::class.java),
                    ScanningActivity.SCANNING_FOR_PRINTER)
                else
                    printImage()
            }

            btnPrint.setOnClickListener {
                if (!Printooth.hasPairedPrinter())
                    startActivityForResult(Intent(this@MainActivity,ScanningActivity::class.java),
                    ScanningActivity.SCANNING_FOR_PRINTER)
                else
                    printText()
            }

    }

    private fun printText() {
        val printables = ArrayList<Printable>()
        printables.add(RawPrintable.Builder(byteArrayOf(27,100,4)).build())

//        Add text
        printables.add(TextPrintable.Builder().setText("hellow ")
            .setCharacterCode(DefaultPrinter.CHARCODE_PC1252).setNewLinesAfter(1).build())

//        custom text
    }

    private fun printImage() {
        TODO("Not yet implemented")
    }

    private fun changePairAndUnpair() {
        if (Printooth.hasPairedPrinter())
            btnPairUnpair.text = "Unpair ${Printooth.getPairedPrinter()!!.name}"

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