package com.example.vudang.bluetooth1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class dang_nhap extends AppCompatActivity {

    EditText Tendangnhap, Matkhau;
    Button Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);

        Tendangnhap = (EditText)findViewById(R.id.edt_username);
        Matkhau = (EditText)findViewById(R.id.edt_password);
        Login = (Button) findViewById(R.id.btn_login);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = "a";
                String password = "a";
                if (Tendangnhap.getText().toString().equals(username) && Matkhau.getText().toString().equals(password))
                {
                    Toast.makeText(getApplicationContext(),"Đăng nhập thành công",Toast.LENGTH_LONG).show();
                    Intent mh2 = new Intent(dang_nhap.this, MainActivity.class);
                    startActivity(mh2);
                }
                else
                    Toast.makeText(getApplicationContext(),"Tài khoản hoặc Mật khẩu sai",Toast.LENGTH_LONG).show();

            }
        });
    }
}
