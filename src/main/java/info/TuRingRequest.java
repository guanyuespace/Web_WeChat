package info;

import info.base.Perception;
import info.base.UserInfo;

public class TuRingRequest {
    private int reqType;
    private Perception perception;
    private UserInfo userInfo;

    public TuRingRequest(int reqType) {
        this.reqType = reqType;
    }

    public int getReqType() {
        return reqType;
    }

    public void setReqType(int reqType) {
        this.reqType = reqType;
    }

    public Perception getPerception() {
        return perception;
    }

    public void setPerception(Perception perception) {
        this.perception = perception;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
