package util;

import java.io.IOException;

public class ShowPic {
    public static boolean show(String picPath) {
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("cmd /c start " + picPath);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
