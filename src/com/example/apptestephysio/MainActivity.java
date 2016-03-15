package com.example.apptestephysio;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MainActivity extends Activity {

	private BluetoothAdapter mBluetoothAdapter;
	
	Handler bluetoothIn;
	final int handlerState = 0;
	
	boolean sendUdp;
	String udpOutputData;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mBluetoothAdapter	= BluetoothAdapter.getDefaultAdapter();
		
		Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
		final List<BluetoothDevice> list = new ArrayList<BluetoothDevice>();
		list.addAll(pairedDevices);
		
		final ListView listDevices  = (ListView) findViewById(R.id.listDevices);
		DeviceAdapter adapter = new DeviceAdapter(getApplicationContext(), list);
		listDevices.setAdapter(adapter);
		
		listDevices.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent,View view, int position, long id) {

				BluetoothDevice device = (BluetoothDevice) listDevices.getItemAtPosition(position);
				Intent i = new Intent(getApplicationContext(), SimulacaoActivity.class);
				i.putExtra(SimulacaoActivity.EXTRA_DEVICE_ADDRESS, device.getAddress());
				startActivity(i);
			}
		});
		
		String s = "hello from app";
		sendUdp(s);

	}
	
	public void sendUdp(String udpMsg) {
		udpOutputData = udpMsg;
		sendUdp = true;
		udpSendThread.start();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	Thread udpSendThread = new Thread(new Runnable() {

		@Override
		public void run() {

			while (true) {
				try {
					Thread.sleep(100);
				}
				catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (sendUdp == true) {
					try {
						// get server name
						InetAddress serverAddr = InetAddress.getByName("224.0.0.224");
						Log.d("UDP", "C: Connecting...");
						// create new UDP socket
						DatagramSocket socket = new DatagramSocket();
						// prepare data to be sent
						byte[] buf = udpOutputData.getBytes();
						// create a UDP packet with data and its destination ip
						// & port
						DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddr, 7778);
						Log.d("UDP", "C: Sending: '" + new String(buf) + "'");
						// send the UDP packet
						socket.send(packet);
						socket.close();
						Log.d("UDP", "C: Sent.");
						Log.d("UDP", "C: Done.");
					}
					catch (Exception e) {
						Log.e("UDP", "C: Error", e);

					}

					try {
						Thread.sleep(100);
					}

					catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					sendUdp = false;
				}

			}
		}

	});

}
