package com.example.gtec.sqlite_login;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class LoginSuccessActivity extends AppCompatActivity {
    private static final int SELECT_PHOTO = 100;
    ImageView dpImage;
    String useremail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);

        TextView txtname = (TextView) findViewById(R.id.txt_success_name);
        TextView txtemail = (TextView) findViewById(R.id.txt_success_email);
        Button _btnlogout = (Button) findViewById(R.id.btn_logout);


        final Intent intent = getIntent();

        String loginName = intent.getStringExtra("fullname");
        String loginEmail = intent.getStringExtra("email");
        useremail = intent.getStringExtra("email");
        txtname.setText("Welcome, " + loginName);
        txtemail.setText(loginEmail);

        _btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(LoginSuccessActivity.this);
                builder.setTitle("Info");
                builder.setMessage("Do you want to logout ??");
                builder.setPositiveButton("Take me away!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(LoginSuccessActivity.this, MainActivity.class);
                        startActivity(intent);

                        finish();
                    }
                });
                builder.setNegativeButton("Now Now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //=========Section For Changing Display Image When Click=========
        //dpImage.setOnClickListener(new View.OnClickListener() {
        //  @Override
        //  public void onClick(View view) {
        //     Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        //     photoPickerIntent.setType("image/*");
        //     startActivityForResult(photoPickerIntent, SELECT_PHOTO);
        // }
        //});
        Button _editaccount = (Button) findViewById(R.id.btn_edit_account);
        _editaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_account = new Intent(LoginSuccessActivity.this, RegisterAccountActivity.class);
                Bundle bundle =new Bundle();
                bundle.putString("email", useremail);
                intent_account.putExtras(bundle);
                startActivity(intent_account);
                finish();
            }
        });
    }
}
