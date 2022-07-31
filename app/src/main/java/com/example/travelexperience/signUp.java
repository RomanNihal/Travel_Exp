package com.example.travelexperience;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signUp extends AppCompatActivity {

    private EditText email,pass,confirmPass;

    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("user");

        email = findViewById(R.id.SUEmail);
        pass = findViewById(R.id.SUPass);
        confirmPass = findViewById(R.id.SUConfirmPass);
    }

    public void createAccount(View view) {

        String mail = email.getText().toString().trim();
        String password = pass.getText().toString().trim();
        String confirmPassword = confirmPass.getText().toString().trim();

        if(mail.isEmpty()){
            email.setError("You can't leave blank");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches()){
            email.setError("Enter valid Email ID");
            email.requestFocus();
            return;
        }
        if(password.isEmpty()){
            pass.setError("You can't leave blank");
            pass.requestFocus();
            return;
        }
        if(password.length()<6){
            pass.setError("Enter at least 6 digit password");
            pass.setText("");
            pass.requestFocus();
            return;
        }

        if(confirmPassword.isEmpty()){
            confirmPass.setError("You can't leave blank");
            confirmPass.requestFocus();
            return;
        }
        if(!confirmPassword.equals(password)){
            confirmPass.setText("");
            confirmPass.setError("Didn't match");
            confirmPass.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                mAuth.signOut();

                String trimEmail;
                trimEmail = mail.replace("@","");
                trimEmail = trimEmail.replace(".","");

                databaseReference.push().setValue(trimEmail);
                    Toast.makeText(signUp.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(signUp.this,logIn.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(signUp.this,e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void signIn(View view) {
        startActivity(new Intent(signUp.this,logIn.class));
        finish();
    }

}