package com.geancarloleiva.jetcomptrivia_13.di

import com.geancarloleiva.jetcomptrivia_13.network.QuestionApi
import com.geancarloleiva.jetcomptrivia_13.repository.QuestionRepository
import com.geancarloleiva.jetcomptrivia_13.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideQuestionApi(): QuestionApi{
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuestionApi::class.java)
    }

    @Singleton
    @Provides
    fun provideQuestionRepository(api: QuestionApi): QuestionRepository{
        return QuestionRepository(api)
    }

}