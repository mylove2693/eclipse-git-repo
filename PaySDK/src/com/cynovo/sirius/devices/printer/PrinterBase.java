package com.cynovo.sirius.devices.printer;

//import com.cynovo.sirius.jni.PrinterInterface;
//import com.cynovo.jni.PrinterInterface;

import com.kivvi.jni.PrinterInterface;

public abstract class PrinterBase {

	public static final int OPEN = 0;
	public static final int CLOSE = 1;
	public static final int ERROR = 2;

	public static final byte NORMAL_SIZE = 0x00;
	public static final byte DOUBLE_WIDTH = 0x10;
	public static final byte DOUBLE_HEIGHT = 0x01;
	public static final byte DOUBLE_WIDTHANDHEIGHT = 0x11;

	public static final byte PRINT1 = 0x01;
	public static final byte PRINT2 = 0x02;
	public static final byte PRINT3 = 0x04;
	public static final byte PRINT4 = 0x08;
	public static final byte PRINT5 = 0x10;
	public static final byte PRINT6 = 0x20;
	public static final byte PRINT7 = 0x30;
	public static final byte PRINT8 = 0x40;

	static byte[] status = new byte[8];

	int isOpen = OPEN;

	public void openPrinter() {
		// modified bu wanhaiping -------------------->begin
		/*
		 * int nResult; nResult = PrinterInterface.open(status); if(nResult <
		 * 0){ isOpen = CLOSE; return ; } if(status[0] <= 0) { isOpen = CLOSE;
		 * PrinterInterface.close(); return ; } nResult =
		 * PrinterInterface.begin(); if(nResult < 0){ isOpen = CLOSE; return ; }
		 * isOpen = OPEN;
		 */

		int nResult;
		nResult = PrinterInterface.open();
		if (nResult < 0) {
			isOpen = CLOSE;
			return;
		}

		nResult = PrinterInterface.begin();
		if (nResult < 0) {
			isOpen = CLOSE;
			return;
		}
		isOpen = OPEN;

		// modified bu wanhaiping--------------------------------<end
	}

	public int getPrinterStatus() {
		// modified bu wanhaiping -------------------->begin
		// return (int) status[0];
		if (isOpen == CLOSE) {
			return 0;
		} else {
			return 1;
		}

		// modified bu
		// wanhaiping---------------------------------------------<end
	}

	public int isPrinterUsable() {
		return 0;
	}

	public void closePrinter() {
		// modified bu wanhaiping -------------------->begin
		/*
		 * int nResult; nResult = PrinterInterface.end(); if(nResult < 0){
		 * isOpen = ERROR; return ; } nResult = PrinterInterface.close();
		 * if(nResult < 0){ isOpen = ERROR; return ; } isOpen = CLOSE;
		 */

		int nResult;
		nResult = PrinterInterface.end();
		if (nResult < 0) {
			isOpen = ERROR;
			return;
		}
		nResult = PrinterInterface.close();
		if (nResult < 0) {
			isOpen = ERROR;
			return;
		}
		isOpen = CLOSE;
		// modified bu
		// wanhaiping---------------------------------------------<end

	}

	public int getIsOpen() {
		return isOpen;
	}

	public void setIsOpen(int isOpen) {
		this.isOpen = isOpen;
	}

	public abstract void setMode(byte mode);

	public abstract void PrintLineStr(String str);

	public abstract void PrintLineFeed();

	public abstract void openCashBox();

	public abstract void printImage(byte[] pic, int length, int offset);

}
