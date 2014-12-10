package com.tingshuo.hearfrom.screen;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.tingshuo.hearfrom.R;
import com.tingshuo.tool.L;
import com.tingshuo.tool.PreferenceConstants;
import com.tingshuo.tool.PreferenceUtils;
import com.tingshuo.tool.RoleUtil;
import com.tingshuo.tool.T;
import com.tingshuo.web.http.HttpJsonTool;

public class leaderFourthScreen extends BaseScreen{
	private ProgressBar progressBar;
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
		L.i("onCreateView");
    	View settingLayout = inflater.inflate(R.layout.screen_leader_fourth,  
                container, false);  
    	ImageButton open_tingshuo_btn=(ImageButton)settingLayout.findViewById(R.id.open_tingshuo_btn);
    	progressBar = (ProgressBar)settingLayout.findViewById(R.id.progressBar);
    	open_tingshuo_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				register();
			}
		});
        return settingLayout;  
    }
	AsyncTask<Void, Void, String>task;
	private void register(){
		progressBar.setVisibility(View.VISIBLE);
//		listener.startMainPage();
		task=new AsyncTask<Void, Void, String>(){

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return HttpJsonTool.getInstance().register(getActivity(),RoleUtil.role_id
						, leaderSecondScreen.i_sex);
			}
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				//progressBar.setVisibility(View.GONE);
				if(result.startsWith(HttpJsonTool.ERROR)){
					Context context=getActivity();
					if(context!=null){
						T.showLong(context, result.replace(HttpJsonTool.ERROR, ""));
					}
					return;
				}else if(result.startsWith(HttpJsonTool.SUCCESS)){
					PreferenceUtils.setPrefBoolean(getActivity()
							, PreferenceConstants.FIRST_USE, false);
					if(listener!=null){
						listener.startMainPage();
					}
				}
			}
		};
		task.execute();
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(task!=null){
			task.cancel(true);
		}
	}
	
}
