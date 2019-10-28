package com.example.fangxq.myapplication.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fangxq.myapplication.R;
import com.fxq.apt.annotation.BindView;
import com.fxq.apt.annotation.Module;
import com.fxq.apt.annotation.Router;
import com.fxq.gradle.plugin.Cost;
import com.fxq.lib.omnipotentfunction.FunctionHasParamHasResult;
import com.fxq.lib.omnipotentfunction.FunctionHasParamNoResult;
import com.fxq.lib.omnipotentfunction.FunctionNoParamHasResult;
import com.fxq.lib.omnipotentfunction.FunctionNoParamNoResult;
import com.fxq.lib.omnipotentfunction.OmnipotentFunctionManager;

import java.util.ArrayList;
import java.util.List;

import fxq.android.com.commonbusiness.ui.BaseSwipeFinishActivity;
import fxq.android.com.commonbusiness.utils.BindViewUtils;

@Module("mainModule")
@Router("main")
public class MainActivity extends BaseSwipeFinishActivity implements AdapterView.OnItemClickListener {

    public static final String FUNCTION_1 = "functon01";
    public static final String FUNCTION_2 = "functon02";
    public static final String FUNCTION_3 = "functon03";
    public static final String FUNCTION_4 = "functon04";

    @BindView(R.id.listView)
    ListView listView;
    @Cost
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BindViewUtils.bind(this);
        addSwipeFinishLayout();
        //首页activtiy可设置为不能滑动关闭
        setEnableGesture(false);
        //listView = (ListView) findViewById(R.id.listView);
        ArrayList<ContentItem> objects = new ArrayList<ContentItem>();
        objects.add(new ContentItem("Line Chart (Dual YAxis) test", ""));
        objects.add(new ContentItem("CustomRangeSeekBar", ""));
        objects.add(new ContentItem("CoordinatorTest", ""));
        objects.add(new ContentItem("PieChartActivity", ""));
        objects.add(new ContentItem("CanvasDrawActivity", ""));
        objects.add(new ContentItem("CustomPagerViewActivity", ""));
        objects.add(new ContentItem("EditTestActivity", ""));
        objects.add(new ContentItem("UITestActivity", ""));
        objects.add(new ContentItem("FragmentTestActivity", ""));
        objects.add(new ContentItem("FlutterTestActivity", ""));
        MyAdapter adapter = new MyAdapter(this, objects);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(this);
        initOmnipotentFunction();
        //SophixManager.getInstance().queryAndLoadNewPatch();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i;
        switch (position) {
            case 0:
                i = new Intent(this, LineChartTestActivity.class);
                startActivity(i);
                break;
            case 1:
                i = new Intent(this, CustomSeekbarActivity.class);
                startActivity(i);
                break;
            case 2:
                i = new Intent(this, CoordinatorTestActivity.class);
                startActivity(i);
                break;
            case 3:
                i = new Intent(this, PieChartActivity.class);
                startActivity(i);
                break;
            case 4:
                i = new Intent(this, CanvasDrawActivity.class);
                startActivity(i);
                break;
            case 5:
                i = new Intent(this, CustomPagerViewActivity.class);
                startActivity(i);
                break;
            case 6:
                i = new Intent(this, EditTestActivity.class);
                startActivity(i);
                break;
            case 7:
                i = new Intent(this, UITestActivity.class);
                startActivity(i);
                break;
            case 8:
                i = new Intent(this, BookManagerActivity.class);
                startActivity(i);
                break;
            case 9:
                i = new Intent(this, FlutterDemoActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    private void initOmnipotentFunction() {
        //无参 无返回
        OmnipotentFunctionManager.getInstance().addFunction(new FunctionNoParamNoResult(FUNCTION_1) {
            @Override
            public void function() {
                Log.e("fxq", FUNCTION_1);
            }
        });

        //无参 有返回值
        OmnipotentFunctionManager.getInstance().addFunction(new FunctionNoParamHasResult(FUNCTION_2) {
            @Override
            public String function() {
                Log.e("fxq", FUNCTION_2);
                return "Hello from the MainActivity";
            }
        });

        OmnipotentFunctionManager.getInstance().addFunction(new FunctionHasParamNoResult<String>(FUNCTION_3) {
            @Override
            public void function(String s) {
                Log.e("fxq", FUNCTION_3);
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });

        OmnipotentFunctionManager.getInstance().addFunction(new FunctionHasParamHasResult<String, String>(FUNCTION_4) {
            @Override
            public String function(String s) {
                Log.e("fxq", FUNCTION_4);
                return s + "from Mainactivity";
            }
        });
    }

    private class ContentItem {
        String name;
        String desc;

        public ContentItem(String n, String d) {
            name = n;
            desc = d;
        }
    }

    private class MyAdapter extends ArrayAdapter<ContentItem> {

        public MyAdapter(Context context, List<ContentItem> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ContentItem c = getItem(position);

            ViewHolder holder = null;

            if (convertView == null) {

                holder = new ViewHolder();

                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, null);
                holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
                holder.tvDesc = (TextView) convertView.findViewById(R.id.tvDesc);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tvName.setText(c.name);
            holder.tvDesc.setText(c.desc);

            return convertView;
        }

        private class ViewHolder {

            TextView tvName, tvDesc;
        }
    }

    private void showPopupView() {

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("fxq", "onPause() isFinishing() =  " + this.isFinishing());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("fxq", "onDestroy() isFinishing() =  " + this.isFinishing());
        OmnipotentFunctionManager.getInstance().removeFunctionNoParamNoResult(FUNCTION_1);
        OmnipotentFunctionManager.getInstance().removeFunctionNoParamHasResult(FUNCTION_2);
        OmnipotentFunctionManager.getInstance().removeFunctionHasParamNoResult(FUNCTION_3);
        OmnipotentFunctionManager.getInstance().removeFunctionHasParamHasResult(FUNCTION_4);

    }

}
