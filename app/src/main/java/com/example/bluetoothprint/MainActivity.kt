package com.example.bluetoothprint

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
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
import com.squareup.picasso.Picasso

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
        printables.add(TextPrintable.Builder().setText("Hllow wolrd")
            .setLineSpacing(DefaultPrinter.LINE_SPACING_60).setAlignment(DefaultPrinter
                .ALIGNMENT_CENTER).setEmphasizedMode(DefaultPrinter.EMPHASIZED_MODE_BOLD)
            .setUnderlined(DefaultPrinter.UNDERLINED_MODE_ON).setNewLinesAfter(1).build())

        printing!!.print(printables)
    }

    private fun printImage() {
        val printables = ArrayList<Printable>()

//        load bitmap from internet
        Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/" +
                "6/64/Android_logo_2019_%28stacked%29.svg")
            .into(object:Target {
                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
            }
                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?){
                }
                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?){
                    printables.add(bitmap!!)
                printing.print(printables)
                }

            )

    }

    private fun changePairAndUnpair() {
        if (Printooth.hasPairedPrinter())
            btnPairUnpair.text = "Unpair ${Printooth.getPairedPrinter()!!.name}"

        else
            btnPairUnpair.text = "Pair with Printer"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER &&
                resultCode == Activity.RESULT_OK)
            initPrinting()
            changePairAndUnpair()
    }

    private fun initPrinting() {
        if (Printooth.hasPairedPrinter())
            if (printing != null) {
                printing!!.printingCallback = this
            }

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