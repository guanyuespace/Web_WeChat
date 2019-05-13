package service.impl;

import com.alibaba.fastjson.JSON;
import info.TuRingRequest;
import info.TuRingRequestEncrypted;
import info.TuRingResponse;
import info.base.Perception;
import info.base.UserInfo;
import service.IMessageProcess;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * 图灵机器人测试
 */
public class MessageProcess implements IMessageProcess {
    @Override
    public String processText(String text) {
        HttpURLConnection urlConnection ;
        try {
            urlConnection = (HttpURLConnection) new URL("http://openapi.tuling123.com/openapi/api/v2").openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            urlConnection.setRequestProperty("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/71.0.3578.98 Safari/537.36");
            urlConnection.setDoOutput(true);
            urlConnection.setConnectTimeout(500);

            urlConnection.connect();

            //明文数据请求构造
            TuRingRequest tuRingRequest = new TuRingRequest(0);
            tuRingRequest.setUserInfo(new UserInfo());
            tuRingRequest.setPerception(new Perception(new Perception.InputText("你在哪？"), new Perception.SelfInfo(new Perception.SelfInfo.Location("深圳"))));

            //数据加密
            TuRingRequestEncrypted tuRingRequestEncrypted = new TuRingRequestEncrypted(tuRingRequest);
            String paramStr = JSON.toJSONString(tuRingRequestEncrypted);
            System.out.println("paramStr: " + paramStr);

            OutputStream outputStream = urlConnection.getOutputStream();
            outputStream.write(paramStr.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            outputStream.close();

            InputStream inputStream = urlConnection.getInputStream();
            byte[] buffer = new byte[512];
            int n ;
            StringBuilder stringBuilder = new StringBuilder();
            while ((n = inputStream.read(buffer)) != -1) {
                stringBuilder.append(new String(buffer, 0, n, StandardCharsets.UTF_8));
            }
            System.out.println(stringBuilder.toString());
            //{"intent":{"actionName":"","code":10034,"intentName":""},"results":[{"groupType":1,"resultType":"text","values":{"text":"我在你的心里，进来就不想离去。"}}]}} catch (MalformedURLException e) {
            TuRingResponse tuRingResponse = JSON.parseObject(stringBuilder.toString(), TuRingResponse.class);
            System.out.println(tuRingResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }
}
