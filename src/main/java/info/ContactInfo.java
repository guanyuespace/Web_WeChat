package info;

import info.base.BaseResponse;

import java.util.ArrayList;

public class ContactInfo {
    private BaseResponse baseResponse;
    private int MemberCount;
    private ArrayList<User> MemberList;
    private int seq;

    public BaseResponse getBaseResponse() {
        return baseResponse;
    }

    public void setBaseResponse(BaseResponse baseResponse) {
        this.baseResponse = baseResponse;
    }

    public int getMemberCount() {
        return MemberCount;
    }

    public void setMemberCount(int memberCount) {
        MemberCount = memberCount;
    }

    public ArrayList<User> getMemberList() {
        return MemberList;
    }

    public void setMemberList(ArrayList<User> memberList) {
        MemberList = memberList;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    @Override
    public String toString() {
        return "ContactInfo{" +
                "baseResponse=" + baseResponse +
                ", MemberCount=" + MemberCount +
                ", MemberList=" + MemberList +
                ", seq=" + seq +
                '}';
    }
}
