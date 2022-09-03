package com.geancarloleiva.jetcomptrivia_13.network

import com.geancarloleiva.jetcomptrivia_13.model.Question
import com.geancarloleiva.jetcomptrivia_13.util.Constants
import retrofit2.http.GET
import javax.inject.Singleton

@Singleton
interface QuestionApi {

    @GET(Constants.QUESTION_URL)
    suspend fun getAllQuestions(): Question

}