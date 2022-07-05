package br.com.murilofarias.checkineventos.util

import org.junit.Assert.*
import org.junit.Test


class UtilsTest{

    @Test
    fun isUserInfoValid_userEmptyEmailNotBlank_returnsFalse() {
        //given
        val user = ""
        val email = "murilo.farias@gmail.com"

        //when
        val result = isUserInfoValid(user, email)

        // Then
        assertEquals(false, result)
    }

    @Test
    fun isUserInfoValid_userNotBlankEmailEmpty_returnsFalse() {
        //given
        val user = "murilo"
        val email = ""

        //when
        val result = isUserInfoValid(user, email)

        // Then
        assertEquals(false, result)
    }

    @Test
    fun isUserInfoValid_userWhiteSpacesEmailNotBlank_returnsFalse() {
        //given
        val user = "   "
        val email = "murilo.farias@gmail.com"

        //when
        val result = isUserInfoValid(user, email)

        // Then
        assertEquals(false, result)
    }

    @Test
    fun isUserInfoValid_userBlankEmailWhiteSpaces_returnsFalse() {
        //given
        val user = "murilo"
        val email = "   "

        //when
        val result = isUserInfoValid(user, email)

        // Then
        assertEquals(false, result)
    }

    @Test
    fun isUserInfoValid_userNotBlankEmailNotBlank_returnsTrue() {
        //given
        val user = "murilo"
        val email = "murilo.farias@gmail.com"

        //when
        val result = isUserInfoValid(user, email)

        // Then
        assertEquals(true, result)
    }
}