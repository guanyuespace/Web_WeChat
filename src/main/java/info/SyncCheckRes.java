package info;

public class SyncCheckRes {
    private String retcode;
    private String selector;

    public SyncCheckRes(String retcode, String selector) {
        this.retcode = retcode;
        this.selector = selector;
    }

    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public String getSelector() {
        return selector;
    }

    public void setSelector(String selector) {
        this.selector = selector;
    }
}
