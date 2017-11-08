package com.example.juliandbarrientos.shoutit.Adapters

import android.content.Context
import android.net.Uri
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.juliandbarrientos.shoutit.Objects.AudioClass
import com.example.juliandbarrientos.shoutit.Objects.UsuarioClass
import com.example.juliandbarrientos.shoutit.R
import com.squareup.picasso.Picasso
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import android.graphics.Bitmap
import com.nostra13.universalimageloader.core.assist.FailReason




/**
 * Created by julian.d.barrientos on 11/3/2017.
 */
public class AudioAdapter (private val context : Context, private val audis : List<AudioClass>, private val usus : HashMap<String,UsuarioClass>, private val imageLoader: ImageLoader) : RecyclerView.Adapter<AudioAdapter.ViewHolder>(){
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView
        var countTextView: TextView
        var thumbImageView : ImageView
        var progressBar : ProgressBar

        init {
            titleTextView = itemView.findViewById(R.id.name)
            countTextView = itemView.findViewById(R.id.date)
            thumbImageView = itemView.findViewById(R.id.thumbnail)
            progressBar = itemView.findViewById(R.id.progress_bar)
        }
    }
    override fun onCreateViewHolder(parent : ViewGroup, type : Int) : AudioAdapter.ViewHolder{
        val view : View = LayoutInflater.from(parent.context).inflate(R.layout.item_audios, parent, false)
        val card : CardView = view.findViewById(R.id.card_view)
        //   card.setCardBackgroundColor(Color.parseColor("#E6E6E6"));
        card.maxCardElevation = 2.0F
        card.radius = 5.0F
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder : AudioAdapter.ViewHolder, position : Int){
        val audio : AudioClass = audis[position]
        val usuario : UsuarioClass? = usus[audio._usu]
        holder.titleTextView.text = usuario!!._name
        holder.countTextView.text = audio._timeRecorded
        imageLoader.displayImage(usuario._imageUrl, holder.thumbImageView, object :SimpleImageLoadingListener(){

            override fun onLoadingStarted(imageUri: String, view: View) {
                holder.progressBar.setVisibility(View.VISIBLE)
            }

            override fun onLoadingFailed(imageUri: String, view: View, failReason: FailReason) {
                holder.progressBar.setVisibility(View.GONE)
            }

            override fun onLoadingComplete(imageUri: String, view: View, loadedImage: Bitmap) {
                holder.progressBar.setVisibility(View.GONE)
            }
        })
    }

    override fun getItemCount() : Int{
        return audis.size
    }

}