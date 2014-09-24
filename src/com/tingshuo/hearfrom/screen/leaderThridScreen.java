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
	public static int role_id=1;

	private final int[] role_pic={
			R.drawable.role_junren,R.drawable.role_dubi,R.drawable.role_chengxuyuan,R.drawable.role_haitama
			,R.drawable.role_xuesheng,R.drawable.role_shejishi,R.drawable.role_daifu,R.drawable.role_lvshi
			,R.drawable.role_maoxianjia,R.drawable.role_nvshen,R.drawable.role_gongren,R.drawable.role_zhufu};
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
				role_id=(Integer) gridData.get(position).get("id");
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
			data.put("pic",role_pic[i]);
			gridData.add(data);
		}
		
		gridView.setAdapter(adapter);
		
	}
}
