package com.team.NEBULA.cwc.Adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.google.android.flexbox.FlexboxLayoutManager
import com.team.NEBULA.cwc.R
import kotlinx.android.synthetic.main.upload_tumbnail_view.view.*

class ImageAdapter(val imageItemList:ArrayList<String>,val context: Context) : androidx.recyclerview.widget.RecyclerView.Adapter<ImageAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.upload_tumbnail_view,parent,false) as View
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  imageItemList.count()
    }

    override fun onBindViewHolder(parent: MyViewHolder, i: Int) {
        /*val _btmp:Bitmap =  BitmapFactory.decodeByteArray(imageItemList.get(i), 0, imageItemList[i].size)

        val height = ((_btmp.height / 100) *200) + _btmp.height
        val width = ((_btmp.height / 100) *200) + _btmp.width

        parent.imageView!!.layoutParams.height = height
        parent.imageView!!.layoutParams.width = width*/

        val _btmp = MediaStore.Images.Media.getBitmap(context.contentResolver, Uri.parse(imageItemList[i]))

        val resized = Bitmap.createScaledBitmap(_btmp,(_btmp.width *0.2).toInt(),
            (_btmp.height *0.2).toInt(), true)

        parent.imageView!!.setImageBitmap(resized)

        val lp = parent.imageView!!.layoutParams
        val marginLayoutParams = lp as ViewGroup.MarginLayoutParams
        marginLayoutParams.topMargin = 20
        marginLayoutParams.leftMargin = 10
        marginLayoutParams.rightMargin = 10

        if (lp is FlexboxLayoutManager.LayoutParams){
            lp.flexGrow = 1f
        }


        parent.imageView!!.scaleType = ImageView.ScaleType.CENTER_CROP
    }


    class MyViewHolder(imgView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(imgView){

        var imageView:ImageView? = null
        init {
            imageView = imgView.findViewById(R.id.uploadImgThumb)
        }
    }
}