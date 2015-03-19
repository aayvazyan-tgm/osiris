package com.welcu.android.zxingfragmentlib;

import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.BitmapFactory.Options;
import android.os.Environment;
import android.util.Log;

public class ImageHelper {

	public static String scanPhotoTempName = "scan_temp.jpg";
	  
	public static void resizeRotateAndSaveByteArrayToSDCardPath(String desiredName, byte[] data, float targetWidth, float targetHeight) {
		/** GET bitmap info **/
		BitmapFactory.Options opts = new Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, opts);

		/** scale approximately **/
		opts.inJustDecodeBounds = false;
		opts.inSampleSize = calculateInSampleSize(opts, Math.round(targetWidth), Math.round(targetHeight));
		Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length, opts);
		data = null;
		
		/** calculate final WIDTH and HEIGHT (with right aspect ratio) **/
		int originalWidth = bmp.getWidth();
		int originalHeight = bmp.getHeight();
		float scale = (float) targetWidth / (float) originalWidth;
		int newWidth = Math.round(targetWidth);
		int newHeight = Math.round(scale * (float) originalHeight);

		/** resize exactly to desired size **/
		Bitmap scaledBmp = Bitmap.createScaledBitmap(bmp, newWidth, newHeight, true);
		/** rotating **/
		Matrix mtx = new Matrix();
		mtx.preRotate(90);
		/** saving **/
		saveToSDCardPath(desiredName, Bitmap.createBitmap(scaledBmp, 0, 0, scaledBmp.getWidth(), scaledBmp.getHeight(), mtx, true));
	}
  
  	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		return calculateInSampleSize(options, reqWidth, reqHeight, false);
	}
  	
  	private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight, boolean fitInside) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			if (fitInside) {
				inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
			} else {
				inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;				
			}
		}
		return inSampleSize;
	}
  	
  	public static String saveToSDCardPath(String fileName, Bitmap myBitmap) {
		String response = saveTo(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator, fileName, myBitmap);
		Log.d("NK", "FILE SAVED : "+response);
		return response;
	}
  	
  	public static String saveTo(String filePathString, String fileNameString, Bitmap myBitmap) {
		File filePath = new File(filePathString);
		filePath.mkdirs();
		File file = new File(filePathString, fileNameString);
		try {
			FileOutputStream out = new FileOutputStream(file);
			myBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
			out.flush();
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return file.getAbsolutePath();
	}

	
}
