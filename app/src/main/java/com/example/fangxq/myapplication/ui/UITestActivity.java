package com.example.fangxq.myapplication.ui;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.HandlerThread;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fangxq.myapplication.R;
import com.example.fangxq.myapplication.adapter.MyRecyclerViewAdapter;
import com.example.fangxq.myapplication.customview.HorizontalProgressWithText;
import com.example.fangxq.myapplication.customview.TestPopupViewManager;
import com.example.fangxq.myapplication.service.NotificationPopupService;
import com.example.fangxq.myapplication.utils.TestUtils;
import com.fxq.apt.annotation.Router;
import com.fxq.lib.customview.AutoVerticalScrollTextview;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import fxq.android.com.commonbusiness.ui.BaseSwipeFinishActivity;

/**
 * Created by Fangxq on 2017/7/21.
 */
@Router("uiTest")
public class UITestActivity extends BaseSwipeFinishActivity {

    private Button addButton;
    private Button deleteButton;
    private View stubView;
    private AutoVerticalScrollTextview autoScrollVerticalTextview;
    private String[] string={"我的剑，就是你的剑!"};

    private String[] strings={"俺也是从石头里蹦出来得!","俺也是从石头里蹦出来得!","我的大刀早已饥渴难耐了!"};
    private TabLayout tabLayout;
    private int[] images = new int[]{
            R.drawable.ic_avatar,
            R.drawable.ic_avatar,
            R.drawable.ic_avatar};
    private String[] tabs = new String[]{"小说", "电影", "相声"};
    private RecyclerView mRecyclerView;
    private ScrollView mScrollView;
    private HorizontalProgressWithText mHorizontalProgressWithText;
    private WebView infoWebView;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        addSwipeFinishLayout();
        initView();
    }

    private void initView() {
        addButton = (Button) findViewById(R.id.add);
        addButton.setTag("test");
        addButton.setOnClickListener(addOnClicked);
        deleteButton = (Button) findViewById(R.id.delete);
        deleteButton.setOnClickListener(deleteOnClicked);
        stubView = (ViewStub) findViewById(R.id.view_stub);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mScrollView = (ScrollView) findViewById(R.id.scrollView);
        mLinearLayout = (LinearLayout) findViewById(R.id.ll_test);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setNestedScrollingEnabled(false);
        testAutoScrollVerticalTextView();
        initTabLayout();
        initRecyclerViewDate();
        mScrollView.smoothScrollBy(0, 0);
        mHorizontalProgressWithText = (HorizontalProgressWithText) findViewById(R.id.HorizontalProgressWithText);
        mHorizontalProgressWithText.setText("仅剩 40000 件仅剩 40000 件仅剩 40000 件");
        infoWebView = (WebView) findViewById(R.id.webview_content);
        infoWebView.getSettings().setJavaScriptEnabled(true);
        infoWebView.getSettings().setDatabaseEnabled(true);
        infoWebView.getSettings().setDomStorageEnabled(true);
        infoWebView.loadUrl("http://test10.mm.airent.test.aiershou.com/product/insurance/?id=3&id_activity=44&id_sku=419954&choose_installments_num=12&third=1");

    }

    private void testAutoScrollVerticalTextView() {
        autoScrollVerticalTextview = (AutoVerticalScrollTextview) findViewById(R.id.autoScrollVerticalTextview);
        autoScrollVerticalTextview.setTextList(Arrays.asList(strings));
//        autoScrollVerticalTextview.setIntervalTime(3000);
        autoScrollVerticalTextview.setInAnimTime(400);
        autoScrollVerticalTextview.setOutAnimTime(400);
        autoScrollVerticalTextview.setOnItemClickListener(new AutoVerticalScrollTextview.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(UITestActivity.this, "点击了 : " + position, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(UITestActivity.this, EditTestActivity.class);
                startActivity(i);
            }
        });
        autoScrollVerticalTextview.startAutoScroll();
    }

    private void  initTabLayout() {
        setupTabIcons();

    }


    private void setTestText() {
        TextView testTextView = (TextView) findViewById(R.id.text);
    }


    private View.OnClickListener addOnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            stubView.setVisibility(View.VISIBLE);
//            String url = "dmlife://test?p_t=p_t";
//            String realUrl = appendCommonParams(url);
//            Log.e("fxq", "realUrl = " + realUrl);
            //ProtocolPopupView.initProtocolPopupView(UITestActivity.this, UITestActivity.this).show(v);
            //showHangNotification();
            //TestPopupViewManager.getInstance().showPopupWindow(TestPopupViewManager.getInstance().buildTestMessageNotificationItem());
            //startService(new Intent(UITestActivity.this, NotificationPopupService.class));
            //getPermission();
            //SortManager.selectSort();
            //SortManager.insertSort();
            //testPatch();
//            test();
            TestUtils.ChainTest();
            String idString = v.getContext().getResources().getResourceEntryName(v.getId());
            String tag = (String) v.getTag();
           Log.e("fxq", "idString = " + idString + "----- tag = " + tag);


        }
    };

    private View.OnClickListener deleteOnClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            stubView.setVisibility(View.GONE);
        }
    };


    private void setupTabIcons() {
        tabLayout.addTab(tabLayout.newTab().setCustomView(getTabView(0)), true);
        tabLayout.addTab(tabLayout.newTab().setCustomView(getTabView(1)), false);
        tabLayout.addTab(tabLayout.newTab().setCustomView(getTabView(2)), false);

    }

    private void getPermission() {
        PackageManager pm = this.getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.READ_CONTACTS", this.getPackageName()));
        //int permission = ActivityCompat.checkSelfPermission(this, "android.permission.READ_CONTACTS");
        Log.e("fxq", "permission = " + permission);
    }


    public View getTabView(int position) {
        View view = LayoutInflater.from(this).inflate(R.layout.item_tab, null);
        TextView txt_title = (TextView) view.findViewById(R.id.txt_title);
        txt_title.setText(tabs[position]);
        ImageView img_title = (ImageView) view.findViewById(R.id.img_title);
        img_title.setImageResource(images[position]);
        return view;
    }

    private void initRecyclerViewDate(){
        List<Integer> datas = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            datas.add(i);
        }
        mRecyclerView.setAdapter(new MyRecyclerViewAdapter(this, datas));
    }

    public static String appendCommonParams(String url) {
        if (StringUtils.isNotEmpty(url)) {
            try {
                Uri uri = Uri.parse(url);
                Uri.Builder builder = uri.buildUpon();
                builder.appendQueryParameter("p_c", "p_c");
                builder.appendQueryParameter("r_i", UUID.randomUUID() + ".M");
                builder.appendQueryParameter("p_o", "a");
                url = builder.build().toString();
            } catch (Exception e) {

            }
        }
        return url;
    }

    private void test() {
        String testStr = "ANR in zmsoft.rest.performancetest (zmsoft.rest.performancetest/.AnrUiActivity) (test.java:***)PI:***";

        String fomatStr = testStr.replaceAll("\\d", "");
//        if (testStr.contains("PID")) {
//            fomatStr = testStr.substring(0, testStr.indexOf("PID"));
//        }
        Log.e("fxq", "fomatStr = " + fomatStr);
        TestUtils.startDevelopmentActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initRecyclerViewDate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TestPopupViewManager.getInstance().dismissPopupView();
    }

    private void testPatch() {
        int i = Integer.parseInt("1.0");
        Log.e("fxq", "patch success  =" + i);
    }



}
