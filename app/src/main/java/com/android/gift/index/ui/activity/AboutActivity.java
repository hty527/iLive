package com.android.gift.index.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.android.gift.R;
import com.android.gift.util.AppUtils;
import com.android.gift.view.CommentTitleView;

/**
 * Created by TinyHung@outlook.com
 * 2019/7/13
 * 关于、版本
 */

public class AboutActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        CommentTitleView titleView = (CommentTitleView) findViewById(R.id.title_view);
        titleView.setOnTitleClickListener(new CommentTitleView.OnTitleClickListener() {
            @Override
            public void onBack(View view) {
                finish();
            }
        });
        String content="<font>软件版本：<font color='#FF0000'>"
                + AppUtils.getInstance().getVersion()
                +"</font><br/>内部版本：<font color='#FF0000'>"
                +AppUtils.getInstance().getVersionCode()
                +"</font></font>";
        ((TextView) findViewById(R.id.tv_version)).setText(Html.fromHtml(content));
        findViewById(R.id.btn_check_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Uri uri=Uri.parse("https://github.com/Yuye584312311/Live/wiki/HistoryVersion");
                    Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(intent);
                }catch (RuntimeException e){
                    e.printStackTrace();
                    Toast.makeText(AboutActivity.this,"未找到合适的应用打开网页",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}