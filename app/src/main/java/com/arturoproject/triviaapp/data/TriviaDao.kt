package com.arturoproject.triviaapp.data

import androidx.room.Dao
import androidx.room.Query


@Dao
interface TriviaDao{

    //Each time that the user select which category we are going to keep it'
    //as an integer then selecting the diffciltty as an integrer
    //after that the query will get the info from the database
    //so we are going to give the easiest solution
    @Query("SELECT * FROM triviadb WHERE category =:category and difficulty=:difficulty")

    suspend fun getQuestionsByCategoryAndDifficulty(category: Int, difficulty: Int): List<QuestionEntity>

    @Query("SELECT hint FROM triviadb where id= :questionId")
    suspend fun getHint(questionId: Int): String


}