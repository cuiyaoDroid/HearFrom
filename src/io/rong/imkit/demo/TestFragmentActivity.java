package io.rong.imkit.demo;

import com.tingshuo.hearfrom.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import io.rong.imkit.veiw.ActionBar;

/**
 * Created by DragonJ on 14-9-15.
 */
public class TestFragmentActivity extends FragmentActivity {

    //io.rong.imkit.veiw.ActionBar
    ActionBar mActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment);
        mActionBar = (ActionBar)findViewById(android.R.id.custom);

        mActionBar.setOnBackClick(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mActionBar.getTitleTextView().setText("Fragment 自定义");
    }


}
