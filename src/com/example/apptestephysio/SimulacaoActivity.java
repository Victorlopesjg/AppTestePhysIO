package com.example.apptestephysio;

import java.io.IOException;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import dca.ufrn.team.physio.utils.PhysioConnect;

public class SimulacaoActivity extends Activity {
	
	Handler bluetoothIn;
	final int handlerState = 0;
	
	private StringBuilder recDataString = new StringBuilder();
	
	private BluetoothSocket btSocket = null;
	private ConnectedThread mConnectedThread;
	
	public static final String EXTRA_DEVICE_ADDRESS = "ADDRESS";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simulacao);

		try {
			Intent intent = getIntent();
			String address = intent.getStringExtra(EXTRA_DEVICE_ADDRESS);
			btSocket = PhysioConnect.createBluetoothSocket(address);
			btSocket.connect();

			mConnectedThread = new ConnectedThread(btSocket);
			mConnectedThread.start();
		} catch (IOException e) {
			Toast.makeText(getBaseContext(), "Socket creation failed", Toast.LENGTH_LONG).show();
			try {
				btSocket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
		
		mConnectedThread.write("#sd");

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		bluetoothIn = new Handler() {
			public void handleMessage(android.os.Message msg) {
				String readMessage = (String) msg.obj; 
				recDataString.append(readMessage);
				Log.d("MSG BLUETOOTH", readMessage);
				Toast.makeText(getApplicationContext(), readMessage, 20).show();

			}
		};

	}
	
	// create new class for connect thread
	private class ConnectedThread extends Thread {
		final PhysioConnect physioConnect; 
		// creation of the connect thread
		public ConnectedThread(BluetoothSocket socket) throws IOException {	
			physioConnect = new PhysioConnect(btSocket);
			physioConnect.writePhysio("#sd");

		}

		public void run() {
			byte[] buffer = new byte[256];
//			int bytes;

			// Keep looping to listen for received messages
			while (true) {
				try {
					String readMessage = physioConnect.readPhysio();
					bluetoothIn.obtainMessage(handlerState, physioConnect.getInStream().read(buffer), -1, readMessage).sendToTarget();
				} catch (IOException e) {
					break;
				}
			}
		}

		// write method
		public void write(String input) {
			try {
				physioConnect.writePhysio(input);
			} catch (IOException e) {
				Toast.makeText(getBaseContext(), "Connection Failure", Toast.LENGTH_LONG).show();
				finish();

			}
		}
	}

}
