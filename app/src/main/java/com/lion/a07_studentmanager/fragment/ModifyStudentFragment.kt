package com.lion.a07_studentmanager.fragment

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lion.a07_studentmanager.MainActivity
import com.lion.a07_studentmanager.R
import com.lion.a07_studentmanager.database.StudentVO
import com.lion.a07_studentmanager.databinding.FragmentModifyStudentBinding
import com.lion.a07_studentmanager.repository.StudentRepository
import com.lion.a07_studentmanager.util.StudentGender
import com.lion.a07_studentmanager.util.StudentGrade
import com.lion.a07_studentmanager.util.StudentType
import com.lion.a07_studentmanager.viewmodel.StudentModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class ModifyStudentFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentModifyStudentBinding: FragmentModifyStudentBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentModifyStudentBinding = FragmentModifyStudentBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        // 툴바를 구성하는 메서드 호출
        settingToolbarModifyStudent()
        // 버튼을 구성하는 메서드를 호출한다.
        settingButton()
        // 입력 요소를 구성하는 메서드
        settingTextField()


        return fragmentModifyStudentBinding.root
    }

    // 툴바를 구성하는 메서드
    fun settingToolbarModifyStudent(){
        fragmentModifyStudentBinding.apply {
            toolbarModifyStudent.title = "학생 정보 수정"
            toolbarModifyStudent.setNavigationIcon(R.drawable.arrow_back_24px)
            toolbarModifyStudent.setNavigationOnClickListener {
                mainFragment.removeFragment(SubFragmentName.MODIFY_STUDENT_FRAGMENT)
            }
        }
    }

    // 입력 요소를 구성하는 메서드
    fun settingTextField(){
        fragmentModifyStudentBinding.apply {
            // 학생 번호를 가져온다.
            val studentIdx = arguments?.getInt("studentIdx")!!
            // 학생 데이터를 가져온다.
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    StudentRepository.selectStudentDataByStudentIdx(mainActivity, studentIdx)
                }
                val studentModel = work1.await()

                textFieldModifyStudentName.editText?.setText(studentModel.studentName)

                when(studentModel.studentGrade){
                    StudentGrade.STUDENT_GRADE_1 -> toggleModifyStudentGrade.check(R.id.buttonGradeOne)
                    StudentGrade.STUDENT_GRADE_2 -> toggleModifyStudentGrade.check(R.id.buttonGradeTow)
                    StudentGrade.STUDENT_GRADE_3 -> toggleModifyStudentGrade.check(R.id.buttonGradeThree)
                }

                when(studentModel.studentType){
                    StudentType.STUDENT_TYPE_BASEBALL -> toggleModifyStudentType.check(R.id.buttonTypeBaseBall)
                    StudentType.STUDENT_TYPE_BASKETBALL -> toggleModifyStudentType.check(R.id.buttonTypeBasketBall)
                    StudentType.STUDENT_TYPE_SOCCER -> toggleModifyStudentType.check(R.id.buttonTypeSoccer)
                }

                when(studentModel.studentGender){
                    StudentGender.STUDENT_GENDER_MALE -> toggleModifyStudentGender.check(R.id.buttonGenderMale)
                    StudentGender.STUDENT_GENDER_FEMALE -> toggleModifyStudentGender.check(R.id.buttonGenderFemale)
                }

                textFieldModifyStudentKorean.editText?.setText(studentModel.studentKorean.toString())
                textFieldModifyStudentEnglish.editText?.setText(studentModel.studentEnglish.toString())
                textFieldModifyStudentMath.editText?.setText(studentModel.studentMath.toString())
            }
        }
    }

    // 버튼을 구성하는 메서드
    fun settingButton(){
        fragmentModifyStudentBinding.apply {
            buttonModifyStudentSubmit.setOnClickListener {
                // 다이얼로그를 띄운다.
                val builder = MaterialAlertDialogBuilder(mainActivity)
                builder.setTitle("학생 정보 수정")
                builder.setMessage("학생 정보 수정시 복구가 불가능합니다")
                builder.setNegativeButton("취소", null)
                builder.setPositiveButton("수정"){ dialogInterface: DialogInterface, i: Int ->
                    // 수정 메서드를 호출한다.
                    processingModifyStudentData()
                }
                builder.show()
            }
        }
    }

    // 학생 정보 수정 처리 메서드
    fun processingModifyStudentData(){
        fragmentModifyStudentBinding.apply {
            // 데이터를 추출한다.
            val studentIdx = arguments?.getInt("studentIdx")!!
            val studentName = textFieldModifyStudentName.editText?.text.toString()
            // 학년
            val studentGrade = when(toggleModifyStudentGrade.checkedButtonId){
                R.id.buttonGradeOne -> StudentGrade.STUDENT_GRADE_1
                R.id.buttonGradeTow -> StudentGrade.STUDENT_GRADE_2
                else -> StudentGrade.STUDENT_GRADE_3
            }
            // 운동부
            val studentType = when(toggleModifyStudentType.checkedButtonId){
                R.id.buttonTypeBasketBall -> StudentType.STUDENT_TYPE_BASKETBALL
                R.id.buttonTypeSoccer -> StudentType.STUDENT_TYPE_SOCCER
                else -> StudentType.STUDENT_TYPE_BASEBALL
            }
            // 성별
            val studentGender = when(toggleModifyStudentGender.checkedButtonId){
                R.id.buttonGenderMale -> StudentGender.STUDENT_GENDER_MALE
                else -> StudentGender.STUDENT_GENDER_FEMALE
            }
            val studentKorean = textFieldModifyStudentKorean.editText?.text.toString().toInt()
            val studentEnglish = textFieldModifyStudentEnglish.editText?.text.toString().toInt()
            val studentMath = textFieldModifyStudentMath.editText?.text.toString().toInt()

            // 모델에 담아둔다.
            val studentModel = StudentModel(
                studentIdx, studentName, studentGrade, studentType, studentGender,
                studentKorean, studentEnglish, studentMath
            )
            // 수정 처리 메서를 호출해준다.
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    StudentRepository.updateStudentDataByStudentIdx(mainActivity, studentModel)
                }
                work1.join()
                // 이전 화면으로 돌아간다.
                mainFragment.removeFragment(SubFragmentName.MODIFY_STUDENT_FRAGMENT)
            }
        }
    }
}








