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
 * File Name					: Zigbee_APIClass.java
 * Since 		    			: 01-08-2014
 * Version Code & Project Name  : v 1.0 & NeonaZigbeeRTU
 * Author Name					: Nejiya Ibrahim          nejiya.ibrahim@neonainnovation.com
 * Purpose						: Implimenting RTU_Zigbee Protocol Layer
 * 
 */


import javax.crypto.spec.OAEPParameterSpec;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.widget.Toast;

public class Zigbee_APIClass  extends Crccheck_API{
	//bytearray for checksum
	StringBuilder hexstring;
	Rexbee_API_class rxbees;
	Handler mHandlerBT;


	public Zigbee_APIClass(Context con,Handler mhandler) {
		// TODO Auto-generated constructor stub
		mHandlerBT=mhandler;
		Toast.makeText(con, "Zigbee Api", 2000).show();
		rxbees=new Rexbee_API_class(con, mHandlerBT);
	}
	public void processdata(byte[] source,byte[] dest,byte[] data,int count) {
	
		
		System.out.println(" process data  :-");
		
		int cnt=0;
		int length=0;
		byte[] temp_data = null;
		byte[] temp_crc = null;
		byte[] src = new byte[source.length];
		src=source;
		byte[] dst = new byte[dest.length];
		dst=dest;
		
		//+2 for adding crcl and crch
		cnt=count+2;
		
		if (data.length>8  ) {

			length=(byte)data[8];
				temp_data=new byte[(9+length)];
				temp_crc=new byte[(11+length)];
				
				for (int i = 0; i < (9+length); i++) {
					temp_data[i]=data[i];
					temp_crc[i]=data[i];
				}
				byte[] crcs=new byte[2];
				System.out.println("before crc");
				crcs=ComputeCrc16(temp_data, temp_data.length, (byte)0x00, (byte)0x00);
				temp_crc[9+length]=crcs[0];
				temp_crc[10+length]=crcs[1];
			}
		else {
			System.out.println("lenght is less than 8");
		}
		for (int i = 0; i < temp_crc.length; i++) {
			System.out.println(temp_crc[i]);
		}
		rxbees.Writedata(src, dest, temp_crc, cnt);
	}



	/*Method	:converthexstrng tobyte array
	Arguments	:hexstring from file
	Return		:bytearray*/
	public static byte[] hexStringToByteArray(String s) {
		int len = s.length();
		byte[] data = new byte[len / 2];
		for (int i = 0; i < len; i += 2) {
			
			try {
				data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
						+ Character.digit(s.charAt(i+1), 16));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("Numberformat Exception");
			}
			
		}
		return data;
	}
	//Method	:bytetohexconversion
	//Argument	:Byte data
	//Return	:Hexstring
	private String byteof(byte data) {
		// TODO Auto-generated method stub
		String y=String.format("%02x", data);
		return y;
	}


	/*Method	:convert byte array to hexstring.
	Argument	:passes byte[]
	Return 		:hexstring*/


	String bytetohex(byte[] txt) {
		// TODO Auto-generated method stub
		String p="";
		byte[] text=new byte[txt.length];
		text=txt;

		hexstring=new StringBuilder();
		for (int j = 0; j < txt.length; j++) {
			String hex= Integer.toHexString(0xFF & txt[j]);
			if (hex.length()==1) {
				hexstring.append("0");
			}

			hexstring.append(hex+" ");

		}

		p=p+hexstring.toString();
		return p;
	}

	//method	:extract path from file uri
	//Argument	:Uri
	//Return	:String

	String Stringpathfromuri(Uri returnUri) {
		// TODO Auto-generated method stub
			String  ss = returnUri.toString();
		String path = "/";
		int position=0;
		String[] arr = ss.split("/");
		String[] arr1;
		String s="";
		for (int i = 0; i < arr.length; i++) {
			System.out.println(arr[i]);
			if(arr[i].contains("sdcard")){
				position = i+1;
				System.out.println(i+"  : position");
			}
		}
		for (int i = position,j=0; i < arr.length; i++,j++) {
			arr1=new String[arr.length];
			arr1[j]=arr[i];
			s=s+("/"+arr1[j]);

		}

		return s;
	}
}
