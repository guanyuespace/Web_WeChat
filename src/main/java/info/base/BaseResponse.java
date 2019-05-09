package info.base;

public class BaseResponse {
    private int Ret;
    private String ErrMsg;

    @Override
    public String toString() {
        return "BaseResponse{" +
                "Ret=" + Ret +
                ", ErrMsg='" + ErrMsg + '\'' +
                '}';
    }

    public int getRet() {
        return Ret;
    }

    public void setRet(int ret) {
        Ret = ret;
    }

    public String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String errMsg) {
        ErrMsg = errMsg;
    }
}
