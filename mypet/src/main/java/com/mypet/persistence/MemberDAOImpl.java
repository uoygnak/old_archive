package com.mypet.persistence;

import java.util.List;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;

import com.mypet.domain.MemberVO;
import com.mypet.security.UserRole;

@Repository
public class MemberDAOImpl implements MemberDAO {
	private static final String namespace = "com.mypet.mapper.MemberMapper";	
	@Inject
	SqlSession session;

	@Override
	public MemberVO selectById(String user_id) throws Exception {
		return session.selectOne(namespace+".selectById",user_id);
	}

	@Override
	public List<UserRole> selectPermissionById(Integer user_no) throws Exception {
		return null;
	}
	
	
	
	
	
	
	

}
