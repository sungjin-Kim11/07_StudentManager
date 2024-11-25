package com.lion.a07_studentmanager.fragment

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.core.content.edit
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.lion.a07_studentmanager.FragmentName
import com.lion.a07_studentmanager.MainActivity
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
                // 비밀번호 등록 처리하는 메서드를 호출해준다.
                processingInputManagerPassword()

//                val builder1 = MaterialAlertDialogBuilder(mainActivity)
//                builder1.setTitle("등록 완료")
//                builder1.setMessage("""
//                    관리자 비밀번호가 등록되었습니다.
//                    로그인해주세요
//                """.trimIndent())
//                builder1.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
//                    // LoginFragment로 교체한다.
//                    mainActivity.replaceFragment(FragmentName.LOGIN_FRAGMENT, false, true, null)
//                }
//
//                builder1.show()
            }
        }
    }

    // 비밀번호 입력 완료 처리 메서드
    fun processingInputManagerPassword(){
        fragmentSettingPasswordBinding.apply {
            // 입력한 비밀번호를 가져온다.
            val pw1 = textFieldSettingPassword1.editText?.text!!.toString()
            val pw2 = textFieldSettingPassword2.editText?.text!!.toString()

            // 입력을 제대로 했는지 확인하기 위한 변수
            var inputFlag = true

            // 첫 번째 비밀번호에 입력한 것이 없다면
            if(pw1.isEmpty()){
                textFieldSettingPassword1.error = "비멀번호를 입력해주세요"
                inputFlag = false
            }
            
            // 두 번째 비밀번호에 입력한 것이 없다면
            if(pw2.isEmpty()){
                textFieldSettingPassword2.error = "비밀번호를 입력해주세요"
                inputFlag = false
            }

            // 두 비밀번호가 서로 다르다면
            if(pw1.isNotEmpty() && pw2.isNotEmpty() && pw1 != pw2){
                textFieldSettingPassword1.error = "입력하신 비빌번호가 서로 다릅니다"
                textFieldSettingPassword2.error = "입력하신 비빌번호가 서로 다릅니다"

                textFieldSettingPassword1.editText?.setText("")
                textFieldSettingPassword2.editText?.setText("")

                textFieldSettingPassword1.editText?.requestFocus()
                inputFlag = false
            }

            // 입력이 제대로 되었다면
            if(inputFlag){
                // Preferences 객체를 가져온다.
                val managerPef = mainActivity.getSharedPreferences("manager", MODE_PRIVATE)
                // 관리자 비밀번호를 저장한다.
                managerPef.edit {
                    putString("password", pw1.toString())
                }

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