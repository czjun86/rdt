package com.wireless.web.daoElastic.utils;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.GeoPolygonQueryBuilder;
import org.elasticsearch.index.query.PrefixQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.TermsQueryBuilder;

import com.wireless.web.model.KpiSettings;
import com.wireless.web.model.VoBean;
import com.wireless.web.utils.TimeUtils;

public class QueryUtils {

	/**
	 * 地图查询条件组装
	 * 
	 * @param vb
	 * @param type
	 *            1.采样点,2.道路
	 * @return
	 */
	public static BoolQueryBuilder qryMapCondition(VoBean vb, int type) {
		String qryKpiName = StringUtils.getKpiName(vb.getKpiType());
		// 时间格式化
		String stime = TimeUtils.chDate(vb.getStarttime().replaceAll("\\.", "-") + " 00:00:00", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		String etime = TimeUtils.chDate(vb.getEndtime().replaceAll("\\.", "-") + " 23:59:59", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		
		BoolQueryBuilder boolQuery = new BoolQueryBuilder();
		// 仅查询4G数据
		// boolQuery.must(new TermsQueryBuilder("network_type", "-1", "0", "13"));
		// 查询时间
		if (null != vb.getStarttime() && null != vb.getEndtime()) {
			boolQuery.must(new RangeQueryBuilder("test_time").from(stime).to(etime));
		}
		// 运营商
		// 轨迹对比
		if (null != vb.getLevel() && vb.getLevel() == 1) {
			// 时间对比
			if (null == vb.getDubiOperator() || vb.getDubiOperator() == "") {
				boolQuery.must(new TermQueryBuilder("telecoms", vb.getOperator()));
				// 运营商对比
			} else {
				String opt[] = vb.getDubiOperator().split(",");
				switch (opt.length) {
				case 1:
					boolQuery.must(new TermsQueryBuilder("telecoms", opt[0]));
					break;
				case 2:
					boolQuery.must(new TermsQueryBuilder("telecoms", opt[0], opt[1]));
					break;
				case 3:
					boolQuery.must(new TermsQueryBuilder("telecoms", opt[0], opt[1], opt[2]));
					break;
				default:
					break;
				}
			}
			// 非轨迹对比
		} else {
			boolQuery.must(new TermQueryBuilder("telecoms", vb.getOperator()));
		}
		// 道路等级
		if (null != vb.getRoadLevel() && vb.getRoadLevel() != -1) {
			boolQuery.must(new TermQueryBuilder("road_level", vb.getRoadLevel()));
		}
		// 道路类型
		if (null != vb.getRoadType() && vb.getRoadType() != -1) {
			boolQuery.must(new TermQueryBuilder("road_type", vb.getRoadType()));
		}
		// 道路ID
		if (null != vb.getRoadId() && !vb.getRoadId().equals("-1")) {
			boolQuery.must(new TermQueryBuilder("road_id", vb.getRoadId()));
		}
		// 区域
		if (null != vb.getArea() && !vb.getArea().equals("-1")) {
			boolQuery.must(new TermQueryBuilder("region_code", vb.getArea()));
		} else if (null != vb.getDistrict() && !vb.getDistrict().equals("-1")) {
			boolQuery.must(new PrefixQueryBuilder("region_code", vb.getDistrict().substring(0, 4)));
		} else if (null != vb.getProvince() && !vb.getProvince().equals("-1")) {
			boolQuery.must(new PrefixQueryBuilder("region_code", vb.getProvince().substring(0, 2)));
		}
		// 指标类型
		if (null != vb.getKpiType() && vb.getKpiType() != -1) {
			if (vb.getKpiType() == 4 || vb.getKpiType() == 5 || vb.getKpiType() == 6) {
				boolQuery.must(QueryBuilders.boolQuery().mustNot(new TermQueryBuilder(qryKpiName, "-998")));
			}
		}
		// 指标范围
		// 查询符号:1.> 2.>= 3.= 4.< 5.<=
		if (null != vb.getKpiVaule() && !"".equals(vb.getKpiVaule())) {
			switch (vb.getKpiSymbol()) {
			case 1:
				boolQuery.must(new RangeQueryBuilder(qryKpiName).gt(vb.getKpiVaule()));
				break;
			case 2:
				boolQuery.must(new RangeQueryBuilder(qryKpiName).gte(vb.getKpiVaule()));
				break;
			case 3:
				boolQuery.must(new TermQueryBuilder(qryKpiName, vb.getKpiVaule()));
				break;
			case 4:
				boolQuery.must(new RangeQueryBuilder(qryKpiName).lt(vb.getKpiVaule()));
			case 5:
				boolQuery.must(new RangeQueryBuilder(qryKpiName).lte(vb.getKpiVaule()));
				break;
			default:
				break;
			}
		}
		// GEO框选
		if (null != vb.getPoints() && !"".equals(vb.getPoints()) && !vb.getPoints().equals("-1")) {
			String lot = type == 1 ? "location" : "bd_location";
			GeoPolygonQueryBuilder geo = new GeoPolygonQueryBuilder(lot);

			String points[] = vb.getPoints().split(";");
			for (int i = 0; i < points.length; i++) {
				String point[] = points[i].split(",");
				geo.addPoint(Double.parseDouble(point[1]), Double.parseDouble(point[0]));
			}
			boolQuery.must(geo);
		}
		// IMEI
		if (null != vb.getImei() && !"".equals(vb.getImei())) {
			boolQuery.must(new TermQueryBuilder("imei", vb.getImei().trim()));
		}
		return boolQuery;
	}

	/**
	 * 指标区间判断
	 * 
	 * @param kpiSetMap
	 * @param kpiValue
	 * @return
	 */
	public static String kpiInterval(KpiSettings kpiSettings, Double kpiValue) {
		String itv = "0";
		int kpiState = 0;
		Double kpiStart = 0.0;
		Double KpiEnd = 0.0;
		for (int i = 6; i >= 1; i--) {
			// 获取区间值
			switch (i) {
			case 1:
				kpiState = kpiSettings.getRange_one_state();
				kpiStart = Double.parseDouble(kpiSettings.getRange_one_start());
				KpiEnd = Double.parseDouble(kpiSettings.getRange_one_end());
				break;
			case 2:
				kpiState = kpiSettings.getRange_two_state();
				kpiStart = Double.parseDouble(kpiSettings.getRange_two_start());
				KpiEnd = Double.parseDouble(kpiSettings.getRange_two_end());
				break;
			case 3:
				kpiState = kpiSettings.getRange_three_state();
				kpiStart = Double.parseDouble(kpiSettings.getRange_three_start());
				KpiEnd = Double.parseDouble(kpiSettings.getRange_three_end());
				break;
			case 4:
				kpiState = kpiSettings.getRange_four_state();
				kpiStart = Double.parseDouble(kpiSettings.getRange_four_start());
				KpiEnd = Double.parseDouble(kpiSettings.getRange_four_end());
				break;
			case 5:
				kpiState = kpiSettings.getRange_five_state();
				kpiStart = Double.parseDouble(kpiSettings.getRange_five_start());
				KpiEnd = Double.parseDouble(kpiSettings.getRange_five_end());
				break;
			case 6:
				kpiState = kpiSettings.getRange_six_state();
				kpiStart = Double.parseDouble(kpiSettings.getRange_six_start());
				KpiEnd = Double.parseDouble(kpiSettings.getRange_six_end());
				break;
			default:
				break;
			}
			// 区间判断
			switch (kpiState) {
			case 1:
				if (kpiStart < kpiValue && kpiValue < KpiEnd)
					itv = i + "";

				break;
			case 2:
				if (kpiStart <= kpiValue && kpiValue <= KpiEnd) {
					itv = i + "";
				}
				break;
			case 3:
				if (kpiStart < kpiValue && kpiValue <= KpiEnd) {
					itv = i + "";
				}
				break;
			case 4:
				if (kpiStart <= kpiValue && kpiValue < KpiEnd) {
					itv = i + "";
				}
				break;
			default:
				break;
			}
			// 匹配,则退出
			if (itv != "0") {
				break;
			}
		}
		return itv;
	}

	/**
	 * 指标区间颜色
	 * 
	 * @param kpiSetMap
	 * @param itv
	 * @return
	 */
	public static String kpiIntervalColor(KpiSettings kpiSettings, String itv) {
		String kpiColor = "";
		switch (itv) {
		case "1":
			kpiColor = kpiSettings.getRange_one_color();
			break;
		case "2":
			kpiColor = kpiSettings.getRange_two_color();
			break;
		case "3":
			kpiColor = kpiSettings.getRange_three_color();
			break;
		case "4":
			kpiColor = kpiSettings.getRange_four_color();
			break;
		case "5":
			kpiColor = kpiSettings.getRange_five_color();
			break;
		case "6":
			kpiColor = kpiSettings.getRange_six_color();
			break;
		default:
			break;
		}
		return kpiColor;
	}

	/**
	 * 
	 * 功能说明: 在所有指标配置中,获取指定指标配置 修改者名字: dsk 修改日期 2015年12月22日 修改内容
	 * 
	 * @参数： @param kpiSettings
	 * @参数： @param kpiType
	 * @参数： @return
	 * @throws
	 */
	public static KpiSettings getKpiSetByList(List<KpiSettings> kpiSettings, Integer kpiType) {
		KpiSettings kpiSet = null;
		try {
			for (KpiSettings ks : kpiSettings) {
				if (kpiType.equals(ks.getKpi_type())) {
					kpiSet = ks;
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kpiSet;
	}
}
