package com.wireless.web.utils;

/**
 * 
 * @ClassName: WebConstants
 * @Description: 该类是用于定义所有web业务模块相关的常量类
 * @author tian.bo
 * @date 2014年2月27日 下午7:12:21
 *
 */
public class WebConstants {
	//一键测试
	public final static String TEST_SHORTCUT_FLAG="test_shortcut_flag";
	
    public static final int LONIN_STATUS_SUCCESS = 1000; // 登陆成功
    public static final int LONIN_STATUS_NOBODY = 1001; // 用户不存在
    public static final int LONIN_STATUS_PWDERROR = 1002; // 密码错误
    public static final int LONIN_STATUS_LOCKED = 1003; // 用户被锁定
    public static final int LONIN_STATUS_NOMENU = 1004; // 用户没有菜单
    public static final int LONIN_STATUS_CODEERROR = 1005; //验证码错误
    public static final int LONIN_STATUS_DBFAILD = -1; // 数据库失败
    
    
    //用户
    public static final String USER_ALL_RIGHTS = "user_all_rights";
    public static final String USER_OP_RIGHTS = "user_op_rights"; //菜单权限    
    public static final String USER_OP_FUNCTIONS = "user_op_functions"; //功能权限
    public static final String USER_OP_ACTIVE = "user_op_active"; //当前被被选中的菜单
    public static final String USER_OP_PARENT_ACTIVE = "user_op_parent_active"; //当前被选中的父级菜单
    public static final String USERINFO = "userinfo"; //用户信息
    public static final String USER = "user"; //用户信息
    public static final String VERIFYCODE = "verifyCode"; //验证码
    public static final String USER_HOME_PAGE = "user_home_page";
    
    //分页
    public static final int PAGE_DEFAULT_PAGESIZE = 10;
    public static final int PAGE_DEFAULT_PAGEINDEX = 1;
    
 // 所有模板存放路径
 	public final static String APP_TEMPLATE_PATH = "/template/";
 	// 报表模块数据导出文件存放路径
 	public final static String APP_REPORT_TEMPLATE_EXPORT_PATH = "/template/report/export/";
 	//终端导入模板
 	public final static String EPINFO_TEMPLATE_PATH = "/template/ImportTemplate.xlsx";
 	
 	public static final int CHART_DEFAULT_SIZE = 15; // 图表默认展示数量
 	
 	public static final String VERMANAGER_CONF_ADDRESS = "config/versionManager.properties"; // 版本管理配置文件地址
    
    
}
