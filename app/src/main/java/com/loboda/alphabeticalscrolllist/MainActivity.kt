package com.loboda.alphabeticalscrolllist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.loboda.alphabeticalscrolllist.ui.theme.AlphabeticalScrollListTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AlphabeticalScrollListTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                        AlphabeticScrollList(fakeListOfNames, headerContent = {
                            DefaultsAlphabeticalScrollList.HeaderContent(it)
                        }, itemContent = {
                            DefaultsAlphabeticalScrollList.ItemContent(it)
                        },
                            indexCharInfo = IndexCharInfo(
                                size = 20.dp,
                                padding = 2.dp,
                                content = {
                                    DefaultsAlphabeticalScrollList.IndexCharContent(it)
                                }
                            ))
                    }
                }
            }
        }
    }
}