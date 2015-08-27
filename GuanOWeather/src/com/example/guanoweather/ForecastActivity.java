package com.example.guanoweather;

import java.util.ArrayList;
import java.util.List;

import com.example.guanoweather.adapter.ListWeatherAdapter;
import com.example.guanoweather.bean.WeatherSubBean;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.components.XAxis.XAxisPosition;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

public class ForecastActivity extends Activity {
	private ListView tweatherlist;
	private LineChart mLineChart;
	private ArrayList<String> xVals;
	private ArrayList<Entry> yValsH;
	private ArrayList<Entry> yValsL;
	private List<WeatherSubBean> tweather;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forecast_activity);
		mLineChart = (LineChart) findViewById(R.id.chart);
		getWeek();
		setLineChart(xVals, yValsH, yValsL);
	
		tweatherlist = (ListView)findViewById(R.id.tomorrow_weather);
		Log.i("TAG", tweatherlist + "-->tweatherlist");
		tweatherlist.setAdapter(new ListWeatherAdapter(this,
				HomePagerActivity.response.getResults().get(0)
						.getWeather_data()));
	}
	
	/**
	 * 获得星期数据
	 * @param futures
	 * @return
	 */
	private void getWeek(){
		xVals = new ArrayList<String>();
		yValsH = new ArrayList<Entry>();
		yValsL = new ArrayList<Entry>();
		tweather = HomePagerActivity.response.getResults().get(0)
				.getWeather_data();
		int size = tweather.size();
		size = size > 4 ? 4 : size;
		for (int i = 0; i < 4; i++) {
			WeatherSubBean futureWeather = tweather.get(i);
			xVals.add(futureWeather.getDate());
			String temp = futureWeather.getTemperature();
			String[] temps = temp.split("~ ", 2);
			temps[0] = temps[0].substring(0, temps[0].length() - 1);
			temps[1] = temps[1].substring(0, temps[1].length() - 1);
			Float tempFloat1 = Float.parseFloat(temps[0]);
			Float tempFloat2 = Float.parseFloat(temps[1]);
			yValsL.add(new Entry(tempFloat2, i));
			yValsH.add(new Entry(tempFloat1, i));
		}
	}
	
	private void setLineChart(ArrayList<String> xVals, ArrayList<Entry> yValsH,
			ArrayList<Entry> yValsL) {
		// 添加数据
		LineDataSet yLDSH = new LineDataSet(yValsH, "高温线");
		yLDSH.setColor(Color.RED);
		yLDSH.setCircleColor(Color.WHITE);
		yLDSH.setLineWidth(2f);
		yLDSH.setCircleSize(3f);

		LineDataSet yLDSL = new LineDataSet(yValsL, "低温线");
		yLDSL.setColor(Color.BLUE);
		yLDSL.setCircleColor(Color.WHITE);
		yLDSH.setLineWidth(2f);
		yLDSH.setCircleSize(3f);

		// 设置数据
		LineData lineData = new LineData(xVals);
		lineData.addDataSet(yLDSL);
		lineData.addDataSet(yLDSH);
		// 去掉边框
		mLineChart.setDrawBorders(false);
		// 右下角描述
		mLineChart.setDescription("");
		// 没有数据描述
		mLineChart.setNoDataTextDescription("没有天气数据");
		// 绘制网格布局
		mLineChart.setDrawGridBackground(false);
		// 网格背景
		mLineChart.setGridBackgroundColor(Color.TRANSPARENT);
		// 背景颜色
		mLineChart.setBackgroundColor(Color.TRANSPARENT);
		// 设置触摸
		mLineChart.setTouchEnabled(false);
		// 设置放大
		mLineChart.setScaleEnabled(false);
		// 设置拖动
		mLineChart.setDragEnabled(false);
		//动画时间
		mLineChart.animateX(1500);
		
		// 设置数据
		mLineChart.setData(lineData);

		// 设置轴线
		XAxis xAxis = mLineChart.getXAxis();
		// 绘制轴线
		xAxis.setDrawAxisLine(false);
		// 绘制网格
		xAxis.setDrawGridLines(true);
		// 设置文本颜色
		xAxis.setTextColor(Color.BLACK);
		// 设置文本大小
		xAxis.setTextSize(9f);
		// 设置位置
		xAxis.setPosition(XAxisPosition.BOTTOM);

		YAxis yAxisLeft = mLineChart.getAxisLeft();
		// 绘制轴线
		yAxisLeft.setDrawAxisLine(true);
		// 绘制网格
		yAxisLeft.setDrawGridLines(false);
		// 设置文本颜色
		yAxisLeft.setTextColor(Color.BLACK);
		// 设置文本大小
		yAxisLeft.setTextSize(9f);
		// 设置最大值
		yAxisLeft.setAxisMaxValue(60);
		// 设置最小值
		yAxisLeft.setAxisMinValue(-60);

		YAxis yAxisRight = mLineChart.getAxisRight();
		// 绘制轴线
		yAxisRight.setDrawAxisLine(true);
		// 绘制网格
		yAxisRight.setDrawGridLines(false);
		// 设置文本颜色
		yAxisRight.setTextColor(Color.BLACK);
		// 设置文本大小
		yAxisRight.setTextSize(9f);
		// 设置最大值
		yAxisRight.setAxisMaxValue(60);
		// 设置最小值
		yAxisRight.setAxisMinValue(-60);
		// 刷新数据
		mLineChart.invalidate();
	}
	
}
