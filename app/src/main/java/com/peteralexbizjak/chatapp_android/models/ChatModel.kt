package com.peteralexbizjak.chatapp_android.models

import kotlin.collections.HashMap

class ChatModel(val chatId: String, val participants: List<HashMap<String, ParticipantModel>>)
