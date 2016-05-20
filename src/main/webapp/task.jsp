<?xml version="1.0" encoding="utf-8" ?>
<%@page import="com.wireless.ability.schedual.context.SchedualContainer"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="org.springframework.web.context.WebApplicationContext,org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="org.quartz.Scheduler,org.quartz.Trigger"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
  String msg=""; 
  StringBuilder sb=new StringBuilder();

  WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(application);      
  SchedualContainer schedualContainer=(SchedualContainer)ctx.getBean("schedualContainer");

  if(request.getParameter("submit")!=null){
      schedualContainer.reload();
      msg ="reload!";
  }
  
  sb.append("<ul>");
  
  Scheduler  scheduler = schedualContainer.getScheduler();
  if(scheduler!=null){
      java.util.Date dtFire =null;
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String[] groupNames=  scheduler.getJobGroupNames();      
      for(int i=0;i<groupNames.length;i++){
          sb.append("<li>").append("<span>").append(groupNames[i]).append("</span><ul>");
          String[] jobNames=  scheduler.getJobNames(groupNames[i]);   
          for(int j=0;j<jobNames.length;j++){
              Trigger[] triggers = scheduler.getTriggersOfJob(jobNames[j],groupNames[i]);
              dtFire =null;
              for(int k=0;k<triggers.length;k++){
                  dtFire = triggers[k].getNextFireTime();                       
              }                            
              sb.append("<li>").append("<span>").append(jobNames[j]).append(",nextExectueTime=");
              sb.append(dtFire==null?"":sdf.format(dtFire)).append("</span></li>");    
          }
          sb.append("<ul></li>");
      }
  }
  sb.append("</ul>");

%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Task</title>
<script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
<script type="text/javascript">

</script>
</head>
<body>
<form action="">
 <input type="submit" name="submit" value="Reload"/>
</form>
<div>
<%=msg%>
</div>
<div>
<%=sb.toString() %>
</div>
</body>
</html>