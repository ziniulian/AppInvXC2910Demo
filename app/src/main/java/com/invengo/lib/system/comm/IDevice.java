package com.invengo.lib.system.comm;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * 解决 libserial_port.so 不一致问题
 * Created by 李泽荣 on 2018/7/19.
 */

public interface IDevice {
	boolean connect();

	void disconnect();

	boolean isConnected();

	boolean isAvailable();

	InputStream getInputStream();

	OutputStream getOutputStream();
}
