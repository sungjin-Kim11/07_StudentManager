package com.lion.a07_studentmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lion.a07_studentmanager.MainActivity
import com.lion.a07_studentmanager.R
import com.lion.a07_studentmanager.databinding.FragmentInputStudentBinding

class InputStudentFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentInputStudentBinding: FragmentInputStudentBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentInputStudentBinding = FragmentInputStudentBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        return fragmentInputStudentBinding.root
    }

}