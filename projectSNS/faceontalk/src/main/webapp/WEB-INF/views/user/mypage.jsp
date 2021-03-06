<!-- bootstrap source by : http://bootsnipp.com/snippets/featured/portfolio-gallery-with-filtering-category -->
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!-- include header -->
<%@ include file="/WEB-INF/views/include/header.jsp" %>

<!-- user detail css -->
<link href="/resources/bootstrap/css/userdetail.css" rel="stylesheet" type="text/css">

<style>
.profile_img img{
	  width: 50px;
	  height: 50px;
	  border: 1px solid #e7eaec;
	  margin-right: 15px;
	  margin-bottom : 10px;
}

.modal-header {
	height : 80px;
}

.modal-backdrop {
	height : 1000px;
}
</style>

<!-- container -->
<div class="container">
	<!-- 상단 profile -->
	<div class="row">
		<div class="profile-teaser-left">
			<div class="profile-img">
				<c:choose>
					<c:when test="${empty memberVO.profile_pic}">					 
						<img class="img-circle img-responsive" src="/resources/bootstrap/images/default_profile.png">
					</c:when>
					<c:otherwise>
						<img class="img-circle img-responsive" src="/displayImage?type=p&fileName=${memberVO.profile_pic}">
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		
		<div class="profile-teaser-main">
			<!-- 유저 아이디 -->
			<h2 class="profile-name">
				${memberVO.user_id}
				<!-- 팔로우 하기 버튼 -->
				<button type="button" class="btn btn-primary" id="editBtn">Edit Profile</button>
				<button type="button" class="btn btn-success" id="logoutBtn">Logout</button>
			</h2>
			<div class="profile-info">
				<div class="info">
					<h4>
					<a href="#" onclick="return false;">
						게시글&nbsp;${feedList.size()}
					</a>
					</h4>
				</div>
				<div class="info">
					<h4>
					<a href="#" onclick="return false;" id="follower_cnt">
						팔로워&nbsp;${memberVO.follower_cnt}명
					</a>
					</h4>
				</div>
				<div class="info" id="following_info">
					<h4>
						<a href="#" onclick="return false;" id="following_cnt">
						팔로잉&nbsp;${memberVO.following_cnt}명
						</a>
					</h4>
				</div>
			</div>
		</div>
	</div> <!-- .// 상단 profile 끝-->

	<!-- image list  -->
	<div class="row">
		<form action="#" method="GET">
			<c:forEach var="vo" items="${feedList}">
				<div class="gallery_product col-lg-4 col-md-4 col-sm-4 col-xs-6 filter hdpe feedImage" data-no="${vo.feed_no}" >
				<img data-no="${vo.feed_no}"
					src="/displayImage?type=f&fileName=${vo.file_name}"
				class="img-responsive imgList">
			</div>	
			</c:forEach>
		</form>
	</div>
			
	<!-- feed modal -->
	<input type="hidden" name="feed_no" id="feed_no">
	<div class="row">	
		<div class='modal fade' id='feedModal'>
    	</div> <!-- / modal -->    
	</div> <!-- .// feed modal 끝 -->	
	
</div>	<!-- .//container 끝-->


<script>
	/*		계정 수정 및 로그아웃 처리	*/		
	//edit 버튼 이벤트 처리
	$('#editBtn').on('click',function(event){
		self.location="/accounts/edit";
	});
	
	//로그아웃 버튼 이벤트 처리
	$('#logoutBtn').on('click',function(event){
		self.location="/user/logout";
	});
	
</script>

<script>

	/*		피드 및 댓글 처리		*/
	$(function() {		
	
		//팔로우 상태 출력
		/* var printFollowCnt = function() {
			var user_no = '${memberVO.user_no}';
			$.getJSON('/follow/'+user_no, function(data) {
				var follower_cnt = data.follower_cnt;
				var following_cnt = data.following_cnt;
				$('#follower_cnt').empty();
				$('#follower_cnt').html('팔로워&nbsp;'+follower_cnt+'명');
				$('#following_cnt').empty();
				$('#following_cnt').html('팔로잉&nbsp;'+following_cnt+'명');
			});		
		}; */
		
		//피드 상세 보여주기
		$('.feedImage').click(function(e) {
			e.preventDefault();
			var feed_no = $(this).data('no');			
			//ajax get 으로 feed 상세 정보 얻어오기
			$.getJSON("/feed/"+ feed_no, function(data) {			
				printModal(data);
				printReply(data.feed_no);
				$('#feedModal').modal();
			});
		});
		
		//피드 출력
		var printModal = function(feed) {
			var str = '';
				str	+= "<div class='modal-dialog'><div class='modal-content'><div class='modal-header'>"
					+"<button type='button' class='close' data-dismiss='modal' aria-hidden='true'>×</button><h4 class='modal-title'>"                                                
		        	+"<a href='/accounts/detail?user_id=" + feed.user_id_fk + "' class='pull-left profile_img'>"
		        	+"<img class='img-circle img-responsive' src='" ;
		        	
		        if( '${memberVO.profile_pic}'.length == 0) {
		        	str += "/resources/bootstrap/images/default_profile.png'>"
		        } else {	        	
		        	str += "/displayImage?type=p&fileName=${memberVO.profile_pic}'>";
		        }	
		        	
		        str +="</a><a href='/accounts/detail?user_id="+ feed.user_id_fk +"'>"
		        	+"<strong>" + feed.user_id_fk + "</strong></a><br/><small class='text-muted'>"+ feed.displayTime +"</small></h4></div>"	        	
		        	+"<div class='modal-body'> <img class='img-responsive pull-left gallery_product' src='/displayImage?fileName=" + feed.file_name +"&type=f'><br/>"
		        	+ "<br /><h4 align='center'>" + feed.content + "</h4><hr><div class='comment'></div></div>"
		        	+ "<div class='modal-footer'><div class='input-group'> <input type='text' class='form-control' id='replytext' placeholder='comment'>"
		        	+ "<span class='input-group-btn'><button type='button' class='btn btn-primary' id='replyAddBtn'>ADD</button></span>"
		        	+ "</div></div></div></div>";
		        	
		    $('#feedModal').empty();
			$('#feedModal').append(str);
		}		
		
		//댓글 출력
		var printReply = function(feed_no) {	
			$.getJSON("/replies/all/"+feed_no , function(data) {
				var str = '';
				for(var i in data) {
					var feed_no = data[i].feed_no_fk;
					var user_id = data[i].user_id_fk
					var rno = data[i].rno;
					var replytext = data[i].replytext;
					
					str += "<li class='pull-left' id='rno"+ rno
					+ "'> <a href='/accounts/detail?user_id=" + user_id +"'>"
					+ user_id +"</a>&nbsp;&nbsp;&nbsp;&nbsp;"		
					+ replytext;
					if(user_id == '${login.user_id}') {				
						str += "&nbsp;&nbsp;&nbsp;&nbsp;" 
							+ "<button type='button' class='btn btn-primary btn-xs'"
							+ "onclick='modifyReply(" + feed_no + "," + rno + "," + replytext + ")'>Modify</button>" 
							+ "&nbsp;&nbsp;<button type='button' class='btn btn-danger btn-xs replyDelBtn'" //삭제 버튼
							+ "onclick='removeReply(" + rno +");'>Remove</button>";
					}
					str += "</li><br/><br/>"
				}
				$('.comment').empty();
				$('.comment').append(str);
			});
		}
			
		//댓글 등록
		$('#replyAddBtn').click(function() {
			//event.preventDefault();
			alert('클릭');
			var feed_no_fk = $('#feed_no').val();
			var user_id_fk = '${login.user_id}';
			var replytext = $('#replyText').val();
			if(replytext.length == 0)
				return;
			
			$.ajax({
				type : 'post',
				url : '/replies/',
				headers : {
					"Content-Type" : "application/json",
					"X-HTTP-Method-Override" : "POST"
				},
				dataType : 'text',
				data : JSON.stringify({
					feed_no_fk : feed_no_fk,
					user_id_fk : user_id_fk,
					replytext : replytext
				}),
				success : function(result) { //성공시
					console.log("result: " + result);
					if (result == 'SUCCESS') {
						printReply(feed_no_fk); //댓글보여주기						
						$('#replyText' + idx).val('');
					}
				}
			});
			
		});
				
		//댓글 삭제 
		function removeReply(rno) {
			var feed_no = $('#feed_no').val();
			
			$.ajax({
				type : 'delete',
				url : '/replies/'+ rno,
				headers : {
					"Content-Type" : "application/json",
					"X-HTTP-Method-Override" : "DELETE"
				},
				dataType : 'text',			
				success : function(result) { //성공시
					if (result == 'SUCCESS') 
						printReply(feed_no);				
				}});
		}
	});
</script>

<%@ include file="/WEB-INF/views/include/footer.jsp" %>



