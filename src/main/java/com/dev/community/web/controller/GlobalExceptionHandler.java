package com.dev.community.web.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.dev.community.exception.DataNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(DataNotFoundException.class)
	public ModelAndView handleDataNotFoundException(DataNotFoundException e) {
		ModelAndView mav = new ModelAndView("error/data_not_found");
		mav.addObject("message", "데이터를 찾을 수 없습니다.");
		mav.addObject("location", "/");
		return mav;
	}

}
