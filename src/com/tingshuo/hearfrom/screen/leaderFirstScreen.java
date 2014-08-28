package com.tingshuo.hearfrom.screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tingshuo.hearfrom.R;

public class leaderFirstScreen extends ScreenFragment{
	 @Override  
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
	            Bundle savedInstanceState) {  
	    	View settingLayout = inflater.inflate(R.layout.screen,  
	                container, false);  
	        return settingLayout;  
	    }  
}
