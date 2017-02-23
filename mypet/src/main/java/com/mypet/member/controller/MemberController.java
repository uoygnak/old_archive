package com.mypet.member.controller;

import java.security.Principal;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mypet.domain.MemberVO;
import com.mypet.dto.EmailAuthDTO;
import com.mypet.email.EmailSenderUtil;
import com.mypet.error.DuplicateIdException;
import com.mypet.error.ExceedPeriodException;
import com.mypet.member.service.JoinService;
import com.mypet.service.MemberService;

/**
 * 가입 회원 관련
 * (예외처리 어떻게 할지 결정하기)
 */
@Controller
@RequestMapping("/user/*")
public class MemberController {
	private final Logger logger = LoggerFactory.getLogger(MemberController.class);
	private static final String USER_JOIN_SUCCESS = "/user/joinSuccess";
	private static final String USER_JOIN_FORM = "/user/join";	
	
	@Inject
	private JoinService joinService;
	@Inject
	private MemberService memberService;
	
	/**		login		*/
	@RequestMapping(value="/loginform", method=RequestMethod.GET)
	public String loginGET() {
		return "/user/loginForm";
	}
	
	/** 	join	*/
	@RequestMapping(value="/join",method=RequestMethod.GET)
	public void joinGET() {
		//return USER_JOIN_FORM;		
	}		
	@RequestMapping(value="/join",method = RequestMethod.POST)
	public String joinPOST(MemberVO vo, RedirectAttributes rttr) {
		logger.info("joinPOST.... POST");
		String viewPage = null;
		try {
			joinService.registMember(vo);			
			viewPage = USER_JOIN_SUCCESS;
		} catch(DuplicateIdException ex) {
			rttr.addFlashAttribute("msg","duplicated id");
			viewPage = USER_JOIN_FORM;
		} catch(Exception e) {
			rttr.addFlashAttribute("msg",e.getMessage());
			viewPage = USER_JOIN_FORM;
		}
		String emailAddr = EmailSenderUtil.getEmailAddr(vo.getUser_email());
		rttr.addFlashAttribute("emailAddr",emailAddr);		
		logger.info("join result viewPage = "+viewPage);
		return "redirect:"+viewPage;		
	}
	
	/** 	authentication	*/
	// /confirm_verifination/{user_id}/auth_token으로 다시 하기
	@RequestMapping(value="/confirm_verification", method=RequestMethod.GET)
	public String confirmAuth(EmailAuthDTO dto,RedirectAttributes rttr) throws Exception {		
		logger.info("confirm_verifination...");
		logger.info("user_id : "+dto.getUser_id());
		logger.info("auth_token : "+dto.getAuth_token());
		String msg = null;
		try {
			joinService.confirmAuth(dto);
			msg = "Auth Success";
		} catch(ExceedPeriodException e) {
			msg = "Exceed Period...";
		}
		
		rttr.addFlashAttribute("msg",msg);
		return "redirect:/index";
	}
	
	
	/** 	modify	*/
	@RequestMapping(value="modify",method=RequestMethod.GET)
	public void modifyGET(Model model,Principal principal) throws Exception  {
		// 인증 유저 얻기 1) 
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		String user_id = auth.getName(); // get logged in username

//		// 인증 유저 얻기 2)
//		UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		String user_id = user.getUsername();
		
		
		// 인증 유저 얻기 3)
		String user_id = principal.getName();
		model.addAttribute("vo",memberService.selectById(user_id));
	}
	
	@RequestMapping(value="modify",method=RequestMethod.POST)
	public String modifyPOST(MemberVO vo,RedirectAttributes rttr) throws Exception {		
		logger.info("/user/modify..POST : " + vo.toString());
		memberService.modifyUser(vo);
		
		rttr.addFlashAttribute("msg","success");
		return "redirect:/user/modify";		
	}
	
	
	/* security authority test*/
	@RequestMapping("/main")
	public void memberMainGET() {
		//empty
	}
	
	
	
	
	
	
	
	
	
}
