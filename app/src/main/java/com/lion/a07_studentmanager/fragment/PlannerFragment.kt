package com.lion.a07_studentmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.lion.a07_studentmanager.MainActivity
import com.lion.a07_studentmanager.R
import com.lion.a07_studentmanager.databinding.FragmentPlannerBinding

class PlannerFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentPlannerBinding: FragmentPlannerBinding
    lateinit var mainActivity: MainActivity

    // Planner Mode 값을 담을 변수
    var plannerMode = PlannerMode.PLANNER_MODE_LIST

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentPlannerBinding = FragmentPlannerBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        settingToolbar()

        return fragmentPlannerBinding.root
    }

    // 툴바를 구성하는 메서드
    fun settingToolbar(){
        fragmentPlannerBinding.apply {
            toolbarPlanner.title = "학사 일정 관리"

            toolbarPlanner.setNavigationIcon(R.drawable.menu_24px)
            toolbarPlanner.setNavigationOnClickListener {
                mainFragment.fragmentMainBinding.drawerLayoutMain.open()
            }

            toolbarPlanner.inflateMenu(R.menu.planner_main_menu)
            settingMenuItemVisible()
            changePlannerSubFragment()

            toolbarPlanner.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.planner_menu_add -> {

                    }
                    R.id.planner_menu_list -> {
                        plannerMode = PlannerMode.PLANNER_MODE_LIST
                        settingMenuItemVisible()
                        changePlannerSubFragment()
                    }
                    R.id.planner_menu_calendar -> {
                        plannerMode = PlannerMode.PLANNER_MODE_CALENDAR
                        settingMenuItemVisible()
                        changePlannerSubFragment()
                    }
                }

                true
            }
        }
    }

    // 학사 일정 모드에 따라 메뉴 노출을 설정하는 메서드
    fun settingMenuItemVisible(){
        fragmentPlannerBinding.apply {
            // 학사 일정 모드에 따라 메뉴를 숨긴다.
            val item1 = toolbarPlanner.menu.findItem(R.id.planner_menu_calendar)
            val item2 = toolbarPlanner.menu.findItem(R.id.planner_menu_list)
            when(plannerMode){
                PlannerMode.PLANNER_MODE_LIST -> {
                    item1.isVisible = true
                    item2.isVisible = false
                }
                PlannerMode.PLANNER_MODE_CALENDAR -> {
                    item1.isVisible = false
                    item2.isVisible = true
                }
            }
        }
    }

    fun changePlannerSubFragment(){
        when(plannerMode){
            PlannerMode.PLANNER_MODE_LIST -> {
                mainActivity.supportFragmentManager.commit {
                    replace(R.id.fragmentContainerViewPlanner, PlannerListFragment(mainFragment))
                }
            }
            PlannerMode.PLANNER_MODE_CALENDAR -> {
                mainActivity.supportFragmentManager.commit {
                    replace(R.id.fragmentContainerViewPlanner, PlannerCalendarFragment(mainFragment))
                }
            }
        }
    }
}

// 학사 일정 모드
enum class PlannerMode(val number:Int, var str:String){
    PLANNER_MODE_LIST(1, "PlannerModeList"),
    PLANNER_MODE_CALENDAR(2, "PlannerModelCalendar")
}