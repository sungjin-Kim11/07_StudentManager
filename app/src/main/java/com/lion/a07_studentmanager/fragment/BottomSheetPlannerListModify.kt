package com.lion.a07_studentmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lion.a07_studentmanager.MainActivity
import com.lion.a07_studentmanager.R
import com.lion.a07_studentmanager.databinding.FragmentBottomSheetPlannerListModifyBinding

class BottomSheetPlannerListModify(val plannerListFragment: PlannerListFragment) : BottomSheetDialogFragment() {

    lateinit var bottomSheetPlannerListModifyBinding: FragmentBottomSheetPlannerListModifyBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        bottomSheetPlannerListModifyBinding = FragmentBottomSheetPlannerListModifyBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        return bottomSheetPlannerListModifyBinding.root
    }
}