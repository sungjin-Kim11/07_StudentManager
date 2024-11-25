package com.lion.a07_studentmanager.viewmodel

import com.lion.a07_studentmanager.util.StudentGender
import com.lion.a07_studentmanager.util.StudentGrade
import com.lion.a07_studentmanager.util.StudentType

class StudentModel(
    val studentIdx:Int,
    val studentName:String,
    val studentGrade:StudentGrade,
    val studentType: StudentType,
    val studentGender: StudentGender,
    val studentKorean:Int,
    val studentEnglish:Int,
    val studentMath:Int
)