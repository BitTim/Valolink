/*
Copyright (c) 2024 BitTim

Project:        Valolink
License:        GPLv3

File:           StringListConverterTest.kt
Author:         Tim Anhalt (BitTim)
Created:        15.10.2024
Description:    Tests for the StringListConverter class.
*/

package dev.bittim.valolink.main.data.local.converter

import dev.bittim.valolink.main.di.MainModule
import junit.framework.TestCase.assertEquals
import org.junit.Test

class StringListConverterTest {
    fun setup(): StringListConverter {
        val moshi = MainModule.provideMoshi()
        val converter = StringListConverter(moshi)

        return converter
    }

    @Test
    fun fromStringList_convertsListToString_withMultipleItems() {
        val converter = setup()

        val list = listOf("item1", "item2", "item3")
        val result = converter.fromStringList(list)

        assertEquals("[\"item1\",\"item2\",\"item3\"]", result)
    }

    @Test
    fun fromStringList_convertsListToString_withSingleItem() {
        val converter = setup()

        val list = listOf("item1")
        val result = converter.fromStringList(list)

        assertEquals("[\"item1\"]", result)
    }

    @Test
    fun fromStringList_convertsEmptyListToString() {
        val converter = setup()

        val list = emptyList<String>()
        val result = converter.fromStringList(list)

        assertEquals("[]", result)
    }

    @Test
    fun toStringList_convertsStringToList_withMultipleItems() {
        val converter = setup()

        val string = "[\"item1\",\"item2\",\"item3\"]"
        val result = converter.toStringList(string)

        assertEquals(listOf("item1", "item2", "item3"), result)
    }

    @Test
    fun toStringList_convertsStringToList_withMultipleItemsWithWhitespace() {
        val converter = setup()

        val string = "[\"item1\", \"item2\", \"item3\"]"
        val result = converter.toStringList(string)

        assertEquals(listOf("item1", "item2", "item3"), result)
    }

    @Test
    fun toStringList_convertsStringToList_withSingleItem() {
        val converter = setup()

        val string = "[\"item1\"]"
        val result = converter.toStringList(string)

        assertEquals(listOf("item1"), result)
    }

    @Test
    fun toStringList_convertsStringToList_withSingleItemWithWhitespace() {
        val converter = setup()

        val string = "[ \"item1\" ]"
        val result = converter.toStringList(string)

        assertEquals(listOf("item1"), result)
    }

    @Test
    fun toStringList_convertsStringToList_withEmptyList() {
        val converter = setup()

        val string = "[]"
        val result = converter.toStringList(string)

        assertEquals(emptyList<String>(), result)
    }

    @Test
    fun toStringList_convertsStringToList_withEmptyListWithWhitespace() {
        val converter = setup()

        val string = "[ ]"
        val result = converter.toStringList(string)

        assertEquals(emptyList<String>(), result)
    }
}