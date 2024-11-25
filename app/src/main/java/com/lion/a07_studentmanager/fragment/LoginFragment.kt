package com.lion.a07_studentmanager.fragment

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lion.a07_studentmanager.FragmentName
import com.lion.a07_studentmanager.MainActivity
import com.lion.a07_studentmanager.R
import com.lion.a07_studentmanager.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    lateinit var fragmentLoginBinding: FragmentLoginBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)
        mainActivity = activity as MainActivity

        // 툴바를 설정하는 메서드를 호출한다.
        settingToolbarLogin()
        // 로그인 버튼 설정 메서드를 호출한다.
        settingButtonLoginSubmit()
        // 기타 뷰를 설정하는 메서드를 호출한다.
        settingInitView()



        return fragmentLoginBinding.root
    }
    
    // 툴바를 설정하는 메서드
    fun settingToolbarLogin(){
        fragmentLoginBinding.apply { 
            // 타이틀
            toolbarLogin.title = "관리자 로그인"
        }
    }

    // 기타 초기 설정
    fun settingInitView(){
        fragmentLoginBinding.apply {
            // 입력 요소에 포커스를 주고 키보드를 올려준다.
            mainActivity.showSoftInput(textFieldLoginPassword.editText!!)
        }
    }

    // 로그인 버튼 설정 메서드
    fun settingButtonLoginSubmit(){
        fragmentLoginBinding.apply {
            buttonLoginSubmit.setOnClickListener {
                // MainFragment로 이동한다.
                mainActivity.replaceFragment(FragmentName.MAIN_FRAGMENT, false, true, null)
            }
        }
    }

    // 로그인 처리 메서드
    fun processingLogin(){
        fragmentLoginBinding.apply {
            // 입력한 비밀번호를 가져온다.
            val pw = textFieldLoginPassword.editText?.text!!.toString()

            // 입력한 비밀번호가 없을 경우
            if(pw.isEmpty()){
                textFieldLoginPassword.error = "비밀번호를 입력해주세요"
                return
            }

            // 저장된 비밀번호를 가져온다.
            // Preferences 객체를 가져온다.
            val managerPef = mainActivity.getSharedPreferences("manager", MODE_PRIVATE)
            // 저장되어 있는 비밀번호를 가져온다.
            val managerPassword = managerPef.getString("password", null)
            // 비밀번호가 다를 경우
            if(managerPassword != pw){
                textFieldLoginPassword.error = "비밀번호를 잘못 입력하였습니다"
                return
            }

            // 비밀번호를 제대로 입력했다면 화면을 이동한다.
            // MainFragment로 이동한다.
            mainActivity.replaceFragment(FragmentName.MAIN_FRAGMENT, false, true, null)
        }
    }
}