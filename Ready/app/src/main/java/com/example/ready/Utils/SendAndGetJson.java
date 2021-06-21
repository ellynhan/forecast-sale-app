package com.example.ready.Utils;

import android.content.Context;
import android.util.Log;
import com.example.ready.DB.DBHelper;
import com.example.ready.DB.Model.Menu;
import com.example.ready.DB.Model.Sale;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SendAndGetJson {
    private DBHelper dbHelper;
    private ArrayList<Menu> menus;
    private int id;

    public void send(Context context) {
        dbHelper = DBHelper.getInstance(context);
        menus = dbHelper.getMenu();

        for(int i = 0; i < 1; i++) {
            int menu_id = menus.get(i)._id;
            id = menu_id;

            SendJson sendJson = new SendJson(menu_id);
            sendJson.start();
        }
    }

    public class SendJson extends Thread {
        private int menu_id;

        SendJson(int menu_id) {
            this.menu_id = menu_id;
            id = menu_id;
        }

        @Override
        public void run() {
            try {
                HttpURLConnection httpURLConnection = null;
                URL url = new URL("http://112.185.185.213:5000/send");

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                httpURLConnection.setRequestProperty("Accept", "application/json");
                httpURLConnection.setDoOutput(true);
                DataOutputStream dataOutputStream = null;

                ArrayList<Sale> sales = dbHelper.getAllSaleWithId(menu_id);

                JSONArray jsonArray = new JSONArray();

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
                SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy.M.d");
                String dateString;

                for (int j = 0; j < sales.size(); j++) {
                    JSONObject jsonObject = new JSONObject();
                    Date date = simpleDateFormat.parse(sales.get(j).date);
                    dateString = simpleDateFormat1.format(date);

                    jsonObject.put("date", dateString);
                    jsonObject.put("day", sales.get(j).day);
                    jsonObject.put("sky", sales.get(j).sky);
                    jsonObject.put("rain", sales.get(j).rain);
                    jsonObject.put("sale", sales.get(j).qty);
                    jsonArray.put(jsonObject);
                }

                    Log.i("JSON", jsonArray.toString());
//
                    dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    dataOutputStream.writeBytes(jsonArray.toString());

                    Log.i("STATUS", String.valueOf(httpURLConnection.getResponseCode()));
                    Log.i("MSG" , httpURLConnection.getResponseMessage());

                    GetJson getJson = new GetJson();
                    getJson.run();

                dataOutputStream.flush();
                dataOutputStream.close();

                httpURLConnection.disconnect();

            } catch(MalformedURLException e) {
                e.printStackTrace();
            } catch(IOException e) {
                e.printStackTrace();
            }  catch (JSONException e) {
                e.printStackTrace();
            } catch(NullPointerException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public class GetJson extends Thread {
        @Override
        public void run() {
            String string;

            try {
                HttpURLConnection httpURLConnection = null;
                URL url = new URL("http://112.185.185.213:5000/test");

                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setRequestProperty("Content-Type", "application/json");

                InputStreamReader inputStreamReader = new InputStreamReader(
                        httpURLConnection.getInputStream(),
                        "UTF-8");

                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuffer stringBuffer = new StringBuffer();
                while ((string = bufferedReader.readLine()) != null) {
                    stringBuffer.append(string);
                }
                bufferedReader.close();
                jsonParse(stringBuffer.toString());
            } catch(MalformedURLException e) {
                e.printStackTrace();
 
            } catch(JSONException e) {
                e.printStackTrace();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void jsonParse(String json) throws JSONException {
        JSONObject jsonObject = new JSONObject(json);

        int todayQty = jsonObject.getInt("b");
        int tomorrowQty = jsonObject.getInt("a");

        System.out.println(jsonObject.get("b"));
        System.out.println(jsonObject.get("a"));

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

        Date date = calendar.getTime();
        String today = simpleDateFormat.format(date);

        calendar.add(Calendar.DAY_OF_YEAR, 1);
        date = calendar.getTime();
        String tomorrow = simpleDateFormat.format(date);

        Sale sale = new Sale();
        sale.menu_id = id;
        sale.qty = todayQty;
        sale.sky = 0;
        sale.rain = 0;
        sale.date = today;
        sale.holiday = false;
        sale.time = true;

        dbHelper.insertSale(sale);

        sale.qty = tomorrowQty;
        sale.sky = 0;
        sale.rain = 0;
        sale.date = tomorrow;
        sale.holiday = false;
        sale.time = true;

        dbHelper.insertSale(sale);
    }
}
