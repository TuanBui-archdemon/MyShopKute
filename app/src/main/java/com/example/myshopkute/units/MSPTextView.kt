package com.example.myshopkute.units

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class MSPTextView (context: Context, attrs: AttributeSet): AppCompatTextView(context, attrs) {
    init {
        // call the function to aplly the font to the component.
        applyFont()
    }

    private fun applyFont() {
        // This is uesd to get the file from the assets folder and set it to title textview
        val typeface: Typeface =
            Typeface.createFromAsset(context.assets, "Montserrat-Regular.ttf")
        setTypeface(typeface)
    }
}