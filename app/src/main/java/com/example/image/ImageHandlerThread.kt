package com.example.image


import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.os.Message
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class ImageHandlerThread internal constructor(private val uiHandler: MainActivity.UiHandler, handlerName : String) : HandlerThread(handlerName) {

    private var handler: Handler? = null
    private lateinit var request : Request
    private lateinit var response: Response
    private lateinit var bitmap: Bitmap
    private val okhttpClient = OkHttpClient()

    override fun onLooperPrepared() {
        super.onLooperPrepared()
        handler = getHandler(looper)
    }

    private fun getHandler(looper : Looper) : Handler {

        return object : Handler(looper) {

            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)
                val msgImage = Message()

                when(msg.what) {
                    AppConstants.IMAGE_ONE_CODE -> {
                        request = Request.Builder().url(AppConstants.IMAGE_ONE_URL).build()
                    }
                    AppConstants.IMAGE_TWO_CODE -> {
                        request = Request.Builder().url(AppConstants.IMAGE_TWO_URL).build()
                    }
                }

                response = okhttpClient.newCall(request).execute()
                bitmap = BitmapFactory.decodeStream(response.body()?.byteStream())
                msgImage.obj = bitmap
                uiHandler.sendMessage(msgImage)
            }

        }
    }

    fun loadImage(code : Int) {
        val msg = Message()
        msg.what = code
        handler?.sendMessage(msg)
    }
}
