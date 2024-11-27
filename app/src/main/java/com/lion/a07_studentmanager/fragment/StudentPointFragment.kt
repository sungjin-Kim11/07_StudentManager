package com.lion.a07_studentmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.lion.a07_studentmanager.MainActivity
import com.lion.a07_studentmanager.R
import com.lion.a07_studentmanager.databinding.FragmentStudentPointBinding


class StudentPointFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentStudentPointBinding: FragmentStudentPointBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentStudentPointBinding = FragmentStudentPointBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        // 툴바 구성 메서드를 호출한다.
        settingToolbar()
        // 탭을 구성하는 메서드를 호출한다.
        settingTab()

        return fragmentStudentPointBinding.root
    }

    // 툴바 구성 메서드
    fun settingToolbar(){
        fragmentStudentPointBinding.apply {
            toolbarStudentPoint.title = "학생 점수 보기"
            toolbarStudentPoint.setNavigationIcon(R.drawable.menu_24px)
            toolbarStudentPoint.setNavigationOnClickListener {
                mainFragment.fragmentMainBinding.drawerLayoutMain.open()
            }
        }
    }

    // 탭을 구성하는 메서드
    fun settingTab(){
        fragmentStudentPointBinding.apply {
            // ViewPager2의 어뎁터를 설정한다. 
            viewPagerStudentPoint.adapter = ViewPagerAdapter(mainActivity.supportFragmentManager, lifecycle)

            // TabLayout과 ViewPager2가 상호 작용을 할 수 있도록 연동시켜준다.
            val tabLayoutMediator = TabLayoutMediator(tabLayoutStudentPoint, viewPagerStudentPoint) { tab, position ->
                // 각 탭에 보여줄 문자열을 새롭게 구성해줘야 한다.
                when(position){
                    0 -> tab.text = "국어점수"
                    1 -> tab.text = "영어점수"
                    2 -> tab.text = "수학점수"
                    3 -> tab.text = "총점"
                    4 -> tab.text = "평균"
                }
            }
            tabLayoutMediator.attach()
        }
    }

    // ViewPager2의 어뎁터
    inner class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle){
        // ViewPager2를 통해 보여줄 프래그먼트의 개수
        override fun getItemCount(): Int {
            return 5
        }

        // position번째에서 사용할 Fragment 객체를 생성해 반환하는 메서드
        override fun createFragment(position: Int): Fragment {
            val newFragment = when(position){
                0 -> StudentPointSubFragment(StudentPointType.STUDENT_POINT_TYPE_KOREAN)
                1 -> StudentPointSubFragment(StudentPointType.STUDENT_POINT_TYPE_ENGLISH)
                2 -> StudentPointSubFragment(StudentPointType.STUDENT_POINT_TYPE_MATH)
                3 -> StudentPointSubFragment(StudentPointType.STUDENT_POINT_TYPE_TOTAL)
                else -> StudentPointSubFragment(StudentPointType.STUDENT_POINT_TYPE_AVG)
            }
            return newFragment
        }
    }

}