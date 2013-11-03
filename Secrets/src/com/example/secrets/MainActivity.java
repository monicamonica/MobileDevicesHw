package com.example.secrets;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private static final int DIALOG_ALERT = 10;
	private static int authenticationTrials = 0;
	private AlertDialog passwordAlertDialog;
	private EditText passwordInput;
	private String password;
	private static String fileName = "mySecrets";
	private Context context;
	private File mySecretFile;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		context = (Context) this;
		String path = context.getFilesDir().getAbsolutePath() + "/" + fileName;
		mySecretFile = new File(path);

		showDialog(DIALOG_ALERT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
					.setNegativeButton("Cancel", new CancelOnClickListener());
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
			MainActivity.this.finish();
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		}
	}

	public void checkPassword(String password) {

		if (!mySecretFile.exists()) {
			try {
				FileOutputStream outputStream;
				outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
				outputStream.write(password.getBytes());
				outputStream.close();

				Toast message = Toast.makeText(context,
						"Your password has been saved.", 100);
				message.show();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			FileInputStream inputStream;
			InputStreamReader inputStreamReader;
			BufferedReader bufferedReader;
			StringBuilder sb = new StringBuilder();
			String line;

			try {
				inputStream = context.openFileInput(fileName);
				inputStreamReader = new InputStreamReader(inputStream);
				bufferedReader = new BufferedReader(inputStreamReader);

				if ((line = bufferedReader.readLine()) != null) {
					Log.d("pass", line);
					Log.d("pass", password);
					if (line.equalsIgnoreCase(password)) {
						Toast message = Toast.makeText(context,
								"Your password is correct.", 200);
						message.show();
						this.setContentView(R.layout.activity_main);
					} 
					else {
						Toast message = Toast.makeText(context,
								"Incorrect password!", 300);
						message.show();
						authenticationTrials++;

						if (authenticationTrials == 3) {
							MainActivity.this.finish();
							Intent intent = new Intent(Intent.ACTION_MAIN);
							intent.addCategory(Intent.CATEGORY_HOME);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
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
}
