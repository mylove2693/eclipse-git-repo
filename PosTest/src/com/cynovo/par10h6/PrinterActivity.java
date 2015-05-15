package com.cynovo.par10h6;


import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.Build;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;

import java.io.IOException;  
import java.net.Inet4Address;  
import java.net.InterfaceAddress;  
import java.net.NetworkInterface;  
import java.util.Enumeration; 

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.kivvi.jni.PrinterInterface;

public class PrinterActivity extends Activity implements OnClickListener{
		private static Button printer1 = null;
		private static Button printer2 = null;
		private static Button printer3 = null;
		private static Button open = null;
		private static Button close = null;
		private static TextView usb1 = null;	
		private static TextView usb2 = null;	
		private static TextView serial = null;	
		
		private static final int VSIZE = 3;
		private static final int pixPerByte = 8;
		
		private static byte[] PRINTIMAGE = {0x1B , 0x2A, 0x00 ,0x00,0x00};
		private static byte[] BETWEEN1 = {0x1B,0x32};
		private static byte[] BETWEEN2 = {0x1B,0x33 ,0x00};
		private static byte[] X0A = {0x0A};
		
		private static int depth = 200;		
		private static int time = 0;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_printer);
			printer1 = (Button) findViewById(R.id.printer01);
			printer2 = (Button) findViewById(R.id.printer02);
			open 	 = (Button) findViewById(R.id.open);
			close 	 = (Button) findViewById(R.id.close);
			usb1 	 = (TextView) findViewById(R.id.usb1);
			usb2 	 = (TextView) findViewById(R.id.usb2);

			printer1.setOnClickListener(this);									
			printer2.setOnClickListener(this);	
			open.setOnClickListener(this);
			close.setOnClickListener(this);									
		}
		public void openPrinter() {
			int nResult;
			nResult = PrinterInterface.open();
			Log.v("printer","open nResult = " + nResult + "\n");
			if (nResult < 0)
				return;
			if((nResult & 0x01)  == 0x01){
				usb1.setText("usb1 printer open success");
				usb1.setTextColor(android.graphics.Color.BLUE);
			}
			else{
				usb1.setText("usb1 printer open fail");
				usb1.setTextColor(android.graphics.Color.RED);
			}
			
			if((nResult & 0x02)  == 0x02){
				usb2.setText("usb2 printer open success");
				usb2.setTextColor(android.graphics.Color.BLUE);
			}
			else{
				usb2.setText("usb2 printer open fail");
				usb2.setTextColor(android.graphics.Color.RED);
			}			

		}

		public void setPrinter(int printer) {
			int nResult;
			nResult = PrinterInterface.set(printer);
			Log.v("printer","set nResult = " + nResult + "\n");		
			
			if (nResult < 0) {
				return;
			}
		}

		public void closePrinter() {
			int nResult;
			usb1.setText("");
			usb2.setText("");
			nResult = PrinterInterface.close();
			Log.v("printer","close nResult = " + nResult + "\n");		
			
			if (nResult < 0) {
				return;
			}
		}
		void open_click(){
			openPrinter();
		}
		void close_click(){
			closePrinter();
		}	
		void printer01_click(){
			setPrinter(1);
			 print();	
		}
		void printer02_click(){
			setPrinter(2);
			 print();	
		}

		void print(){
			int nResult = PrinterInterface.begin();
			Log.v("TAG","\n\n\n begin return ret = " + nResult + " \n\n\n");
			int ret = 0;

			byte[] dev = null;
			byte[] dev_null = null;		
			byte[] CMD_SetDEFAULT = {0x1b,0x40};
			int len = 0;
			Drawable mDrawable = this.getResources().getDrawable(R.drawable.zfb);
			Bitmap mBitMap = ((BitmapDrawable)mDrawable).getBitmap();
			
			byte[] point = {0x1d,0x48,0x01};
			byte[] height = {0x1d,0x68,0x40};
			byte[] weight = {0x1d,0x77,0x03};
			byte[] font_size = {0x1d,0x66,0x00};
		        
			byte[] code_UPCA={0x1d,0x6b,0x41,0x0c,0x31,0x32,0x33,0x34,0x35,0x31,0x32,0x33,0x34,0x35,0x31,0x32}; 
			byte[] code_UPCB={0x1d,0x6b,0x42,0x0c,0x30,0x32,0x33,0x34,0x35,0x31,0x32,0x33,0x34,0x35,0x31,0x32}; 
			
			
			try {
				dev = "test cynovo\n\n\n\n\n".getBytes("utf-8");
				dev_null = "\n".getBytes("utf-8");
				len = dev.length;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			time = 0;
			while(time < 1){
				PrinterInterface.write(CMD_SetDEFAULT,2);
				
				PrinterInterface.write(point,point.length);
				PrinterInterface.write(height,height.length);
				PrinterInterface.write(weight,weight.length);
				PrinterInterface.write(font_size,font_size.length);
				
				ret = PrinterInterface.write(code_UPCA,code_UPCA.length);
				ret = PrinterInterface.write(dev_null,dev_null.length);

				printBitMap(Bitmap.createScaledBitmap(mBitMap, 256, 80, false));
				ret = PrinterInterface.write(dev_null,dev_null.length);

				ret = PrinterInterface.write(dev,len);
				ret = PrinterInterface.write(dev_null,dev_null.length);
				ret = PrinterInterface.write(dev_null,dev_null.length);
				ret = PrinterInterface.write(dev_null,dev_null.length);
				ret = PrinterInterface.write(dev_null,dev_null.length);			

				time ++;
			}

			
			nResult = PrinterInterface.end();
			Log.v("printer","end nResult = " + nResult + "\n");	
				
		}
		

			public static void printBitMap(Bitmap mBitmap)
			{
				Bitmap mTmpBitmap = null;
				boolean singlemode = false;
				int bmpWidth = mBitmap.getWidth();
				if( bmpWidth > 0 && bmpWidth < 256 ){
					singlemode = true;
					mTmpBitmap = mBitmap;//Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth(), mBitmap.getHeight(), false);
				}
				else if( bmpWidth >=256 && bmpWidth <=390 ){
					mTmpBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.getWidth()/2, mBitmap.getHeight(), false);
				}
				else if( bmpWidth >390 ){
					mTmpBitmap = Bitmap.createScaledBitmap(mBitmap, 390/2, mBitmap.getHeight(), false);
				}
			
				boolean[][] boolArray = bitmapToArray(mTmpBitmap);
				mTmpBitmap.recycle();
//				dumpBoolArray(boolArray);
				int width = boolArray.length;
				if(width < 1)
					return ;
				int height = boolArray[0].length;
				int batch = (height/(VSIZE*pixPerByte))+1;	
				byte[] printArray = boolArrayToPrintArray(boolArray);
				PrinterInterface.write(BETWEEN2, BETWEEN2.length);
				for(int i = 0 ; i < batch ; i++)
				{
					printImage(printArray,VSIZE*width,VSIZE*width*i,singlemode);
					PrinterInterface.write(X0A,X0A.length);
				}
				PrinterInterface.write(BETWEEN1, BETWEEN1.length);
			}
			
			private static boolean[][] bitmapToArray(Bitmap mBitmap)
			{
				return bitmapToArray( mBitmap,depth );
			}
			
			private static boolean[][] bitmapToArray(Bitmap mBitmap,int thresholdvalue)
			{
				int width = mBitmap.getWidth();
				int height = mBitmap.getHeight();       
				boolean[][] imageArray = new boolean[width][height];
				for(int i=0;i < height ;i++)
				{
					for(int j=0; j < width;j++)
					{
						int color = mBitmap.getPixel(j, i);
					
						int r = Color.red(color);
						int g = Color.green(color);
						int b = Color.blue(color);
						int a = Color.alpha(color);
						if(a < 170)
						{
							imageArray[j][i] = false;
						}
						else
						{
							int ret = ((int)(0.7 * r) + (int)(0.2 * g)+ (int)(0.1 * b));
							boolean tmp = (thresholdvalue >= ret);
							imageArray[j][i] = tmp;
						}
					}
				}
				return imageArray;
			}
			
			private static byte[]  boolArrayToPrintArray(boolean[][] boolArray)
			{
				
				if(boolArray.length < 1)
					return null;
				int width = boolArray.length;
				int height = boolArray[0].length;
				int batch = (height/(VSIZE*pixPerByte))+1;
				if(height%(VSIZE*pixPerByte) ==0)
					batch--;
				byte[] printArray = new byte[batch*width*VSIZE];

				for(int i = 0; i < printArray.length ; i++)
				{
					printArray[i] = 0;
				}
				
				for(int i = 0; i < width ; i++)
				{
					for(int j = 0 ; j < height ; j++)
					{
						int index = VSIZE*i+(j/pixPerByte)%VSIZE+(j/VSIZE/pixPerByte)*VSIZE*width;
						if(i == 0)
							Log.d("",""+(j/VSIZE/pixPerByte)+"  "+(j/VSIZE/pixPerByte)*VSIZE*width);
						if(boolArray[i][j])
						{
							printArray[index] <<= 1;
							printArray[index] |= 0x01;
						}
						else
						{
							printArray[index] <<= 1;
							printArray[index] &= 0xfe;
						}
					}
				}
				
				return printArray;
			}
			
			public static void printImage(byte[] pic, int length,int offset,boolean mode)
			{
				int ret = 0;
				byte[] tmpArray = new byte[length+5];
				for(int i = 0; i < length ; i++)
				{
					tmpArray[i+5] = pic[offset+i];
				}
				
				tmpArray[0] = PRINTIMAGE[0];
				tmpArray[1] = PRINTIMAGE[1];
				if(mode)
					tmpArray[2] = 0x21;
				else {
					tmpArray[2] = 0x20;
				}
				byte len = (byte)(length/3);
				if(len > 255)
					len = (byte)255;
				tmpArray[3] = len;
				tmpArray[4] = PRINTIMAGE[4];
				ret = PrinterInterface.write(tmpArray, tmpArray.length);
				Log.v("printer app","photo ret = " + ret +"\n");

			}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.printer01:
				printer01_click();
				break;
			case R.id.printer02:
				printer02_click();
				break;
			case R.id.open:
				open_click();
				break;
			case R.id.close:
				close_click();
				break;			
			default:
				break;
			}		
		}

	}
