package com.crawler.example.util;

public class StringUtil {
    public static boolean isEmpty(String str){
        if(str==null||str.trim().isEmpty()) return true;
        return false;
    }
    public static boolean isEmpty(String... strs){
        boolean isEmpty=false;
        for(String str:strs){
            if(isEmpty(str)){
                isEmpty=true;
                break;
            }
        }
        return isEmpty;
    }

    /**
     * 去除字符串首尾的[]
     */
    public static String removeMark(String str){
        if(isEmpty(str)) return str;
        String txt=null;
        if(!"[]".equals(str)){
            if(str.startsWith("[")&&str.endsWith("]")){
                txt=str.substring(1,str.length()-1);
            }
        }
        if(str.contains("[\"")&& str.contains("\"]")){
            txt=str.replace("[\"","").replace("\"]","");
        }
        if(null==txt) txt=str;
        return txt;
    }

    /**
     * 获取前100个字符串
     */
    public static String gainFirst100Words(String str){
        if(isEmpty(str)||str.length()<100) return str;
        return str.substring(0,100)+"...";
    }
    public static String getRealTime(String create_at) {
        if(isEmpty(create_at)) return "";
        try {
            if(create_at.contains("分钟前")){
                int mm=Integer.parseInt(create_at.replaceAll("[^0-9]",""));
                return TimeUtil.beforeCurrentMillis(-mm*60*1000);
            }else if(create_at.contains("小时前")){
                int mm=Integer.parseInt(create_at.replaceAll("[^0-9]",""));
                return TimeUtil.beforeCurrentMillis(-mm*60*60*1000);
            }else if(create_at.contains("秒前")){
                int mm=Integer.parseInt(create_at.replaceAll("[^0-9]",""));
                return TimeUtil.beforeCurrentMillis(-mm*1000);
            }else if(create_at.contains("昨天")){
                String time=create_at.replaceAll("[^0-9:]","");
                return TimeUtil.getDate(-1)+" "+time;
            }else if(create_at.contains("刚刚")){
                return TimeUtil.getDateTime();
            }
        }catch (Exception e){
        }
        return create_at;
    }

    public static String formatContentTxt(String content) {
        if(isEmpty(content)) return content;
        if(content.contains("&nbsp;")){
            content= content.replace("&nbsp;"," ");
        }
        if(content.contains("网页链接")){
            content=content.replace("网页链接","");
        }
        content=content.replaceAll("&.*?;","");
        return content;
    }

    public static String prefixStockCode(String code){
        if(isEmpty(code)) return null;
        if(code.startsWith("sz")||code.startsWith("sh")) return code;
        String code_type="unknown";
        if(code.startsWith("0")||code.startsWith("3")){
            code_type="sz"+code;
        }else if(code.startsWith("6")){
            code_type="sh"+code;
        }
        return code_type;
    }

    public static void main(String[] args) {
        String txt="大师攀岩&gt;&gt;7ug;hiajs";
        System.out.println(formatContentTxt(txt));
    }



}
