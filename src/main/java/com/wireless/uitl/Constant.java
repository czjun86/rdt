package com.wireless.uitl;

public class Constant {

	public static int PAGESIZE = 10;
	//读取消息头偏移量
	public static final int offset = 7;
	//读取消息体长度(含在消息头中的)
	public final static int len = 4;
	//读取消息头长度
	public final static int headLen = 11;
	//数据库int占位符
	public final static int INTPLACEHOLDER = -9999;
	
	public final static int TOKEN_RESUBMIT = -100;
	//GIS导出每次读取行数
	public final static int READ_ROW = 500;
	//GIS报表分页行数
	public final static int SHEET_ROW = 300000;
	//生成报表存放位置
	public final static String WXCS_REPORT_TEMPLATE = "/template/report/export/";
}
