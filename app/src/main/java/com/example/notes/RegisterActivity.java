package com.example.notes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.Arrays;

public class RegisterActivity extends AppCompatActivity {
    private Button btnReg;
    private TextInputLayout inName, inEmail, inPass;

    private FirebaseAuth fAuth;
    private DatabaseReference fUsersDatabase;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnReg = (Button) findViewById(R.id.btn_reg);
        inName = (TextInputLayout) findViewById(R.id.input_reg_name);
        inEmail = (TextInputLayout) findViewById(R.id.input_reg_email);
        inPass = (TextInputLayout) findViewById(R.id.input_reg_pass);

        fAuth = FirebaseAuth.getInstance();
        fUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uname = inName.getEditText().getText().toString().trim();
                String uemail = inEmail.getEditText().getText().toString().trim();
                String upass = inPass.getEditText().getText().toString().trim();

              if(uname.isEmpty() || uemail.isEmpty() || upass.isEmpty()){
                  checknullinput(uname,uemail,upass);

              }else {
                  registerUser(uname, uemail, upass);
              }
              //  registerUser(uname, uemail, upass);
            }
        });
    }


    private void checknullinput(final String name,String email, String pass){

        if(name.isEmpty()){
            Log.e("test input "," uname done check");
            inName.setHint(" Username cannot be empty.");

        }

        if(email.equals("")){
            Log.e("test input "," uname done check");
            inEmail.setHint(" Email cannot be empty.");

        }
        if(pass.equals("")){
            Log.e("test input "," uname done check");
            inPass.setHint(" Password cannot be empty.");

        }

    }


    private void registerUser(final String name, String email, String password){

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing your request, please wait...");

        progressDialog.show();

        fAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){

                            fUsersDatabase.child(fAuth.getCurrentUser().getUid())
                                    .child("basic").child("name").setValue(name)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (task.isSuccessful()){

                                                progressDialog.dismiss();

                                                Intent mainIntent = new Intent(RegisterActivity.this, Main.class);
                                                startActivity(mainIntent);
                                                finish();
                                                Toast.makeText(RegisterActivity.this, "User created!", Toast.LENGTH_SHORT).show();

                                            } else {
                                                progressDialog.dismiss();
                                                Toast.makeText(RegisterActivity.this, "ERROR : " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });

                        } else {

                            progressDialog.dismiss();

                            Toast.makeText(RegisterActivity.this, "ERROR: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }
}
