package com.example.startactitivity.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.startactitivity.HomeActivity
import com.example.startactitivity.SignInActivity
import com.example.startactitivity.User
import com.example.startactitivity.UserDatabase
import com.example.startactitivity.databinding.ActivitySignupBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    /** [0] 숙련 주차에 와서 처음 배운 바인딩을 이용해서 코드를 작성하였다. **/
    private lateinit var binding: ActivitySignupBinding

    private var accessPath: Int = 0

    private var checkName: String? = ""
    private var checkId: String? = ""
    private var checkPassword: String? = ""

    val editTextArray by lazy {
        arrayOf(binding.etName, binding.etId, binding.etPw)
    }

    /** [1] 가장 먼저 액티비티 클래스와 뷰모델 클래스를 연결해 주어야 한다. **/
    private val viewModel by lazy {
        ViewModelProvider(this@SignUpActivity)[SignUpViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initView()
        /** [3] 뷰모델 초기 함수를 만들어준다. **/
        initViewModel()
    }

    private fun initView() {
        if (intent.hasExtra("edit")) {
            binding.btnSignup.text = "회원 수정"
            accessPath = 1
        }
        /** [5] 가장 먼저 initView 함수가 실행 될것이거 그에 따라 아래 3개의 함수가 실행될 것이다. **/
        setTextChangedListener()
        setOnFocusChangedListener()
        onClickBtnSignUp()
    }

    /** [4] 이 함수도 = with(viewModel)을 적어주어야 한다.
     *      이 함수에서 뷰모델을 관찰할거라고 말해주는 부분인 것 같다.**/
    private fun initViewModel() = with(viewModel) {
        /** [8] 어느 EditText에 입력값이 변경되었는지 전달 해 주었다.
         *      이제 이 새로 입력되거나 지워진 값이 로그인 유효성 검사를 해주기 위해 setErrorMessage() 함수를 호출한다.
         *      그리고 어느 EditText인지도 it을 통해 전달한다.
         *      추가적으로 입력 정보가 모두 유효하면 로그인 버튼이 활성화되어야 하기 때문에
         *      setConfrimButtenEnable() 함수도 실행해 준다.**/
        editTextTextChange.observe(this@SignUpActivity) {
            setErrorMessage(it)
            setConfirmButtonEnable()
        }
        /** [12] SignUpErrorMessage 타입에서 문자열로 바꾸어준다.
         *       EditText의 type과 그 문자열을  getNullIfValid() 함수를 호출하여 전달한다.**/
        nameErrorMessage.observe(this@SignUpActivity) {
            getNullIfValid("name", getString(it.message))
        }
        idErrorMessage.observe(this@SignUpActivity) {
            getNullIfValid("id", getString(it.message))
        }
        passwordErrorMessage.observe(this@SignUpActivity) {
            getNullIfValid("password", getString(it.message))
        }

        /** [15] checkName에 저장되어있던 값을 EditText가 유효하지 않으면 띄울 수 있게 .error을 사용해준다.
         *       그리고 나중에 이름,비밀번호, 아이디가 유효할 때 로그인 버튼을 활성화 해주기 위해 SignUpActivity에
         *       있는 checkName 변수가 이름 에딧텍스트가 유효한 경우 null값을 가지게 해준다.**/
        checkName.observe(this@SignUpActivity) {
            binding.etName.error = it
            this@SignUpActivity.checkName = it

        }
        checkId.observe(this@SignUpActivity) {
            binding.etId.error = it
            this@SignUpActivity.checkId = it
        }
        checkPassword.observe(this@SignUpActivity) {
            binding.etPw.error = it
            this@SignUpActivity.checkPassword = it
        }
        enableConfrimButton.observe(this@SignUpActivity) {
            binding.btnSignup.isEnabled = it
        }
        /** [18] [8]번과 동일**/
        editTextOnFocus.observe(this@SignUpActivity) {
            setErrorMessage(it)
            setConfirmButtonEnable()
        }


    }


    private fun onClickBtnSignUp() {
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    intent.putExtra("id", binding.etId.text.toString())
                    intent.putExtra("password", binding.etPw.text.toString())
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }


        binding.btnSignup.setOnClickListener {


            when (accessPath) {
                0 -> {

                    UserDatabase.addUser(
                        User(
                            binding.etName.text.toString(),
                            binding.etId.text.toString(),
                            binding.etPw.text.toString()
                        )
                    )

                    val intent = Intent(this, SignInActivity::class.java).apply {
                        putExtra("id", binding.etId.text.toString())
                        putExtra("password", binding.etPw.text.toString())
                    }
                    setResult(RESULT_OK, intent)
                    resultLauncher.launch(intent)
                }

                1 -> {

                    UserDatabase.editUserData(
                        User(
                            binding.etName.text.toString(),
                            binding.etId.text.toString(),
                            binding.etPw.text.toString()
                        )
                    )

                    val intent = Intent(this, HomeActivity::class.java).apply {
                        putExtra("id", binding.etId.text.toString())
                        putExtra("password", binding.etPw.text.toString())
                    }
                    setResult(RESULT_OK, intent)
                    resultLauncher.launch(intent)
                }
            }


        }
    }

    /** [6] setTextChangedListener은 말 그대로 입력된 값이 변할 때마다 호출 되는 함수이다.
     *      함수가 실행 되면 뷰모델에 있는 같은 이름의 함수를 호출한다.
     *      이때 editTextArray를 넘겨준다.
     *      EditTextArray는 이름/아이디/비밀번호에 입력된 값을 모두 가지고 있다.**/
    private fun setTextChangedListener() {
        viewModel.setTextChangedListener(editTextArray)
    }
    /** [16] setOnFocusChangedListner은 EditText가 활성화 되어있는지 확인해주는 함수이다.
     *       setTextChangedListener와 마찬가지로 editTextArray를 넘겨준다.**/
    private fun setOnFocusChangedListener() {
        viewModel.setOnFocusChangedListener(editTextArray)

    }
    /** [10] 뷰모델에 있는 이름 유효성 검사 로직을 가진 함수를 호출해준다.
     *       그리고 EditText에 적혀있는 입력값을 문자열로 바꾸어 전달한다.**/
    private fun getMessageValidName() {
        viewModel.setMessageValidName(binding.etName.text.toString())
    }

    private fun getMessageValidId() {
        viewModel.setMessageValidId(binding.etId.text.toString())
    }

    private fun getMessageValidPassword() {
        viewModel.setMessageValidPassword(binding.etPw.text.toString())
    }
    /** [13] 받은 값 그대로 뷰모델에 있는 setNullIfValid 함수로 바로 전해준다.
     *       editText에 Error 함수를 사용하여 유효성 검사를 하고 싶은데 유효한 경우가 현재 Null이 아니고 empty String이어서
     *       empty string을 null로 바꾸어주는 함수이다.**/
    private fun getNullIfValid(type: String, message: String) {
        viewModel.setNullIfValid(type, message)
    }

    private fun setConfirmButtonEnable() {
        viewModel.enableConfirmButton(checkName, checkId, checkPassword)
    }

    /** [9] 어떤 EditText가 전달되었느냐에 따라 이름, 아이디 그리고 비밀번호의 유효성검사를 해준다.
     * 이름 유효성 검사로 가보자!**/
    private fun setErrorMessage(type: String?) {
        when (type) {
            "name" -> getMessageValidName()
            "id" -> getMessageValidId()
            "password" -> getMessageValidPassword()
        }
    }
}
