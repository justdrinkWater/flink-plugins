package com.finchina.plugin.base.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Auther: renjianfei
 * @Date: 2020/5/29 10:23
 * @Description:
 */

public class Htmlmodify {

    public static String formatHtmlAgilityPack(String html) {
        //预处理，获取body内容
        html = getBodyHtml(html);
        //处理body内容，图片上cdn
        //结束后特殊处理
        return htmlNormal(html);
    }



    private static String getHwyBodyHtml(String html) {
        Pattern pattern = Pattern.compile("<br><br><div>------------contentstart!-------------</div><br><br><br>(.*)", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            html = matcher.group(1);
        } else {
            Matcher matcher2 = Pattern.compile("\"\"Html\"\":\"\"([\\s\\S]*?)</body></html>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE).matcher(html);
            if (matcher2.find()) {
                html = matcher2.group(1);
            }
        }
        return html;
    }


    /**
     * 获取html中的body
     *
     * @param html
     * @return
     */
    private static String getBodyHtml(String html) {
        //采集最开始数据为json数据，次数为获取json中hrml内容
        if (!html.contains("class=\"div1\"")) {
            try {
                JSONObject json = JSON.parseObject(html);

                html = json.getString("qwContent");
            } catch (Exception e) {
                Matcher matcher = Pattern.compile("\"\"Html\"\":\"\"([\\s\\S]*?)</body></html>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE).matcher(html);
                if (matcher.find()) {
                    html = matcher.group(1);
                }
            }
        }

        //删除class='div1'
        html = replace(html, "<div\\s*class=\"div1\"[^<>]*?>([\\s\\S]*?\"\\})</div><br/>", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        html = replace(html, "<div\\s*class=\"div1\"[^<>]*?>([\\s\\S]*?)</div>", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        return html;
    }


    private static String replace(String source, String regex, String target, int flags) {
        // 过滤table标签
        Pattern p = Pattern.compile(regex, flags);
        Matcher m = p.matcher(source);

        return m.replaceAll(target);
    }


    /// <summary>
    /// Html标准处理无图片
    /// </summary>
    /// <param name="path3"></param>
    /// <returns></returns>
    private static String htmlNormal(String htmlStr) {

        //移除script的正则表达式
        //var BodyHTML = Regex.Replace(HtmlCode, "<script[^>]*?>[\\s\\S]*?<\\/script>", "", RegexOptions.IgnoreCase | RegexOptions.Multiline);
        htmlStr = replace(htmlStr, "<script[^>]*?>[\\s\\S]*?<\\/script>", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        //移除style的正则表达式
        //BodyHTML = Regex.Replace(BodyHTML, "<style[^>]*?>[\\s\\S]*?<\\/style>", "", RegexOptions.IgnoreCase | RegexOptions.Multiline);
        htmlStr = replace(htmlStr, "<style[^>]*?>[\\s\\S]*?<\\/style>", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        //移除link的正则表达式
        // BodyHTML = Regex.Replace(BodyHTML, @"(<link.*\s+href=(?:""[^""]*""|'[^']*')[^<]*>)", "", RegexOptions.IgnoreCase | RegexOptions.Multiline);
        htmlStr = replace(htmlStr, "(<link.*\\s+href=(?:\"\"[^\"\"]*\"\"|'[^']*')[^<]*>)", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        ////删除<meta[^>]*?> 标记
        //BodyHTML = Regex.Replace(BodyHTML, "<meta[^>]*?>", "", RegexOptions.IgnoreCase | RegexOptions.Multiline);
        //BodyHTML = Regex.Replace(BodyHTML, "</meta[^>]*?>", "", RegexOptions.IgnoreCase | RegexOptions.Multiline);
        htmlStr = replace(htmlStr, "<meta[^>]*?>", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        htmlStr = replace(htmlStr, "</meta[^>]*?>", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);


        //去掉html标签中所有标签属性保留img a table tr td TBODY
        htmlStr = replace(htmlStr, "<((?!IMG)(?!A)(?!TABLE)(?!THEAD)(?!TFOOT)(?!TR)(?!TD)(?!TH)(?!TBODY)[a-zA-Z0-9]+)\\s*[^><]*>", "<$1>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        ////去掉img标签
        //去除img标签除src以外其他属性
        htmlStr = replace(htmlStr, "<img\\b[^>]*?\\s+(src\\s*=\\s*['\"]\\s*[^'\">]*?\\s*['\"]).*?>", "<img $1/>", Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        //去掉a标签属性
        htmlStr = replace(htmlStr, "<a\\b[^>]*>", "<a>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        //去除表格相关标签除cospan,rospan以外属性
        htmlStr = replace(htmlStr, "(<(?!/)(?:TABLE|TBODY|THEAD|TFOOT|TH|TR|TD))(?:(?:(?!(?:rowspan|colspan))[^>])+)?((?:rowspan|colspan)\\s*=\\s*['\"\"]?\\s*\\d+\\s*['\"\"]?)?(?:(?:(?!(?:rowspan|colspan))[^>])+)?((?:rowspan|colspan)\\s*=\\s*['\"\"]?\\s*\\d+\\s*['\"\"]?)?(?:[^>]*?)(>)", "$1 $2 $3$4", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);


        //去掉 DOCTYPE
        //BodyHTML = Regex.Replace(BodyHTML, "<!DOCTYPE([\\s\\S]*?)>", "", RegexOptions.IgnoreCase | RegexOptions.Multiline);
        htmlStr = replace(htmlStr, "<!DOCTYPE([\\s\\S]*?)>", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        //去掉 head
        //BodyHTML = Regex.Replace(BodyHTML, "<head>([\\s\\S]*?)</head>", "", RegexOptions.IgnoreCase | RegexOptions.Multiline);
        htmlStr = replace(htmlStr, "<head>([\\s\\S]*?)</head>", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        //去掉正文中的input框
        htmlStr = replace(htmlStr, "<input([\\s\\S]*?)>", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        htmlStr = replace(htmlStr, "</input[^>]*?>", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        //去掉 title
        //BodyHTML = Regex.Replace(BodyHTML, "<title>([\\s\\S]*?)</title>", "", RegexOptions.IgnoreCase | RegexOptions.Multiline);
        htmlStr = replace(htmlStr, "<title>([\\s\\S]*?)</title>", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        //html
        //BodyHTML = Regex.Replace(BodyHTML, "<html([\\s\\S]*?)>", "", RegexOptions.IgnoreCase | RegexOptions.Multiline);
        //BodyHTML = Regex.Replace(BodyHTML, "</html[^>]*?>", "", RegexOptions.IgnoreCase | RegexOptions.Multiline);
        htmlStr = replace(htmlStr, "<html([\\s\\S]*?)>", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        htmlStr = replace(htmlStr, "</html[^>]*?>", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        //body
        //BodyHTML = Regex.Replace(BodyHTML, "<body([\\s\\S]*?)>", "", RegexOptions.IgnoreCase | RegexOptions.Multiline);
        htmlStr = replace(htmlStr, "<body([\\s\\S]*?)>", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        //body
        //BodyHTML = Regex.Replace(BodyHTML, "</body></html>", "", RegexOptions.IgnoreCase | RegexOptions.Multiline);
        htmlStr = replace(htmlStr, "</body></html>", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        //body
        //BodyHTML = Regex.Replace(BodyHTML, "</body[^>]*?>", "", RegexOptions.IgnoreCase | RegexOptions.Multiline);
        htmlStr = replace(htmlStr, "</body[^>]*?>", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);


        ///拆分数据
        //BodyHTML = Regex.Replace(BodyHTML, "<pstyle[\\s\\S]*?>", "<p>", RegexOptions.IgnoreCase | RegexOptions.Multiline);
        //BodyHTML = Regex.Replace(BodyHTML, "</pstyle[\\s\\S]*?>", "</p>", RegexOptions.IgnoreCase | RegexOptions.Multiline);
        htmlStr = replace(htmlStr, "<pstyle[\\s\\S]*?>", "<p>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        htmlStr = replace(htmlStr, "</pstyle[\\s\\S]*?>", "</p>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        //BodyHTML = Regex.Replace(BodyHTML, "<palign[\\s\\S]*?>", "<p>", RegexOptions.IgnoreCase | RegexOptions.Multiline);
        //BodyHTML = Regex.Replace(BodyHTML, "</palign[\\s\\S]*?>", "</p>", RegexOptions.IgnoreCase | RegexOptions.Multiline);
        htmlStr = replace(htmlStr, "<palign[\\s\\S]*?>", "<p>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        htmlStr = replace(htmlStr, "</palign[\\s\\S]*?>", "</p>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        //BodyHTML = Regex.Replace(BodyHTML, "<pclass[\\s\\S]*?>", "<p>", RegexOptions.IgnoreCase | RegexOptions.Multiline);
        //BodyHTML = Regex.Replace(BodyHTML, "</pclass[\\s\\S]*?>", "</p>", RegexOptions.IgnoreCase | RegexOptions.Multiline);
        htmlStr = replace(htmlStr, "<pclass[\\s\\S]*?>", "<p>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        htmlStr = replace(htmlStr, "</pclass[\\s\\S]*?>", "</p>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        //BodyHTML = Regex.Replace(BodyHTML, "<spanstyle[\\s\\S]*?>", "<span>", RegexOptions.IgnoreCase | RegexOptions.Multiline);
        //BodyHTML = Regex.Replace(BodyHTML, "</spanstyle[\\s\\S]*?>", "</span>", RegexOptions.IgnoreCase | RegexOptions.Multiline);
        htmlStr = replace(htmlStr, "<spanstyle[\\s\\S]*?>", "<span>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        htmlStr = replace(htmlStr, "</spanstyle[\\s\\S]*?>", "</span>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        //BodyHTML = Regex.Replace(BodyHTML, "</spanstyle[\\s\\S]*?>", "</span>", RegexOptions.IgnoreCase | RegexOptions.Multiline);
        htmlStr = replace(htmlStr, "</spanstyle[\\s\\S]*?>", "</span>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        htmlStr = htmlStr.replace("p t ; ” > ", "");
        htmlStr = htmlStr.replace("p t ; ” &gt; ", "");//p t ” &gt; p t ; ” >
        htmlStr = htmlStr.replace("p t ” &gt; ", "");//p t ” &gt;
        htmlStr = htmlStr.replace("p t ” > ", "");//p t ” &gt;
        htmlStr = htmlStr.replace("&nbsp;", " ")
                .replace("&ldquo;", "“")
                .replace("&rdquo;", "”")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&amp;", "&")
                .replace("p t ” ", "");
        //去掉html
        //var partext = Regex.Replace(BodyHTML, @"<[^>]+>", "", RegexOptions.IgnoreCase | RegexOptions.Multiline);
        if (htmlStr.contains(" s p a n s t y l e")) {
            //BodyHTML = Regex.Replace(BodyHTML, @ "<[^<>]*?\s*s\s*t\s*y\s*l\s*e\s*=[^<>]*\:(?!\s*/)[\da - zA - Z\s]+", "", RegexOptions.IgnoreCase | RegexOptions.Multiline);
            htmlStr = replace(htmlStr, "<[^<>]*?\\s*s\\s*t\\s*y\\s*l\\s*e\\s*=[^<>]*\\:(?!\\s*/)[\\da-zA-Z\\s]+", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        }

        //BodyHTML = Regex.Replace(BodyHTML, @"<\s+[^<>=]*\s+[^<>]*>", "", RegexOptions.IgnoreCase | RegexOptions.Multiline);
        //页面中尽量不要有无意义的空标签，如： <div></div>，<p></p>,<div>&nbsp;</div>,<p>&nbsp;</p>
        htmlStr = replace(htmlStr, "<\\s+[^<>=]*\\s+[^<>]*>", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        //页面中尽量不要有无意义的空标签，如： <div></div>，<p></p>,<div>&nbsp;</div>,<p>&nbsp;</p>
        htmlStr = replace(htmlStr, "<div\\b[^>]*?>[\\s]*?(?:&nbsp;)*[\\s]*?</div>", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        htmlStr = replace(htmlStr, "<p\\b[^>]*?>[\\s]*(?:&nbsp;)*</p>", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);


        // 换行、文本缩进不要使用转义字符，如\\n\\r  \\r\\n  \\\\n\\\\r   \\\\r\\\\n  \\n   \\\\n  \\t   \\\\t
        htmlStr = htmlStr.replace("\\n", "")
                .replace("\\r", "")
                .replace("\\\\n", "")
                .replace("\\\\r", "")
                .replace("\\t", "")
                .replace("\t", "")
                .replace("\r", "")
                .replace("\\\\t", "").replace("\n", "");

        if (StringUtils.isNotBlank(htmlStr)) {
            String regex = "<body[^>]*>([\\s\\S]*?)<\\/body>";//使用非贪婪模式
            Matcher matcher = Pattern.compile(regex).matcher(htmlStr);
            if (matcher.find()) {
                htmlStr = matcher.group(1);
            }
        }
        //特殊处理
        htmlStr = formatHtmlSpecial(htmlStr);

        return htmlStr;
    }



    private static boolean havaKeyWord(String str, String regex, int isFlags) {

        Matcher matcher = Pattern.compile(regex, isFlags).matcher(str);
        return matcher.find();
    }


    /// <summary>
    /// 文书网特殊处理
    /// </summary>
    /// <param name="BodyHTML"></param>
    /// <returns></returns>
    private static String formatHtmlSpecial(String htmlStr) {
        //判断路径是否为空
        if (htmlStr == null || htmlStr.length() < 1) {
            return "";
        }
        htmlStr = htmlStr.replace("p t ; ” > ", "");
        htmlStr = htmlStr.replace("p t ; ” &gt; ", "");//p t ” &gt; p t ; ” >
        htmlStr = htmlStr.replace("p t ” &gt; ", "");//p t ” &gt;
        htmlStr = htmlStr.replace("&nbsp;", " ")
                .replace("&ldquo;", "“")
                .replace("&rdquo;", "”")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&quot;", "\"")
                .replace("&amp;", "&")
                .replace("p t ” ", "");

        if (havaKeyWord(htmlStr, ".MsoNormal\\{[\\s\\S]*?\\}", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE)) {
            htmlStr = replace(htmlStr, ".MsoNormal\\{[\\s\\S]*?\\}", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        }

        //BodyHTML = Regex.Replace(BodyHTML, "<style[^>]*?>", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        //BodyHTML = Regex.Replace(BodyHTML, "\\{[a-zA-Z]\\}", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        htmlStr = replace(htmlStr, "<style[^>]*?>", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
        htmlStr = replace(htmlStr, "\\{[a-zA-Z]\\}", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);

        //[^<>/](div>)
        //if (Regex.IsMatch(BodyHTML, @ "[^<>/](div>)"))
        return htmlStr;

    }

    /// <summary>
    /// html法院公告特殊处理
    /// </summary>
    /// <param name="BodyHTML"></param>
    /// <returns></returns>
    public static String htmlSpecial(String bodyHTML, String stype) {
        //判断路径是否为空
        if (StringUtils.isEmpty(bodyHTML)) {
            return "";
        }
        if ("法院公告".equals(stype)) {
            //div rmfy-contents-foot-download
//            if (Regex.IsMatch(BodyHTML, @"<div\s*class=""rmfy-contents-foot-download"">([\s\S]*?)</div>", RegexOptions.IgnoreCase | RegexOptions.Multiline))
//            {
//                BodyHTML = Regex.Replace(BodyHTML, @"<div\s*class=""rmfy-contents-foot-download"">([\s\S]*?)</div>", "", RegexOptions.IgnoreCase | RegexOptions.Multiline);
//            }

            if (havaKeyWord(bodyHTML, "<div\\s*class=\"\"rmfy-contents-foot-download\"\">([\\s\\S]*?)</div>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE)) {
                bodyHTML = replace(bodyHTML, "<div\\s*class=\"\"rmfy-contents-foot-download\"\">([\\s\\S]*?)</div>", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
            }
            //div
//            if (Regex.IsMatch(BodyHTML, @"<div\s*class=""rmfy-type-right"">([\s\S]*?)</div>", RegexOptions.IgnoreCase | RegexOptions.Multiline))
//            {
//                BodyHTML = Regex.Replace(BodyHTML, @"<div\s*class=""rmfy-type-right"">([\s\S]*?)</div>", "", RegexOptions.IgnoreCase | RegexOptions.Multiline);
//            }
            if (havaKeyWord(bodyHTML, "<div\\s*class=\"\"rmfy-type-right\"\">([\\s\\S]*?)</div>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE)) {
                bodyHTML = replace(bodyHTML, "<div\\s*class=\"\"rmfy-type-right\"\">([\\s\\S]*?)</div>", "", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
            }
        }
        return bodyHTML;
    }

}
