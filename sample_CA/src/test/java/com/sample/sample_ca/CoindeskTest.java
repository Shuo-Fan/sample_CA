package com.sample.sample_ca;


import com.sample.sample_ca.controller.CurrencyController;
import com.sample.sample_ca.entity.CurrencyChineseNameData;
import com.sample.sample_ca.entity.CurrencyData;
import com.sample.sample_ca.service.CurrencyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CoindeskTest {

    @Autowired
    CurrencyController currencyController;

    @Autowired
    CurrencyService currencyService;

    @BeforeEach
    public void setCurrencyChineseData() {

        try {
            currencyService.initCurrencyChineseNameData();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 測試呼叫查詢幣別對應表資料 API
     */
    @Test
    public void testGetCurrencyChineseNameData() throws IOException {

        List<CurrencyChineseNameData> expected = new ArrayList<>();
        CurrencyChineseNameData testDataA = new CurrencyChineseNameData();
        testDataA.setId(1);
        testDataA.setCurrencyCode("USD");
        testDataA.setCodeName("美元");

        CurrencyChineseNameData testDataB = new CurrencyChineseNameData();
        testDataB.setId(2);
        testDataB.setCurrencyCode("GBP");
        testDataB.setCodeName("英鎊");

        CurrencyChineseNameData testDataC = new CurrencyChineseNameData();
        testDataC.setId(3);
        testDataC.setCurrencyCode("EUR");
        testDataC.setCodeName("歐元");

        expected.add(testDataA);
        expected.add(testDataB);
        expected.add(testDataC);

        List<CurrencyChineseNameData> actual = new ArrayList<>();
        actual = currencyController.getCurrencyChineseDataList();

        assertEquals(expected.size(), actual.size());
        assertThat(actual.get(0).getCurrencyCode())
                .isEqualTo("USD");
        assertThat(actual.get(1).getCurrencyCode())
                .isEqualTo("GBP");
        assertThat(actual.get(2).getCurrencyCode())
                .isEqualTo("EUR");

        System.out.println(" =====testGetCurrencyChineseNameData===== ");
        for (CurrencyChineseNameData data : actual) {
            System.out.println(data.toString());
        }

    }

    /**
     * 測試呼叫新增幣別對應表資料 API
     */
    @Test
    public void testInsertCurrencyChineseNameData() {

        List<CurrencyChineseNameData> expected = new ArrayList<>();
        CurrencyChineseNameData testDataA = new CurrencyChineseNameData();

        testDataA.setCurrencyCode("TWD");
        testDataA.setCodeName("台幣");
        expected.add(testDataA);

        currencyController.saveCurrencyChineseData(expected);

        CurrencyChineseNameData actual = new CurrencyChineseNameData();
        actual = currencyService.getCurrencyChineseNameData(4);

        assertEquals(expected.get(0).getCurrencyCode(), actual.getCurrencyCode());
        assertEquals(expected.get(0).getCodeName(), actual.getCodeName());

    }

    /**
     * 測試呼叫更新幣別對應表資料 API
     */
    @Test
    public void testUpdateCurrencyChineseNameData() {

        List<CurrencyChineseNameData> expected = new ArrayList<>();
        CurrencyChineseNameData testDataA = new CurrencyChineseNameData();

        testDataA.setId(1);
        testDataA.setCurrencyCode("TWD");
        testDataA.setCodeName("台幣");
        expected.add(testDataA);

        currencyController.saveCurrencyChineseData(expected);

        CurrencyChineseNameData actual = new CurrencyChineseNameData();
        actual = currencyService.getCurrencyChineseNameData(1);

        assertEquals(expected.get(0).getCurrencyCode(), actual.getCurrencyCode());
        assertEquals(expected.get(0).getCodeName(), actual.getCodeName());
        System.out.println(" =====testUpdateCurrencyChineseNameData===== ");
        System.out.println(actual.toString());
    }

    /**
     * 測試呼叫刪除幣別對應表資料 API
     */
    @Test
    public void testDeleteCurrencyChineseNameData() {

        currencyController.deleteCurrencyChineseData(1);

        CurrencyChineseNameData actual = new CurrencyChineseNameData();
        actual = currencyService.getCurrencyChineseNameData(1);

        assertThat(actual)
                .isNull();

    }

    /**
     * 測試呼叫 coindesk API，並顯示其內容。
     */
    @Test
    public void testGetCoindeskData() {

        try {

            String actual = currencyController.getCoindeskData();

            assertThat(actual)
                    .isNotEmpty();

            System.out.println(" =====testGetCoindeskData===== ");
            System.out.println(actual);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 測試呼叫資料轉換的 API，並顯示其內容
     */
    @Test
    public void testGetCurrencyData() {

        List<CurrencyData> expected = new ArrayList<>();
        CurrencyData testDataA = new CurrencyData();

        testDataA.setCurrencyCode("USD");
        testDataA.setCodeName("美元");

        CurrencyData testDataB = new CurrencyData();

        testDataB.setCurrencyCode("GBP");
        testDataB.setCodeName("英鎊");

        CurrencyData testDataC = new CurrencyData();

        testDataC.setCurrencyCode("EUR");
        testDataC.setCodeName("歐元");

        expected.add(testDataA);
        expected.add(testDataB);
        expected.add(testDataC);

        List<CurrencyData> actual = new ArrayList<>();
        actual = currencyController.getCurrencyData();

        assertEquals(expected.size(), actual.size());
        assertThat(actual.get(0).getCurrencyCode())
                .isEqualTo("USD");
        assertThat(actual.get(1).getCurrencyCode())
                .isEqualTo("GBP");
        assertThat(actual.get(2).getCurrencyCode())
                .isEqualTo("EUR");

        System.out.println(" =====testGetCurrencyData===== ");
        for (CurrencyData data : actual) {
            System.out.println(data.toString());
        }

    }


}
