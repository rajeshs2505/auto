package com.neona.homeautomation;


import com.neona.*;

import com.physicaloid.lib.usb.driver.uart.UartConfig;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	private SeekBar seekbar;
	int l=6;
	ImageView imageview;
	private int mTextFontSize       = 12;
	private Typeface mTextTypeface  = Typeface.MONOSPACE;
	private static final String ACTION_USB_PERMISSION =
			"com.neona.homeautomation.USB_PERMISSION";
	private int mBaudrate           = 9600;
	private int mDataBits           = UartConfig.DATA_BITS8;
	private int mParity             = UartConfig.PARITY_NONE;
	private int mStopBits           = UartConfig.STOP_BITS1;
	private int mFlowControl        = UartConfig.FLOW_CONTROL_OFF;
	public static  final int MESSAGE_REPLAY=7;
	public static  final int MESSAGE_SEND=8;
	public static final int MESSAGE_TOASTNOTCONNECT = 5;	
	public static final String TOASTNOTCONNECT = "notconnect";
	public static final int MESSAGE_TOASTNOTCONNECT1 = 6;	
	public static final String TOASTNOTCONNECT1 = "notconnect1";
	public static final int MESSAGE_TOASTCONNECT = 4;	
	public static  String TOASTCONNECT = "connect";
	public static final int MESSAGE_TOASTCONNECT1 = 3;	
	public static final String TOASTCONNECT1 = "connect1";
	public static final String TOASTTEST = "test";
	Zigbee_APIClass zigbee_api;
	protected void onNewIntent(Intent intent) {
		

		zigbee_api.rxbees.openUsbSerial(getApplicationContext());
	};
	private void detachedUi() {
		
		Toast.makeText(this, "disconnect", Toast.LENGTH_SHORT).show();
	}
	BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();

			if (UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action)) {
				
				if (!zigbee_api.rxbees.mSerial.isOpened()) {
					
					zigbee_api.rxbees.openUsbSerial(getApplicationContext());
				}
				if (!zigbee_api.rxbees.mRunningMainLoop) {
					
					zigbee_api.rxbees.mainloop();
				}
			} else if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
				
				zigbee_api.rxbees.mStop = true;
				detachedUi();
				zigbee_api.rxbees.mSerial.close();
			} else if (ACTION_USB_PERMISSION.equals(action)) {
				
				synchronized (this) {
					if (!zigbee_api.rxbees.mSerial.isOpened()) {
						
						zigbee_api.rxbees.openUsbSerial(getApplicationContext());
					}
				}
				if (!zigbee_api.rxbees.mRunningMainLoop) {
					
					zigbee_api.rxbees.mainloop();
				}
			}
		}
	};

	private final Handler mHandlerBT = new Handler() {

		@Override
		public void handleMessage(Message msg) {        	
			switch (msg.what) {



			case MESSAGE_TOASTNOTCONNECT:

				Toast.makeText(getApplicationContext(), msg.getData().getString(TOASTNOTCONNECT),
						Toast.LENGTH_SHORT).show();		
				break;

			case MESSAGE_TOASTNOTCONNECT1:

				Toast.makeText(getApplicationContext(), msg.getData().getString(TOASTNOTCONNECT1),
						Toast.LENGTH_SHORT).show();		
				break;		
			case MESSAGE_TOASTCONNECT:

				Toast.makeText(getApplicationContext(), msg.getData().getString(TOASTCONNECT),
						Toast.LENGTH_SHORT).show();	
				
				break;
			case MESSAGE_TOASTCONNECT1:

				Toast.makeText(getApplicationContext(), msg.getData().getString(TOASTCONNECT1),
						Toast.LENGTH_SHORT).show();		
				break;

			case MESSAGE_REPLAY:

				String s="";
				int length=0;
				byte[] b=msg.getData().getByteArray("status_load");
				length=msg.getData().getInt("length");
				char[] c=new char[b.length];
				for(int j=0;j<c.length;j++)
				{
					c[j]=(char) (b[j]);
				}

				s=new String(c);
				Toast.makeText(getApplicationContext(), s+"", 2000).show();
				
				break;
			case MESSAGE_SEND:


				byte[] txt=msg.getData().getByteArray("rexbee");
				
				

				break;
			}
		}


	};    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
	     //imageview= (ImageView)findViewById(R.id.image);
         zigbee_api=new Zigbee_APIClass(MainActivity.this,mHandlerBT );
        IntentFilter filter = new IntentFilter();
		filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
		filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
		registerReceiver(mUsbReceiver, filter);
        seekbar = (SeekBar)findViewById(R.id.seekBar);
        seekbar.setMax(10);	
        seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
        		@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				l=seekbar.getProgress();
                l=l;
				/*	Toast.makeText(getApplicationContext(), String.valueOf(l), 
						Toast.LENGTH_SHORT).show();
				
				*/
				byte[] data={(byte)0x91,(byte)0x34,(byte)0x01,(byte)0x00,
						(byte)0x47,(byte)0x35,(byte)0x01,(byte)0x00,
						(byte)1,(byte)l};
				byte[] source={(byte)0x00};
				byte[] dest={(byte)0x00};
				
				Toast.makeText(getApplicationContext(), String.valueOf(l)+"  val", 
						Toast.LENGTH_SHORT).show();
				zigbee_api.rxbees.writeDataToSerial(String.valueOf(l));
				
				
			}
		});
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
}
