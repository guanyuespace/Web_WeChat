package info.base;

import info.LoginInfo;
import info.User;

public class InitInfo {
    private BaseResponse baseResponse;
    private SyncKey syncKey;
    private User user;
    private String SKey;
    private String ClientVersion;

    @Override
    public String toString() {
        return "InitInfo{" + "\n" +
                "baseResponse=" + baseResponse + "\n" +
                ", syncKey=" + syncKey + "\n" +
                ", user=" + user + "\n" +
                ", SKey='" + SKey + '\'' + "\n" +
                ", ClientVersion='" + ClientVersion + '\'' + "\n" +
                '}';
    }

    public BaseResponse getBaseResponse() {
        return baseResponse;
    }

    public void setBaseResponse(BaseResponse baseResponse) {
        this.baseResponse = baseResponse;
    }

    public String getSyncKeyStr() {
        StringBuilder stringBuilder = new StringBuilder(128);
        for (SyncKey.SyncKeyInfo syncKeyInfo : syncKey.getList()) {
            stringBuilder.append(syncKeyInfo.getKey() + "_" + syncKeyInfo.getVal() + "|");
        }
        return stringBuilder.substring(0, stringBuilder.length() - 1);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        LoginInfo.setMe(user);
    }

    public String getSKey() {
        return SKey;
    }

    public void setSKey(String SKey) {
        this.SKey = SKey;
    }

    public String getClientVersion() {
        return ClientVersion;
    }

    public void setClientVersion(String clientVersion) {
        ClientVersion = clientVersion;
    }

    public SyncKey getSyncKey() {
        return syncKey;
    }

    public void setSyncKey(SyncKey syncKey) {
        System.out.println("Set syncKey=" + syncKey);
        this.syncKey = syncKey;
    }
}