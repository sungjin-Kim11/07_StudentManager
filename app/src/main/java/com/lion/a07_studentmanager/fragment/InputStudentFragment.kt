package com.lion.a07_studentmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lion.a07_studentmanager.MainActivity
import com.lion.a07_studentmanager.R
import com.lion.a07_studentmanager.databinding.FragmentInputStudentBinding
import com.lion.a07_studentmanager.repository.StudentRepository
import com.lion.a07_studentmanager.util.StudentGender
import com.lion.a07_studentmanager.util.StudentGrade
import com.lion.a07_studentmanager.util.StudentType
import com.lion.a07_studentmanager.viewmodel.StudentModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class InputStudentFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentInputStudentBinding: FragmentInputStudentBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentInputStudentBinding = FragmentInputStudentBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        // 툴바를 구성하는 메서드를 호출한다.
        settingToolbarInputStudent()
        // 입력 요소 초기 설정 메서드를 호출한다.
        settingTextField()
        // 버튼 구성 메서드를 호출한다.
        settingButtonInputStudentSubmit()

        return fragmentInputStudentBinding.root
    }

    // 툴바를 구성하는 메서드
    fun settingToolbarInputStudent(){
        fragmentInputStudentBinding.apply {
            toolbarInputStudent.title = "학생 정보 입력"
            toolbarInputStudent.setNavigationIcon(R.drawable.arrow_back_24px)
            toolbarInputStudent.setNavigationOnClickListener {
                mainFragment.removeFragment(SubFragmentName.INPUT_STUDENT_FRAGMENT)
            }
        }
    }

    // 입력 요소 초기 설정
    fun settingTextField(){
        fragmentInputStudentBinding.apply {
            mainActivity.showSoftInput(textFieldInputStudentName.editText!!)
        }
    }

    // 버튼 구성 메서드
    fun settingButtonInputStudentSubmit(){
        fragmentInputStudentBinding.apply {
            buttonInputStudentSubmit.setOnClickListener {
                // 학생 정보 등록 완료 처리 메서드를 호출한다.
                processingAddStudentInfo()
            }
        }
    }

    // 학생 정보 등록 완료 처리 메서드
    fun processingAddStudentInfo(){
        fragmentInputStudentBinding.apply {
            // 사용자가 입력한 값을 가져온다
            val studentName = textFieldInputStudentName.editText?.text?.toString()!!
            val studentKorean = textFieldInputStudentKorean.editText?.text?.toString()!!
            val studentEnglish = textFieldInputStudentEnglish.editText?.text?.toString()!!
            val studentMath = textFieldInputStudentMath.editText?.text?.toString()!!

            // 사용자 이름
            if(studentName.isEmpty()){
                mainActivity.showConfirmDialog("이름 입력 오류", "학생의 이름을 입력해세요"){
                    textFieldInputStudentName.editText?.requestFocus()
                }
                return
            }
            // 국어점수
            if(studentKorean.isEmpty()){
                mainActivity.showConfirmDialog("국어 점수 입력 오류", "국어 점수를 입력해주세요"){
                    textFieldInputStudentKorean.editText?.requestFocus()
                }
                return
            }
            // 영어점수
            if(studentEnglish.isEmpty()){
                mainActivity.showConfirmDialog("영어 점수 입력 오류", "영어 점수를 입력해주세요"){
                    textFieldInputStudentEnglish.editText?.requestFocus()
                }
                return
            }
            // 수학점수
            if(studentMath.isEmpty()){
                mainActivity.showConfirmDialog("수학 점수 입력 오류", "수학 점수를 입력해주세요"){
                    textFieldInputStudentMath.editText?.requestFocus()
                }
                return
            }

            // 점수를 정수값으로 변환한다.
            val studentKoreanInt = studentKorean.toInt()
            val studentEnglishInt = studentEnglish.toInt()
            val studentMathInt = studentMath.toInt()
            // 학년
            val studentGrade = when(toggleInputStudentGrade.checkedButtonId){
                R.id.buttonInputGradeOne -> StudentGrade.STUDENT_GRADE_1
                R.id.buttonInputGradeTow -> StudentGrade.STUDENT_GRADE_2
                else -> StudentGrade.STUDENT_GRADE_3
            }
            // 운동부
            val studentType = when(toggleInputStudentType.checkedButtonId){
                R.id.buttonInputTypeBasketBall -> StudentType.STUDENT_TYPE_BASKETBALL
                R.id.buttonInputTypeSoccer -> StudentType.STUDENT_TYPE_SOCCER
                else -> StudentType.STUDENT_TYPE_BASEBALL
            }
            // 성별
            val studentGender = when(toggleInputStudentGender.checkedButtonId){
                R.id.buttonInputGenderMale -> StudentGender.STUDENT_GENDER_MALE
                else -> StudentGender.STUDENT_GENDER_FEMALE
            }

            // ViewModel 객체에 담는다.
            val studentModel = StudentModel(
                0, studentName, studentGrade, studentType, studentGender,
                studentKoreanInt, studentEnglishInt, studentMathInt
            )

            // 데이터를 저장하는 메서드를 호출한다.
            CoroutineScope(Dispatchers.Main).launch{
                val work1 = async(Dispatchers.IO){
                    StudentRepository.insertStudentData(mainActivity, studentModel)
                }
                work1.join()
                // 이전 화면으로 돌아간다.
                mainFragment.removeFragment(SubFragmentName.INPUT_STUDENT_FRAGMENT)
            }
        }
    }
}