package com.example.apptestephysio;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

public class Simulacao2Activity extends Activity {


	BarChart chart;
	
	ArrayList<BarEntry> entries;
	ArrayList<String> labels;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simulacao);
		
		chart = (BarChart) findViewById(R.id.chart);
		BarTask();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}
	
	private void BarTask() {
		new AsyncTask<Void, Void , Void >() {

			@Override
			protected Void doInBackground(Void... params) {
				entries = new ArrayList<BarEntry>();
				labels = new ArrayList<String>();
				
				Random r = new Random();
				int tamanho = r.nextInt(9);
		
				for(int i = 0; i < tamanho; i++){
					r = new Random();
					entries.add(new BarEntry((float) r.nextInt(300), i));
					labels.add("Valor: " + String.valueOf(i)); 
				}
				
				return null;
			}

			@Override
			protected void onPostExecute(final Void result) {
				
				runOnUiThread(new Runnable() {
					public void run() {
						
//						chart = (BarChart) findViewById(R.id.chart);
						chart.clear();
						
						BarDataSet dataset = new BarDataSet(entries, "# Valores");
						BarData data = new BarData(labels, dataset);
						chart.setData(data);
						
						
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						BarTask();
					}
				});

			}
		}.execute(null, null, null);
	}
	

}
