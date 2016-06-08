package com.dingyu.miracleenglish.parse;

import android.content.res.AssetManager;
import android.util.Log;

import com.dingyu.miracleenglish.data.Item;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dyu on 16-6-2.
 */

public class ParseSrt {
    private static final String TAG = "ParseSrt";

    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

    public static List<Item> parse(AssetManager assetManager, String fileName){
        Log.d(TAG, "fileName="+fileName);
        try {
            InputStream inputStream = assetManager.open(fileName);
            return parse(inputStream);
        }catch (IOException e){
            e.printStackTrace();
        }
        return new ArrayList<Item>();
    }

    public static List<Item> parse(InputStream inputStream){
        try{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            return parse(bufferedReader);
        }catch (IOException e){
            e.printStackTrace();
        }
        return new ArrayList<Item>();
    }

    private static List<Item> parse(BufferedReader bufferedReader) throws IOException{
        String str = bufferReader2String(bufferedReader);
        if(str != null){
            str = dealLineBreak(str);
        }

        if(str == null){
            throw new EOFException();
        }

        String nl = "\\r?\\n";
        String sp = "[\\t]*";
        String regular = "\\d+" + sp + nl
                       + "\\d{2,}.*" + sp + nl
                       + ".*" + sp + nl
                       + ".*" + sp + nl + nl;
        Pattern pattern = Pattern.compile(regular, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(str);

        ArrayList<Item> result = new ArrayList<Item>();
        while (matcher.find()){
            String[] lines = matcher.group().split("\n");
            Item item = new Item();

            item.setId(Integer.parseInt(lines[0]));

            String[] time = lines[1].split("-->");
            item.setStartTime(parseTime("1970-01-01 " + time[0].trim()));
            item.setEndTime(parseTime("1970-01-01 " + time[1].trim()));

            item.setChinese(lines[2]);
            item.setEnglish(lines[3]);

            result.add(item);
        }

        return merge(result);
    }

    private static ArrayList<Item> merge(ArrayList<Item> items){
        ArrayList<Item> result = new ArrayList<Item>();

        if(items == null || items.size() == 0) return result;

        Item item = items.get(0);
        for (int i = 1; i < items.size() - 1; i++){
            Item temp = items.get(i);
            if(item.getEndTime() == temp.getStartTime()){
                item.setEndTime(temp.getEndTime());
                item.setEnglish(item.getEnglish()+temp.getEnglish());
                item.setChinese(item.getChinese()+temp.getChinese());
            }else {
                result.add(item);
                item = temp;
            }
        }

        return result;
    }

    private static String bufferReader2String(BufferedReader br) throws IOException {
        StringBuffer out = new StringBuffer();
        String line = "";
        while ((line = br.readLine()) != null) {
            out.append(line + "\n");
        }
        return out.toString();
    }

    private static String dealLineBreak(String content){
        String p = "(\\s*\\n){2,}";
        Pattern pattern = Pattern.compile(p, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(content);
        return matcher.replaceAll("\r\n\r\n");
    }

    private static long parseTime(String timeStr){
        try {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT0"));
            Date date = simpleDateFormat.parse(timeStr);
            return date.getTime();
        }catch (ParseException e){
            e.printStackTrace();
        }
        return 0;
    }
}
