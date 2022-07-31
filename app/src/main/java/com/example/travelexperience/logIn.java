package com.example.travelexperience;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.DialogInterface;
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
import com.google.firebase.auth.FirebaseUser;

public class logIn extends AppCompatActivity {

    private EditText email,pass;

    public static FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        mAuth = FirebaseAuth.getInstance();

        email = findViewById(R.id.LIEmail);
        pass = findViewById(R.id.LIPass);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if(firebaseUser != null){
            String trimEmail;
            trimEmail = firebaseUser.getEmail().replace("@","");
            trimEmail = trimEmail.replace(".","");

            Intent intent = new Intent(logIn.this, dataSet.class);
            intent.putExtra("mail",trimEmail);
            intent.putExtra("activity",-1);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialogExit = new AlertDialog.Builder(logIn.this);
        dialogExit.setTitle("EXIT!!");
        dialogExit.setMessage("Are you sure?");

        dialogExit.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
                finish();
            }
        });

        dialogExit.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogExit.show();
    }
    public void logIn(View view) {

        String mail = email.getText().toString().trim();
        String password = pass.getText().toString().trim();

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

        mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    String trimEmail;
                    trimEmail = mail.replace("@","");
                    trimEmail = trimEmail.replace(".","");

                    Intent intent=new Intent(logIn.this, dataSet.class);
                    intent.putExtra("mail",trimEmail);
                    intent.putExtra("activity",-1);
                    startActivity(intent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e.getMessage() == "The email address is badly formatted."){
                    email.setError("Not valid");
                    email.requestFocus();
                }
                else if(e.getMessage() == "The password is invalid or the user does not have a password."){
                    pass.setError("Wrong password");
                    pass.requestFocus();
                }
                else{
                    Toast.makeText(logIn.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void forgetPass(View view){
        startActivity(new Intent(logIn.this, forgetPass.class));
    }
    public void signUp(View view){
        startActivity(new Intent(logIn.this, signUp.class));
    }
}