package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Base64toImg {
    private static void getImg(String path, String imgStr) {
        Pattern pattern = Pattern.compile("data:image/png;base64,(.*)");
        Matcher matcher = pattern.matcher(imgStr);
        if (matcher.find()) {
            parseImg(path, matcher.group(1));
        }

    }

    public static void parseImg(String path, String imgStr) {
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] retbytes = decoder.decode(imgStr);
        try {
            Files.write(Paths.get(path), retbytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
