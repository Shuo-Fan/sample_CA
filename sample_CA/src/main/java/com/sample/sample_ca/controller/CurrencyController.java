package com.sample.sample_ca.controller;

import com.sample.sample_ca.entity.CurrencyChineseNameData;
import com.sample.sample_ca.entity.CurrencyData;
import com.sample.sample_ca.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
public class CurrencyController {

    @Autowired
    CurrencyService currencyService;

    /**
     * 查詢幣別對應表資料 API
     */
    @GetMapping("/getCurrencyChineseData")
    public List<CurrencyChineseNameData> getCurrencyChineseDataList() {
        try {
            return currencyService.getCurrencyChineseNameList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 新增/更新幣別對應表資料 API
     */
    @PostMapping("/saveCurrencyChineseData")
    public void saveCurrencyChineseData(@RequestParam List<CurrencyChineseNameData> data) {
        currencyService.saveCurrencyChineseNameData(data);
    }

    /**
     * 新增/更新幣別對應表資料 API
     */
    @PostMapping("/deleteCurrencyChineseData")
    public void deleteCurrencyChineseData(@RequestParam int id) {
        currencyService.deleteCurrencyChineseNameData(id);
    }

    /**
     * 測試呼叫 coindesk API
     */
    @GetMapping("/getCoindeskData")
    public String getCoindeskData() {
        return currencyService.getCoindeskData().toString();
    }

    /**
     * 測試呼叫資料轉換的 API，並顯示其內容
     */
    @GetMapping("/getCurrencyData")
    public List<CurrencyData> getCurrencyData() {
        return currencyService.getCurrencyData();
    }


}
