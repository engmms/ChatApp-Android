package com.peteralexbizjak.chatapp_android.models.firestore;

import java.util.HashMap;
import java.util.List;

public class ChatModel {
    public String chatId;
    public List<HashMap<String, ParticipantModel>> participants;
    public HashMap<String, String> latestMessage;

    public ChatModel() {}
    public ChatModel(String chatId, List<HashMap<String, ParticipantModel>> participants, HashMap<String, String> latestMessage) {
        this.chatId = chatId;
        this.participants = participants;
        this.latestMessage = latestMessage;
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

    public HashMap<String, String> getLatestMessage() {
        return latestMessage;
    }

    public void setLatestMessage(HashMap<String, String> latestMessage) {
        this.latestMessage = latestMessage;
    }
}
