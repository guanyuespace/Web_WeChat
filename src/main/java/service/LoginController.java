package service;


import com.alibaba.fastjson.JSON;
import info.ContactInfo;
import info.LoginInfo;
import info.base.InitInfo;
import info.base.Status;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import util.Base64toImg;
import util.FileUtils;
import util.MyHttpClient;
import util.ShowPic;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static info.LoginInfo.*;

public class LoginController {
    private static MyHttpClient httpClient = MyHttpClient.getINSTANCE();

    /**
     * uuid
     *
     * @throws IOException
     */
    private static void getUUID() throws IOException {

        String url = "https://login.wx.qq.com/jslogin?appid=wx782c26e4c19acffb&redirect_uri=https%3A%2F%2Fwx.qq.com%2Fcgi-bin%2Fmmwebwx-bin%2Fwebwxnewloginpage&fun=new&lang=zh_CN&_=" + LoginInfo.getLoginTime();
        String res = EntityUtils.toString(httpClient.doGet(url, false).getEntity());
        System.out.println(res);
        Pattern pattern = Pattern.compile("window.QRLogin.code = (\\d+); window.QRLogin.uuid = \"(\\S+?)\";");
        Matcher matcher = pattern.matcher(res);
        if (matcher.find()) {
            if (matcher.group(1).equals("200")) {
                LoginInfo.uuid_flag = true;
                LoginInfo.uuid = matcher.group(2);
            }
        }
    }

    /**
     * 二维码
     */
    private static void getQRCode() {
        if (FileUtils.existsOrCreateDirs(LoginInfo.qr_path) && FileUtils.existsOrCreateFile(LoginInfo.qr_path_store)) {
            String url = "https://login.weixin.qq.com/qrcode/" + LoginInfo.uuid;
            HttpEntity entity = httpClient.doGet(url, false).getEntity();
            try (FileOutputStream fos = new FileOutputStream(LoginInfo.qr_path_store)) {
                fos.write(EntityUtils.toByteArray(entity));
                fos.flush();
                fos.close();
                ShowPic.show(LoginInfo.qr_path_store);//不允许异常
            } catch (IOException e) {
                e.printStackTrace();
//            retry...
            }
        }
    }

    /**
     * 用户头像
     *
     * @throws IOException
     */
    private static void getAvatar() throws IOException {
        String rand = getRand(10);
        String url = "https://login.wx.qq.com/cgi-bin/mmwebwx-bin/login?loginicon=true&uuid=" + LoginInfo.uuid + "&tip=1&r=" + rand + "&_=" + LoginInfo.getLoginTime();
        String res = EntityUtils.toString(httpClient.doGet(url, false).getEntity());
        Pattern pattern = Pattern.compile("window.code=(\\d+);(window.userAvatar = \'data:(.*);base64,(.*)\')?");
        Matcher matcher = pattern.matcher(res);
        if (matcher.find()) {
            if (matcher.group(1).equals("201")) {
                if (matcher.group(2) != null && !matcher.group(2).equals("")) {
                    if (matcher.group(3).equals("img/jpg")) {
                        Base64toImg.parseImg(LoginInfo.info_mine + "/avatar_" + rand + ".jpg", matcher.group(4));
                    }
                } else {
                    System.out.println("no avatar !");
                }
            }
        }
    }

    /**
     * 跳转
     *
     * @throws IOException
     */
    private static void redict() throws IOException {
        String url = "https://login.wx.qq.com/cgi-bin/mmwebwx-bin/login?loginicon=true&uuid=" + LoginInfo.uuid + "&tip=0&r=" + getRand(10) + "&_=" + LoginInfo.getLoginTime();
        String res = EntityUtils.toString(httpClient.doGet(url, false).getEntity());
        String[] retState = res.split("\n");
        Pattern pattern = Pattern.compile("window.code=(\\d+);");
        Matcher matcher = pattern.matcher(retState[0]);
        if (matcher.find() && matcher.group(1).equals("200")) {
            pattern = Pattern.compile("window.redirect_uri=\"(.*)\";");
            matcher = pattern.matcher(retState[1]);
            if (matcher.find()) {
                System.out.println("redict: " + matcher.group(1));
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                redictforStatus(matcher.group(1));
            }
        }
    }

    /**
     * 获取status{skey, sid, uin, pass_ticket}
     *
     * @param url
     * @throws IOException
     */
    private static void redictforStatus(String url) throws IOException {
        CloseableHttpResponse response = httpClient.doGet(url, false);
        String redict = "";
        if (response.containsHeader("Location"))
            redict = response.getFirstHeader("Location").getValue().equals("/") ? "https://wx2.qq.com/"
                    : response.getFirstHeader("Location").getValue();
        String res = EntityUtils.toString(response.getEntity());
        Status status = parseXML(res);
        if (status != null) {
            LoginInfo.status = status;
            System.out.println(status);
            if (status.ret.equals("0")) {
                LoginInfo.isLogin = true;

                System.out.println("ok ... ...");
                if (!redict.equals(""))
                    httpClient.doGet(redict, false);//登陆首页跳转
            } else {
                System.out.println(status.getMessage());
            }
        }
    }

    /**
     * 获取好友列表，download head_image
     */
    private static void getContact() throws IOException {
        if (FileUtils.existsOrCreateDirs(LoginInfo.contact_imgs)) {
            String url = "https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxgetcontact?r=" + System.currentTimeMillis() + "&seq=0&skey=" + LoginInfo.status.getSkey();
            CloseableHttpResponse response = httpClient.doGet(url, false);
            String text = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            ContactInfo contactInfo = JSON.parseObject(text, ContactInfo.class);
            System.out.println(contactInfo);
            friendsList = contactInfo.getMemberList();
            //下载好友头像
           /* for (User user : friendsList) {
                String urlStr = user.getHeadImgUrl();
                CloseableHttpResponse resp = httpClient.doGet(user.getHeadImgUrl(), false);
                InputStream inputStream = resp.getEntity().getContent();
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(Files.newOutputStream(Paths.get(LoginInfo.contact_imgs + "/" + FileUtils.getFileName(user) + ".jpg")), 5120);
                int cached = 0, size;
                byte[] bytes = new byte[1024];
                while ((size = inputStream.read(bytes)) != -1) {
                    bufferedOutputStream.write(bytes, 0, size);
                    cached += size;
                    if (cached + 1024 > 5120) {
                        bufferedOutputStream.flush();
                        cached = 0;
                    }
                }
                if (cached > 0)
                    bufferedOutputStream.flush();
                bufferedOutputStream.close();
                if (inputStream != null)
                    inputStream.close();
            }*/
        }
    }


    /**
     * 登陆控制
     */
    public void login() {
        try {
            firstHomeGet();
            LoginInfo.setLoginTime();//设置登录时间，js GET 参数：'_'递增
            getUUID();
            if (LoginInfo.uuid_flag) {
                //二维码
                getQRCode();
                //头像
                getAvatar();
                //跳转,获取status{skey, sid, uin, pass_ticket}
                redict();
                if (LoginInfo.isLogin) {//ok
                    //获取InitInfo{SyncKey, Skey}
                    webwxinit();

                    //获取好友列表
                    getContact();

                    wxStatusNotify();

                    //开始信息接收
                    MessageTools.start();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     *
     */
    private void wxStatusNotify() throws IOException {
        String url = "https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxstatusnotify?lang=en_US&pass_ticket=" + LoginInfo.status.getPass_ticket();
        String param = "{\"BaseRequest\":{\"Uin\":" + LoginInfo.status.getWxuin() + ",\"Sid\":\"" + LoginInfo.status.getWxsid() + "\"," +
                "\"Skey\":\"" + LoginInfo.status.getSkey() + "\"," +
                "\"DeviceID\":\"e" + LoginInfo.getRand(15) + "\"},\"Code\":3," +
                "\"FromUserName\":\"" + LoginInfo.getMe().getUserName() + "\"," +
                "\"ToUserName\":\"" + LoginInfo.getMe().getUserName() + "\"," +
                "\"ClientMsgId\":" + System.currentTimeMillis() + "}";

        String res = EntityUtils.toString(httpClient.doPost(url, null, param, null).getEntity());
        System.out.println(res);
    }

    /**
     * 注销登陆
     * Content-Type: application/x-www-form-urlencoded
     */
    public void logout() {
        String url = "https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxlogout?redirect=1&type=0&skey=" + LoginInfo.status.getSkey();
        List<NameValuePair> nameValuePairList = new ArrayList<>();
        nameValuePairList.add(new BasicNameValuePair("sid", LoginInfo.status.getWxsid()));
        nameValuePairList.add(new BasicNameValuePair("uin", LoginInfo.status.getWxuin()));
        CloseableHttpResponse response = httpClient.doPost(url, nameValuePairList, null, null);
        try {
            response.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("logout... ok");
    }

    /**
     * 初始信息,获取InitInfo{SyncKey, Skey}
     *
     * @throws IOException
     */
    private void webwxinit() throws IOException {
        String url = "https://wx2.qq.com/cgi-bin/mmwebwx-bin/webwxinit?r=" + getRand(10) + "&lang=zh_CN&pass_ticket=" + LoginInfo.status.getPass_ticket();
        String param = "{\"BaseRequest\":{\"Uin\":\"" + LoginInfo.status.getWxuin() + "\",\"Sid\":\"" + LoginInfo.status.getWxsid() + "\",\"Skey\":\"" + "" + "\",\"DeviceID\":\"e" + getRand(15) + "\"}}";
        CloseableHttpResponse response = httpClient.doPost(url, null, param, null);
        LoginInfo.initInfo = JSON.parseObject(EntityUtils.toString(response.getEntity()), InitInfo.class);
        if (LoginInfo.status.getSkey() != LoginInfo.initInfo.getSKey()) {
            System.out.println("NO WAY !!!");
            LoginInfo.initInfo.setSKey(LoginInfo.initInfo.getSKey());
        }
    }


    /**
     * web版登陆首页
     * https://wx.qq.com/
     */
    private void firstHomeGet() {
        String url = "https://wx.qq.com/";
        CloseableHttpResponse httpResponse = httpClient.doGet(url, false);
        try {
            httpResponse.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
