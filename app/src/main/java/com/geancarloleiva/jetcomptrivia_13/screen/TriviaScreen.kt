package com.geancarloleiva.jetcomptrivia_13.screen

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradient
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.geancarloleiva.jetcomptrivia_13.model.QuestionItem
import com.geancarloleiva.jetcomptrivia_13.util.AppColors

@Composable
fun TriviaHome(viewModel: QuestionViewModel = hiltViewModel()) {
    TriviaQuestions(viewModel)
}

@Composable
fun TriviaQuestions(viewModel: QuestionViewModel) {
    val questions = viewModel.data.value.data?.toMutableList()
    val questionIndex = remember {
        mutableStateOf(0)
    }

    if (viewModel.data.value.loading == true) {
        Log.d("QUESTION", "TriviaQuestions: is loading...")
        CircularProgressIndicator()
    } else {
        Log.d("QUESTION", "TriviaQuestions: loaded...")
        /*questions?.forEach { questionItem ->
            Log.d("QUESTION", "Items: ${questionItem.question}")
        }*/

        val question = try {
            questions?.get(questionIndex.value)
        } catch (e: Exception) {
            Log.e("ERROR", "TriviaQuestions: ${e.localizedMessage}")
            null
        }

        if (questions != null) {
            QuestionDisplay(
                totalQuestions = questions.size,
                questionItem = question!!,
                questionIndex = questionIndex
//                ,
//                viewModel = viewModel
            ) {
                questionIndex.value = questionIndex.value + 1
            }
        }
    }
}

@Composable
fun QuestionDisplay(
    totalQuestions: Int,
    questionItem: QuestionItem,
    questionIndex: MutableState<Int>,
//    viewModel: QuestionViewModel,
    onNextClicked: (Int) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        color = AppColors.myDarkPurple
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            //Score
            if (questionIndex.value >= 3) {
                ShowProgress(questionIndex.value)
            }

            //Header
            QuestionHeader(counter = questionIndex.value, outOf = totalQuestions)

            //Separator
            val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
            QuestionHeaderSeparator(pathEffect = pathEffect)

            //Question
            QuestionAsIs(questionItem.question)
            //Choices
            ChoicesForQuestion(questionItem = questionItem)

            //Continue
            QuestionContinue(questionIndex, onNextClicked)
        }
    }
}

@Composable
fun QuestionHeader(
    counter: Int = 10,
    outOf: Int = 100
) {
    Text(text = buildAnnotatedString {
        withStyle(style = ParagraphStyle(textIndent = TextIndent.None)) {
            withStyle(
                style = SpanStyle(
                    color = AppColors.myLightGray,
                    fontWeight = FontWeight.Bold,
                    fontSize = 26.sp
                )
            ) {
                //Big text style
                append("Question ${counter + 1}/")

                //Small text style
                withStyle(
                    style = SpanStyle(
                        color = AppColors.myLightGray,
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    )
                ) {
                    append("$outOf")
                }
            }
        }
    }, modifier = Modifier.padding(20.dp))
}

@Composable
fun QuestionHeaderSeparator(pathEffect: PathEffect) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
    ) {
        drawLine(
            color = AppColors.myLightGray,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            pathEffect = pathEffect
        )
    }
}

@Composable
fun QuestionAsIs(question: String) {
    Column {
        Text(
            text = question,
            modifier = Modifier
                .padding(6.dp)
                .align(alignment = Alignment.Start)
                .fillMaxHeight(0.3f),
            fontSize = 17.sp,
            color = AppColors.myOffWhite,
            fontWeight = FontWeight.Bold,
            lineHeight = 22.sp
        )
    }
}

@Composable
fun ChoicesForQuestion(questionItem: QuestionItem) {
    val choicesState = remember(questionItem) {
        questionItem.choices.toMutableList()
    }
    val answerState = remember(questionItem) {
        mutableStateOf<Int?>(null)
    }
    val correctAnswerState = remember(questionItem) {
        mutableStateOf<Boolean?>(null)
    }
    val updateAnswer: (Int) -> Unit = remember(questionItem) {
        {
            answerState.value = it
            correctAnswerState.value = choicesState[it] == questionItem.answer
        }
    }

    choicesState.forEachIndexed { index, answerText ->
        Row(
            modifier = Modifier
                .padding(3.dp)
                .fillMaxWidth()
                .height(45.dp)
                .border(
                    width = 4.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            AppColors.myOffDarkPurple,
                            AppColors.myOffWhite
                        )
                    ),
                    shape = RoundedCornerShape(15.dp)
                )
                .clip(
                    RoundedCornerShape(
                        topStartPercent = 50,
                        topEndPercent = 50,
                        bottomStartPercent = 50,
                        bottomEndPercent = 50
                    )
                )
                .background(Color.Transparent),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = (answerState.value == index),
                onClick = {
                    updateAnswer(index)
                },
                modifier = Modifier.padding(start = 16.dp),
                colors = RadioButtonDefaults.colors(
                    selectedColor =
                    if (correctAnswerState.value == true
                        && index == answerState.value
                    ) {
                        Color.Green.copy(alpha = 0.2f)
                    } else {
                        Color.Red.copy(alpha = 0.2f)
                    }
                )
            )

            val annotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Light,
                        color = if (correctAnswerState.value == true
                            && index == answerState.value
                        ) {
                            Color.Green
                        } else if (correctAnswerState.value == false
                            && index == answerState.value
                        ) {
                            Color.Red
                        } else {
                            AppColors.myOffWhite
                        }
                    )
                ) {
                    append(answerText)
                }
            }
            Text(text = annotatedString, modifier = Modifier.padding(6.dp))
        }
    }
}

@Composable
fun QuestionContinue(
    questionIndex: MutableState<Int>,
    onNextClicked: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { onNextClicked(questionIndex.value) },
            modifier = Modifier.padding(6.dp),
            shape = RoundedCornerShape(34.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = AppColors.myLightBlue
            )
        ) {
            Text(
                text = "Next", modifier = Modifier.padding(4.dp),
                color = AppColors.myOffWhite,
                fontSize = 17.sp
            )
        }
    }
}

@Composable
fun ShowProgress(score: Int = 12) {
    val gradient = Brush.linearGradient(listOf(Color(0xFFF95075), Color(0xFFBE6BE5)))
    val progressFactor by remember(score) {
        mutableStateOf(score * 0.005f)
    }

    Row(
        modifier = Modifier
            .padding(3.dp)
            .fillMaxWidth()
            .height(45.dp)
            .border(
                width = 4.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        AppColors.myLightPurple,
                        AppColors.myLightPurple
                    )
                ),
                shape = RoundedCornerShape(34.dp)
            )
            .clip(
                RoundedCornerShape(
                    topStartPercent = 50,
                    topEndPercent = 50,
                    bottomStartPercent = 50,
                    bottomEndPercent = 50
                )
            )
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            onClick = {},
            contentPadding = PaddingValues(1.dp),
            modifier = Modifier
                .fillMaxWidth(progressFactor)
                .background(brush = gradient),
            enabled = false,
            elevation = null,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.Transparent,
                disabledBackgroundColor = Color.Transparent
            )
        ) {
            Text(
                text = (score * 10).toString(),
                modifier = Modifier.clip(shape = RoundedCornerShape(23.dp))
                    .fillMaxHeight(0.87f)
                    .fillMaxWidth()
                    .padding(6.dp),
                color = AppColors.myOffWhite,
                textAlign = TextAlign.Center
            )
        }
    }
}