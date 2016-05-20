package com.wireless.ability.schedual.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 容器监听
 * @author tian.bo
 */
public class CustomApplicationListener implements ApplicationListener<ContextRefreshedEvent> {

    private static Logger log = LoggerFactory.getLogger(CustomApplicationListener.class);

    private boolean done = false;

    public void onApplicationEvent(ContextRefreshedEvent event) {
    	//只运行一次
        if (done) return;
        done = true;
        ApplicationContext ctx = event.getApplicationContext();
        // 启动Scheduler
        log.info("start scheduler");
   
        SchedualContainer schedualContainer =
                ctx.getBean(BeanNames.SCHEDUALCONTAINER, SchedualContainer.class);
        try {
        	// init
            schedualContainer.init();
        } catch (Exception se) {
            log.error("init  sche", se);
        }
    }

}
