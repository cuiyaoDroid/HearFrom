package io.rong.imkit.demo;

import android.os.Bundle;
import android.util.Log;

import com.sea_monster.core.exception.BaseException;
import com.sea_monster.core.network.AbstractHttpRequest;
import com.sea_monster.core.network.ApiCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.rong.imkit.RongActivity;
import io.rong.imkit.RongIM;
import io.rong.imkit.demo.model.User;
import io.rong.imlib.RongIMClient;

/**
 * Created by DragonJ on 14/11/4.
 */
public class DemoActivity extends RongActivity {
    FriendCallback mCallback;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        if(RongIM.getInstance() == null){
            String userName = DemoContext.getInstance().getUsername();
            mCallback = new FriendCallback();
            DemoContext.getInstance().getDemoApi().getFriends(userName, mCallback);
            initGroupInfo();
        }
    }

    class FriendCallback implements ApiCallback<ArrayList<User>>{

        @Override
        public void onComplete(AbstractHttpRequest<ArrayList<User>> arrayListAbstractHttpRequest, ArrayList<User> users) {
            DemoContext.getInstance().setFriends(getFriends(users));
        }

        @Override
        public void onFailure(AbstractHttpRequest<ArrayList<User>> arrayListAbstractHttpRequest, BaseException e) {

        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


    /**
     * 把自己系统中的用户对象转换成融云中的用户对象
     *
     * @param users
     * @return
     */
    public ArrayList<RongIMClient.UserInfo> getFriends(ArrayList<User> users) {

        if (users == null)
            return null;

        ArrayList<RongIMClient.UserInfo> friends = new ArrayList<RongIMClient.UserInfo>();

        for (User user : users) {
            RongIMClient.UserInfo info = new RongIMClient.UserInfo(String.valueOf(user.getId()), user.getUsername(), user.getPortrait());
            friends.add(info);
        }

        return friends;
    }
    HashMap<String, RongIMClient.Group> groupM;
    private void initGroupInfo() {

        RongIMClient.Group group1 = new RongIMClient.Group("group001", "群组一", "http://docs.rongcloud.cn/assets/img/logo@2x.png");
        RongIMClient.Group group2 = new RongIMClient.Group("group002", "群组二", "http://cn.bing.com/fd/s/a/k_zh_cn_s2.png");
        RongIMClient.Group group3 = new RongIMClient.Group("group003", "群组三", "http://www.baidu.com/img/bdlogo.png");
        List<RongIMClient.Group> groups = new ArrayList<RongIMClient.Group>();
        groups.add(group1);
        groups.add(group2);
        groups.add(group3);

        groupM = new HashMap<String, RongIMClient.Group>();
        groupM.put("group001", group1);
        groupM.put("group002", group2);
        groupM.put("group003", group3);

        DemoContext.getInstance().setGroupMap(groupM);
        DemoContext.getInstance().setGroupInfoProvider();
    }

}
