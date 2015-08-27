package com.example.guanoweather;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import com.example.guanoweather.adapter.GridCityMAdapter;
import com.example.guanoweather.bean.CityManagerBean;

public class CityManagerActivity extends Activity {

	private GridView mgridview;
	private List<CityManagerBean> mcmb;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gridview_activity);
		mcmb = new ArrayList<CityManagerBean>();
//		CityManagerBean F = FragmentHomeContent.cmb;
//		mcmb.add(F);
		mgridview = (GridView) findViewById(R.id.gridview);
		mgridview.setNumColumns(3);
		mgridview.setBackgroundResource(R.drawable.selectedcity);
		mgridview.setAdapter(new GridCityMAdapter(this, mcmb));
	}
}
