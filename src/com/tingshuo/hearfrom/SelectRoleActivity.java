package com.tingshuo.hearfrom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;

import com.tingshuo.hearfrom.base.BaseSelectActivity;
import com.tingshuo.tool.RoleUtil;

public class SelectRoleActivity extends BaseSelectActivity {
	private SimpleAdapter adapter;
	private ArrayList<Map<String, Object>> adapterData;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		adapterData = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < RoleUtil.role_ids.length; i++) {
			Map<String, Object> data = new HashMap<String, Object>();
			data.put(RoleUtil.ROLE_ID, RoleUtil.role_ids[i]);
			data.put(RoleUtil.ROLE_NAME, RoleUtil.role_names[i]);
			data.put(RoleUtil.ROLE_PIC, RoleUtil.role_pic[i]);
			adapterData.add(data);
		}
		adapter = new SimpleAdapter(getApplicationContext(), adapterData,
				R.layout.cell_setting_img_click,
				new String[] { RoleUtil.ROLE_NAME, RoleUtil.ROLE_PIC }, new int[] { R.id.title,
						R.id.img });
		getSelect_list().setAdapter(adapter);
		getSelect_list().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) { 
				// TODO Auto-generated method stub
				int role_id=(Integer) adapterData.get(position).get(RoleUtil.ROLE_ID);
				String role_name=(String) adapterData.get(position).get(RoleUtil.ROLE_NAME);
				Intent intent=new Intent(SelectRoleActivity.this,publishTopickActivity.class);
				intent.putExtra(RoleUtil.ROLE_ID, role_id);
				intent.putExtra(RoleUtil.ROLE_NAME, role_name);
				setResult(RoleUtil.RESULT_SETTING_ROLE, intent);
				finish();
			}
		});
	}
}
