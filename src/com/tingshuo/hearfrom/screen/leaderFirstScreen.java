package com.tingshuo.hearfrom.screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.tingshuo.hearfrom.R;
import com.tingshuo.tool.RongIMTool;

public class leaderFirstScreen extends BaseScreen{
	 @Override  
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
	            Bundle savedInstanceState) {  
		 
	    	View settingLayout = inflater.inflate(R.layout.screen_leader_first,  
	                container, false);  
	    	ImageButton register_fast_button=(ImageButton)settingLayout.findViewById(R.id.register_fast_button);
	    	register_fast_button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(listener!=null){
						listener.changeToScreen(2);
//						listener.startMainPage();
					}
				}
			});
	        return settingLayout;  
	    }  
}
