package com.example.secrets;
//monica's
import java.io.BufferedReader;

import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.view.KeyEvent;

public class MainActivity extends Activity {

	private static final String EXTRA_MESSAGE = "com.example.secrets.MESSAGE";
	private static final int DIALOG_ALERT = 10;
	private static int authenticationTrials = 0;
	private boolean isEditEnabled = false;
	private boolean isDeleteEnabled = false;
	private AlertDialog passwordAlertDialog;
	private EditText passwordInput;
	private String password;
	private static String fileName = "mySecrets";
	private Context context;
	private File mySecretFile;
	private ListView secretsList;
	private SecretsList adapter;
	private Intent saveEditIntent;
	private static final String PREFS_PASS = "PassPref";
	private static boolean passwordIsChecked = false;
	private Bundle saveEditBundle;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		secretsList = (ListView)findViewById(android.R.id.list);
		adapter = new SecretsList(this);
		secretsList.setAdapter(adapter);
		//enabling/disabling of edit and delete buttons
		
		
		secretsList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parentView, View childView,
					int position, long id) {
				// TODO Auto-generated method stub
				int selectedCount = secretsList.getCheckedItemCount();
				if (selectedCount > 0){
					isDeleteEnabled = true;
					if (selectedCount == 1){
						isEditEnabled = true;
					}
					else{
						isEditEnabled = false;
					}
				}
				else{
					isDeleteEnabled = false;
					isEditEnabled = false;
				}
				invalidateOptionsMenu();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parentView) {
				// TODO Auto-generated method stub		
			}
		});
		context = (Context) this;
		String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
		mySecretFile = new File(path);
		//TODO make the following dialog (password prompt) appear only when you launch the application 
		Bundle extras = getIntent().getExtras();
		if(extras != null)
		{
			if(extras.containsKey("Secret"))
			{
				Log.d("mainbundle",extras.getString("Secret"));
				
			}
			else if(extras.containsKey("Cancelled"))
			{
				Log.d("mainbundle","cancelled");
			}
			saveEditBundle = extras;
			displayList();
		}
		else 
		{
			Log.d("mainbundle","cam golut");
		}
		if(extras != null)
		{
			if(extras.getString("Secret")!= null)
			{
				String r = extras.getString("Secret");
				Log.d("listtt", r);
				adapter.addNewSecret(r);
				displayList();
			}
		}
		if(savedInstanceState == null && saveEditBundle == null)
		{
			showDialog(DIALOG_ALERT);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		//Enable/Disable edit button
		MenuItem item = menu.findItem(R.id.action_edit);
		item.setEnabled(isEditEnabled);
		//Enable/Disable delete button
		item = menu.findItem(R.id.action_delete);
		item.setEnabled(isDeleteEnabled);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		//check which ActionBar button was pressed
		switch (item.getItemId()){
		case (R.id.action_add):
			openAdd();
			return true;
		case (R.id.action_edit):
			openEdit();
			return true;
		case (R.id.action_delete):
			deleteSecrets();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	//it saves smth=> bundle is not null
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		Log.d("dialog", "onSaveInstance called");
		Log.d("dialog", outState+"");
	}
	@Override
	//it is never called
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}


	private void openAdd(){
		Intent intent = new Intent(this, SaveEditActivity.class);
		intent.putExtra("ComingFrom", "0");
		intent.putExtra("FileName", fileName);
		startActivity(intent);
		Log.d("intent", "here");
		Log.d("intent", getIntent().getExtras()+"");
//		Bundle extras = getIntent().getExtras();
//		if(extras != null)
//		{
//			String r = extras.getString("Secret");
//			Log.d("listtt", r);
//			adapter.addNewSecret(r);
//			displayList();
//		}
	}
	
	private void openEdit(){
		Intent intent = new Intent(this, SaveEditActivity.class);
		//get selected item's message
		Secret selectedSecret = (Secret)adapter.getItem(secretsList.getSelectedItemPosition());
		String intentMessage = selectedSecret.getDescription();
		intent.putExtra(EXTRA_MESSAGE, intentMessage);
		startActivity(intent);
	}
	
	private void deleteSecrets(){
		// TODO Put delete functionality here
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		// TODO Auto-generated method stub
		switch (id) {
		case DIALOG_ALERT:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			LayoutInflater inflater = this.getLayoutInflater();
			View dialogView = inflater.inflate(R.layout.dialog_password, null);
			passwordInput = (EditText) dialogView.findViewById(R.id.password);

			builder.setTitle(R.string.passwordAlertDialog).setView(dialogView)
					.setPositiveButton("Ok", new OkOnClickListener())
					.setNegativeButton("Cancel", new CancelOnClickListener())
.setOnKeyListener(new DialogInterface.OnKeyListener() {
		        @Override
		        public boolean onKey (DialogInterface dialog, int keyCode, KeyEvent event) {
		            if (keyCode == KeyEvent.KEYCODE_BACK && 
		                event.getAction() == KeyEvent.ACTION_UP && 
		                !event.isCanceled()) {
		                dialog.cancel();
		               exitApplication();
		                return true;
		            }
		            return false;
		        }
		    });

			passwordAlertDialog = builder.create();
			passwordAlertDialog.show();
		}
		return super.onCreateDialog(id);

	}

	private final class OkOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			password = passwordInput.getText().toString();
			checkPassword(password);
		}
	}

	private final class CancelOnClickListener implements
			DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			exitApplication();
		}
	}

	public void checkPassword(String password) {

		if (!mySecretFile.exists()) {
			try {
				
				FileOutputStream outputStream;
				outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
				outputStream.write(password.getBytes());
				outputStream.close();

				Toast message = Toast.makeText(context,	"Your password has been saved.", 100);
				message.show();
				this.passwordIsChecked = true;
				 SharedPreferences preferences = getPreferences(MODE_PRIVATE);
				 //this.passwordIsChecked = preferences.setBoolean("passwordIsChecked", true); // value to store 
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else {
			FileInputStream inputStream;
			InputStreamReader inputStreamReader;
			BufferedReader bufferedReader;
			StringBuilder sb = new StringBuilder();
			String line;
			try {
				
				inputStream = context.openFileInput(fileName);
				inputStreamReader = new InputStreamReader(inputStream);
				bufferedReader = new BufferedReader(inputStreamReader);
//				while((line = bufferedReader.readLine()) != null)
//					Log.d("pass", line+" liiiine");

				if ((line = bufferedReader.readLine()) != null) {
					Log.d("pass", line+" fiiirst line");
					Log.d("pass", password+" paaassword");
					
					if (line.equalsIgnoreCase(password)) {
						this.passwordIsChecked = true;
						passwordAlertDialog.dismiss();
						displayList();
					} 
					else {
						Toast message = Toast.makeText(context,
								"Incorrect password!", 300);
						message.show();
						authenticationTrials++;

						if (authenticationTrials == 3) {
							exitApplication();
						} 
						else {
							showDialog(DIALOG_ALERT);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void displayList(){
		
		adapter.removeSecrets();
		Log.d("listtt", adapter.getCount()+"");
		
		try {
			FileInputStream inputStream;
			InputStreamReader inputStreamReader;
			BufferedReader bufferedReader;
			StringBuilder sb = new StringBuilder();
			String line;
			
			inputStream = context.openFileInput(fileName);
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			int i = 0;
			while ((line = bufferedReader.readLine()) != null) {
				if(i != 0){
					adapter.addNewSecret(line);
				}
				else Log.d("pass", "firstLine "+line);
				i++;
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void exitApplication(){
		MainActivity.this.finish();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		this.authenticationTrials = 0;
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		  SharedPreferences preferences = getPreferences(MODE_PRIVATE);
		  this.passwordIsChecked = preferences.getBoolean("passwordIsChecked", false); // value to store    
		  Log.d("dialog", "on resume "+this.passwordIsChecked);
//		  if(!this.passwordIsChecked)
//			  showDialog(DIALOG_ALERT);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	Log.d("dialog", "back is pressed");
	        exitApplication();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}



	
}