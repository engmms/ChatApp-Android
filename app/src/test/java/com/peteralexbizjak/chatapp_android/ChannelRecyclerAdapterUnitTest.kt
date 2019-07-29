package com.peteralexbizjak.chatapp_android

import com.peteralexbizjak.chatapp_android.models.firestore.ParticipantModel
import org.junit.Test

class ChannelRecyclerAdapterUnitTest {

    val currentUserId: String = "abcd"

    val firstUid: String = "abcd"
    val firstParticipant: ParticipantModel = ParticipantModel("John Wick", "https://ewedit.files.wordpress.com/2016/12/screen-shot-2016-12-21-at-1-58-18-pm.jpg?w=1889")

    val secondUid: String = "zyxv"
    val secondParticipant: ParticipantModel = ParticipantModel("The Adjudicator", "https://www.turn-on.de/media/cache/article_image_slider/media/cms/2019/05/John-Wick-3-The-Adjudicator-Concorde-Filmverleih-GmbH.jpg?329258")

    var firstParticipantHashMap: HashMap<String, ParticipantModel> = HashMap()
    var secondParticipantHashMap: HashMap<String, ParticipantModel> = HashMap()

    @Test
    fun void () {
        firstParticipantHashMap[firstUid] = firstParticipant
        secondParticipantHashMap[secondUid] = secondParticipant

        val chatModel = ChatModel("qwerty", arrayListOf(firstParticipantHashMap, secondParticipantHashMap))

        chatModel.participants.forEach {
            if (!it.keys.contains(currentUserId)) {
                it.values.forEach { participantModel ->
                    println(participantModel.displayName)
                    println(participantModel.photoUrl)
                }
                assert(it.keys.contains("zyxv"))
            }
        }
    }
}