package info;

import com.alibaba.fastjson.JSON;
import constant.Secret;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class TuRingRequestEncrypted {
    private String secret = Secret.secret;
    private String timestamp;
    private String apiKey = Secret.apiKey;
    private String data;
    private String paramStr;
    private String md5Key;

    /**
     * 文本信息处理
     *
     * @param tuRingRequest 数据请求，明文
     */
    public TuRingRequestEncrypted(TuRingRequest tuRingRequest) {

        this.paramStr = JSON.toJSONString(tuRingRequest);
        this.timestamp = "" + System.currentTimeMillis();
        this.md5Key = processKey(secret, timestamp, apiKey);
        this.data = AES_Encrypted(this.paramStr, md5Key);
    }

    private String AES_Encrypted(String paramStr, String md5Key) {
        Cipher cipher = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(md5Key.getBytes(StandardCharsets.UTF_8));
            byte[] tmp = messageDigest.digest();
            Key key = new SecretKeySpec(tmp, "AES");
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(new byte[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
            byte[] encryptedData = cipher.doFinal(paramStr.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String processKey(String secret, String timestamp, String apiKey) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            return byte2HexStr(messageDigest.digest((secret + timestamp + apiKey).getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String byte2HexStr(byte[] digest) {
        String str = "0123456789abcdef";
        StringBuilder stringBuilder = new StringBuilder(2 * digest.length);
        for (int i = 0; i < digest.length; i++) {
            byte high = (byte) ((byte) 0x0f & (digest[i] >> 4));
            stringBuilder.append(str.charAt(high));
            byte low = (byte) (digest[i] & 0x0f);
            stringBuilder.append(str.charAt(low));
        }
        return stringBuilder.toString();
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getData() {
        return data;
    }

    public String getKey() {
        return apiKey;
    }
}
