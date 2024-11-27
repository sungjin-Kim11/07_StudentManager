package com.lion.a07_studentmanager.fragment

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemLongClickListener
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.lion.a07_studentmanager.MainActivity
import com.lion.a07_studentmanager.R
import com.lion.a07_studentmanager.databinding.BottomSheetMenuPlannerListBinding
import com.lion.a07_studentmanager.databinding.FragmentPlannerListBinding
import com.lion.a07_studentmanager.databinding.RowText2Binding

class PlannerListFragment(val mainFragment: MainFragment) : Fragment() {


    lateinit var fragmentPlannerListBinding: FragmentPlannerListBinding
    lateinit var mainActivity: MainActivity

    // RecyclerView 구성을 위한 임시 데이터
    val tempMainTitleData = Array(50){
        "학사 일정 이름 $it"
    }

    val tempSubTitleData = Array(50){
        "2024-11-${it + 1}"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentPlannerListBinding = FragmentPlannerListBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        // RecyclerView를 구성하는 메서드를 호출한다.
        settingRecyclerView()

        return fragmentPlannerListBinding.root
    }

    // RecyclerView를 구성하는 메서드
    fun settingRecyclerView(){
        fragmentPlannerListBinding.apply {
            recyclerViewPlannerList.adapter = RecyclerViewPlannerListAdapter()
            recyclerViewPlannerList.layoutManager = LinearLayoutManager(mainActivity)
            val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewPlannerList.addItemDecoration(deco)
        }
    }

    // RecyclerView의 어뎁터
    inner class RecyclerViewPlannerListAdapter : RecyclerView.Adapter<RecyclerViewPlannerListAdapter.ViewHolderPlannerList>(){
        // View Holder
        inner class ViewHolderPlannerList(val rowText2Binding: RowText2Binding) : RecyclerView.ViewHolder(rowText2Binding.root), OnLongClickListener{
            override fun onLongClick(v: View?): Boolean {
                // BottomSheet를 띄워준다
                showMenuBottomSheet()
                return false
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPlannerList {
            val rowText2Binding = RowText2Binding.inflate(layoutInflater, parent, false)
            val viewHolderPlannerList = ViewHolderPlannerList(rowText2Binding)
            rowText2Binding.root.setOnLongClickListener(viewHolderPlannerList)
            return viewHolderPlannerList
        }

        override fun getItemCount(): Int {
            return tempMainTitleData.size
        }

        override fun onBindViewHolder(holder: ViewHolderPlannerList, position: Int) {
            holder.rowText2Binding.textViewRowText2MainTitle.text = tempMainTitleData[position]
            holder.rowText2Binding.textViewRowText2SubTitle.text = tempSubTitleData[position]
        }
    }

    // 수정 삭제를 선택하기 위한 BottomSheet를 띄우는 메서드
    fun showMenuBottomSheet(){
        // BottomSheetDialog 사용
        val bottomSheetMenuPlannerListBinding = BottomSheetMenuPlannerListBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(mainActivity)
        bottomSheetDialog.setContentView(bottomSheetMenuPlannerListBinding.root)

        // 버튼들 리스너
        bottomSheetMenuPlannerListBinding.buttonBottomSheetPlannerListModify.setOnClickListener {

            bottomSheetDialog.setOnDismissListener {
                // 수정을 위한 BottomSheet를 띄운다.
                val bottomSheetPlannerListModify =  BottomSheetPlannerListModify(this)
                bottomSheetPlannerListModify.show(mainActivity.supportFragmentManager, "list modify")
            }

            bottomSheetDialog.dismiss()
        }

        bottomSheetMenuPlannerListBinding.buttonBottomSheetPlannerListDelete.setOnClickListener {

            bottomSheetDialog.setOnDismissListener {
                val builder = MaterialAlertDialogBuilder(mainActivity)
                builder.setTitle("일정 삭제")
                builder.setMessage("삭제할 경우 복구가 불가능합니다")
                builder.setNegativeButton("취소", null)
                builder.setPositiveButton("확인", null)
                builder.show()
            }

            bottomSheetDialog.dismiss()
        }


        // BottomSheetDialog를 띄운다.
        bottomSheetDialog.show()
    }
}







