package org.example.project

import App
import EntryItem
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import data.enums.Category
import data.enums.Type
import data.models.Entry

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun EntryItemPreview() {
    Surface {
        Column(
            modifier = Modifier.fillMaxSize(),
        ) {
            EntryItem(
                modifier = Modifier.fillMaxSize(),
                data = Entry(
                    id = 1,
                    title = "Lunch",
                    amount = 10,
                    date = "2021-09-01",
                    type = Type.EXPENSE,
                    category = Category.FOOD,
                ),
            )
        }
    }
}