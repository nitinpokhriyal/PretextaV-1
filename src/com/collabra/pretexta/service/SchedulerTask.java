package com.collabra.pretexta.service;

import java.util.Calendar;
import java.util.Date;

import com.collabra.pretexta.db.DataBaseHelper;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.CallLog;
import android.util.Log;

public class SchedulerTask extends IntentService {
	


//http://techblogon.com/simple-android-service-example-code-description-start-stop-service/
//http://developer.android.com/guide/components/services.html	

public SchedulerTask() {
		super("intentService");
		// TODO Auto-generated constructor stub
	}

	/*
Intent intent = new Intent(DashboardScreen.this, ServiceClass.class);
PendingIntent pintent = PendingIntent.getService(DashboardScreen.this, 0, intent, 0);
AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
alarm.cancel(pintent);
alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 30*1000, pintent);*/
	@Override
	public void onCreate() {
		super.onCreate();
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Bundle bundle=intent.getExtras();
		String val=bundle.getString("nitin");
		Log.i("SchedulerTaskclass", val);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	 private void getContactIdFromNumber(Context context) {
		  SQLiteDatabase db;
		  //  db = context.openOrCreateDatabase( "Pretexta.db"        , SQLiteDatabase.CREATE_IF_NECESSARY ,null);    
		  DataBaseHelper dbHelper=new DataBaseHelper(context);
		  
		  db=dbHelper.getWritableDatabase();
		  StringBuilder callLogInfo=new StringBuilder();
			Calendar cal=Calendar.getInstance();
			cal.set(Calendar.MONTH, -3);
			
			Cursor cursor = context.getContentResolver().query(
					CallLog.Calls.CONTENT_URI,
					new String[] { CallLog.Calls.DATE, CallLog.Calls.DURATION,
	                        CallLog.Calls.NUMBER, CallLog.Calls._ID , CallLog.Calls.TYPE},
	                        CallLog.Calls.DATE + ">?",  new String[] {String.valueOf(cal.getTime())},
	                        CallLog.Calls.DATE + " desc");
			
			/*Cursor cursor = context.getContentResolver().query(
					CallLog.Calls.CONTENT_URI,
					new String[] { CallLog.Calls.DATE, CallLog.Calls.DURATION,
	                        CallLog.Calls.NUMBER, CallLog.Calls._ID , CallLog.Calls.TYPE},
	                        CallLog.Calls.DATE ,  null,
	                        CallLog.Calls.DATE + " desc");*/
			
			int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
		    int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
		    int date = cursor.getColumnIndex(CallLog.Calls.DATE);
		    int duration = cursor.getColumnIndex(CallLog.Calls.DURATION); 
		    int missedCall =0;
		    int incomingCall =0;
		    int outgoingCall =0;
		    Log.i("Pretexta-ContactId",cursor.getCount()+"");
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
			        String insert_sql="INSERT or replace INTO Contactsync (phone, lastspoken,duration) "
			        		+ "VALUES("+phNumber+","+ callDate +"," +callDuration+")" ;
			        Log.i("Pretexta-ContactId",insert_sql);
			        db.execSQL(insert_sql);
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
		    //return callLogInfo.toString();
		}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		getContactIdFromNumber(this);
		
	   /* PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);
	    AlarmManager alarm = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
	    alarm.cancel(pintent);
	    alarm.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),5000, pintent);*/
	}


}
