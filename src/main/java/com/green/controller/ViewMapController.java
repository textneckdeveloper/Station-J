package com.green.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/viewMap")
public class ViewMapController {

	@GetMapping("")
	public String goToViewMap() {
		return "/section/viewmap";
	}
	
}
