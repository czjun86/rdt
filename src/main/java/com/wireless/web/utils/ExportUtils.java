package com.wireless.web.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExportUtils {
		
	private static final Logger logger = LoggerFactory.getLogger(ExportUtils.class);

	/**
	 * @author pengdd
	 * @param request 
	 * @param sheetNames sheet页名字集合
	 * @param sheetColumns sheet页数据key集合
	 * @param titleNames 每页sheet的title名字集合
	 * @param columnNames 每页sheet的列名集合
	 * @param cellTypes 单元格类型
	 * @param lists  每页sheet的内容集合  1、文本 2、数字 3、boolean 4、公式
	 * @return 返回导出excel的文件名字
	 * @throws Exception 
	 */
	public static String createExcel(HttpServletRequest request ,
			List<String> sheetNames , List<String> sheetColumn ,
			Map<String ,Object> titleNames ,
			Map<String ,Object> columnNames ,
			Map<String ,Object> cellTypes ,
			Map<String ,Object> lists ) throws Exception{
		//生成excel
		SXSSFWorkbook swb = new SXSSFWorkbook(1000);
		for(int i = 0;i<sheetNames.size();i++){
			String sheetName = sheetNames.get(i);
			String column = sheetColumn.get(i);
			//创建sheet页
			Sheet sheet = swb.createSheet(sheetName);
			CellStyle style = getStyle(swb);
			createSheetValue(sheet,style ,
					(List<Object>)titleNames.get(column) ,
					(List<String>)columnNames.get(column) ,
					(List<Integer>)cellTypes.get(column),
					(List<Map<String ,Object>>)lists.get(column)
					);
		}
		//生成文件名字
		String name =null;
		//excel文件保存路径
		String path = request.getSession().getServletContext().getRealPath("/").replace("\\", "/")+WebConstants.APP_REPORT_TEMPLATE_EXPORT_PATH;
		String fileName = outFile(path,name,swb);
		return fileName;
	}
	
	public static void createSheetValue(Sheet sheet,CellStyle style,List<Object> titleName ,
			List<String> columnName ,List<Integer> cellType,List<Map<String ,Object>> list) throws Exception{
		//存放每列最宽宽度
		Map<Integer , Integer> size = new HashMap<Integer , Integer>();
		//创建第一行,写入title
		Row rowt = sheet.createRow(0);
		Cell cellt = null;
		if(titleName!=null &&!"".equals(titleName)){
			for(int i=0;i<titleName.size();i++){
				cellt = rowt.createCell(i); 
				cellt.setCellValue(titleName.get(i).toString());//区域
				cellt.setCellStyle(style);
				cellt.setCellType(XSSFCell.CELL_TYPE_STRING);//字符串--文本类型
				size.put(i, StringUtils.strByte(titleName.get(i).toString()));
			}
		}
		//写入数据
		Map<String ,Object> map = null;
		Row row = null;
		Cell cell = null;
		if(list!=null && columnName!=null){
			for(int i=0;i<list.size();i++){
				map = list.get(i);
				row = sheet.createRow(i+1);
				for(int j=0;j<columnName.size();j++){
					cell = row.createCell(j);
					String str = map.get(columnName.get(j))!=null&&!"N/A".equals(map.get(columnName.get(j)).toString())?map.get(columnName.get(j)).toString():null;
					compareSize(str, j,size);
					cell.setCellStyle(style);
					switch(cellType.get(j)){
						case 1://字符串--文本类型
							cell.setCellType(Cell.CELL_TYPE_STRING);
							if(str!=null){
								cell.setCellValue(str);//内容
							}else{
								cell.setCellValue("N/A");//内容
							}
						break;
						case 2://数字类型
							if(str!=null){
								cell.setCellType(Cell.CELL_TYPE_NUMERIC);
								cell.setCellValue(Double.valueOf(str));//内容
							}else{
								cell.setCellType(Cell.CELL_TYPE_STRING);//没内容的时候摄入控制
								cell.setCellValue("N/A");//内容
							}
						break;
						case 3://boolean类型
							cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
							if(str!=null){
								cell.setCellValue(str);//内容
							}
						break;
						case 4://公式类型
							cell.setCellType(Cell.CELL_TYPE_FORMULA);
							if(str!=null){
								cell.setCellValue(str);//内容
							}
						break;
						default://默认字符串--文本类型
							if(str!=null){
								cell.setCellValue(str);//内容
							}else{
								cell.setCellValue("N/A");//内容
							}
					}
				}
			}
		}
		//适应单元格列宽度
		for(int i=0;i<size.size();i++){
			sheet.setColumnWidth(i,(size.get(i)+2)*256);
		}
	}
	
	/**
	 * @author pengdd
	 * excel写入文件
	 * @param path excel路径
	 * @param name excel文件名
	 * @param swb excel内容
	 * @return 返回下载全路径
	 * @throws Exception
	 */
	private static String outFile(String path ,String name ,SXSSFWorkbook swb) throws Exception{
		//如果文件夹不存在则创建    
		File file = new File(path);
		if(!file.exists()&&!file.isDirectory())  {
			file.mkdirs(); 
		}
		//excel内容写入文件
		//模板路径和名称,创建输入流
		String excelName = StringUtils.getUuid();
		String fileName = path+excelName+".xlsx";
		FileOutputStream out =null;
		try {
			out =  new FileOutputStream(fileName);
			swb.write(out);
		} catch (Exception e) {
			throw new Exception();
		} finally{
			try {
				if(out!=null){
					out.close();
				}
			} catch (IOException e) {
				logger.error(" ",e);
			}
		}
		return excelName;
	}
	
	/**
	 * 分析sheet样式
	 */
	private static CellStyle getStyle(SXSSFWorkbook swb){
		CellStyle style = swb.createCellStyle();
		//加边框
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER_SELECTION);// 水平居中
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);// 垂直居中
		
		return style;
	}
	
	/**
	 * 判断当前内容长度大于存放长度就加入map
	 */
	private static void compareSize(String val ,Integer index ,Map<Integer ,Integer> map){
		try {
			if(val != null){
				int oldlength = map.get(index);
				int newlength = StringUtils.strByte(val);
				if(newlength>oldlength){
					map.put(index, newlength);
				}
			}
		} catch (Exception e) {
			logger.error("excel report count title length" ,e);
		}
	}
}
