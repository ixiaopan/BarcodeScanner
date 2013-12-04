/*
 * Copyright 2013 Peng fei Pan
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.xiaopan.easy.barcode;

import java.util.EnumMap;
import java.util.Map;
import java.util.Vector;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;

/**
 * 解码器
 */
public class Decoder implements DecodeListener{
	private Camera.Size cameraPreviewSize;	//相机预览尺寸
	private Rect scanningAreaRect;	//扫描框相对于预览界面的矩形
	private MultiFormatReader multiFormatReader;	//解码读取器
	private ResultPointCallback resultPointCallback;	//结果可疑点回调对象
	private DecodeListener decodeListener;	//解码监听器
	private DecodeThread decodeThread;	//解码线程
	private boolean isPortrait;	//是否是竖屏
	private boolean running = true;	//运行中
	private Handler handler = new Handler();
	
	public Decoder(Context context, Camera.Size cameraPreviewSize, Rect scanningAreaRect, Map<DecodeHintType, Object> hints, String charset){
		this.cameraPreviewSize = cameraPreviewSize;
		this.scanningAreaRect = scanningAreaRect;
		isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
		
		if(hints == null){
			hints = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
		}
		
		if(!hints.containsKey(DecodeHintType.POSSIBLE_FORMATS)){
			Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>(3);
			decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
			decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
			decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
			hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
		}
		
		if(!hints.containsKey(DecodeHintType.CHARACTER_SET)){
			if(charset != null && !"".equals(charset.trim())){
				hints.put(DecodeHintType.CHARACTER_SET, charset);
			}else{
				hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
			}
		}
		
		hints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, new ResultPointCallback() {
			@Override
			public void foundPossibleResultPoint(ResultPoint arg0) {
				if(getResultPointCallback() != null){
					getResultPointCallback().foundPossibleResultPoint(arg0);
				}
			}
		});
		
		multiFormatReader = new MultiFormatReader();
		multiFormatReader.setHints(hints);
		decodeThread = new DecodeThread(this, this);
		decodeThread.start();
	}
	
	/**
	 * 解码
	 * @param sourceData 源数据
	 */
	public void decode(byte[] sourceData) {
		if(running){
			decodeThread.tryDecode(sourceData);
		}
	}
	
	/**
	 * 暂停解码
	 */
	public void pause(){
		running = false;
		decodeThread.pause();
	}
	
	/**
	 * 恢复解码
	 */
	public void resume(){
		running = true;
	}
	
	/**
	 * 释放，请务必在Activity的onDestory()中调用此方法来释放Decoder所拥用的线程
	 */
	public void release(){
		pause();
		decodeThread.finish();
	}

	@Override
	public void onDecodeSuccess(final Result result, final byte[] bitmapByteArray, final float scaleFactor) {
		if(decodeListener != null){
			handler.post(new Runnable() {
				@Override
				public void run() {
					decodeListener.onDecodeSuccess(result, bitmapByteArray, scaleFactor);
				}
			});
		}
	}

	@Override
	public void onDecodeFailure() {
		if(decodeListener != null){
			handler.post(new Runnable() {
				@Override
				public void run() {
					decodeListener.onDecodeFailure();
				}
			});
		}
	}

	public ResultPointCallback getResultPointCallback() {
		return resultPointCallback;
	}

	public void setResultPointCallback(ResultPointCallback resultPointCallback) {
		this.resultPointCallback = resultPointCallback;
	}

	public DecodeListener getDecodeListener() {
		return decodeListener;
	}

	public void setDecodeListener(DecodeListener decodeListener) {
		this.decodeListener = decodeListener;
	}

	public Camera.Size getCameraPreviewSize() {
		return cameraPreviewSize;
	}

	public void setCameraPreviewSize(Camera.Size cameraPreviewSize) {
		this.cameraPreviewSize = cameraPreviewSize;
	}

	public MultiFormatReader getMultiFormatReader() {
		return multiFormatReader;
	}

	public void setMultiFormatReader(MultiFormatReader multiFormatReader) {
		this.multiFormatReader = multiFormatReader;
	}

	public boolean isPortrait() {
		return isPortrait;
	}

	public void setPortrait(boolean isPortrait) {
		this.isPortrait = isPortrait;
	}

	public Rect getScanningAreaRect() {
		return scanningAreaRect;
	}

	public void setScanningAreaRect(Rect scanningAreaRect) {
		this.scanningAreaRect = scanningAreaRect;
	}

	public DecodeThread getDecodeThread() {
		return decodeThread;
	}

	public void setDecodeThread(DecodeThread decodeThread) {
		this.decodeThread = decodeThread;
	}

	public boolean isPause() {
		return running;
	}

	public void setPause(boolean pause) {
		this.running = pause;
	}
}
