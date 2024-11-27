package com.lion.a07_studentmanager.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.lion.a07_studentmanager.MainActivity
import com.lion.a07_studentmanager.R
import com.lion.a07_studentmanager.databinding.FragmentStudentPointSubBinding
import com.lion.a07_studentmanager.databinding.RowText1Binding
import com.lion.a07_studentmanager.repository.StudentRepository
import com.lion.a07_studentmanager.viewmodel.StudentModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class StudentPointSubFragment(val studentPointType: StudentPointType) : Fragment() {

    lateinit var fragmentStudentPointSubBinding: FragmentStudentPointSubBinding
    lateinit var mainActivity: MainActivity

    // 정렬 기준값
    var studentPointSort = StudentPointSort.STUDENT_POINT_SORT_ASCENDING

    // RecyclerView 구성을 위한 임시데이터
//    val tempData = Array(100){
//        "학생 ${it + 1}"
//    }

    // RecyclerView 구성을 위한 리스트
    var studentList = mutableListOf<StudentModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentStudentPointSubBinding = FragmentStudentPointSubBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        // RecyclerView를 구성하는 메서드를 호출한다.
        settingRecyclerView()
        // Chip을 구성하는 메서드를 호출한다.
        settingChip()
        //  데이터를 가져오는 메서드
        gettingStudentPointData()

        return fragmentStudentPointSubBinding.root
    }

    //  데이터를 가져오는 메서드
    fun gettingStudentPointData(){
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                StudentRepository.selectStudentDataAll(mainActivity)
            }
            studentList = work1.await()
            // 각 학생의 총점과 평균을 구해 담아준다.
            studentList.forEach {
                it.studentTotal = it.studentKorean + it.studentEnglish + it.studentMath
                it.studentAvg = it.studentTotal / 3
            }
            // 보여주고자 하는 정보를 기준으로 분기한다.
            when(studentPointType){
                StudentPointType.STUDENT_POINT_TYPE_KOREAN -> {
                    when(studentPointSort){
                        StudentPointSort.STUDENT_POINT_SORT_ASCENDING -> {
                            studentList.sortBy {
                                it.studentKorean
                            }
                        }
                        StudentPointSort.STUDENT_POINT_SORT_DESCENDING -> {
                            studentList.sortByDescending {
                                it.studentKorean
                            }
                        }
                    }
                }
                StudentPointType.STUDENT_POINT_TYPE_ENGLISH -> {
                    when(studentPointSort){
                        StudentPointSort.STUDENT_POINT_SORT_ASCENDING -> {
                            studentList.sortBy {
                                it.studentEnglish
                            }
                        }
                        StudentPointSort.STUDENT_POINT_SORT_DESCENDING -> {
                            studentList.sortByDescending {
                                it.studentEnglish
                            }
                        }
                    }
                }
                StudentPointType.STUDENT_POINT_TYPE_MATH -> {
                    when(studentPointSort){
                        StudentPointSort.STUDENT_POINT_SORT_ASCENDING -> {
                            studentList.sortBy {
                                it.studentMath
                            }
                        }
                        StudentPointSort.STUDENT_POINT_SORT_DESCENDING -> {
                            studentList.sortByDescending {
                                it.studentMath
                            }
                        }
                    }
                }
                StudentPointType.STUDENT_POINT_TYPE_TOTAL -> {
                    when(studentPointSort){
                        StudentPointSort.STUDENT_POINT_SORT_ASCENDING -> {
                            studentList.sortBy {
                                it.studentTotal
                            }
                        }
                        StudentPointSort.STUDENT_POINT_SORT_DESCENDING -> {
                            studentList.sortByDescending {
                                it.studentTotal
                            }
                        }
                    }
                }
                StudentPointType.STUDENT_POINT_TYPE_AVG -> {
                    when(studentPointSort){
                        StudentPointSort.STUDENT_POINT_SORT_ASCENDING -> {
                            studentList.sortBy {
                                it.studentAvg
                            }
                        }
                        StudentPointSort.STUDENT_POINT_SORT_DESCENDING -> {
                            studentList.sortByDescending {
                                it.studentAvg
                            }
                        }
                    }
                }
            }

            // RecyclerView 갱신
            fragmentStudentPointSubBinding.recyclerViewStudentPoint.adapter?.notifyDataSetChanged()
        }
    }

    // Chip을 구성하는 메서드
    fun settingChip(){
        fragmentStudentPointSubBinding.apply {
            chipStudentPointOrdering.setOnClickListener{
                when(studentPointSort){
                    StudentPointSort.STUDENT_POINT_SORT_ASCENDING -> {
                        chipStudentPointOrdering.text = "내림차순 ▽"
                        studentPointSort = StudentPointSort.STUDENT_POINT_SORT_DESCENDING
                    }
                    StudentPointSort.STUDENT_POINT_SORT_DESCENDING -> {
                        chipStudentPointOrdering.text = "오름차순 △"
                        studentPointSort = StudentPointSort.STUDENT_POINT_SORT_ASCENDING
                    }
                }
                //  데이터를 가져오는 메서드
                gettingStudentPointData()
            }
        }
    }

    // RecyclerView를 구성하는 메서드
    fun settingRecyclerView(){
        fragmentStudentPointSubBinding.apply {
            recyclerViewStudentPoint.adapter = RecyclerViewStudentPointSubAdapter()
            recyclerViewStudentPoint.layoutManager = LinearLayoutManager(mainActivity)
            val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewStudentPoint.addItemDecoration(deco)
        }
    }

    // RecyclerView의 어뎁터
    inner class RecyclerViewStudentPointSubAdapter : RecyclerView.Adapter<RecyclerViewStudentPointSubAdapter.ViewHolderStudentPointSub>(){
        // ViewHolder
        inner class ViewHolderStudentPointSub(val rowText1Binding: RowText1Binding) : RecyclerView.ViewHolder(rowText1Binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderStudentPointSub {
            val rowText1Binding = RowText1Binding.inflate(layoutInflater, parent, false)
            val viewHolderStudentPointSub = ViewHolderStudentPointSub(rowText1Binding)
            return viewHolderStudentPointSub
        }

        override fun getItemCount(): Int {
            return studentList.size
        }

        override fun onBindViewHolder(holder: ViewHolderStudentPointSub, position: Int) {
            holder.rowText1Binding.textViewRow.text = studentList[position].studentName

            // 보여주고자 하는 정보를 기준으로 분기한다.
            when(studentPointType){
                StudentPointType.STUDENT_POINT_TYPE_KOREAN -> {
                    holder.rowText1Binding.textViewRow.append(" : ${studentList[position].studentKorean}")
                }
                StudentPointType.STUDENT_POINT_TYPE_ENGLISH -> {
                    holder.rowText1Binding.textViewRow.append(" : ${studentList[position].studentEnglish}")
                }
                StudentPointType.STUDENT_POINT_TYPE_MATH -> {
                    holder.rowText1Binding.textViewRow.append(" : ${studentList[position].studentMath}")
                }
                StudentPointType.STUDENT_POINT_TYPE_TOTAL -> {
                    holder.rowText1Binding.textViewRow.append(" : ${studentList[position].studentTotal}")
                }
                StudentPointType.STUDENT_POINT_TYPE_AVG -> {
                    holder.rowText1Binding.textViewRow.append(" : ${studentList[position].studentAvg}")
                }
            }
        }
    }
}

// 프래그먼트를 통해 보고자 하는 정보 이름
enum class StudentPointType(val number:Int, val str:String){
    STUDENT_POINT_TYPE_KOREAN(1, "국어점수"),
    STUDENT_POINT_TYPE_ENGLISH(2, "영어점수"),
    STUDENT_POINT_TYPE_MATH(3, "수학점수"),
    STUDENT_POINT_TYPE_TOTAL(4, "총점"),
    STUDENT_POINT_TYPE_AVG(5, "평균")
}

// 정렬 기준 값
enum class StudentPointSort(val number:Int, var str:String){
    STUDENT_POINT_SORT_ASCENDING(1, "오름차순"),
    STUDENT_POINT_SORT_DESCENDING(2, "내림차순")
}
