package com.example.gtec.sqlite_login;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterAccountActivity extends AppCompatActivity {
    SQLiteOpenHelper openHelper;
    SQLiteDatabase db;
    String useremail = "";
    int userid = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_account);

        openHelper = new SQLiteDBHelper(this);

        //Referencing EditText widgets and Button placed inside in xml layout file
        final EditText _txtfullname = (EditText) findViewById(R.id.txtname_reg);
        final EditText _txtemail = (EditText) findViewById(R.id.txtemail_reg);
        final EditText _txtpass = (EditText) findViewById(R.id.txtpass_reg);
        final EditText _txtmobile = (EditText) findViewById(R.id.txtmobile_reg);
        Button _btnreg = (Button) findViewById(R.id.btn_reg);
        Button _btndelete = (Button) findViewById(R.id.btn_delete);
        _btndelete.setVisibility(View.GONE);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            _btndelete.setVisibility(View.VISIBLE);
            useremail = bundle.getString("email");
            db = openHelper.getReadableDatabase();
            ContentValues values = ViewUser(useremail);
            _txtfullname.setText(values.getAsString("fullname"));
            _txtemail.setText(values.getAsString("email"));
            _txtpass.setText(values.getAsString("password"));
            _txtmobile.setText(values.getAsString("mobile"));
            userid = values.getAsInteger("userid");
            _btnreg.setText("Update Profile");
        }

        _btnreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = openHelper.getWritableDatabase();

                String fullname = _txtfullname.getText().toString();
                String email = _txtemail.getText().toString();
                String pass = _txtpass.getText().toString();
                String mobile = _txtmobile.getText().toString();

                if (userid == 0) {
                    //Calling InsertData Method - Defined below
                    InsertData(fullname, email, pass, mobile);

                    //Alert dialog after clicking the Register Account
                    final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterAccountActivity.this);
                    builder.setTitle("Information");
                    builder.setMessage("Your Account is Successfully Created.");

                    builder.setPositiveButton("Okey", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Finishing the dialog and removing Activity from stack.
                            dialogInterface.dismiss();
                            finish();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else if (userid > 0) {
                    UpdateProfile(fullname, email, pass, mobile, userid);

                    //Alert dialog after clicking the Register Account
                    final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterAccountActivity.this);
                    builder.setTitle("Information");
                    builder.setMessage("Your Account is Successfully Updated.");

                    builder.setPositiveButton("Okey", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //Finishing the dialog and removing Activity from stack.
                            dialogInterface.dismiss();
                            startActivity(new Intent(RegisterAccountActivity.this, MainActivity.class));
                            finish();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
        _btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(RegisterAccountActivity.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure to delete your account.");
                builder.setNegativeButton("No", null);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DeleteProfile(userid);
                        Toast.makeText(RegisterAccountActivity.this, "User deleted successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterAccountActivity.this, MainActivity.class));
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    //Inserting Data into database - Like INSERT INTO QUERY.
    public void InsertData(String fullName, String email, String password, String mobile) {
        try {
            ContentValues values = new ContentValues();
            values.put(SQLiteDBHelper.COLUMN_FULLNAME, fullName);
            values.put(SQLiteDBHelper.COLUMN_EMAIL, email);
            values.put(SQLiteDBHelper.COLUMN_PASSWORD, password);
            values.put(SQLiteDBHelper.COLUMN_MOBILE, mobile);
            long id = db.insert(SQLiteDBHelper.TABLE_NAME, null, values);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong", Toast.LENGTH_SHORT).show();

        }
    }

    public ContentValues ViewUser(String email) {
        ContentValues values = new ContentValues();
        try {
            Cursor data = db.rawQuery("select * from profile where email=?", new String[]{email});
            if (data.getCount() > 0) {
                data.moveToFirst();
                values.put(SQLiteDBHelper.COLUMN_ID, data.getString(data.getColumnIndex("userid")));
                values.put(SQLiteDBHelper.COLUMN_FULLNAME, data.getString(data.getColumnIndex("fullname")));
                values.put(SQLiteDBHelper.COLUMN_EMAIL, data.getString(data.getColumnIndex("email")));
                values.put(SQLiteDBHelper.COLUMN_PASSWORD, data.getString(data.getColumnIndex("password")));
                values.put(SQLiteDBHelper.COLUMN_MOBILE, data.getString(data.getColumnIndex("mobile")));
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong", Toast.LENGTH_SHORT).show();
        }
        return values;
    }

    public void UpdateProfile(String fullName, String email, String password, String mobile, int userid) {
        try {
            ContentValues values = new ContentValues();
            values.put(SQLiteDBHelper.COLUMN_FULLNAME, fullName);
            values.put(SQLiteDBHelper.COLUMN_EMAIL, email);
            values.put(SQLiteDBHelper.COLUMN_PASSWORD, password);
            values.put(SQLiteDBHelper.COLUMN_MOBILE, mobile);
            db.update(SQLiteDBHelper.TABLE_NAME, values, "userid=" + userid, null);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong", Toast.LENGTH_SHORT).show();
        }
    }

    public void DeleteProfile(int userid) {
        try {
            db.delete(SQLiteDBHelper.TABLE_NAME, "userid=" + userid, null);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Something wrong", Toast.LENGTH_SHORT).show();
        }
    }
}
