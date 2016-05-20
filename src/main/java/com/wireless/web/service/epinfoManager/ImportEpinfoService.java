package com.wireless.web.service.epinfoManager;

import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.wireless.web.dao.epinfoManager.EpinfoDao;
import com.wireless.web.dao.epinfoManager.ImportEpinfoDao;
import com.wireless.web.service.commManager.BaseService;
import com.wireless.web.utils.TimeUtils;

@Service("importEpinfoService")
public class ImportEpinfoService extends BaseService {

	private static final Logger logger = LoggerFactory
			.getLogger(ImportEpinfoService.class);
	@Autowired
	public ImportEpinfoDao dao;

	/**
	 * 解析数据
	 * @param file
	 * @param filePath
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String, Object>> analytical(MultipartFile file, String filePath) throws Exception {
		//解析EXCEL
		InputStream inp = null;
		inp = file.getInputStream();
		//获取EXCEL内容
		List<Map<String, Object>> list = this.getContent(inp);
		List<Map<String, Object>> ls = this.validate(list);
		return ls;
	}
	
	/**
	 * 获取EXCEL内容
	 * @param inp
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String, Object>> getContent(InputStream inp) throws Exception{
		//EXCEL内容集合
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		//根据input流生成excel
		XSSFWorkbook wb = new XSSFWorkbook(inp);
		//获得excel的第一页
		XSSFSheet sheet = wb.getSheetAt(0);
		String[] col ={"imei","model","operator","province","district","area","memo"};
		for(int i=1;i<sheet.getLastRowNum()+1;i++){
			map = new HashMap<String, Object>();//当前行数据
			//获取行
	    	XSSFRow row = sheet.getRow(i);
	    	if(row==null){
	    		break;
	    	}
	    	XSSFCell cell= null;
	    	for(int j=0;j<7;j++){
	    		cell = row.getCell(j);
	    		map.put(col[j], getValue(cell));
	    		
	    	}
	    	if(map.get("imei")==null &&
    			map.get("model")==null &&
    			map.get("operator")==null &&
    			map.get("province")==null){//IMEI,终端型号,运营商,省  为空则为最后一行
	    		break;
	    	}
	    	list.add(map);
		}
		return list;
	}
	
	/**
	 * 获取单元格内容
	 */
	public String getValue(XSSFCell cell){
		String value = null;
		if(cell==null){
			return value;
		}
		//查出单元格类型
		try {
			switch(cell.getCellType()){
			case XSSFCell.CELL_TYPE_STRING://字符串--文本类型
					value = cell.getRichStringCellValue().toString().trim();
				break;
			case XSSFCell.CELL_TYPE_NUMERIC://数字类型
				if (DateUtil.isCellDateFormatted(cell)){//时间类型
					value = cell.getDateCellValue().toString();
				}else{
					DecimalFormat df = new DecimalFormat("0");
					String str = String.valueOf(df.format(cell.getNumericCellValue())).trim();
					if(str!=null&&!"".equals(str)){
						String [] strArray =str.split(".");
						if(strArray!=null&&strArray.length>1){
							value = strArray[0];
						}else{
							value = str;
						}
					}
				}
				break;
			case XSSFCell.CELL_TYPE_BOOLEAN://boolean类型
					value = String.valueOf(cell.getBooleanCellValue()).trim();
				break;
			case XSSFCell.CELL_TYPE_FORMULA://公式类型
					value = cell.getCellFormula().trim();
				break;
			}
		} catch (Exception e) {
			logger.error("get lead excel null",e);
		}
		return value;
	}
	/**
	 * 验证及入库
	 * @param List<List<Integer>> 每一个List<Integer>代表一行错误
	 * @param 		List<List<Integer>> 第一位代表行数  ，之后代表列数 
	 * @return
	 * @throws Exception 
	 */
	public List<Map<String, Object>> validate(List<Map<String, Object>> list) throws Exception{
		List<Map<String, Object>> ls = new ArrayList<Map<String, Object>>();//错误数据集合
		Map<String, Object> error = null;//单行错误数据
		List<Map<String, Object>> params = new ArrayList<Map<String, Object>>();//保存数据集合
		Map<String, Object> param = null;//单条数据内容
		List<String> hasImei = dao.queryAllImei();
		List<String> imeiMyself = new ArrayList<String>();//收集所有imei 判断是否传入imei有相同的
		String time = TimeUtils.getTime(0);
		for(int i = 0; i<list.size();i++){//数据验证以及组装
			error = new HashMap<String, Object>();
			param = new HashMap<String ,Object>();
			String words ="";
			Map<String, Object> value = list.get(i);
			
			//imei
			String imei = value.get("imei")!=null?value.get("imei").toString().trim():null;
			if(imei!=null && !"".equals(imei)){
				if(this.validateFormate("^[A-Za-z0-9]*$",imei)){
					if(strByte(imei)<20){
						if(noRepeatImei(imei,hasImei)){
							param.put("imei", imei);
						}else{
							words += "该imei已在存在数据库中,";
						}
					}else{
						words += "imei内容过长,";
					}
				}else{
					words += "imei数据类型不正确,";
				}
				imeiMyself.add(imei);
			}else{
				words += "imei不能为空,";
			}
			
			//终端型号
			String model = value.get("model")!=null?value.get("model").toString():null;
			if(model!=null && !"".equals(model.trim())){
				if(strByte(model)<50){
					param.put("term_model", model);
				}else{
					words += "终端型号内容过长,";
				}
			}else{
				words += "终端型号不能为空,";
			}
			
			//运营商
			String operator = value.get("operator")!=null?value.get("operator").toString():null;
			if(operator!=null && !"".equals(operator.trim())){
				Integer telecoms = this.validateTelecoms(operator.toString());
				if(telecoms!=-1){
					param.put("telecoms", telecoms);
				}else{
					words += "该运营商不存在,";
				}
			}else{
				words += "运营商不能为空,";
			}
			//区域
			String province =  value.get("province")!=null?value.get("province").toString():null;
			String district =  value.get("district")!=null?value.get("district").toString():null;
			String area =  value.get("area")!=null?value.get("area").toString():null;
			Map<String ,Object> va = validateArea(province,district,area);
			if(va.get("error")==null &&!"".equals(va.get("error"))){
				param.put("province", (String)va.get("province"));
				param.put("district", (String)va.get("district"));
				param.put("area", (String)va.get("area"));
				param.put("region_code", (String)va.get("region_code"));
			}else{
				words += (String)va.get("error");
			}
			
			
			
			//备注
			String memo =  value.get("memo")!=null?value.get("memo").toString():null;
			if(strByte(memo)<80){
				param.put("memo",memo);
			}else{
				words += "备注过长,";
			}
			
			if(words!=null && !"".equals(words)){//有错误信息则记录当前行有错
				error.put("name","第"+(i+2)+"行");
				error.put("info",words.substring(0, words.length()-1));
				ls.add(error);
			}
			param.put("is_del", 0);
			param.put("update_time", time);
			param.put("create_time", time);
			params.add(param);//待入库数据信息
		}
		String repeatMySelf = repeatImeiMySelf(imeiMyself);//自身重复imei
		if(repeatMySelf!=null && !"".equals(repeatMySelf)){
			error.put("name","excel内部重复imei");
			error.put("info",repeatMySelf);
			ls.add(error);
		}
		
		//没有错误信息则准备入库
		if(ls.size()<=0){
			this.baseDao.batchInsert(ImportEpinfoDao.class, params, 200);
		}
		return ls;
	}
	
	/**
	 * 验证格式
	 * @param str
	 * @param value
	 * @return
	 */
	private boolean validateFormate(String str ,String value){
		 Pattern p = Pattern.compile(str);  
	     Matcher m = null; 
	     m = p.matcher(value);
	     return m.matches(); 
	}
	
	/**
	 * 判断IMEI是否重复
	 * @param imei
	 * @param is
	 * @return
	 */
	private boolean noRepeatImei(String imei ,List<String> is){
		boolean flag = true;
		for(String s:is){
			if(s.equals(imei)){
				flag = false;
				break;
			}
		}
		return flag;
	}
	/**
	 * 验证运营商
	 * @param name
	 * @return
	 */
	private Integer validateTelecoms(String name){
		Integer id = -1;
		if("联通".equals(name)){
			id = 1;
		}else if("移动".equals(name)){
			id = 2;
		}else if("电信".equals(name)){
			id = 3;
		}
		return id;
	}
	
	/**
	 * 计算中英文混合的字节长度
	 * @throws Exception 
	 */
	private Integer strByte(String str) throws Exception{
		if(str!=null&&!("".equals(str))){
			str=new String(str.getBytes("gb2312"),"iso-8859-1");
			return str.length();
		}else{
			return 0;
		}
	}
	/**
	 * 区域验证
	 */
	private Map<String ,Object> validateArea(String pr ,String di ,String ar){
		Map<String ,Object> map = new HashMap<String ,Object>();
		Map<String ,Object> param = null;
		String error ="";
		String province =pr;
		String district =di;
		String area =ar;
		String regionCode ="500000";//默认值
		if(pr!=null && !"".equals(pr)){
			district = districtTranslation(di);
			area = (ar!=null&&!"".equals(ar)?ar:"全部");
			param = new HashMap<String ,Object>();
			param.put("province", province);
			param.put("district", district);
			param.put("area", area);
			regionCode = dao.queryArea(param);
			if(regionCode==null || "".equals(regionCode)){
				error += "找不到对应区域,";
			}
		}else{
			error += "省级区域不能为空,";
		}
		map.put("province", province);
		map.put("district", district);
		map.put("area", area);
		map.put("region_code", regionCode);
		map.put("error", error);
		return map;
	}
	
	private String districtTranslation(String name){
		String district = "全部";
		if(name!=null && !"".equals(name.trim())){
			if(name.indexOf("市辖区")>0){
				district = "市辖区/县";
			}else if(name.indexOf("省直辖区")>0){
				district = "省直辖县级行政区划";
			}else{
				district = name;
			}
		}
		return district;
	}
	/**
	 * 计算自身重复imei
	 */
	private String repeatImeiMySelf(List<String> imeis){
		String str = "";
		for(int i = 0;i<imeis.size();i++){
			for(int j = 0;j<imeis.size();j++){
				if(i != j){
					if(imeis.get(i).equals(imeis.get(j))){
						str += imeis.get(i)+",";
					}
				}else{
					continue;
				}
			}
		}
		if(str!=null && !"".equals(str)){
			str = str.substring(0, str.length()-1);
		}
		return str;
	}
}
