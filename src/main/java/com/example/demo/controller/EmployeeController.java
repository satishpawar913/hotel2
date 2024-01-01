package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class EmployeeController {
	
	@RequestMapping(value = { "/user" }, method = RequestMethod.GET)
	public String hotel() {
		return "user";
	}
	
	

}
