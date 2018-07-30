package com.example.a6_newsclient;

import com.example.a6_newsclient.javabean.News;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XmlParserUtils {
    // 解析xml的业务方法
    public static List<News> parserXml(InputStream in) {
        List<News> newsList = null;
        try {
            News news = null;
            // 1.获取xml的解析器
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            // 2.设置解析器
            parser.setInput(in, "utf-8");
            // 3.获取解析的事件类型
            int type = parser.getEventType();
            // 4.不停的向下解析
            while (type != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                // 5.具体判断一下解析的是开始结点还是结束结点
                switch (type) {
                    case XmlPullParser.START_TAG:
                        // 6.具体判断一下解析的是哪个开始标签
                        if ("channel".equals(nodeName)) {
                            newsList = new ArrayList<News>();
                        } else if ("item".equals(nodeName)) {
                            news = new News();
                        } else if ("title".equals(nodeName)) {
                            news.setTitle(parser.nextText());
                        } else if ("description".equals(nodeName)) {
                            news.setDescription(parser.nextText());
                        } else if ("image".equals(nodeName)) {
                            news.setImage(parser.nextText());
                        } else if ("type".equals(nodeName)) {
                            news.setType(parser.nextText());
                        } else if ("comment".equals(nodeName)) {
                            news.setComment(parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("item".equals(nodeName)) {
                            // 把javabean添家到集合
                            newsList.add(news);
                        }
                        break;
                }
                type = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newsList;
    }
}
