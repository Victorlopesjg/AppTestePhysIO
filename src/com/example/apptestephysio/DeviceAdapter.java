package com.example.apptestephysio;

import java.util.List;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DeviceAdapter extends BaseAdapter {

	private Context context;
	private List<BluetoothDevice> listaDevices;
	
	public DeviceAdapter(Context context, List<BluetoothDevice> listaDevices) {
		this.context = context;
		this.listaDevices = listaDevices;
	}

	@Override
	public int getCount() {
		return listaDevices.size();
	}

	@Override
	public Object getItem(int position) {
		return listaDevices.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		convertView = mInflater.inflate(R.layout.item_list_devices, null);
		
		TextView txtNome = (TextView) convertView.findViewById(R.id.txtNomeDevice);
		txtNome.setText(String.valueOf(listaDevices.get(position).getName()));
		
		TextView txtAddress = (TextView) convertView.findViewById(R.id.txtAddress);
		txtAddress.setText(listaDevices.get(position).getAddress());
			
		return convertView;
	}

}
