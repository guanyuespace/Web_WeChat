package service;

import com.alibaba.fastjson.JSON;
import info.LoginInfo;
import info.SyncCheckRes;
import info.SyncInfo;
import info.base.MessageInfo;
import org.apache.http.util.EntityUtils;
import util.MyHttpClient;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 消息收发处理
 */
public class MessageTools {
    private static MyHttpClient httpClient = MyHttpClient.getINSTANCE();

    public static void start() {
        new Thread(() -> {
            while (LoginInfo.isLogin) {
                try {
                    SyncCheckRes syncCheckRes = syncCheck();
                    if (!syncCheckRes.getRetcode().equals("0"))
                        continue;
                    if (syncCheckRes.getSelector().equals("2"))
                        webWxSync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private static void webWxSync() throws IOException {
        String url = "https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxsync?sid=" + LoginInfo.status.getWxsid() + "&skey=" + LoginInfo.status.getSkey();

        String param = "{\"BaseRequest\":{\"Uin\":" + LoginInfo.status.getWxuin() + ",\"Sid\":\"" + LoginInfo.status.getWxsid() + "\"," +
                "\"Skey\":\"" + LoginInfo.status.getSkey() + "\",\"DeviceID\":\"e" + LoginInfo.getRand(15) + "\"}," +
                "\"SyncKey\":" + JSON.toJSONString(LoginInfo.initInfo.getSyncKey()) + ",\"rr\":" + LoginInfo.getRand(10) + "}";

        String res = EntityUtils.toString(httpClient.doPost(url, null, param, null).getEntity());
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

    private static void webWxSendMsg(String content, String fromUserName, String toUserName) throws IOException {
        long currernt = System.currentTimeMillis();//1557392913498
        //        15573811355850869
        String url = "https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxsendmsg";
        String param = "{\"BaseRequest\":{\"Uin\":" + LoginInfo.status.getWxuin() + ",\"Sid\":\"" + LoginInfo.status.getWxsid() + "\"," +
                "\"Skey\":\"" + LoginInfo.status.getSkey() + "\",\"DeviceID\":\"e" + LoginInfo.getRand(15) + "\"}," +
                "\"Msg\":{\"Type\":1,\"Content\":\"" + content + "\",\"FromUserName\":\"" + fromUserName + "\",\"ToUserName\":\"" + toUserName + "\"," +
                "\"LocalID\":\"" + currernt + "\",\"ClientMsgId\":\"" + currernt + "\"},\"Scene\":0}";
        String res = EntityUtils.toString(httpClient.doPost(url, null, param, null).getEntity());
        System.out.println(res);
    }

    private static SyncCheckRes syncCheck() throws IOException {
        String url = "https://webpush.wx2.qq.com/cgi-bin/mmwebwx-bin/synccheck?r=" + System.currentTimeMillis() + "&skey=" + LoginInfo.initInfo.getSKey()
                + "&sid=" + LoginInfo.status.getWxsid() + "&uin=" + LoginInfo.status.getWxuin() + "&deviceid=e" + LoginInfo.getRand(15)
                + "&synckey=" + LoginInfo.initInfo.getSyncKeyStr() + "&_=" + LoginInfo.getLoginTime();

        String res = EntityUtils.toString(httpClient.doGet(url, false).getEntity());
        System.out.println(res);
        //window.synccheck={retcode:"0",selector:"2"}
        Pattern pattern = Pattern.compile("window.synccheck=\\{retcode:\"(\\d)\",selector:\"(\\d)\"\\}");
        Matcher matcher = pattern.matcher(res);
        if (matcher.find()) {
            return new SyncCheckRes(matcher.group(1), matcher.group(2));
        }
        return null;
    }
}
