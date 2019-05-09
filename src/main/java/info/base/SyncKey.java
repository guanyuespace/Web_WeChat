package info.base;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Comparator;
import java.util.List;

public class SyncKey {
    private int Count;
    private List<SyncKeyInfo> List;

    public SyncKey() {
    }

    public SyncKey(int count) {
        Count = count;
    }

    @JSONField(name = "Count")
    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    @JSONField(name = "List")
    public List<SyncKeyInfo> getList() {
        return List;
    }

    public void setList(List<SyncKeyInfo> list) {
        List = list;
        List.sort(new Comparator<SyncKeyInfo>() {
            @Override
            public int compare(SyncKeyInfo o1, SyncKeyInfo o2) {
                return o1.getKey() - o2.getKey();
            }
        });
    }

    @Override
    public String toString() {
        return "SyncKey{" +
                "Count=" + Count +
                ", List=" + List +
                '}';
    }

    class SyncKeyInfo {
        private int Key;//
        private int Val;//15 5349 8882

        @JSONField(name = "Key")
        public int getKey() {
            return Key;
        }

        public void setKey(int key) {
            Key = key;
        }

        @JSONField(name = "Val")
        public int getVal() {
            return Val;
        }

        public void setVal(int val) {
            Val = val;
        }

        @Override
        public String toString() {
            return "SyncKeyInfo{" +
                    "Key=" + Key +
                    ", Val=" + Val +
                    '}';
        }
    }
}
