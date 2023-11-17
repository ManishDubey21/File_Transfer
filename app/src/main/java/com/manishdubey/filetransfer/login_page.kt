package com.manishdubey.filetransfer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.manishdubey.filetransfer.databinding.ActivityLoginPageBinding
import com.manishdubey.filetransfer.databinding.ActivitySignUpPageBinding

class login_page : AppCompatActivity() {

    private lateinit var binding: ActivityLoginPageBinding
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()


        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase.reference.child("users")

        binding.loginBtn.setOnClickListener {
            val password=binding.password.text.toString()
            val username=binding.userName.text.toString()

            if(password.isNotEmpty() &&  username.isNotEmpty())
            {
                loginPage(username,password)
            }else
            {
                Toast.makeText(this@login_page,"Please fill in all the fields",Toast.LENGTH_SHORT).show()
            }
        }
        binding.signUpText.setOnClickListener{
            startActivity(Intent(this@login_page,SignUp_Page::class.java))
            finish()
        }

    }
    private fun loginPage(username: String,password: String){
        databaseReference.orderByChild("username").equalTo(username).addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for (userSnapshot in dataSnapshot.children){
                        val userdata=userSnapshot.getValue(user_data::class.java)

                        if(userdata!=null && userdata.password==password)
                        {
                            Toast.makeText(this@login_page,"Login Successfully",Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@login_page,MainActivity::class.java))
                            finish()
                            return
                        }
                    }
                }
                Toast.makeText(this@login_page,"Login Failed",Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(this@login_page,"Database Error: ${databaseError.message}",Toast.LENGTH_SHORT).show()
            }
        })
    }
}