package me.shouheng.samples.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import me.shouheng.samples.R;
import me.shouheng.samples.activity.TestActivityResult.Request;
import me.shouheng.utils.activity.ActivityHelper;

/**
 * @author shouh
 * @version $Id: TestActivityHelper, v 0.1 2018/11/22 12:40 shouh Exp$
 */
public class TestActivityHelper extends AppCompatActivity {

    private static final int REQUEST_RESULT = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper_test);

        findViewById(R.id.btn_view).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityHelper.open()
                        .setAction(Intent.ACTION_VIEW)
                        .setData(Uri.parse("http://www.baidu.com"))
                        .launch(TestActivityHelper.this);
            }
        });
        findViewById(R.id.btn_request_code).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityHelper.open(TestActivityResult.class)
                        .put(TestActivityResult.REQUEST_EXTRA_KEY_DATA,
                                new Request("Request-name", "Request-value"))
                        .launch(TestActivityHelper.this, REQUEST_RESULT);
            }
        });

        final View btnShared = findViewById(R.id.btn_shared_activity);
        if (VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            btnShared.setTransitionName("SHARED_ELEMENT");
        }
        btnShared.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityHelper.open(TestActivityResult.class)
                        .wishSharedElements(new View[]{btnShared})
                        .launch(TestActivityHelper.this);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_RESULT && resultCode == Activity.RESULT_OK) {
            assert data != null;
            TestActivityResult.Result result = (TestActivityResult.Result)
                    data.getSerializableExtra(TestActivityResult.RESULT_EXTRA_KEY_DATA);
            Toast.makeText(TestActivityHelper.this, result.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}