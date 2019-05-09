package info.base;

public class MessageInfo {
    private String MsgId;
    private String FromUserName;
    private String ToUserName;
    private int MsgType;
    private String Content;
    private int Status;

    public MessageInfo(String msgId, String fromUserName, String toUserName, int msgType, String content, int status) {
        MsgId = msgId;
        FromUserName = fromUserName;
        ToUserName = toUserName;
        MsgType = msgType;
        Content = content;
        Status = status;
    }

    public String getMsgId() {
        return MsgId;
    }

    public void setMsgId(String msgId) {
        MsgId = msgId;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public int getMsgType() {
        return MsgType;
    }

    public void setMsgType(int msgType) {
        MsgType = msgType;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}
