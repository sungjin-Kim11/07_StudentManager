# ViewBinding 셋팅

[Build.gradle.kts]

```kt
    viewBinding {
        enable = true
    }
```

[MainActivity.kt]

```kt
    lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
```

---

# 필요 라이브러리 추가

[build.gralde.kts]
```kt
plugins {

    kotlin("kapt")
}

dependencies {

    implementation("androidx.fragment:fragment-ktx:1.8.5")
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
}
```

# MainActivity 작업

### 화면 구성

[res/layout/activity_main.xml]

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/fragmentContainerViewMain"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

### Fragment 이름을 관리하는 enum class를 만들어준다

[MainActivity.kt]

```kt
// 프래그먼트들을 나타내는 값들
enum class FragmentName(var number:Int, var str:String){

}
```

### Fragment 관리 메서드들을 구현해준다.

```kt
    // 프래그먼트를 교체하는 함수
    fun replaceFragment(fragmentName: FragmentName, isAddToBackStack:Boolean, animate:Boolean, dataBundle: Bundle?){
        // 프래그먼트 객체
        val newFragment = when(fragmentName){

        }

        // bundle 객체가 null이 아니라면
        if(dataBundle != null){
            newFragment.arguments = dataBundle
        }

        // 프래그먼트 교체
        supportFragmentManager.commit {

            if(animate) {
                newFragment.exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                newFragment.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
                newFragment.enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                newFragment.returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
            }

            replace(R.id.fragmentContainerViewMain, newFragment)
            if(isAddToBackStack){
                addToBackStack(fragmentName.str)
            }
        }
    }

    // 프래그먼트를 BackStack에서 제거하는 메서드
    fun removeFragment(fragmentName: FragmentName){
        supportFragmentManager.popBackStack(fragmentName.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
```

---

# 필요한 패키지들을 만들어준다.

- database
- repository
- viewmodel
- fragment
- util

--- 

# 로그인 화면 구성

### fragment를 생성한다.
- LoginFragment

### 기본 코드를 작성해준다.

[fragment/LoginFragment.kt]
```kt
class LoginFragment : Fragment() {

    lateinit var fragmentLoginBinding: FragmentLoginBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentLoginBinding = FragmentLoginBinding.inflate(inflater, container, false)
        mainActivity = activity as MainActivity
        
        return fragmentLoginBinding.root
    }
}
```

### Fragment의 이름을 정의해준다.

[MainActivity.kt - FragmentName]
```kt
    // 로그인 화면
    LOGIN_FRAGMENT(1, "LoginFragment"),
```

### LoginFragment의 객체를 생성해서 반환한다.

[MainActivity.kt - replaceFragment()]
```kt
            // 로그인 화면
            FragmentName.LOGIN_FRAGMENT -> LoginFragment()
```

### drawable 폴더에 아이콘 파일을 넣어준다.
- lock_24px.xml

### 화면을 구성해준다.

[res/layout/fragment_login.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".fragment.LoginFragment" >

    <com.google.android.material.appbar.MaterialToolbar
        android:id="@+id/toolbarLogin"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@android:color/transparent"
        android:minHeight="?attr/actionBarSize"
        android:theme="?attr/actionBarTheme" />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical"
        android:padding="10dp">

        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/textFieldLoginPassword"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:hint="관리자 비밀번호"
            app:endIconMode="password_toggle"
            app:startIconDrawable="@drawable/lock_24px">

            <com.google.android.material.textfield.TextInputEditText
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:inputType="text|textPassword"
                android:singleLine="true" />
        </com.google.android.material.textfield.TextInputLayout>

        <Button
            android:id="@+id/buttonLoginSubmit"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:text="로그인" />
    </LinearLayout>
</LinearLayout>
```

### 툴바를 설정하는 메서드를 구현해준다.

[fragment/LoginFragment.kt]
```kt
    // 툴바를 설정하는 메서드
    fun settingToolbarLogin(){
        fragmentLoginBinding.apply { 
            // 타이틀
            toolbarLogin.title = "관리자 로그인"
        }
    }
```

### 메서드를 호출한다.

[fragment/LoginFragment.kt - onCreateView()]
```kt
        // 툴바를 설정하는 메서드를 호출한다.
        settingToolbarLogin()
```

### 키보드 관련 코드를 작성해준다.

[MainActivity.kt]
```kt

    // 키보드 올리는 메서드
    fun showSoftInput(view: View){
        // 입력을 관리하는 매니저
        val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        // 포커스를 준다.
        view.requestFocus()

        thread {
            SystemClock.sleep(500)
            // 키보드를 올린다.
            inputManager.showSoftInput(view, 0)
        }
    }
    // 키보드를 내리는 메서드
    fun hideSoftInput(){
        // 포커스가 있는 뷰가 있다면
        if(currentFocus != null){
            // 입력을 관리하는 매니저
            val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            // 키보드를 내린다.
            inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
            // 포커스를 해제한다.
            currentFocus?.clearFocus()
        }
    }
```

### 기타 뷰 설정 메서드를 구현해준다.

[fragment/LoginFragment.kt]
```kt
    // 기타 초기 설정
    fun settingInitView(){
        fragmentLoginBinding.apply {
            // 입력 요소에 포커스를 주고 키보드를 올려준다.
            mainActivity.showSoftInput(textFieldLoginPassword.editText!!)
        }
    }
```

### 메서드를 호출한다.

[fragment/LoginFragment.kt - onCreateView()]
```kt
        // 기타 뷰를 설정하는 메서드를 호출한다.
        settingInitView()
```

### 로그인 버튼을 설정하는 메서드를 구현한다.

[fragment/LoginFragment.kt]
```kt
    // 로그인 버튼 설정 메서드
    fun settingButtonLoginSubmit(){
        fragmentLoginBinding.apply {
            buttonLoginSubmit.setOnClickListener {

                // 비밀번호를 잘못 입력했을 때
                // textFieldLoginPassword.error = "비밀번호를 잘못 입력하였습니다"
                
                // 비밀번호를 제대로 입력했을 때
                mainActivity.hideSoftInput()
            }
        }
    }
```

### 메서드를 호출한다.

[fragment/LoginFragment.kt - onCreateView()]

```kt
        // 로그인 버튼 설정 메서드를 호출한다.
        settingButtonLoginSubmit()
```

---

# 비밀번호 등록 화면

### 프래그먼트를 생성한다.
- SettingPasswordFragment

### 프래그먼트 기본 코드를 작성한다.

[fragment/SettingPasswordFragment.kt]
```kt
class SettingPasswordFragment : Fragment() {

    lateinit var fragmentSettingPasswordBinding: FragmentSettingPasswordBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        fragmentSettingPasswordBinding = FragmentSettingPasswordBinding.inflate(layoutInflater, container, false)
        mainActivity = activity as MainActivity

        return fragmentSettingPasswordBinding.root
    }

}
```

### Fragment의 이름을 설정한다.

[MainActivity.kt - FragmentName]
```kt
    // 관리자 비밀번호 설정화면
    SETTING_PASSWORD_FRAGMENT(2, "SettingPasswordFragment"),
```

### Fragment의 객체를 생성한다.

[MainActivity.kt - replaceFragment()]
```kt
            // 관리자 비밀번호 설정 화면
            FragmentName.SETTING_PASSWORD_FRAGMENT -> SettingPasswordFragment()
```



