package com.loboda.alphabeticalscrolllist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * @author James Loboda aka papayev
 * created by James Loboda aka papayev at 11/16/24
 * www.papayev.com
 */

@Composable
fun AlphabeticScrollList(items: List<String>,
                         headerUi: @Composable (Char) -> Unit,
                         itemUi: @Composable (String) -> Unit,
                         indexCharInfo: IndexCharInfo) {
    val groupedItems = remember {
        items.groupBy { it.first().uppercaseChar() }
    }
    val alphabet = ('A'..'Z').toList()
    var selectedChar by remember { mutableStateOf<Char?>(null) }
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val totalSizeChar = indexCharInfo.size + indexCharInfo.padding

    val processSelectedChar = {
        selectedChar?.let { char ->
            // Calculate the target index by summing the number of headers and items before the target group
            val targetIndex = groupedItems.entries
                .takeWhile { it.key != char }
                .sumOf { 1 + it.value.size }
            if (groupedItems.keys.contains(char)) {
                scope.launch {
                    scrollState.scrollToItem(targetIndex)
                }
            }
        }
    }

    Row(Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {
        // Main List
        LazyColumn(
            modifier = Modifier.weight(1f),
            state = scrollState
        ) {
            groupedItems.forEach { (initial, items) ->
                item { headerUi(initial) }
                items.forEach { listItem ->
                    item { itemUi(listItem) }
                }
            }
        }

        // Alphabet Index
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(40.dp)
                .pointerInput(Unit) {
                    detectVerticalDragGestures { change, _ ->
                        val index = (change.position.y / (totalSizeChar.toPx()))
                            .toInt()
                            .coerceIn(0, alphabet.size - 1)
                        selectedChar = alphabet[index]
                        processSelectedChar()
                    }
                },
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            alphabet.forEach { char ->
                Box (modifier = Modifier
                    .size(totalSizeChar)
                    .padding(indexCharInfo.padding)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(bounded = false)
                    ) {
                        selectedChar = char
                        processSelectedChar()
                    }
                    .graphicsLayer {
                        scaleX = if (char == selectedChar) 1.5f else 1f
                        scaleY = if (char == selectedChar) 1.5f else 1f
                    }) {
                    indexCharInfo.content(char)
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AlphabeticIndexedListPreview() {
    AlphabeticScrollList(fakeListOfNames, headerUi = {
        DefaultAlphabeticalScrollList.HeaderUi(it)
    }, itemUi = {
        DefaultAlphabeticalScrollList.ItemUi(it)
    },
        indexCharInfo = IndexCharInfo(
            size = 20.dp,
            padding = 2.dp,
            content = {
                DefaultAlphabeticalScrollList.IndexCharUi(it)
            }
        ))
}


val fakeListOfNames = listOf(
    "Alice",
    "Annie",
    "Anna",
    "Bob",
    "Bill",
    "Charlie",
    "Cathy",
    "David",
    "Eve",
    "Frank",
    "Francis",
    "Grace",
    "Hannah",
    "Isaac",
    "Jack",
    "Jill",
    "James",
    "Jenny",
    "Katie",
    "Liam",
    "Mia",
    "Nathan",
    "Olivia",
    "Peter",
    "Quinn",
    "Rachel",
    "Sam",
    "Tina",
    "Ulysses",
    "Victoria",
    "Wendy",
    "Xander",
    "Yvonne",
    "Zach"
)