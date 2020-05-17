package com.example.image

import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.ref.WeakReference

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var uiHandler: UiHandler
    private lateinit var imageHandlerThreadOne: ImageHandlerThread
    private lateinit var imageHandlerThreadTwo: ImageHandlerThread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initHandler()
    }

    private fun initHandler() {
        uiHandler = UiHandler()
        uiHandler.setView(this)
        imageHandlerThreadOne = ImageHandlerThread(uiHandler)
        imageHandlerThreadOne.start()
        imageHandlerThreadTwo = ImageHandlerThread(uiHandler)
        imageHandlerThreadTwo.start()

    }

    private fun initViews() {
        button_one.setOnClickListener(this)
        button_two.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.button_one -> {
                imageHandlerThreadOne.loadImage(1)
            }
            R.id.button_two -> {
                imageHandlerThreadTwo.loadImage(2)
            }
        }
    }

    internal class UiHandler : Handler() {
        private lateinit var mainActivityRef: WeakReference<MainActivity?>

        fun setView(activity: MainActivity?) {
            mainActivityRef = WeakReference(activity)
        }
        override fun handleMessage(msg: Message) {
            val mainActivity : MainActivity? = mainActivityRef.get()
            if (mainActivity == null) {
                return
            }
            Glide.with(mainActivity).load(msg.obj.toString()).centerInside().into(mainActivity.getView(msg.what))
        }
    }

    private fun getView(what: Int) : AppCompatImageView {
        return if(what == 1) image_one else image_two
    }


    override fun onDestroy() {
        super.onDestroy()
        imageHandlerThreadOne.quit()
        imageHandlerThreadTwo.quit()
    }
}
