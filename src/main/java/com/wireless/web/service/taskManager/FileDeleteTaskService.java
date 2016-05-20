package com.wireless.web.service.taskManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.wireless.ability.schedual.context.Context;
import com.wireless.web.utils.WebConstants;



/**
 * 终端管理批量导入错误文件删除任务类
 * @author tian.bo
 */
@Service("fileDeleteTaskService")
public class FileDeleteTaskService implements TaskService {
	
	private static Logger log = LoggerFactory.getLogger(FileDeleteTaskService.class);
	
	public Object runTask(Context context) {
		String app_report_export_path = String.format("%s%s", context.getServletContext().getRealPath(""),WebConstants.APP_REPORT_TEMPLATE_EXPORT_PATH);
		new Thread(new FileDeleteThread(app_report_export_path)).start();
		return null;
	}
	
	class FileDeleteThread implements Runnable {
		String path;
		DeleteFileTask task;
		public FileDeleteThread(String path){
			this.path = path;
			task = new DeleteFileTask(path);
		}

		public void run() {
			try {
				if(task != null){
					task.run();
					log.info("Mission success !!");
				}
			} catch (Exception e) {
				log.error(String.format("delete file error.path=%s", path),e);
			}
		}
		
	}

}
