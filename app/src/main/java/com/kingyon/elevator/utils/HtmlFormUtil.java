package com.kingyon.elevator.utils;

import com.orhanobut.logger.Logger;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by Leo on 2017/6/28.#
 */

public class HtmlFormUtil {


    private static final String baseHtml = "<html>  <meta name=\"format-detection\" content=\"telephone=no\" />  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=2.0\"/><body style=\"padding:0px;\">%s</body></html>";

    public static String formatHtml(String content) {
        return String.format(baseHtml, dealHtml(content));
    }

    public static String dealHtml(String data) {
        Document doc = Jsoup.parse(data, "UTF-8");
        Element head = doc.head();
//        head.clearAttributes();
//        Elements children = head.children();
//        if (children != null) {
//            children.remove();
//        }

//        Element list = head.getElementById("list");
//        if (list != null) {
//            list.remove();
//        }

        head.prepend("<meta name=\"format-detection\" content=\"telephone=no\" />");
        head.prepend("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0\"/>");
        head.append("<style type=\"text/css\"> body{word-wrap:break-word;font-family:Arial;max-width:96%;width:96%;height:auto;margin:auto} img{max-width:100%;height:auto}*{max-width:100% !important;}</style>");
        Element body = doc.body();
        body.attr("style", "max-width:100% !important");
//        body.append("<style type=\"text/css\"> body{width:100%;height:auto;margin:auto} img{max-width:100%} </style>");


//        Element body = doc.body();
//        Elements allElements = body.getAllElements();
//        if (allElements != null && allElements.size() > 0) {
//            for (Element element : allElements) {
//                if (element != null) {
//                    String style = element.attr("style");
//                    element.attr("style", style + "max-width:100%;");
//                }
//            }
//        }
        Logger.d(doc.toString());
        return doc.toString();
    }

    public static String dealHtml(String data, String color) {
        Document doc = Jsoup.parse(data, "UTF-8");
        Element head = doc.head();
//        head.clearAttributes();
//        Elements children = head.children();
//        if (children != null) {
//            children.remove();
//        }

//        Element list = head.getElementById("list");
//        if (list != null) {
//            list.remove();
//        }

        head.prepend("<meta name=\"format-detection\" content=\"telephone=no\" />");
        head.prepend("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, user-scalable=no, minimum-scale=1.0, maximum-scale=1.0\"/>");
        head.append("<style type=\"text/css\"> body{background-color:" + color + ";word-wrap:break-word;font-family:Arial;width:100%;height:auto;margin:auto} img{max-width:100%;height:auto} </style>");
//        Element body = doc.body();
//        body.attr("style", "padding:0px;");
//        body.append("<style type=\"text/css\"> body{width:100%;height:auto;margin:auto} img{max-width:100%} </style>");


//        Element body = doc.body();
//        Elements allElements = body.getAllElements();
//        if (allElements != null && allElements.size() > 0) {
//            for (Element element : allElements) {
//                if (element != null) {
//                    element.attr("max-width", "100%");
//                }
//            }
//        }
        Logger.d(doc.toString());
        return doc.toString();
    }
}
