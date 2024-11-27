package com.lion.a07_studentmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lion.a07_studentmanager.MainActivity
import com.lion.a07_studentmanager.databinding.FragmentPlannerCalendarBinding

class PlannerCalendarFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentPlannerCalendarBinding: FragmentPlannerCalendarBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentPlannerCalendarBinding = FragmentPlannerCalendarBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        return fragmentPlannerCalendarBinding.root
    }


}