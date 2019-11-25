package com.example.coldchaintransportationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coldchaintransportationapp.model.AndroidLogin;
import com.example.coldchaintransportationapp.model.MyApplication;
import com.example.coldchaintransportationapp.unit.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    Button b1;
    EditText eu;
    EditText ep;
    String us;
    String ps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        b1=(Button)findViewById(R.id.btn_login);
        eu=(EditText)findViewById(R.id.user);
        ep=(EditText)findViewById(R.id.password);

        b1.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {
                String url=view.getContext().getString( R.string.url )+"chauffeur/login";
               /* Map<String, String> map = new HashMap<String, String>();
                map.put("userid", "admin");
                map.put("pwd", "123456");
                OkhttpUtils http=new OkhttpUtils();
                http.postDataAsyn(url,map,new OkhttpUtils.MyNetCall()*/

                TextView userid=findViewById(R.id.user);
                TextView pwd=findViewById(R.id.password);
                Map<String, String> map = new HashMap<String, String>();
                map.put("userid", userid.getText().toString());
                map.put("pwd", pwd.getText().toString());
                OkhttpUtils http=new OkhttpUtils();
                http.postDataAsyn(url,map,new OkhttpUtils.MyNetCall()

                {

                    @Override
                    public void success(Call call, Response response) throws IOException {
                        final String res = response.body().string();
                        showResponse(res);
                    }

                    @Override
                    public void failed(Call call, IOException e) {
                        showResponse(e.toString());
                    }
                });

            }
        });

    }

    private void showResponse(final String response) {
        runOnUiThread( new Runnable() {
            @Override
            public void run() {

                /*查看response取到什么*/
                /* Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();*/
                Log.d("response",response);
               /* try {*/

                    AndroidLogin loginData= JacksonUtil.deserialize(response, AndroidLogin.class);
                    boolean bl=loginData.getSuccess();
                    if(bl){
                        MyApplication app = (MyApplication)getApplication();
                        app.setUserid(loginData.getUserid());
                        //Toast.makeText(login.this, "登录成功", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "账号或密码错误！", Toast.LENGTH_SHORT).show();
                    }

               /* }catch(Exception e){
                    Toast.makeText(LoginActivity.this, "连接出错！", Toast.LENGTH_SHORT).show();
                }*/

            }
        } );

    }


}
