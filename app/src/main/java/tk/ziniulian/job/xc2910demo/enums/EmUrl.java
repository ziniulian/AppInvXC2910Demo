package tk.ziniulian.job.xc2910demo.enums;

/**
 * 页面信息
 * Created by 李泽荣 on 2018/7/17.
 */

public enum EmUrl {
	// RFID 测试
	RfScaning("javascript: rfid.scan();"),
	RfStoped("javascript: rfid.stop();"),
	RfWrtOk("javascript: rfid.hdWrt(true);"),
	RfWrtErr("javascript: rfid.hdWrt(false);"),
	ScanTt("file:///android_asset/testRFID/scanDemo.html"),
	ScanTmpTt("file:///android_asset/testRFID/scanTemperatureDemo.html"),
	WrtTt("file:///android_asset/testRFID/writeDemo.html"),

	// 条码、二维码 测试
	QrOnRead("javascript: qr.hdScan(<0>);"),
	QrTt("file:///android_asset/testQR/qrDemo.html"),

	// 主页
	Home("file:///android_asset/web/home.html"),
	Back("javascript: dat.back();"),
	Err("file:///android_asset/web/err.html");

	private final String url;
	EmUrl(String u) {
		url = u;
	}

	@Override
	public String toString() {
		return url;
	}
}
