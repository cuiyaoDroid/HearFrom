package com.tingshuo.hearfrom.screen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tingshuo.hearfrom.R;

public class leaderThridScreen extends BaseScreen{
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
    	View settingLayout = inflater.inflate(R.layout.screen_leader_third,  
                container, false);  
        return settingLayout;  
    }  
}
