package org.houxg.pixiurss.ui;

import android.os.Bundle;

import org.houxg.pixiurss.Article;
import org.houxg.pixiurss.ArticleDao;
import org.houxg.pixiurss.R;
import org.houxg.pixiurss.module.App;
import org.houxg.pixiurss.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * desc:
 * <br>
 * author: houxg
 * <br>
 * create on 2015/9/19
 */
public class TestActivity extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        List<Article> list1 = new ArrayList<>();
        List<Article> list2 = new ArrayList<>();

        list1.add(getOne("http1", "haha", "d1"));
        list1.add(getOne("http2", "hah", "d1"));
        list1.add(getOne("http3", "haha", "d1"));

        list2.add(getOne("http4", "haha", "d1"));
        list2.add(getOne("http3", "hah", "d1"));
        list2.add(getOne("http2", "haha", "d1"));

        try {
            ArticleDao articleDao = App.getDaoSession().getArticleDao();
            articleDao.insertOrReplaceInTx(list1);
            articleDao.insertOrReplaceInTx(list2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Article getOne(String link, String title, String desc) {
        Article article = new Article();
        article.setDesc(desc);
        article.setLink(link);
        article.setTitle(title);
        return article;
    }
}
