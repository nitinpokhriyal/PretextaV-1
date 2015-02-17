package com.collabra.pretexta.service;

import java.util.Calendar;
import java.util.Date;

import com.collabra.pretexta.db.DataBaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.CallLog;
import android.util.Log;
/**
 * 
 * Move contact and sms data to pretexta table
 * **/

public class SyncDataHelper{
	
	 	  
	 /* private String getSMSDetails(String contactNumber,Context context) {
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
*/
}
