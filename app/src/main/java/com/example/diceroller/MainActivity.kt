package com.example.diceroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diceroller.ui.theme.DiceRollerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DiceRollerTheme {
                DiceRollerApp()
            }
        }
    }
}

@Preview
@Composable
fun DiceRollerApp() {
    DiceWithButtonAndImage(modifier = Modifier
        .fillMaxSize()
        .wrapContentSize(Alignment.Center)
    )
}

@Composable
fun DiceWithButtonAndImage(modifier: Modifier = Modifier) {
    var result by remember { mutableStateOf(0) } // 0 means not rolled yet
    var isRolling by remember { mutableStateOf(false) }
    val rotation = remember { Animatable(0f) }

    val imageResource = when (result) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        6 -> R.drawable.dice_6
        else -> R.drawable.dice_1 // default placeholder before first roll
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        Text(
            text = "Dice Roller",
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = colorResource(id = R.color.purple_500)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Subtitle
        Text(
            text = "Click the button to roll the dice!",
            fontSize = 16.sp,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Dice Image
        Image(
            painter = painterResource(imageResource),
            contentDescription = if (result == 0) "Dice" else result.toString(),
            modifier = Modifier.rotate(rotation.value)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Result Text (only show after first roll)
        if (result != 0) {
            Text(
                text = buildAnnotatedString {
                    append("You rolled ")
                    withStyle(style = SpanStyle(color = colorResource(id = R.color.purple_700), fontWeight = FontWeight.Bold)) {
                        append(result.toString())
                    }
                },
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))
        } else {
            Spacer(modifier = Modifier.height(34.dp)) // keep layout height consistent before first roll
        }

        // Roll Button
        Button(
            onClick = {
                if (!isRolling) {
                    isRolling = true
                }
            },
            enabled = !isRolling
        ) {
            Text(stringResource(R.string.roll))
        }
    }

    if (isRolling) {
        LaunchedEffect(Unit) {
            val shakeDegrees = listOf(0f, 15f, -15f, 10f, -10f, 5f, -5f, 0f)
            for (angle in shakeDegrees) {
                rotation.animateTo(angle, animationSpec = tween(50))
                result = (1..6).random()
            }
            isRolling = false
        }
    }
}
