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

### 입력 요소의 digits 속성에 설정할 문자열을 등록해준다.

[res/values/strings.xml]

```xml
    <!-- 비밀번호의 digits 속성에 설정할 문자열 값 -->
    <string name="password_disgits">abcdefghijklmnopqrstuvwxyz0123456789_</string>
```

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
    android:transitionGroup="true"
    tools:context=".fragment.LoginFragment">

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
                android:digits="@string/password_disgits"
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

### 화면을 구성해준다.

[res/layout/fragment_setting_passworld.xml]

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:transitionGroup="true"
    tools:context=".fragment.SettingPasswordFragment">

    <com.google.android.material.appbar.MaterialToolbar
        android:id="@+id/toolbarSettingPassword"
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
            android:id="@+id/textFieldSettingPassword1"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:hint="관리자 비밀번호"
            app:endIconMode="password_toggle"
            app:startIconDrawable="@drawable/lock_24px">

            <com.google.android.material.textfield.TextInputEditText
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:digits="@string/password_disgits"
                android:inputType="text|textPassword"
                android:singleLine="false" />
        </com.google.android.material.textfield.TextInputLayout>

        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/textFieldSettingPassword2"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:hint="관리자 비밀번호 확인"
            app:endIconMode="password_toggle"
            app:startIconDrawable="@drawable/lock_24px">

            <com.google.android.material.textfield.TextInputEditText
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:digits="@string/password_disgits"
                android:inputType="text|textPassword"
                android:singleLine="false" />
        </com.google.android.material.textfield.TextInputLayout>

        <Button
            android:id="@+id/buttonSettingPasswordSubmit"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:text="등록완료" />
    </LinearLayout>
</LinearLayout>
```


### 툴바 설정 메서드를 구현한다.

[fragment/SettingPasswordFragment.kt]

```kt
    // 툴바 설정 메서드
    fun settingToolbar(){
        fragmentSettingPasswordBinding.apply { 
            // 타이틀
            toolbarSettingPassword.title = "관리자 비밀번호 등록"
        }
    }
```

### 메서드를 호출한다.

[fragment/SettingPasswordFragment.kt - onCreateView()]
```kt
        // 툴바 설정 메서드를 호출한다.
        settingToolbar()
```

### 기타 화면 설정 메서드를 구현한다.

[fragment/SettingPasswordFragment.kt]
```kt
    // 기타 초기 설정
    fun settingInitView(){
        fragmentSettingPasswordBinding.apply {
            // 입력 요소에 포커스를 주고 키보드를 올려준다.
            mainActivity.showSoftInput(textFieldSettingPassword1.editText!!)
        }
    }
```

### 메서드를 호출한다.

[fragment/SettingPasswordFragment.kt - onCreateView()]
```kt
        // 기타 초기 설정 메서드를 호출한다.
        settingInitView()
```

### 버튼 설정 메서드를 구현한다.

[fragment/SettingPasswordFragment.kt]
```kt
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
                    mainActivity.replaceFragment(FragmentName.LOGIN_FRAGMENT, false, false, null)
                }

                builder1.show()
            }
        }
    }
```

[fragment/SettingPasswordFragment.kt - onCreateView()]
### 메서드를 호출한다.

```kt
        // 등록 완료 버튼 설정 메서드를 호출한다.
        settingButtonSettingPasswordSubmit()
```

---

# 메인 화면

### 프래그래그먼트를 만들어준다.
- MainFragment

### Fragment 기본 코드를 작성한다.

```kt
class MainFragment : Fragment() {

    lateinit var fragmentMainBinding: FragmentMainBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentMainBinding = FragmentMainBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        return fragmentMainBinding.root
    }
}
```

### Fragment 이름을 설정해준다.

[MainActivity.kt]
```kt
    // 메인 화면
    MAIN_FRAGMENT(3, "MainFragment"),
```

### MainFragment 객체를 생성한다.

[MainActivity.kt - replaceFragment()]
```kt
            // 메인 화면
            FragmentName.MAIN_FRAGMENT -> MainFragment()
```

### LoginFragment의 버튼 리스너를 수정한다.

[fragment/LoginFragment.kt]
```kt
    // 로그인 버튼 설정 메서드
    fun settingButtonLoginSubmit(){
        fragmentLoginBinding.apply {
            buttonLoginSubmit.setOnClickListener {
                // MainFragment로 이동한다.
                
                mainActivity.replaceFragment(FragmentName.MAIN_FRAGMENT, false, true, null)
            }
        }
    }
```

### 네비게이션 뷰의 헤더로 사용할 layout을 만들어준다.

[res/layout/navigation_view_header_main.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="10dp">

    <TextView
        android:id="@+id/textView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="환영합니다"
        android:textAppearance="@style/TextAppearance.AppCompat.Large" />

    <TextView
        android:id="@+id/textView2"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:text="관리자님" />
</LinearLayout>
```

### drawable 폴더에 아이콘을 넣어준다.
- calendar_month_24px.xml
- list_alt_24px.xml
- manufacturing_24px.xml
- point_of_sale_24px.xml
- settings_input_svideo_24px.xml

### NavigationView에 설정할 메뉴 파일을 만들어준다.

[res/menu/navigation_main_menu.xml]

```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:android="http://schemas.android.com/apk/res/android">

    <item android:title="학생관리" >
        <menu >
            <item
                android:id="@+id/navigation_main_menu_student_list"
                android:checkable="true"
                android:icon="@drawable/list_alt_24px"
                android:title="학생목록"
                app:showAsAction="ifRoom" />
            <item
                android:id="@+id/navigation_main_menu_student_point"
                android:checkable="true"
                android:icon="@drawable/point_of_sale_24px"
                android:title="학생성적"
                app:showAsAction="ifRoom" />
            <item
                android:id="@+id/navigation_main_menu_student_data"
                android:checkable="true"
                android:icon="@drawable/manufacturing_24px"
                android:title="학생통계"
                app:showAsAction="ifRoom" />
        </menu>
    </item>
    <item android:title="관리자" >
        <menu >
            <item
                android:id="@+id/navigation_main_menu_calendar"
                android:checkable="true"
                android:icon="@drawable/calendar_month_24px"
                android:title="학사일정"
                app:showAsAction="ifRoom" />
            <item
                android:id="@+id/navigation_main_menu_setting_manager"
                android:checkable="true"
                android:icon="@drawable/settings_input_svideo_24px"
                android:title="관리자설정"
                app:showAsAction="ifRoom" />
        </menu>
    </item>
</menu>
```

### MainFragment의 화면을 구성해준다.

[res/layout/fragment_main.xml]

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.drawerlayout.widget.DrawerLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/drawerLayoutMain"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:transitionGroup="true"
    tools:context=".fragment.MainFragment">

    <androidx.coordinatorlayout.widget.CoordinatorLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <androidx.fragment.app.FragmentContainerView
            android:id="@+id/fragmentContainerViewNavigation"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
    </androidx.coordinatorlayout.widget.CoordinatorLayout>

    <com.google.android.material.navigation.NavigationView
        android:id="@+id/navigationViewMain"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_gravity="start" />
</androidx.drawerlayout.widget.DrawerLayout>
```

### 네비게이션 뷰를 구성해준다.

[fragment/MainFragment.kt]
```kt
    // NavigationView를 설정하는 메서드
    fun settingNavigationViewMain(){
        fragmentMainBinding.apply {
            // 네비게이션 뷰의 헤더
            val navigationViewHeaderMainBinding = NavigationViewHeaderMainBinding.inflate(layoutInflater)
            navigationViewMain.addHeaderView(navigationViewHeaderMainBinding.root)
            // 메뉴
            navigationViewMain.inflateMenu(R.menu.navigation_main_menu)
            // 첫 번째 메뉴가 선택되어 있도록 한다.
            navigationViewMain.setCheckedItem(R.id.navigation_main_menu_student_list)
            navigationViewMain.setNavigationItemSelectedListener {
                // 메뉴 id로 분기한다
                when(it.itemId){
                    // 학생목록
                    R.id.navigation_main_menu_student_list -> Log.d("test100", "학생목록")
                    R.id.navigation_main_menu_student_point -> Log.d("test100", "학생성적")
                    R.id.navigation_main_menu_student_data -> Log.d("test100", "학생통계")
                    R.id.navigation_main_menu_calendar -> Log.d("test100", "학사일정")
                    R.id.navigation_main_menu_setting_manager -> Log.d("test100", "관리자설정")
                }
                true
            }
        }
    }
```

### 메서드를 호출해준다.

[fragment/MainFragment.kt - onCreateView()]
```kt
        // NavigationView를 설정하는 메서드를 호출한다.
        settingNavigationViewMain()
```

### Fragment 들의 이름을 정의할 enum class를 만들어준다.

[fragment/MainFragment.kt]
```kt
// MainFragment를 통해 보여줄 Fragment들의 이름
enum class SubFragmentName(val number:Int, val str:String){
    
}
```

### Fragment를 관리하는 기본 코드를 작성한다.

```kt
    // 프래그먼트를 교체하는 함수
    fun replaceFragment(fragmentName: SubFragmentName, isAddToBackStack:Boolean, animate:Boolean, dataBundle: Bundle?){
        // 프래그먼트 객체
        val newFragment = when(fragmentName){
            
        }

        // bundle 객체가 null이 아니라면
        if(dataBundle != null){
            newFragment.arguments = dataBundle
        }

        // 프래그먼트 교체
        mainActivity.supportFragmentManager.commit {

            if(animate) {
                newFragment.exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                newFragment.reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
                newFragment.enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ true)
                newFragment.returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, /* forward= */ false)
            }

            replace(R.id.fragmentContainerViewNavigation, newFragment)
            if(isAddToBackStack){
                addToBackStack(fragmentName.str)
            }
        }
    }

    // 프래그먼트를 BackStack에서 제거하는 메서드
    fun removeFragment(fragmentName: SubFragmentName){
        mainActivity.supportFragmentManager.popBackStack(fragmentName.str, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }
```

### StudentListFragment 를 만들어준다.

[fragment/StudentListFragment.kt]
```kt
class StudentListFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentStudentListBinding: FragmentStudentListBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentStudentListBinding = FragmentStudentListBinding.inflate(inflater, container, false)
        mainActivity = activity as MainActivity

        return fragmentStudentListBinding.root
    }
}
```

### 프래그먼트의 이름을 정의해준다.

[fragment/MainFragment.kt - SubFragmentName]
```kt
    // 학생 목록 화면
    STUDENT_LIST_FRAGMENT(1, "StudentListFragment"),
```

### 프래그먼트의 객체를 생성한다.

[fragment/MainFragment.kt - replaceFragment()]
```kt
           // 학생 목록 화면
            SubFragmentName.STUDENT_LIST_FRAGMENT -> StudentListFragment(this)
```

### StudnetListFragment가 보이도록 설정한다.

[fragment/MainFragment.kt - onCreateView()]
```kt
        // 첫 화면이 보이도록 설정한다.
        replaceFragment(SubFragmentName.STUDENT_LIST_FRAGMENT, false, false, null)
```

---

# 학생 목록 화면을 구성한다.

### Toolbar의 메뉴의 아이콘으로 사용할 이미지를 drawable 폴더에 넣어준다.
- search_24px.xml
- filter_alt_24px.xml

### Toolbar에 배치할 메뉴를 구성해준다.

[res/menu/toolbar_student_list_menu.xml]
```kt
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:android="http://schemas.android.com/apk/res/android">

    <item
        android:id="@+id/student_list_menu_filter"
        android:icon="@drawable/filter_alt_24px"
        android:title="필터"
        app:showAsAction="always" />
    <item
        android:id="@+id/student_list_menu_search"
        android:icon="@drawable/search_24px"
        android:title="필터"
        app:showAsAction="always" />
</menu>
```

## FAB에 사용할 아이콘을 drawable 폴더에 넣어준다.
- add_24px.xml

### StudentListFragment의 화면을 구성해준다.

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:transitionGroup="true"
    tools:context=".fragment.StudentListFragment">

    <com.google.android.material.appbar.MaterialToolbar
        android:id="@+id/toolbarStudentList"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:background="@android:color/transparent"
        android:minHeight="?attr/actionBarSize"
        android:theme="?attr/actionBarTheme"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

    <com.google.android.material.floatingactionbutton.FloatingActionButton
        android:id="@+id/fabStudentList"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginEnd="16dp"
        android:layout_marginBottom="16dp"
        android:clickable="true"
        android:src="@drawable/add_24px"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent" />

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerViewStudentList"
        android:layout_width="0dp"
        android:layout_height="0dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/toolbarStudentList" />
</androidx.constraintlayout.widget.ConstraintLayout>
```

### RecyclerView의 항목으로 사용할 layout 파일을 만들어준다.

[res/layout/row_text1.xml]

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="10dp"
    android:transitionGroup="true">

    <TextView
        android:id="@+id/textViewRow"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="TextView"
        android:textAppearance="@style/TextAppearance.AppCompat.Large" />
</LinearLayout>
```

### RecyclerView 구성을 위한 임시 데이터를 정의한다.

[ fragment/StudentListFragment.kt]
```kt
    // RecyclerView 구성을 위한 임시데이터
    val tempData = Array(100){
        "학생 ${it + 1}"
    }
```

### 툴바를 구성하는 메서드를 구현한다.

[ fragment/StudentListFragment.kt]
```kt
    // 툴바를 구성하는 메서드
    fun settingToolbarStudentList(){
        fragmentStudentListBinding.apply {
            toolbarStudentList.title = "학생 목록"
            toolbarStudentList.inflateMenu(R.menu.toolbar_student_list_menu)
            toolbarStudentList.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.student_list_menu_filter ->{}
                    R.id.student_list_menu_search -> {}
                }
                true
            }
        }
    }
```

### 메서드를 호출한다.

[ fragment/StudentListFragment.kt - onCreateView()]
```kt
        // 툴바를 구성하는 메서드를 호출한다.
        settingToolbarStudentList()
```

### RecyclerView의 어뎁터 클래스를 작성한다.

[ fragment/StudentListFragment.kt]
```kt
    // RecyclerView의 어뎁터
    inner class RecyclerViewStudentListAdapter : RecyclerView.Adapter<RecyclerViewStudentListAdapter.ViewHolderStudentList>(){
        // ViewHOlder
        inner class ViewHolderStudentList(val rowText1Binding: RowText1Binding) : RecyclerView.ViewHolder(rowText1Binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderStudentList {
            val rowText1Binding = RowText1Binding.inflate(layoutInflater, parent, false)
            val viewHolderStudentList = ViewHolderStudentList(rowText1Binding)
            return viewHolderStudentList
        }

        override fun getItemCount(): Int {
            return tempData.size
        }

        override fun onBindViewHolder(holder: ViewHolderStudentList, position: Int) {
            holder.rowText1Binding.textViewRow.text = tempData[position]
        }
    }
```

### RecyclerView를 구성하는 메서드를 만들어준다.

[ fragment/StudentListFragment.kt]
```kt
    // RecyclerView를 구성하는 메서드
    fun settingRecyclerViewStudentList(){
        fragmentStudentListBinding.apply {
            recyclerViewStudentList.adapter = RecyclerViewStudentListAdapter()
            recyclerViewStudentList.layoutManager = LinearLayoutManager(mainActivity)
            val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewStudentList.addItemDecoration(deco)
        }
    }
```

### 메서드를 호출한다.

[ fragment/StudentListFragment.kt - onCreateView()]
```kt
        // RecyclerView를 구성하는 메서드를 호출한다.
        settingRecyclerViewStudentList()
```

### 학년과 운동부 메뉴 구성을 위한 문자열을 구성해준다.

[res/values/strings.xml]

```xml
    <!-- 학년 -->
    <array name="studentGrade">
        <item>전체</item>
        <item>1학년</item>
        <item>2학년</item>
        <item>3학년</item>
    </array>

    <!-- 운동부 -->
    <array name="studentType">
        <item>전체</item>
        <item>농구부</item>
        <item>축구부</item>
        <item>야구부</item>
    </array>
```


### 필터 아이콘을 누르면 나타나는 다이얼로그를 구성하기 위한 레이아웃을 만든다.

[res/layout/dialog_student_list.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:padding="20dp"
    android:transitionGroup="true">

    <TextView
        android:id="@+id/textView4"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="학년"
        android:textAppearance="@style/TextAppearance.AppCompat.Large" />

    <com.google.android.material.textfield.TextInputLayout
        android:id="@+id/textFieldStudentListGrade"
        style="@style/Widget.Material3.TextInputLayout.OutlinedBox.ExposedDropdownMenu"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp">

        <AutoCompleteTextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:inputType="none"
            android:text="전체"
            app:simpleItems="@array/studentGrade" />
    </com.google.android.material.textfield.TextInputLayout>

    <com.google.android.material.divider.MaterialDivider
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp" />

    <TextView
        android:id="@+id/textView5"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:text="운동부"
        android:textAppearance="@style/TextAppearance.AppCompat.Large" />

    <com.google.android.material.textfield.TextInputLayout
        android:id="@+id/textFieldStudentListType"
        style="@style/Widget.Material3.TextInputLayout.OutlinedBox.ExposedDropdownMenu"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp">

        <AutoCompleteTextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:inputType="none"
            android:text="전체"
            app:simpleItems="@array/studentType" />
    </com.google.android.material.textfield.TextInputLayout>

    <com.google.android.material.divider.MaterialDivider
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp" />

    <TextView
        android:id="@+id/textView6"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:text="성별"
        android:textAppearance="@style/TextAppearance.AppCompat.Large" />

    <com.google.android.material.button.MaterialButtonToggleGroup
        android:id="@+id/toggleStudentListGender"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        app:checkedButton="@id/buttonGenderAll"
        app:selectionRequired="true"
        app:singleSelection="true"
        tools:singleSelection="true">

        <Button
            android:id="@+id/buttonGenderAll"
            style="@style/Widget.Material3.Button.OutlinedButton"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="전체" />

        <Button
            android:id="@+id/buttonGenderMale"
            style="@style/Widget.Material3.Button.OutlinedButton"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="남자" />

        <Button
            android:id="@+id/buttonGenderFemale"
            style="@style/Widget.Material3.Button.OutlinedButton"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:text="여자" />
    </com.google.android.material.button.MaterialButtonToggleGroup>
</LinearLayout>
```

### 다이얼로그를 띄우는 메서드를 구현한다.

[fragment/StudentListFragment.kt]
```kt
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
```

### 메서드를 호출한다.

[fragment/StudentListFragment.kt - settingToolbarStudentList()]
```kt
                    R.id.student_list_menu_filter ->{
                        // 필터 다이얼로그를 띄우는 메서드를 호출한다.
                        showFilterDialog()
                    }
```

---

# 학생 검색 화면

### 프로그먼트를 만들어준다.

[fragment/SearchStudentFragment.kt]

```kt
class SearchStudentFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentSearchStudentBinding: FragmentSearchStudentBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentSearchStudentBinding = FragmentSearchStudentBinding.inflate(inflater)
        mainActivity = activity as MainActivity
        return fragmentSearchStudentBinding.root
    }

}
```

### 프래그먼트의 이름을 정의해준다.

[fragment/MainFragment.kt - SubFragmentName]
```kt
    // 학생 검색 화면
    SEARCH_STUDENT_FRAGMENT(2, "SearchStudentFragment"),
```

### 프래그먼트 객체를 생성한다.

[fragment/MainFragment.kt - replaceFragment()]
```kt
            // 학정 정보 검색 화면
            SubFragmentName.SEARCH_STUDENT_FRAGMENT -> SearchStudentFragment(this)
```

### 메뉴를 누르면 검색 화면이 보이도록 한다.

[fragment/StudentListFragment.kt - settingToolbarStudentList()]
```kt
                    R.id.student_list_menu_search -> {
                        // 검색화면으로 이동한다.
                        mainFragment.replaceFragment(SubFragmentName.SEARCH_STUDENT_FRAGMENT, true, true, null)
                    }
```

### 아이콘 파일을 drawable 폴더에 넣어준다.
- search_24px.xml
- arrow_back_24px.xml

### SearchStudentFragment의 화면을 구성한다.

[res/layout/fragment_search_student.xml]

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:transitionGroup="true"
    tools:context=".fragment.SearchStudentFragment" >

    <com.google.android.material.appbar.MaterialToolbar
        android:id="@+id/toolbarSearchStudent"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@android:color/transparent"
        android:minHeight="?attr/actionBarSize"
        android:theme="?attr/actionBarTheme" />

    <com.google.android.material.textfield.TextInputLayout
        android:id="@+id/textFieldSearchStudentName"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_margin="10dp"
        android:hint="검색어"
        app:endIconMode="clear_text"
        app:startIconDrawable="@drawable/search_24px">

        <com.google.android.material.textfield.TextInputEditText
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="hint" />
    </com.google.android.material.textfield.TextInputLayout>

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerViewSearchStudent"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</LinearLayout>
```

### 툴바를 구성하는 메서드를 구현한다.

[fragment/SearchStudentFragment.kt]
```kt

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
```

### 메서드를 호출한다.

[fragment/SearchStudentFragment.kt - onCreateView()]

```kt
        // 툴바를 구성하는 메서드를 호출한다.
        settingToolbarSearchStudent()
```

### 리사이클러 뷰 구성을 위한 임시 데이터

[fragment/SearchStudentFragment.kt]
```kt
    // 리사이클러 뷰 구성을 위한 임시 데이터
    val tempData = Array(100){
        "학생 ${it + 1}"
    }
```

### 어뎁터 클래스를 작성한다.

[fragemnt/SearchStudentFragment.kt]
```kt
    // Recyclerview의 어뎁터
    inner class RecyclerViewStudentSearchAdapter : RecyclerView.Adapter<RecyclerViewStudentSearchAdapter.ViewHolderStudentSearch>(){
        inner class ViewHolderStudentSearch(val rowText1Binding: RowText1Binding) : RecyclerView.ViewHolder(rowText1Binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderStudentSearch {
            val rowText1Binding = RowText1Binding.inflate(layoutInflater, parent, false)
            val viewHolderStudentSearch = ViewHolderStudentSearch(rowText1Binding)
            return viewHolderStudentSearch
        }

        override fun getItemCount(): Int {
            return tempData.size
        }

        override fun onBindViewHolder(holder: ViewHolderStudentSearch, position: Int) {
            holder.rowText1Binding.textViewRow.text = tempData[position]
        }
    }
```

### RecyclerView를 구성하는 메서드를 만들어준다.

[fragemnt/SearchStudentFragment.kt]
```kt
    // recyclerView를 구성하는 메서드
    fun settingRecyclerViewSearchStudent(){
        fragmentSearchStudentBinding.apply {
            recyclerViewSearchStudent.adapter = RecyclerViewStudentSearchAdapter()
            recyclerViewSearchStudent.layoutManager = LinearLayoutManager(mainActivity)
            val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewSearchStudent.addItemDecoration(deco)
        }
    }
```

### 메서드를 호출해준다.

[fragemnt/SearchStudentFragment.kt - onCreateView()]
```kt
        // recyclerView를 구성하는 메서드
        settingRecyclerViewSearchStudent()
```


---

# 학생 정보를 보는 화면

### Fragment 생성

[fragment/ShowStudentFragment.kt]

```kt
class ShowStudentFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentShowStudentBinding: FragmentShowStudentBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentShowStudentBinding = FragmentShowStudentBinding.inflate(inflater)
        mainActivity = activity as MainActivity
        
        return fragmentShowStudentBinding.root
    }
}
```

### Fragment 이름을 설정한다.

[fragment/MainFragment.kt - SubFragmentName]
```kt
    // 학생 정보 보는 화면
    SHOW_STUDENT_FRAGMENT(3, "ShowStudentFragment"),
```

### Fragment 객체를 생성한다.

[fragment/MainFragment.kt - replaceFragment()]

```kt
            // 학생 정보 보는 화면
            SubFragmentName.SHOW_STUDENT_FRAGMENT -> ShowStudentFragment(this)
```

### RecyclerView의 항목을 누르면 ShowStudentFragment가 보여지게 한다.

[fragment/SearchStudentFragment - RecyclerViewStudentSearchAdapter]
```kt
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
```

### ShowStudentFragment의 화면을 구성해준다.

[res/layout/fragment_show_student.xml]

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:transitionGroup="true"
    tools:context=".fragment.ShowStudentFragment" >

    <com.google.android.material.appbar.MaterialToolbar
        android:id="@+id/toolbarShowStudent"
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
            android:id="@+id/textFieldShowStudentName"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="이름"
            app:startIconDrawable="@drawable/calendar_month_24px">

            <com.google.android.material.textfield.TextInputEditText
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:enabled="false"
                android:singleLine="true"
                android:text=" "
                android:textColor="#000000" />
        </com.google.android.material.textfield.TextInputLayout>

        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/textFieldShowStudentGrade"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:hint="학년"
            app:startIconDrawable="@drawable/delete_24px">

            <com.google.android.material.textfield.TextInputEditText
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:enabled="false"
                android:singleLine="true"
                android:text=" "
                android:textColor="#000000" />
        </com.google.android.material.textfield.TextInputLayout>

        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/textFieldShowStudentType"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:hint="운동부"
            app:startIconDrawable="@drawable/delete_24px">

            <com.google.android.material.textfield.TextInputEditText
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:enabled="false"
                android:singleLine="true"
                android:text=" "
                android:textColor="#000000" />
        </com.google.android.material.textfield.TextInputLayout>

        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/textFieldShowStudentGender"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:hint="성별"
            app:startIconDrawable="@drawable/filter_alt_24px">

            <com.google.android.material.textfield.TextInputEditText
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:enabled="false"
                android:singleLine="true"
                android:text=" "
                android:textColor="#000000" />
        </com.google.android.material.textfield.TextInputLayout>

        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/textFieldShowStudentKorean"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:hint="국어점수"
            app:startIconDrawable="@drawable/filter_alt_24px">

            <com.google.android.material.textfield.TextInputEditText
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:enabled="false"
                android:singleLine="true"
                android:text=" "
                android:textColor="#000000" />
        </com.google.android.material.textfield.TextInputLayout>


        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/textFieldShowStudentEnglish"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:hint="영어점수"
            app:startIconDrawable="@drawable/point_of_sale_24px">

            <com.google.android.material.textfield.TextInputEditText
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:enabled="false"
                android:singleLine="true"
                android:text=" "
                android:textColor="#000000" />
        </com.google.android.material.textfield.TextInputLayout>


        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/textFieldShowStudentMath"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:hint="수학점수"
            app:startIconDrawable="@drawable/delete_24px">

            <com.google.android.material.textfield.TextInputEditText
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:enabled="false"
                android:singleLine="true"
                android:text=" "
                android:textColor="#000000" />
        </com.google.android.material.textfield.TextInputLayout>



    </LinearLayout>
</LinearLayout>
```

### 툴바에 배치할 메뉴를 구성해준다.

[res/menu/toolbar_show_student_menu.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:android="http://schemas.android.com/apk/res/android">

    <item
        android:id="@+id/show_student_menu_modify"
        android:icon="@drawable/edit_24px"
        android:title="수정"
        app:showAsAction="always" />
    <item
        android:id="@+id/show_student_menu_remove"
        android:icon="@drawable/delete_24px"
        android:title="삭제"
        app:showAsAction="always" />
</menu>
```

### 툴바를 구성하는 메서드를 만들어준다.

[fragment/ShowStudentFragment.kt]
```kt

    // 툴바를 구성하는 메서드
    fun settingToolbarShowStudent(){
        fragmentShowStudentBinding.apply {
            toolbarShowStudent.title = "학생 정보 보기"

            toolbarShowStudent.setNavigationIcon(R.drawable.arrow_back_24px)
            toolbarShowStudent.setNavigationOnClickListener {
                mainFragment.removeFragment(SubFragmentName.SHOW_STUDENT_FRAGMENT)
            }

            toolbarShowStudent.inflateMenu(R.menu.toolbar_show_student_menu)
            toolbarShowStudent.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.show_student_menu_modify -> {}
                    R.id.show_student_menu_remove -> {}
                }
                true
            }
        }
    }
```

### 메서드를 호출해준다.

[fragment/ShowStudentFragment.kt - onCreateView()]
```kt
        // 툴바를 구성하는 메서드
        settingToolbarShowStudent()
```

### 삭제 메뉴를 누르면 이전으로 돌아가게 한다.

[fragment/ShowStudentFragment.kt - settingToolbarShowStudent()]
```kt
                    R.id.show_student_menu_remove -> {
                        mainFragment.removeFragment(SubFragmentName.SHOW_STUDENT_FRAGMENT)
                    }
```

--- 

# 학생 정보 수정 화면

### 프래그먼트를 만들어준다.

[fragment/ModifyStudentFragment.kt]

```kt
class ModifyStudentFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentModifyStudentBinding: FragmentModifyStudentBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentModifyStudentBinding = FragmentModifyStudentBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        return fragmentModifyStudentBinding.root
    }
}
```

### 프래그먼트의 이름을 정의한다

[layout/MainFragment.kt - SubFragmentName]
```kt
    // 학생 정보 수정 화면
    MODIFY_STUDENT_FRAGMENT(4, "ModifyStudentFragment"),
```

### 프래그먼트 객체를 생성한다.

[layout/MainFragment.kt - replaceFragment()]

```kt
            // 학생 정보 수정 화면
            SubFragmentName.MODIFY_STUDENT_FRAGMENT ->  ModifyStudentFragment(this)
```

### ShowStudentFragment의 메뉴를 누르면 정보 수정 화면으로 이동하게 한다.

[fragment/ShowStudentFragment.kt - settingToolbarShowStudent()]
```kt
                    R.id.show_student_menu_modify -> {
                        // 정보 수정 화면으로 이동한다.
                        mainFragment.replaceFragment(SubFragmentName.MODIFY_STUDENT_FRAGMENT,
                            true, true, null)
                    }
```


### ShowStudentFragment의 화면을 구성해준다.

[res/layout/fragment_modify_student.xml]

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:transitionGroup="true"
    tools:context=".fragment.ModifyStudentFragment">

    <com.google.android.material.appbar.MaterialToolbar
        android:id="@+id/toolbarModifyStudent"
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
            android:id="@+id/textFieldModifyStudentName"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:hint="이름"
            app:endIconMode="clear_text"
            app:startIconDrawable="@drawable/calendar_month_24px">

            <com.google.android.material.textfield.TextInputEditText
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:singleLine="true" />
        </com.google.android.material.textfield.TextInputLayout>

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:text="학년" />

        <com.google.android.material.button.MaterialButtonToggleGroup
            android:id="@+id/toggleModifyStudentGrade"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            app:checkedButton="@id/buttonGradeOne"
            app:selectionRequired="true"
            app:singleSelection="true">

            <Button
                android:id="@+id/buttonGradeOne"
                style="@style/Widget.Material3.Button.OutlinedButton"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="1학년" />

            <Button
                android:id="@+id/buttonGradeTow"
                style="@style/Widget.Material3.Button.OutlinedButton"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="2학년" />

            <Button
                android:id="@+id/buttonGradeThree"
                style="@style/Widget.Material3.Button.OutlinedButton"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="3학년" />
        </com.google.android.material.button.MaterialButtonToggleGroup>


        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:text="운동부" />

        <com.google.android.material.button.MaterialButtonToggleGroup
            android:id="@+id/toggleModifyStudentType"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            app:checkedButton="@id/buttonTypeBasketBall"
            app:selectionRequired="true"
            app:singleSelection="true">

            <Button
                android:id="@+id/buttonTypeBasketBall"
                style="@style/Widget.Material3.Button.OutlinedButton"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="농구부" />

            <Button
                android:id="@+id/buttonTypeSoccer"
                style="@style/Widget.Material3.Button.OutlinedButton"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="축구부" />

            <Button
                android:id="@+id/buttonTypeBaseBall"
                style="@style/Widget.Material3.Button.OutlinedButton"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="야구부" />
        </com.google.android.material.button.MaterialButtonToggleGroup>


        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:text="성별" />

        <com.google.android.material.button.MaterialButtonToggleGroup
            android:id="@+id/toggleModifyStudentGender"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            app:checkedButton="@id/buttonGenderMale"
            app:selectionRequired="true"
            app:singleSelection="true">

            <Button
                android:id="@+id/buttonGenderMale"
                style="@style/Widget.Material3.Button.OutlinedButton"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="남자" />

            <Button
                android:id="@+id/buttonGenderFemale"
                style="@style/Widget.Material3.Button.OutlinedButton"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="여자" />

        </com.google.android.material.button.MaterialButtonToggleGroup>

        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/textFieldModifyStudentKorean"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:hint="국어점수"
            app:endIconMode="clear_text"
            app:startIconDrawable="@drawable/calendar_month_24px">

            <com.google.android.material.textfield.TextInputEditText
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:inputType="number"
                android:singleLine="true" />
        </com.google.android.material.textfield.TextInputLayout>

        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/textFieldModifyStudentEnglish"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:hint="영어점수"
            app:endIconMode="clear_text"
            app:startIconDrawable="@drawable/calendar_month_24px">

            <com.google.android.material.textfield.TextInputEditText
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:inputType="number"
                android:singleLine="true" />
        </com.google.android.material.textfield.TextInputLayout>

        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/textFieldModifyStudentMath"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:hint="수학점수"
            app:endIconMode="clear_text"
            app:startIconDrawable="@drawable/calendar_month_24px">

            <com.google.android.material.textfield.TextInputEditText
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:inputType="number"
                android:singleLine="true" />
        </com.google.android.material.textfield.TextInputLayout>

        <Button
            android:id="@+id/buttonModifyStudentSubmit"
            style="@style/Widget.Material3.Button.OutlinedButton"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:text="수정 완료" />

    </LinearLayout>

</LinearLayout>
```

### 툴바를 구성하는 메서드를 만들어준다.

[fragment/ModifyStudentFragment.kt]
```kt
    // 툴바를 구성하는 메서드
    fun settingToolbarModifyStudent(){
        fragmentModifyStudentBinding.apply {
            toolbarModifyStudent.title = "학생 정보 수정"
            toolbarModifyStudent.setNavigationIcon(R.drawable.arrow_back_24px)
            toolbarModifyStudent.setNavigationOnClickListener {
                mainFragment.removeFragment(SubFragmentName.MODIFY_STUDENT_FRAGMENT)
            }
        }
    }
```

### 메서드를 호출한다.

[fragment/ModifyStudentFragment.kt - onCreateView()]
```kt
        // 툴바를 구성하는 메서드 호출
        settingToolbarModifyStudent()
```