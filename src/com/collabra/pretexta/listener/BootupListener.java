package com.collabra.pretexta.listener;

import com.collabra.pretexta.service.SchedulerTask;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootupListener extends BroadcastReceiver {
	@Override
	public void onReceive(Context ctx, Intent intent) {
		
		Intent intent1=new Intent(ctx, SchedulerTask.class);
		intent1.putExtra("nitin", "start service");
		ctx.startService(intent1);
	}

}
