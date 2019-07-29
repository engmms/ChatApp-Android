package com.peteralexbizjak.chatapp_android.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.peteralexbizjak.chatapp_android.R
import com.peteralexbizjak.chatapp_android.models.firestore.ChatModel
import com.peteralexbizjak.chatapp_android.utils.CircleTransform
import com.squareup.picasso.Picasso

class ChannelRecyclerAdapter(
    private val context: Context?,
    private val chatModelList: List<ChatModel>,
    private val currentUserId: String
) : RecyclerView.Adapter<ChannelRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.channel_item, parent, false))
    }

    override fun getItemCount(): Int {
        return chatModelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatModel: ChatModel = chatModelList[position]
        chatModel.participants.forEach {
            if (!it.keys.contains(currentUserId)) {
                it.values.forEach { participantModel ->
                    if (participantModel.photoUrl != null)
                        Picasso.get()
                            .load(participantModel.photoUrl)
                            .transform(CircleTransform())
                            .into(holder.recipientProfilePicture)
                    holder.recipientDisplayName.text = participantModel.displayName
                    holder.recipientLastMessage.text = "Something something something..."
                }
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val recipientProfilePicture: ImageView = itemView.findViewById(R.id.channelItemImageView)
        val recipientDisplayName: TextView = itemView.findViewById(R.id.channelItemReceiver)
        val recipientLastMessage: TextView = itemView.findViewById(R.id.channelItemLastMessage)
    }
}
