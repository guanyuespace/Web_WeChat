package service;

import com.alibaba.fastjson.JSON;
import info.LoginInfo;
import info.SyncCheckRes;
import info.SyncInfo;
import info.base.MessageInfo;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import util.MyHttpClient;

import java.net.URLEncoder;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 消息收发处理逻辑
 */
public class MessageTools {
    private static Logger LOG = Logger.getLogger(MyHttpClient.class.getName());
    private static MyHttpClient httpClient = MyHttpClient.getINSTANCE();

    static void start() {
        new Thread(() -> {
            while (LoginInfo.isLogin.get()) {
                try {
                    SyncCheckRes syncCheckRes = syncCheck();
                    if (syncCheckRes == null)
                        break;
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
                    LOG.info("Exception:" + e.getMessage());
                }
            }
            LoginInfo.isLogin.set(false);
            LoginInfo.getLogout().doLogout();
        }).start();
    }

    /**
     *
     */
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
            LOG.info("EXCEPTION-URL:  " + url);
            LOG.info("EXCEPTION-POST: " + JSON.toJSONString(LoginInfo.initInfo.getSyncKey()));
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

    /**
     * @param content
     * @param fromUserName
     * @param toUserName
     */
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
            LOG.info(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return
     */
    private static SyncCheckRes syncCheck() {
        String res = "";
        String url = "";
        try {
            url = "https://webpush.wx2.qq.com/cgi-bin/mmwebwx-bin/synccheck?r=" + System.currentTimeMillis() + "&skey=" + LoginInfo.initInfo.getSKey()
                    + "&sid=" + LoginInfo.status.getWxsid() + "&uin=" + LoginInfo.status.getWxuin() + "&deviceid=e" + LoginInfo.getRand(15)
                    + "&synckey=" + URLEncoder.encode(LoginInfo.initInfo.getSyncKeyStr(), "utf-8") + "&_=" + LoginInfo.getLoginTime();
            CloseableHttpResponse httpResponse = httpClient.doGet(url, false);
            if (httpResponse != null && httpResponse.getEntity() != null) {//上层发生异常，关闭response
                res = EntityUtils.toString(httpResponse.getEntity());
            } else {
                return new SyncCheckRes("9999", "9999");//自定义... ...
            }
        } catch (Exception e) {
            LOG.info("EXCEPTION-RES:  " + res);
            LOG.info("EXCEPTION-URL:  " + url);
            LOG.info("EXCEPTION-GET:  " + LoginInfo.initInfo.getSyncKeyStr());
            e.printStackTrace();
        }
        //window.synccheck={retcode:"0",selector:"2"}
        Pattern pattern = Pattern.compile("window.synccheck=\\{retcode:\"(\\d+)\",selector:\"(\\d+)\"\\}");
        LOG.info(res);
        Matcher matcher = pattern.matcher(res);
        if (matcher.find()) {
            return new SyncCheckRes(matcher.group(1), matcher.group(2));
        }
        return null;
    }
}
