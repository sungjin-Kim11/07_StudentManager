package com.lion.a07_studentmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lion.a07_studentmanager.MainActivity
import com.lion.a07_studentmanager.R
import com.lion.a07_studentmanager.databinding.FragmentModifyStudentBinding


class ModifyStudentFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentModifyStudentBinding: FragmentModifyStudentBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentModifyStudentBinding = FragmentModifyStudentBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        // 툴바를 구성하는 메서드 호출
        settingToolbarModifyStudent()

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
}