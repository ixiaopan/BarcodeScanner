/*
 * Copyright (C) 2017 Peng fei Pan <sky@panpf.me>
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

package me.panpf.barcode;

import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;

/**
 * 解码预览回调
 */
class DecodePreviewCallback implements PreviewCallback {
	private BarcodeScanner barcodeScanner;
	
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		if(barcodeScanner != null && !barcodeScanner.isReleased() && barcodeScanner.isScanning()){
			barcodeScanner.getDecodeThread().getDecodeHandler().sendDecodeMessage(data);
			barcodeScanner = null;
		}
	}

	void setBarcodeScanner(BarcodeScanner barcodeScanner) {
		this.barcodeScanner = barcodeScanner;
	}
}