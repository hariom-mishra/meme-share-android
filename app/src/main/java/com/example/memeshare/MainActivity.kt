package com.example.memeshare

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    lateinit var buttonNext : Button
    lateinit var buttonShare : Button
    var memeUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()

        buttonNext = findViewById<Button>(R.id.nextMeme)
        buttonNext.setOnClickListener {
            loadMeme()
        }
        buttonShare = findViewById<Button>(R.id.shareMeme)
        buttonShare.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT,"Hey checkout this hillarious $memeUrl")
            val chooser = Intent.createChooser(intent, "share this meme using...")
            startActivity(chooser)
        }
    }

    private fun loadMeme(){
        val progress_bar = findViewById<ProgressBar>(R.id.progress_bar)
        progress_bar.visibility = View.VISIBLE
        val queue = Volley.newRequestQueue(this)
        val url = "https://meme-api.com/gimme"
        val jsonObject = StringRequest(Request.Method.GET, url,
            { response ->
                val jsonObject = JSONObject(response)
                memeUrl = jsonObject.getString("url")
                val memeImageView = findViewById<ImageView>(R.id.meme_image)
                Glide.with(this).load(memeUrl).listener( object: RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress_bar.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress_bar.visibility = View.GONE
                        return false
                    }

                }
                ).into(memeImageView)
            },
            { error ->

            }
        )
        queue.add(jsonObject)

    }
}