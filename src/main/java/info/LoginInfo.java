package info;


import info.base.InitInfo;
import info.base.Status;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import service.Logout;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

public class LoginInfo {
    public static boolean uuid_flag = false;
    public static String uuid;//获取二维码
    public static String qr_path = "D:/WeChat/info/mine";
    public static String qr_path_store = "D:/WeChat/info/mine/QR.jpg";//二维码存储位置
    public static String info_mine = "D:/WeChat/info/mine";
    public static String contact_imgs = "D:/WeChat/info/img";
    public static AtomicBoolean isLogin = new AtomicBoolean();//登陆状态
    public static Status status;//登陆信息{skey, sid, uin, pass_ticket}
    public static InitInfo initInfo;//init信息{SyncKey,Skey,user}
    public static ArrayList<User> friendsList;//好友列表
    private static long loginTime = 0l;//js GET param '_' 数值递增
    private static User me;
    private static Logout logout;

    static {
        isLogin.set(false);
    }


    private LoginInfo() {

    }

    public static Logout getLogout() {
        return logout;
    }

    public static void setLogout(Logout logout) {
        LoginInfo.logout = logout;
    }

    public static User getMe() {
        return me;
    }

    public static void setMe(User me) {
        LoginInfo.me = me;
    }

    /**
     * 10位随机数同时可用于头像命名
     * 1879422556
     *
     * @param length 随机数长度
     * @return
     */
    public static String getRand(int length) {
        StringBuilder stringBuilder = new StringBuilder(16);
        String cache = "0123456789";
        Random random = new Random(System.currentTimeMillis());
        for (int i = 0; i < length; i++) {
            stringBuilder.append(cache.charAt(random.nextInt(10)));
        }
        return stringBuilder.toString();
    }

    /**
     * <error>
     * <ret>0</ret>
     * <message></message>
     * <skey>XXXXXXXXXXXXXXX</skey>
     * <wxsid>XXXXXXXXXXXXXXX</wxsid>
     * <wxuin>XXXXXXXXXXXXXXX</wxuin>
     * <pass_ticket>XXXXXXXXXXXXXXX</pass_ticket>
     * <isgrayscale>XXXXXXXXX</isgrayscale>
     * </error>
     *
     * @param text
     * @return
     */
    public static Status parseXML(String text) {
        System.err.println(text);
        Document document;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        StringReader stringReader = new StringReader(text);
        InputSource inputSource = new InputSource(stringReader);
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(inputSource);
            String ret = document.getElementsByTagName("ret").item(0).getFirstChild().getNodeValue();
            if (ret.equals("0")) {
                return Status.custom()
                        .setSkey(document.getElementsByTagName("skey").item(0).getFirstChild().getNodeValue())
                        .setWxsid(document.getElementsByTagName("wxsid").item(0).getFirstChild().getNodeValue())
                        .setWxuin(document.getElementsByTagName("wxuin").item(0).getFirstChild().getNodeValue())
                        .setPass_ticket(document.getElementsByTagName("pass_ticket").item(0).getFirstChild().getNodeValue())
                        .setIsgrayscale(document.getElementsByTagName("isgrayscale").item(0).getFirstChild().getNodeValue())
                        .build();
            } else {
                return Status.custom()
                        .setMessage(document.getElementsByTagName("message").item(0).getFirstChild().getNodeValue() == null ? "" : document.getElementsByTagName("message").item(0).getFirstChild().getNodeValue())
                        .build(ret);
            }
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static long getLoginTime() {
        return LoginInfo.loginTime++;
    }

    public static void setLoginTime() {
        LoginInfo.loginTime = System.currentTimeMillis();
    }
}
