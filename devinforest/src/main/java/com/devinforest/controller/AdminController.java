package com.devinforest.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.devinforest.service.AdminService;
import com.devinforest.service.RestorationService;
import com.devinforest.vo.Admin;

@Controller
public class AdminController {
	@Autowired private AdminService adminService;
	@Autowired private RestorationService restorationService;
	// 관리자 홈
	@GetMapping("/adminHome")
	public String adminHome(HttpSession session, Model model) {
		
		// 로그인 세션확인
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/index";
		}
		
		model.addAttribute("restorationCount", restorationService.inquiryStateTotalCount());
		return "admin/adminHome";
	}
	// 관리자 목록 출력
	@GetMapping("/getAdminList")
	public String getAdminList(HttpSession session, 
							   Model model,
							   @RequestParam(defaultValue = "1") int currentPage,
							   @RequestParam(defaultValue = "5") int rowPerPage,
							   @RequestParam(defaultValue = "") String searchWord) {
		// 로그인 세션확인
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/index";
		}
		System.out.println(currentPage+" <- AdminController.getAdminList: currentPage");
		System.out.println(searchWord+" <- AdminController.getAdminList: searchWord");
		Map<String, Object> map = adminService.getAdminList(currentPage, rowPerPage, searchWord);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("rowPerPage", rowPerPage);
		model.addAttribute("searchWord", searchWord);
		model.addAttribute("adminTotalCount", map.get("adminTotalCount"));
		model.addAttribute("lastPage", map.get("lastPage"));
		model.addAttribute("adminList", map.get("adminList"));
		return "admin/getAdminList";
	}
	
	// 관리자 추가
	@GetMapping("/addAdmin")
	public String addAdmin(HttpSession session) {
		// 로그인 세션확인
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/index";
		}
		return "admin/addAdmin";
	}
	@PostMapping("/addAdmin")
	public String addAdmin(HttpSession session, Admin admin) {
		// 로그인 세션확인
		if(session.getAttribute("loginAdmin")==null) {
			return "redirect:/index";
		}
		System.out.println(admin+" <- AdminController.addAdmin: admin");
		String email = "@devinforest.com";
		String adminEmail = admin.getAdminEmail()+email;
		System.out.println(adminEmail+" <- AdminController.addAdmin: adminEmail");
		admin.setAdminEmail(adminEmail);
		adminService.addAdmin(admin);
		return "redirect:/getAdminList";
	}
}
