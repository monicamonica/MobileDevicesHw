package com.example.secrets;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SecretsList extends BaseAdapter{
	
	private Activity context;
	ArrayList<Secret> secrets; 
	
	public SecretsList(Activity context){
		this.context = context;
		secrets = new ArrayList<Secret>();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return secrets.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return secrets.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		View element;
		if (convertView == null) {
			LayoutInflater inflater = context.getLayoutInflater();
			element = inflater.inflate(R.layout.secret_row, null);
		} else
			element = convertView;

		TextView description = (TextView) element.findViewById(R.id.secret);
	

		description.setText(secrets.get(position).description);


		return element;
		
	}
	
	public void addNewSecret(String description){
		Secret secret = new Secret(description);
		secrets.add(secret);
		this.notifyDataSetChanged();
	}
	
	public void removeSecrets(){
		secrets.clear();
	}

}
