package com.mypet.admin.controller;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mypet.domain.PageMaker;
import com.mypet.domain.SearchCriteria;
import com.mypet.service.MemberService;

/**
 *  회원 관리
 */
@Controller
@RequestMapping("/admin/usermanagent")
public class AdminMemberController {
	private static final Logger logger = LoggerFactory.getLogger(AdminMemberController.class);
	@Inject
	MemberService service;
	
	
//	
	/*	회원 리스트 보기*/
	@Transactional
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public void memberLists(@ModelAttribute("cri") SearchCriteria cri, Model model) throws Exception {
		logger.info("AdminMemberController.memberLists....GET");

		model.addAttribute("list",service.listSearchCriteria(cri));
		
		PageMaker pageMaker = new PageMaker();
		pageMaker.setCri(cri);
		pageMaker.setTotalCount(service.listSearchCount(cri));			
		model.addAttribute("pageMaker",pageMaker);		
	}
	
	
	/*	회원 상세 정보 보기*/
	/*	회원 정보 수정 */
	
	
	
}