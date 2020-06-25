package com.studilieferservice.groupmanager.controller.bodys;

public class TestForm {
    public String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "TestForm{" +
                "msg='" + msg + '\'' +
                '}';
    }
}
