package com.geancarloleiva.jetcomptrivia_13.repository

import android.util.Log
import com.geancarloleiva.jetcomptrivia_13.data.DataOrException
import com.geancarloleiva.jetcomptrivia_13.model.QuestionItem
import com.geancarloleiva.jetcomptrivia_13.network.QuestionApi
import javax.inject.Inject

class QuestionRepository @Inject constructor(
    private val api: QuestionApi
) {

    private val dataOrException = DataOrException<
            ArrayList<QuestionItem>,
            Boolean,
            Exception>()

    suspend fun getAllQuestions(): DataOrException<ArrayList<QuestionItem>, Boolean, Exception> {
        try {
            dataOrException.loading = true
            dataOrException.data = api.getAllQuestions()
            if(dataOrException.data.toString().isNotEmpty())
                dataOrException.loading = false

        } catch (e: Exception) {
            dataOrException.e = e
            Log.d("ERROR loading data", "getAllQuestions: ${e.localizedMessage}")
        }
        return dataOrException
    }
}