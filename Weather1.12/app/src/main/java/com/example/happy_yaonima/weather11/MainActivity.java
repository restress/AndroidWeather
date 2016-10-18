package com.example.happy_yaonima.weather11;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity implements View.OnClickListener {
    // 返回按钮
    private ImageView scBackImg;
    // 输入框
    private EditText scCityEd;
    // 搜索框
    private Button scCityBtn;
    // 城市列表的GridView
    private GridView scGridView;
    // GridView的适配器
    private SimpleAdapter adapter;
    // 城市名
    private String myCity;
    private List<Map<String, Object>> data;
    private Map<String, Object> map;
    private int cityFlag = 0;
    private String City[] = { "北京", "天津", "上海", "重庆", "河北", "石家庄", "张家口", "廊坊",
            "南京", "武汉", "合肥", "安庆", "菏泽", "临沂", "沈阳", "哈尔滨", "杭州", "六安", "南昌",
            "大连", "佳木斯", "镇江", "温州", "宿州", "亳州", "阜阳", "蚌埠", "淮南", "滁州", "铜陵",
            "池州", "巢湖", "厦门", "景德镇", "济南", "香港", "澳门", "台湾", "泰州" };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        // 初始化控件
        initView();
        // 获取数据
        getData();
        // 添加数据到适配器
        addData();
    }

    private void initView() {
       /* scBackImg = (ImageView) findViewById(R.id.scBackImg);
        scBackImg.setOnClickListener(this);*/
        //搜索箭头
        scCityBtn = (Button) findViewById(R.id.scCityBtn);
        scCityBtn.setOnClickListener(this);
        //搜索框内容
        scCityEd = (EditText) findViewById(R.id.scCityEd);
        scCityEd.addTextChangedListener(new TextWatcher() {// EditText的文本监听

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // 如果输入框没内容，按钮不出现。有内容则按钮出现
                if (scCityEd.getText().toString().length() == 0) {
                    scCityBtn.setVisibility(View.GONE);
                } else {
                    scCityBtn.setVisibility(View.VISIBLE);
                }
            }
        });
        //选择框
        scGridView = (GridView) findViewById(R.id.scGridView);
        //gridview选择框被点击
        scGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {// GridView的item点击事件

            @SuppressWarnings("unchecked")
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 取出当前的item
                map = (Map<String, Object>) adapter.getItem(position);
                String r = map.get("city").toString();
                Intent i = new Intent(MainActivity.this,
                        Weather.class);
                i.putExtra("city", r);
                i.putExtra("cityFlag", cityFlag);
                startActivity(i);
            }
        });
        //新建map对象的数列
        data = new ArrayList<Map<String, Object>>();
    }

    //把city数组里面的对象转化为hashmap对象保存在data数列里
    private List<Map<String, Object>> getData() {
        for (int i = 0; i < City.length; i++) {
            map = new HashMap<String, Object>();
            map.put("city", City[i]);
            data.add(map);
        }
        return data;
    }

    //将data内容显示在gridview上
    private void addData() {
        //SimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to)
      /*  参数context：上下文，比如this。关联SimpleAdapter运行的视图上下文
        参数data：Map列表，列表要显示的数据，这部分需要自己实现，如例子中的getData()，类型要与上面的一致，每条项目要与from中指定条目一致
        参数resource：ListView单项布局文件的Id,这个布局就是你自定义的布局了，你想显示什么样子的布局都在这个布局中。这个布局中必须包括了to中定义的控件id
        参数 from：一个被添加到Map上关联每一个项目列名称的列表，数组里面是列名称
        参数 to：是一个int数组，数组里面的id是自定义布局中各个控件的id，需要与上面的from对应*/
                adapter = new SimpleAdapter(this, data, R.layout.city_item,
                new String[] { "city" }, new int[] { R.id.city });
        scGridView.setAdapter(adapter);
    }

    @SuppressWarnings("unchecked")
    @Override
    //搜索框搜索，达到页面的跳转
    public void onClick(View v) {
        switch (v.getId()) {
         /*   case R.id.scBackImg:
                MainActivity.this.finish();
                break;*/
            case R.id.scCityBtn:
                myCity = scCityEd.getText().toString();
                // 根据城市名去查找天气信息，可以参考下方的word文档（ApiStoreSDK(Android) 使用说明文档 ）
                Parameters para = new Parameters();
                para.put("area", myCity);
                ApiStoreSDK
                        .execute(
                                "http://apis.baidu.com/showapi_open_bus/weather_showapi/address",
                                ApiStoreSDK.GET, para, new ApiCallBack() {
                                    @Override
                                    public void onSuccess(int status,
                                                          String responseString) {
                                        Log.v("r的值", responseString);

                                        if (responseString.contains("找不到此地名")) {
                                            new AlertDialog.Builder(
                                                    MainActivity.this).setMessage(
                                                    "找不到此地名").show();
                                        } else {
                                            // cityFlag用来区别GridView和搜索的两种跳转方式
                                            cityFlag = 1;
                                            Intent z = new Intent(
                                                    MainActivity.this,
                                                    Weather.class);
                                            z.putExtra("mycity", myCity);
                                            z.putExtra("cityFlag", cityFlag);
                                            startActivity(z);
                                        }
                                    }

                                    @Override
                                    public void onComplete() {
                                    }

                                    @Override
                                    public void onError(int status,
                                                        String responseString, Exception e) {

                                    }
                                });
                break;

            default:
                break;
        }
    }

}
