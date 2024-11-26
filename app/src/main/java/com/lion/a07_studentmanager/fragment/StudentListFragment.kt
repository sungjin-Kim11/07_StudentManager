package com.lion.a07_studentmanager.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.lion.a07_studentmanager.MainActivity
import com.lion.a07_studentmanager.R
import com.lion.a07_studentmanager.databinding.DialogStudentListFilterBinding
import com.lion.a07_studentmanager.databinding.FragmentStudentListBinding
import com.lion.a07_studentmanager.databinding.RowText1Binding
import com.lion.a07_studentmanager.repository.StudentRepository
import com.lion.a07_studentmanager.viewmodel.StudentModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class StudentListFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentStudentListBinding: FragmentStudentListBinding
    lateinit var mainActivity: MainActivity

    // RecyclerView 구성을 위한 임시데이터
//    val tempData = Array(100){
//        "학생 ${it + 1}"
//    }

    // 학생 데이터를 담고 있는 리스트
    var studentList = mutableListOf<StudentModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentStudentListBinding = FragmentStudentListBinding.inflate(inflater, container, false)
        mainActivity = activity as MainActivity

        // 툴바를 구성하는 메서드를 호출한다.
        settingToolbarStudentList()
        // RecyclerView를 구성하는 메서드를 호출한다.
        settingRecyclerViewStudentList()
        // FAB를 구성하는 메서드를 호출한다.
        settingFabStudentList()

        return fragmentStudentListBinding.root
    }

    // 툴바를 구성하는 메서드
    fun settingToolbarStudentList(){
        fragmentStudentListBinding.apply {
            toolbarStudentList.title = "학생 목록"
            toolbarStudentList.inflateMenu(R.menu.toolbar_student_list_menu)
            toolbarStudentList.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.student_list_menu_filter ->{
                        // 필터 다이얼로그를 띄우는 메서드를 호출한다.
                        showFilterDialog()
                    }
                    R.id.student_list_menu_search -> {
                        // 검색화면으로 이동한다.
                        mainFragment.replaceFragment(SubFragmentName.SEARCH_STUDENT_FRAGMENT,
                            true, true, null)
                    }
                }
                true
            }
        }
    }

    // RecyclerView를 구성하는 메서드
    fun settingRecyclerViewStudentList(){
        fragmentStudentListBinding.apply {
            recyclerViewStudentList.adapter = RecyclerViewStudentListAdapter()
            recyclerViewStudentList.layoutManager = LinearLayoutManager(mainActivity)
            val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewStudentList.addItemDecoration(deco)

            // 데이터를 읽어와 리사이클러 뷰를 갱신한다.
            refreshRecyclerView()
        }
    }

    // 필터 다이얼로그를 띄우는 메서드
    fun showFilterDialog(){
        fragmentStudentListBinding.apply {
            val builder = MaterialAlertDialogBuilder(mainActivity)
            builder.setTitle("검색 필터 설정")
            val dialogStudentListFilterBinding = DialogStudentListFilterBinding.inflate(layoutInflater)
            builder.setView(dialogStudentListFilterBinding.root)
            builder.setPositiveButton("설정완료", null)
            builder.setNegativeButton("취소", null)
            builder.show()
        }
    }

    // FAB를 구성하는 메서드
    fun settingFabStudentList(){
       fragmentStudentListBinding.apply {
           fabStudentList.setOnClickListener {
               // 학생 정보 입력 화면으로 이동한다.
               mainFragment.replaceFragment(SubFragmentName.INPUT_STUDENT_FRAGMENT, true, true, null)
           }
       }
    }

    // 데이터 베이스에서 데이터를 읽어와 RecyclerView를 갱신한다.
    fun refreshRecyclerView(){
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                // 데이터를 읽어온다.
                StudentRepository.selectStudentDataAll(mainActivity)
            }
            studentList = work1.await()
            // RecyclerView를 갱신한다.
            fragmentStudentListBinding.recyclerViewStudentList.adapter?.notifyDataSetChanged()
        }
    }

    // RecyclerView의 어뎁터
    inner class RecyclerViewStudentListAdapter : RecyclerView.Adapter<RecyclerViewStudentListAdapter.ViewHolderStudentList>(){
        // ViewHolder
        inner class ViewHolderStudentList(val rowText1Binding: RowText1Binding) : RecyclerView.ViewHolder(rowText1Binding.root), OnClickListener{
            override fun onClick(v: View?) {
                // 학생의 번호를 담는다.
                val dataBundle = Bundle()
                dataBundle.putInt("studentIdx", studentList[adapterPosition].studentIdx)
                // 학생 정보를 보는 화면으로 이동한다.
                mainFragment.replaceFragment(SubFragmentName.SHOW_STUDENT_FRAGMENT, true, true, dataBundle)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderStudentList {
            val rowText1Binding = RowText1Binding.inflate(layoutInflater, parent, false)
            val viewHolderStudentList = ViewHolderStudentList(rowText1Binding)
            rowText1Binding.root.setOnClickListener(viewHolderStudentList)
            return viewHolderStudentList
        }

        override fun getItemCount(): Int {
            return studentList.size
        }

        override fun onBindViewHolder(holder: ViewHolderStudentList, position: Int) {
            holder.rowText1Binding.textViewRow.text = studentList[position].studentName
        }
    }
}