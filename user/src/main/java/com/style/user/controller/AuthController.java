package com.style.user.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author leon
 * @date 2020-09-21 16:06:46
 */
@RestController
public class AuthController {

	@GetMapping("/get")
	//@PreAuthorize(value = "hasAuthority('get')")
	public String get() {
		return "获取接口";
	}

	@PostMapping("/update")
	public String update() {
		return "更新接口";
	}

	@GetMapping("/info")
	public String info() {
		return "详情 自由查看 不受限制";
	}
}
