package com.lion.a07_studentmanager.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "StudentTable")
data class StudentVO(
    // 학생 번호
    @PrimaryKey(autoGenerate = true)
    var studentIdx:Int = 0,
    // 학생 이름
    var studentName:String = "",
    // 학년
    var studentGrade:Int = 0,
    // 운동부
    var studentType:Int = 0,
    // 성별
    var studentGender:Int = 0,
    // 국어점수
    var studentKorean:Int = 0,
    // 영어점수
    var studentEnglish:Int = 0,
    // 수학점수
    var studentMath:Int = 0
)