package com.kivvi.jni;

import java.util.Arrays;

import android.annotation.SuppressLint;
import android.util.Log;

/**
 * @author Scott
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
public class ISO8583Interface {

	public static class ICDataClass {
		public String PAN; // F2: 主账号
		public String dateOfExpired; // F14: 卡有效期
		public String cardSeqNo; // F23: 卡片序列号
		public byte[] f55;
		public int f55Length;
		public int flag;
	}

	static {
		System.loadLibrary("kivvi_iso8583");
	}

	/**
	 * 打开库
	 * @return
	 */
	public native static int open();

	/**
	 * 关闭库
	 * @return
	 */
	public native static int close();

	/**
	 * 下载主密钥
	 * 
	 * @return
	 */
	public native static int updatemkey();

	/**
	 * 下载工作密钥
	 * 
	 * @param traceNo
	 * @param operatorNo
	 * @return
	 */
	public native static int updatewkey();

	/**
	 * 签到
	 * @return
	 */
	public native static int checkIn(String traceNo);
	
	/**
	 * 签退
	 * @return
	 */
	public native static int checkOut(String traceNo);
	
	/**
	 * 批结算
	 * @return
	 */
	public native static int batchSettlement(String traceNo, byte[] settlementData);
	
	/**
	 * 批上送
	 * @return
	 */
	public native static int batchFeeding(String traceNo);

	/**
	 * 余额查询
	 * 
	 * @param traceNo
	 * @param track2
	 * @param track3
	 * @param pinData
	 * @param icData
	 * @return
	 */
	public native static int balanceInquiry(String traceNo, String track2, String track3,
			byte[] pinData, ICDataClass icData);

	/**
	 * 消费
	 * 
	 * @param price
	 * @param traceNo
	 * @param track2
	 * @param track3
	 * @param pinData
	 * @param icData
	 * @return
	 */
	public native static int sale(String traceNo, String batchNo, String price, String track2, String track3,
			byte[] pinData, ICDataClass icData);

	/**
	 * 消费撤销
	 * 
	 * @param price
	 * @param traceNo
	 * @param track2
	 * @param track3
	 * @param originalReferencNo
	 * @param originalAuthorizationNo
	 * @param pinData
	 * @param originalTraceNo
	 * @param icData
	 * @return
	 */
	public native static int saleVoid(String traceNo, String price, String track2,
			String track3, String originalReferencNo,
			String originalAuthorizationNo, byte[] pinData,
			String originalTraceNo, String originalBatchNo, ICDataClass icData);

	/**
	 * 退货
	 * @param price
	 * @param track2
	 * @param track3
	 * @param originalReferencNo
	 * @param originalAuthorizationNo
	 * @param pinData
	 * @param originalTraceNo
	 * @param originalDate
	 * @param icData
	 * @return
	 */
	public native static int refund(String traceNo, String price, String track2, String track3,
			String originalReferencNo, String originalAuthorizationNo,
			byte[] pinData, String originalTraceNo, String originalDate,
			ICDataClass icData);

	/**
	 * 消费冲正
	 * @param price
	 * @param originalAuthorizationNo
	 * @param reversalNo
	 * @param icData
	 * @return
	 */
	public native static int saleReversal(String originalTraceNo, String price,
			String originalAuthorizationNo, String reversalNo,
			ICDataClass icData);

	/**
	 * 消费撤销冲正
	 * @param price
	 * @param originalAuthorizationNo
	 * @param originalTraceNo
	 * @param reversalNo
	 * @param icData
	 * @return
	 */
	public native static int saleVoidReversal(String originalTraceNo, String price,
			String originalAuthorizationNo, String originalBatchNo, String reversalNo, ICDataClass icData);

//	/**
//	 * 预授权
//	 * @param price
//	 * @param track2
//	 * @param track3
//	 * @param pinData
//	 * @param icData
//	 * @return
//	 */
//	public native static int preAuthorization(String price, String track2,
//			String track3, byte[] pinData, ICDataClass icData);
//
//	/**
//	 * 预授权冲正
//	 * @param price
//	 * @param originalAuthorizationNo
//	 * @param reversalNo
//	 * @param icData
//	 * @return
//	 */
//	public native static int preAuthorizationReversal(String price,
//			String originalAuthorizationNo, String reversalNo,
//			ICDataClass icData);
//
//	/**
//	 * 追加预授权
//	 * @param price
//	 * @param track2
//	 * @param track3
//	 * @param originalAuthorizationNo
//	 * @param pinData
//	 * @param originalTraceNo
//	 * @param originalDate
//	 * @return
//	 */
//	public native static int AdditionalPreAuthorization(String price,
//			String track2, String track3, String originalAuthorizationNo,
//			byte[] pinData, String originalTraceNo, String originalDate);
//
//	/**
//	 * 追加预授权冲正
//	 * @param price
//	 * @param originalAuthorizationNo
//	 * @return
//	 */
//	public native static int AdditionalPreAuthorizationReversal(String price,
//			String originalAuthorizationNo);
//
//	/**
//	 * 预授权撤销
//	 * @param price
//	 * @param track2
//	 * @param track3
//	 * @param originalAuthorizationNo
//	 * @param pinData
//	 * @param originalTraceNo
//	 * @param originalDate
//	 * @param icData
//	 * @return
//	 */
//	public native static int preAuthorizationVoid(String price, String track2,
//			String track3, String originalAuthorizationNo, byte[] pinData,
//			String originalTraceNo, String originalDate, ICDataClass icData);
//
//	/**
//	 * 预授权撤销冲正
//	 * @param price
//	 * @param originalAuthorizationNo
//	 * @param originalTraceNo
//	 * @param originalDate
//	 * @param reversalNo
//	 * @param icData
//	 * @return
//	 */
//	public native static int preAuthorizationVoidReversal(String price,
//			String originalAuthorizationNo, String originalTraceNo,
//			String originalDate, String reversalNo, ICDataClass icData);
//
//	/**
//	 * 预授权完成，联机
//	 * @param price
//	 * @param track2
//	 * @param track3
//	 * @param originalAuthorizationNo
//	 * @param pinData
//	 * @param originalTraceNo
//	 * @param originalDate
//	 * @param icData
//	 * @return
//	 */
//	public native static int preAuthorizationDoneOnline(String price,
//			String track2, String track3, String originalAuthorizationNo,
//			byte[] pinData, String originalTraceNo, String originalDate,
//			ICDataClass icData);
//
//	/**
//	 * 预授权完成，离线
//	 * @param price
//	 * @param track2
//	 * @param track3
//	 * @param originalAuthorizationNo
//	 * @param originalTraceNo
//	 * @param originalDate
//	 * @param icData
//	 * @return
//	 */
//	public native static int preAuthorizationDoneOffline(String price,
//			String track2, String track3, String originalAuthorizationNo,
//			String originalTraceNo, String originalDate, ICDataClass icData);
//
//	/**
//	 * 预授权完成(联机)冲正
//	 * @param price
//	 * @param originalAuthorizationNo
//	 * @param originalTraceNo
//	 * @param originalDate
//	 * @param reversalNo
//	 * @param icData
//	 * @return
//	 */
//	public native static int preAuthorizationDoneOnlineReversal(String price,
//			String originalAuthorizationNo, String originalTraceNo,
//			String originalDate, String reversalNo, ICDataClass icData);
//
//	/**
//	 * 预授权完成撤销
//	 * @param price
//	 * @param track2
//	 * @param track3
//	 * @param originalReferencNo
//	 * @param originalAuthorizationNo
//	 * @param pinData
//	 * @param originalTraceNo
//	 * @param originalDate
//	 * @param icData
//	 * @return
//	 */
//	public native static int preAuthorizationDoneVoid(String price,
//			String track2, String track3, String originalReferencNo,
//			String originalAuthorizationNo, byte[] pinData,
//			String originalTraceNo, String originalDate, ICDataClass icData);
//
//	/**
//	 * 预授权完成撤销冲正
//	 * @param price
//	 * @param originalAuthorizationNo
//	 * @param originalTraceNo
//	 * @param originalDate
//	 * @param reversalNo
//	 * @param icData
//	 * @return
//	 */
//	public native static int preAuthorizationDoneVoidReversal(String price,
//			String originalAuthorizationNo, String originalTraceNo,
//			String originalDate, String reversalNo, ICDataClass icData);

	/**
	 * IC卡脚本处理结果通知
	 * @param price
	 * @param originalReferencNo
	 * @param originalAuthorizationNo
	 * @param originalTraceNo
	 * @param originalDate
	 * @param icData
	 * @return
	 */
	public native static int ICscriptNotification(String price,
			String originalReferencNo, String originalAuthorizationNo,
			String originalTraceNo, String originalDate, ICDataClass icData);

	/**
	 * TMS请求接口
	 * @param orderCode
	 * @return
	 */
	public native static int tmsRequest(String orderCode);
	
	/**
	 * TMS下载接口
	 * @param orderCode
	 * @param type
	 * @param startPos
	 * @param downloadLen
	 * @return
	 */
	public native static int tmsDownload(String orderCode, int type, int startPos, int downloadLen);
	
	/**
	 * TMS通知接口
	 * @param orderCode
	 * @return
	 */
	public native static int tmsNotify(String orderCode);
	
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
	 * 获取时间
	 * @return
	 */
	public native static byte[] getTime();

	/**
	 * 获取日期
	 * @return
	 */
	public native static byte[] getDate();

	/**
	 * 获取结算日期
	 * @return
	 */
	public native static byte[] getSettlementDate();

	public native static byte[] getRefenceNo();

	/**
	 * 获取授权码
	 * @return
	 */
	public native static byte[] getAuthorizationNo();

	/**
	 * 获取响应码
	 * @return
	 */
	public native static byte[] getAckNo();
	
	public native static byte[] getAdditionalData();

	/**
	 * 获取终端号
	 * @return
	 */
	public native static byte[] getTerminalCode();

	/**
	 * 获取商户号
	 * @return
	 */
	public native static byte[] getMerchantCode();

	public native static byte[] getAccountNo();

	/**
	 * 获取交易金额
	 * @return
	 */
	public native static byte[] getAmount();

	/**
	 * 获取流水号
	 * @return
	 */
	public native static byte[] getTraceNo();

	/**
	 * 获取IC卡55域数据
	 * @param f55
	 * @return
	 */
	public native static int getField55(byte[] f55);

	/**
	 * 获取消息类型
	 * @return
	 */
	public native static byte[] getMessageType();

	/**
	 * 获取批次号
	 * @return
	 */
	public native static byte[] getBatchNo();

	public native static byte[] getDatagramHead();

	public native static void setTraceNo(String traceNo);

	/**
	 * 设置操作员号
	 * @param operator
	 */
	public native static void setOperator(String operator);

	/**
	 * 设置终端号
	 * @param terminalCode
	 */
	public native static void setTerminalCode(byte terminalCode[]);

	/**
	 * 设置商户号
	 * @param merchantCode
	 */
	public native static void setMerchantCode(byte merchantCode[]);

	/**
	 * 设置机构号
	 * @param orgCode
	 */
	public native static void setOrgCode(String orgCode);

	/**
	 * 设置TPDU
	 * @param tpdu
	 */
	public native static void setTPDU(String tpdu);

	/**
	 * 设置IP地址
	 * @param ip
	 */
	public native static void setIP(String ip);

	/**
	 * 设置端口号
	 * @param port
	 */
	public native static void setPort(short port);
	
	/**
	 * 设置TMS端口号
	 * @param port
	 */
	public native static void setTmsPort(short port);

	/**
	 * 设置网络超时时间
	 * @param sec
	 * @param millisec
	 */
	public native static void setNetworkTimeout(long sec, long millisec);

	public native static void setDatagramEncryptEnable(boolean b);

	/**
	 * 设置货比类型代码
	 * @param currencyCode
	 */
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
		SafeInterface.open();
		ret = 0;
		// ret = HsmInterface.safe_storeCAPK(new byte[] { 0x00, 0x00, 0x00 }, 3,
		// recordID++);
		SafeInterface.close();
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

		SafeInterface.open();
		// if (HsmInterface.safe_storeCAPK(new byte[] { 0x00, 0x01, (byte)
		// (recordID - 2) }, 3, 1) < 0) {
		// SafeInterface.safeClose();
		// return -20;
		// }
		SafeInterface.close();
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
		SafeInterface.open();
		// if (HsmInterface.safe_storeAID(new byte[] { 0x00, 0x00, 0x00 }, 3,
		// recordID++) < 0) {
		// SafeInterface.safeClose();
		// return -20;
		// }
		SafeInterface.close();
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
		SafeInterface.open();
		// if (HsmInterface.safe_storeAID(new byte[] { 0x00, 0x01, (byte)
		// (recordID - 2) }, 3, 1) < 0) {
		// SafeInterface.safeClose();
		// return -20;
		// }
		SafeInterface.close();
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
		int ret = 0;
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
				SafeInterface.open();
				// ret = HsmInterface.safe_storeCAPK(block, maxBuffLength,
				// recordID++);
				SafeInterface.close();
			} else {
				hexDump("safe_storeAID send", block, maxBuffLength);
				SafeInterface.open();
				// ret = HsmInterface.safe_storeAID(block, maxBuffLength,
				// recordID++);
				SafeInterface.close();
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
			SafeInterface.open();
			// ret = HsmInterface.safe_storeCAPK(block, maxBuffLength,
			// recordID++);
			SafeInterface.close();
		} else {
			hexDump("safe_storeAID send", block, maxBuffLength);
			SafeInterface.open();
			// ret = HsmInterface.safe_storeAID(block, maxBuffLength,
			// recordID++);
			SafeInterface.close();
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