package com.example.guanoweather;

import java.io.IOException;

import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.guanoweather.bean.CityManagerBean;
import com.example.guanoweather.bean.MHttpEntity;
import com.example.guanoweather.bean.ResponseWrapper;
import com.example.guanoweather.bean.SendDataBean;
import com.google.gson.GsonBuilder;

public class HomePagerActivity extends FragmentActivity implements
		OnClickListener, FragmentAndActivity {

	private long nowtime;// 保存当前时间
	public static TextView bottom_weathertext;// 底部天气预报
	public static ResponseWrapper response = new ResponseWrapper();// 数据结构的对象
	public static ResponseWrapper response2;
	public static TextView bottom_citymanager;// 底部
	public static TextView bottom_todaycan;// 底部
	private DrawerLayout drawerlayout_main;// drawerlayout_main
	private View left_drawer;
	private EditText inputcity;//
//	private String inputcitytext;
	public static final int succeed = 1;
	public static final int fail = 2;
	public static final int nonet = 3;
	private static int tag = 0;
	public static String TAG_H = null;
	private ProgressDialog pDialog;
	public static FragmentHomeContent homecontent = new FragmentHomeContent();
	public FragmentCityManager citymanager = new FragmentCityManager();
	public static CityManagerBean cmb2 = new CityManagerBean();
	
	GestureDetector detector;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.homepager_main_activity);
		
		detector=new GestureDetector(new OnGestureListener() {
			
			@Override
			public boolean onSingleTapUp(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onShowPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
					float distanceY) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public void onLongPress(MotionEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				// TODO Auto-generated method stub
				// e1 触摸的起始位置，e2 触摸的结束位置，velocityX X轴每一秒移动的像素速度（大概这个意思） velocityY 就是Ｙ咯
	            //手势左,上为正 ——，右，下为负正
	            if (e2.getX()-e1.getX()>50) {
	                //为什么是50？ 这个根据你的模拟器大小来定，看看模拟器宽度，e2.getX()-e1.getX()<屏幕宽度就ＯＫ
	              //  Toast.makeText(getApplicationContext(), "向右滑动", Toast.LENGTH_LONG).show();
	                //要触发什么事件都在这里写就OK
	                
	            }
	            if (Math.abs(e2.getX()-e1.getX())>50) {
	               // Toast.makeText(getApplicationContext(), "向左滑动", Toast.LENGTH_LONG).show();
	            }
	            if (Math.abs(e2.getY()-e1.getY())>50) {
	              //  Toast.makeText(getApplicationContext(), "向上滑动", Toast.LENGTH_LONG).show();
	                Intent intent=new Intent(HomePagerActivity.this, ForecastActivity.class);
	                startActivity(intent);
	                overridePendingTransition(R.anim.shanghua, R.anim.xiahua);
	            }
	            if (e2.getY()-e1.getY()>50) {
	               // Toast.makeText(getApplicationContext(), "向下滑动", Toast.LENGTH_LONG).show();
	            }
				return false;
			}
			
			@Override
			public boolean onDown(MotionEvent e) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		DrawerLayout layout=(DrawerLayout)findViewById(R.id.drawerlayout_main);
        layout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				return detector.onTouchEvent(event);
			}
		});
        layout.setLongClickable(true);
		
		Intent intent = getIntent();
		String wetherdata = intent.getStringExtra("weather_data");// 得到启动页传递过来的数据
		GsonBuilder gson = new GsonBuilder();//
		response2 = gson.create().fromJson(wetherdata, ResponseWrapper.class);
		if(response2.getError() == 0){
			response = response2;
		}
		
		//触摸屏幕监听
	    class TouhListener implements OnTouchListener{

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				//Toast.makeText(getApplicationContext(), "----?", event.getAction()).show();
	            return detector.onTouchEvent(event);
			}
	         
	    }
	    
	   
		initview();

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.replace(R.id.fragmentlayout, homecontent, FragmentHomeContent.TAG);
		ft.commit();

		bottom_weathertext.setTextColor(Color
				.parseColor(getString(R.string.color_bottombg)));
	}

	private void initview() {
		bottom_weathertext = (TextView) findViewById(R.id.bottom_weathertext);
		bottom_todaycan = (TextView) findViewById(R.id.bottom_todaycan);
		bottom_citymanager = (TextView) findViewById(R.id.bottom_citymanager);
		drawerlayout_main = (DrawerLayout) findViewById(R.id.drawerlayout_main);
		left_drawer = findViewById(R.id.left_drawer);
		drawerlayout_main.setScrimColor(0x00000000);// 设置底部页面背景透明度
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.homep_menu:
			openleftlayout();
			break;
		case R.id.homep_refresh:
			showDialog();
			bottom_weathertext.setTextSize(18);
			bottom_citymanager.setTextSize(16);
			bottom_todaycan.setTextSize(16);
			
			bottom_todaycan.setTextColor(Color
					.parseColor(getString(R.string.color_bottombgn)));
			bottom_citymanager.setTextColor(Color
					.parseColor(getString(R.string.color_bottombgn)));
			bottom_weathertext.setTextColor(Color
					.parseColor(getString(R.string.color_bottombg)));
			chagepage(homecontent, FragmentHomeContent.TAG);
			new Thread(new Runnable() {

				@Override
				public void run() {
					sendRequest(FragmentHomeContent.currentcity.getText()
							.toString());
				}
			}).start();
			break;
		case R.id.bottom_todaycan:
			bottom_todaycan.setTextSize(18);
			bottom_citymanager.setTextSize(16);
			bottom_weathertext.setTextSize(16);
			
			bottom_todaycan.setTextColor(Color
					.parseColor(getString(R.string.color_bottombg)));
			bottom_citymanager.setTextColor(Color
					.parseColor(getString(R.string.color_bottombgn)));
			bottom_weathertext.setTextColor(Color
					.parseColor(getString(R.string.color_bottombgn)));
			chagepage(new FragmentTodayCan(), FragmentTodayCan.TAG);
			break;
		case R.id.bottom_citymanager:
			bottom_citymanager.setTextSize(18);
			bottom_weathertext.setTextSize(16);
			bottom_todaycan.setTextSize(16);
			
			bottom_citymanager.setTextColor(Color
					.parseColor(getString(R.string.color_bottombg)));
			bottom_weathertext.setTextColor(Color
					.parseColor(getString(R.string.color_bottombgn)));
			bottom_todaycan.setTextColor(Color
					.parseColor(getString(R.string.color_bottombgn)));
			chagepage(citymanager, FragmentCityManager.TAG);
			break;
		case R.id.bottom_weathertext:
			bottom_weathertext.setTextSize(18);
			bottom_citymanager.setTextSize(16);
			bottom_todaycan.setTextSize(16);
			
			bottom_weathertext.setTextColor(Color
					.parseColor(getString(R.string.color_bottombg)));
			bottom_citymanager.setTextColor(Color
					.parseColor(getString(R.string.color_bottombgn)));
			bottom_todaycan.setTextColor(Color
					.parseColor(getString(R.string.color_bottombgn)));
			chagepage(homecontent, FragmentHomeContent.TAG);
			break;
		case R.id.button1:
			//TODO..
			showToast("开发中...");
			break;
		case R.id.button2:
			//TODO..
			showToast("开发中...");
			break;
		case R.id.button3:
			//TODO..
			showToast("开发中...");
			break;
		case R.id.button4:
			//TODO..
			showToast("开发中...");
			break;
		case R.id.button5:
			//TODO..
			showToast("开发中...");
			break;
		case R.id.button6:
			//TODO..
			showToast("开发中...");
			break;
		case R.id.exitapp:
			final Dialog dialog = new Dialog(this, 
					android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
			View exitappview = getLayoutInflater().inflate
					(R.layout.exitapp_dialog, null);
			TextView exitapp_text = (TextView) exitappview.findViewById(R.id.exitapp_text);
			Button leftbutton = (Button) exitappview.findViewById(R.id.leftbutton);
			Button rightbutton = (Button) exitappview.findViewById(R.id.rightbutton);
			exitapp_text.setText("退出程序");
			leftbutton.setText("确定");
			rightbutton.setText("取消");
			leftbutton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			rightbutton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			dialog.setContentView(exitappview);
			dialog.show();
			break;
		}
	}

	private void fromJson(String wetherdata) {
		GsonBuilder gson = new GsonBuilder();//
		response2 = gson.create().fromJson(wetherdata, ResponseWrapper.class);
		if (response2.getError() == 0) {
			response = response2;
			homecontent.setpagedata();
			if (tag == 4 && inputcity != null) {
				closeinput(inputcity);
			}
		} else if (response2.getError() == -3 || response2.getError() == -2) {
			showToast(getString(R.string.input_truename));
		} else {
			showToast(getString(R.string.getdata_fail));
		}
		if(FragmentHomeContent.pDialog != null){
			FragmentHomeContent.pDialog.dismiss();
		}
	}

	/**
	 * 点击多次bt，Toast只显示一次的解决方案
	 */
	public Toast toast = null;

	public void showToast(String text) {
		if (toast == null) {
			toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
		} else {
			toast.setText(text);
		}
		toast.show();
	}

	/**
	 * 向服务器发送数据请求
	 */
	public void sendRequest(String cityname) {
		String getData = null;
		MHttpEntity mhe = null;
		try {
			SendDataBean.setCity(cityname);// 获取用户输入的城市名
			mhe = MHttpEntity.sendhttpclient(SendDataBean.getData());
			if (mhe.getHentity() != null) {
				getData = EntityUtils.toString(mhe.getHentity());
				mhe.getMessage().obj = getData;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		handler.sendMessage(mhe.getMessage());// 使用Handler对网络状态做处理
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (pDialog != null)
				pDialog.dismiss();
			if (msg != null)
				switch (msg.arg1) {
				case succeed:// 与服务器连接成功
					if (msg.obj != null) {
						fromJson(msg.obj.toString());
					}
					break;
				case fail:// 与服务器连接失败
					showToast(getString(R.string.net_fail));
					break;
				}
		}
	};

	/**
	 * 关联menu键
	 */
	private void openleftlayout() {
		if (drawerlayout_main.isDrawerOpen(left_drawer)) {
			drawerlayout_main.closeDrawer(left_drawer);
		} else {
			drawerlayout_main.openDrawer(left_drawer);
		}
	}

	public void chagepage(Fragment fragment, String str) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();// 开启一个事物，得到事物对象ft
		ft.replace(R.id.fragmentlayout, fragment, str);
		ft.commit();
	}

	/**
	 * 关闭输入法键盘
	 */
	public void closeinput(EditText editText) {
		editText.setText("");
		InputMethodManager imm = (InputMethodManager) 
				getSystemService(HomePagerActivity.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	/**
	 * 连续按两次返回则退出程序
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (System.currentTimeMillis() - nowtime > 2000) {
				Toast.makeText(this, R.string.click_exit, Toast.LENGTH_SHORT)
						.show();
				nowtime = System.currentTimeMillis();
				return true;
			} else {
				finish();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void senddata(EditText inputcity) {
		this.inputcity = inputcity;
	}

	@Override
	public void sendcitytext(final String inputcitytext) {
//		this.inputcitytext = inputcitytext;
		tag = 4;
		if ("".equals(inputcitytext)) {
			showToast(getString(R.string.edittext_hint));
			if(FragmentHomeContent.pDialog != null){
				FragmentHomeContent.pDialog.dismiss();
			}
		} else {
			SendDataBean.setCity(inputcitytext);// 获取用户输入城市
			new Thread(new Runnable() {

				@Override
				public void run() {
					sendRequest(inputcitytext);
				}
			}).start();
		}
	}

	@Override
	public void showDialog() {
		pDialog = new ProgressDialog(HomePagerActivity.this);
		pDialog.setCancelable(true);// 点击可以取消Dialog的展现
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setMessage("正在更新...");
		pDialog.show();
	}
}
