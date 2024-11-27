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

---

# 학생 정보를 입력하는 화면

### Fragment를 생성한다.
- InputStudentFragment
```kt
class InputStudentFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentInputStudentBinding: FragmentInputStudentBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentInputStudentBinding = FragmentInputStudentBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        return fragmentInputStudentBinding.root
    }

} 
```

### Fragment의 이름을 정의해준다.

[fragment/MainFragment.kt]
```kt
    // 학생 정보 입력 화면
    INPUT_STUDENT_FRAGMENT(5, "InputStudentFragment"),
```


### Fragment 객체를 생성한다.

[fragment/MainFragment.kt - replaceFragment()]
```kt
            // 학생 정보 입력 화면
            SubFragmentName.INPUT_STUDENT_FRAGMENT -> InputStudentFragment(this)
```

### FAB 버튼을 구성하는 메서드를 구현한다.

[fragment/StudentListFragment.kt]
```kt
    // FAB를 구성하는 메서드
    fun settingFabStudentList(){
       fragmentStudentListBinding.apply {
           fabStudentList.setOnClickListener {
               // 학생 정보 입력 화면으로 이동한다.
               mainFragment.replaceFragment(SubFragmentName.INPUT_STUDENT_FRAGMENT, true, true, null)
           }
       }
    }
```

### 메서드를 호출한다.

[fragment/StudentListFragment.kt - onCreateView()]
```kt
        // FAB를 구성하는 메서드를 호출한다.
        settingFabStudentList()
```

### 학생 정보 입력화면을 구성해준다.

[res/layout/fragment_input_student.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:transitionGroup="true"
    tools:context=".fragment.InputStudentFragment">

    <com.google.android.material.appbar.MaterialToolbar
        android:id="@+id/toolbarInputStudent"
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
            android:id="@+id/textFieldInputStudentName"
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
            android:id="@+id/toggleInputStudentGrade"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            app:checkedButton="@id/buttonInputGradeOne"
            app:selectionRequired="true"
            app:singleSelection="true">

            <Button
                android:id="@+id/buttonInputGradeOne"
                style="@style/Widget.Material3.Button.OutlinedButton"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="1학년" />

            <Button
                android:id="@+id/buttonInputGradeTow"
                style="@style/Widget.Material3.Button.OutlinedButton"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="2학년" />

            <Button
                android:id="@+id/buttonInputGradeThree"
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
            android:id="@+id/toggleInputStudentType"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            app:checkedButton="@id/buttonInputTypeBasketBall"
            app:selectionRequired="true"
            app:singleSelection="true">

            <Button
                android:id="@+id/buttonInputTypeBasketBall"
                style="@style/Widget.Material3.Button.OutlinedButton"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="농구부" />

            <Button
                android:id="@+id/buttonInputTypeSoccer"
                style="@style/Widget.Material3.Button.OutlinedButton"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="축구부" />

            <Button
                android:id="@+id/buttonInputTypeBaseBall"
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
            android:id="@+id/toggleInputStudentGender"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            app:checkedButton="@id/buttonInputGenderMale"
            app:selectionRequired="true"
            app:singleSelection="true">

            <Button
                android:id="@+id/buttonInputGenderMale"
                style="@style/Widget.Material3.Button.OutlinedButton"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="남자" />

            <Button
                android:id="@+id/buttonInputGenderFemale"
                style="@style/Widget.Material3.Button.OutlinedButton"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_weight="1"
                android:text="여자" />

        </com.google.android.material.button.MaterialButtonToggleGroup>

        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/textFieldInputStudentKorean"
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
            android:id="@+id/textFieldInputStudentEnglish"
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
            android:id="@+id/textFieldInputStudentMath"
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
            android:id="@+id/buttonInputStudentSubmit"
            style="@style/Widget.Material3.Button.OutlinedButton"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="10dp"
            android:text="입력 완료" />

    </LinearLayout>

</LinearLayout>
```

---

# 일단 여기까지 기능을 구현하겠습니다.

---

# 관리자 비밀번호 처리

### Preferences에 관리자 비밀번호가 저장되어 있는지 확인한다.

[MainActivity.kt - onCreate()]
```kt
        // Preferences 객체를 가져온다.
        val managerPef = getSharedPreferences("manager", MODE_PRIVATE)
        // 저장되어 있는 비밀번호를 가져온다.
        val managerPassword = managerPef.getString("password", null)
        // 저장되어 있는 비밀번호가 없다면..
        if(managerPassword == null){
            replaceFragment(FragmentName.SETTING_PASSWORD_FRAGMENT, false, false, null)
        } else {
            replaceFragment(FragmentName.LOGIN_FRAGMENT, false, false, null)
        }
```

### 입력 완료 처리를 해준다.

[fragment/SettingPasswordFragment.kt]
```kt
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
```

### 메서드를 호출해준다.

[fragment/settingPasswordFragment.kt - settingButtonSettingPasswordSubmit()]
```kt
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
```

---

# 관리자 로그인 처리

### 로그인 처리하는 메서드를 구현해준다.

[fragment/LoginFragment.kt]
```kt
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
                textFieldLoginPassword.editText?.setText("")
                textFieldLoginPassword.editText?.requestFocus()
                return
            }

            // 비밀번호를 제대로 입력했다면 화면을 이동한다.
            // MainFragment로 이동한다.
            mainActivity.replaceFragment(FragmentName.MAIN_FRAGMENT, false, true, null)
        }
    }
```

### 메서드를 호출한다.

[fragemnt/LoginFragment.kt - settingInitView()]
```kt

                // 로그인 처리 메서드를 호출한다.
                processingLogin()
```


---

# 학생 정보 저장 처리

### 학생 정보를 관리할 VO 클래스를 정의한다.

[database/StudentVO.kt]
```kt
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "StudentTable")
data class StudentVO(
    @PrimaryKey(autoGenerate = true)
    // 학생 번호
    var studentIdx:Int = 0,
    // 학생 이름
    var studentName:String = "",
    // 학년
    var studentGrade:Int = 0,
    // 운동부
    var studentType:Int = 0,
    // 성별
    var studentGender:Int = 0,
    // 국어점수
    var studentKorean:Int = 0,
    // 영어점수
    var studentEnglish:Int = 0,
    // 수학점수
    var studentMath:Int = 0
)
```

### dao 인터페이스를 만들어준다.

[database/StudentDao.kt]

```kt
import androidx.room.Dao

@Dao
interface StudentDao {
}
```

### database 파일을 만들어준다.

[database/StudentDataBase.kt]

```kt

@Database(entities = [StudentVO::class], version = 1, exportSchema = true)
abstract class StudentDataBase : RoomDatabase(){
    // dao
    abstract fun studentDao() : StudentDao

    companion object{
        // 데이터 베이스 객체를 담을 변수
        var studentDatabase:StudentDataBase? = null
        @Synchronized
        fun getInstance(context: Context) : StudentDataBase?{
            // 만약 데이터베이스 객체가 null이라면 객체를 생성한다.
            // 데이터베이스 파일 이름 꼭 변경!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            synchronized(StudentDataBase::class){
                studentDatabase = Room.databaseBuilder(
                    context.applicationContext, StudentDataBase::class.java,
                    "Student.db"
                ).build()
            }
            return studentDatabase
        }

        // 데이터 베이스 객체가 소멸될 때 호출되는 메서드
        fun destroyInstance(){
            studentDatabase = null
        }
    }
}
```

### 툴바를 구성하는 메서드를 구현해준다.

[fragment/InputStudentFragment.kt]
```kt
    // 툴바를 구성하는 메서드
    fun settingToolbarInputStudent(){
        fragmentInputStudentBinding.apply {
            toolbarInputStudent.title = "학생 정보 입력"
            toolbarInputStudent.setNavigationIcon(R.drawable.arrow_back_24px)
            toolbarInputStudent.setNavigationOnClickListener {
                mainFragment.removeFragment(SubFragmentName.INPUT_STUDENT_FRAGMENT)
            }
        }
    }
```

### 메서드를 호출한다.

```kt
        // 툴바를 구성하는 메서드를 호출한다.
        settingToolbarInputStudent()
```

### 입력 요소에 대한 설정을 하는 메서드를 구현한다.
```kt
    // 입력 요소 초기 설정
    fun settingTextField(){
        fragmentInputStudentBinding.apply {
            mainActivity.showSoftInput(textFieldInputStudentName.editText!!)
        }
    }
```

### 메서드를 호출한다

```kt
        // 입력 요소 초기 설정 메서드를 호출한다.
        settingTextField()
```

### 다이얼로그를 띄워주는 메서드를 만들어준다.

[MainActivity.kt]
```kt
    // 확인 버튼만 있는 다이얼로그를 띄우는 메서드
    fun showConfirmDialog(title:String, message:String, callback:() -> Unit){
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("확인"){ dialogInterface: DialogInterface, i: Int ->
            callback()
        }
        builder.show()
    }
```

### util 패키지에 Value.kt를 만들어준다.

[util/Value.kt]
```kt

enum class StudentGrade(val number:Int, val str:String){
    STUDENT_GRADE_1(1, "1학년"),
    STUDENT_GRADE_2(2, "2학년"),
    STUDENT_GRADE_3(3, "3학년"),
}

enum class StudentType(val number:Int, val str:String){
    STUDENT_TYPE_BASKETBALL(1, "농구부"),
    STUDENT_TYPE_SOCCER(2, "축구부"),
    STUDENT_TYPE_BASEBALL(3, "야구부"),
}

enum class StudentGender(val number:Int, val str:String){
    STUDENT_GENDER_MALE(1, "남자"),
    STUDENT_GENDER_FEMALE(2, "여자"),
}

fun numberToStudentGrade(studentGrade:Int) = when(studentGrade){
    1 -> StudentGrade.STUDENT_GRADE_1
    2 -> StudentGrade.STUDENT_GRADE_2
    else -> StudentGrade.STUDENT_GRADE_3
}

fun numberToStudentType(studentType:Int) = when(studentType){
    1 -> StudentType.STUDENT_TYPE_BASKETBALL
    2 -> StudentType.STUDENT_TYPE_SOCCER
    else -> StudentType.STUDENT_TYPE_BASEBALL
}

fun numberToStudentGender(studentGender:Int) = when(studentGender){
    1 -> StudentGender.STUDENT_GENDER_MALE
    else -> StudentGender.STUDENT_GENDER_FEMALE
}
```

### InputStudentFragment에 대한 ViewModel 클래스를 정의한다

[viewmodel/InputStudentViewModel.kt]

```kt
import com.lion.a07_studentmanager.util.StudentGender
import com.lion.a07_studentmanager.util.StudentGrade
import com.lion.a07_studentmanager.util.StudentType

class InputStudentViewModel(
    val studentIdx:Int,
    val studentName:String,
    val studentGrade:StudentGrade,
    val studentType: StudentType,
    val studentGender: StudentGender,
    val studentKorean:Int,
    val studentEnglish:Int,
    val studentMath:Int
)
```

### dao에 학생 정보를 저장하는 메서드를 정의해준다.

[database/StudentDao.kt]

```kt
// 학생정보 저장
@Insert
fun insertStudentData(studentVO: StudentVO)
```

### Repository 클래스를 만들어준다.

[database/StudentRepository.kt]
```kt
class StudentRepository {

    companion object{
        
    }
}
```

### Repository에 학생 정보를 저장하는 메서드를 구현해준다.

[repository/StudentRepository.kt]
```kt
        // 학생 정보를 저장하는 메서드
        fun insertStudentData(context: Context, inputStudentViewModel: InputStudentViewModel){
            // 데이터를 VO 객체에 담는다.
            val studentVO = StudentVO(
                studentName = inputStudentViewModel.studentName,
                studentGrade = inputStudentViewModel.studentGrade.number,
                studentType = inputStudentViewModel.studentType.number,
                studentGender = inputStudentViewModel.studentGender.number,
                studentKorean = inputStudentViewModel.studentKorean,
                studentEnglish = inputStudentViewModel.studentEnglish,
                studentMath = inputStudentViewModel.studentMath
            )
            // 저장한다.
            val studentDataBase = StudentDataBase.getInstance(context)
            studentDataBase?.studentDao()?.insertStudentData(studentVO)
        }
```

###  InputStudentFragment에 학생 정보를 저장하는 메서드를 구현해준다.

[fragment/InputStudentFragment.kt]
```kt
    // 학생 정보 등록 완료 처리 메서드
    fun processingAddStudentInfo(){
        fragmentInputStudentBinding.apply {
            // 사용자가 입력한 값을 가져온다
            val studentName = textFieldInputStudentName.editText?.text?.toString()!!
            val studentKorean = textFieldInputStudentKorean.editText?.text?.toString()!!
            val studentEnglish = textFieldInputStudentEnglish.editText?.text?.toString()!!
            val studentMath = textFieldInputStudentMath.editText?.text?.toString()!!

            // 사용자 이름
            if(studentName.isEmpty()){
                mainActivity.showConfirmDialog("이름 입력 오류", "학생의 이름을 입력해세요"){
                    textFieldInputStudentName.editText?.requestFocus()
                }
                return
            }
            // 국어점수
            if(studentKorean.isEmpty()){
                mainActivity.showConfirmDialog("국어 점수 입력 오류", "국어 점수를 입력해주세요"){
                    textFieldInputStudentKorean.editText?.requestFocus()
                }
                return
            }
            // 영어점수
            if(studentEnglish.isEmpty()){
                mainActivity.showConfirmDialog("영어 점수 입력 오류", "영어 점수를 입력해주세요"){
                    textFieldInputStudentEnglish.editText?.requestFocus()
                }
                return
            }
            // 수학점수
            if(studentMath.isEmpty()){
                mainActivity.showConfirmDialog("수학 점수 입력 오류", "수학 점수를 입력해주세요"){
                    textFieldInputStudentMath.editText?.requestFocus()
                }
                return
            }

            // 점수를 정수값으로 변환한다.
            val studentKoreanInt = studentKorean.toInt()
            val studentEnglishInt = studentEnglish.toInt()
            val studentMathInt = studentMath.toInt()
            // 학년
            val studentGrade = when(toggleInputStudentGrade.checkedButtonId){
                R.id.buttonInputGradeOne -> StudentGrade.STUDENT_GRADE_1
                R.id.buttonInputGradeTow -> StudentGrade.STUDENT_GRADE_2
                else -> StudentGrade.STUDENT_GRADE_3
            }
            // 운동부
            val studentType = when(toggleInputStudentType.checkedButtonId){
                R.id.buttonInputTypeBasketBall -> StudentType.STUDENT_TYPE_BASKETBALL
                R.id.buttonInputTypeSoccer -> StudentType.STUDENT_TYPE_SOCCER
                else -> StudentType.STUDENT_TYPE_BASEBALL
            }
            // 성별
            val studentGender = when(toggleInputStudentGender.checkedButtonId){
                R.id.buttonInputGenderMale -> StudentGender.STUDENT_GENDER_MALE
                else -> StudentGender.STUDENT_GENDER_FEMALE
            }

            // ViewModel 객체에 담는다.
            val inputStudentViewModel = InputStudentViewModel(
                0, studentName, studentGrade, studentType, studentGender,
                studentKoreanInt, studentEnglishInt, studentMathInt
            )

            // 데이터를 저장하는 메서드를 호출한다.
            CoroutineScope(Dispatchers.Main).launch{
                val work1 = async(Dispatchers.IO){
                    StudentRepository.insertStudentData(mainActivity, inputStudentViewModel)
                }
                work1.join()
                // 이전 화면으로 돌아간다.
                mainFragment.removeFragment(SubFragmentName.INPUT_STUDENT_FRAGMENT)
            }
        }
    }

```

### 버튼을 구성하는 메서를 구현한다.

[fragment/InputStudentFragment.kt]
```kt
    // 버튼 구성 메서드
    fun settingButtonInputStudentSubmit(){
        fragmentInputStudentBinding.apply {
            buttonInputStudentSubmit.setOnClickListener {
                // 학생 정보 등록 완료 처리 메서드를 호출한다.
                processingAddStudentInfo()
            }
        }
    }
```

### 메서드를 호출한다.

[fragment/InputStudentFragment.kt - onCreateView()]
```kt
        // 버튼 구성 메서드를 호출한다.
        settingButtonInputStudentSubmit()
```

---

# StudentListFragment의 기능을 구현한다.

### dao에 학생 정보 전체를 가져오는 메서드를 정의한다.

[database/StudentDao.kt]
```kt
    // 학생 정보 목록을 가져온다.
    // select * from StudentTable
    // StudentTable에서 모든 컬럼의 데이터를 가져온다.
    // 조건이 없으므로 모든 행들을 가져온다.

    // order by 컬럼명 : 컬럼명을 기준으로 행을 오름 차순 정렬한다.
    // order by 컬럼명 desc : 컬럼명을 기준으로 행을 내림 차순 정렬한다.
    @Query("""
        select * from StudentTable
        order by studentIdx desc
    """)
    fun selectStudentDataAll():List<StudentVO>
```

### Repository에 학생 데이터 전체를 가져오는 메서드를 구현한다.

[repository/StudentRepository.kt]
```kt
        // 학생 데이터 전체를  가져오는 메서드
        fun selectStudentDataAll(context: Context) : MutableList<StudentModel>{
            // 데이터를 가져온다.
            val studentDataBase = StudentDataBase.getInstance(context)
            val studentList = studentDataBase?.studentDao()?.selectStudentDataAll()

            // 학생 데이터를 담을 리스트
            val tempList = mutableListOf<StudentModel>()

            // 학생의 수 만큼 반복한다.
            studentList?.forEach {
                val studentModel = StudentModel(
                    it.studentIdx, it.studentName, numberToStudentGrade(it.studentGrade),
                    numberToStudentType(it.studentType), numberToStudentGender(it.studentGender),
                    it.studentKorean, it.studentEnglish, it.studentMath
                )
                // 리스트에 담는다.
                tempList.add(studentModel)
            }
            return tempList
        }
```

### Recyclerview 구성을 위한 임시 데이터를 없애준다.

[fragment/StudentListFragment.kt]
```kt
    // RecyclerView 구성을 위한 임시데이터
//    val tempData = Array(100){
//        "학생 ${it + 1}"
//    }
```

### 학생 객체를 담고 있을 리스트를 선언해준다.

[fragment/StudentListFragment.kt]
```kt
    // 학생 데이터를 담고 있는 리스트
    var studentList = mutableListOf<StudentModel>()
```

### Adapter 메서드를 수정한다.


[fragment/StudentListFragment.kt - RecyclerViewStudentListAdapter]
```kt
        override fun getItemCount(): Int {
            return studentList.size
        }

        override fun onBindViewHolder(holder: ViewHolderStudentList, position: Int) {
            holder.rowText1Binding.textViewRow.text = studentList[position].studentName
        }
```

### 리사이클러 뷰를 갱신하는 메서드를 구현해준다.

[fragment/StudentListFragment.kt]
```kt
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
```

### 메서드를 호출한다.

[fragment/StudentListFragment.kt - settingRecyclerViewStudentList()]
```kt
            // 데이터를 읽어와 리사이클러 뷰를 갱신한다.
            refreshRecyclerView()
```

---

# 학생 목록에서 학생을 눌렀을 때를 구현한다.

### RecyclerView의 항목을 누르면 학생 정보를 보는 화면으로 이동할 수 있도록 구현한다.

[fragment/StudentListFragment.kt - RecyclerViewStudentListAdapter]
```kt
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
```

### 리스너를 연결해준다.

[fragment/StudentListFragment.kt - RecyclerViewStudentListAdapter]
```kt
        rowText1Binding.root.setOnClickListener(viewHolderStudentList)
```

### 학생의 데이터를 가져오는 메서드를 dao에 정의한다.

[database/StudentDao.kt]
```kt
    // 학생 번호와 같은 학생의 데이터를 반환한다.
    // where studentIdx = 값
    // 테이블에 있는 행들 중에 studentIdx 컬럼의 값이 지정된 값과 같은 행들만 가져온다.
    @Query("""
        select * from StudentTable
        where studentIdx = :studentIdx
    """)
    fun selectStudentDataByStudentIdx(studentIdx:Int):StudentVO
```

### Repository에 학생 한명의 정보를 가져오는 메서드를 만들어준다.

```kt
        // 학생 한명의 데이터를 가져오는 메서드
        fun selectStudentDataByStudentIdx(context: Context, studentIdx:Int) : StudentModel{
            val studentDataBase = StudentDataBase.getInstance(context)
            // 학생 데이터를 가져온다.
            val studentVo = studentDataBase?.studentDao()?.selectStudentDataByStudentIdx(studentIdx)
            // Model 객체에 담는다.
            val studentModel = StudentModel(
                studentVo?.studentIdx!!, studentVo.studentName, numberToStudentGrade(studentVo.studentGrade),
                numberToStudentType(studentVo.studentType), numberToStudentGender(studentVo.studentGender),
                studentVo.studentKorean, studentVo.studentEnglish, studentVo.studentMath
            )
            return studentModel
        }
```

### 데이터를 가져와 보여주는 메서드를 구현한다.

[fragment/ShowStudentFragment.kt]
```kt
    // 학생 데이터를 가져와 보여주는 메서드
    fun settingTextField(){
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                // 학생 번호를 가져온다.
                val studentIdx = arguments?.getInt("studentIdx")
                // 학생 데이터를 가져온다.
                StudentRepository.selectStudentDataByStudentIdx(mainActivity, studentIdx!!)
            }
            val studentModel = work1.await()

            fragmentShowStudentBinding.apply {
                textFieldShowStudentName.editText?.setText(studentModel.studentName)
                textFieldShowStudentGrade.editText?.setText(studentModel.studentGrade.str)
                textFieldShowStudentType.editText?.setText(studentModel.studentType.str)
                textFieldShowStudentGender.editText?.setText(studentModel.studentGender.str)
                textFieldShowStudentKorean.editText?.setText(studentModel.studentKorean.toString())
                textFieldShowStudentEnglish.editText?.setText(studentModel.studentEnglish.toString())
                textFieldShowStudentMath.editText?.setText(studentModel.studentMath.toString())
            }
        }
    }
```

### 메서드를 호출해준다

[fragment/ShowStudentFragment.kt - onCreateView()]
```kt
        // 학생 데이터를 가져와 보여주는 메서드
        settingTextField()
```

---

# 학생 데이터 삭제

### dao에 학생 데이터를 삭제하는 메서드를 정의한다.

[database/StudentDao.kt]
```kt
    // 학생 정보를 삭제한다.
    // 매개변수로 받은 객체가 가지고 있는 프로퍼티 중에 primary key 프로퍼티가 조건식이 된다.
    @Delete
    fun deleteStudentData(studentVO: StudentVO)
```

### Repository에 삭제하는 메서드를 만들어준다.

[repository/StudentRepository.kt]
```kt
        // 학생 정보를 삭제하는 메서드
        fun deleteStudentDataByStudentIdx(context: Context, studentIdx:Int){
            // 삭제한다.
            val studentDataBase = StudentDataBase.getInstance(context)
            val studentVO = StudentVO(studentIdx = studentIdx)
            studentDataBase?.studentDao()?.deleteStudentData(studentVO)
        }
```

### 삭제 처리하는 메서드를 구현한다.

[fragment/ShowStudentFragment.kt]
```kt
    // 학생 정보를 삭제하는 메서드
    fun deleteStudentData(){
        CoroutineScope(Dispatchers.Main).launch {
            val work1 = async(Dispatchers.IO){
                // 삭제한다.
                val studentIdx = arguments?.getInt("studentIdx")!!
                StudentRepository.deleteStudentDataByStudentIdx(mainActivity, studentIdx)
            }
            work1.join()
            // 학생 목록을 보는 화면으로 돌아간다.
            mainFragment.removeFragment(SubFragmentName.SHOW_STUDENT_FRAGMENT)
        }
    }
```

### 삭제 메뉴를 눌렀을 때를 구현한다.

[fragment/ShowStudentFragment.kt - settingToolbarShowStudent()]
```kt
                    R.id.show_student_menu_remove -> {
                        // mainFragment.removeFragment(SubFragmentName.SHOW_STUDENT_FRAGMENT)
                        // 다이얼로를 띄운다.
                        val builder = MaterialAlertDialogBuilder(mainActivity)
                        builder.setTitle("학생 정보 삭제")
                        builder.setMessage("학생 정보 삭제시 복구가 불가능합니다")
                        builder.setNegativeButton("취소", null)
                        builder.setPositiveButton("삭제"){ dialogInterface: DialogInterface, i: Int ->
                            // 삭제 메서드를 호출한다.
                            deleteStudentData()
                        }
                        builder.show()
                    }
```

---

# 학생 정보 수정 구현

### 학생 정보를 보는 화면에서 수정 화면으로 이동할 때 학생 번호를 전달한다.

[fragment/ShowStudentFragment.kt - settingToolbarShowStudent()]
```kt
                    R.id.show_student_menu_modify -> {
                        // 정보 수정 화면으로 이동한다.

                        // 학생 번호를 추출하여 전달해준다.
                        val studentIdx = arguments?.getInt("studentIdx")
                        val dataBundle = Bundle()
                        dataBundle.putInt("studentIdx", studentIdx!!)

                        mainFragment.replaceFragment(SubFragmentName.MODIFY_STUDENT_FRAGMENT,
                            true, true, dataBundle)
                    }
```

### 입력 요소를 구성하는 메서드를 구현한다.

[fragment/ModifyStudentFragment.kt - settingTextField()]
```kt
    // 입력 요소를 구성하는 메서드
    fun settingTextField(){
        fragmentModifyStudentBinding.apply {
            // 학생 번호를 가져온다.
            val studentIdx = arguments?.getInt("studentIdx")!!
            // 학생 데이터를 가져온다.
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    StudentRepository.selectStudentDataByStudentIdx(mainActivity, studentIdx)
                }
                val studentModel = work1.await()

                textFieldModifyStudentName.editText?.setText(studentModel.studentName)

                when(studentModel.studentGrade){
                    StudentGrade.STUDENT_GRADE_1 -> toggleModifyStudentGrade.check(R.id.buttonGradeOne)
                    StudentGrade.STUDENT_GRADE_2 -> toggleModifyStudentGrade.check(R.id.buttonGradeTow)
                    StudentGrade.STUDENT_GRADE_3 -> toggleModifyStudentGrade.check(R.id.buttonGradeThree)
                }

                when(studentModel.studentType){
                    StudentType.STUDENT_TYPE_BASEBALL -> toggleModifyStudentType.check(R.id.buttonTypeBaseBall)
                    StudentType.STUDENT_TYPE_BASKETBALL -> toggleModifyStudentType.check(R.id.buttonTypeBasketBall)
                    StudentType.STUDENT_TYPE_SOCCER -> toggleModifyStudentType.check(R.id.buttonTypeSoccer)
                }

                when(studentModel.studentGender){
                    StudentGender.STUDENT_GENDER_MALE -> toggleModifyStudentGender.check(R.id.buttonGenderMale)
                    StudentGender.STUDENT_GENDER_FEMALE -> toggleModifyStudentGender.check(R.id.buttonGenderFemale)
                }

                textFieldModifyStudentKorean.editText?.setText(studentModel.studentKorean.toString())
                textFieldModifyStudentEnglish.editText?.setText(studentModel.studentEnglish.toString())
                textFieldModifyStudentMath.editText?.setText(studentModel.studentMath.toString())
            }
        }
    }
```

### 메서드를 호출한다.

[fragment/ModifyStudentFragment.kt - onCreateView()]
```kt
        // 입력 요소를 구성하는 메서드
        settingTextField()
```

### dao에 학생 정보를 수정하는 메서드를 정의해준다.

[database/StudentDao.kt]
```kt

    // 학생 정보를 수정한다.
    // 매개변수로 받은 객체가 가지고 있는 프로퍼티 중에 primary key 프로퍼티가 조건식이 된다.
    @Update
    fun updateStudentData(studentVO: StudentVO)
```

### Repository에 학생 정보를 수정하는 메서드를 만들어준다.

[repository/StudentRepository.kt]
```kt
        // 학생 정보를 수정하는 메서드
        fun updateStudentDataByStudentIdx(context: Context, studentModel: StudentModel){
            // VO에 데이터를 담는다.
            val studentVO = StudentVO(
                studentModel.studentIdx, studentModel.studentName, studentModel.studentGrade.number,
                studentModel.studentType.number, studentModel.studentGender.number,
                studentModel.studentKorean, studentModel.studentEnglish, studentModel.studentMath
            )
            // 수정하는 메서드를 호출한다.
            val studentDataBase = StudentDataBase.getInstance(context)
            studentDataBase?.studentDao()?.updateStudentData(studentVO)
        }
```

### 수정 처리 메서드를 구현한다.

[fragment/ModifyStudentFragment.kt]
```kt

    // 학생 정보 수정 처리 메서드
    fun processingModifyStudentData(){
        fragmentModifyStudentBinding.apply {
            // 데이터를 추출한다.
            val studentIdx = arguments?.getInt("studentIdx")!!
            val studentName = textFieldModifyStudentName.editText?.text.toString()
            // 학년
            val studentGrade = when(toggleModifyStudentGrade.checkedButtonId){
                R.id.buttonGradeOne -> StudentGrade.STUDENT_GRADE_1
                R.id.buttonGradeTow -> StudentGrade.STUDENT_GRADE_2
                else -> StudentGrade.STUDENT_GRADE_3
            }
            // 운동부
            val studentType = when(toggleModifyStudentType.checkedButtonId){
                R.id.buttonTypeBasketBall -> StudentType.STUDENT_TYPE_BASKETBALL
                R.id.buttonTypeSoccer -> StudentType.STUDENT_TYPE_SOCCER
                else -> StudentType.STUDENT_TYPE_BASEBALL
            }
            // 성별
            val studentGender = when(toggleModifyStudentGender.checkedButtonId){
                R.id.buttonGenderMale -> StudentGender.STUDENT_GENDER_MALE
                else -> StudentGender.STUDENT_GENDER_FEMALE
            }
            val studentKorean = textFieldModifyStudentKorean.editText?.text.toString().toInt()
            val studentEnglish = textFieldModifyStudentEnglish.editText?.text.toString().toInt()
            val studentMath = textFieldModifyStudentMath.editText?.text.toString().toInt()

            // 모델에 담아둔다.
            val studentModel = StudentModel(
                studentIdx, studentName, studentGrade, studentType, studentGender,
                studentKorean, studentEnglish, studentMath
            )
            // 수정 처리 메서를 호출해준다.
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    StudentRepository.updateStudentDataByStudentIdx(mainActivity, studentModel)
                }
                work1.join()
                // 이전 화면으로 돌아간다.
                mainFragment.removeFragment(SubFragmentName.MODIFY_STUDENT_FRAGMENT)
            }
        }
    }
```

### 수정 완료 버튼을 눌렀을 때 호출될 메서드를 구현한다.

```kt

    // 버튼을 구성하는 메서드
    fun settingButton(){
        fragmentModifyStudentBinding.apply {
            buttonModifyStudentSubmit.setOnClickListener {
                // 다이얼로그를 띄운다.
                val builder = MaterialAlertDialogBuilder(mainActivity)
                builder.setTitle("학생 정보 수정")
                builder.setMessage("학생 정보 수정시 복구가 불가능합니다")
                builder.setNegativeButton("취소", null)
                builder.setPositiveButton("수정"){ dialogInterface: DialogInterface, i: Int ->
                    // 수정 메서드를 호출한다.
                    processingModifyStudentData()
                }
                builder.show()
            }
        }
    }
```

### 메서드를 호출한다.

[fragment/ModifyStudentFragment.kt - onCreateView()]
```kt
        // 버튼을 구성하는 메서드를 호출한다.
        settingButton()
```

---

# 필터 기능 구현

### 전체를 의미하는 값을 정의한다.

[tools/Value.kt]

```kt
class ValueClass{
    companion object{
        // 전체를 의미하는 값
        val VALUE_ALL = 0
    }
}
```

### 필터 값을 담을 변수를 선언한다.

[fragment/StudentListFragment.kt]
```kt
    // 필터 값
    var filterStudentGrade = ValueClass.VALUE_ALL
    var filterStudentType = ValueClass.VALUE_ALL
    var filterStudentGender = ValueClass.VALUE_ALL
```

### 다이얼로그의 필터 옵션들을 설정하는 코드를 작성한다.

[fragment/StudentListFragment.kt - showFilterDialog]
```kt
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
```

### 다이얼로그의 확인 버튼을 누르면 선택한 옵션의 값을 변수에 담아준다.

[fragment/StudentListFragment.kt - showFilterDialog]
```kt
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
            }
```

### 데이터를 필터링하는 메서드를 구현한다.

[fragment/StudentRepository.kt]
```kt
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
```

### Recyclerview를 갱신하는 메서드에서 메서드를 호출해준다.

[fragment/StudentRepository.kt - refreshRecyclerView()]
```kt
                // 데이터를 필터링한다
                filteringData()
```

### RecyclerVioew를 갱신하는 메서드를 호출한다.

[fragment/StudnetRepository.kt - showFilterDialog()]

```kt
                // RecyclerView를 갱신한다.
                refreshRecyclerView()
```

---

# 만약 학생 정보를 새롭게 등록하고 돌아왔을 경우 필터를 초기화 하고 싶다면..
- 만약 유지하고 싶다면 이 작업은 하지 마세요~~
- 이 작업은 다른 프래그먼트를 갔다 돌아왔을 경우 무언가 작업을 해야할 경우를 예시로 하는 작업이다.
- 만약 Activity 였다면 ActivityResultLauncher를 이용하면 된다.
- 하지만 Fragment는 다른 Fragment를 갔다 돌아왔을 경우에 어떤 Fragment에서 돌아왔는지를 구분할 수 없다.
- 이에 변수를 정의해서 어떤 Fragment를 갔다 왔는지를 구분해줘야 한다.

### 학생 정보 입력 화면에서 돌아온 것인지를 구분하는 변수를 정의해준다.

[fragment/StudentListFragment.kt]
```kt
    // InputStudenFragment 를 갔다 왔는지 구분하기 위한 변수
    var isBackToInputStudentFragment = false
```

### 학생 정보 입력 화면으로 이동할 때 변수에 true를 넣어준다.

[fragment/StudentListFragment.kt - settingFabStudentList()]
```kt
               isBackToInputStudentFragment = true
```

### onCreateView에서 갔다 돌아왔을 때를 처리해준다.

[fragment/StudentListFragment.kt - onCreateView()]
```kt
        // InputStudentFragment에서 돌아온 경우라면
        if(isBackToInputStudentFragment == true){
            // 필요한 작업을 한다.
            filterStudentGender = ValueClass.VALUE_ALL
            filterStudentType = ValueClass.VALUE_ALL
            filterStudentGrade = ValueClass.VALUE_ALL

            isBackToInputStudentFragment = false
        }
```

---

# 검색

### UI 작업할 때 빠진 부분을 부분을 작업한다

[res/layout/fragment_search_student.xml]
```xml
            android:imeOptions="actionSearch"
            android:singleLine="true"
```

### 검색어를 입력하는 요소에 포커스를 준다.

[fragment/SearchStudentFragment.kt]
```kt
    // 입력 요소 설정
    fun settingTextField(){
        fragmentSearchStudentBinding.apply {
            // 검색창에 포커스를 준다.
            mainActivity.showSoftInput(textFieldSearchStudentName.editText!!)
        }
    }
```

### 메서드를 호출한다.

[fragment/SearchStudentFragment.kt - onCreateView()]
```kt
        // 입력 요소 설정 메서드를 호출한다.
        settingTextField()
```

### 리사이클러뷰 구성을 위한 임시 데이터는 제거한다.

[fragment/SearchStudentFragment.kt]
```kt
    // 리사이클러 뷰 구성을 위한 임시 데이터
//    val tempData = Array(100){
//        "학생 ${it + 1}"
//    }
```

### 리사이클로 뷰 구성을 위한 리스트를 선언해준다.

[fragment/SearchStudentFragment.kt]
```kt
    // 리사클리어뷰 구성을 위한 리스트
    var studentList = mutableListOf<StudentModel>()
```

### 어뎁터를 수정한다.

[fragment/SearchStudentFragment.kt - RecyclerViewStudentSearchAdapter]
```kt
        override fun getItemCount(): Int {
            return studentList.size
        }

        override fun onBindViewHolder(holder: ViewHolderStudentSearch, position: Int) {
            holder.rowText1Binding.textViewRow.text = studentList[position].studentName
        }
```

### dao 에 데이터를 가져오기 위한 메서드를 정의한다.

[database/StudentDao.kt]
```kt
    // where studentName = :studentName
    // studentName 컬럼의 값이 지정된 값과 같은 행만 가져온다.
    @Query("""
        select * from StudentTable
        where studentName = :studentName
        order by studentIdx desc
    """)
    fun selectStudentDataAllByStudentName(studentName:String):List<StudentVO>
```

### Repsitory에 이름을 통해 검색하는 메서드를 구현한다.

[repository/StudentRepository.kt]
```kt
        // 학생이름으로 검색하여 학생 데이터 전체를 가져오는 메서드
        fun selectStudentDataAllByStudentName(context: Context, studentName:String) : MutableList<StudentModel>{
            // 데이터를 가져온다.
            val studentDataBase = StudentDataBase.getInstance(context)
            val studentList = studentDataBase?.studentDao()?.selectStudentDataAllByStudentName(studentName)

            // 학생 데이터를 담을 리스트
            val tempList = mutableListOf<StudentModel>()

            // 학생의 수 만큼 반복한다.
            studentList?.forEach {
                val studentModel = StudentModel(
                    it.studentIdx, it.studentName, numberToStudentGrade(it.studentGrade),
                    numberToStudentType(it.studentType), numberToStudentGender(it.studentGender),
                    it.studentKorean, it.studentEnglish, it.studentMath
                )
                // 리스트에 담는다.
                tempList.add(studentModel)
            }
            return tempList
        }
```

### 키보드의 엔터키를 눌렀을 때 처리를 구현한다.

[fragment/SearchStudentFragment.kt - settingTextField()]
```kt
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
```

### 항목을 눌렀을 때의 부분을 수정한다.

[fragment/SearchStudentFragment.kt - RecyclerViewStudentSearchAdapter]
```kt
                // 학생 정보를 보는 화면으로 이동한다.
                val dataBundle = Bundle()
                dataBundle.putInt("studentIdx", studentList[adapterPosition].studentIdx)

                mainFragment.replaceFragment(SubFragmentName.SHOW_STUDENT_FRAGMENT,
                    true, true, dataBundle)
```

---

# 학생 성적 기능 구현

### StudentListFragment의 툴바에 네비게이션 메뉴를 배치한다.

[fragment/StduentListFragment.kt - settingToolbarStudentList()]
```kt
            // 네비게이션 메뉴
            toolbarStudentList.setNavigationIcon(R.drawable.menu_24px)
            toolbarStudentList.setNavigationOnClickListener {
                // 네비게이션 뷰를 보여준다.
                mainFragment.fragmentMainBinding.drawerLayoutMain.open()
            }
```


### NavigationView의 메뉴를 눌렀을 때를 구현한다.

[fragment/MainFragment.kt - settingNavigationViewMain()]
```kt
                // 메뉴 id로 분기한다
                when(it.itemId){
                    // 학생목록
                    R.id.navigation_main_menu_student_list -> {
                        replaceFragment(SubFragmentName.STUDENT_LIST_FRAGMENT, false,  false, null)
                    }
                    R.id.navigation_main_menu_student_point -> Log.d("test100", "학생성적")
                    R.id.navigation_main_menu_student_data -> Log.d("test100", "학생통계")
                    R.id.navigation_main_menu_calendar -> Log.d("test100", "학사일정")
                    R.id.navigation_main_menu_setting_manager -> Log.d("test100", "관리자설정")
                }

                drawerLayoutMain.close()
```

### 학생 성적 프래그먼트를 만들어준다.

[fragment/StudentPointFragment.kt]
```kt

class StudentPointFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentStudentPointBinding: FragmentStudentPointBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentStudentPointBinding = FragmentStudentPointBinding.inflate(inflater)
        mainActivity = activity as MainActivity

        return fragmentStudentPointBinding.root
    }

}
```

### Fragment의 이름을 정의해준다.

[fragment/MainFragment.kt - SubFragmentName]
```kt
    // 학생 성적 화면
    STUDENT_POINT_FRAGMENT(6, "StudentPointFragment"),
```

### Fragment의 객체를 생성한다.

[fragment/MainFragment.kt - replaceFragment()]
```kt
           // 학생 성적 화면
            SubFragmentName.STUDENT_POINT_FRAGMENT -> StudentPointFragment(this)
```

### 네비게이션 뷰의 메뉴를 누르면 프래그먼트가 보이도록 한다.

[fragment/MainFragment.kt - settingNavigationViewMain()]
```kt
                    R.id.navigation_main_menu_student_point -> {
                        replaceFragment(SubFragmentName.STUDENT_POINT_FRAGMENT, false, false, null)
                    }
```

### 화면을 구성한다.

[res/layout/fragment_student_point.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:transitionGroup="true"
    tools:context=".fragment.StudentPointFragment" >

    <com.google.android.material.appbar.MaterialToolbar
        android:id="@+id/toolbarStudentPoint"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@android:color/transparent"
        android:minHeight="?attr/actionBarSize"
        android:theme="?attr/actionBarTheme" />

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:orientation="vertical">

        <com.google.android.material.tabs.TabLayout
            android:id="@+id/tabLayoutStudentPoint"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            app:tabMode="auto">

            <com.google.android.material.tabs.TabItem
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="국어점수" />

            <com.google.android.material.tabs.TabItem
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="영어점수" />

            <com.google.android.material.tabs.TabItem
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="수학점수" />

            <com.google.android.material.tabs.TabItem
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="총점" />

            <com.google.android.material.tabs.TabItem
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:text="평균" />
        </com.google.android.material.tabs.TabLayout>

        <androidx.viewpager2.widget.ViewPager2
            android:id="@+id/viewPagerStudentPoint"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
    </LinearLayout>
</LinearLayout>
```

### ViewPager를 통해 보여줄 Fragment를 생성한다.

[fragment/StudentPointSubFragment.kt]
```kt
class StudentPointSubFragment : Fragment() {

    lateinit var fragmentStudentPointSubBinding: FragmentStudentPointSubBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentStudentPointSubBinding = FragmentStudentPointSubBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        return fragmentStudentPointSubBinding.root
    }

}
```

### 툴바를 구성하는 메서드를 구현한다.

[fragment/StudentPointFragment.kt]
```kt
    // 툴바 구성 메서드
    fun settingToolbar(){
        fragmentStudentPointBinding.apply {
            toolbarStudentPoint.title = "학생 점수 보기"
            toolbarStudentPoint.setNavigationIcon(R.drawable.menu_24px)
            toolbarStudentPoint.setNavigationOnClickListener {
                mainFragment.fragmentMainBinding.drawerLayoutMain.open()
            }
        }
    }
```

### 메서드를 호출한다.

[fragment/StudentPointFragment.kt - onCreateView()]
```kt
        // 툴바 구성 메서드를 호출한다.
        settingToolbar()
```

### ViewPager에 셋팅할 어뎁터 클래스를 만들어준다

[fragment/StudentPointFragment.kt]
```kt
    // ViewPager2의 어뎁터
    inner class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle){
        // ViewPager2를 통해 보여줄 프래그먼트의 개수
        override fun getItemCount(): Int {
            return 5
        }

        // position번째에서 사용할 Fragment 객체를 생성해 반환하는 메서드
        override fun createFragment(position: Int): Fragment {
            val newFragment = when(position){
                0 -> StudentPointSubFragment()
                1 -> StudentPointSubFragment()
                2 -> StudentPointSubFragment()
                3 -> StudentPointSubFragment()
                else -> StudentPointSubFragment()
            }
            return newFragment
        }
    }
```

### 탭을 구성하는 메서드를 작성해준다.


[fragment/StudentPointFragment.kt]
```kt
    // 탭을 구성하는 메서드
    fun settingTab(){
        fragmentStudentPointBinding.apply {
            // ViewPager2의 어뎁터를 설정한다. 
            viewPagerStudentPoint.adapter = ViewPagerAdapter(mainActivity.supportFragmentManager, lifecycle)

            // TabLayout과 ViewPager2가 상호 작용을 할 수 있도록 연동시켜준다.
            val tabLayoutMediator = TabLayoutMediator(tabLayoutStudentPoint, viewPagerStudentPoint) { tab, position ->
                // 각 탭에 보여줄 문자열을 새롭게 구성해줘야 한다.
                when(position){
                    0 -> tab.text = "국어점수"
                    1 -> tab.text = "영어점수"
                    2 -> tab.text = "수학점수"
                    3 -> tab.text = "총점"
                    4 -> tab.text = "평균"
                }
            }
            tabLayoutMediator.attach()
        }
    }
```

### 메서드를 호출한다.

[fragment/StudentPointFragment.kt - onCreateView()]
```kt
        // 탭을 구성하는 메서드를 호출한다.
        settingTab()
```

### 프래그먼트를 통해 보고자 하는 정보의 이름을 정의한다.

[fragment/StudentPointSubFragment.kt - StudentPointType]
```kt
// 프래그먼트를 통해 보고자 하는 정보 이름
enum class StudentPointType(val number:Int, val str:String){
    STUDENT_POINT_TYPE_KOREAN(1, "국어점수"),
    STUDENT_POINT_TYPE_ENGLISH(2, "영어점수"),
    STUDENT_POINT_TYPE_MATH(3, "수학점수"),
    STUDENT_POINT_TYPE_TOTAL(4, "총점"),
    STUDENT_POINT_TYPE_AVG(5, "평균")
}
```

### 주 생성자를 통해 타입을 받는다.

[fragment/StudentPointSubFragment.kt]
```kt
class StudentPointSubFragment(val studentPointType: StudentPointType) : Fragment() {
```

### 프래그먼트 객체를 생성하는 곳을 수정한다.

[fragment/StudentPointFragment.kt - ViewPagerAdapter]
```kt
                0 -> StudentPointSubFragment(StudentPointType.STUDENT_POINT_TYPE_KOREAN)
                1 -> StudentPointSubFragment(StudentPointType.STUDENT_POINT_TYPE_ENGLISH)
                2 -> StudentPointSubFragment(StudentPointType.STUDENT_POINT_TYPE_MATH)
                3 -> StudentPointSubFragment(StudentPointType.STUDENT_POINT_TYPE_TOTAL)
                else -> StudentPointSubFragment(StudentPointType.STUDENT_POINT_TYPE_AVG)
```

### ViewPager를 통해 보여줄 화면을 구성한다.

[res/layout/fragment_student_point_sub.xml]

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".fragment.StudentPointSubFragment" >

    <com.google.android.material.chip.Chip
        android:id="@+id/chipStudentPointOrdering"
        style="@style/Widget.Material3.Chip.Assist"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_margin="10dp"
        android:text="오름차순 △" />

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerViewStudentPoint"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</LinearLayout>
```

### ReyclcerView 구성을 위한 임시 데이터를 정의한다.

[fragment/StudentPointSubFragment.kt]
```kt
    // RecyclerView 구성을 위한 임시데이터
    val tempData = Array(100){
        "학생 ${it + 1}"
    }
```

### RecyclerView의 어뎁터를 작성한다.

[fragment/StudentPointSubFragment.kt]
```kt
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
            return tempData.size
        }

        override fun onBindViewHolder(holder: ViewHolderStudentPointSub, position: Int) {
            holder.rowText1Binding.textViewRow.text = tempData[position]
        }
    }
```

### RecyclerView를 구성하는 메서드를 작성해준다.

[fragment/StudentPointSubFragment.kt]
```kt
    // RecyclerView를 구성하는 메서드
    fun settingRecyclerView(){
        fragmentStudentPointSubBinding.apply {
            recyclerViewStudentPoint.adapter = RecyclerViewStudentPointSubAdapter()
            recyclerViewStudentPoint.layoutManager = LinearLayoutManager(mainActivity)
            val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewStudentPoint.addItemDecoration(deco)
        }
    }
```

### 메서드를 호출해준다.

[fragment/StudentPointSubFragment.kt - onCreateView()]
```kt
        // RecyclerView를 구성하는 메서드를 호출한다.
        settingRecyclerView()
```

### 정렬 기준값을 정의해준다.

[fragment/StudentPointSubFragment.kt]
```kt
// 정렬 기준 값
enum class StudentPointSort(val number:Int, var str:String){
    STUDENT_POINT_SORT_ASCENDING(1, "오름차순"),
    STUDENT_POINT_SORT_DESCENDING(2, "내림차순")
}
```

### 정렬 기준값을 담을 변수를 정의한다.

[fragment/StudentPointSubFragment.kt]
```kt
    // 정렬 기준값
    var studentPointSort = StudentPointSort.STUDENT_POINT_SORT_ASCENDING
```

### Chip을 구성하는 메서드를 구현해준다.

[fragment/StudentPointSubFragment.kt]
```kt
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
            }
        }
    }
```

### 메서드를 호출해준다.

[fragment/StudentPointSubFragment.kt - onCreateView()]
```kt
        // Chip을 구성하는 메서드를 호출한다.
        settingChip()
```

### StudentModel에 총점과 평균을 담을 프로퍼티를 정의한다.

[viewmodel/StudentModel.kt]
```kt
    var studentTotal:Int = 0,
    var studentAvg:Int = 0
```

### RecyclerView 구성을 위한 리스트를 정의한다.

[viewmodel/StudentPointSubFragment.kt]
```kt
    // RecyclerView 구성을 위한 임시데이터
//    val tempData = Array(100){
//        "학생 ${it + 1}"
//    }

    // RecyclerView 구성을 위한 리스트
    var studentList = mutableListOf<StudentModel>()
```

### 어뎁터를 수정한다.

[viewmodel/StudentPointSubFragment.kt - RecyclerViewStudentPointSubAdapter]
```kt

        override fun getItemCount(): Int {
            return studentList.size
        }

        override fun onBindViewHolder(holder: ViewHolderStudentPointSub, position: Int) {
            holder.rowText1Binding.textViewRow.text = studentList[position].studentName
        }
```

### 데이터를 가져와 정렬하고 RecyclerView를 갱신하는 메서드를 구현한다.
[viewmodel/StudentPointSubFragment.kt]
```kt

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

```

### 어뎁터에 출력하는 부분을 수정한다.

[viewmodel/StudentPointSubFragment.kt - RecyclerViewStudentPointSubAdapter]
```kt
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
```

### 메서드를 호출한다.

[fragment/StudentPointSubFragment.kt - onCreateView()]
```kt
        //  데이터를 가져오는 메서드
        gettingStudentPointData()
```

[fragment/StudentPointSubFragment.kt - settingChip()]
```kt
                //  데이터를 가져오는 메서드
                gettingStudentPointData()
```

---

# 학생 통계 구현

### Fragment를 생성한다.

[fragment/StudentInfoFragment.kt]

```kt
class StudentInfoFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentStudentInfoBinding: FragmentStudentInfoBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentStudentInfoBinding = FragmentStudentInfoBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        return fragmentStudentInfoBinding.root
    }
}
```

### Fragment의 이름을 정의한다.

[fragment/MainFragment.kt - SubFragmentName]
```kt
    // 학생 통계 화면
    STUDENT_INFO_FRAGMENT(7, "StudentInfoFragment"),
```

### Fragment의 객체를 생성한다.

[fragment/MainFragment.kt - replaceFragment()]
```kt
            // 학생 통계 화면
            SubFragmentName.STUDENT_INFO_FRAGMENT -> StudentInfoFragment(this)
```

### NavigationView의 메뉴를 누르면 프래그먼트가 보이게 한다.

[fragment/MainFragment.kt - settingNavigationViewMain()]
```kt
                    R.id.navigation_main_menu_student_data -> {
                        replaceFragment(SubFragmentName.STUDENT_INFO_FRAGMENT, false, false, null)
                    }
```

### 화면을 구성해준다.

[res/layout/fragment_student_info.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:transitionGroup="false"
    tools:context=".fragment.StudentInfoFragment" >

    <com.google.android.material.appbar.MaterialToolbar
        android:id="@+id/toolbarStudentInfo"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@android:color/transparent"
        android:minHeight="?attr/actionBarSize"
        android:theme="?attr/actionBarTheme" />

    <ScrollView
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical"
            android:padding="10dp" >

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/textFieldStudentInfoTotalCount"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="전체 학생의 수"
                app:startIconDrawable="@drawable/person_24px">

                <com.google.android.material.textfield.TextInputEditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:enabled="false"
                    android:singleLine="true"
                    android:text=" "
                    android:textColor="#000000" />
            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.divider.MaterialDivider
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="10dp" />

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/textFieldStudentInfoGradeOneCount"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="1학년의 수"
                app:startIconDrawable="@drawable/person_24px"
                android:layout_marginTop="10dp">

                <com.google.android.material.textfield.TextInputEditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:enabled="false"
                    android:singleLine="true"
                    android:text=" "
                    android:textColor="#000000" />
            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/textFieldStudentInfoGradeTwoCount"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="2학년의 수"
                app:startIconDrawable="@drawable/person_24px"
                android:layout_marginTop="10dp">

                <com.google.android.material.textfield.TextInputEditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:enabled="false"
                    android:singleLine="true"
                    android:text=" "
                    android:textColor="#000000" />
            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/textFieldStudentInfoGradeThreeCount"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="3학년의 수"
                app:startIconDrawable="@drawable/person_24px"
                android:layout_marginTop="10dp">

                <com.google.android.material.textfield.TextInputEditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:enabled="false"
                    android:singleLine="true"
                    android:text=" "
                    android:textColor="#000000" />
            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.divider.MaterialDivider
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="10dp" />

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/textFieldStudentInfoTypeBaseBallCount"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="야구부의 수"
                app:startIconDrawable="@drawable/person_24px"
                android:layout_marginTop="10dp">

                <com.google.android.material.textfield.TextInputEditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:enabled="false"
                    android:singleLine="true"
                    android:text=" "
                    android:textColor="#000000" />
            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/textFieldStudentInfoTypeSoccerCount"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="축구부의 수"
                app:startIconDrawable="@drawable/person_24px"
                android:layout_marginTop="10dp">

                <com.google.android.material.textfield.TextInputEditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:enabled="false"
                    android:singleLine="true"
                    android:text=" "
                    android:textColor="#000000" />
            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/textFieldStudentInfoTypeBasketBallCount"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="농구부의 수"
                app:startIconDrawable="@drawable/person_24px"
                android:layout_marginTop="10dp">

                <com.google.android.material.textfield.TextInputEditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:enabled="false"
                    android:singleLine="true"
                    android:text=" "
                    android:textColor="#000000" />
            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.divider.MaterialDivider
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="10dp" />

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/textFieldStudentInfoTypeKoreanTotal"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="국어 총점"
                app:startIconDrawable="@drawable/person_24px"
                android:layout_marginTop="10dp">

                <com.google.android.material.textfield.TextInputEditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:enabled="false"
                    android:singleLine="true"
                    android:text=" "
                    android:textColor="#000000" />
            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/textFieldStudentInfoTypeEnglishTotal"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="영어 총점"
                app:startIconDrawable="@drawable/person_24px"
                android:layout_marginTop="10dp">

                <com.google.android.material.textfield.TextInputEditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:enabled="false"
                    android:singleLine="true"
                    android:text=" "
                    android:textColor="#000000" />
            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/textFieldStudentInfoTypeMathTotal"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="수학 총점"
                app:startIconDrawable="@drawable/person_24px"
                android:layout_marginTop="10dp">

                <com.google.android.material.textfield.TextInputEditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:enabled="false"
                    android:singleLine="true"
                    android:text=" "
                    android:textColor="#000000" />
            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.divider.MaterialDivider
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="10dp" />

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/textFieldStudentInfoTypeEnglishAvg"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="영어 평균"
                app:startIconDrawable="@drawable/person_24px"
                android:layout_marginTop="10dp">

                <com.google.android.material.textfield.TextInputEditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:enabled="false"
                    android:singleLine="true"
                    android:text=" "
                    android:textColor="#000000" />
            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/textFieldStudentInfoTypeMathAvg"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="수학 평균"
                app:startIconDrawable="@drawable/person_24px"
                android:layout_marginTop="10dp">

                <com.google.android.material.textfield.TextInputEditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:enabled="false"
                    android:singleLine="true"
                    android:text=" "
                    android:textColor="#000000" />
            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.divider.MaterialDivider
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="10dp" />

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/textFieldStudentInfoTypeTotalAll"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="전체 총점"
                app:startIconDrawable="@drawable/person_24px"
                android:layout_marginTop="10dp">

                <com.google.android.material.textfield.TextInputEditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:enabled="false"
                    android:singleLine="true"
                    android:text=" "
                    android:textColor="#000000" />
            </com.google.android.material.textfield.TextInputLayout>

            <com.google.android.material.textfield.TextInputLayout
                android:id="@+id/textFieldStudentInfoTypeAvgAll"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:hint="전체 평균"
                app:startIconDrawable="@drawable/person_24px"
                android:layout_marginTop="10dp">

                <com.google.android.material.textfield.TextInputEditText
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:enabled="false"
                    android:singleLine="true"
                    android:text=" "
                    android:textColor="#000000" />
            </com.google.android.material.textfield.TextInputLayout>

        </LinearLayout>
    </ScrollView>
</LinearLayout>
```

### 툴바를 구성하는 메서드를 구현해준다.

[fragment/StudentInfoFragment.kt]
```kt
    // 툴바를 구성하는 메서드
    fun settingToolbar(){
        fragmentStudentInfoBinding.apply {
            toolbarStudentInfo.title = "학생 통계"
            toolbarStudentInfo.setNavigationIcon(R.drawable.menu_24px)
            toolbarStudentInfo.setNavigationOnClickListener {
                mainFragment.fragmentMainBinding.drawerLayoutMain.open()
            }
        }
    }
```

### 메서드를 호출한다.

[fragment/StudentInfoFragment.kt - onCreateView()]
```kt
        // 툴바를 구성하는 메서드를 호출한다.
        settingToolbar()
```

### TextField를 구성하는 메서드를 구현한다.

[fragment/StudentInfoFragment.kt]
```kt
    // 입력 요소를 설정하는 메서드
    fun settingTextField(){
        fragmentStudentInfoBinding.apply {
            CoroutineScope(Dispatchers.Main).launch {
                val work1 = async(Dispatchers.IO){
                    StudentRepository.selectStudentDataAll(mainActivity)
                }
                val studentList = work1.await()

                // 각 정보를 담을 변수들
                var gradeOneCount = 0
                var gradeTwoCount = 0
                var gradeThreeCount = 0
                var baseBallCount = 0
                var soccerCount = 0
                var basketBallCount = 0
                var koreanTotal = 0
                var englishTotal = 0
                var mathTotal = 0

                studentList.forEach {
                    when(it.studentGrade){
                        StudentGrade.STUDENT_GRADE_1 -> gradeOneCount++
                        StudentGrade.STUDENT_GRADE_2 -> gradeTwoCount++
                        StudentGrade.STUDENT_GRADE_3 -> gradeThreeCount++
                    }

                    when(it.studentType){
                        StudentType.STUDENT_TYPE_BASEBALL -> basketBallCount++
                        StudentType.STUDENT_TYPE_SOCCER -> soccerCount++
                        StudentType.STUDENT_TYPE_BASKETBALL -> baseBallCount++
                    }

                    koreanTotal += it.studentKorean
                    englishTotal += it.studentEnglish
                    mathTotal += it.studentMath
                }
                val totalCount = gradeOneCount + gradeTwoCount + gradeThreeCount
                var koreanAvg = koreanTotal / studentList.size
                var englishAvg = englishTotal / studentList.size
                var mathAvg = mathTotal / studentList.size

                var pointTotal = koreanTotal + englishTotal + mathTotal
                var pointAvg = pointTotal / (studentList.size * 3)

                textFieldStudentInfoTotalCount.editText?.setText("$totalCount 명")
                textFieldStudentInfoGradeOneCount.editText?.setText("$gradeOneCount 명")
                textFieldStudentInfoGradeTwoCount.editText?.setText("$gradeTwoCount 명")
                textFieldStudentInfoGradeThreeCount.editText?.setText("$gradeThreeCount 명")
                textFieldStudentInfoTypeBaseBallCount.editText?.setText("$baseBallCount 명")
                textFieldStudentInfoTypeSoccerCount.editText?.setText("$soccerCount 명")
                textFieldStudentInfoTypeBasketBallCount.editText?.setText("$basketBallCount 명")
                textFieldStudentInfoTypeKoreanTotal.editText?.setText("$koreanTotal 점")
                textFieldStudentInfoTypeEnglishTotal.editText?.setText("$englishTotal 점")
                textFieldStudentInfoTypeMathTotal.editText?.setText("$mathTotal 점")
                textFieldStudentInfoTypeKoreanAvg.editText?.setText("$koreanAvg 점")
                textFieldStudentInfoTypeEnglishAvg.editText?.setText("$englishAvg 점")
                textFieldStudentInfoTypeMathAvg.editText?.setText("$mathAvg 점")
                textFieldStudentInfoTypeTotalAll.editText?.setText("$pointTotal 점")
                textFieldStudentInfoTypeAvgAll.editText?.setText("$pointAvg 점")
            }
        }
    }
```

### 메서드를 호출한다.

[fragment/StudentInfoFragment.kt - onCreateView()]
```kt
        // 입력 요소를 설정하는 메서드
        settingTextField()
```

---

# 학사 일정 기능

### PlannerFragment 를 생성한다.

[fragment/PlannerFragment]
```kt
class PlannerFragment(val mainFragment: MainFragment) : Fragment() {
    
    lateinit var fragmentPlannerBinding: FragmentPlannerBinding
    lateinit var mainActivity: MainActivity
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentPlannerBinding = FragmentPlannerBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity
        return fragmentPlannerBinding.root
    }

}
```

### 프래그먼트의 이름을 정의한다.

[fragment/MainFragment.kt - SubFragmentName]
```kt
    // 학사 일정 화면
    PLANNER_FRAGMENT(8, "PlannerFragment"),
```

### 프래그먼트 객체를 생성한다.

[fragment/MainFragment.kt - replaceFragment()]
```kt
            // 학사 일정 화면
            SubFragmentName.PLANNER_FRAGMENT -> PlannerFragment(this)
```

### 프래그먼트를 변경하는 코드를 작성한다.

[fragment/MainFragment.kt - settingNavigationViewMain()]
```kt
                    R.id.navigation_main_menu_calendar -> {
                        replaceFragment(SubFragmentName.PLANNER_FRAGMENT, false, false, null)
                    }
```

### 화면을 구성한다.

[res/layout/fragment_planner.xml]

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:transitionGroup="true"
    tools:context=".fragment.PlannerFragment" >

    <com.google.android.material.appbar.MaterialToolbar
        android:id="@+id/toolbarPlanner"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@android:color/transparent"
        android:minHeight="?attr/actionBarSize"
        android:theme="?attr/actionBarTheme" />

    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/fragmentContainerViewPlanner"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</LinearLayout>
```

### 툴바의 메뉴를 구성한다.

[res/menu/planner_main_menu.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<menu xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:android="http://schemas.android.com/apk/res/android">

    <item
        android:id="@+id/planner_menu_add"
        android:icon="@drawable/add_24px"
        android:title="일정추가"
        app:showAsAction="always" />
    <item
        android:id="@+id/planner_menu_list"
        android:icon="@drawable/list_alt_24px"
        android:title="목록"
        app:showAsAction="always" />
    <item
        android:id="@+id/planner_menu_calendar"
        android:icon="@drawable/calendar_month_24px"
        android:title="달력"
        app:showAsAction="always" />
</menu>
```

### 학사 일정 모드를 나타내는 값을 정의한다.

[fragment/PlannerFragment.kt]
```kt
// 학사 일정 모드
enum class PlannerMode(val number:Int, var str:String){
    PLANNER_MODE_LIST(1, "PlannerModeList"),
    PLANNER_MODE_CALENDAR(2, "PlannerModelCalendar")
}
```

### 학사 일정 모드를 담을 변수를 정의해준다.

[fragment/PlannerFragment.kt]
```kt
    // Planner Mode 값을 담을 변수
    var plannerMode = PlannerMode.PLANNER_MODE_LIST
```

### 메뉴의 보이지는 것을 설정하는 메서드를 만든다.
[fragment/PlannerFragment.kt]
```kt
    // 학사 일정 모드에 따라 메뉴 노출을 설정하는 메서드
    fun settingMenuItemVisible(){
        fragmentPlannerBinding.apply {
            // 학사 일정 모드에 따라 메뉴를 숨긴다.
            val item1 = toolbarPlanner.menu.findItem(R.id.planner_menu_calendar)
            val item2 = toolbarPlanner.menu.findItem(R.id.planner_menu_list)
            when(plannerMode){
                PlannerMode.PLANNER_MODE_LIST -> {
                    item1.isVisible = true
                    item2.isVisible = false
                }
                PlannerMode.PLANNER_MODE_CALENDAR -> {
                    item1.isVisible = false
                    item2.isVisible = true
                }
            }
        }
    }
```

### 메서드를 호출한다.
[fragment/PlannerFragment.kt - settingToolbar()]
```kt
            settingMenuItemVisible()
```

### 툴바의 메뉴를 눌렀을 때를 구현해준다.
[fragment/PlannerFragment.kt - settingToolbar()]

```kt
            toolbarPlanner.setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.planner_menu_add -> {

                    }
                    R.id.planner_menu_list -> {
                        plannerMode = PlannerMode.PLANNER_MODE_LIST
                        settingMenuItemVisible()
                    }
                    R.id.planner_menu_calendar -> {
                        plannerMode = PlannerMode.PLANNER_MODE_CALENDAR
                        settingMenuItemVisible()
                    }
                }

                true
            }
```

### 프래그먼트를 만든다.

[fragment/PlannerListFragment.kt]
```kt
class PlannerListFragment(val mainFragment: MainFragment) : Fragment() {


    lateinit var fragmentPlannerListBinding: FragmentPlannerListBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentPlannerListBinding = FragmentPlannerListBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        return fragmentPlannerListBinding.root
    }

}
```

[fragment/PlannerCalendarFragment.kt]

```kt
class PlannerCalendarFragment(val mainFragment: MainFragment) : Fragment() {

    lateinit var fragmentPlannerCalendarBinding: FragmentPlannerCalendarBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentPlannerCalendarBinding = FragmentPlannerCalendarBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        return fragmentPlannerCalendarBinding.root
    }

}
```

### Fragment를 교체하는 메서드를 구현한다.

[fragment/PlannerFragment.kt]
```kt
    fun changePlannerSubFragment(){
        when(plannerMode){
            PlannerMode.PLANNER_MODE_LIST -> {
                mainActivity.supportFragmentManager.commit {
                    replace(R.id.fragmentContainerViewPlanner, PlannerListFragment(mainFragment))
                }
            }
            PlannerMode.PLANNER_MODE_CALENDAR -> {
                mainActivity.supportFragmentManager.commit {
                    replace(R.id.fragmentContainerViewPlanner, PlannerCalendarFragment(mainFragment))
                }
            }
        }
    }
```

### 메서드를 호출한다.
[fragment/PlannerFragment.kt - onCreateView()]
```kt
changePlannerSubFragment()
```

### 메뉴를 눌렀을 때도 호출해준다.
[fragment/PlannerFragment.kt - settingToolbar()]
```kt
R.id.planner_menu_list -> {
    plannerMode = PlannerMode.PLANNER_MODE_LIST
    settingMenuItemVisible()
    changePlannerSubFragment()
}
R.id.planner_menu_calendar -> {
    plannerMode = PlannerMode.PLANNER_MODE_CALENDAR
    settingMenuItemVisible()
    changePlannerSubFragment()
}
```

### PlannerListFragment의 화면을 구성해준다.

[res/layout/fragment_planner_list.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".fragment.PlannerListFragment" >

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerViewPlannerList"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />
</LinearLayout>
```

### 항목으로 사용할 레이아웃을 구성해준다.

[res/layout/row_text2.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="10dp">

    <TextView
        android:id="@+id/textViewRowText2MainTitle"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="TextView"
        android:textAppearance="@style/TextAppearance.AppCompat.Large" />

    <TextView
        android:id="@+id/textViewRowText2SubTitle"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:text="TextView" />
</LinearLayout>
```

### RecyclerView 구성을 위한 임시 데이터를 정의한다.
[fragment/PlannerListFragment.kt]
```kt
    // RecyclerView 구성을 위한 임시 데이터
    val tempMainTitleData = Array(50){
        "학사 일정 이름 $it"
    }

    val tempSubTitleData = Array(50){
        "2024-11-${it + 1}"
    }
```

### RecyclerView의 어뎁터를 작성한다.

[fragment/PlannerListFragment.kt]
```kt
    // RecyclerView의 어뎁터
    inner class RecyclerViewPlannerListAdapter : RecyclerView.Adapter<RecyclerViewPlannerListAdapter.ViewHolderPlannerList>(){
        // View Holder
        inner class ViewHolderPlannerList(val rowText2Binding: RowText2Binding) : RecyclerView.ViewHolder(rowText2Binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderPlannerList {
            val rowText2Binding = RowText2Binding.inflate(layoutInflater, parent, false)
            val viewHolderPlannerList = ViewHolderPlannerList(rowText2Binding)
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
```

### RecyclerView를 구성하는 메서드를 구현한다.

[fragment/PlannerListFragment.kt]
```kt
    // RecyclerView를 구성하는 메서드
    fun settingRecyclerView(){
        fragmentPlannerListBinding.apply {
            recyclerViewPlannerList.adapter = RecyclerViewPlannerListAdapter()
            recyclerViewPlannerList.layoutManager = LinearLayoutManager(mainActivity)
            val deco = MaterialDividerItemDecoration(mainActivity, MaterialDividerItemDecoration.VERTICAL)
            recyclerViewPlannerList.addItemDecoration(deco)
        }
    }
```

### 메서드를 호출한다.

[fragment/PlannerListFragment.kt - onCreateView()]
```kt
        // RecyclerView를 구성하는 메서드를 호출한다.
        settingRecyclerView()
```

### BottomSheet로 사용할 레이아웃을 구성한다.

[res/layout/bottom_sheet_menu_planner_list.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:paddingStart="10dp"
    android:paddingEnd="10dp">

    <com.google.android.material.bottomsheet.BottomSheetDragHandleView
        android:id="@+id/view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

    <Button
        android:id="@+id/buttonBottomSheetPlannerListModify"
        style="@style/Widget.Material3.Button.TextButton"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="수정" />

    <Button
        android:id="@+id/buttonBottomSheetPlannerListDelete"
        style="@style/Widget.Material3.Button.TextButton"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:text="삭제" />

</LinearLayout>
```

### BottomSheet를 구성하는 메서드를 구현한다.
[fragment/PlannerListFragment.kt]
```kt
    // 수정 삭제를 선택하기 위한 BottomSheet를 띄우는 메서드
    fun showMenuBottomSheet(){
        // BottomSheetDialog 사용
        val bottomSheetMenuPlannerListBinding = BottomSheetMenuPlannerListBinding.inflate(layoutInflater)
        val bottomSheetDialog = BottomSheetDialog(mainActivity)
        bottomSheetDialog.setContentView(bottomSheetMenuPlannerListBinding.root)

        // BottomSheetDialog를 띄운다.
        bottomSheetDialog.show()
    }
```

### Adapter에 LongClick 리스너를 구현해준다.

[fragment/PlannerListFragment.kt]
```kt
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
```

### BottomSheet 구성을 위한 Fragment를 생성한다.

[fragment/BottomSheetPlannerListModify.kt]

```kt

class BottomSheetPlannerListModify(val plannerListFragment: PlannerListFragment) : BottomSheetDialogFragment() {

    lateinit var bottomSheetPlannerListModifyBinding: FragmentBottomSheetPlannerListModifyBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        bottomSheetPlannerListModifyBinding = FragmentBottomSheetPlannerListModifyBinding.inflate(layoutInflater)
        mainActivity = activity as MainActivity

        return bottomSheetPlannerListModifyBinding.root
    }
}
```

### 화면을 구성한다.

[res/layout/fragment_bottom_sheet_planner_list_modify.xml]
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:paddingStart="10dp"
    android:paddingEnd="10dp"
    tools:context=".fragment.BottomSheetPlannerListModify" >

    <com.google.android.material.bottomsheet.BottomSheetDragHandleView
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

    <com.google.android.material.textfield.TextInputLayout
        android:id="@+id/textFieldBottomSheetPlannerModifyContent"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:hint="학사 일정"
        app:endIconMode="clear_text">

        <com.google.android.material.textfield.TextInputEditText
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:singleLine="true"
            android:text=" " />

    </com.google.android.material.textfield.TextInputLayout>

    <Button
        android:id="@+id/buttonBottomSheetPlannerModifyDate"
        style="@style/Widget.Material3.Button.OutlinedButton"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="10dp"
        android:text="날짜 선택" />
</LinearLayout>
```

### 수정 버튼을 눌렀을 때 BottomSheet가 나오도록 한다.

[fragment/PlannerListFragment.kt - showMenuBottomSheet()]
```kt
        bottomSheetMenuPlannerListBinding.buttonBottomSheetPlannerListModify.setOnClickListener {

            bottomSheetDialog.setOnDismissListener {
                // 수정을 위한 BottomSheet를 띄운다.
                val bottomSheetPlannerListModify =  BottomSheetPlannerListModify(this)
                bottomSheetPlannerListModify.show(mainActivity.supportFragmentManager, "list modify")
            }

            bottomSheetDialog.dismiss()
        }
```

### 삭제 버튼을 눌렀을 때 삭제 여부를 묻는 다이얼로그를 띄워준다.

[fragment/PlannerListFragment.kt - showMenuBottomSheet()]
```kt
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
```

### 달력을 보여주는 화면 구성을 위해 라이브러리를 셋팅한다.

- https://github.com/prolificinteractive/material-calendarview/tree/master

- settings.gradle.kts 파일에 Repository 정보를 작성해준다.

```kt
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        ...
        maven {
            url = uri("https://jitpack.io")
        }
    }
}

```

- build.gradle.kts에 라이브러리를 설정한다.

```kt
     implementation("com.github.prolificinteractive:material-calendarview:2.0.0")
```

- gradle.properties 에 다음 코드를 작성해준다.

```kt
android.enableJetifier=true
```

### 화면을 구성한다.

[res/layout/fragment_planner_calendar.xml]

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    tools:context=".fragment.PlannerCalendarFragment" >

    <com.prolificinteractive.materialcalendarview.MaterialCalendarView
        android:id="@+id/materialCalendarViewPlanner"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" >

    </com.prolificinteractive.materialcalendarview.MaterialCalendarView>

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerViewPlannerCalendar"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_marginTop="10dp" />
</LinearLayout>
```
