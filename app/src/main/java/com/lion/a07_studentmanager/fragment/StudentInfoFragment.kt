package com.lion.a07_studentmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lion.a07_studentmanager.MainActivity
import com.lion.a07_studentmanager.R
import com.lion.a07_studentmanager.databinding.FragmentStudentInfoBinding
import com.lion.a07_studentmanager.repository.StudentRepository
import com.lion.a07_studentmanager.util.StudentGrade
import com.lion.a07_studentmanager.util.StudentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class StudentInfoFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentStudentInfoBinding: FragmentStudentInfoBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentStudentInfoBinding = FragmentStudentInfoBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        // 툴바를 구성하는 메서드를 호출한다.
        settingToolbar()
        // 입력 요소를 설정하는 메서드
        settingTextField()

        return fragmentStudentInfoBinding.root
    }

    // 툴바를 구성하는 메서드
    fun settingToolbar(){
        fragmentStudentInfoBinding.apply {
            toolbarStudentInfo.title = "학생 통계"
            toolbarStudentInfo.setNavigationIcon(R.drawable.menu_24px)
            toolbarStudentInfo.setNavigationOnClickListener {
                mainFragment.fragmentMainBinding.drawerLayoutMain.open()
            }
        }
    }

    // 입력 요소를 설정하는 메서드
    fun settingTextField(){
        fragmentStudentInfoBinding.apply {
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    StudentRepository.selectStudentDataAll(mainActivity)
                }
                val studentList = work1.await()

                // 각 정보를 담을 변수들
                var gradeOneCount = 0
                var gradeTwoCount = 0
                var gradeThreeCount = 0
                var baseBallCount = 0
                var soccerCount = 0
                var basketBallCount = 0
                var koreanTotal = 0
                var englishTotal = 0
                var mathTotal = 0

                studentList.forEach {
                    when(it.studentGrade){
                        StudentGrade.STUDENT_GRADE_1 -> gradeOneCount++
                        StudentGrade.STUDENT_GRADE_2 -> gradeTwoCount++
                        StudentGrade.STUDENT_GRADE_3 -> gradeThreeCount++
                    }

                    when(it.studentType){
                        StudentType.STUDENT_TYPE_BASEBALL -> basketBallCount++
                        StudentType.STUDENT_TYPE_SOCCER -> soccerCount++
                        StudentType.STUDENT_TYPE_BASKETBALL -> baseBallCount++
                    }

                    koreanTotal += it.studentKorean
                    englishTotal += it.studentEnglish
                    mathTotal += it.studentMath
                }
                val totalCount = gradeOneCount + gradeTwoCount + gradeThreeCount
                var koreanAvg = koreanTotal / studentList.size
                var englishAvg = englishTotal / studentList.size
                var mathAvg = mathTotal / studentList.size

                var pointTotal = koreanTotal + englishTotal + mathTotal
                var pointAvg = pointTotal / (studentList.size * 3)

                textFieldStudentInfoTotalCount.editText?.setText("$totalCount 명")
                textFieldStudentInfoGradeOneCount.editText?.setText("$gradeOneCount 명")
                textFieldStudentInfoGradeTwoCount.editText?.setText("$gradeTwoCount 명")
                textFieldStudentInfoGradeThreeCount.editText?.setText("$gradeThreeCount 명")
                textFieldStudentInfoTypeBaseBallCount.editText?.setText("$baseBallCount 명")
                textFieldStudentInfoTypeSoccerCount.editText?.setText("$soccerCount 명")
                textFieldStudentInfoTypeBasketBallCount.editText?.setText("$basketBallCount 명")
                textFieldStudentInfoTypeKoreanTotal.editText?.setText("$koreanTotal 점")
                textFieldStudentInfoTypeEnglishTotal.editText?.setText("$englishTotal 점")
                textFieldStudentInfoTypeMathTotal.editText?.setText("$mathTotal 점")
                textFieldStudentInfoTypeKoreanAvg.editText?.setText("$koreanAvg 점")
                textFieldStudentInfoTypeEnglishAvg.editText?.setText("$englishAvg 점")
                textFieldStudentInfoTypeMathAvg.editText?.setText("$mathAvg 점")
                textFieldStudentInfoTypeTotalAll.editText?.setText("$pointTotal 점")
                textFieldStudentInfoTypeAvgAll.editText?.setText("$pointAvg 점")
            }
        }
    }
}