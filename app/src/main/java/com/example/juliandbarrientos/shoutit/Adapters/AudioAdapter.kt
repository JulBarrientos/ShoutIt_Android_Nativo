package com.example.juliandbarrientos.shoutit.Adapters

import android.content.Context
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import com.example.juliandbarrientos.shoutit.Objects.AudioClass
import com.example.juliandbarrientos.shoutit.R

/**
 * Created by julian.d.barrientos on 11/3/2017.
 */
public class AudioAdapter (private val context : Context, private val list : List<AudioClass>) : RecyclerView.Adapter<AudioAdapter.ViewHolder>(){

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var titleTextView: TextView
        var countTextView: TextView
        var thumbImageView : ImageView

        init {
            titleTextView = itemView.findViewById(R.id.name)
            countTextView = itemView.findViewById(R.id.date)
            thumbImageView = itemView.findViewById(R.id.thumbnail)
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
        var audio : AudioClass = list[position]
        holder.titleTextView.text = audio._name
        holder.countTextView.text = audio._timeRecorded

    }

    override fun getItemCount() : Int{
        return list.size
    }

}