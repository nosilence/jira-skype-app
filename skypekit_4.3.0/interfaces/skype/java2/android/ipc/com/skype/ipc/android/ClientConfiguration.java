package com.skype.ipc.android;

import android.net.LocalSocketAddress.*;

public class ClientConfiguration extends com.skype.ipc.ClientConfiguration {

    public ClientConfiguration() {
        super();
        mLocalSocketName = "SkypeKit";
        mLocalSocketNamespace = Namespace.ABSTRACT;
        useLocalTransport = true;
        setTransportFactory(new TransportFactory());
    }

    public String getLocalSocketName() {
        return mLocalSocketName;
    }

    public Namespace getLocalSocketNamespace() {
        return mLocalSocketNamespace;
    }

    public void setLocalSocketName(String name) {
	useLocalTransport = true;
        mLocalSocketName = name;
    }

    public void getLocalSocketNamespace(Namespace namespace) {
	useLocalTransport = true;
        mLocalSocketNamespace = namespace;
    }

    private String    mLocalSocketName;
    private Namespace mLocalSocketNamespace;
}

