package com.tingshuo.hearfrom;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.tingshuo.hearfrom.base.BaseAcivity;
import com.tingshuo.tool.view.SideBar;


public class ContactsActivity extends BaseAcivity {

    ExpandableListView expandableListView;
    TextView tViewShowLetter;
    private static String[] letters = new String[]{"*","a","b","c","d","e","f","g","h","i","j","k","l","m"
    	,"n","o","p","q","r","s","t","u","v","w","x","y","z"};
    private HashMap<String, ArrayList<String>> lettersDivider = new HashMap<String,
            ArrayList<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        initContentView();
        //test data;
        //由于是演示就不是用真实数据了
        ArrayList<String> names = new ArrayList<String>();
        names.add("heheh");
        names.add("我了个");
        names.add("并不是我");
        names.add("呵呵");
        names.add("嘻嘻");
        names.add("啦啦");
        names.add("lel");
        names.add("lle");
        names.add("lele");
        names.add("hehe");
        for (String letter : letters) {
            lettersDivider.put(letter, names);
        }
        tViewShowLetter = (TextView) findViewById(R.id.tView_letter);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;//屏蔽点击收缩事件
            }
        });
        expandableListView.setAdapter(new ContactsExpandableAdapter());

        SideBar sideBar = ((SideBar) findViewById(R.id.side_bar));
        sideBar.setOnLetterTouchListener(new SideBar.OnLetterTouchListener() {
            @Override
            public void onLetterTouch(String letter, int position) {
                tViewShowLetter.setVisibility(View.VISIBLE);
                tViewShowLetter.setText(letter);
                expandableListView.setSelectedGroup(position);
            }

            @Override
            public void onActionUp() {
                tViewShowLetter.setVisibility(View.GONE);
            }
        });
        sideBar.setShowString(letters);
    }
    @Override
    protected void initContentView() {
    	// TODO Auto-generated method stub
    	super.initContentView();
    	title_middle.setText("好友");
    	titleRight.setVisibility(View.VISIBLE);
    	titleRight.setImageResource(R.drawable.btn_top_add_bg);
    	titleback.setVisibility(View.GONE);
    }
    class ContactsExpandableAdapter extends BaseExpandableListAdapter {

        @Override
        public int getGroupCount() {
            return letters.length;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return lettersDivider.get(letters[groupPosition]).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if (!isExpanded) {
                expandableListView.expandGroup(groupPosition);//展开组
            }
            if (convertView == null) {
                convertView = View.inflate(ContactsActivity.this, R.layout.expendcell_group_contracts, null);
                convertView.setTag(new GroupViewHolder(convertView));
            }
            GroupViewHolder holder = (GroupViewHolder) convertView.getTag();
            holder.tViewGroupName.setText(letters[groupPosition]);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(ContactsActivity.this, R.layout.expandcell_child_contracts, null);
                convertView.setTag(new ChildViewHolder(convertView));
            }
            ChildViewHolder holder = (ChildViewHolder) convertView.getTag();
            holder.tViewChildName.setText(lettersDivider.get(letters[groupPosition]).get(childPosition));
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }

        class GroupViewHolder {
            TextView tViewGroupName;

            public GroupViewHolder(View parent) {
                tViewGroupName = (TextView) parent.findViewById(R.id.tView_group_name);
            }
        }

        class ChildViewHolder {
            TextView tViewChildName;

            public ChildViewHolder(View parent) {
                tViewChildName = (TextView) parent.findViewById(R.id.tView_child_name);
            }
        }
    }

}