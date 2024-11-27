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
import com.lion.a07_studentmanager.repository.StudentRepository
import com.lion.a07_studentmanager.viewmodel.StudentModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SearchStudentFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentSearchStudentBinding: FragmentSearchStudentBinding
    lateinit var mainActivity: MainActivity

    // 리사이클러 뷰 구성을 위한 임시 데이터
//    val tempData = Array(100){
//        "학생 ${it + 1}"
//    }

    // 리사클리어뷰 구성을 위한 리스트
    var studentList = mutableListOf<StudentModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentSearchStudentBinding = FragmentSearchStudentBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        // 툴바를 구성하는 메서드를 호출한다.
        settingToolbarSearchStudent()
        // recyclerView를 구성하는 메서드
        settingRecyclerViewSearchStudent()
        // 입력 요소 설정 메서드를 호출한다.
        settingTextField()


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
                val dataBundle = Bundle()
                dataBundle.putInt("studentIdx", studentList[adapterPosition].studentIdx)

                mainFragment.replaceFragment(SubFragmentName.SHOW_STUDENT_FRAGMENT,
                    true, true, dataBundle)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderStudentSearch {
            val rowText1Binding = RowText1Binding.inflate(layoutInflater, parent, false)
            val viewHolderStudentSearch = ViewHolderStudentSearch(rowText1Binding)
            rowText1Binding.root.setOnClickListener(viewHolderStudentSearch)
            return viewHolderStudentSearch
        }

        override fun getItemCount(): Int {
            return studentList.size
        }

        override fun onBindViewHolder(holder: ViewHolderStudentSearch, position: Int) {
            holder.rowText1Binding.textViewRow.text = studentList[position].studentName
        }
    }

    // 입력 요소 설정
    fun settingTextField(){
        fragmentSearchStudentBinding.apply {
            // 검색창에 포커스를 준다.
            mainActivity.showSoftInput(textFieldSearchStudentName.editText!!)
            // 키보드의 엔터를 누르면 동작하는 리스너
            textFieldSearchStudentName.editText?.setOnEditorActionListener { v, actionId, event ->
                // 검색 데이터를 가져와 보여준다.
                CoroutineScope(Dispatchers.Main).launch {
                    val work1 = async(Dispatchers.IO){
                        val keyword = textFieldSearchStudentName.editText?.text.toString()
                        StudentRepository.selectStudentDataAllByStudentName(mainActivity, keyword)
                    }
                    studentList = work1.await()
                    recyclerViewSearchStudent.adapter?.notifyDataSetChanged()
                }
                mainActivity.hideSoftInput()
                true
            }
        }
    }
}