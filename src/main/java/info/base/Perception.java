package info.base;


public class Perception {
    private InputText inputText;
    private InputMedia inputMedia;
    private SelfInfo selfInfo;

    public Perception(InputText inputText, SelfInfo selfInfo) {
        this.inputText = inputText;
        this.selfInfo = selfInfo;
    }

    public InputText getInputText() {
        return inputText;
    }

    public void setInputText(InputText inputText) {
        this.inputText = inputText;
    }

    public InputMedia getInputMedia() {
        return inputMedia;
    }

    public void setInputMedia(InputMedia inputMedia) {
        this.inputMedia = inputMedia;
    }

    public SelfInfo getSelfInfo() {
        return selfInfo;
    }

    public void setSelfInfo(SelfInfo selfInfo) {
        this.selfInfo = selfInfo;
    }

    public static class InputText {
        private String text;

        public InputText(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class InputMedia {
        private String url;

        public InputMedia(String url) {
            this.url = url;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class SelfInfo {
        private Location location;

        public SelfInfo(Location location) {
            this.location = location;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public static class Location {
            private String city;
            private String province;
            private String street;

            public Location(String city) {
                this.city = city;
            }

            public Location(String city, String province, String street) {
                this.city = city;
                this.province = province;
                this.street = street;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getStreet() {
                return street;
            }

            public void setStreet(String street) {
                this.street = street;
            }
        }
    }
}
