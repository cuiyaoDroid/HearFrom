package com.tingshuo.hearfrom.screen;

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

		@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
		L.i("onCreateView");
    	View settingLayout = inflater.inflate(R.layout.screen_leader_third,  
                container, false);  
    	gridView=(GridView)settingLayout.findViewById(R.id.gridView);
    	initGridView();
    	gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				RoleUtil.role_id=(Integer) RoleUtil.getInstance().getRoleData().get(position).get(RoleUtil.ROLE_ID);
				listener.changeToScreen(4);
			}
		});
        return settingLayout;  
    }  
	private void initGridView(){
		adapter=new GridListAdapter(getActivity(), RoleUtil.getInstance().getRoleData(), R.layout.cell_role_grid
				, new String[]{RoleUtil.ROLE_NAME,RoleUtil.ROLE_PIC}, new int[]{R.id.name_txt,R.id.head_img});
		gridView.setAdapter(adapter);
		
	}
}
