package com.sample.sample_ca.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.sample_ca.dao.CurrencyDao;
import com.sample.sample_ca.entity.CurrencyChineseNameData;
import com.sample.sample_ca.entity.CurrencyData;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class CurrencyService {

    private static String apiURL = "https://api.coindesk.com/v1/bpi/currentprice.json";

    private static SimpleDateFormat UTC_DATE_FORMAT = new SimpleDateFormat("MMM d, yyyy HH:mm:ss", Locale.US);
    private static SimpleDateFormat ISO_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.UK);
    private static SimpleDateFormat UK_DATE_FORMAT = new SimpleDateFormat("MMM d, yyyy 'at' HH:mm", Locale.UK);

    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


    @Autowired
    CurrencyDao currencyDao;

    /**
     * 查詢幣別對應表資料List API
     */
    public List<CurrencyChineseNameData> getCurrencyChineseNameList() throws IOException {
        List<CurrencyChineseNameData> returnData = currencyDao.findCurrencyChineseNameList();
        return returnData;
    }

    /**
     * 新增/更新幣別對應表資料 API
     */
    public void saveCurrencyChineseNameData(List<CurrencyChineseNameData> data) {
        currencyDao.saveAll(data);
    }

    /**
     * 刪除幣別對應表資料 API
     */
    public void deleteCurrencyChineseNameData(int id) {
        currencyDao.deleteById(id);
    }

    /**
     * 呼叫 coindesk API
     */
    public JSONObject getCoindeskData() {

        JSONObject returnData = new JSONObject();
        try {
            returnData = getCurrentPriceData();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnData;
    }


    /**
     * 呼叫資料轉換的 API
     */
    public List<CurrencyData> getCurrencyData() {
        return transferToCurrencyResultData(convertJsonObjectToMap(getCoindeskData()));
    }

    //查詢幣別對應表資料
    public CurrencyChineseNameData getCurrencyChineseNameData(int id) {

        try {
            return currencyDao.findById(id).get();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    //init幣別對應表資料 API 資料
    public void initCurrencyChineseNameData() throws IOException {

        List<CurrencyChineseNameData> initData = new ArrayList<>();
        CurrencyChineseNameData testDataA = new CurrencyChineseNameData();

        testDataA.setCurrencyCode("USD");
        testDataA.setCodeName("美元");

        CurrencyChineseNameData testDataB = new CurrencyChineseNameData();
        testDataB.setCurrencyCode("GBP");
        testDataB.setCodeName("英鎊");

        CurrencyChineseNameData testDataC = new CurrencyChineseNameData();
        testDataC.setCurrencyCode("EUR");
        testDataC.setCodeName("歐元");

        initData.add(testDataA);
        initData.add(testDataB);
        initData.add(testDataC);

        currencyDao.saveAll(initData);

    }


    private List<CurrencyData> transferToCurrencyResultData(Map<String, Object> currentPriceMap) {
        List<CurrencyData> currencyList = new ArrayList<>();

        try {
            Map<String, Object> timeMap = (Map<String, Object>) currentPriceMap.get("time");
            Map<String, Object> bpiMap = (Map<String, Object>) currentPriceMap.get("bpi");

            Map<String, Object> chineseNameMap = convertToMap(getCurrencyChineseNameList());

            currencyList.add(getCurrencyObject(timeMap.get("updated").toString(), (Map<String, Object>) bpiMap.get("USD"), UTC_DATE_FORMAT, "UTC", chineseNameMap));
            currencyList.add(getCurrencyObject(timeMap.get("updatedISO").toString(), (Map<String, Object>) bpiMap.get("GBP"), ISO_DATE_FORMAT, "", chineseNameMap));
            currencyList.add(getCurrencyObject(timeMap.get("updateduk").toString(), (Map<String, Object>) bpiMap.get("EUR"), UK_DATE_FORMAT, "BST", chineseNameMap));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return currencyList;
    }

    private Map<String, Object> convertToMap(List<CurrencyChineseNameData> currencyChineseNameList) {

        Map<String, Object> returnMap = new HashMap<>();

        if (currencyChineseNameList.size() > 0) {

            for (CurrencyChineseNameData object : currencyChineseNameList) {
                returnMap.put(object.getCurrencyCode(), object.getCodeName());
            }

        }

        return returnMap;
    }

    private CurrencyData getCurrencyObject(String updatedISO, Map<String, Object> currencyMap, SimpleDateFormat format, String zone, Map<String, Object> chineseMap) {

        CurrencyData returnData = new CurrencyData();
        try {
            if (false == StringUtils.isEmpty(zone)) {
//                format.setTimeZone(TimeZone.getTimeZone(zone));
                updatedISO = updatedISO.replace(zone, "").trim();
            }
            Date updateDate = format.parse(updatedISO);
            returnData.setUpdateTime(DATE_FORMAT.format(updateDate));
            returnData.setCurrencyCode(currencyMap.get("code").toString());
            returnData.setCodeName(chineseMap.get(currencyMap.get("code").toString()).toString());
            returnData.setRate((Double) currencyMap.get("rate_float"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnData;
    }

    private Map<String, Object> convertJsonObjectToMap(JSONObject currentPriceData) {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> data = null;
        try {
            data = mapper.readValue(
                    currentPriceData.toString(), new TypeReference<Map<String, Object>>() {
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return data;

    }


    private JSONObject getCurrentPriceData() throws IOException {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpget = new HttpGet(apiURL);// 建立httpPost
        String charSet = "UTF-8";
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpget);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();

                JSONObject rtnObject = new JSONObject(EntityUtils.toString(responseEntity, charSet));
                return rtnObject;
            } else {
                System.out.println("請求返回:" + state + "(" + apiURL + ")");
            }
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
