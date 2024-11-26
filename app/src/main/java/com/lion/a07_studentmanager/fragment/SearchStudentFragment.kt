package com.lion.a07_studentmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.lion.a07_studentmanager.MainActivity
import com.lion.a07_studentmanager.R
import com.lion.a07_studentmanager.databinding.FragmentSearchStudentBinding
import com.lion.a07_studentmanager.databinding.RowText1Binding

class SearchStudentFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentSearchStudentBinding: FragmentSearchStudentBinding
    lateinit var mainActivity: MainActivity

    // 리사이클러 뷰 구성을 위한 임시 데이터
    val tempData = Array(100){
        "학생 ${it + 1}"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentSearchStudentBinding = FragmentSearchStudentBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        // 툴바를 구성하는 메서드를 호출한다.
        settingToolbarSearchStudent()
        // recyclerView를 구성하는 메서드
        settingRecyclerViewSearchStudent()

        return fragmentSearchStudentBinding.root
    }

    // 툴바를 구성하는 메서드
    fun settingToolbarSearchStudent(){
        fragmentSearchStudentBinding.apply {
            toolbarSearchStudent.title = "학생 정보 검색"

            toolbarSearchStudent.setNavigationIcon(R.drawable.arrow_back_24px)
            toolbarSearchStudent.setNavigationOnClickListener {
                mainFragment.removeFragment(SubFragmentName.SEARCH_STUDENT_FRAGMENT)
            }
        }
    }

    // recyclerView를 구성하는 메서드
    fun settingRecyclerViewSearchStudent(){
        fragmentSearchStudentBinding.apply {
            recyclerViewSearchStudent.adapter = RecyclerViewStudentSearchAdapter()
            recyclerViewSearchStudent.layoutManager = LinearLayoutManager(mainActivity)
            val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewSearchStudent.addItemDecoration(deco)
        }
    }

    // Recyclerview의 어뎁터
    inner class RecyclerViewStudentSearchAdapter : RecyclerView.Adapter<RecyclerViewStudentSearchAdapter.ViewHolderStudentSearch>(){
        inner class ViewHolderStudentSearch(val rowText1Binding: RowText1Binding) : RecyclerView.ViewHolder(rowText1Binding.root), OnClickListener{
            override fun onClick(v: View?) {
                // 학생 정보를 보는 화면으로 이동한다.
                mainFragment.replaceFragment(SubFragmentName.SHOW_STUDENT_FRAGMENT, true, true, null)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderStudentSearch {
            val rowText1Binding = RowText1Binding.inflate(layoutInflater, parent, false)
            val viewHolderStudentSearch = ViewHolderStudentSearch(rowText1Binding)
            rowText1Binding.root.setOnClickListener(viewHolderStudentSearch)
            return viewHolderStudentSearch
        }

        override fun getItemCount(): Int {
            return tempData.size
        }

        override fun onBindViewHolder(holder: ViewHolderStudentSearch, position: Int) {
            holder.rowText1Binding.textViewRow.text = tempData[position]
        }
    }
}