/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.collabra.pretexta.activity;

import java.io.IOException;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.collabra.pretexta.R;
import com.collabra.pretexta.db.DataBaseHelper;
import com.collabra.pretexta.service.SchedulerTask;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        //String[] items = getResources().getStringArray(R.array.main_activity_items);
       // this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items));
        
        setContentView(R.layout.main);
        
        DataBaseHelper helper= new DataBaseHelper(this);
        try {
			helper.createDataBase();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.e("Pretexta-MainActivity", "Error reading database");
			e.printStackTrace();
		}
    }

   /* @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        // Highly coupled with the order of contents in main_activity_items
        Intent intent = new Intent(this, AuthActivity.class);
        if (position == 0) {
            intent.putExtra(AuthActivity.TYPE_KEY, AuthActivity.Type.FOREGROUND.name());
        } else if (position == 1) {
            intent.putExtra(AuthActivity.TYPE_KEY, AuthActivity.Type.BACKGROUND.name());
        } else if (position == 2) {
            intent.putExtra(AuthActivity.TYPE_KEY, AuthActivity.Type.BACKGROUND_WITH_SYNC.name());
        }
        startActivity(intent);
    }*/
    
    public void syncData(View v){
    	Context ctx=v.getContext();
    	Intent intent1=new Intent(ctx, SchedulerTask.class);
		intent1.putExtra("nitin", "start service");
		this.startService(intent1);
    }
}
