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
 * File Name					: Crccheck_API.java
 * Since 		    			: 01-08-2014
 * Version Code & Project Name  : v 1.0 & NeonaZigbeeRTU
 * Author Name					: Nejiya Ibrahim          nejiya.ibrahim@neonainnovation.com
 * Purpose						: Implimenting Crc calculation of byte array
 * 
 */




public class Crccheck_API {

	static byte bb[]=new byte[2];

	final static byte LOOKUP1[ ] = {
		(byte)0X00,(byte)0X21,(byte)0X42,(byte)0X63,(byte)0X84,(byte)0XA5,(byte)0XC6,(byte)0XE7,
		(byte)0X08,(byte)0X29,(byte)0X4A,(byte)0X6B,(byte)0X8C,(byte)0XAD,(byte)0XCE,(byte)0XEF,
		(byte)0X31,(byte)0X10,(byte)0X73,(byte)0X52,(byte)0XB5,(byte)0X94,(byte)0XF7,(byte)0XD6,
		(byte)0X39,(byte)0X18,(byte)0X7B,(byte)0X5A,(byte)0XBD,(byte)0X9C,(byte)0XFF,(byte)0XDE,
		(byte)0X62,(byte)0X43,(byte)0X20,(byte)0X01,(byte)0XE6,(byte)0XC7,(byte)0XA4,(byte)0X85,
		(byte)0X6A,(byte)0X4B,(byte)0X28,(byte)0X09,(byte)0XEE,(byte)0XCF,(byte)0XAC,(byte)0X8D,
		(byte)0X53,(byte)0X72,(byte)0X11,(byte)0X30,(byte)0XD7,(byte)0XF6,(byte)0X95,(byte)0XB4,
		(byte)0X5B,(byte)0X7A,(byte)0X19,(byte)0X38,(byte)0XDF,(byte)0XFE,(byte)0X9D,(byte)0XBC,
		(byte)0XC4,(byte)0XE5,(byte)0X86,(byte)0XA7,(byte)0X40,(byte)0X61,(byte)0X02,(byte)0X23,
		(byte)0XCC,(byte)0XED,(byte)0X8E,(byte)0XAF,(byte)0X48,(byte)0X69,(byte)0X0A,(byte)0X2B,
		(byte)0XF5,(byte)0XD4,(byte)0XB7,(byte)0X96,(byte)0X71,(byte)0X50,(byte)0X33,(byte)0X12,
		(byte)0XFD,(byte)0XDC,(byte)0XBF,(byte)0X9E,(byte)0X79,(byte)0X58,(byte)0X3B,(byte)0X1A,
		(byte)0XA6,(byte)0X87,(byte)0XE4,(byte)0XC5,(byte)0X22,(byte)0X03,(byte)0X60,(byte)0X41,
		(byte)0XAE,(byte)0X8F,(byte)0XEC,(byte)0XCD,(byte)0X2A,(byte)0X0B,(byte)0X68,(byte)0X49,
		(byte)0X97,(byte)0XB6,(byte)0XD5,(byte)0XF4,(byte)0X13,(byte)0X32,(byte)0X51,(byte)0X70,
		(byte)0X9F,(byte)0XBE,(byte)0XDD,(byte)0XFC,(byte)0X1B,(byte)0X3A,(byte)0X59,(byte)0X78,
		(byte)0X88,(byte)0XA9,(byte)0XCA,(byte)0XEB,(byte)0X0C,(byte)0X2D,(byte)0X4E,(byte)0X6F,
		(byte)0X80,(byte)0XA1,(byte)0XC2,(byte)0XE3,(byte)0X04,(byte)0X25,(byte)0X46,(byte)0X67,
		(byte)0XB9,(byte)0X98,(byte)0XFB,(byte)0XDA,(byte)0X3D,(byte)0X1C,(byte)0X7F,(byte)0X5E,
		(byte)0XB1,(byte)0X90,(byte)0XF3,(byte)0XD2,(byte)0X35,(byte)0X14,(byte)0X77,(byte)0X56,
		(byte)0XEA,(byte)0XCB,(byte)0XA8,(byte)0X89,(byte)0X6E,(byte)0X4F,(byte)0X2C,(byte)0X0D,
		(byte)0XE2,(byte)0XC3,(byte)0XA0,(byte)0X81,(byte)0X66,(byte)0X47,(byte)0X24,(byte)0X05,
		(byte)0XDB,(byte)0XFA,(byte)0X99,(byte)0XB8,(byte)0X5F,(byte)0X7E,(byte)0X1D,(byte)0X3C,
		(byte)0XD3,(byte)0XF2,(byte)0X91,(byte)0XB0,(byte)0X57,(byte)0X76,(byte)0X15,(byte)0X34,
		(byte)0X4C,(byte)0X6D,(byte)0X0E,(byte)0X2F,(byte)0XC8,(byte)0XE9,(byte)0X8A,(byte)0XAB,
		(byte)0X44,(byte)0X65,(byte)0X06,(byte)0X27,(byte)0XC0,(byte)0XE1,(byte)0X82,(byte)0XA3,
		(byte)0X7D,(byte)0X5C,(byte)0X3F,(byte)0X1E,(byte)0XF9,(byte)0XD8,(byte)0XBB,(byte)0X9A,
		(byte)0X75,(byte)0X54,(byte)0X37,(byte)0X16,(byte)0XF1,(byte)0XD0,(byte)0XB3,(byte)0X92,
		(byte)0X2E,(byte)0X0F,(byte)0X6C,(byte)0X4D,(byte)0XAA,(byte)0X8B,(byte)0XE8,(byte)0XC9,
		(byte)0X26,(byte)0X07,(byte)0X64,(byte)0X45,(byte)0XA2,(byte)0X83,(byte)0XE0,(byte)0XC1,
		(byte)0X1F,(byte)0X3E,(byte)0X5D,(byte)0X7C,(byte)0X9B,(byte)0XBA,(byte)0XD9,(byte)0XF8,
		(byte)0X17,(byte)0X36,(byte)0X55,(byte)0X74,(byte)0X93,(byte)0XB2,(byte)0XD1,(byte)0XF0
	};

	final static byte LOOKUP2[ ] = {
		(byte)0X00,(byte)0X10,(byte)0X20,(byte)0X30,(byte)0X40,(byte)0X50,(byte)0X60,(byte)0X70,
		(byte)0X81,(byte)0X91,(byte)0XA1,(byte)0XB1,(byte)0XC1,(byte)0XD1,(byte)0XE1,(byte)0XF1,
		(byte)0X12,(byte)0X02,(byte)0X32,(byte)0X22,(byte)0X52,(byte)0X42,(byte)0X72,(byte)0X62,
		(byte)0X93,(byte)0X83,(byte)0XB3,(byte)0XA3,(byte)0XD3,(byte)0XC3,(byte)0XF3,(byte)0XE3,
		(byte)0X24,(byte)0X34,(byte)0X04,(byte)0X14,(byte)0X64,(byte)0X74,(byte)0X44,(byte)0X54,
		(byte)0XA5,(byte)0XB5,(byte)0X85,(byte)0X95,(byte)0XE5,(byte)0XF5,(byte)0XC5,(byte)0XD5,
		(byte)0X36,(byte)0X26,(byte)0X16,(byte)0X06,(byte)0X76,(byte)0X66,(byte)0X56,(byte)0X46,
		(byte)0XB7,(byte)0XA7,(byte)0X97,(byte)0X87,(byte)0XF7,(byte)0XE7,(byte)0XD7,(byte)0XC7,
		(byte)0X48,(byte)0X58,(byte)0X68,(byte)0X78,(byte)0X08,(byte)0X18,(byte)0X28,(byte)0X38,
		(byte)0XC9,(byte)0XD9,(byte)0XE9,(byte)0XF9,(byte)0X89,(byte)0X99,(byte)0XA9,(byte)0XB9,
		(byte)0X5A,(byte)0X4A,(byte)0X7A,(byte)0X6A,(byte)0X1A,(byte)0X0A,(byte)0X3A,(byte)0X2A,
		(byte)0XDB,(byte)0XCB,(byte)0XFB,(byte)0XEB,(byte)0X9B,(byte)0X8B,(byte)0XBB,(byte)0XAB,
		(byte)0X6C,(byte)0X7C,(byte)0X4C,(byte)0X5C,(byte)0X2C,(byte)0X3C,(byte)0X0C,(byte)0X1C,
		(byte)0XED,(byte)0XFD,(byte)0XCD,(byte)0XDD,(byte)0XAD,(byte)0XBD,(byte)0X8D,(byte)0X9D,
		(byte)0X7E,(byte)0X6E,(byte)0X5E,(byte)0X4E,(byte)0X3E,(byte)0X2E,(byte)0X1E,(byte)0X0E,
		(byte)0XFF,(byte)0XEF,(byte)0XDF,(byte)0XCF,(byte)0XBF,(byte)0XAF,(byte)0X9F,(byte)0X8F,
		(byte)0X91,(byte)0X81,(byte)0XB1,(byte)0XA1,(byte)0XD1,(byte)0XC1,(byte)0XF1,(byte)0XE1,
		(byte)0X10,(byte)0X00,(byte)0X30,(byte)0X20,(byte)0X50,(byte)0X40,(byte)0X70,(byte)0X60,
		(byte)0X83,(byte)0X93,(byte)0XA3,(byte)0XB3,(byte)0XC3,(byte)0XD3,(byte)0XE3,(byte)0XF3,
		(byte)0X02,(byte)0X12,(byte)0X22,(byte)0X32,(byte)0X42,(byte)0X52,(byte)0X62,(byte)0X72,
		(byte)0XB5,(byte)0XA5,(byte)0X95,(byte)0X85,(byte)0XF5,(byte)0XE5,(byte)0XD5,(byte)0XC5,
		(byte)0X34,(byte)0X24,(byte)0X14,(byte)0X04,(byte)0X74,(byte)0X64,(byte)0X54,(byte)0X44,
		(byte)0XA7,(byte)0XB7,(byte)0X87,(byte)0X97,(byte)0XE7,(byte)0XF7,(byte)0XC7,(byte)0XD7,
		(byte)0X26,(byte)0X36,(byte)0X06,(byte)0X16,(byte)0X66,(byte)0X76,(byte)0X46,(byte)0X56,
		(byte)0XD9,(byte)0XC9,(byte)0XF9,(byte)0XE9,(byte)0X99,(byte)0X89,(byte)0XB9,(byte)0XA9,
		(byte)0X58,(byte)0X48,(byte)0X78,(byte)0X68,(byte)0X18,(byte)0X08,(byte)0X38,(byte)0X28,
		(byte)0XCB,(byte)0XDB,(byte)0XEB,(byte)0XFB,(byte)0X8B,(byte)0X9B,(byte)0XAB,(byte)0XBB,
		(byte)0X4A,(byte)0X5A,(byte)0X6A,(byte)0X7A,(byte)0X0A,(byte)0X1A,(byte)0X2A,(byte)0X3A,
		(byte)0XFD,(byte)0XED,(byte)0XDD,(byte)0XCD,(byte)0XBD,(byte)0XAD,(byte)0X9D,(byte)0X8D,
		(byte)0X7C,(byte)0X6C,(byte)0X5C,(byte)0X4C,(byte)0X3C,(byte)0X2C,(byte)0X1C,(byte)0X0C,
		(byte)0XEF,(byte)0XFF,(byte)0XCF,(byte)0XDF,(byte)0XAF,(byte)0XBF,(byte)0X8F,(byte)0X9F,
		(byte)0X6E,(byte)0X7E,(byte)0X4E,(byte)0X5E,(byte)0X2E,(byte)0X3E,(byte)0X0E,(byte)0X1E
	};

/*
 * Method:		Crccalculation
 * Arguments:	byte[] of data,length of array,0x00,0x00*
 * Return:		byte[] of crc*/
	public static  byte[] ComputeCrc16( byte[] i_puc_Buffer_ch,int length,

			byte io_puc_CrcH, 
			byte io_puc_CrcL )
	{
		int	l_uc_Indice=0;
		byte	l_uc_L    = io_puc_CrcL;
		byte	l_uc_H    = io_puc_CrcH;
		for(int i=0;i<length;i++)
		{

			l_uc_Indice = (byte)( (l_uc_H) ^ (i_puc_Buffer_ch[i]) );
			l_uc_H      = (byte)( (LOOKUP2[l_uc_Indice & 0xff])^ l_uc_L);
			l_uc_L       = (byte)(LOOKUP1[l_uc_Indice & 0xff]);
		}
		System.out.println("crccheck:- ");
		bb[0]=l_uc_L;
		bb[1]= l_uc_H;
		return bb;
	}

}
