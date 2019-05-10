package info;

public class User {
    private int Uin;
    private String UserName;
    private String NickName;
    private String HeadImgUrl;
    private String RemarkName;
    private int Sex;//1:male 2:female 0:...
    private int VerifyFlag;
    private int ContactFlag;
    private String Signature;
    private String Province;
    private String City;

    public String getProvince() {
        return Province;
    }

    public void setProvince(String province) {
        Province = province;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public int getSex() {
        return Sex;
    }

    public void setSex(int sex) {
        Sex = sex;
    }

    public String getSignature() {
        return Signature;
    }

    public void setSignature(String signature) {
        Signature = signature;
    }

    @Override
    public String toString() {
        return "User{" +
                "Uin=" + Uin +
                ", UserName='" + UserName + '\'' +
                ", NickName='" + NickName + '\'' +
                ", HeadImgUrl='" + HeadImgUrl + '\'' +
                ", RemarkName='" + RemarkName + '\'' +
                ", Sex=" + Sex +
                ", VerifyFlag=" + VerifyFlag +
                ", ContactFlag=" + ContactFlag +
                ", Signature='" + Signature + '\'' +
                ", Province='" + Province + '\'' +
                ", City='" + City + '\'' +
                '}';
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getHeadImgUrl() {
        return HeadImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        HeadImgUrl = "https://wx2.qq.com" + headImgUrl;
    }

    public String getRemarkName() {
        return RemarkName;
    }

    public void setRemarkName(String remarkName) {
        RemarkName = remarkName;
    }

    public int isSex() {
        return Sex;
    }

    public int getVerifyFlag() {
        return VerifyFlag;
    }

    public void setVerifyFlag(int verifyFlag) {
        VerifyFlag = verifyFlag;
    }

    public int getContactFlag() {
        return ContactFlag;
    }

    public void setContactFlag(int contactFlag) {
        ContactFlag = contactFlag;
    }

    public int getUin() {
        return Uin;
    }

    public void setUin(int uin) {
        Uin = uin;
    }
}
