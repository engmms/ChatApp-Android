package com.peteralexbizjak.chatapp_android.models.firestore;

import java.util.HashMap;
import java.util.List;

public class ChatModel {
    public String chatId;
    public List<HashMap<String, ParticipantModel>> participants;

    public ChatModel() {}
    public ChatModel(String chatId, List<HashMap<String, ParticipantModel>> participants) {
        this.chatId = chatId;
        this.participants = participants;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public List<HashMap<String, ParticipantModel>> getParticipants() {
        return participants;
    }

    public void setParticipants(List<HashMap<String, ParticipantModel>> participants) {
        this.participants = participants;
    }
}
