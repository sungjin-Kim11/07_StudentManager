package com.lion.a07_studentmanager.fragment

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lion.a07_studentmanager.FragmentName
import com.lion.a07_studentmanager.MainActivity
import com.lion.a07_studentmanager.R
import com.lion.a07_studentmanager.databinding.FragmentSettingPasswordBinding

class SettingPasswordFragment : Fragment() {

    lateinit var fragmentSettingPasswordBinding: FragmentSettingPasswordBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentSettingPasswordBinding = FragmentSettingPasswordBinding.inflate(layoutInflater, container, false)
        mainActivity = activity as MainActivity

        // 툴바 설정 메서드를 호출한다.
        settingToolbar()
        // 등록 완료 버튼 설정 메서드를 호출한다.
        settingButtonSettingPasswordSubmit()
        // 기타 초기 설정 메서드를 호출한다.
        settingInitView()

        return fragmentSettingPasswordBinding.root
    }

    // 툴바 설정 메서드
    fun settingToolbar(){
        fragmentSettingPasswordBinding.apply {
            // 타이틀
            toolbarSettingPassword.title = "관리자 비밀번호 등록"
        }
    }

    // 기타 초기 설정
    fun settingInitView(){
        fragmentSettingPasswordBinding.apply {
            // 입력 요소에 포커스를 주고 키보드를 올려준다.
            mainActivity.showSoftInput(textFieldSettingPassword1.editText!!)
        }
    }

    // 등록 완료 버튼 설정
    fun settingButtonSettingPasswordSubmit(){
        fragmentSettingPasswordBinding.apply {
            buttonSettingPasswordSubmit.setOnClickListener {

                val builder1 = MaterialAlertDialogBuilder(mainActivity)
                builder1.setTitle("등록 완료")
                builder1.setMessage("""
                    관리자 비밀번호가 등록되었습니다.
                    로그인해주세요
                """.trimIndent())
                builder1.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
                    // LoginFragment로 교체한다.
                    mainActivity.replaceFragment(FragmentName.LOGIN_FRAGMENT, false, true, null)
                }

                builder1.show()
            }
        }
    }
}