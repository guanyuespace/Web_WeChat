package test;

import info.LoginInfo;
import service.LoginController;
import service.Logout;

public class WeChat {
    public static void main(String[] args) {
        System.setProperty("jsse.enableSNIExtension", "false"); // 防止SSL错误
        LoginController loginController = new LoginController();

        Logout logout = new Logout() {
            @Override
            public void doLogout() {
                loginController.logout();
            }
        };
        LoginInfo.setLogout(logout);

        loginController.login();
    }
}
