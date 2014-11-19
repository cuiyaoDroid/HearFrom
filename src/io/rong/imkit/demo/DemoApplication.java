package io.rong.imkit.demo;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.tingshuo.hearfrom.R;
import com.umeng.analytics.MobclickAgent;

import java.lang.reflect.Method;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

/**
 * Created by zhjchen on 14-3-20.
 */
public class DemoApplication extends Application {

    private static final String IS_FIRST = "is_first";
    DemoContext mContext;
    //    public static final String APP_KEY = "e0x9wycfx7flq";
    public static final String APP_KEY = "z3v5yqkbv8v30";


    @Override
    public void onCreate() {
        super.onCreate();

        /**
         * IMKit SDK调用第一步 初始化
         * 第一个参数，  context上下文
         * 第二个参数，APPKey换成自己的appkey
         * 第三个参数，push消息通知所要打个的action页面
         * 第四个参数，push消息中可以自定义push图标
         */
        RongIM.init(this, APP_KEY, R.drawable.ic_launcher);
        RongIM.setConversationBehaviorListener(new RongIM.ConversationBehaviorListener() {
            @Override
            public void onClickUserPortrait(Context context, RongIMClient.ConversationType conversationType, RongIMClient.UserInfo user) {
                Log.d("Begavior", conversationType.getName() + ":" + user.getName() + " context:" + context);
            }

            @Override
            public void onClickMessage(Context context, RongIMClient.Message message) {
                Log.d("Begavior", message.getObjectName() + ":" + message.getMessageId() + " context:" + context);
            }
        });

        mContext = DemoContext.getInstance();
        mContext.init(this);

        try {
            Class c;
            c = Class.forName("com.networkbench.agent.impl.NBSAppAgent");
            Method m = c.getMethod("setLicenseKey", new Class[]{String.class});
            m.invoke(c, new Object[]{"a546c342ba704acf91b27e9603b6860d"});

        } catch (Exception e) {
            e.printStackTrace();
        }

       try {
            System.loadLibrary("imdemo");
        } catch (UnsatisfiedLinkError e) {
//            e.printStackTrace();
        }



        Thread.setDefaultUncaughtExceptionHandler(new DefaultExceptionHandler(this));

    }

}
