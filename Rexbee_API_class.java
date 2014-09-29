package com.neona.homeautomation;

/**
 *Copyright 2013 Neona Embedded Labz as an unpublished work. All Rights Reserved.
 *
 * The information contained herein is confidential property of Neona Embedded Labz. The use, copying,
 * transfer or disclosure of such information is prohibited except by express written agreement with
 * Company.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of  the License at 
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 * 
 * File Name					: Rexbee_API_class.java
 * Since 		    			: 01-08-2014
 * Version Code & Project Name  : v 1.0 & NeonaZigbeeRTU
 * Author Name					: Nejiya Ibrahim          nejiya.ibrahim@neonainnovation.com
 * Purpose						: Implimenting Basic_zigbee transmission layer
 * 
 */


import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ScrollView;
import android.widget.Toast;

import com.physicaloid.lib.Physicaloid;
import com.physicaloid.lib.usb.driver.uart.UartConfig;

@SuppressLint("NewApi")
public class Rexbee_API_class {

	String TAG = "AndroidSerialTerminal";
	boolean mRunningMainLoop = false;
	private static final boolean SHOW_DEBUG                 = false;
	private static final boolean USE_WRITE_BUTTON_FOR_DEBUG = false;
	private final Handler mHandler;
	// Linefeed Code Settings
	public static final int LINEFEED_CODE_CR   = 0;
	public static final int LINEFEED_CODE_CRLF = 1;
	public static final int LINEFEED_CODE_LF   = 2;
	public int mWriteLinefeedCode  = LINEFEED_CODE_LF;
	public int mBaudrate           = 115200;
	public int mDataBits           = UartConfig.DATA_BITS8;
	public int mParity             = UartConfig.PARITY_NONE;
	public int mStopBits           = UartConfig.STOP_BITS1;
	public int mFlowControl        = UartConfig.FLOW_CONTROL_OFF;
	public boolean mStop = false;

	byte[] rexbee_tx_api_frame;
	Physicaloid mSerial;
	byte[] temp_rexbee;
	byte[] rexbee_android_api_frame={
			(byte)0x41, (byte) 0x88,  (byte)0x00,  (byte)0x00,  (byte)0x00,
			(byte)0x00,  (byte)0x00,  (byte)0x00, (byte) 0x91,  (byte)0x46,
			(byte)0x2c,  (byte)0x00, 	(byte)0x00,  (byte)0x00,  (byte)0x00,
			(byte)0x00,  (byte)0x5b,  (byte)0x47,  (byte)0x2c,  (byte)0x00,
			(byte)0x00,  (byte)0x00,  (byte)0x00,  (byte)0x00,  (byte)0x00, 
			(byte)0x00,  (byte)0x25,  (byte)0x78,  (byte)0x00,  (byte)0x00,	//Read/write - 0x20/025, router/coo - 0x58/0x78
			(byte) 0xa0,  (byte)0x0f,  (byte)0x00,  (byte)0x00
	};



/* Constructrot to pass instance of Rexbee class*/
	public Rexbee_API_class(Context con,
			Handler mHandlerBT) {
		// TODO Auto-generated constructor stub
		mSerial=new Physicaloid(con);
		rexbee_tx_api_frame=new byte[37];
		temp_rexbee=new byte[35];
		mHandler = mHandlerBT;	
		Toast.makeText(con, "rexbee", 2000).show();
	}
/*Method		:Write Data to Zigbee
 *Arguments		: byte[] source address,destination address,data,datalength as int
 *Return:		:Transfer data via handler */ 
	public byte Writedata(byte[] source_adress,byte[] target_address, byte[] data, int count) {
		
		int checksum = 0;
		byte check  = 0x00;
		byte[] target = new byte[target_address.length];
		target=target_address;
		byte[] source = new byte[source_adress.length];
		source=source_adress;

		rexbee_tx_api_frame=new byte[count+39];
		rexbee_tx_api_frame[0]=(byte)0x2a;
		rexbee_tx_api_frame[1]=(byte)(count+35);

		for (int i = 0,j=2; i < 34; i++,j++) {
			rexbee_tx_api_frame[j]=rexbee_android_api_frame[i];
		}

		for (int i = 0,j=10; i < source.length; i++,j++) {
			rexbee_tx_api_frame[j]=source[i];
		}
		for (int i = 0,j=18; i < target.length; i++,j++) {
			rexbee_tx_api_frame[j]=target[i];
		}
		rexbee_tx_api_frame[36] =(byte)count;
		for (int i = 0,j=37; i < data.length; i++,j++) {
			rexbee_tx_api_frame[j]=data[i];
		}
		temp_rexbee=new byte[35+count];
			for (int i = 0,j=2; j < rexbee_tx_api_frame.length-2; i++,j++) {
			temp_rexbee[i]=rexbee_tx_api_frame[j];

		}

		checksum = rexbee_checksum(temp_rexbee, temp_rexbee.length);


		checksum &= 0x000000FF;
		check = (byte) checksum;


		rexbee_tx_api_frame[37+count]=check;
		rexbee_tx_api_frame[38+count]=(byte)0x23;

		mSerial.write(rexbee_tx_api_frame,rexbee_tx_api_frame.length);
		Message msg = mHandler.obtainMessage(com.neona.homeautomation.MainActivity.MESSAGE_SEND);
		Bundle bundle = new Bundle();
		bundle.putByteArray("rexbee", rexbee_tx_api_frame);

		msg.setData(bundle);
		mHandler.sendMessage(msg);
		return 0;

	}
	void openUsbSerial(Context con) {
		if(mSerial == null) {
			Message msg = mHandler.obtainMessage(com.neona.homeautomation.MainActivity.MESSAGE_TOASTCONNECT);
			Bundle bundle = new Bundle();
			bundle.putString(com.neona.homeautomation.MainActivity.TOASTNOTCONNECT,  "cannot open");
			msg.setData(bundle);
			System.out.println("open");
			mHandler.sendMessage(msg);

			return;
		}

		if (!mSerial.isOpened()) {
			if (SHOW_DEBUG) {
				Log.d(TAG, "onNewIntent begin");
			}
			if (!mSerial.open()) {
				Message msg = mHandler.obtainMessage(com.neona.homeautomation.MainActivity.MESSAGE_TOASTNOTCONNECT1);
				Bundle bundle = new Bundle();
				bundle.putString(com.neona.homeautomation.MainActivity.TOASTNOTCONNECT1,  "cannot open");
				msg.setData(bundle);
				mHandler.sendMessage(msg);
				return;
			} else {
				boolean dtrOn=false;
				boolean rtsOn=false;
				if(mFlowControl == UartConfig.FLOW_CONTROL_ON) {
					dtrOn = true;
					rtsOn = true;
				}
				mSerial.setConfig(new UartConfig(mBaudrate, mDataBits, mStopBits, mParity, dtrOn, rtsOn));

				if(SHOW_DEBUG) {
					Log.d(TAG, "setConfig : baud : "+mBaudrate+", DataBits : "+mDataBits+", StopBits : "+mStopBits+", Parity : "+mParity+", dtr : "+dtrOn+", rts : "+rtsOn);
				}


				Message msg = mHandler.obtainMessage(com.neona.homeautomation.MainActivity.MESSAGE_TOASTCONNECT);
				Bundle bundle = new Bundle();
				bundle.putString(com.neona.homeautomation.MainActivity.TOASTCONNECT,  "connected config");
				msg.setData(bundle);
				mHandler.sendMessage(msg);
			}
		}

		if (!mRunningMainLoop) {
			mainloop();
		}

	}

	void mainloop() {
		mStop = false;
		mRunningMainLoop = true;

		Message msg = mHandler.obtainMessage(com.neona.homeautomation.MainActivity.MESSAGE_TOASTNOTCONNECT1);
		Bundle bundle = new Bundle();
		bundle.putString(com.neona.homeautomation.MainActivity.TOASTNOTCONNECT1,  "connected");
		msg.setData(bundle);
		mHandler.sendMessage(msg);

		if (SHOW_DEBUG) {
			Log.d(TAG, "start mainloop");
		}
		new Thread(mLoop).start();
	}



	private Runnable mLoop = new Runnable() {
		@Override
		public void run() {
			int len;
			byte[] rbuf = new byte[4096];

			for (;;) {// this is the main loop for transferring
				// ////////////////////////////////////////////////////////
				// Read and Display to Terminal
				// ////////////////////////////////////////////////////////
				len = mSerial.read(rbuf);
				rbuf[len] = 0;
				//showToast(len+": value");

				if (len > 0) {
					if (SHOW_DEBUG) {
						Log.d(TAG, "Read  Length : " + len);
					}

					Message msg = mHandler.obtainMessage(com.neona.homeautomation.MainActivity.MESSAGE_REPLAY);
					Bundle bundle = new Bundle();
					bundle.putByteArray("status_load", rbuf);
					bundle.putInt("length", len);
					msg.setData(bundle);
					mHandler.sendMessage(msg);
				}
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				if (mStop) {
					mRunningMainLoop = false;
					return;
				}
			}
		}
	};

/*	Methd		:Checksum calculation
	Arguments	:byte[] data,datalenght
	return		:byte checksum*/
	public int rexbee_checksum(byte[] data, int count)
	{
		int checksum = 0;
		int i;
		for(i = 0; i < count; i++)
		{
			checksum=checksum+(int)(data[i]&0xff);
				}

		return checksum;
	}
/*method:	write any data
 * Argument	:String
 * */
	public void writeDataToSerial(String stringwrite) {
		String strWrite=stringwrite;
		strWrite = changeEscapeSequence(strWrite);
		mSerial.write(strWrite.getBytes(), strWrite.length());
	}

	String changeEscapeSequence(String in) {
		String out = new String();
		try {
			out = unescapeJava(in);
		} catch (IOException e) {
			return "";
		}
		switch (mWriteLinefeedCode) {
		case LINEFEED_CODE_CR:
			out = out + "\r";
			break;
		case LINEFEED_CODE_CRLF:
			out = out + "\r\n";
			break;
		case LINEFEED_CODE_LF:
			out = out + "\n";
			break;
		default:
		}
		return out;
	}
	

	/**
	 * <p>Unescapes any Java literals found in the <code>String</code> to a
	 * <code>Writer</code>.</p>
	 *
	 * <p>For example, it will turn a sequence of <code>'\'</code> and
	 * <code>'n'</code> into a newline character, unless the <code>'\'</code>
	 * is preceded by another <code>'\'</code>.</p>
	 * 
	 * <p>A <code>null</code> string input has no effect.</p>
	 * 
	 * @param out  the <code>String</code> used to output unescaped characters
	 * @param str  the <code>String</code> to unescape, may be null
	 * @throws IllegalArgumentException if the Writer is <code>null</code>
	 * @throws IOException if error occurs on underlying Writer
	 */
	private String unescapeJava(String str) throws IOException {
		if (str == null) {
			return "";
		}
		int sz = str.length();
		StringBuffer unicode = new StringBuffer(4);

		StringBuilder strout = new StringBuilder();
		boolean hadSlash = false;
		boolean inUnicode = false;
		for (int i = 0; i < sz; i++) {
			char ch = str.charAt(i);
			if (inUnicode) {
				// if in unicode, then we're reading unicode
				// values in somehow
				unicode.append(ch);
				if (unicode.length() == 4) {
					// unicode now contains the four hex digits
					// which represents our unicode character
					try {
						int value = Integer.parseInt(unicode.toString(), 16);
						strout.append((char) value);
						unicode.setLength(0);
						inUnicode = false;
						hadSlash = false;
					} catch (NumberFormatException nfe) {
						// throw new NestableRuntimeException("Unable to parse unicode value: " + unicode, nfe);
						throw new IOException("Unable to parse unicode value: " + unicode, nfe);
					}
				}
				continue;
			}
			if (hadSlash) {
				// handle an escaped value
				hadSlash = false;
				switch (ch) {
				case '\\':
					strout.append('\\');
					break;
				case '\'':
					strout.append('\'');
					break;
				case '\"':
					strout.append('"');
					break;
				case 'r':
					strout.append('\r');
					break;
				case 'f':
					strout.append('\f');
					break;
				case 't':
					strout.append('\t');
					break;
				case 'n':
					strout.append('\n');
					break;
				case 'b':
					strout.append('\b');
					break;
				case 'u':
				{
					// uh-oh, we're in unicode country....
					inUnicode = true;
					break;
				}
				default :
					strout.append(ch);
					break;
				}
				continue;
			} else if (ch == '\\') {
				hadSlash = true;
				continue;
			}
			strout.append(ch);
		}
		if (hadSlash) {
			// then we're in the weird case of a \ at the end of the
			// string, let's output it anyway.
			strout.append('\\');
		}
		return new String(strout.toString());
	}
	void closeUsbSerial() {

		mStop = true;
		mSerial.close();
	}


	
}
