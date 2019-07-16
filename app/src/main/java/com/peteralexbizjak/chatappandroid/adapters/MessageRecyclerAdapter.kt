package com.peteralexbizjak.chatappandroid.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.peteralexbizjak.chatappandroid.R
import com.peteralexbizjak.chatappandroid.models.MessageModel
import com.peteralexbizjak.chatappandroid.utils.CircleTransform
import com.squareup.picasso.Picasso

class MessageRecyclerAdapter(
    private val context: Context?,
    private val messageModelList: List<MessageModel>,
    private val userProfilHashMap: List<HashMap<String, String>>
) : RecyclerView.Adapter<MessageRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.message_item, parent, false))
    }

    override fun getItemCount(): Int {
        return messageModelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val messageModel: MessageModel = messageModelList[position]

        userProfilHashMap.forEach {
            if (it.containsKey(messageModel.senderId)) {
                Picasso.get().load(it.getValue(messageModel.senderId)).transform(CircleTransform()).into(holder.senderPicture)
            }
        }

        holder.messageContents.text = messageModel.messageContents
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val senderPicture: ImageView = itemView.findViewById(R.id.messageItemProfilePicture)
        val messageContents: TextView = itemView.findViewById(R.id.messageItemMessageText)
    }

}