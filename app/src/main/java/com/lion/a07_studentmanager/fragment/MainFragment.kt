package com.lion.a07_studentmanager.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import com.lion.a07_studentmanager.MainActivity
import com.lion.a07_studentmanager.R
import com.lion.a07_studentmanager.databinding.FragmentMainBinding
import com.lion.a07_studentmanager.databinding.NavigationViewHeaderMainBinding
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.google.android.material.transition.MaterialSharedAxis


class MainFragment : Fragment() {

    lateinit var fragmentMainBinding: FragmentMainBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentMainBinding = FragmentMainBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        // NavigationView를 설정하는 메서드를 호출한다.
        settingNavigationViewMain()

        // 첫 화면이 보이도록 설정한다.
        replaceFragment(SubFragmentName.STUDENT_LIST_FRAGMENT, false, false, null)

        return fragmentMainBinding.root
    }

    // NavigationView를 설정하는 메서드
    fun settingNavigationViewMain(){
        fragmentMainBinding.apply {
            // 네비게이션 뷰의 헤더
            val navigationViewHeaderMainBinding = NavigationViewHeaderMainBinding.inflate(layoutInflater)
            navigationViewMain.addHeaderView(navigationViewHeaderMainBinding.root)
            // 메뉴
            navigationViewMain.inflateMenu(R.menu.navigation_main_menu)
            // 첫 번째 메뉴가 선택되어 있도록 한다.
            navigationViewMain.setCheckedItem(R.id.navigation_main_menu_student_list)
            navigationViewMain.setNavigationItemSelectedListener {
                // 메뉴 id로 분기한다
                when(it.itemId){
                    // 학생목록
                    R.id.navigation_main_menu_student_list -> {
                        replaceFragment(SubFragmentName.STUDENT_LIST_FRAGMENT, false,  false, null)
                    }
                    R.id.navigation_main_menu_student_point -> {
                        replaceFragment(SubFragmentName.STUDENT_POINT_FRAGMENT, false, false, null)
                    }
                    R.id.navigation_main_menu_student_data -> {
                        replaceFragment(SubFragmentName.STUDENT_INFO_FRAGMENT, false, false, null)
                    }
                    R.id.navigation_main_menu_calendar -> {
                        replaceFragment(SubFragmentName.PLANNER_FRAGMENT, false, false, null)
                    }
                    R.id.navigation_main_menu_setting_manager -> Log.d("test100", "관리자설정")
                }

                drawerLayoutMain.close()
                true
            }
        }
    }

    // 프래그먼트를 교체하는 함수
    fun replaceFragment(fragmentName: SubFragmentName, isAddToBackStack:Boolean, animate:Boolean, dataBundle: Bundle?){
        // 프래그먼트 객체
        val newFragment = when(fragmentName){
            // 학생 목록 화면
            SubFragmentName.STUDENT_LIST_FRAGMENT -> StudentListFragment(this)
            // 학생 정보 검색 화면
            SubFragmentName.SEARCH_STUDENT_FRAGMENT -> SearchStudentFragment(this)
            // 학생 정보 보는 화면
            SubFragmentName.SHOW_STUDENT_FRAGMENT -> ShowStudentFragment(this)
            // 학생 정보 수정 화면
            SubFragmentName.MODIFY_STUDENT_FRAGMENT ->  ModifyStudentFragment(this)
            // 학생 정보 입력 화면
            SubFragmentName.INPUT_STUDENT_FRAGMENT -> InputStudentFragment(this)
            // 학생 성적 화면
            SubFragmentName.STUDENT_POINT_FRAGMENT -> StudentPointFragment(this)
            // 학생 통계 화면
            SubFragmentName.STUDENT_INFO_FRAGMENT -> StudentInfoFragment(this)
            // 학사 일정 화면
            SubFragmentName.PLANNER_FRAGMENT -> PlannerFragment(this)
        }

        // bundle 객체가 null이 아니라면
        if(dataBundle != null){
            newFragment.arguments = dataBundle
        }

        // 프래그먼트 교체
        mainActivity.supportFragmentManager.commit {

            if(animate) {
                newFragment.exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                newFragment.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
                newFragment.enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                newFragment.returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
            }

            replace(R.id.fragmentContainerViewNavigation, newFragment)
            if(isAddToBackStack){
                addToBackStack(fragmentName.str)
            }
        }
    }

    // 프래그먼트를 BackStack에서 제거하는 메서드
    fun removeFragment(fragmentName: SubFragmentName){
        mainActivity.supportFragmentManager.popBackStack(fragmentName.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
}

// MainFragment를 통해 보여줄 Fragment들의 이름
enum class SubFragmentName(val number:Int, val str:String){
    // 학생 목록 화면
    STUDENT_LIST_FRAGMENT(1, "StudentListFragment"),
    // 학생 검색 화면
    SEARCH_STUDENT_FRAGMENT(2, "SearchStudentFragment"),
    // 학생 정보 보는 화면
    SHOW_STUDENT_FRAGMENT(3, "ShowStudentFragment"),
    // 학생 정보 수정 화면
    MODIFY_STUDENT_FRAGMENT(4, "ModifyStudentFragment"),
    // 학생 정보 입력 화면
    INPUT_STUDENT_FRAGMENT(5, "InputStudentFragment"),
    // 학생 성적 화면
    STUDENT_POINT_FRAGMENT(6, "StudentPointFragment"),
    // 학생 통계 화면
    STUDENT_INFO_FRAGMENT(7, "StudentInfoFragment"),
    // 학사 일정 화면
    PLANNER_FRAGMENT(8, "PlannerFragment"),
}