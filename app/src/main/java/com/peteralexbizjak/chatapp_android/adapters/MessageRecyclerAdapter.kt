package com.peteralexbizjak.chatapp_android.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.peteralexbizjak.chatapp_android.R
import com.peteralexbizjak.chatapp_android.models.firestore.MessageModel
import com.peteralexbizjak.chatapp_android.utils.CircleTransform
import com.squareup.picasso.Picasso

class MessageRecyclerAdapter(
    private val context: Context?,
    private val messageModelList: List<MessageModel>
) : RecyclerView.Adapter<MessageRecyclerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.message_item, parent, false))
    }

    override fun getItemCount(): Int {
        return messageModelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val messageModel: MessageModel = messageModelList[position]
        holder.messageContents.text = messageModel.messangeContents
        if (messageModel.messageAuthorPic != null)
            Picasso.get()
                .load(messageModel.messageAuthorPic)
                .transform(CircleTransform())
                .into(holder.senderPicture)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val senderPicture: ImageView = itemView.findViewById(R.id.messageItemProfilePicture)
        val messageContents: TextView = itemView.findViewById(R.id.messageItemMessageContents)
    }
}