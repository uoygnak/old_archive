package org.board.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.board.util.WebConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * <h4>로그인 인터셉터 클래스</h4>
 * 
 * 
 * <strong>preHandle<strong><br>
 * <ul>
 * 	<li>기존 로그인 정보가 세션에 존재하면, 초기화</li>
 * </ul>
 * 		 
 * <strong>postHandle<strong><br>
 * <ul>
 * 	<li>로그인 정보를 세션에 담는다</li>
 *  <li>자동 로그인 체크 시, response에 자동로그인 쿠키를 담는다</li>
 *  <li>기존 요청 경로 경로로 리다이렉트 시키고 없으면 최상위 경로 "/"로 리다이렉트</li>
 * </ul>
 *  
 * 
 * @author zaccoding
 * @date 2017. 5. 1.
 */

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
	
	/**
	 *  로그인 요청 처리 전 preHandle 메소드 <br>
	 *  아래와 같은 기능을 수행 함 <br>
	 *  <ul>
	 *  	<li>로그인 요청 후 세션에 같은 사용자가 존재하면 세션을 초기화</li>
	 *  </ul>
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		logger.info("LoginInterceptor preHandle..");
		
		HttpSession session = request.getSession();
		
		if( session.getAttribute(WebConstants.LOGIN_SESSION_NAME) != null ) {
			logger.info("clear before login data in session");
			session.invalidate();
		}
		
		return true;
	}
	
	
	/**
	 *  로그인 요청 처리 후 postHandle 메소드 <br>
	 *  아래와 같은 기능을 수행 함 <br>
	 *  <ul>
	 *  	<li>로그인 VO 인스턴스를 세션에 담음</li>  	
	 *  	<li>자동로그인을 체크한 경우, 쿠키를 response에 담음(세션ID,1주일)</li>
	 *  	<li>로그인 전 요청 URL로 redirect 시킴 </li>
	 *  </ul>
	 *  
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
				
		logger.info("LoginInterceptor postHandle..");
				
		HttpSession session = request.getSession();
		
		Object memberVO = modelAndView.getModel().get("memberVO");
		
		if( memberVO != null ) {
			logger.info("new login success");
			
			session.setAttribute(WebConstants.LOGIN_SESSION_NAME, memberVO);
			
			//자동 로그인 체크
			if( request.getParameter("useCookie") != null ) {
				logger.info("use auto login service");
				Cookie loginCookie = new Cookie( WebConstants.LOGIN_COOKIE_NAME , session.getId() );
				loginCookie.setPath("/");
				loginCookie.setMaxAge(60*60*24*7); // 1week 
				response.addCookie(loginCookie);
			}
			
			//기존 요청 경로 URL 처리
			Object dest = session.getAttribute( WebConstants.URI_DESTINATION );
			if( dest != null ) 
				logger.info("dest : " + dest.toString());			
			response.sendRedirect( dest == null ? "/" : (String)dest );
		}		
	}
}













