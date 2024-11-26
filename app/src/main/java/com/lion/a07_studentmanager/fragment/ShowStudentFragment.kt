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
import com.lion.a07_studentmanager.databinding.FragmentShowStudentBinding
import com.lion.a07_studentmanager.repository.StudentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class ShowStudentFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentShowStudentBinding: FragmentShowStudentBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentShowStudentBinding = FragmentShowStudentBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        // 툴바를 구성하는 메서드
        settingToolbarShowStudent()
        // 학생 데이터를 가져와 보여주는 메서드
        settingTextField()

        return fragmentShowStudentBinding.root
    }

    // 툴바를 구성하는 메서드
    fun settingToolbarShowStudent(){
        fragmentShowStudentBinding.apply {
            toolbarShowStudent.title = "학생 정보 보기"

            toolbarShowStudent.setNavigationIcon(R.drawable.arrow_back_24px)
            toolbarShowStudent.setNavigationOnClickListener {
                mainFragment.removeFragment(SubFragmentName.SHOW_STUDENT_FRAGMENT)
            }

            toolbarShowStudent.inflateMenu(R.menu.toolbar_show_student_menu)
            toolbarShowStudent.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.show_student_menu_modify -> {
                        // 정보 수정 화면으로 이동한다.

                        // 학생 번호를 추출하여 전달해준다.
                        val studentIdx = arguments?.getInt("studentIdx")
                        val dataBundle = Bundle()
                        dataBundle.putInt("studentIdx", studentIdx!!)

                        mainFragment.replaceFragment(SubFragmentName.MODIFY_STUDENT_FRAGMENT,
                            true, true, dataBundle)
                    }
                    R.id.show_student_menu_remove -> {
                        // mainFragment.removeFragment(SubFragmentName.SHOW_STUDENT_FRAGMENT)
                        // 다이얼로를 띄운다.
                        val builder = MaterialAlertDialogBuilder(mainActivity)
                        builder.setTitle("학생 정보 삭제")
                        builder.setMessage("학생 정보 삭제시 복구가 불가능합니다")
                        builder.setNegativeButton("취소", null)
                        builder.setPositiveButton("삭제"){ dialogInterface: DialogInterface, i: Int ->
                            // 삭제 메서드를 호출한다.
                            deleteStudentData()
                        }
                        builder.show()
                    }
                }
                true
            }
        }
    }

    // 학생 데이터를 가져와 보여주는 메서드
    fun settingTextField(){
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                // 학생 번호를 가져온다.
                val studentIdx = arguments?.getInt("studentIdx")
                // 학생 데이터를 가져온다.
                StudentRepository.selectStudentDataByStudentIdx(mainActivity, studentIdx!!)
            }
            val studentModel = work1.await()

            fragmentShowStudentBinding.apply {
                textFieldShowStudentName.editText?.setText(studentModel.studentName)
                textFieldShowStudentGrade.editText?.setText(studentModel.studentGrade.str)
                textFieldShowStudentType.editText?.setText(studentModel.studentType.str)
                textFieldShowStudentGender.editText?.setText(studentModel.studentGender.str)
                textFieldShowStudentKorean.editText?.setText(studentModel.studentKorean.toString())
                textFieldShowStudentEnglish.editText?.setText(studentModel.studentEnglish.toString())
                textFieldShowStudentMath.editText?.setText(studentModel.studentMath.toString())
            }
        }
    }

    // 학생 정보를 삭제하는 메서드
    fun deleteStudentData(){
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                // 삭제한다.
                val studentIdx = arguments?.getInt("studentIdx")!!
                StudentRepository.deleteStudentDataByStudentIdx(mainActivity, studentIdx)
            }
            work1.join()
            // 학생 목록을 보는 화면으로 돌아간다.
            mainFragment.removeFragment(SubFragmentName.SHOW_STUDENT_FRAGMENT)
        }
    }
}