package com.example.dlworkhelper

import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.annotation.ColorInt
import com.example.dlworkhelper.databinding.ActivityBarcodeGeneratorBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.oned.Code128Writer

class BarcodeGeneratorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBarcodeGeneratorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBarcodeGeneratorBinding.inflate(layoutInflater)

        with(binding){
            BarcodeGeneratorInputButton.setOnClickListener {
                val value = BarcodeGeneratorInputText.text.toString()
                if (value != "")
                    displayBitmap(value)
                else
                    Toast.makeText(this@BarcodeGeneratorActivity, "Введите штрихкод!",
                        Toast.LENGTH_SHORT).show()
            }
            BarcodeGeneratorButtonBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
            BarcodeGeneratorButtonsClear.setOnClickListener {
                BarcodeGeneratorInputText.text = Editable.Factory.getInstance()
                    .newEditable("")
            }
            BarcodeGeneratorButtonsWB.setOnClickListener {
                BarcodeGeneratorInputText.text = Editable.Factory.getInstance()
                    .newEditable("WB_")
            }
            BarcodeGeneratorButtonsWBGI.setOnClickListener {
                BarcodeGeneratorInputText.text = Editable.Factory.getInstance()
                    .newEditable("WB-GI-")
            }
        }
        setContentView(binding.root)
    }

    private fun createBarcodeBitmap(
        barcodeValue: String,
        @ColorInt barcodeColor: Int,
        @ColorInt backgroundColor: Int,
        widthPixels: Int,
        heightPixels: Int
    ): Bitmap {
        val bitMatrix = Code128Writer().encode(
            barcodeValue,
            BarcodeFormat.CODE_128,
            widthPixels,
            heightPixels
        )
        val pixels = IntArray(bitMatrix.width * bitMatrix.height)
        for (y in 0 until bitMatrix.height){
            val offset = y * bitMatrix.width
            for (x in 0 until bitMatrix.width) {
                pixels[offset + x] =
                    if (bitMatrix.get(x, y)) barcodeColor else backgroundColor
            }
        }

        val bitmap = Bitmap.createBitmap(
            bitMatrix.width,
            bitMatrix.height,
            Bitmap.Config.ARGB_8888
        )
        bitmap.setPixels(
            pixels,
            0,
            bitMatrix.width,
            0,
            0,
            bitMatrix.width,
            bitMatrix.height
        )
        return bitmap
    }

    private fun displayBitmap(value: String){
        with(binding){
            val widthPixels = 600
            val heightPixels = 300
            BarcodeGeneratorImage.setImageBitmap(
                createBarcodeBitmap(
                    barcodeValue = value,
                    barcodeColor = getColor(R.color.black),
                    backgroundColor = getColor(R.color.background),
                    widthPixels = widthPixels,
                    heightPixels = heightPixels
                )
            )
        }
    }
}