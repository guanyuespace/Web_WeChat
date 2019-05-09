package service;

import com.alibaba.fastjson.JSON;
import info.LoginInfo;
import info.SyncCheckRes;
import info.SyncInfo;
import info.base.MessageInfo;
import org.apache.http.util.EntityUtils;
import util.MyHttpClient;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 消息收发处理
 */
public class MessageTools {
    private static MyHttpClient httpClient = MyHttpClient.getINSTANCE();

    public static void start() {
        new Thread(() -> {
            while (LoginInfo.isLogin.get()) {
                try {
                    SyncCheckRes syncCheckRes = syncCheck();
                    if (syncCheckRes.getRetcode().equals("0") && syncCheckRes.getSelector().equals("2"))
                        webWxSync();
                    if (syncCheckRes.getRetcode().equals("1101"))//其它地方登陆
                        break;
                    if (syncCheckRes.getRetcode().equals("1102"))//移动端退出
                        break;
                    if (syncCheckRes.getRetcode().equals("9999"))//UNKNOW
                        continue;
                    Thread.sleep(500);
//                    if(....)
//                    LoginInfo.isLogin.set(false);
                } catch (InterruptedException e) {
                    System.out.println("Exception:" + e.getMessage());
                }
            }
        }).start();

    }

    private static void webWxSync() {
        String res = "";
        String url = "";
        try {
            url = "https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxsync?sid=" + LoginInfo.status.getWxsid() + "&skey=" + LoginInfo.status.getSkey();

            String param = "{\"BaseRequest\":{\"Uin\":" + LoginInfo.status.getWxuin() + ",\"Sid\":\"" + LoginInfo.status.getWxsid() + "\"," +
                    "\"Skey\":\"" + LoginInfo.status.getSkey() + "\",\"DeviceID\":\"e" + LoginInfo.getRand(15) + "\"}," +
                    "\"SyncKey\":" + JSON.toJSONString(LoginInfo.initInfo.getSyncKey()) + ",\"rr\":" + LoginInfo.getRand(10) + "}";
            res = EntityUtils.toString(httpClient.doPost(url, null, param, null).getEntity());
        } catch (Exception e) {
            System.out.println("EXCEPTION-URL:  " + url);
            System.out.println("EXCEPTION-POST: " + JSON.toJSONString(LoginInfo.initInfo.getSyncKey()));
            e.printStackTrace();
        }
        SyncInfo syncInfo = JSON.parseObject(res, SyncInfo.class);
        if (syncInfo.getAddMsgCount() > 0) {
            MessageInfo messageInfo = syncInfo.getAddMsgList().get(0);
            if (messageInfo.getMsgType() == 1 && messageInfo.getContent() != "") {
                String Content = messageInfo.getContent();
                String toUserName = messageInfo.getFromUserName();
                String fromUserName = messageInfo.getToUserName();
                webWxSendMsg(Content, fromUserName, toUserName);
            }
        }
    }

    private static void webWxSendMsg(String content, String fromUserName, String toUserName) {
        try {
            long currernt = System.currentTimeMillis() * 10000;
            //        1557400802185
            //        15574008017360241
            String url = "https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxsendmsg";
            String param = "{\"BaseRequest\":{\"Uin\":" + LoginInfo.status.getWxuin() + ",\"Sid\":\"" + LoginInfo.status.getWxsid() + "\"," +
                    "\"Skey\":\"" + LoginInfo.status.getSkey() + "\",\"DeviceID\":\"e" + LoginInfo.getRand(15) + "\"}," +
                    "\"Msg\":{\"Type\":1,\"Content\":\"" + content + "\",\"FromUserName\":\"" + fromUserName + "\",\"ToUserName\":\"" + toUserName + "\"," +
                    "\"LocalID\":\"" + currernt + "\",\"ClientMsgId\":\"" + currernt + "\"},\"Scene\":0}";
            String res = EntityUtils.toString(httpClient.doPost(url, null, param, null).getEntity());
            System.out.println(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static SyncCheckRes syncCheck() {
        String res = "";
        String url = "";
        try {
            url = "https://webpush.wx2.qq.com/cgi-bin/mmwebwx-bin/synccheck?r=" + System.currentTimeMillis() + "&skey=" + LoginInfo.initInfo.getSKey()
                    + "&sid=" + LoginInfo.status.getWxsid() + "&uin=" + LoginInfo.status.getWxuin() + "&deviceid=e" + LoginInfo.getRand(15)
                    + "&synckey=" + URLEncoder.encode(LoginInfo.initInfo.getSyncKeyStr(), "utf-8") + "&_=" + LoginInfo.getLoginTime();
            res = EntityUtils.toString(httpClient.doGet(url, false).getEntity());
        } catch (Exception e) {
            System.out.println("EXCEPTION-URL:  " + url);
            System.out.println("EXCEPTION-GET:  " + LoginInfo.initInfo.getSyncKeyStr());
            e.printStackTrace();
        }
        //window.synccheck={retcode:"0",selector:"2"}
        Pattern pattern = Pattern.compile("window.synccheck=\\{retcode:\"(\\d+)\",selector:\"(\\d+)\"\\}");
        System.out.println(res);
        Matcher matcher = pattern.matcher(res);
        if (matcher.find()) {
            return new SyncCheckRes(matcher.group(1), matcher.group(2));
        }
        return null;
    }
}
