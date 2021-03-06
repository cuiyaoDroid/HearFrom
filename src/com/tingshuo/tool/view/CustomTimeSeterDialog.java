package com.tingshuo.tool.view;

import java.util.Calendar;
import java.util.Locale;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;

import com.tingshuo.hearfrom.R;
import com.tingshuo.tool.view.wheel.NumericWheelAdapter;
import com.tingshuo.tool.view.wheel.OnWheelChangedListener;
import com.tingshuo.tool.view.wheel.WheelView;


/**
 * <p>
 * Title: CustomDialog
 * </p>
 * <p>
 * Description:自定义Dialog（参数传入Dialog样式文件，Dialog布局文件）
 * </p>
 * <p>
 * Copyright: Copyright (c) 2013
 * </p>
 * 
 * @author archie
 * @version 1.0
 */
public class CustomTimeSeterDialog extends Dialog {
	private int layoutRes;// 布局文件
	private Context context;
	private long milltime;
	public CustomTimeSeterDialog(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * 自定义布局的构造方法
	 * 
	 * @param context
	 * @param resLayout
	 */
	public CustomTimeSeterDialog(Context context, int resLayout) {
		super(context);
		this.context = context;
		this.layoutRes = resLayout;
	}

	/**
	 * 自定义主题及布局的构造方法
	 * 
	 * @param context
	 * @param theme
	 * @param resLayout
	 */
	public CustomTimeSeterDialog(Context context, int theme, int resLayout,long milltime) {
		super(context, theme);
		this.context = context;
		this.layoutRes = resLayout;
		this.milltime=milltime;
	}
	public interface TimeDialogListener {
		public void getTimeInMill(long time);
	}
	private TimeDialogListener listener;
	public void setTimeDialogListener(TimeDialogListener listener){
		this.listener=listener;
	}
	private Calendar calendar;
	private final int START_YEAR=1900;
	private NumericWheelAdapter dayAdapter;
	int moth_days=31;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(layoutRes);
		final WheelView year = (WheelView) findViewById(R.id.year);
		NumericWheelAdapter yearAdapter = new NumericWheelAdapter(context, START_YEAR,
				2100, "%04d");
		yearAdapter.setItemResource(R.layout.wheel_text_item);
		yearAdapter.setItemTextResource(R.id.text);
		year.setViewAdapter(yearAdapter);
		year.setCyclic(true);
		
		final WheelView month = (WheelView) findViewById(R.id.month);
		NumericWheelAdapter monthAdapter = new NumericWheelAdapter(context, 1,
				12, "%02d");
		monthAdapter.setItemResource(R.layout.wheel_text_item);
		monthAdapter.setItemTextResource(R.id.text);
		month.setViewAdapter(monthAdapter);
		month.setCyclic(true);

		final WheelView day = (WheelView) findViewById(R.id.day);
		
		day.setCyclic(true);
		OnWheelChangedListener changerlistener=new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				int i_month=month.getCurrentItem();
				moth_days=getDaysByYearMonth(year.getCurrentItem()+START_YEAR,i_month);
				dayAdapter = new NumericWheelAdapter(context, 1,
						moth_days, "%02d");
				dayAdapter.setItemResource(R.layout.wheel_text_item);
				dayAdapter.setItemTextResource(R.id.text);
				day.setViewAdapter(dayAdapter);
			}
		};
		month.addChangingListener(changerlistener);
		year.addChangingListener(changerlistener);
		// set current time
		calendar = Calendar.getInstance(Locale.CHINA);
		if(milltime!=0){
			calendar.setTimeInMillis(milltime);
		}
		month.setCurrentItem(calendar.get(Calendar.MONTH));
		day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH));
		year.setCurrentItem(calendar.get(Calendar.YEAR)-START_YEAR);
		final Button setBtn = (Button) findViewById(R.id.set_btn);
		setBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				calendar.set(Calendar.MONTH,month.getCurrentItem());
				calendar.set(Calendar.DAY_OF_MONTH,day.getCurrentItem()+1);
				calendar.set(Calendar.YEAR, year.getCurrentItem()+START_YEAR);
				if(listener!=null){
					listener.getTimeInMill(calendar.getTimeInMillis());
				}
				dismiss();
			}
		});
		final Button cancalBtn = (Button) findViewById(R.id.cancal_btn);
		cancalBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
	}
	public static int getDaysByYearMonth(int year, int month) {  
        
        Calendar a = Calendar.getInstance();  
        a.set(Calendar.YEAR, year);  
        a.set(Calendar.MONTH, month+1);  
        a.set(Calendar.DATE, 1);  
        a.add(Calendar.DATE, -1);  
        int maxDate = a.get(Calendar.DATE);  
        return maxDate;  
    }  
}