package com.peteralexbizjak.chatapp_android.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.peteralexbizjak.chatapp_android.R
import com.peteralexbizjak.chatapp_android.interfaces.OnItemClickListener
import com.peteralexbizjak.chatapp_android.models.general.UserModel
import com.peteralexbizjak.chatapp_android.utils.CircleTransform
import com.squareup.picasso.Picasso

class UserRecyclerAdapter(
    private val context: Context?,
    private var userModelList: List<UserModel>
) : RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder>() {

    private var firebaseStorage: FirebaseStorage = FirebaseStorage.getInstance()
    private var onItemClickListener: OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.user_item, parent, false))
    }

    override fun getItemCount(): Int {
        return userModelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val userModel: UserModel = userModelList[position]

        if (!userModel.photoUrl.isNullOrBlank()) {
            if (userModel.photoUrl.contains("firebasestorage")) {
                val storageReference: StorageReference = firebaseStorage.reference.child("userpics/${userModel.uid}/profile.jpg")
                storageReference.downloadUrl.addOnSuccessListener { uri ->
                    Picasso.get().load(uri.toString()).transform(CircleTransform()).into(holder.profilePicture)
                }
            } else Picasso.get().load(Uri.parse(userModel.photoUrl)).transform(CircleTransform()).into(holder.profilePicture)
        }

        holder.profileDisplayName.text = userModel.displayName
        holder.profileEmail.text = userModel.email
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        val profilePicture: ImageView = itemView.findViewById(R.id.userItemProfilePhoto)
        val profileDisplayName: TextView = itemView.findViewById(R.id.userItemProfileName)
        val profileEmail: TextView = itemView.findViewById(R.id.userItemProfileEmail)

        override fun onClick(v: View?) {
            onItemClickListener?.onItemClicked(itemView, adapterPosition)
        }
    }
}