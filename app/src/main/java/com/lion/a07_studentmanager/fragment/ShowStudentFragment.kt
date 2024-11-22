package com.lion.a07_studentmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lion.a07_studentmanager.MainActivity
import com.lion.a07_studentmanager.R
import com.lion.a07_studentmanager.databinding.FragmentShowStudentBinding


class ShowStudentFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentShowStudentBinding: FragmentShowStudentBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentShowStudentBinding = FragmentShowStudentBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        // 툴바를 구성하는 메서드
        settingToolbarShowStudent()

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
                        mainFragment.replaceFragment(SubFragmentName.MODIFY_STUDENT_FRAGMENT,
                            true, true, null)
                    }
                    R.id.show_student_menu_remove -> {
                        mainFragment.removeFragment(SubFragmentName.SHOW_STUDENT_FRAGMENT)
                    }
                }
                true
            }
        }
    }
}