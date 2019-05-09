package info.base;

public class Status {
    public String ret;
    private Builder builder;

    public Status(String ret) {
        this.ret = ret;
        this.builder = null;
    }

    public Status(String ret, Builder builder) {
        this.ret = ret;
        this.builder = builder;
    }

    public static Builder custom() {
        return new Builder();
    }

    public String getMessage() {
        return builder.getMessage();
    }

    public String getSkey() {
        return builder.getSkey();
    }

    public void setSkey(String skey) {
        builder.setSkey(skey);
    }

    public String getWxsid() {
        return builder.getWxsid();
    }

    public String getWxuin() {
        return builder.getWxuin();
    }

    public String getPass_ticket() {
        return builder.getPass_ticket();
    }

    public String getIsgrayscale() {
        return builder.getIsgrayscale();
    }


    @Override
    public String toString() {
        return "Status{" +
                "ret='" + ret + '\'' +
                ", builder=[" + builder.toString() +
                "] }";
    }

    public static class Builder {
        String message;
        String skey;
        String wxsid;
        String wxuin;
        String pass_ticket;
        String isgrayscale;

        Builder() {
            message = "";
            skey = "";
            wxsid = "";
            wxuin = "";
            pass_ticket = "";
            isgrayscale = "";
        }


        String getMessage() {
            return message;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        String getSkey() {
            return skey;
        }

        public Builder setSkey(String skey) {
            this.skey = skey;
            return this;
        }

        String getWxsid() {
            return wxsid;
        }

        public Builder setWxsid(String wxsid) {
            this.wxsid = wxsid;
            return this;
        }

        String getWxuin() {
            return wxuin;
        }

        public Builder setWxuin(String wxuin) {
            this.wxuin = wxuin;
            return this;
        }

        String getPass_ticket() {
            return pass_ticket;
        }

        public Builder setPass_ticket(String pass_ticket) {
            this.pass_ticket = pass_ticket;
            return this;
        }

        String getIsgrayscale() {
            return isgrayscale;
        }

        public Builder setIsgrayscale(String isgrayscale) {
            this.isgrayscale = isgrayscale;
            return this;
        }

        @Override
        public String toString() {
            return "Builder{" +
                    "message='" + message + '\'' +
                    ", skey='" + skey + '\'' +
                    ", wxsid='" + wxsid + '\'' +
                    ", wxuin='" + wxuin + '\'' +
                    ", pass_ticket='" + pass_ticket + '\'' +
                    ", isgrayscale='" + isgrayscale + '\'' +
                    '}';
        }

        public Status build() {
            return build("0");
        }

        public Status build(String ret) {
            return new Status(ret, this);
        }
    }
}
