package com.peteralexbizjak.chatappandroid.models;

import java.util.HashMap;
import java.util.List;

public class ChannelModel {

    public String channelId;
    public List<HashMap<String, List<String>>> basicUserInfos;
    public int channelColor;

    public ChannelModel() {}
    public ChannelModel(String channelId, List<HashMap<String, List<String>>> basicUserInfos, int channelColor) {
        this.channelId = channelId;
        this.basicUserInfos = basicUserInfos;
        this.channelColor = channelColor;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public List<HashMap<String, List<String>>> getBasicUserInfos() {
        return basicUserInfos;
    }

    public void setBasicUserInfos(List<HashMap<String, List<String>>> basicUserInfos) {
        this.basicUserInfos = basicUserInfos;
    }

    public int getChannelColor() {
        return channelColor;
    }

    public void setChannelColor(int channelColor) {
        this.channelColor = channelColor;
    }
}
