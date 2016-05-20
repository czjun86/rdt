package com.wireless.web.service.taskManager;

import java.io.File;
/**
 * 文件删除
 * @author tian.bo
 */
public class DeleteFileTask {

	// 文件路径
	private String path;
	
	public DeleteFileTask(String path) {
		this.path = path;
	}

	public void run() {
		try {
			File file = new File(this.path); 
			if(file.exists()){
				deleteDir(file);
			}
		} catch (Exception e) {
			
		}
	}
	
	private boolean deleteDir(File dir) {		
		if (dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;					
				}
			}
		}else{
			return dir.delete();
		}	
		return true;
	}
	
}
