package com.collabra.pretexta.listener;

import java.util.Date;











import com.collabra.pretexta.R;
import com.collabra.pretexta.R.drawable;
import com.collabra.pretexta.db.DataBaseHelper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.provider.CallLog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BroadCastlistener extends BroadcastReceiver {

	 private WindowManager wm;
     private static LinearLayout ly1;
     private WindowManager.LayoutParams params1;
    // TextView txtView;
     String callDetails;
     String smsDetails;
     // onReceive function of the Broadcast Receiver
     public void onReceive(Context context, Intent arg1) {
             String state = arg1.getStringExtra(TelephonyManager.EXTRA_STATE);
            final ImageView  imgView =new ImageView(context);
             // Adds a view on top of the dialer app when it launches.
             if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
            	 String incomingNumber = arg1.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
               //  Toast.makeText(context, "Call from:" +incomingNumber, Toast.LENGTH_LONG).show();
                 callDetails=getContactIdFromNumber(incomingNumber,context);
                // smsDetails=getSMSDetails(incomingNumber,context);
                 wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                 params1 = new WindowManager.LayoutParams(
                         LayoutParams.MATCH_PARENT,
                         LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.TYPE_SYSTEM_ALERT |
                         WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
                         WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                         WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                         PixelFormat.TRANSPARENT);

                 params1.height = 75;
                 params1.width = 300;
                 params1.x = -200; 
                 params1.y = 300;
                 params1.gravity =Gravity.TOP | Gravity.LEFT;
                 params1.format = PixelFormat.TRANSLUCENT;
                //TextView txtView= new TextView(context);
                
                 imgView.setImageResource(R.drawable.phone);
                 imgView.setVisibility(View.VISIBLE);
              //   new AnimationUtils();
				
                 Animation animationFadeIn=AnimationUtils.loadAnimation(context, R.anim.fade_in);
                 Animation animationFadeOut=AnimationUtils.loadAnimation(context, R.anim.fade_out);
                 Animation animationBounce = AnimationUtils.loadAnimation(context,R.anim.bounce);
                 Animation animationSequential = AnimationUtils.loadAnimation(context,R.anim.sequential);
                 AnimationListener animListener = new AnimationListener(){

                     @Override
                     public void onAnimationStart(Animation animation) {
                     }

                     @Override
                     public void onAnimationRepeat(Animation animation) {
                         }

                     @Override
                     public void onAnimationEnd(Animation animation) {
                      
                     }
                 };
             imgView.startAnimation(animationFadeOut);
             animationFadeOut.setAnimationListener(animListener);
                 imgView.startAnimation(animationSequential);
                 animationSequential.setAnimationListener(animListener);
             AnimationListener animFadeInListener = new AnimationListener(){

                 @Override
                 public void onAnimationStart(Animation animation) {
                 }

                 @Override
                 public void onAnimationRepeat(Animation animation) {
                     }

                 @Override
                 public void onAnimationEnd(Animation animation) {
                  
                 }
             };
                 ly1 = new LinearLayout(context);
         //imgView.startAnimation(animationFadeIn);
         //animationFadeIn.setAnimationListener(animFadeInListener);
                 final TextView txtView= new TextView(context);	
               txtView.setGravity(Gravity.RIGHT);
               txtView.setTextSize(14);
           //  txtView.setText("test : "+callDetails);
             imgView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					 imgView.clearAnimation();
					 String details=callDetails + smsDetails;
					 txtView.setText(details);
					 ly1.removeView(imgView);
					 ly1.addView(txtView);		
					imgView.setVisibility(View.INVISIBLE);
					//wm.updateViewLayout(arg0, params1);
				}
			});
                
                 ly1.setBackgroundColor(Color.TRANSPARENT);
                 ly1.setOrientation(LinearLayout.VERTICAL);
                 ly1.addView(imgView);
               
                 wm.addView(ly1, params1);
             }

             // To remove the view once the dialer app is closed.
             if(arg1.getAction().equals("android.intent.action.PHONE_STATE")){
                  state = arg1.getStringExtra(TelephonyManager.EXTRA_STATE);
                 if(state.equals(TelephonyManager.EXTRA_STATE_IDLE) || state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
                     WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                     if(ly1!=null)
                     {
                         wm.removeView(ly1);
                         ly1 = null;
                     }
                 }
             }
         }
     
     
     private String getContactIdFromNumber(String contactNumber, Context context) {
			StringBuilder callLogInfo=new StringBuilder();
			DataBaseHelper helper= new DataBaseHelper(context);
			SQLiteDatabase sqlLiteDB= helper.getReadableDatabase();
			Cursor cursor =sqlLiteDB.rawQuery("select lastspoken,duration,phone from Contactsync where phone=?",new String[]{contactNumber});
			Log.i("phone found" ,cursor.getCount()+"");
			while (cursor.moveToNext()) {
				 String phNumber = cursor.getString(cursor.getColumnIndex("phone"));
			        
				 Long lastSpoken = cursor.getLong(cursor.getColumnIndex("lastspoken"));
				 Integer duration = cursor.getInt(cursor.getColumnIndex("duration"));
				 Date callDayTime = new Date(lastSpoken);
				 callLogInfo.append("LastSpoken :" + callDayTime +", Duration :" + duration);
			}
		    return callLogInfo.toString();
		}

     
     
     
   /*  private String getContactIdFromNumber(String contactNumber, Context context) {
			StringBuilder callLogInfo=new StringBuilder();
			Cursor cursor = context.getContentResolver().query(
					CallLog.Calls.CONTENT_URI,
					new String[] { CallLog.Calls.DATE, CallLog.Calls.DURATION,
	                        CallLog.Calls.NUMBER, CallLog.Calls._ID , CallLog.Calls.TYPE},
	                        CallLog.Calls.NUMBER + "=?",  new String[] { contactNumber},
	                        CallLog.Calls.DATE + " desc");
			
			int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
		    int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
		    int date = cursor.getColumnIndex(CallLog.Calls.DATE);
		    int duration = cursor.getColumnIndex(CallLog.Calls.DURATION); 
		    int missedCall =0;
		    int incomingCall =0;
		    int outgoingCall =0;
			while (cursor.moveToNext()) {
				 String phNumber = cursor.getString(number);
			        String callType = cursor.getString(type);
			        String callDate = cursor.getString(date);
			        Date callDayTime = new Date(Long.valueOf(callDate));
			        String callDuration = cursor.getString(duration);
			        String dir = null;
			        
			        int dircode = Integer.parseInt(callType);
			        switch (dircode) {
			        case CallLog.Calls.OUTGOING_TYPE:
			            dir = "OUTGOING";
			            outgoingCall ++;
			            break;
			        case CallLog.Calls.INCOMING_TYPE:
			            dir = "INCOMING";
			            incomingCall++;
			            break;

			        case CallLog.Calls.MISSED_TYPE:
			            dir = "MISSED";
			            missedCall++;
			            break;
			        }
			        
			        Log.i("dir", dir);
			    }
				if(missedCall>0){
					callLogInfo.append(missedCall  + " missed Calls \n");
				}
			
				if(outgoingCall>0){
					callLogInfo.append(outgoingCall  + " outgoing Calls \n");
				}
				if(incomingCall>0){
					callLogInfo.append(incomingCall  + " incoming Calls \n");
				}
			   cursor.close();
			//   addInvitePopup(contactNumber,mContext);
		    return callLogInfo.toString();
		}
*/	

     private String getSMSDetails(String contactNumber,Context context) {
         StringBuffer stringBuffer = new StringBuffer();
       //  stringBuffer.append("*********SMS History*************** :");
         Uri uri = Uri.parse("content://sms");
         String[] reqCols = new String[] { "date", "address", "body","type","_id" };
         Cursor cursor = context.getContentResolver().query(uri, reqCols,
        		  "address=?",  new String[] { contactNumber},
               "date desc");
         int inboxType=0;
         int sentType=0;
         if (cursor.moveToFirst()) {
                for (int i = 0; i < cursor.getCount(); i++) {
                      String body = cursor.getString(cursor.getColumnIndexOrThrow("body"))
                                    .toString();
                      String number = cursor.getString(cursor.getColumnIndexOrThrow("address"))
                                    .toString();
                      String date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                                    .toString();
                      Date smsDayTime = new Date(Long.valueOf(date));
                      String type = cursor.getString(cursor.getColumnIndexOrThrow("type"))
                                    .toString();
                      String typeOfSMS = null;
                      switch (Integer.parseInt(type)) {
                      case 1:
                             typeOfSMS = "INBOX";
                             inboxType++;
                             break;

                      case 2:
                             typeOfSMS = "SENT";
                             break;

                      case 3:
                             typeOfSMS = "DRAFT";
                             break;
                      }
                    
                      cursor.moveToNext();
                }
                if(inboxType>0){
              	  stringBuffer.append(inboxType  + " messages in Inbox");
				}
         }
         cursor.close();
         return stringBuffer.toString();
  }


}
