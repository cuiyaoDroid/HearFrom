package com.tingshuo.hearfrom.screen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.tingshuo.hearfrom.R;
import com.tingshuo.tool.L;
import com.tingshuo.tool.RoleUtil;
import com.tingshuo.tool.view.adapter.GridListAdapter;

public class leaderThridScreen extends BaseScreen{
	private GridView gridView;
	private GridListAdapter adapter;
	private ArrayList<Map<String,Object>>gridData;

		@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
		L.i("onCreateView");
    	View settingLayout = inflater.inflate(R.layout.screen_leader_third,  
                container, false);  
    	gridData=new ArrayList<Map<String,Object>>();
    	gridView=(GridView)settingLayout.findViewById(R.id.gridView);
    	initGridView();
    	gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				RoleUtil.role_id=(Integer) gridData.get(position).get("id");
				listener.changeToScreen(4);
			}
		});
        return settingLayout;  
    }  
	private void initGridView(){
		gridData.clear();
		adapter=new GridListAdapter(getActivity(), gridData, R.layout.cell_role_grid
				, new String[]{"name","pic"}, new int[]{R.id.name_txt,R.id.head_img});
		for(int i=0;i<RoleUtil.role_ids.length;i++){
			Map<String,Object>data=new HashMap<String, Object>();
			data.put("id", RoleUtil.role_ids[i]);
			data.put("name", RoleUtil.role_names[i]);
			data.put("pic",RoleUtil.role_pic[i]);
			gridData.add(data);
		}
		
		gridView.setAdapter(adapter);
		
	}
}
