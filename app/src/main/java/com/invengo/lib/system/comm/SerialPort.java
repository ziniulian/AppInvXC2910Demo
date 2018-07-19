package com.invengo.lib.system.comm;

import com.invengo.lib.diagnostics.InvengoLog;
import com.invengo.lib.util.SysUtil;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 解决 libserial_port.so 不一致问题
 * Created by 李泽荣 on 2018/7/19.
 */

public class SerialPort implements IDevice {
	private static final String TAG = SerialPort.class.getSimpleName();
	private static final String VERSION = "1.0";
	private FileDescriptor mFileDesc;
	private final String mPortName;
	private final int mBaudrate;
	private FileOutputStream mDebug;

	static {
		System.loadLibrary("serial_port_qr");
	}

	public SerialPort(String portName, int baudrate) {
		this.mPortName = portName;
		this.mBaudrate = baudrate;
	}

	public boolean connect() {
		File device = new File(this.mPortName);
		InvengoLog.i(TAG, "INFO. connect - [[%s], %d]", new Object[]{this.mPortName, Integer.valueOf(this.mBaudrate)});
		if(!device.canRead() || !device.canWrite()) {
			try {
				Process e = Runtime.getRuntime().exec("su");
				String cmd = "chmod 666 " + device.getAbsolutePath() + "\n" + "exit\n";
				e.getOutputStream().write(cmd.getBytes());
				e.getOutputStream().flush();
				if(e.waitFor() != 0 || !device.canRead() || !device.canWrite()) {
					InvengoLog.e(TAG, "ERROR. connect() - Failed to obtain permission for serial port [[%s], %d]", new Object[]{this.mPortName, Integer.valueOf(this.mBaudrate)});
				}
			} catch (Exception var5) {
				InvengoLog.e(TAG, var5, "ERROR. connect() - Failed to obtain permission for serial port [[%s], %d]", new Object[]{this.mPortName, Integer.valueOf(this.mBaudrate)});
			}
		}

		try {
			this.mFileDesc = openSerialPort(device.getAbsolutePath(), this.mBaudrate);
			if(this.mFileDesc == null) {
				InvengoLog.e(TAG, "ERROR. connect() - Failed to open serial port [[%s], %d]", new Object[]{this.mPortName, Integer.valueOf(this.mBaudrate)});
				return false;
			} else {
				return true;
			}
		} catch (Exception var4) {
			InvengoLog.e(TAG, var4, "ERROR. connect() - Failed to open serial port [[%s], %d]", new Object[]{this.mPortName, Integer.valueOf(this.mBaudrate)});
			return false;
		}
	}

	public void disconnect() {
		if(this.mFileDesc != null) {
			closeSerialPort(this.mFileDesc);
			this.mFileDesc = null;
			if(this.mDebug != null) {
				try {
					this.mDebug.flush();
					this.mDebug.close();
					this.mDebug = null;
				} catch (IOException var2) {
					InvengoLog.e(TAG, var2, "ERROR. disconnect() - Failed to close debug", new Object[0]);
				}
			}

		}
	}

	public boolean isConnected() {
		return this.mFileDesc != null;
	}

	public InputStream getInputStream() {
		return new FileInputStream(this.mFileDesc);
	}

	public OutputStream getOutputStream() {
		return new FileOutputStream(this.mFileDesc);
	}

	public boolean isAvailable() {
		if(this.mPortName != null && this.mBaudrate != 0) {
			File device = new File(this.mPortName);
			InvengoLog.i(TAG, "INFO. isAvailable() - check SerialPort [[%s], %d]", new Object[]{this.mPortName, Integer.valueOf(this.mBaudrate)});
			if(!device.canRead() || !device.canWrite()) {
				try {
					Process e = Runtime.getRuntime().exec("su");
					String cmd = "chmod 666 " + device.getAbsolutePath() + "\n" + "exit\n";
					e.getOutputStream().write(cmd.getBytes());
					e.getOutputStream().flush();
					if(e.waitFor() != 0 || !device.canRead() || !device.canWrite()) {
						InvengoLog.e(TAG, "ERROR. isAvailable() - Failed to obtain permission for serial port [[%s], %d]", new Object[]{this.mPortName, Integer.valueOf(this.mBaudrate)});
						return false;
					}
				} catch (Exception var6) {
					InvengoLog.e(TAG, var6, "ERROR. isAvailable() - Failed to obtain permission for serial port [[%s], %d]", new Object[]{this.mPortName, Integer.valueOf(this.mBaudrate)});
					return false;
				}
			}

			FileDescriptor fd;
			try {
				fd = openSerialPort(device.getAbsolutePath(), this.mBaudrate);
				if(fd == null) {
					InvengoLog.e(TAG, "ERROR. isAvailable() - Failed to open serial port [[%s], %d]", new Object[]{this.mPortName, Integer.valueOf(this.mBaudrate)});
					return false;
				}
			} catch (Exception var5) {
				InvengoLog.e(TAG, var5, "ERROR. isAvailable() - Failed to open serial port [[%s], %d]", new Object[]{this.mPortName, Integer.valueOf(this.mBaudrate)});
				return false;
			}

			if(fd != null) {
				InvengoLog.d(TAG, "DEBUG. Wait for 50ms");
				SysUtil.sleep(50L);
				closeSerialPort(fd);
				fd = null;
			}

			return true;
		} else {
			InvengoLog.e(TAG, "ERROR. isAvailable() - Failed to invalid arguments [[%s], %d]", new Object[]{this.mPortName == null?"null":this.mPortName, Integer.valueOf(this.mBaudrate)});
			return false;
		}
	}

	private static native FileDescriptor openSerialPort(String var0, int var1);

	private static native void closeSerialPort(FileDescriptor var0);

	public static native String getLibVersion();

	public static String getVersion() {
		return "1.0";
	}
}
