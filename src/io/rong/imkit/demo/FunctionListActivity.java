package io.rong.imkit.demo;

import com.tingshuo.hearfrom.R;

import android.content.Intent;
import android.os.*;
import android.os.Process;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import io.rong.imkit.RongIM;
import io.rong.imkit.model.RichContentMessage;
import io.rong.imkit.veiw.ActionBar;
import io.rong.imkit.veiw.AlterDialog;
import io.rong.imlib.RongIMClient;

public class FunctionListActivity extends BaseActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private ListView mListView;
    private FunctionListAdapter mFunctionListAdapter;
    private Button mLogout;
    private ActionBar mAction;


    @Override
    protected int setContentViewResId() {
        return R.layout.activity_functioan_list;
    }

    @Override
    protected void initView() {

        mListView = getViewById(android.R.id.list);
        View headerView = LayoutInflater.from(this).inflate(R.layout.view_list_header, null);
        mListView.addHeaderView(headerView);
        mLogout = getViewById(android.R.id.button1);
        mLogout.setOnClickListener(this);
        mAction = getViewById(R.id.action_bar);
    }

    @Override
    protected void initData() {

        String[] titleNameArray = this.getResources().getStringArray(R.array.function_list);
        mFunctionListAdapter = new FunctionListAdapter(this, titleNameArray);
        mListView.setAdapter(mFunctionListAdapter);
        mFunctionListAdapter.notifyDataSetChanged();
        mListView.setOnItemClickListener(this);
        mAction.setOnBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (position == 1) {

            /**
             * 打开会话列表
             *
             * 如果您需要会话列表界面，可以启动聊天会话列表，在聊天会话列表中，用户可以通过点击右上角的加号按钮创建聊天会话
             *
             * API详见 http://docs.rongcloud.cn/android.html
             */
            RongIM.getInstance().startConversationList(this);

        } else if (position == 2) {
            /**
             * 打开客服会话
             */
            RongIM.getInstance().startCustomerServiceChat(this, "kefu114", "客服");
        } else if (position == 3) {


            String content = "新华网莫斯科10月14日电 国务院总理李克强14日出席第三届莫斯科国际创新发展论坛并发表题为《以创新实现共同发展包容发展》的演讲。演讲全文如下：创新是人类发展进步的不熄引擎。当今世界正处于大变革、大调整之中，迫切要求更大范围、更深层次的创新。实现这样的创新，墨守成规不行，单打独斗也不行，需要开放、合作与分享。6年前，面对国际金融危机，国际社会同舟共济，避免了危机向纵深蔓延。随着经济全球化、社会信息化的深入推进，更需要各国携起手来，在合作创新中实现知识的倍增、价值的倍增，解决发展的难题，促进共同繁荣。这正是开放式创新的意义所在。";
            String title = "新华网莫斯科10月14日电,李克强在第三届莫斯科国际创新发展论坛上的演讲";
            String url = "http://img2.cache.netease.com/photo/0003/2014-10-15/900x600_A8J6CVA400AJ0003.jpg";
            RichContentMessage imageTextMessage = new RichContentMessage(title, content, url);
            imageTextMessage.setExtra("可以存放的网址，商品编号或URI,在点击消息时你可以取到进入你的商品页面");

            RongIM.getInstance().sendMessage(RongIMClient.ConversationType.PRIVATE, DemoContext.getInstance().getCurrentUser().getUserId(), imageTextMessage, new RongIMClient.SendMessageCallback() {

                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError(ErrorCode errorCode) {

                        }

                        @Override
                        public void onProgress(int i) {

                        }
                    }
            );


            /**
             * 打开二人会话页面
             *
             * API详见 http://docs.rongcloud.cn/android.html
             */
            RongIM.getInstance().startPrivateChat(this, DemoContext.getInstance().getCurrentUser().getUserId(), "光头强");
        } else if (position == 4) {
            startActivity(new Intent(this, GroupListActivity.class));
        } else if (position == 5) {
            startActivity(new Intent(this, TestFragmentActivity.class));
        } else if (position == 6) {
            startActivity(new Intent(this, TestFragment2Activity.class));
        }else if(position == 7){
            RongIM.getInstance().startChatroom(this, "chatroom002", "聊天室");

        }
    }

    @Override
    public void onClick(View v) {

        if (v == mLogout) {

            /**
             * 断开与服务器的连接
             *
             * API详见 http://docs.rongcloud.cn/android.html
             */
            RongIM.getInstance().disconnect(false);


            finish();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {

            final AlterDialog alterDialog = new AlterDialog(this);
            alterDialog.setTitle("确定退出应用？", true);

            alterDialog.setButton1("确定", new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    RongIM.getInstance().disconnect(true);
                    android.os.Process.killProcess(Process.myPid());
                }
            });

            alterDialog.setButton2("取消", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alterDialog.dismiss();
                }
            });

            alterDialog.show();
        }

        return false;
    }
}
