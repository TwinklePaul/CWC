package com.team.NEBULA.cwc.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.team.NEBULA.cwc.R
import com.team.NEBULA.cwc.data.SendFileDetails
import kotlinx.android.synthetic.main.activity_history.view.*

class ImgHisAdapter(val imgLists:ArrayList<SendFileDetails>, val context: Context):RecyclerView.Adapter<ImgHisAdapter.MyViewHolder>() {


    private val TAG by lazy {ImageAdapter::class.java.simpleName}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view:View = layoutInflater.inflate(R.layout.imglist_items,parent,false) as View
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {

        return imgLists.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.tView!!.text = imgLists[position].humanReadableTime
        Glide.with(context)
            .load(imgLists[position].imageUrl)
            .placeholder(R.drawable.loading)
            .into(holder.imageView!!)
    }

    class MyViewHolder(imgView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(imgView){

        var imageView: ImageView? = null
        var tView:TextView? = null
        var loadingProgressBar:LinearLayout ? = null
        init {
            loadingProgressBar = imgView.findViewById(R.id.loadingBar)
            imageView = imgView.findViewById(R.id.imgVIew)
            tView = imgView.findViewById(R.id.timeView)
        }
    }
}