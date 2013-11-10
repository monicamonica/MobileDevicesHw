package com.example.secrets;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.io.BufferedWriter;
import java.io.FileWriter;

//import com.example.secrets.MainActivity.CancelOnClickListener;
//import com.example.secrets.MainActivity.OkOnClickListener;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;

public class SaveEditActivity extends Activity{
	
	private Button saveEdit, cancel;
	private EditText newSecret;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_edit);
		// Show the Up button in the action bar.
		setupActionBar();
		saveEdit = (Button)findViewById(R.id.saveEditButton);
		cancel = (Button)findViewById(R.id.cancelButton);
		newSecret = (EditText)findViewById(R.id.newSecret);
		final Intent sender = getIntent();
		final String option = sender.getExtras().getString("ComingFrom");
		if(option.contentEquals("0")==true)
			saveEdit.setText("Add");
		else 
			saveEdit.setText("Edit");
		saveEdit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(option.contentEquals("0")==true)
				{				
					String text = newSecret.getText().toString();
			    	//adding to file
					
					try {
		
						FileOutputStream outputStream;
						outputStream = openFileOutput(sender.getExtras().getString("FileName"), Context. MODE_APPEND);
						String separator = System.getProperty("line.separator");
						outputStream.write(separator.getBytes());
						outputStream.write(text.getBytes());
						outputStream.close();
						Toast message = Toast.makeText(SaveEditActivity.this, "Your secret is safe with me", 100);
						message.show();
						Intent intent = new Intent( SaveEditActivity.this, MainActivity.class);
						intent.putExtra("Secret", text);					
						startActivity(intent);
					
					} catch (IOException e) {
						// TODO Auto-generated catch block
						Log.d("exception", "io "+e.getMessage());
					}
				}
					//reading
					
					
				else
				{
					editAction(sender);
					saveEdit.setText("Edit");
				}
			}
		});
		
	cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent( SaveEditActivity.this, MainActivity.class);
				intent.putExtra("Cancelled", "true");					
				startActivity(intent);
			}
		});
		
	}
	
	private void addAction(Intent intent){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = this.getLayoutInflater();
		View dialogView = inflater.inflate(R.layout.dialog_save_edit, null);
//		String passwordInput = (EditText) dialogView.findViewById(R.id.saveEdit).get.toString();

		builder.setTitle(R.string.saveEditDialog)
				.setView(dialogView)
				.setPositiveButton("Save", new OkOnClickListener())
				.setNegativeButton("Cancel", new CancelOnClickListener());
	
		//saveEditAlertDialog = builder.create();
		//saveEditAlertDialog.show();
		
		/*
		 	// Set up the buttons
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		    	
		    	Intent intent = new Intent( SaveEditActivity.this, MainActivity.class);
				intent.putExtra("Secret", text);
				
				
				startActivity(intent);
				//finish();
		    }
		});
		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        dialog.cancel();
		    }
		});

		builder.show();
		
	}
	
		 */
	}
	
	private final class OkOnClickListener implements	DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int which) {
			
		
		}
	}

private final class CancelOnClickListener implements
	DialogInterface.OnClickListener {
public void onClick(DialogInterface dialog, int which) {
	//exitApplication();
	Intent backToMenu = new Intent();
	setResult(RESULT_OK,backToMenu);
	finish();
}}

	
	private void editAction(Intent intent){
		
	}
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
			System.out.println("setupactionbar");
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.save_edit, menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//switch (item.getItemId()) {
		//case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			//NavUtils.navigateUpFromSameTask(this);
			//System.out.println(item.getItemId());
			//return true;
		//}
		return super.onOptionsItemSelected(item);
	}
	
}
