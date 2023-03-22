package com.mock.application.auth

import org.mindrot.jbcrypt.BCrypt
import java.security.SecureRandom
class PasswordEncryptor {
    private val letters: String = "abcdefghijklmnopqrstuvwxyz"
    private val uppercaseLetters: String = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    private val numbers: String = "0123456789"
    private val special: String = "@#=+!£$%&?"
    private val maxPasswordLength: Float = 20F //Max password lenght that my app creates
    private val maxPasswordFactor: Float = 10F //Max password factor based on chars inside password

    //  see evaluatePassword function below

    /**
     * Generate a random password
     * @param isWithLetters Boolean value to specify if the password must contain letters
     * @param isWithUppercase Boolean value to specify if the password must contain uppercase letters
     * @param isWithNumbers Boolean value to specify if the password must contain numbers
     * @param isWithSpecial Boolean value to specify if the password must contain special chars
     * @param length Int value with the length of the password
     * @return the new password.
     */
    fun generatePassword(
        isWithLetters: Boolean = true,
        isWithUppercase: Boolean = true,
        isWithNumbers: Boolean = true,
        isWithSpecial: Boolean = true,
        length: Int = 6
    ): String {

        var result = ""
        var i = 0

        if (isWithLetters) {
            result += this.letters
        }
        if (isWithUppercase) {
            result += this.uppercaseLetters
        }
        if (isWithNumbers) {
            result += this.numbers
        }
        if (isWithSpecial) {
            result += this.special
        }

        val rnd = SecureRandom.getInstance("SHA1PRNG")
        val sb = StringBuilder(length)

        while (i < length) {
            val randomInt: Int = rnd.nextInt(result.length)
            sb.append(result[randomInt])
            i++
        }

        return sb.toString()
    }

    fun validatePassword(attempt: String, userPassword: String): Boolean {
        return BCrypt.checkpw(attempt, userPassword)
    }

    fun encryptPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

}