package com.tingshuo.hearfrom.screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.tingshuo.hearfrom.R;
import com.tingshuo.tool.L;
import com.tingshuo.tool.SexTool;
import com.tingshuo.web.http.HttpJsonTool;

public class leaderSecondScreen extends BaseScreen{
	public static int i_sex=1;
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
		L.i("onCreateView");
    	View settingLayout = inflater.inflate(R.layout.screen_leader_second,  
                container, false); 
    	ImageButton male_btn=(ImageButton)settingLayout.findViewById(R.id.male_btn);
    	ImageButton female_btn=(ImageButton)settingLayout.findViewById(R.id.female_btn);
    	male_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				i_sex=SexTool.MELA;
				listener.changeToScreen(3);
			}
		});
    	female_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				i_sex=SexTool.FEMELA;
				listener.changeToScreen(3);
			}
		});
        return settingLayout;  
    }  
}
