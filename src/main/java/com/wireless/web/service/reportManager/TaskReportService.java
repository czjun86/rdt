package com.wireless.web.service.reportManager;

import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wireless.support.PageBean;
import com.wireless.web.dao.reportManager.ComprehensiveDao;
import com.wireless.web.dao.reportManager.TaskReportDao;
import com.wireless.web.service.commManager.BaseService;

@Service("taskReportService")
public class TaskReportService extends BaseService {
	
	private static final Logger logger = LoggerFactory
			.getLogger(TaskReportService.class);

	@Autowired
	TaskReportDao taskReportDao;
	
	/**
	 * 分页查询
	 * @param params
	 * @return
	 */
	public PageBean queryPage(Map<String, Object> params){
		try {
			PageBean page = super.queryForList(TaskReportDao.class, params);
			return page;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	
	public String saveTask(Map<String, Object> params){
		String re = "scc";
		try {
			taskReportDao.addTask(params);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			re = "保存任务条件失败!";
		}
		return re;
	}
}
