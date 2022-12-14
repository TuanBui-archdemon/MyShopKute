package com.example.myshopkute.units

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

class MSPRadioButton(context: Context, attributeSet: AttributeSet): AppCompatRadioButton(context, attributeSet) {
    init {
        applyFont()
    }
    private fun applyFont(){
        // This is uesd to get the file from the assets folder and set it to title textview
        val typeface: Typeface =
            Typeface.createFromAsset(context.assets, "Montserrat-Bold.ttf")
        setTypeface(typeface)
    }
}