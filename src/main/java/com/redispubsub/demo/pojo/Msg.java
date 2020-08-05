package com.redispubsub.demo.pojo;

public class Msg  {
    //
    String msgType;
    public String getMsgType() {
        return this.msgType;
    }
    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    String content;
    public String getContent() {
        return this.content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
