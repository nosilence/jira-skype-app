package com.skype.ipc;

public interface EnumConverting {
    public int getId();
    public EnumConverting convert(final int value);
    public EnumConverting getDefault();
    public EnumConverting[] getArray(final int size);
}

