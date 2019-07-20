package com.peteralexbizjak.chatappandroid

import com.peteralexbizjak.chatappandroid.models.ChannelModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class HashMapUnitTests {

    private val currentUserId: String = "9876"

    @Test
    fun testRecipientDataExtraction() {
        println(extractRecipientData())
        assert(extractRecipientData().size == 3)
    }


    /**
     * Return a list of recipient data relevant to the transition from the main activity to the chat activity. This list
     * contains three items in the precisely defined order.
     *
     * Index 0: recipient ID
     * Index 1: recipient display name
     * Index 2: recipient profile photo URL (as a string)
     */
    fun extractRecipientData(): List<String> {
        val currentUserTest: HashMap<String, List<String>> = HashMap()
        val recipientUserTest: HashMap<String, List<String>> = HashMap()

        val currentUserTestInfos: List<String> = arrayListOf("John Doe", "https://www.google.com")
        val recipientUserTestInfos: List<String> = arrayListOf("John Wick", "John Wick has no profile pic, he is too cool for that")

        currentUserTest["1234"] = currentUserTestInfos
        recipientUserTest["9876"] = recipientUserTestInfos

        val basicUserInfosTest: List<HashMap<String, List<String>>> = arrayListOf(currentUserTest, recipientUserTest)

        val testChannelModel = ChannelModel("sampleChannelId", basicUserInfosTest, 123)

        var listOfRecipientData: List<String> = emptyList()
        testChannelModel.basicUserInfos?.forEach {
            if (!it.containsKey(currentUserId)) {
                val recipientId: String = it.keys.elementAt(0)
                listOfRecipientData = listOf(recipientId, it[recipientId]!![0], it[recipientId]!![1])
            }
        }
        return listOfRecipientData
    }
}