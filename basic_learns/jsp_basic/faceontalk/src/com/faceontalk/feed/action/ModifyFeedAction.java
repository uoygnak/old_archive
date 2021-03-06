package com.faceontalk.feed.action;

import java.sql.Connection;
import java.sql.SQLException;

import com.faceontalk.feed.dao.FeedDAO;
import com.faceontalk.feed.dto.FeedVO;
import com.faceontalk.jdbc.JdbcUtil;
import com.faceontalk.jdbc.connection.ConnectionProvider;

public class ModifyFeedAction {
	private FeedDAO feedDao = FeedDAO.getInstance();
	
	public int update(FeedVO feed) {		
		Connection conn = null;
		int result = -1;
		try {
			conn = ConnectionProvider.getConnection();
			result = feedDao.update(conn,feed);
		} catch(SQLException e) {
			throw new RuntimeException(e);
		} finally {
			JdbcUtil.close(conn);		
		}
		return result;
	}
	
}
