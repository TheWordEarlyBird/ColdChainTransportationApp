package com.example.coldchaintransportationapp;

/*import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
    }
}*/

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.coldchaintransportationapp.R;
import com.example.coldchaintransportationapp.unit.JsonData;
import com.example.coldchaintransportationapp.unit.PhotoUtils;
import com.example.coldchaintransportationapp.unit.ShangChuanClass;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CameraActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn_photo, select_photo,btn_void,select_void;
    private String url="http://192.168.43.50:8080/AONT/user/andtest";
    private ImageView headIv;
    private TextView textView;
    private static final String TAG = "MainActivity";
    private final int IMAGE_RESULT_CODE = 2;
    private final int PICK = 1;
    private final int VOICE_RESULT_CODE = 3;
    private String imgString = "";
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        btn_photo = findViewById(R.id.btn_photo);
        select_photo = findViewById(R.id.select_photo);
        btn_void = findViewById(R.id.btn_void);
        select_void = findViewById(R.id.select_void);

        btn_photo.setOnClickListener(this);
        select_photo.setOnClickListener(this);
        btn_void.setOnClickListener(this);
        select_void.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_photo:
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, PICK);
                break;
            case R.id.btn_void:
                intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(intent, PICK);

                break;
            case R.id.select_photo:
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, IMAGE_RESULT_CODE);
                break;
            case R.id.select_void:
                intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, VOICE_RESULT_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            // 表示 调用照相机拍照
            case PICK:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    imgString = bitmapToBase64(bitmap,"pick");
                    uploadImg();
                }
                break;
            // 选择图片库的图片
            case IMAGE_RESULT_CODE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String path = PhotoUtils.getPath(this,uri);
                    Log.d("path:",path);
                    Bitmap bitmap2 = PhotoUtils.getBitmapFromUri(uri, this);
                    imgString = bitmapToBase64(bitmap2,"select");
                    Log.d("imgString:",imgString);
                    uploadImg();
                }
                break;
            case VOICE_RESULT_CODE:
                if (resultCode == RESULT_OK){
                    Uri uri = data.getData();
                    String path = getRealPathFromURI(uri);
                    Log.d("path", "path==" + path);
                    ShangChuanClass sc = new ShangChuanClass();
                    String result = sc.ShangChuanClass(path,url);
                    Log.d("result",result);
                }
                break;
        }
    }

    /**
     * uri转path
     * @param contentUri  uri参数
     * @return
     */
    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    //上传图片文件的操作
    public void uploadImg() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        FormBody body = new FormBody.Builder().add("dir", "c/image")
                .add("data", imgString)
                .add("file", "headicon")
                .add("ext", "jpg").build();
        Request request = new Request.Builder().url(url).post(body).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("xyh",e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                parseJSONWithGSON(response.body().string());
            }
        });
    }
    private void parseJSONWithGSON(String jsonData) {
        //使用Jastjson
        JsonData res= JSON.parseObject(jsonData, JsonData.class);
        if(res==null){
            Looper.prepare();
            Toast.makeText(this,"图片上传失败",Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
        else{
            if(res.code==0){
                Looper.prepare();
                Toast.makeText(this,res.msg,Toast.LENGTH_SHORT).show();
                Looper.loop();
            }else{
                Looper.prepare();
                Toast.makeText(this,res.msg,Toast.LENGTH_SHORT).show();
                Looper.loop();
            }

        }
    }

    //如上传需要64位编码可调用此方法，不需要可以忽略
    public static String bitmapToBase64(Bitmap bitmap,String type) {

        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                int option = 100;
                if(type.equals("select")) {
                    option=20;
                }
                bitmap.compress(Bitmap.CompressFormat.JPEG, option, baos);
                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}