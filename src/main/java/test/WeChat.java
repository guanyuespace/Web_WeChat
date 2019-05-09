package test;

import info.LoginInfo;
import service.LoginController;

public class WeChat {
    public static void main(String[] args) {
        System.setProperty("jsse.enableSNIExtension", "false"); // 防止SSL错误
        LoginController loginController = new LoginController();
        loginController.login();


        while (LoginInfo.isLogin.get()) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        loginController.logout();


    }
}
