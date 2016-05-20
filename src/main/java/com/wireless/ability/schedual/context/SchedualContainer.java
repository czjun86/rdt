package com.wireless.ability.schedual.context;

import java.util.HashSet;
import java.util.Set;

import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.StatefulJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.wireless.ability.schedual.config.Task;
import com.wireless.ability.schedual.config.TasksConfig;
import com.wireless.web.service.taskManager.TaskService;


/**
 * Scheduler容器
 * @author tian.bo
 */
public class SchedualContainer implements ResourceLoaderAware, ApplicationContextAware,
        DisposableBean {
    private static Logger logger = LoggerFactory.getLogger(SchedualContainer.class);
    // quartz的调度器
    private Scheduler scheduler;
    private ApplicationContext applicationContext;
    private ResourceLoader resourceLoader;
    private ConfigContainer configContainer;
    private Set<String[]> jobSet;


    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }
    
    public void setConfigContainer(ConfigContainer configContainer) {
        this.configContainer = configContainer;
    }

    private void createScheduler() throws Exception {
        // 如果没有则创建
        if (this.scheduler == null) {
            SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
            schedulerFactory.setApplicationContext(applicationContext);
            schedulerFactory.setResourceLoader(resourceLoader);
            schedulerFactory.setBeanName("scheduler");
            schedulerFactory.setAutoStartup(true);
            schedulerFactory.afterPropertiesSet();
            scheduler = schedulerFactory.getObject();
            scheduler.start();
        }
    }

    /**
     * init
     */
    public void init() throws Exception {
        if (jobSet == null) {
            jobSet = new HashSet<String[]>();
        } else {
            jobSet.clear();
        }
        runTasks();
    }

    /**
     * 任务执行
     */
    private void runTasks() {
        try {
            TasksConfig config = configContainer.getTasksConfig();
            if (config != null && config.isEnable()) {
                for (Task task : config.getTasks()) {
                    createScheduler();
                    runTask(task, config.getAppName());
                }
            }
        } catch (Exception e) {
            logger.error("runTasks error.", e);
        }
    }

    /**
     * run  task
     * @param    appName
     *               appName
     * @param    task
     *               task
     * @return   boolean
     *                      
     */
    private void runTask(Task task, String appName) throws Exception {
        CronTrigger trigger = new CronTrigger(task.getPath(), appName, task.getCron());
        // 是否允许串行
        Class<?> jobClazz = "true".equals(task.getAsync()) ? CEJob.class : CustomStatefulJob.class;
        JobDetail jobDetail = new JobDetail(task.getPath(), appName, jobClazz);
        JobDataMap dataMap = jobDetail.getJobDataMap();
        dataMap.put("task", task);
        dataMap.put("appName", appName);
        TaskService taskServices = null;
        try {
			taskServices = this.applicationContext.getBean(task.getExecute(),TaskService.class);
		} catch (Exception e) {
			logger.error(String.format("%s does not exist or does not implement the TaskService interface.", task.getExecute()), e);
		}
        // 存在service才执行
        if(taskServices != null){
	        dataMap.put("taskServices", taskServices);   
	        Context ctx = new Context();
	        ctx.setApplicationContext(applicationContext);
	        ctx.setServletContext(configContainer.getServletContext());
	        dataMap.put("context", ctx);
	        logger.info("scheduleJob:" + jobDetail.getFullName());
	        // 记录Job的名称
	        String[] jobFullName = {task.getPath(), appName};
	        jobSet.add(jobFullName);
	        this.scheduler.scheduleJob(jobDetail, trigger);
        }

    }

    /**
     * del  task
     * @param    appName
     *               appName
     * @param    task
     *               task
     * @return   boolean
     *                      
     */
    public boolean deleteTask(String appName, Task task) throws Exception {
        if (task != null) {
            try {
                logger.info("delete task:" + task.toString());
                return this.scheduler.deleteJob(task.getPath(), appName);
            } catch (Exception ex) {
                logger.error(task.toString(), ex);
            }
        }
        return false;
    }

    
    /**
     * add  task
     * @param    appName
     *               appName
     * @param    task
     *               task
     * @return   boolean
     *                      
     */
    public boolean addTask(String appName, Task task) throws Exception {
        createScheduler();
        runTask(task, appName);
        return true;
    }

    /**
     * update task
     * @param    appName
     *               appName
     * @param    task
     *               task
     * @return   boolean
     *                      
     */
    public boolean updateTask(String appName, Task task) throws Exception {
        if (deleteTask(appName, task)) {
            return this.addTask(appName, task);
        }
        return false;
    }

    private void clearJobs() throws Exception {
        //clear all jobs
        if (this.scheduler != null) {
            for (String[] jobFullName : jobSet) {
                String jobName = jobFullName[0];
                String groupName = jobFullName[1];
                this.scheduler.deleteJob(jobName, groupName);
            }
        }
    }

    /**
     * 重新执行所有的task
     */
    public synchronized void reload() throws Exception {
        clearJobs();
        init();
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    static void execute(JobExecutionContext context) throws JobExecutionException {
    	
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        Task task = (Task) dataMap.get("task");
        String appName = (String) dataMap.get("appName");
        Context ctx = (Context)dataMap.get("context");
        TaskService taskServices = (TaskService)dataMap.get("taskServices");

        if (logger.isDebugEnabled()) {
            logger.debug("run " + appName + ",task=" + task.toString());
        }
        try {
        	taskServices.runTask(ctx);
        } catch (Exception e) {
            logger.error(task.toString(), e);
        }
    }

    /**
     * 该JOB是到点就执行，不管前一次有木有执行完
     */
    public static class CEJob implements Job {

        public void execute(JobExecutionContext context) throws JobExecutionException {
            SchedualContainer.execute(context);
        }

    }

    /**
     * 该JOB是串行的，即前一次任务执行完毕后才能接着下一次
     */
    public static class CustomStatefulJob implements StatefulJob {

        public void execute(JobExecutionContext context) throws JobExecutionException {
            SchedualContainer.execute(context);
        }

    }

    public void destroy() throws Exception {
        // shutdown
        if (this.scheduler != null) {
            this.scheduler.shutdown();
        }
    }

}
