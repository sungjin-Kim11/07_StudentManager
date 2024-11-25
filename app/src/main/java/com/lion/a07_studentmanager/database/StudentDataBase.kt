package com.lion.a07_studentmanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [StudentVO::class], version = 1, exportSchema = true)
abstract class StudentDataBase : RoomDatabase(){
    // dao
    abstract fun studentDao() : StudentDao

    companion object{
        // 데이터 베이스 객체를 담을 변수
        var studentDatabase:StudentDataBase? = null
        @Synchronized
        fun getInstance(context: Context) : StudentDataBase?{
            // 만약 데이터베이스 객체가 null이라면 객체를 생성한다.
            // 데이터베이스 파일 이름 꼭 변경!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            synchronized(StudentDataBase::class){
                studentDatabase = Room.databaseBuilder(
                    context.applicationContext, StudentDataBase::class.java,
                    "Student.db"
                ).build()
            }
            return studentDatabase
        }

        // 데이터 베이스 객체가 소멸될 때 호출되는 메서드
        fun destroyInstance(){
            studentDatabase = null
        }
    }
}