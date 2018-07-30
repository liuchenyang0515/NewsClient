package com.example.a6_newsclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.a6_newsclient.javabean.News;
import com.loopj.android.image.SmartImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<News> newsList;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 1.找到关心的控件
        lv = (ListView) findViewById(R.id.lv);
        // 2.准备ListView要显示的数据，去服务器取数据，进行封装
        initListData();
    }

    // 准备ListView的数据
    private void initListData() {
        new Thread() {

            @Override
            public void run() {
                // 去服务器取数据
                try {
                    String path = "http://192.168.164.1:80/news.xml";
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setConnectTimeout(5000);
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        InputStream in = conn.getInputStream();
                        // 解析xml,抽出一个业务方法
                        newsList = XmlParserUtils.parserXml(in);
                        // 更新ui
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                lv.setAdapter(new MyAdapter());
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return newsList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        private class ViewHolder {
            SmartImageView iv_icon;
            TextView tv_title, tv_desc, tv_type;
        }

        // 定义数据适配器
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder viewHolder;
            if (convertView == null) {
                view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item, parent, false);
                // 找到空间显示集合里面的数据
                viewHolder = new ViewHolder();
                viewHolder.iv_icon = (SmartImageView) view.findViewById(R.id.iv_icon);
                viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);
                viewHolder.tv_desc = (TextView) view.findViewById(R.id.tv_desc);
                viewHolder.tv_type = (TextView) view.findViewById(R.id.tv_type);
                view.setTag(viewHolder);
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            News news = newsList.get(position);
            // 展示图片的数据
            String imageUrl = news.getImage();
            // 因为ImageView没有这种根据url就获取图片资源的方法，所以用开源的smartimageview，在github下载
            // 也可以自己实现，类似于网络图片查看器，但是开源的更加稳定强大，便于使用
            // viewHolder.iv_icon.setImageUrl(imageUrl);
            viewHolder.iv_icon.setImageUrl(imageUrl, R.drawable.piclost); // 如果服务器的图片网址错误，默认显示piclost图片
            // 显示数据
            viewHolder.tv_title.setText(news.getTitle());
            viewHolder.tv_desc.setText(news.getDescription());
            String typ = news.getType();
            int type = Integer.parseInt(typ);
            String comment = news.getComment();
            switch (type) {
                case 1:
                    viewHolder.tv_type.setText(comment + "国内");
                    break;
                case 2:
                    viewHolder.tv_type.setText("跟帖");
                    break;
                case 3:
                    viewHolder.tv_type.setText("国外");
                    break;
                default:
                    break;
            }
            return view;
        }
    }
}
