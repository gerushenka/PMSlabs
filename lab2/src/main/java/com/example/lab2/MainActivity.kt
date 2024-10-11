package com.example.lab2
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab2.ui.theme.Lab2Theme
import androidx.compose.ui.platform.LocalConfiguration

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab2Theme {
                ImageGallery()
            }
        }
    }
}

@Composable
fun ImageGallery() {
    val images = listOf(R.drawable.image1, R.drawable.image2, R.drawable.image3)
    val descriptions = listOf(
        "капуцин в деловом костюме.",
        "горилла в чёрном костюме и с ярким красным галстуком.",
        "шимпанзе в строгом чёрном костюме с галстуком."
    )

    var currentIndex by remember { mutableStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        content = { innerPadding ->
            val configuration = LocalConfiguration.current
            val screenWidth = configuration.screenWidthDp.dp

            // Используем LazyColumn для прокрутки
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    Image(
                        painter = painterResource(id = images[currentIndex]),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(if (screenWidth < 600.dp) 250.dp else 400.dp)
                    )
                }

                item {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = descriptions[currentIndex],
                            textAlign = TextAlign.Center,
                            fontSize = if (screenWidth < 600.dp) 14.sp else 18.sp
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(onClick = {
                            currentIndex = if (currentIndex == 0) images.size - 1 else currentIndex - 1
                        }) {
                            Text("Prev")
                        }

                        Button(onClick = {
                            currentIndex = (currentIndex + 1) % images.size
                        }) {
                            Text("Next")
                        }
                    }
                }

                item {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (currentIndex == 0) {
                            Text("Это первое изображение")
                        } else if (currentIndex == images.size - 1) {
                            Text("Это последнее изображение")
                        }
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewImageGallery() {
    Lab2Theme {
        ImageGallery()
    }
}