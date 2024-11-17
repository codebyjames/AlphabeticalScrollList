package com.loboda.alphabeticalscrolllist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
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
import kotlinx.coroutines.launch

/**
 * @author James Loboda aka papayev
 * created by James Loboda aka papayev at 11/16/24
 * www.papayev.com
 */

private val ALPHABET = ('A'..'Z').toList()

@Composable
fun AlphabeticScrollList(items: List<String>,
                         headerContent: @Composable (Char) -> Unit = { DefaultAlphabeticalScrollList.HeaderContent(it) },
                         itemContent: @Composable (String) -> Unit = { DefaultAlphabeticalScrollList.ItemContent(it) },
                         indexCharInfo: IndexCharInfo = IndexCharInfo { DefaultAlphabeticalScrollList.IndexCharContent(it) }
) {
    val groupedItems = remember { items.sorted().groupBy { it.first().uppercaseChar() } }
    var selectedChar by remember { mutableStateOf<Char?>(null) }
    val scrollState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val firstVisibleIndex by remember {
        derivedStateOf { scrollState.firstVisibleItemIndex }
    }

    // Update selectedChar when the first visible index changes
    LaunchedEffect(firstVisibleIndex) {
        determineVisibleGroupKey(firstVisibleIndex, groupedItems)?.let { selectedChar = it }
    }

    val onSelectedChar: (Char) -> Unit = { char ->
        // Update the selectedChar state
        selectedChar = char

        // Calculate the target index by summing the number of headers and items before the target group
        val targetIndex = groupedItems.entries
            .takeWhile { it.key != char }
            .sumOf { 1 + it.value.size }

        // Scroll to the selected group
        if (groupedItems.keys.contains(char)) {
            scope.launch { scrollState.scrollToItem(targetIndex) }
        }
    }

    Row(Modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center) {

        // List of items: Header + Items
        LazyColumn(
            modifier = Modifier.weight(0.9f),
            state = scrollState
        ) {
            groupedItems.forEach { (initial, items) ->
                item { headerContent(initial) }
                items.forEach { listItem ->
                    item { itemContent(listItem) }
                }
            }
        }

        // vertical bar with alphabet
        AlphabetVerticalBar(indexCharInfo, selectedChar, onSelectedChar)
    }
}

@Composable
private fun RowScope.AlphabetVerticalBar(
    indexCharInfo: IndexCharInfo,
    selectedChar: Char?,
    onSelectedChar: (Char) -> Unit,
) {

    val totalSizeChar = indexCharInfo.size + indexCharInfo.padding

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .weight(0.1f)
            .pointerInput(Unit) {
                detectVerticalDragGestures { change, _ ->
                    val index = (change.position.y / (totalSizeChar.toPx()))
                        .toInt()
                        .coerceIn(0, ALPHABET.size - 1)
                    onSelectedChar(ALPHABET[index])
                }
            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ALPHABET.forEach { char ->
            Box (modifier = Modifier
                .size(indexCharInfo.size)
                .padding(indexCharInfo.padding)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = false)
                ) {
                    onSelectedChar(char)
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

/**
 * Determines the visible group key based on the provided first visible index.
 * It calculates the group that the visible index belongs to by iterating over
 * grouped items and maintaining a cumulative index.
 *
 * @param firstVisibleIndex The index of the first visible item in the list.
 * @param groupedItems A map representing grouped items with their headers as keys.
 * @return The key of the currently visible group, or null if no matching group is found.
 */
fun determineVisibleGroupKey(
    firstVisibleIndex: Int,
    groupedItems: Map<Char, List<Any>>
): Char? {
    var cumulativeIndex = 0
    return groupedItems.entries.find { entry ->
        val groupSize = 1 + entry.value.size  // 1 for the header + number of items
        val withinGroup = firstVisibleIndex in cumulativeIndex until (cumulativeIndex + groupSize)
        cumulativeIndex += groupSize  // Move to the next group's start index
        withinGroup
    }?.key
}


@Preview(showSystemUi = true)
@Composable
fun AlphabeticIndexedListPreview() { AlphabeticScrollList(fakeListOfNames) }


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