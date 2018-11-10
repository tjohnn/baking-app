package com.tjohnn.baking.util;

public class RxEventWrapper<T> {

    private T content;
    private boolean hasBeenUsed;

    public RxEventWrapper(T content) {
        this.content = content;
    }

    public T getContentIfNotUsed(){
        if(hasBeenUsed){
            return null;
        }
        hasBeenUsed = true;
        return content;
    }

    public T peekContent(){
        return content;
    }

}
