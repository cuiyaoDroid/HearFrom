package com.tingshuo.hearfrom;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		adapter = new SimpleAdapter(getApplicationContext(), RoleUtil.getInstance().getRoleData(),
				R.layout.cell_setting_img_click,
				new String[] { RoleUtil.ROLE_NAME, RoleUtil.ROLE_PIC }, new int[] { R.id.title,
						R.id.img });
		RoleUtil.getInstance().refreshRoleContent();
		getSelect_list().setAdapter(adapter);
		getSelect_list().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) { 
				// TODO Auto-generated method stub
				int role_id=(Integer) RoleUtil.getInstance().getRoleData().get(position).get(RoleUtil.ROLE_ID);
				String role_name=(String) RoleUtil.getInstance().getRoleData().get(position).get(RoleUtil.ROLE_NAME);
				Intent intent=new Intent(SelectRoleActivity.this,publishTopickActivity.class);
				intent.putExtra(RoleUtil.ROLE_ID, role_id);
				intent.putExtra(RoleUtil.ROLE_NAME, role_name);
				setResult(RoleUtil.RESULT_SETTING_ROLE, intent);
				finish();
			}
		});
	}
}
