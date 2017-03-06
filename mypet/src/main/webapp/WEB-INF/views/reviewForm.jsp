<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="include/header.jsp" %>

<style>
.span4 img{
    margin-right: 10px;
}
.span4 .img-left{
    float: left;
}
.span4 .img-right{
    float: right;
}
.fileDrop {
  width: 80%;
  height: 100px;
  border: 1px dotted gray;
  background-color: lightslategrey;
  margin: auto;  
}
</style>

<div class="container">
	<button class="btn btn-primary btn-lg" data-toggle="modal" data-target="#myModal">
		리뷰 등록
	</button>
	        
	<div class="modal fade" id="myModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<!-- 폼 -->
			<form role="form">			
			<input type="hidden" name="review_image" id="review_image">  
				<div class="modal-content">
					<!-- 상단(타이틀 + x) -->
					<div class="form-group">
						<div class="modal-header">
							<button type="button" class="close" data-dismiss="modal">
								<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
							</button>	
							<h2 class="modal-title" id="myModalLabel">Regist Review</h2>
						</div><!-- .상단(타이틀 + x) 끝 -->
					</div>														
					<!-- 내용 -->
					<div class="form-group">
						<div class="modal-body">					
							<div class="block">
								<div class="row">
									<!-- 이미지 드랍 -->
									<div class="fileDrop" style="display:block;">&nbsp;<Strong>Drag and drop</Strong> image here</div>										
								</div>
								<div class="row">								
									<div class="span4" id="uploadedPic">
										<!-- 드랍 이미지 뷰 (466 x 320 px) -->
										<img class="img-left" id="viewImage"> 
									</div>	
								</div>
								<br/><br/>
								<div class="row">																				
										<!-- 제목 인풋 -->										
										<div class="row">
											<div class="col-sm-4  col-lg-4">																													
											<input type="text" class="form-control" name="" placeholder="TITLE">
											</div>
										</div>
										<div class="row">																												
											<div class="col-md-9">
												<!-- content -->
	                							<textarea class="form-control" id="content" name="content" placeholder="Content" rows="8"></textarea>
	              							</div>
              							</div>										
									</div>
								</div>
								<br />
							</div>
							<div class="social-comment">
						</div> <!-- .내용 끝 -->						
					</div>
					<!-- 하단 버튼 -->
					<div class="form-group">					
						<div class="modal-footer">
							<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
							<button type="submit" class="btn btn-primary" id="registerForm">Regist</button>
						</div> <!-- .하단 버튼 끝 -->
					</div>
				</div> <!-- 모달 콘텐츠 -->
			</form>			
		</div> <!-- 모달 다이얼로그 -->		
	</div> <!-- 모달 전체 윈도우 -->        
</div>

<script>

//드래그진입,오버 이벤트 제거
$(".fileDrop").on("dragenter dragover", function(event){
	event.preventDefault();
});

//파일 드랍 -> 서버로 전송 -> 썸네일 생성 -> 현재 페이지에서 뷰   
$(".fileDrop").on("drop", function(event){
	event.preventDefault();	
	var files = event.originalEvent.dataTransfer.files;	
	var file = files[0];
	
	var formData = new FormData();
	formData.append("file", file);
	
	$.ajax({
		  url: '/reviews/uploadPic',
		  data: formData,
		  dataType:'text',
		  processData: false,
		  contentType: false,
		  type: 'POST',
		  success: function(data){
			 if(data=='notMatchedTypes') {
				 
			 } else {
				 str = "/displayImage?type=r&fileName="+data;
				 alert(str);
				 //str="<img class='img-left' src='/displayImage?type=r&fileName="+data+"'/>"
				 //	+ "<small id='file_name' data-src="+data+">X</small>";				 	
				 $('#review_image').attr('value',data); //file_name 속성	
				 $('.fileDrop').css('display','none'); // 드랍창 숨기기	
				 //$('#uploadedPic').append(str); // 썸네일 이미지 보이기
				 $('#viewImage').attr('src',str);				 
			}
		  }
	});	
});	

//파일 이미지 삭제 
$('.uploadedPic').on('click','small',function(event) {
	var that = $(this);	
	$.ajax({
		url: '/reviews/deleteImage',
		type: 'post',
		data: {fileName:$(this).attr("data-src")},
		dataType: 'text',
		success:function(result) {
			if(result == 'deleted') {				
				$('#file_name').attr('value',''); //file_name ''으로 바꾸기
				$('.fileDrop').css('display','block'); //드랍창 보이기
				$('#viewImage').attr('src','');
			}
		}		
	});	
});

//등록 submit
$('#registerForm').submit(function(event) {
	event.preventDefault();	
	$(this).get(0).submit();
});

	
</script>

<%@ include file="include/footer.jsp" %>

