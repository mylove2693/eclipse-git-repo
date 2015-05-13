package com.cynovo.sirius.jni;

import java.util.Arrays;

import android.annotation.SuppressLint;
import android.util.Log;

/**
 * @author Edwin
 * @return
 * 
 *         0: Pass<br>
 *         -4: Field 39 is not '00'<br>
 *         -5: Field 39 not exist<br>
 *         -8: Field 62 not exist<br>
 *         -10: Socket connect fail<br>
 *         -101: Socket write fail<br>
 *         -102: Socket read fail<br>
 *         -200: traceNo is null<br>
 */
public class HostlinkInterface {

	public static class ICDataClass {
		public String PAN; // F2: 主账号
		public String dateOfExpired; // F14: 卡有效期
		public String cardSeqNo; // F23: 卡片序列号
		public byte[] f55;
		public int f55Length;
		public int flag;
	}

	static {
		System.loadLibrary("UnionpayCloudPos");
		System.loadLibrary("PosMsg");
		System.loadLibrary("hostlink");
	}

	public native static int open();

	public native static int close();

	/**
	 * 下载主密钥
	 * 
	 * @return
	 */
	public native static int updatemkey();

	// 下载工作密钥
	public native static int updatewkey(String traceNo, String operatorNo);

	// 余额查询
	public native static int balanceInquiry(String traceNo, String track2,
			String track3, byte[] pinData, ICDataClass icData);

	// 消费
	public native static int sale(String price, String traceNo, String track2,
			String track3, byte[] pinData, ICDataClass icData);

	// 消费撤销
	public native static int saleVoid(String price, String traceNo,
			String track2, String track3, String originalReferencNo,
			String originalAuthorizationNo, byte[] pinData,
			String originalTraceNo, ICDataClass icData);

	// 退货
	public native static int refund(String price, String traceNo,
			String track2, String track3, String originalReferencNo,
			String originalAuthorizationNo, byte[] pinData,
			String originalTraceNo, String originalDate, ICDataClass icData);

	// 消费冲正
	public native static int saleReversal(String price, String originalTraceNo,
			String originalAuthorizationNo, String reversalNo,
			ICDataClass icData);

	// 消费撤销冲正
	public native static int saleVoidReversal(String price,
			String originalAuthorizationNo, String originalTraceNo,
			String reversalNo, ICDataClass icData);

	// 预授权
	public native static int preAuthorization(String price, String traceNo,
			String track2, String track3, byte[] pinData, ICDataClass icData);

	// 预授权冲正
	public native static int preAuthorizationReversal(String price,
			String traceNo, String originalAuthorizationNo, String reversalNo,
			ICDataClass icData);

	// 追加预授权
	public native static int AdditionalPreAuthorization(String price,
			String traceNo, String track2, String track3,
			String originalAuthorizationNo, byte[] pinData,
			String originalTraceNo, String originalDate);

	// 追加预授权冲正
	public native static int AdditionalPreAuthorizationReversal(String price,
			String traceNo, String originalAuthorizationNo);

	// 预授权撤销
	public native static int preAuthorizationVoid(String price, String traceNo,
			String track2, String track3, String originalAuthorizationNo,
			byte[] pinData, String originalTraceNo, String originalDate,
			ICDataClass icData);

	// 预授权撤销冲正
	public native static int preAuthorizationVoidReversal(String price,
			String traceNo, String originalAuthorizationNo,
			String originalTraceNo, String originalDate, String reversalNo,
			ICDataClass icData);

	// 预授权完成(联机)
	public native static int preAuthorizationDoneOnline(String price,
			String traceNo, String track2, String track3,
			String originalAuthorizationNo, byte[] pinData,
			String originalTraceNo, String originalDate, ICDataClass icData);

	// 预授权完成(离线)
	public native static int preAuthorizationDoneOffline(String price,
			String traceNo, String track2, String track3,
			String originalAuthorizationNo, String originalTraceNo,
			String originalDate, ICDataClass icData);

	// 预授权完成(联机)冲正
	public native static int preAuthorizationDoneOnlineReversal(String price,
			String traceNo, String originalAuthorizationNo,
			String originalTraceNo, String originalDate, String reversalNo,
			ICDataClass icData);

	// 预授权完成撤销
	public native static int preAuthorizationDoneVoid(String price,
			String traceNo, String track2, String track3,
			String originalReferencNo, String originalAuthorizationNo,
			byte[] pinData, String originalTraceNo, String originalDate,
			ICDataClass icData);

	// 预授权完成撤销冲正
	public native static int preAuthorizationDoneVoidReversal(String price,
			String traceNo, String originalAuthorizationNo,
			String originalTraceNo, String originalDate, String reversalNo,
			ICDataClass icData);

	// IC卡脚本处理结果通知
	public native static int ICscriptNotification(String price, String traceNo,
			String originalReferencNo, String originalAuthorizationNo,
			String originalTraceNo, String originalDate, ICDataClass icData);

	// IC卡终端公钥下载到文件中
	public native static int downloadICPKToFile();

	// IC卡终端参数下载到文件中
	public native static int downloadICParamToFile();

	// IC卡终端公钥开始下载
	public native static int ICPKdownloadStart(int messageNum, byte[] field62);

	// IC卡终端公钥下载
	public native static int ICPKdownload(byte[] field62, int field62Size);

	// IC卡终端公钥下载结束
	public native static int ICPKdownloadFinish();

	// IC卡终端参数下载开始
	public native static int ICParamDownloadStart(int messageNum, byte[] field62);

	// IC卡终端参数下载
	public native static int ICParamDownload(byte[] field62, int field62Size);

	// IC卡终端参数下载开始结束
	public native static int ICParamDownloadFinish();

	/**
	 * 从服务器返回的交易时间
	 * 
	 * @return
	 */
	public native static byte[] getTime();

	/**
	 * 从服务器返回的交易日期
	 * 
	 * @return
	 */
	public native static byte[] getDate();

	/**
	 * 清算日期
	 * 
	 * @return
	 */
	public native static byte[] getSettlementDate();

	/**
	 * 参考号
	 * 
	 * @return
	 */
	public native static byte[] getRefenceNo();

	/**
	 * 授权码
	 * 
	 * @return
	 */
	public native static byte[] getAuthorizationNo();

	/**
	 * 返回码/应答码
	 * 
	 * @return
	 */
	public native static byte[] getAckNo();

	/**
	 * 域44，可以查询到发卡行和收单行
	 * 
	 * @return
	 */
	public native static byte[] getAdditionalData();

	/**
	 * 终端号
	 * 
	 * @return
	 */
	public native static byte[] getTerminalCode();

	/**
	 * 商户号
	 * 
	 * @return
	 */
	public native static byte[] getMerchantCode();

	/**
	 * 卡号
	 * 
	 * @return
	 */
	public native static byte[] getAccountNo();

	/**
	 * 交易金额
	 * 
	 * @return
	 */
	public native static byte[] getAmount();

	/**
	 * 凭证号
	 * 
	 * @return
	 */
	public native static byte[] getTraceNo();

	/**
	 * @param f55
	 * @return
	 */
	public native static int getField55(byte[] f55);

	/**
	 * 
	 * @return
	 */
	public native static byte[] getMessageType();

	/**
	 * 批次号
	 * 
	 * @return
	 */
	public native static byte[] getBatchNo();

	public native static byte[] getDatagramHead();

	public native static void setTerminalCode(byte terminalCode[]);

	public native static void setMerchantCode(byte merchantCode[]);

	public native static void setTPDU(String tpdu);

	public native static void setIP(String ip);

	public native static void setPort(short port);

	public native static void setNetworkTimeout(long sec, long millisec);

	public native static void setDatagramEncryptEnable(boolean b);

	public native static void setCurrencyCode(String currencyCode);

	public native static void setEnableLog(boolean e);

	/**
	 * IC卡终端公钥下载
	 * 
	 * @return 下载状态
	 */
	public static int downloadICPK() {
		int ret;
		// int messageNum = 0;
		messageNum = 0;
		byte[] field62;
		int field62Size;
		recordID = 1;
		Log.i("SafeInterface", "safe_storeCAPK enter");
		ret = SafeInterface.safe_storeCAPK(new byte[] { 0x00, 0x00, 0x00 }, 3,
				recordID++);
		Log.i("SafeInterface", "safe_storeCAPK exit " + ret);

		if (ret < 0) {
			return -20;
		}
		while (true) {
			field62 = new byte[1024];
			field62Size = 0;
			field62Size = ICPKdownloadStart(messageNum, field62);
			messageNum = 0;

			if (field62Size <= 0)
				return field62Size;

			hexDump("ICPKdownloadStart receive", field62, field62Size);

			// messageNum = (field62Size - 1) / 23; // 23为一组信息的长度: RID+索引+有效期

			if (field62[0] == '0') // 没有信息
			{
			} else if (field62[0] == '2') // 有后续部分信息
			{
				ret = ICPKGroupDownload(field62, field62Size);
				if (ret < 0)
					return ret;

				continue;
			} else if (field62[0] == '3' || field62[0] == '1') // 3:有最后一组信息;
																// 1:有后续所有信息
			{
				ret = ICPKGroupDownload(field62, field62Size);
				if (ret < 0)
					return ret;
			}
			break;
		}

		if (SafeInterface.safe_storeCAPK(new byte[] { 0x00, 0x01,
				(byte) (recordID - 2) }, 3, 1) < 0) {
			return -20;
		}
		return ICPKdownloadFinish();
	}

	/**
	 * IC卡终端参数下载
	 * 
	 * @return 下载状态
	 */
	public static int downloadICParam() {
		int ret;
		// int messageNum = 0;
		messageNum = 0;

		byte[] field62;
		int field62Size;

		recordID = 1;
		if (SafeInterface.safe_storeAID(new byte[] { 0x00, 0x00, 0x00 }, 3,
				recordID++) < 0) {
			return -20;
		}

		while (true) {
			field62 = new byte[1024];
			field62Size = 0;

			field62Size = ICParamDownloadStart(messageNum, field62);
			messageNum = 0;

			if (field62Size <= 0)
				return field62Size;

			hexDump("ICParamDownloadStart receive", field62, field62Size);

			// messageNum = (field62Size - 1) / 11; // 10为一组信息的长度: AID

			if (field62[0] == '0') // 没有信息
			{
			} else if (field62[0] == '2') // 有后续部分信息
			{
				ret = ICParamGroupDownload(field62, field62Size);
				if (ret < 0)
					return ret;

				continue;
			} else if (field62[0] == '3' || field62[0] == '1') // 3:有最后一组信息;
																// 1:有后续所有信息
			{
				ret = ICParamGroupDownload(field62, field62Size);
				if (ret < 0)
					return ret;
			}
			break;
		}

		if (SafeInterface.safe_storeAID(new byte[] { 0x00, 0x01,
				(byte) (recordID - 2) }, 3, 1) < 0) {

			return -20;
		}
		return ICParamDownloadFinish();
	}

	@SuppressLint("NewApi")
	private static int ICPKGroupDownload(byte[] field62, int field62Size) {
		byte[] f62;
		int f62Size;

		for (int i = 1; i < field62Size; i += 23, messageNum++) {
			f62Size = 12; // 12为公钥RID和索引TLV数据长度
			f62 = Arrays.copyOfRange(field62, i, i + 1024);
			hexDump("ICPKdownload send", f62, f62Size);

			f62Size = ICPKdownload(f62, f62Size);

			Log.e("f62Size", String.valueOf(f62Size));
			if (f62Size < 0)
				return f62Size;

			hexDump("ICPKdownload receive", f62, f62Size);

			if (f62[0] == '1') {
				int ret = writeToSafeModule(f62, f62Size, 1);
				if (ret < 0)
					return ret;
			}
		}

		return 0;
	}

	@SuppressLint("NewApi")
	private static int ICParamGroupDownload(byte[] field62, int field62Size) {
		byte[] f62;
		int f62Size;
		int len;
		for (int i = 1; i < field62Size; i += len, messageNum++) {
			f62 = Arrays.copyOfRange(field62, i, i + 1024);
			f62Size = field62[i + 2] + 3; // 计算TLV的总长度
			len = f62Size;
			hexDump("ICParamDownload send", f62, f62Size);

			f62Size = ICParamDownload(f62, f62Size);
			if (f62Size < 0)
				return f62Size;

			hexDump("ICParamDownload receive", f62, f62Size);

			if (f62[0] == '1') {
				int ret = writeToSafeModule(f62, f62Size, 2);
				if (ret < 0)
					return ret;
			}
		}
		return 0;
	}

	@SuppressLint("NewApi")
	private static int writeToSafeModule(byte[] f62, int f62Size, int type) {
		int ret;
		f62 = Arrays.copyOfRange(f62, 1, f62Size); // 去除62域的头一个字节
		f62Size--;
		int maxBlockLength = 198;
		int maxBuffLength = 200;
		byte[] block;
		int blockSize = f62Size / maxBlockLength
				+ ((f62Size % maxBlockLength) > 0 ? 1 : 0); // 将62域分为blockSize块，每块不超过maxBlockLength
		Log.i("blockSize", String.valueOf(blockSize));

		if (blockSize <= 0)
			return -21;

		int j;
		for (j = 0; j < blockSize - 1; j++) {
			block = new byte[maxBuffLength];
			block[0] = 0x01; // 有后续块时，block第一个字节为0x01
			block[1] = (byte) maxBlockLength;
			System.arraycopy(f62, j * maxBlockLength, block, 2, maxBlockLength);
			Log.i("recordID", String.valueOf(recordID));
			if (type == 1) {
				hexDump("safe_storeCAPK send", block, maxBuffLength);
				ret = SafeInterface.safe_storeCAPK(block, maxBuffLength,
						recordID++);
			} else {
				hexDump("safe_storeAID send", block, maxBuffLength);
				ret = SafeInterface.safe_storeAID(block, maxBuffLength,
						recordID++);
			}
			if (ret < 0) {
				return -20;
			}
		}

		Log.i("recordID", String.valueOf(recordID));
		block = new byte[200];
		block[0] = 0x00; // 无后续块时，block第一个字节为0x00
		block[1] = (byte) (f62Size - j * maxBlockLength);
		Log.i("f62Size - j * maxBlockLength",
				String.format("value: %d", f62Size - j * maxBlockLength));
		Log.i("j", String.format("value: %d", j));
		System.arraycopy(f62, j * maxBlockLength, block, 2, f62Size - j
				* maxBlockLength);
		if (type == 1) {
			hexDump("safe_storeCAPK send", block, maxBuffLength);
			ret = SafeInterface
					.safe_storeCAPK(block, maxBuffLength, recordID++);
		} else {
			hexDump("safe_storeAID send", block, maxBuffLength);
			ret = SafeInterface.safe_storeAID(block, maxBuffLength, recordID++);
		}
		if (ret < 0) {
			return -20;
		}

		return 0;
	}

	private static void hexDump(String tag, byte[] buff, int buffLength) {
		Log.e("buffLength", String.valueOf(buffLength));
		if (buffLength < 0)
			return;

		StringBuffer sBuffer = new StringBuffer();
		for (int j = 0; j < buffLength; j++) {
			sBuffer.append(String.format("%02x ", buff[j]));
		}
		Log.i(tag, sBuffer.toString());
	}

	private static int messageNum;
	private static int recordID;
}