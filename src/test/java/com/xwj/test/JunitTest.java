package com.xwj.test;

import com.xwj.BigDataAdminApplication;
import com.xwj.service.PollutionService;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;


/**
 * @ClassName: com.xwj.test.JunitTest
 * @Description:
 * @Author: Wenjie XU
 * @Date: 2023-05-27 11:46
 **/


@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = BigDataAdminApplication.class)
public class JunitTest {
    @Autowired
    private PollutionService pollutionService;

    /**
     * 测试获取所有省份六年各污染物平均值
     */
    @Test
    public void getAllProvincePollutionsTest() {
        List<Map<String, Object>> infoByYear = pollutionService.getAllProvincePollutions();
        Assert.assertNotNull(infoByYear);
    }


    /**
     * 测试根据省份名称以及年份获取污染物平均数据
     */
    @Test
    public void getCityInfoByProvinceTest() {
        List<Map<String, Object>> infoByYear = pollutionService.getCityInfoByProvince("2018", "上海市");
        for (Map<String, Object> map : infoByYear) {
            for (String key : map.keySet()) {
                if (key.equals("name")) {
                    // 判断是否是上海市的数据
                    Assert.assertEquals("上海市", map.get(key));
                } else if (key.equals("value")) {
                    Double[] array = (Double[]) map.get(key);
                    // 判断AQI计算是否正确
                    Assert.assertEquals(50.92, (double) array[2], 0.001);
                }
            }
        }

    }

    @Test
    public void getCityInfoByProvinceTestByNull() {
        // 当输入分别为null或空字符串时返回是否正确
        List<Map<String, Object>> listByNull = pollutionService.getCityInfoByProvince(null, null);
        Assert.assertNull(listByNull);
        List<Map<String, Object>> listByNullStr = pollutionService.getCityInfoByProvince("", "");
        Assert.assertNull(listByNullStr);
    }


    /**
     * 测试获取全国六年污染物平均值
     */
    @Test
    public void getSomeAvgCountTestAQI() {
        Double[] realAQI = {44.16, 42.62, 39.29, 36.48, 36.01, 35.95};

        Map<String, Object> avgMap = pollutionService.getSomeAvgCount();
        for (Map.Entry<String, Object> entry : avgMap.entrySet()) {
            if (entry.getKey().equals("aqi")) {
                Double[] testAQI = (Double[]) entry.getValue();
                // 测试得到的AQI是否正确
                Assert.assertArrayEquals(realAQI, testAQI);
            }
        }
    }

    @Test
    public void getSomeAvgCountTestRH() {
        Double[] realRH = {55.35, 55.08, 56.35, 57.57, 56.41, 55.95};

        Map<String, Object> avgMap = pollutionService.getSomeAvgCount();
        for (Map.Entry<String, Object> entry : avgMap.entrySet()) {
            if (entry.getKey().equals("rh")) {
                Double[] testRH = (Double[]) entry.getValue();
                // 测试得到的相对湿度是否正确
                Assert.assertArrayEquals(realRH, testRH);
            }
        }
    }

    @Test
    public void getSomeAvgCountTestPSFC() {

        Double[] realPSFC = {83261.95, 83231.02, 83205.86, 83386.94, 83364.79, 83313.38};

        Map<String, Object> avgMap = pollutionService.getSomeAvgCount();
        for (Map.Entry<String, Object> entry : avgMap.entrySet()) {
            if (entry.getKey().equals("psfc")) {
                Double[] testPSFC = (Double[]) entry.getValue();
                // 测试得到的地面气压是否正确
                Assert.assertArrayEquals(realPSFC, testPSFC);
            }
        }
    }

    @Test
    public void getSomeAvgCountTestTemp() {
        Double[] realTemp = {280.41, 280.29, 281.87, 279.73, 280.76, 280.57};

        Map<String, Object> avgMap = pollutionService.getSomeAvgCount();
        for (Map.Entry<String, Object> entry : avgMap.entrySet()) {
            if (entry.getKey().equals("temp")) {
                Double[] testTemp = (Double[]) entry.getValue();
                // 测试得到温度是否正确(单位：开尔文, K = 摄氏度 +273.15)
                Assert.assertArrayEquals(realTemp, testTemp);
            }
        }
    }


    /**
     * 测试获取六年中，每年AQI全国前十名省份的aqi、no2等污染物
     */
    @Test
    public void getTenProvinceAscTestProvince() {
        String[] real2018TenProvinces = {"西藏", "青海", "黑龙江", "海南", "台湾", "内蒙古", "香港", "四川", "云南", "吉林"};

        List<Map<String, Object>> tenProvinceAsc = pollutionService.getTenProvinceAsc();
        // 获得省份名称
        Map<String, Object> provinceMap = tenProvinceAsc.get(0);
        for (Map.Entry<String, Object> entry : provinceMap.entrySet()) {
            if (entry.getKey().equals("2018")) {
                String[] test2018TenProvinces = (String[]) entry.getValue();
                // 测试2018年省份排名是否正确
                Assert.assertArrayEquals(test2018TenProvinces, real2018TenProvinces);
            }
        }
    }

    @Test
    public void getTenProvinceAscTestAQI() {
        Double[] real2018TenAQI = {9.62, 14.17, 25.55, 25.61, 26.3, 26.57, 32.77, 34.51, 35.07, 35.83};

        List<Map<String, Object>> tenProvinceAsc = pollutionService.getTenProvinceAsc();

        //获得AQI
        Map<String, Object> AQIMap = tenProvinceAsc.get(1);
        for (Map.Entry<String, Object> entry : AQIMap.entrySet()) {
            if (entry.getKey().equals("2018")) {
                Double[] test2018TenAQI = (Double[]) entry.getValue();
                // 测试2018年AQI排名是否正确
                Assert.assertArrayEquals(test2018TenAQI, real2018TenAQI);
            }
        }
    }

    /**
     * 测试根据年份获取当年各省份污染物平均信息
     */
    @Test
    public void getInfoByYearTest() {
        // Lon,Lat,AQI,Pm2.5,PM10,So2,NO2,CO,O3
        Double[] realPollution = {121.42, 31.17, 53.38, 37.15, 56.76, 11.29, 37.06, 0.69, 73.28};
        List<Map<String, Object>> infoByYear = pollutionService.getInfoByYear("2017");
        for (Map<String, Object> map : infoByYear) {
            if (map.get("name").equals("上海市")) {
                Double[] testPollution = (Double[]) map.get("value");
                // 测试上海市2017年污染物平均值是否正确
                Assert.assertArrayEquals(testPollution, realPollution);
            }
        }

        // 测试年份为null,空字符串,年份超出范围是否返回正常
        List<Map<String, Object>> list = pollutionService.getInfoByYear(null);
        Assert.assertNull(list);
        list = pollutionService.getInfoByYear("");
        Assert.assertNull(list);
        list = pollutionService.getInfoByYear("2009");
        Assert.assertNull(list);
    }

    @Test
    public void getInfoByYearTestByIllegal() {
        // 测试年份为null,空字符串,年份超出范围是否返回正常
        List<Map<String, Object>> list = pollutionService.getInfoByYear(null);
        Assert.assertNull(list);
        list = pollutionService.getInfoByYear("");
        Assert.assertNull(list);
        list = pollutionService.getInfoByYear("2009");
        Assert.assertNull(list);
    }


    /**
     * 测试获取某省份六年中，每年温度、压强、相对湿度、AQI平均值
     */
    @Test
    public void getSomeCityAvgCountByTemp() {
        Double[] realTemp = {289.76, 289.08, 290.29, 288.42, 289.16, 289.1};

        Map<String, Object> map = pollutionService.getSomeCityAvgCount("重庆市");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().equals("temp")) {
                Double[] testTemp = (Double[]) entry.getValue();
                Assert.assertArrayEquals(realTemp, testTemp);
            }
        }
    }

    @Test
    public void getSomeCityAvgCountByPSFC() {
        Double[] realPSFC = {93084.89, 93022.04, 92965.02, 93231.51, 93221.33, 93155.29};

        Map<String, Object> map = pollutionService.getSomeCityAvgCount("重庆市");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().equals("psfc")) {
                Double[] testTemp = (Double[]) entry.getValue();
                Assert.assertArrayEquals(realPSFC, testTemp);
            }
        }
    }

    @Test
    public void getSomeCityAvgCountByRH() {
        Double[] realRH = {63.48, 65.88, 67.0, 69.53, 68.37, 67.83};

        Map<String, Object> map = pollutionService.getSomeCityAvgCount("重庆市");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().equals("rh")) {
                Double[] testRH = (Double[]) entry.getValue();
                Assert.assertArrayEquals(realRH, testRH);
            }
        }
    }

    @Test
    public void getSomeCityAvgCountByAQI() {
        Double[] realAQI = {76.34, 76.59, 70.52, 65.19, 59.32, 54.45};

        Map<String, Object> map = pollutionService.getSomeCityAvgCount("重庆市");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getKey().equals("aqi")) {
                Double[] testAQI = (Double[]) entry.getValue();
                Assert.assertArrayEquals(realAQI, testAQI);
            }
        }
    }

    /**
     * 测试获取六年中，每年AQI指定省份前十名的城市及其aqi、no2等污染物
     */
    @Test
    public void getTenCityAsc() {
        String[] real2018TenCity = {"舟山", "丽水", "台州", "宁波", "温州", "衢州", "金华", "绍兴", "杭州", "嘉兴"};

        List<Map<String, Object>> tenCityAsc = pollutionService.getTenCityAsc("浙江省");
        // 获得省份名称
        Map<String, Object> cityMap = tenCityAsc.get(0);
        for (Map.Entry<String, Object> entry : cityMap.entrySet()) {
            if (entry.getKey().equals("2018")) {
                String[] test2018TenCities = (String[]) entry.getValue();
                // 测试2018年城市排名是否正确
                Assert.assertArrayEquals(test2018TenCities, real2018TenCity);
            }
        }
    }

    @Test
    public void getTenCityAscByNull() {

        List<Map<String, Object>> list = pollutionService.getTenCityAsc(null);
        Assert.assertNull(list);
    }

    @Test
    public void getTenCityAscByNullString() {

        List<Map<String, Object>> list = pollutionService.getTenCityAsc("");
        Assert.assertNull(list);
    }

    @Test
    public void getTenCityAscByIllegal() {

        List<Map<String, Object>> list = pollutionService.getTenCityAsc("美国");
        Assert.assertNull(list);
    }
}




