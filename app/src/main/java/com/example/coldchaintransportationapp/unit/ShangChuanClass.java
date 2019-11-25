package com.example.coldchaintransportationapp.unit;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ShangChuanClass {
    private final String BOUNDARYSTR = "--------aifudao7816510d1hq";
    private final String END = "\r\n";
    private final String LAST = "--";
    /**
     * @param path 附件本地地址
     * @param url  附件存储的服务器地址
     */
    public String ShangChuanClass(String path, String url) {
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) httpUrl.openConnection();
            connection.setRequestMethod("POST");
            //必须为post connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-type", "multipart/form-data;boundary=" + BOUNDARYSTR);
            //固定格式
            DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
            StringBuffer sb = new StringBuffer();
            /**
             * 写入文本数据
             * */
            sb.append(LAST + BOUNDARYSTR + END);
            sb.append("Content-Disposition: form-data; name=\"data\"" + END + END);
            sb.append(path + END);
            /**
             * 循环写入文件
             *
             **/
            String[] split = path.split("\\.");
            String s = split[1];
            Log.i("weicypath","weikai"+s);

            sb.append(LAST + BOUNDARYSTR + END);
            sb.append("Content-Disposition:form-data;Content-Type:application/octet-stream;name=\"file\";");
            //判断传入的格式
            if (s.equals("jpg")){
                sb.append("filename=\""+"map_image."+s+"\""+END+END);
                Log.i("weicypath","jpg");
            }else {
                sb.append("filename=\""+"map_image."+s+"\""+END+END);
                Log.i("weicypath","mp4");
            }
            // sb.append("filename=\""+"map_image.jpg"+"\""+END+END);
            dos.write(sb.toString().getBytes("utf-8"));
            FileInputStream fis = new FileInputStream(path);
            if (fis != null) {
                byte[] b = new byte[1024];
                int len;
                while ((len = fis.read(b)) != -1) {
                    dos.write(b, 0, len);
                }
                dos.write(END.getBytes());
            }
            dos.write((LAST + BOUNDARYSTR + LAST + END).getBytes());
            dos.flush();
            sb = new StringBuffer();
            if (connection.getResponseCode() == 200) {
                //请求成功
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                Log.i("weicypath", "成功");
            } else {
                Log.i("weicypath", "失败");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "hhh";
    }
}
