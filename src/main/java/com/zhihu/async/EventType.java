package com.zhihu.async;

public enum EventType {
    LIKE(0),
    COMMENT(6),
    LOGIN(2),
    MAIL(3),
    FOLLOW(4),
    UNFOLLOW(5),
    ANSWER(1),
    ADD_QUESTION(6);

    private int value;

    EventType(int value){
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
