package com.loboda.alphabeticalscrolllist

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * @author James Loboda aka papayev
 * created by James Loboda aka papayev at 11/16/24
 * www.papayev.com
 */

data class IndexCharInfo(
    val size: Dp = 20.dp,
    val padding: Dp = 2.dp,
    val content: @Composable (Char) -> Unit
)