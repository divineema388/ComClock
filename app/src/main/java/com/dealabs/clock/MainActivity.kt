package com.dealabs.clock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dealabs.clock.ui.theme.ClockTheme
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClockTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ClockScreen()
                }
            }
        }
    }
}

@Composable
fun ClockScreen() {
    var currentTime by remember { mutableStateOf(Calendar.getInstance()) }
    
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Calendar.getInstance()
            delay(1000)
        }
    }
    
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val clockSize = minOf(screenWidth * 0.8f, 300.dp)
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Digital time display
        DigitalTimeDisplay(currentTime)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Analog clock
        Box(
            modifier = Modifier.size(clockSize),
            contentAlignment = Alignment.Center
        ) {
            AnalogClock(
                time = currentTime,
                modifier = Modifier.fillMaxSize()
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Date display
        DateDisplay(currentTime)
    }
}

@Composable
fun DigitalTimeDisplay(time: Calendar) {
    val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
    val timeString = timeFormat.format(time.time)
    
    Card(
        modifier = Modifier.padding(horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Text(
            text = timeString,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(24.dp)
        )
    }
}

@Composable
fun DateDisplay(time: Calendar) {
    val dateFormat = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault())
    val dateString = dateFormat.format(time.time)
    
    Text(
        text = dateString,
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center
    )
}

@Composable
fun AnalogClock(
    time: Calendar,
    modifier: Modifier = Modifier
) {
    val hours = time.get(Calendar.HOUR) % 12
    val minutes = time.get(Calendar.MINUTE)
    val seconds = time.get(Calendar.SECOND)
    
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Background clock image
        Image(
            painter = painterResource(id = R.drawable.clock_rounded),
            contentDescription = "Clock background",
            modifier = Modifier
                .fillMaxSize()
                .clip(CircleShape),
            contentScale = ContentScale.Fit
        )
        
        // Clock hands overlay
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = minOf(size.width, size.height) / 2f * 0.8f
            
            // Hour hand
            val hourAngle = (hours * 30f) + (minutes * 0.5f) - 90f
            rotate(hourAngle, center) {
                drawLine(
                    color = Color.Black,
                    start = center,
                    end = center.copy(y = center.y - radius * 0.5f),
                    strokeWidth = 12f,
                    cap = StrokeCap.Round
                )
            }
            
            // Minute hand
            val minuteAngle = (minutes * 6f) - 90f
            rotate(minuteAngle, center) {
                drawLine(
                    color = Color.Black,
                    start = center,
                    end = center.copy(y = center.y - radius * 0.7f),
                    strokeWidth = 8f,
                    cap = StrokeCap.Round
                )
            }
            
            // Second hand
            val secondAngle = (seconds * 6f) - 90f
            rotate(secondAngle, center) {
                drawLine(
                    color = Color.Red,
                    start = center,
                    end = center.copy(y = center.y - radius * 0.8f),
                    strokeWidth = 4f,
                    cap = StrokeCap.Round
                )
            }
            
            // Center dot
            drawCircle(
                color = Color.Black,
                radius = 16f,
                center = center
            )
        }
    }
}