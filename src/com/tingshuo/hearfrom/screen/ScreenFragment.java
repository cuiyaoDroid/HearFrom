package com.tingshuo.hearfrom.screen;

import com.tingshuo.hearfrom.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("NewApi") 
public class ScreenFragment extends Fragment {  
  
    @Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
    	View settingLayout = inflater.inflate(R.layout.screen,  
                container, false);  
        return settingLayout;  
    }  
  
}  