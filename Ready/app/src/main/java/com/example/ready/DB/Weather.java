package com.example.ready.DB;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;

public class Weather extends Thread{
    public String func(String date, String time, String lat, String lng) throws IOException, JSONException {
        String endPoint =  "http://apis.data.go.kr/1360000/VilageFcstInfoService/";
        String serviceKey = "인증키";
        String pageNo = "1";
        String numOfRows = "10";
        String baseDate = date; //원하는 날짜 20210526
        String baseTime = time; //원하는 시간 1100
        String nx = lat; //위도 98
        String ny = lng; //경도 77

        String s = endPoint+"getVilageFcst?serviceKey="+serviceKey
                +"&pageNo=" + pageNo
                +"&numOfRows=" + numOfRows
                +"+&dataType=JSON"
                + "&base_date=" + baseDate
                +"&base_time="+baseTime
                +"&nx="+nx
                +"&ny="+ny;

        URL url = new URL(s);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader bufferedReader=null;
        if(conn.getResponseCode() == 200) {
            bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }else{
            //connection error :(
        }
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        while (bufferedReader!=null && (line = bufferedReader.readLine()) != null) {
            stringBuilder.append(line);
        }
        bufferedReader.close();
        String result= stringBuilder.toString();
        conn.disconnect();
        JSONObject mainObject = new JSONObject(result);
        JSONArray itemArray = mainObject.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");
        String passValue="";
        for(int i=0; i<itemArray.length(); i++){
            JSONObject item = itemArray.getJSONObject(i);
            String category = item.getString("category");
            if(category.equals("PTY")){
                System.out.println("PTY:   "+item.getString("fcstValue"));
                passValue+="PTY:"+item.getString("fcstValue");
            }
            else if(category.equals("SKY")){
                System.out.println("SKY:   "+item.getString("fcstValue"));
                passValue+="SKY:"+item.getString("fcstValue");
            }
        }
        return passValue;
    }
}
//SKY 하늘상태 맑음(1), 구름많음(3), 흐림(4)
//PTY 강수형태 없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4), 빗방울(5), 빗방울/눈날림(6), 눈날림(7)
//일단 0,1,4,5 만 사용하자. 눈 관련되서는 겨울에 업데이트 해볼 것.