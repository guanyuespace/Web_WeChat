package info;

import info.base.BaseResponse;
import info.base.MessageInfo;
import info.base.SyncKey;

import java.util.List;

public class SyncInfo {
    private BaseResponse baseResponse;
    private int AddMsgCount;
    private List<MessageInfo> AddMsgList;
    private SyncKey syncKey;
    private SyncKey SyncCheckKey;

    public BaseResponse getBaseResponse() {
        return baseResponse;
    }

    public void setBaseResponse(BaseResponse baseResponse) {
        System.out.println(baseResponse.getRet() != 0 ? "??????\t" + baseResponse.getRet() : "");
        this.baseResponse = baseResponse;
    }

    public int getAddMsgCount() {
        return AddMsgCount;
    }

    public void setAddMsgCount(int addMsgCount) {
        AddMsgCount = addMsgCount;
    }

    public List<MessageInfo> getAddMsgList() {
        return AddMsgList;
    }

    public void setAddMsgList(List<MessageInfo> addMsgList) {
        AddMsgList = addMsgList;
    }

    public SyncKey getSyncKey() {
        return syncKey;
    }

    public void setSyncKey(SyncKey syncKey) {
        this.syncKey = syncKey;
        LoginInfo.initInfo.setSyncKey(syncKey);//更新SyncKey
    }

    public SyncKey getSyncCheckKey() {
        return SyncCheckKey;
    }

    public void setSyncCheckKey(SyncKey syncCheckKey) {
        SyncCheckKey = syncCheckKey;

    }
}
