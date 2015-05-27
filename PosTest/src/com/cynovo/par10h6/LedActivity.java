package com.cynovo.par10h6;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import com.kivvi.jni.LedInterface;

public class LedActivity extends Activity implements OnClickListener  {
	
	public final static int  Camerled     = 0x01;
	public final static int  redled       = 0x02;
	public final static int  greenled     = 0x03;
	public final static int  rednfcled    = 0x04;
	public final static int  greennfcled  = 0x05;
	public final static int  bluenfcled   = 0x06;
	public final static int  yellownfcled = 0x07;
	
	public final static int  ON = 0x01;
	public final static int  OFF = 0x00;
	
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_led);
			 findViewById(R.id.nfcblueledon).setOnClickListener(this);
			 findViewById(R.id.nfcblueledoff).setOnClickListener(this);
			 findViewById(R.id.nfcyellowledon).setOnClickListener(this);
			 findViewById(R.id.nfcyellowledoff).setOnClickListener(this);
			 findViewById(R.id.nfcgreenledon).setOnClickListener(this);
			 findViewById(R.id.nfcgreenledoff).setOnClickListener(this);
			 findViewById(R.id.nfcredledon).setOnClickListener(this);
			 findViewById(R.id.nfcredledoff).setOnClickListener(this);
			 
			 findViewById(R.id.greenledon).setOnClickListener(this);
			 findViewById(R.id.greenledoff).setOnClickListener(this);
			 findViewById(R.id.redledon).setOnClickListener(this);
			 findViewById(R.id.redledoff).setOnClickListener(this);
			 
			 findViewById(R.id.cameraledon).setOnClickListener(this);
			 findViewById(R.id.cameraledoff).setOnClickListener(this);	
			 
			 findViewById(R.id.turnonall).setOnClickListener(this);
			 findViewById(R.id.turnoffall).setOnClickListener(this);				 
			 LedInterface.open();
			 
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
				case R.id.cameraledon:
					LedInterface.set(ON,Camerled);
					break;
				case R.id.cameraledoff:
					LedInterface.set(OFF,Camerled);					
					break;	
				case R.id.redledon:
					LedInterface.set(ON,redled);
					break;
				case R.id.redledoff:
					LedInterface.set(OFF,redled);					
					break;
				case R.id.greenledon:
					LedInterface.set(ON,greenled);
					break;
				case R.id.greenledoff:
					LedInterface.set(OFF,greenled);					
					break;	
				case R.id.nfcredledon:
					LedInterface.set(ON,rednfcled);
					break;
				case R.id.nfcredledoff:
					LedInterface.set(OFF,rednfcled);					
					break;	
				case R.id.nfcgreenledon:
					LedInterface.set(ON,greennfcled);
					break;
				case R.id.nfcgreenledoff:
					LedInterface.set(OFF,greennfcled);					
					break;	
				case R.id.nfcblueledon:
					LedInterface.set(ON,bluenfcled);
					break;
				case R.id.nfcblueledoff:
					LedInterface.set(OFF,bluenfcled);					
					break;		
				case R.id.nfcyellowledon:
					LedInterface.set(ON,yellownfcled);
					break;
				case R.id.nfcyellowledoff:
					LedInterface.set(OFF,yellownfcled);
					break;					
				case R.id.turnonall:
					turnall(ON);					
					break;	
				case R.id.turnoffall:
					turnall(OFF);					
					break;						
				default:
					break;
			}
		}
				
		
		void turnall(int mode){
			LedInterface.set(mode,Camerled);
			LedInterface.set(mode,redled);
			LedInterface.set(mode,greenled);
			LedInterface.set(mode,rednfcled);
			LedInterface.set(mode,greennfcled);
			LedInterface.set(mode,bluenfcled);					
			LedInterface.set(mode,yellownfcled);							
		}
		@Override
		public void onDestroy() {
			LedInterface.close();
			super.onDestroy();
		}		
}
