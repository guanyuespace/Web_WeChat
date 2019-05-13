package service.impl;

import service.IMessageProcess;

public class MessageProcessTest {
    public static void main(String[] args) {
        IMessageProcess iMessageProcess = new MessageProcess();
        iMessageProcess.processText("Hello");
    }
}