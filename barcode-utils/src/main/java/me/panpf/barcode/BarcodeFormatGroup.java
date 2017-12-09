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

/**
 * 条码格式组
 */
public enum BarcodeFormatGroup {
	/**
	 * 产品码
	 */
	PRODUCT_FORMATS, 
	
	/**
	 * 一维码
	 */
	ONE_D_FORMATS, 
	
	/**
	 * 二维码
	 */
	QR_CODE_FORMATS,
	
	/**
	 * 数据码
	 */
	DATA_MATRIX_FORMATS,
}