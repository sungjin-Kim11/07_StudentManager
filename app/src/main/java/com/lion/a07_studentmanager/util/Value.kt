package com.lion.a07_studentmanager.util

enum class StudentGrade(val number:Int, val str:String){
    STUDENT_GRADE_1(1, "1학년"),
    STUDENT_GRADE_2(2, "2학년"),
    STUDENT_GRADE_3(3, "3학년"),
}

enum class StudentType(val number:Int, val str:String){
    STUDENT_TYPE_BASKETBALL(1, "농구부"),
    STUDENT_TYPE_SOCCER(2, "축구부"),
    STUDENT_TYPE_BASEBALL(3, "야구부"),
}

enum class StudentGender(val number:Int, val str:String){
    STUDENT_GENDER_MALE(1, "남자"),
    STUDENT_GENDER_FEMALE(2, "여자"),
}

fun numberToStudentGrade(studentGrade:Int) = when(studentGrade){
    1 -> StudentGrade.STUDENT_GRADE_1
    2 -> StudentGrade.STUDENT_GRADE_2
    else -> StudentGrade.STUDENT_GRADE_3
}

fun numberToStudentType(studentType:Int) = when(studentType){
    1 -> StudentType.STUDENT_TYPE_BASKETBALL
    2 -> StudentType.STUDENT_TYPE_SOCCER
    else -> StudentType.STUDENT_TYPE_BASEBALL
}

fun numberToStudentGender(studentGender:Int) = when(studentGender){
    1 -> StudentGender.STUDENT_GENDER_MALE
    else -> StudentGender.STUDENT_GENDER_FEMALE
}