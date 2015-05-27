package com.cynovo.par10h6;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;

import android.graphics.Bitmap;

public class QRCode {

	/**
	 * 创建条形码
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Bitmap createOneQRCode(String content) throws Exception {
		// 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = new MultiFormatWriter().encode(content,
				BarcodeFormat.CODE_128, 500, 200);

		// 矩阵的宽度
		int width = matrix.getWidth();

		// 矩阵的高度
		int height = matrix.getHeight();

		// 矩阵像素数组
		int[] pixels = new int[width * height];

		// 双重循环遍历每一个矩阵点
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					// 设置矩阵像素点的值
					pixels[y * width + x] = 0xFF000000;
				}
			}
		}

		// 根据颜色数组来创建位图
		// 此函数创建位图的过程可以简单概括为为:更加width和height创建空位图，
		// 然后用指定的颜色数组colors来从左到右从上至下一次填充颜色。
		// config是一个枚举，可以用它来指定位图“质量”。
		Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		// 通过像素数组生成bitmap,具体参考api
		bm.setPixels(pixels, 0, width, 0, 0, width, height);

		// 将生成的条形码返回给调用者
		return bm;
	}

	/**
	 * 创建二维码
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Bitmap createTwoQRCode(String content) throws Exception {
		// 生成二维码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
		BitMatrix matrix = new MultiFormatWriter().encode(content,
				BarcodeFormat.QR_CODE, 300, 300);

		// 矩阵的宽度
		int width = matrix.getWidth();

		// 矩阵的高度
		int height = matrix.getHeight();

		// 矩阵像素数组
		int[] pixels = new int[width * height];

		// 双重循环遍历每一个矩阵点
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					// 设置矩阵像素点的值
					pixels[y * width + x] = 0xFF000000;
				}
			}
		}

		// 根据颜色数组来创建位图
		// 此函数创建位图的过程可以简单概括为为:更加width和height创建空位图，
		// 然后用指定的颜色数组colors来从左到右从上至下一次填充颜色。
		// config是一个枚举，可以用它来指定位图“质量”。
		Bitmap bm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		// 通过像素数组生成bitmap,具体参考api
		bm.setPixels(pixels, 0, width, 0, 0, width, height);

		// 将生成的条形码返回给调用者
		return bm;
	}
}