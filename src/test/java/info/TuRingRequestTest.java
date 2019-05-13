package info;

import com.alibaba.fastjson.JSON;
import info.base.Perception;
import info.base.UserInfo;

public class TuRingRequestTest {
    public static void main(String[] args) {
        TuRingRequest tuRingRequest = new TuRingRequest(0);
        tuRingRequest.setUserInfo(new UserInfo());
        tuRingRequest.setPerception(new Perception(new Perception.InputText("你在哪？"), new Perception.SelfInfo(new Perception.SelfInfo.Location("深圳"))));
        System.out.println(JSON.toJSONString(tuRingRequest));

        TuRingRequestEncrypted tuRingRequestEncrypted = new TuRingRequestEncrypted(tuRingRequest);
        System.out.println(JSON.toJSONString(tuRingRequestEncrypted));
    }
}