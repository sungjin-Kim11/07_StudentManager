package com.lion.a07_studentmanager.fragment

import android.content.DialogInterface
import android.os.Bundle
import android.renderscript.Sampler.Value
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.lion.a07_studentmanager.MainActivity
import com.lion.a07_studentmanager.R
import com.lion.a07_studentmanager.databinding.DialogStudentListFilterBinding
import com.lion.a07_studentmanager.databinding.FragmentStudentListBinding
import com.lion.a07_studentmanager.databinding.RowText1Binding
import com.lion.a07_studentmanager.repository.StudentRepository
import com.lion.a07_studentmanager.util.StudentGender
import com.lion.a07_studentmanager.util.StudentGrade
import com.lion.a07_studentmanager.util.StudentType
import com.lion.a07_studentmanager.util.ValueClass
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

    // 필터 값
    var filterStudentGrade = ValueClass.VALUE_ALL
    var filterStudentType = ValueClass.VALUE_ALL
    var filterStudentGender = ValueClass.VALUE_ALL

    // InputStudenFragment 를 갔다 왔는지 구분하기 위한 변수
    var isBackToInputStudentFragment = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentStudentListBinding = FragmentStudentListBinding.inflate(inflater, container, false)
        mainActivity = activity as MainActivity

        // InputStudentFragment에서 돌아온 경우라면
        if(isBackToInputStudentFragment == true){
            // 필요한 작업을 한다.
            filterStudentGender = ValueClass.VALUE_ALL
            filterStudentType = ValueClass.VALUE_ALL
            filterStudentGrade = ValueClass.VALUE_ALL

            isBackToInputStudentFragment = false
        }

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

            // 네비게이션 메뉴
            toolbarStudentList.setNavigationIcon(R.drawable.menu_24px)
            toolbarStudentList.setNavigationOnClickListener {
                // 네비게이션 뷰를 보여준다.
                mainFragment.fragmentMainBinding.drawerLayoutMain.open()
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

            // 필터 옵션들 설정
            // 학년
            val gradeStr = when(filterStudentGrade){
                StudentGrade.STUDENT_GRADE_1.number -> "1학년"
                StudentGrade.STUDENT_GRADE_2.number -> "2학년"
                StudentGrade.STUDENT_GRADE_3.number -> "3학년"
                else -> "전체"
            }
            val a1 = dialogStudentListFilterBinding.textFieldStudentListGrade.editText as MaterialAutoCompleteTextView
            a1.setText(gradeStr, false)

            // 운동부
            val typeStr = when(filterStudentType){
                StudentType.STUDENT_TYPE_BASKETBALL.number -> "농구부"
                StudentType.STUDENT_TYPE_SOCCER.number -> "축구부"
                StudentType.STUDENT_TYPE_BASEBALL.number -> "야구부"
                else -> "전체"
            }
            val a2 = dialogStudentListFilterBinding.textFieldStudentListType.editText as MaterialAutoCompleteTextView
            a2.setText(typeStr, false)

            // 성별
            when(filterStudentGender){
                StudentGender.STUDENT_GENDER_MALE.number -> {
                    dialogStudentListFilterBinding.toggleStudentListGender.check(R.id.buttonGenderMale)
                }
                StudentGender.STUDENT_GENDER_FEMALE.number -> {
                    dialogStudentListFilterBinding.toggleStudentListGender.check(R.id.buttonGenderFemale)
                }
                else -> {
                    dialogStudentListFilterBinding.toggleStudentListGender.check(R.id.buttonGenderAll)
                }
            }

            builder.setPositiveButton("설정완료"){ dialogInterface: DialogInterface, i: Int ->
                // 현재 설정되어 있는 필터의 값을 변수에 담아준다.
                // 학년
                filterStudentGrade = when(dialogStudentListFilterBinding.textFieldStudentListGrade.editText?.text.toString()){
                    "1학년" -> StudentGrade.STUDENT_GRADE_1.number
                    "2학년" -> StudentGrade.STUDENT_GRADE_2.number
                    "3학년" -> StudentGrade.STUDENT_GRADE_3.number
                    else -> ValueClass.VALUE_ALL
                }
                // 운동부
                filterStudentType = when(dialogStudentListFilterBinding.textFieldStudentListType.editText?.text.toString()){
                    "농구부" -> StudentType.STUDENT_TYPE_BASKETBALL.number
                    "축구부" -> StudentType.STUDENT_TYPE_SOCCER.number
                    "야구부" -> StudentType.STUDENT_TYPE_BASEBALL.number
                    else -> ValueClass.VALUE_ALL
                }
                // 성별
                filterStudentGender = when(dialogStudentListFilterBinding.toggleStudentListGender.checkedButtonId){
                    R.id.buttonGenderMale -> StudentGender.STUDENT_GENDER_MALE.number
                    R.id.buttonGenderFemale -> StudentGender.STUDENT_GENDER_FEMALE.number
                    else -> ValueClass.VALUE_ALL
                }
                // RecyclerView를 갱신한다.
                refreshRecyclerView()
            }
            builder.setNegativeButton("취소", null)
            builder.show()
        }
    }

    // FAB를 구성하는 메서드
    fun settingFabStudentList(){
       fragmentStudentListBinding.apply {
           fabStudentList.setOnClickListener {
               // 학생 정보 입력 화면으로 이동한다.
               isBackToInputStudentFragment = true
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
            // 데이터를 필터링한다.
            filteringData()
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

    // 필터에 선택되어 있는 것만 남겨두는 메서드
    fun filteringData(){
        // 삭제할 객체를 담을 List
        val removeData = mutableListOf<StudentModel>()
        // 학년
        if(filterStudentGrade != ValueClass.VALUE_ALL){
            studentList.forEach {
                // 필터에 설정되어 있는 학년이 아닌 경우..
                if(it.studentGrade.number != filterStudentGrade){
                    removeData.add(it)
                }
            }
            // 객체들을 제거한다.
            studentList.removeAll(removeData)
        }

        removeData.clear()

        // 운동부
        if(filterStudentType != ValueClass.VALUE_ALL){
            studentList.forEach {
                // 필터에 설정되어 있는 운동부가 아닌 경우..
                if(it.studentType.number != filterStudentType){
                    removeData.add(it)
                }
            }
            studentList.removeAll(removeData)
        }

        removeData.clear()
        // 성별
        if(filterStudentGender != ValueClass.VALUE_ALL){
            studentList.forEach {
                // 필터에 설정되어 있는 성별이 아닌 경우..
                if(it.studentGender.number != filterStudentGender){
                    removeData.add(it)
                }
            }
            studentList.removeAll(removeData)
        }
    }
}