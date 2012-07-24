package com.skype.ipc;

public interface ConnectionListener
{
    /**
     * The connection to the runtime has closed. Recovery is
     * theoretically possible if the runtime can be restarted, the
     * connection re-initialized, and session logins restored, but more
     * often this is indicative of a fatal error condition.
     */
    public void sidOnDisconnected(String cause);
    public void sidOnConnected();
    public void sidOnConnecting();
}

