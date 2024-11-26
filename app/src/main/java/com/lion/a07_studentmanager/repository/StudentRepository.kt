package com.lion.a07_studentmanager.repository

import android.content.Context
import com.lion.a07_studentmanager.database.StudentDataBase
import com.lion.a07_studentmanager.database.StudentVO
import com.lion.a07_studentmanager.util.numberToStudentGender
import com.lion.a07_studentmanager.util.numberToStudentGrade
import com.lion.a07_studentmanager.util.numberToStudentType
import com.lion.a07_studentmanager.viewmodel.StudentModel

class StudentRepository {

    companion object{
        // 학생 정보를 저장하는 메서드
        fun insertStudentData(context: Context, studentModel: StudentModel){
            // 데이터를 VO 객체에 담는다.
            val studentVO = StudentVO(
                studentName = studentModel.studentName,
                studentGrade = studentModel.studentGrade.number,
                studentType = studentModel.studentType.number,
                studentGender = studentModel.studentGender.number,
                studentKorean = studentModel.studentKorean,
                studentEnglish = studentModel.studentEnglish,
                studentMath = studentModel.studentMath
            )
            // 저장한다.
            val studentDataBase = StudentDataBase.getInstance(context)
            studentDataBase?.studentDao()?.insertStudentData(studentVO)
        }

        // 학생 데이터 전체를  가져오는 메서드
        fun selectStudentDataAll(context: Context) : MutableList<StudentModel>{
            // 데이터를 가져온다.
            val studentDataBase = StudentDataBase.getInstance(context)
            val studentList = studentDataBase?.studentDao()?.selectStudentDataAll()

            // 학생 데이터를 담을 리스트
            val tempList = mutableListOf<StudentModel>()

            // 학생의 수 만큼 반복한다.
            studentList?.forEach {
                val studentModel = StudentModel(
                    it.studentIdx, it.studentName, numberToStudentGrade(it.studentGrade),
                    numberToStudentType(it.studentType), numberToStudentGender(it.studentGender),
                    it.studentKorean, it.studentEnglish, it.studentMath
                )
                // 리스트에 담는다.
                tempList.add(studentModel)
            }
            return tempList
        }

        // 학생이름으로 검색하여 학생 데이터 전체를 가져오는 메서드
        fun selectStudentDataAllByStudentName(context: Context, studentName:String) : MutableList<StudentModel>{
            // 데이터를 가져온다.
            val studentDataBase = StudentDataBase.getInstance(context)
            val studentList = studentDataBase?.studentDao()?.selectStudentDataAllByStudentName(studentName)

            // 학생 데이터를 담을 리스트
            val tempList = mutableListOf<StudentModel>()

            // 학생의 수 만큼 반복한다.
            studentList?.forEach {
                val studentModel = StudentModel(
                    it.studentIdx, it.studentName, numberToStudentGrade(it.studentGrade),
                    numberToStudentType(it.studentType), numberToStudentGender(it.studentGender),
                    it.studentKorean, it.studentEnglish, it.studentMath
                )
                // 리스트에 담는다.
                tempList.add(studentModel)
            }
            return tempList
        }

        // 학생 한명의 데이터를 가져오는 메서드
        fun selectStudentDataByStudentIdx(context: Context, studentIdx:Int) : StudentModel{
            val studentDataBase = StudentDataBase.getInstance(context)
            // 학생 데이터를 가져온다.
            val studentVo = studentDataBase?.studentDao()?.selectStudentDataByStudentIdx(studentIdx)
            // Model 객체에 담는다.
            val studentModel = StudentModel(
                studentVo?.studentIdx!!, studentVo.studentName, numberToStudentGrade(studentVo.studentGrade),
                numberToStudentType(studentVo.studentType), numberToStudentGender(studentVo.studentGender),
                studentVo.studentKorean, studentVo.studentEnglish, studentVo.studentMath
            )
            return studentModel
        }

        // 학생 정보를 삭제하는 메서드
        fun deleteStudentDataByStudentIdx(context: Context, studentIdx:Int){
            // 삭제한다.
            val studentDataBase = StudentDataBase.getInstance(context)
            val studentVO = StudentVO(studentIdx = studentIdx)
            studentDataBase?.studentDao()?.deleteStudentData(studentVO)
        }

        // 학생 정보를 수정하는 메서드
        fun updateStudentDataByStudentIdx(context: Context, studentModel: StudentModel){
            // VO에 데이터를 담는다.
            val studentVO = StudentVO(
                studentModel.studentIdx, studentModel.studentName, studentModel.studentGrade.number,
                studentModel.studentType.number, studentModel.studentGender.number,
                studentModel.studentKorean, studentModel.studentEnglish, studentModel.studentMath
            )
            // 수정하는 메서드를 호출한다.
            val studentDataBase = StudentDataBase.getInstance(context)
            studentDataBase?.studentDao()?.updateStudentData(studentVO)
        }
    }
}

