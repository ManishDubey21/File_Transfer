package com.manishdubey.filetransfer

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.UserData
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.initialize
import com.manishdubey.filetransfer.databinding.ActivitySignUpPageBinding

class SignUp_Page : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpPageBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        binding = ActivitySignUpPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        binding.sgnBtn.setOnClickListener {
            val email=binding.emailId.text.toString()
            val password=binding.password.text.toString()
            val confirmPassword=binding.confirmPassword.text.toString()
            val username=binding.userName.text.toString()

            if(email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty() && username.isNotEmpty())
            {
                if(password==confirmPassword){
                    signupUser(username,password,email,confirmPassword)
                }else{
                    Toast.makeText(this@SignUp_Page,"Password doesn't Match",Toast.LENGTH_SHORT).show()
                }
            }else
            {
                Toast.makeText(this@SignUp_Page,"Please fill in all the fields",Toast.LENGTH_SHORT).show()
            }
        }
        binding.loginText.setOnClickListener {
            startActivity(Intent(this@SignUp_Page,login_page::class.java))
            finish()
        }


    }
    private fun signupUser(username: String,password: String,email: String,cp: String)
    {
            databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (!dataSnapshot.exists()){
                        val userdata=user_data(username,password,email,cp)
                        databaseReference.child(email).setValue(userdata)
                        Toast.makeText(this@SignUp_Page, "Sign Up Successfully Completed", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@SignUp_Page,MainActivity::class.java))
                        finish()
                    }else{
                        Toast.makeText(this@SignUp_Page, "User already Exists", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@SignUp_Page, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })

    }
}