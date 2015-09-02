package org.houxg.pixiurss.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.houxg.pixiurss.R;
import org.houxg.pixiurss.module.rss.RSSGetter;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.title_activity)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar.setNavigationIcon(R.drawable.icon_back);

        RSSGetter getter = new RSSGetter("http://rss.cnbeta.com/rss", null);
        new Thread(getter).start();
    }


}
