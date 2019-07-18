package com.peteralexbizjak.chatappandroid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.peteralexbizjak.chatappandroid.R
import com.peteralexbizjak.chatappandroid.interfaces.OnItemClickListener
import com.peteralexbizjak.chatappandroid.models.ChannelModel
import com.peteralexbizjak.chatappandroid.utils.CircleTransform
import com.squareup.picasso.Picasso

class ChannelRecyclerAdapter(
    private val context: Context?,
    private val channelList: List<ChannelModel>
) : RecyclerView.Adapter<ChannelRecyclerAdapter.ViewHolder>() {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.channel_item, parent, false))
    }

    override fun getItemCount(): Int {
        return channelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val channelModel: ChannelModel = channelList[position]

        holder.channelColor.setColorFilter(channelModel.channelColor)

        channelModel.basicUserInfos.forEach {
            for (key: String in it.keys) {
                if (key != firebaseAuth.currentUser?.photoUrl.toString()) {
                    holder.channelRecipient.text = it[key]?.get(0)
                }
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val channelColor: ImageView = itemView.findViewById(R.id.channelItemColor)
        val channelRecipient: TextView = itemView.findViewById(R.id.channelItemRecipient)
        val channelNumberOfMessages: TextView = itemView.findViewById(R.id.channelItemNumberOfMessages)

        override fun onClick(v: View?) {
            onItemClickListener?.onItemClicked(itemView, adapterPosition)
        }
    }
}