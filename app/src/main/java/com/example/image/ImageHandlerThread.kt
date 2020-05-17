package com.example.image

import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import androidx.appcompat.widget.AppCompatImageView

class ImageHandlerThread internal constructor(private val uiHandler: MainActivity.UiHandler) : HandlerThread("asdf") {
    private var handler: Handler? = null

    override fun onLooperPrepared() {
        super.onLooperPrepared()
        handler = getHandler()
    }

    private fun getHandler() : Handler {
        return object : Handler(looper) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)

                val msgA = Message()
                when(msg.what) {
                    1 -> {
                        msgA.obj = "https://www.gstatic.com/webp/gallery/1.jpg"
                    }
                    2 -> {
                        msgA.obj = "https://www.gstatic.com/webp/gallery/2.webp"
                    }
                }
                msgA.what = msg.what
                uiHandler.sendMessage(msgA)
            }
        }
    }

    fun loadImage(code : Int) {
        val msg = Message()
        msg.what = code
        handler?.sendMessage(msg)
    }
}
