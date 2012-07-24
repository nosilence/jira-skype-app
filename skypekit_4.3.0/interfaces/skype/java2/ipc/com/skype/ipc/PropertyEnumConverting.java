package com.skype.ipc;

public interface PropertyEnumConverting extends EnumConverting {
    EnumConverting getEnumConverter();
    boolean isCached();
    int     getIdx();
    byte[]  getRequest();
    int     getModuleId();
}

