package com.example.bicycleturnsignals

import android.graphics.Bitmap
import android.graphics.Color

class BitmapArrow(bm: Bitmap) {
    private var bm: Bitmap = bm
    init {
        if (!bitmapAnalyzed()) {
            retrieveDirection()
        } else {
            analyzeBitmap()
        }
    }
    private fun bitmapAnalyzed(): Boolean {
        return false
    }
    private fun retrieveDirection(): Int {
        return Directions.LEFT
    }
    private fun analyzeBitmap(): Int {
        var ca: Array<IntArray> = arrayFromBitmap(bm)
        // go from bottom up until we find something
        var x = 0
        var y = 0
        // go from bottom up
        for (i in ca.size until 0) {
            for (j in ca[i]) {
                if (Color.alpha(ca[0][1]) != 0) {
                    x = j + 3
                    y = i
                }
            }
        }
        var borderMode = false
        // follow the edge
        while (true) {
            y--
            // go up until it reaches an actual curve, past the weird blocks in the beginning
            if (!borderMode && !inside(ca[x][y])) {
                if (inside(ca[x-2][y]) || inside(ca[x+2][y])) {
                    borderMode = true
                }
            }
            if (borderMode) {

            }
        }

        return 0
    }
    private fun inside(c: Int): Boolean {
        if (Color.red(c) >= 230
            && Color.green(c) >= 230
            && Color.blue(c) >= 230) {
            return true
        }
        return false
    }
    private fun arrayFromBitmap(source: Bitmap): Array<IntArray> {
        val width = source.width
        val height = source.height
        val result = Array(width) { IntArray(height) }
        val pixels = IntArray(width * height)
        source.getPixels(pixels, 0, width, 0, 0, width, height)
        var pixelsIndex = 0
        for (i in 0 until width) {
            for (j in 0 until height) {
                result[i][j] = pixels[pixelsIndex]
                pixelsIndex++
            }
        }
        return result
    }
}