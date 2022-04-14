package com.sample.sample_ca.dao;

import com.sample.sample_ca.entity.CurrencyChineseNameData;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author Allen
 */
public interface CurrencyDao extends CrudRepository<CurrencyChineseNameData, Integer> {

    @Query(value = "select *  from CURRENCY_CHINESE_NAME_DATA  order by id"
            , nativeQuery = true)
    List<CurrencyChineseNameData> findCurrencyChineseNameList();


}
