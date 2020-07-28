package com.example.newsapp.ui

import android.graphics.Paint
import android.graphics.Rect
import android.text.Html
import android.view.View
import android.widget.TextView
import androidx.core.text.HtmlCompat


fun TextView.setEllipsizedSuffixWithLink(maxLines: Int, suffix: String, link:String){
    addOnLayoutChangeListener(object : View.OnLayoutChangeListener{
        override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom:Int) {
            val allText = text.toString()
            var newText = allText
            val tvWidth = width
            val textSize = textSize

            if( !textHasEllipsized(newText, tvWidth, textSize,maxLines)) return

            while ( textHasEllipsized(newText, tvWidth, textSize,maxLines) ){
                newText = newText.substring(0, newText.length - 1).trim()
            }
            val endIndex = newText.length - suffix.length - 10
            if(endIndex > 0) {
                newText = newText.substring(0, endIndex).trim()
            }
            val s = HtmlCompat.fromHtml("$newText...<a href='$link'>$suffix</a>",HtmlCompat.FROM_HTML_MODE_LEGACY)
            text =s


            removeOnLayoutChangeListener(this)
        }
    })
}

private fun textHasEllipsized(text: String, tvWidth: Int, textSize: Float, maxLines: Int): Boolean {
    val paint = Paint()
    paint.textSize = textSize
    val size = paint.measureText(text).toInt()
    return size >= tvWidth * maxLines
}


 fun getTextWidth(text: String, textSize: Float): Int {
    val bounds = Rect()
    val paint = Paint()
    paint.textSize = textSize
    paint.getTextBounds(text, 0, text.length, bounds)
    return Math.ceil(bounds.width().toDouble()).toInt()
}