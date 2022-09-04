package com.geancarloleiva.jetcomptrivia_13

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.geancarloleiva.jetcomptrivia_13.model.Question
import com.geancarloleiva.jetcomptrivia_13.screen.QuestionViewModel
import com.geancarloleiva.jetcomptrivia_13.ui.theme.JetCompTrivia_13Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetCompTrivia_13Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TriviaHome()
                }
            }
        }
    }
}

@Composable
fun TriviaHome(viewModel: QuestionViewModel = hiltViewModel()){
    TriviaQuestions(viewModel)
}

@Composable
fun TriviaQuestions(viewModel: QuestionViewModel){
    val questions = viewModel.data.value.data?.toMutableList()
    if(viewModel.data.value.loading == true)
        Log.d("QUESTION", "TriviaQuestions: is loading...")
    else {
        Log.d("QUESTION", "TriviaQuestions: loaded...")
        questions?.forEach { questionItem ->
            Log.d("QUESTION", "Items: ${questionItem.question}")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetCompTrivia_13Theme {
    }
}