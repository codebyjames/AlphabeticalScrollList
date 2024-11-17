package com.loboda.alphabeticalscrolllist

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * @author James Loboda aka papayev
 * created by James Loboda aka papayev at 11/16/24
 * www.papayev.com
 */

object DefaultAlphabeticalScrollList {

    @Composable
    fun HeaderContent(initial: Char) {
        Text(
            text = initial.toString(),
            style = MaterialTheme.typography.bodyLarge
        )
    }

    @Composable
    fun ItemContent(item: String) {
        Text(
            text = item,
            modifier = Modifier.padding(8.dp)
        )
    }

    @Composable
    fun IndexCharContent(char: Char) {
        BasicText(
            text = char.toString(),
            modifier = Modifier.fillMaxSize(),
        )
    }
}