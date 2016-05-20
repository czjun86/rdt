package com.wireless.web.action.commManager;
import org.springframework.web.servlet.ModelAndView;
public class SuperAction {
	protected ModelAndView redirect(String url) {
		return new ModelAndView("redirect:" + url);
	}
}
