package com.cynovo.sirius.devices.printer;

import java.io.UnsupportedEncodingException;

import android.util.Log;
//import com.cynovo.sirius.jni.PrinterInterface;

//import com.cynovo.jni.PrinterInterface;
import com.kivvi.jni.PrinterInterface;

public class SPRTPrinter extends PrinterBase {

	private static final byte[] LF = { 0x0A };
	private static final byte[] OPENCASHBOX = { 0x1B, 0x70, 0x00, (byte) 0xC8,
			(byte) 0xC8 };
	byte[] SIZEMODE = { 0x1D, 0x21, 0x00 };
	byte[] PRINTIMAGE = { 0x1B, 0x2A, 0x21, 0x00, 0x00 };
	byte[] PRINTFEED = { 0x1B, 0x4A, 0x18 };
	byte[] PRINTFEED2 = { 0x1D, 0x2f, 0x00 };
	byte[] X0A = { 0x0A };
	byte[] BETWEEN1 = { 0x1B, 0x32 };
	byte[] BETWEEN2 = { 0x1B, 0x33, 0x00 };
	byte[] QUERYPRINTERSENCER = { 0x10, 0x04, 0x04 };

	private byte PRINTTYPE = PRINT1;

	public static final int HAVE_PAPER = 0;
	public static final int NO_PAPER = 0;

	public int isPrintOK() {
		if (getIsOpen() != OPEN)
			return -1;
		return 0;
	}

	public int isPrinterUsable() {
		if (getIsOpen() != OPEN) {
			Log.d("PRINTER", "=======================PRINTER NOT OPEN");
			return -1;
		}
		byte[] reSt = new byte[1];
		reSt[0] = -100;
		int count = 0;
		while (reSt[0] == -100) {
			if (++count > 3)
				break;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			// modified by
			// wanhaiping------------------------------------------------------>begin
			// PrinterInterface.write(PRINTTYPE, QUERYPRINTERSENCER,
			// QUERYPRINTERSENCER.length);
			// PrinterInterface.read(PRINTTYPE, reSt, reSt.length);

			PrinterInterface.set(0);
			PrinterInterface.write(QUERYPRINTERSENCER,
					QUERYPRINTERSENCER.length);

			// modified by
			// wanhaiping-------------------------------------------------------<end
		}
		Log.d("PRINTER", "=======================PRINTER CODE:" + reSt[0]);
		if ((int) reSt[0] == 0x12 || (int) reSt[0] == -100)
			return 0;
		else {
			return -1;
		}
	}

	public void setMode(byte mode) {
		byte[] tmp = SIZEMODE.clone();
		tmp[2] = mode;
		if (isPrintOK() < 0)
			return;
		write(tmp, tmp.length);
	}

	public void PrintLineStr(String str) {
		if (isPrintOK() < 0)
			return;
		if (str == null || str.isEmpty())
			return;
		try {
			byte[] tmp = str.getBytes("gb2312");
			write(tmp, tmp.length);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	public void PrintLineFeed() {
		if (isPrintOK() < 0)
			return;
		write(LF, LF.length);
	}

	public void openCashBox() {
		if (isOpen != OPEN) {
			openPrinter();
		}
		if (isPrintOK() < 0)
			return;
		write(OPENCASHBOX, OPENCASHBOX.length);
	}

	public void printImage(byte[] pic, int length, int offset) {
		if (isPrintOK() < 0)
			return;
		byte[] tmpArray = new byte[length + 5];
		for (int i = 0; i < length; i++) {
			tmpArray[i + 5] = pic[offset + i];
		}

		tmpArray[0] = PRINTIMAGE[0];
		tmpArray[1] = PRINTIMAGE[1];
		tmpArray[2] = PRINTIMAGE[2];

		byte len = (byte) (length / 3);
		if (len > 255)
			len = (byte) 255;
		tmpArray[3] = len;
		tmpArray[4] = PRINTIMAGE[4];

		Log.d("------------...>>>", "" + length / 3 + "     "
				+ (byte) (length / 3));

		write(tmpArray, tmpArray.length);
		write(BETWEEN2, BETWEEN2.length);
		write(X0A, X0A.length);
		write(BETWEEN1, BETWEEN1.length);
	}

	public void printTipTest() {
		byte[] printInit = new byte[] { 0x1b,
				0x40,// init print
				0x1b, 0x57, 0x00, 0x00, 0x00, 0x00, (byte) 0xC0, 0x01,
				(byte) 0xf0, 0x00,// set print zone
				0x18, 0x1B, 0x54, 0x00,// set print orientation
				0x1B, 0x24, 0x20, 0x00,// horizontal location
				0x1D, 0x24, 0x00, 0x00, // landscape location
		};
		byte[] printClear = new byte[] { 0x1B, 0x24, (byte) 0xD0, 0x00, 0x1D,
				0x24, (byte) 0x90, 0x00, 0x1B, 0x24, (byte) 0xD0, 0x00, 0x1D,
				0x24, (byte) 0xB0, 0x00, 0x0C // 打印并清除缓存
		};

		// modified by
		// wanhaiping------------------------------------------------------>begin
		// PrinterInterface.write(PRINT2, printInit, printInit.length);
		// String str ="CYNOVO IMBAK";
		// PrinterInterface.write(PRINT2, str.getBytes(),str.getBytes().length);
		// PrinterInterface.write(PRINT2, printClear, printClear.length);

		PrinterInterface.set(1);
		PrinterInterface.write(printInit, printInit.length);
		String str = "CYNOVO IMBAK";
		PrinterInterface.write(str.getBytes(), str.getBytes().length);
		PrinterInterface.write(printClear, printClear.length);

		// modified by
		// wanhaiping-------------------------------------------------------<end

	}

	private int write(byte[] arryData, int nDataLength) {
		// modified by
		// wanhaiping------------------------------------------------------>begin
		// PrinterInterface.write(PRINTTYPE,arryData, nDataLength);

		PrinterInterface.set(0);
		PrinterInterface.write(arryData, nDataLength);
		// modified by
		// wanhaiping-------------------------------------------------------<end
		return 0;
	}

}
