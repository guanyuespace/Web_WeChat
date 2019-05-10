package util;

import info.User;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    /**
     * 文件名不包含特殊字符
     *
     * @param user
     * @return
     */
    public static String getFileName(User user) {
        String nickName = user.getNickName();
        if (user.getRemarkName() != null && !user.getRemarkName().equals(""))
            nickName = user.getRemarkName();
        if (nickName.length() > 0) {
            nickName = nickName.replaceAll("\\\\", "+");
//            nickName = nickName.replaceAll("[/:\"|?<>*-.\\\\]", "+");// 为何会替换英文首字母&数字？？
            nickName = nickName.replaceAll("[/:\"|?<>*-.]", "+");// /\:"|?<>*-    [:/\\|?<>\"\\\\]
            nickName.replaceAll("\\s+", "+");
            return nickName;
        } else return "null";
    }

    public static boolean existsOrCreateFile(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile())
            return true;
        try {
            return file.createNewFile();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean existsOrCreateDirs(String dirPath) {
        File dirs = new File(dirPath);
        if (dirs.exists() && dirs.isDirectory())
            return true;
        return dirs.mkdirs();
    }
}
