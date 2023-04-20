package com.xwj.service.impl;

import com.xwj.mapper.PollutionMapper;
import com.xwj.pojo.Pollution;
import com.xwj.service.PollutionService;
import com.xwj.util.CountUtils;
import com.xwj.util.PollutionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class    PollutionServiceImpl implements PollutionService {

    @Autowired
    private PollutionMapper pollutionMapper;

    @Autowired
    private CountUtils countUtils;

    @Autowired
    private PollutionUtils pollutionUtils;

    /*
    *  获取所有省份平均污染水平
    * */
    @Override
    public List<Map<String, Object>> getAllProvincePollutions() {
        // 开始进行业务处理


        List<Pollution> pollution = pollutionMapper.selectAvgPollutionGroupByProvince();

        List<Map<String,Object>> pollutions = new ArrayList<>();

        pollution.forEach(v ->{
            Map<String, Object> map = new LinkedHashMap<>();
            // 计算aqi
            // double aqi = getAQI();
            //double aqi = 55.5;
            Double aqi = countUtils.getAQI(v);

            map.put("name",v.getProvince());
            List<Double> doubles = new LinkedList<>();
            // 保留两位小数
            doubles.add(v.getLon());
            doubles.add(v.getLat());
            doubles.add(aqi);
            doubles.add(v.getPm2());
            doubles.add(v.getPm10());
            doubles.add(v.getSo2());
            doubles.add(v.getNo2());
            doubles.add(v.getCo());
            doubles.add(v.getO3());

            // 格式化doubles
            Double[] formatData = countUtils.formatData(doubles);
            map.put("value",formatData);

            // 放入集合
            pollutions.add(map);
        });
        return pollutions;
    }

    /*
    *  根据省份名称以及年份获取数据
    * */
    @Override
    public List<Map<String, Object>> getCityInfoByProvince(String year,String name) {
        // 判断year 为哪一年也就是查询哪张表
        List<Pollution> list = null;
        switch (year){
            case "2013":
                list = pollutionMapper.selectCityByName2013(name);
                break;
            case "2014":
                list = pollutionMapper.selectCityByName2014(name);
                break;
            case "2015":
                list = pollutionMapper.selectCityByName2015(name);
                break;
            case "2016":
                list = pollutionMapper.selectCityByName2016(name);
                break;
            case "2017":
                list = pollutionMapper.selectCityByName2017(name);
                break;
            case "2018":
                list = pollutionMapper.selectCityByName2018(name);
                break;
            default:
                list = new ArrayList<>();
                break;
        }

        List<Map<String,Object>> cityInfos = new ArrayList<>();

        list.forEach(v->{
            Map<String, Object> map = new LinkedHashMap<>();
            // 计算aqi
            // double aqi = getAQI();
            //double aqi = 55.5;
            Double aqi = countUtils.getAQI(v);

            map.put("name",v.getCity());
            List<Double> doubles = new LinkedList<>();
            // 保留两位小数
            doubles.add(v.getLon());
            doubles.add(v.getLat());
            doubles.add(aqi);
            doubles.add(v.getPm2());
            doubles.add(v.getPm10());
            doubles.add(v.getSo2());
            doubles.add(v.getNo2());
            doubles.add(v.getCo());
            doubles.add(v.getO3());

            // 格式化doubles
            Double[] formatData = countUtils.formatData(doubles);
            map.put("value",formatData);

            // 放入集合
            cityInfos.add(map);
        });

        return cityInfos;
    }

    @Override
    public Map<String, Object> getSomeAvgCount() {
        return null;
    }

    @Override
    public List<Map<String, Object>> getTenProvinceAsc() {
        return null;
    }

    @Override
    public List<Double[]> getSixAverage() {
        return null;
    }

    @Override
    public List<Map<String, Object>> getInfoByYear(String year) {
        return null;
    }

    @Override
    public Map<String, Object> getSomeCityAvgCount(String name) {
        return null;
    }

    @Override
    public List<Map<String, Object>> getTenCityAsc(String name) {
        return null;
    }

    @Override
    public List<Double[]> getSixAverageByProvince(String name) {
        return null;
    }


}
