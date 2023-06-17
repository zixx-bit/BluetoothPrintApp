package com.example.bluetoothprint

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import android.widget.Toast
import com.mazenrashed.printooth.Printooth
import com.mazenrashed.printooth.data.printable.Printable
import com.mazenrashed.printooth.data.printable.RawPrintable
import com.mazenrashed.printooth.data.printable.TextPrintable
import com.mazenrashed.printooth.data.printer.DefaultPrinter
import com.mazenrashed.printooth.ui.ScanningActivity
import com.mazenrashed.printooth.utilities.Printing
import com.mazenrashed.printooth.utilities.PrintingCallback
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), PrintingCallback {
    internal var printing: Printing ?=null;
   lateinit var  btnPairUnpair :AppCompatButton
   lateinit var  btnPrint:AppCompatButton
    lateinit var  btnPrintImage :AppCompatButton

    private val REQUEST_BLUETOOTH_PERMISSION = 1



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnPairUnpair = findViewById(R.id.btnPairUnpair)
        btnPrint = findViewById(R.id.btnPrint)
        btnPrintImage = findViewById(R.id.btnPrintImage)







        initView()
    }
    private fun initView() {

        // Request Bluetooth permission if not granted
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH), REQUEST_BLUETOOTH_PERMISSION)
        } else {
            initPrinting()
        }
        // ...

        if (printing!= null)
            printing!!.printingCallback = this

        btnPairUnpair.setOnClickListener {
                    if (Printooth.hasPairedPrinter())
                        Printooth.removeCurrentPrinter()
                     else{
                        startActivityForResult(Intent(this@MainActivity, ScanningActivity::class.java),
                        ScanningActivity.SCANNING_FOR_PRINTER)
                        changePairAndUnpair()
                    }
                }

//            btnPrintImages.setOnClickListener(){
//                if (!Printooth.hasPairedPrinter())
//                    startActivityForResult(Intent(this@MainActivity,ScanningActivity::class.java),
//                    ScanningActivity.SCANNING_FOR_PRINTER)
//                else
//                    printImage()
//            }

        btnPrint.setOnClickListener {
                if (!Printooth.hasPairedPrinter())
                    startActivityForResult(Intent(this@MainActivity,ScanningActivity::class.java),
                    ScanningActivity.SCANNING_FOR_PRINTER)
                else
                    printText()
            }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
          ) {
        if (requestCode == REQUEST_BLUETOOTH_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initPrinting()
            } else {
                // Bluetooth permission denied, handle accordingly
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun printText() {

        val supplierName = findViewById<EditText>(R.id.suppliersName)
        val identityNumber = findViewById<EditText>(R.id.IdNumber)
        val route = findViewById<EditText>(R.id.route)
        val clerk = findViewById<EditText>(R.id.clerkName)
        val weightLitres = findViewById<EditText>(R.id.weightLitres)

        val printables = ArrayList<Printable>()
        printables.add(RawPrintable.Builder(byteArrayOf(27,100,4)).build())

        val supplier = supplierName.text
        val identity = identityNumber.text
        val routeName = route.text
        val clerkName = clerk.text
        val weight = weightLitres.text

        //        get cuurent time
        val currentdateTimeTv = findViewById<TextView>(R.id.date)
        val currentDateTime = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault()).format(Date())
        currentdateTimeTv.text = currentDateTime


//        Add text
        printables.add(TextPrintable.Builder().setText("Date: $currentDateTime \n" +
                "Litres in weight: $weight \n" +
                "Supplier: $supplier \n" +
                "ID number: $identity \n" +
                "Route: $routeName \n" +
                "Clerk Name: $clerkName\n")
            .setCharacterCode(DefaultPrinter.CHARCODE_PC1252).setNewLinesAfter(1).build())

//        custom text
        printables.add(TextPrintable.Builder().setText("")
            .setLineSpacing(DefaultPrinter.LINE_SPACING_60)
            .setAlignment(DefaultPrinter.ALIGNMENT_CENTER)
            .setEmphasizedMode(DefaultPrinter.EMPHASIZED_MODE_BOLD)
            .setUnderlined(DefaultPrinter.UNDERLINED_MODE_ON)
            .setNewLinesAfter(1)
            .build())

        printing!!.print(printables)
    }

//    private fun printImage() {
//        val printables = ArrayList<Printable>()
//
////        load bitmap from internet
//        Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/" +
//                "6/64/Android_logo_2019_%28stacked%29.svg")
//            .into(object:Target {
//                override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
//            }
//                override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?){
//                }
//                override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?){
//                    printables.add(bitmap!!)
//                printing.print(printables)
//                }
//
//            )
//
//    }

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
            initPrinting();
            changePairAndUnpair()
     }

    private fun initPrinting() {
        if (Printooth.hasPairedPrinter())
          printing = Printooth.printer()
        if (printing!=null)
            printing!!.printingCallback =this
    }

    override fun connectingWithPrinter() {
     Toast.makeText(this,"connecting to printer", Toast.LENGTH_SHORT).show()
    }

    override fun connectionFailed(error: String) {
        Toast.makeText(this, "Failed: $error", Toast.LENGTH_SHORT).show()
    }

    override fun onError(error: String) {
     Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
    }

    override fun onMessage(message: String) {
    Toast.makeText(this, "Message: $message", Toast.LENGTH_SHORT).show()
    }

    override fun printingOrderSentSuccessfully() {
        Toast.makeText(this,"Order sent to printer", Toast.LENGTH_SHORT).show()
    }
}