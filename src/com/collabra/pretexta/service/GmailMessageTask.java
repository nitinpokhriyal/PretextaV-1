package com.collabra.pretexta.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


import org.json.JSONException;

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
import com.google.api.services.gmail.model.ListThreadsResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.Thread;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class GmailMessageTask extends AbstractGetNameTask{

	 private static final String GMAIL_SCOPE = "oauth2:https://www.googleapis.com/auth/gmail.readonly";
		private static final String APP_NAME = "GmailApiTest";
		
		
		  public GmailMessageTask(AuthActivity activity, String email, String scope) {
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
		  
		  
		  protected void fetchNameFromProfileServer() throws IOException, JSONException {
		        String token = fetchToken();
		        if (token == null) {
		          // error has already been handled in fetchToken()
		          return;
		        }
		        try{
		    		List<Message> m=null;
			        GoogleCredential credential = new GoogleCredential().setAccessToken(token);
			        HttpTransport httpTransport = new NetHttpTransport();
					JsonFactory jsonFactory = new JacksonFactory();
					Gmail mailService= new Gmail.Builder(httpTransport, jsonFactory,credential).setApplicationName(APP_NAME).build();
					try {
						/*ListMessagesResponse messagesRespose = mailService.users().messages().list("me").setMaxResults(Long.parseLong("10"))
								.execute();*/
						  ArrayList<String> ids = new ArrayList<String>();
						    ids.add("INBOX");
						    ListMessagesResponse messagesRespose= mailService.users().messages().list("me").setLabelIds(ids).setQ("From: sanjaygu@gmail.com").setMaxResults(Long.parseLong("10"))
						            .execute();
						 m = messagesRespose.getMessages();
						 String mText=null;
						 Log.i("PRETEXTA MESSAGE calling message start",""+m.size());
						for(Message message:m){
							Log.i("PRETEXTA MESSAGE Before bare", "");
							if(message==null){
								Log.i(" message is null" ,"");
							}else{
								mText=getBareMessageDetails(message.getId(),mailService);
								
							}
						}
						Log.i("PRETEXTA MESSAGE calling message start",mText);
						//getFirstName(mText);
				         mActivity.show("Hello " + mText + "!");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
		        }catch (Exception e) {
		        	e.printStackTrace();
		        }
		     /*   int sc = con.getResponseCode();
		        if (sc == 200) {
		          InputStream is = con.getInputStream();
		          String name = getFirstName(readResponse(is));
		          mActivity.show("Hello " + name + "!");
		          is.close();
		          return;
		        } else if (sc == 401) {
		            GoogleAuthUtil.invalidateToken(mActivity, token);
		            onError("Server auth error, please try again.", null);
		            Log.i(TAG, "Server auth error: " + readResponse(con.getErrorStream()));
		            return;
		        } else {
		          onError("Server returned the following error code: " + sc, null);
		          return;
		        }
		   */ }
		
		// Gets oauth2 token using Play Services SDK and runs connectIMAP task after
			// receiving token
			/*public class getAuthToken extends AsyncTask<Void, Void, List<Message>> {

				@Override
				protected List<Message> doInBackground(Void... params) {
					try {
						List<Message> m=null;
						String token = GoogleAuthUtil.getToken(mActivity,
								"nitinpokhriyal@gmail.com", GMAIL_SCOPE);
						GoogleCredential credential = new GoogleCredential()
								.setAccessToken(token);
						HttpTransport httpTransport = new NetHttpTransport();
						JsonFactory jsonFactory = new JacksonFactory();
						Gmail mailService= new Gmail.Builder(httpTransport, jsonFactory,
								credential).setApplicationName(APP_NAME).build();
						try {
							ListMessagesResponse messagesRespose = mailService.users().messages().list("me").setMaxResults(Long.parseLong("1"))
									.execute();
							 m = messagesRespose.getMessages();
						
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return m;
					} catch (UserRecoverableAuthException e) {
						//startActivityForResult(e.getIntent(), 1);
					} catch (IOException e) {
						e.printStackTrace();
					} catch (GoogleAuthException e) {
						e.printStackTrace();
					}
					return null;
				}

				@Override
				protected void onPostExecute(List<Message> result) {
					if (result != null) {
						for(Message message:result){
							Log.d("GMAIL MESSAGE", message.getSnippet());
						}
					}
				}

			}
*/		
		  
		  public static String getBareMessageDetails(String messageId,Gmail service) {
		      Map<String, Object> messageDetails = new HashMap<String, Object>();
		      String text=null;
		      try {
		          Message message = service.users().messages().get("me", messageId).setFormat("raw").execute();
		          Properties props = new Properties();
		          //Session session = Session.getDefaultInstance(props, null);
		          System.out.println(message.getSnippet());
		          Log.i("PRETEXTA MESSAGE", message.getSnippet());
		          /*byte[] emailBytes = Base64.decodeBase64(message.getRaw());
		          MimeMessage email = new MimeMessage(session, new ByteArrayInputStream(emailBytes));
		         System.out.println(email.getSubject());*/
		          text=message.getSnippet();
		      }  catch (IOException ex) {
		      }
		      return text;
		  }
}
