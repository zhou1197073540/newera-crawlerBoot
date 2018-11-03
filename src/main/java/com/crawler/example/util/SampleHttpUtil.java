package com.crawler.example.util;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.GZIPInputStream;

public class SampleHttpUtil {
    public static int MAX_REQUEST = 0;
    private static Logger logger = LoggerFactory
            .getLogger(SampleHttpUtil.class);

    /**
     * 模拟get请求
     *
     * @param urlStr 请求的url
     * @return
     */
    public static String getResult(String urlStr) throws Exception {
        URL url = null;
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        InputStream inputStream = null;
        try {
            // 建立连接
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("accept-encoding", "gzip,deflate,br");
            connection.setUseCaches(false);
            connection.setConnectTimeout(40000);
            connection.setReadTimeout(40000);
            connection.setRequestProperty("Referer", urlStr);
            connection
                    .setRequestProperty("User-Agent",
                            "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:53.0) Gecko/20100101 Firefox/53.0");
            inputStream = connection.getInputStream();
            String charset = "UTF-8";
            String contentEncoding = connection.getContentEncoding();
            if (null == contentEncoding) {
                contentEncoding = charset;
            }
            String headerField = connection.getHeaderField("Content-Type");
            if (headerField.contains("charset")) {
                String[] content_types = headerField.split("\\;");
                for (int i = 0; i < content_types.length; i++) {
                    if (content_types[i].contains("charset")) {
                        charset = content_types[i].split("\\=")[1];
                        break;
                    }
                }
            }
            // 获得返回结果
            if (contentEncoding.contains("gzip")) {
                reader = new BufferedReader(new InputStreamReader(
                        new GZIPInputStream(inputStream), charset));
            } else {
                reader = new BufferedReader(new InputStreamReader(inputStream,
                        charset));
            }
            StringBuffer buffer = new StringBuffer();
            String line = reader.readLine();
            while (line != null) {
                buffer.append(line);
                try {
                    line = reader.readLine();
                } catch (Exception e) {
                    line = null;
                }
            }
            return buffer.toString();
        } catch (IOException e) {
            if (e.getMessage().contains("Read timed out")) {
                MAX_REQUEST++;
                if (MAX_REQUEST <= 3) {
                    getResult(urlStr);
                }
                MAX_REQUEST = 0;
            }
            throw e;
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 模拟get请求
     *
     * @param urlStr  请求的url
     * @param charset 编码方式
     * @return
     */
    public static String getResult(String urlStr, String charset)
            throws Exception {
        URL url = null;
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        InputStream inputStream = null;
        try {
            // 建立连接
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("accept-encoding", "gzip,deflate");
            connection.setUseCaches(false);
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);
            connection
                    .setRequestProperty("User-Agent",
                            "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:31.0) Gecko/20100101 Firefox/31.0");
//            connection
//                    .setRequestProperty("Cookie",
//                            "UOR=ent.ifeng.com,widget.weibo.com,spr_sinamkt_buy_lhykj_weibo_t105; SINAGLOBAL=13397393708.914906.1517368285815; ULV=1528335683198:16:2:2:8028486528971.253.1528335683160:1528257692307; SUHB=08J7KxQvZpiTJ1; SCF=AsLV80KVzRRbnfIPlC5bRLRx1vNY4I88MwPh_EvIc3o9X-C-xgXQ-tnx2mzSOB195YknwNhfCtRx-ls7F-IKFN4.; UM_distinctid=1634e6fdee35a0-012312d03421778-173a7640-15f900-1634e6fdee6847; SUBP=0033WrSXqPxfM72wWs9jqgMF55529P9D9W5_Np1klNssdA3bN2SRgoR-5JpV2K2p1KM7S0efShW5MP2Vqcv_; SUB=_2AkMsRVVcdcPxrAZVnPwcxWzha4VH-jyfkDyqAn7uJhMyAxh87nslqSVutBF-XGpfi4LdKIEkzjvKgCkkMS1c6nKQ; _s_tentry=-; Apache=8028486528971.253.1528335683160; TC-Ugrow-G0=968b70b7bcdc28ac97c8130dd353b55e; login_sid_t=561b689ce66782b144793af85d7a8966; cross_origin_proto=SSL; TC-V5-G0=31f4e525ed52a18c5b2224b4d56c70a1; TC-Page-G0=1bbd8b9d418fd852a6ba73de929b3d0c; WBtopGlobal_register_version=cd58c0d338fe446e; YF-Page-G0=46f5b98560a83dd9bfdd28c040a3673e; YF-Ugrow-G0=ad83bc19c1269e709f753b172bddb094; YF-V5-G0=cd5d86283b86b0d506628aedd6f8896e; appkey=; WB_register_version=cd58c0d338fe446e; wb_view_log=1600*9001");
            inputStream = connection.getInputStream();
            String contentEncoding = connection.getContentEncoding();
            if (null == contentEncoding) {
                contentEncoding = charset;
            }
            String headerField = connection.getHeaderField("Content-Type");
            if (headerField.contains("charset")) {
                String[] content_types = headerField.split("\\;");
                for (int i = 0; i < content_types.length; i++) {
                    if (content_types[i].contains("charset")) {
                        charset = content_types[i].split("\\=")[1];
                        break;
                    }
                }
            }
            // 获得返回结果
            if (contentEncoding.contains("gzip")) {
                reader = new BufferedReader(new InputStreamReader(
                        new GZIPInputStream(inputStream), charset));
            } else {
                reader = new BufferedReader(new InputStreamReader(inputStream,
                        charset));
            }
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            return buffer.toString();
        } catch (IOException e) {
            if (e.getMessage().contains("Read timed out")) {
                MAX_REQUEST++;
                if (MAX_REQUEST <= 3) {
                    getResult(urlStr);
                }
                MAX_REQUEST = 0;
            }
            throw e;
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url 发送请求的 URL
     */
    public static String doPost(String url, Map<String, String> param)
            throws Exception {
        // 建立HttpPost对象
        HttpPost httppost = new HttpPost(url);
        // 建立一个NameValuePair数组，用于存储欲传送的参数
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // 添加参数
        for (Entry<String, String> entity : param.entrySet()) {
            params.add(new BasicNameValuePair(entity.getKey(), entity
                    .getValue()));
        }
        // 设置编码
        httppost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
        //===========================================
        //===========================================
        // 发送Post,并返回一个HttpResponse对象
        HttpResponse response = new DefaultHttpClient().execute(httppost);
        if (response.getStatusLine().getStatusCode() == 200) {// 如果状态码为200,就是正常返回
            String result = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
            return result;
        }
        return response.getStatusLine().toString();
    }

    /**
     * @param urlStr  请求url
     * @param jsonStr json字符串
     * @return
     * @throws Exception
     */
    public static String doPost(String urlStr, Object jsonStr) throws Exception {
        BufferedReader in = null;
        String result = "";
        OutputStream os = null;
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlStr);
            // 然后我们使用httpPost的方式把lientKey封装成Json数据的形式传递给服务器
            // 在这里呢我们要封装的时这样的数据
            // 我们把JSON数据转换成String类型使用输出流向服务器写
            String content = String.valueOf(jsonStr);
            // 现在呢我们已经封装好了数据,接着呢我们要把封装好的数据传递过去
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10000);
            // 设置允许输出
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            // 设置User-Agent: Fiddler
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:57.0) Gecko/20100101 Firefox/57.0");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setRequestProperty("Accept", "text/plain");
            os = conn.getOutputStream();
            os.write(content.getBytes(HTTP.UTF_8));
            os.flush();
            // 定义BufferedReader输入流来读取URL的响应

            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), HTTP.UTF_8));
            String line;
            if (conn.getResponseCode() == 200) {
                while ((line = in.readLine()) != null) {
                    result += line;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }// 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (os != null) {
                    os.close();
                }
                if (in != null) {
                    in.close();
                }
                if (conn != null) {
                    conn.disconnect();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
