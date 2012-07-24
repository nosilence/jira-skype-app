package com.skype.ipc;

public interface ObjectFactoring {
	SidObject   sidGetObject (int moduleId, int oid);
	SidObject[] sidGetObjects(int moduleId, int size);
}

