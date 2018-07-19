package tk.ziniulian.job.xc2910demo.entity;

import android.webkit.JavascriptInterface;

import com.google.gson.Gson;

import tk.ziniulian.job.qr.EmQrCb;
import tk.ziniulian.job.qr.InfQrListener;
import tk.ziniulian.job.qr.xc2910.Qrd;
import tk.ziniulian.job.rfid.EmCb;
import tk.ziniulian.job.rfid.EmPushMod;
import tk.ziniulian.job.rfid.InfTagListener;
import tk.ziniulian.job.rfid.tag.T6C;
import tk.ziniulian.job.rfid.xc2910.Rd;
import tk.ziniulian.job.xc2910demo.Ma;
import tk.ziniulian.job.xc2910demo.enums.EmUh;
import tk.ziniulian.job.xc2910demo.enums.EmUrl;
import tk.ziniulian.util.dao.DbLocal;

/**
 * 业务接口
 * Created by 李泽荣 on 2018/7/17.
 */

public class Web {
	private Rd rfd = new Rd();
	private Qrd qr = new Qrd();
	private Gson gson = new Gson();
	private DbLocal ldao = null;
	private Ma ma;

	public Web (Ma m) {
		this.ma = m;
		ldao = new DbLocal(m);
	}

	// 读写器设置
	public void initRd () {
		rfd.setPwd(new byte[] {0x20, 0x26, 0x31, 0x07});
		rfd.setHex(true);
		rfd.setPm(EmPushMod.Catch);
		rfd.setTagListenter(new InfTagListener() {
			@Override
			public void onReadTag(T6C bt, InfTagListener itl) {}

			@Override
			public void onWrtTag(T6C bt, InfTagListener itl) {
				ma.sendUrl(EmUrl.RfWrtOk);
			}

			@Override
			public void cb(EmCb e, String[] args) {
				// Log.i("--rfd--", e.name());
				switch (e) {
					case Scanning:
						ma.sendUrl(EmUrl.RfScaning);
						break;
					case Stopped:
						ma.sendUrl(EmUrl.RfStoped);
						break;
					case ErrWrt:
						ma.sendUrl(EmUrl.RfWrtErr);
						break;
					case ErrConnect:
						ma.sendUrl(EmUrl.Err);
						break;
					case Connected:
						ma.sendUh(EmUh.Connected);
						break;
				}
			}
		});
		rfd.init();
	}

	// 二维码设置
	public void initQr() {
		qr.setQrListenter(new InfQrListener() {
			@Override
			public void onRead(String content) {
				ma.sendUrl(EmUrl.QrOnRead, gson.toJson(content));
			}

			@Override
			public void cb(EmQrCb e, String[] args) {
//				Log.i("--qr--", e.name());
				switch (e) {
					case ErrConnect:
						ma.sendUrl(EmUrl.Err);
						break;
					case Connected:
						ma.sendUh(EmUh.Connected);
						break;
				}
			}
		});
		qr.init();
	}

	public void open() {
		rfd.open();
		qr.open();
	}

	public void close() {
		rfd.close();
		qr.close();
	}

	public void qrDestroy() {
		qr.destroy();
	}

/*------------------- RFID ---------------------*/

	@JavascriptInterface
	public boolean isRfidBusy () {
		return rfd.isBusy();
	}

	@JavascriptInterface
	public void rfidScan() {
		rfd.scan();
	}

	@JavascriptInterface
	public void rfidStop() {
		rfd.stop();
	}

	@JavascriptInterface
	public void rfidWrt (String bankNam, String dat, String tid) {
		rfd.wrt(bankNam, dat, tid);
	}

	@JavascriptInterface
	public String rfidCatchScanning() {
		return rfd.catchScanning();
	}

/*------------------- 二维码 ---------------------*/

	@JavascriptInterface
	public boolean isQrBusy() {
		return qr.isBusy();
	}

	@JavascriptInterface
	public void qrScan() {
		qr.scan();
	}

	@JavascriptInterface
	public void qrStop() {
		qr.stop();
	}

/*------------------- 数据库 ---------------------*/

	@JavascriptInterface
	public String kvGet(String k) {
		return ldao.kvGet(k);
	}

	@JavascriptInterface
	public void kvSet(String k, String v) {
		ldao.kvSet(k, v);
	}

	@JavascriptInterface
	public void kvDel(String k) {
		ldao.kvDel(k);
	}

/*------------------- 其它 ---------------------*/

}
