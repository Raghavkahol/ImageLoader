package com.example.image

import android.graphics.Bitmap
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var uiHandlerA: UiHandler = UiHandler()
    private var uiHandlerB: UiHandler = UiHandler()
    private lateinit var imageHandlerThreadOne: ImageHandlerThread
    private lateinit var imageHandlerThreadTwo: ImageHandlerThread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initHandler()
    }

    private fun initHandler() {
        uiHandlerA.setView(image_one)
        uiHandlerB.setView(image_two)
        imageHandlerThreadOne = ImageHandlerThread(uiHandlerA, AppConstants.HANDLER_ONE)
        imageHandlerThreadOne.start()
        imageHandlerThreadTwo = ImageHandlerThread(uiHandlerB, AppConstants.HANDLER_TWO)
        imageHandlerThreadTwo.start()

    }

    private fun initViews() {
        button_one.setOnClickListener(this)
        button_two.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_one -> {
                imageHandlerThreadOne.loadImage(AppConstants.IMAGE_ONE_CODE)
            }
            R.id.button_two -> {
                imageHandlerThreadTwo.loadImage(AppConstants.IMAGE_TWO_CODE)
            }
        }
    }

    internal class UiHandler : Handler() {

        private lateinit var mainActivityRef: WeakReference<AppCompatImageView?>

        fun setView(activity: AppCompatImageView?) {
            mainActivityRef = WeakReference(activity)
        }

        override fun handleMessage(msg: Message) {
            val imageView : AppCompatImageView? = mainActivityRef.get()
            imageView?.setImageBitmap(msg.obj as Bitmap)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        imageHandlerThreadOne.quit()
        imageHandlerThreadTwo.quit()
    }
}
