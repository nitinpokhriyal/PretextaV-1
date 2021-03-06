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

package com.collabra.pretexta.service;

import com.collabra.pretexta.activity.AuthActivity;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.UserRecoverableNotifiedException;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.List;

/**
 * This example shows how to fetch tokens if you are creating a background task. It also shows how
 * you can create a broadcast receiver and create a callback for it.
 */
public class GetNameInBackground extends AbstractGetNameTask {

  public GetNameInBackground(AuthActivity activity, String email, String scope) {
      super(activity, email, scope);
  }

  /**
   * Get a authentication token if one is not available. If the error was recoverable then a
   * notification will automatically be pushed. The callback provided will be fired once the
   * notification is addressed by the user successfully. If the error is not recoverable then
   * it displays the error message on parent activity.
   */
  @Override
  protected String fetchToken() throws IOException {
      try {
          return GoogleAuthUtil.getTokenWithNotification(
                  mActivity, mEmail, mScope, null, makeCallback(mEmail));
      } catch (UserRecoverableNotifiedException userRecoverableException) {
          // Unable to authenticate, but the user can fix this.
          // Because we've used getTokenWithNotification(), a Notification is
          // created automatically so the user can recover from the error
          onError("Could not fetch token.", null);
      } catch (GoogleAuthException fatalException) {
          onError("Unrecoverable error " + fatalException.getMessage(), fatalException);
      }
      return null;
  }

  private Intent makeCallback(String accountName) {
      Intent intent = new Intent();
      intent.setAction("com.collabra.pretexta.service.Callback");
      intent.putExtra(AuthActivity.EXTRA_ACCOUNTNAME, accountName);
      intent.putExtra(AuthActivity.TYPE_KEY, AuthActivity.Type.BACKGROUND.name());
      return intent;
  }

  /**
   * Note: Make sure that the receiver can be called from outside the app. You can do that by adding
   * android:exported="true" in the manifest file.
   */
  public static class CallbackReceiver extends BroadcastReceiver {
    public static final String TAG = "CallbackReceiver";

    @Override
    public void onReceive(Context context, Intent callback) {
        Bundle extras = callback.getExtras();
        Intent intent = new Intent(context, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtras(extras);
        Log.i(TAG, "Received broadcast. Resurrecting activity");
        context.startActivity(intent);
    }
  }
		
}
