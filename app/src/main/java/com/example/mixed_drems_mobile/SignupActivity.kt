package com.example.mixed_drems_mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.mixed_drems_mobile.api.RetrofitHelper
import com.example.mixed_drems_mobile.api.auth.AuthServiceImpl
import com.example.mixed_drems_mobile.api.auth.IAuthService
import com.example.mixed_drems_mobile.api.auth.CustomerRegisterRequest
import com.example.mixed_drems_mobile.databinding.ActivitySignupBinding
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var IAuthService: IAuthService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        IAuthService =
            AuthServiceImpl()//RetrofitHelper.getInstance().create(IAuthService::class.java)

        binding.signupBtn.setOnClickListener {
            register()
        }
        binding.loginRedirect.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

    private fun register() {
        val firstName = binding.firstName.text.toString()
        val lastName = binding.lastName.text.toString()
        val email = binding.email.text.toString()
        val password = binding.password.text.toString()
        val confirmPassword = binding.confirmPassword.text.toString()
        if (email.isNotEmpty() && password.isNotEmpty() &&
            confirmPassword.isNotEmpty() && firstName.isNotEmpty() &&
            lastName.isNotEmpty()
        ) {
            if (password == confirmPassword) {
                lifecycleScope.launch {
                    val data = CustomerRegisterRequest(firstName, lastName, email, password)
                    val result = IAuthService.register(data)
                    if (result.isSuccess) {
                        println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOKKKKKKKKKKKKKKKKKKKKKKKKKK")
                    } else {
                        Toast
                            .makeText(this@SignupActivity, result.error?.title, Toast.LENGTH_SHORT)
                            .show()
                    }
                    print(result)
                }
            } else {
                Toast.makeText(this, "Password does not matched", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }
}