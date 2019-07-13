package com.peteralexbizjak.chatappandroid.utils

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.regex.Pattern
import kotlin.experimental.and

class ValidatorHelper {

    /**
     * Validate email against a standard email regular expression
     *
     * @param emailString email string
     * @return true or false, which is the result of Matcher.matches() operation
     */
    public fun isEmailValid(emailString: String): Boolean {
        //Standard regular expression for validating emails
        val regex = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")

        //Run the emailString gains the regular expression, return result of matches()
        return Pattern.compile(regex, Pattern.CASE_INSENSITIVE).matcher(emailString).matches()
    }

    /**
     * Check if passwords match
     *
     * @param passwordString password string
     * @param retypedPassword retyped password string
     * @return result of equals() operation
     */
    public fun doPasswordsMatch(passwordString: String, retypedPassword: String): Boolean {
        return passwordString == retypedPassword
    }

    /**
     * Hash the password with the MD5 algorithm
     *
     * @param passwordString password string
     * @return string that represents a hashed password
     */
    public fun hashPassword(passwordString: String): String? {
        var hashedPassword: String? = null
        try {
            //Create MessageDigest instance for MD5 algorithm
            val messageDigest = MessageDigest.getInstance("MD5")
            messageDigest.update(passwordString.toByteArray())

            //Get hashed bytes and convert them to hex form
            val hashedBytes = messageDigest.digest()
            val stringBuilder = StringBuilder()
            for (hashedByte in hashedBytes)
                stringBuilder.append(((hashedByte and 0xff.toByte()) + 0x100).toString(16).substring(1))

            //Get complete hashed password (in hex)
            hashedPassword = stringBuilder.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }

        return hashedPassword
    }
}
