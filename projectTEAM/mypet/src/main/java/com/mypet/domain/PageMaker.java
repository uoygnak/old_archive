package com.mypet.domain;

import org.springframework.web.util.UriComponentsBuilder;

public class PageMaker {
	//variables
		private int totalCount;
		private int startPage;
		private int endPage;
		private boolean prev;
		private boolean next;	
		private int displayPageNum = 10;	
		private Criteria cri;
		
		//constructor
		public PageMaker() {
			//System.out.println("PageMaker()생성");
		}
		//setter
		public void setCri(Criteria cri) {
			this.cri = cri;
		}	
		public void setTotalCount(int totalCount) {
			this.totalCount = totalCount;
			calcData();
		}	
		private void calcData() {
			endPage = (int) (Math.ceil(cri.getPage() / (double)displayPageNum) * displayPageNum);
			startPage = (endPage - displayPageNum) +1;
			
			int tempEndPage = (int) (Math.ceil(totalCount / (double) cri.getPerPageNum()));
			if(endPage > tempEndPage)
				endPage = tempEndPage;
			
			prev = startPage==1?false:true;
			next = endPage * cri.getPerPageNum() >=totalCount? false:true;
		}	
		//기존 페이징 처리
		public String makeQuery(int page) {
			return	UriComponentsBuilder.newInstance()
						.queryParam("page", page)
						.queryParam("perPageNum", cri.getPerPageNum())
						.build()
						.toUriString();
		}	
		//페이징 처리 + 검색
		public String makeSearch(int page) {
			return UriComponentsBuilder.newInstance()
					.queryParam("page",page)
					.queryParam("perPageNum",cri.getPerPageNum())
					.queryParam("searchType",((SearchCriteria)cri).getSearchType())
					.queryParam("keyword",((SearchCriteria)cri).getKeyword())
					.build()
					.toUriString();
		}
		//상품 페이지 처리
				public String makeProductSearch(int page) {
					return UriComponentsBuilder.newInstance()
							.queryParam("page",page)
							.queryParam("perPageNum",cri.getPerPageNum())
							.queryParam("searchType",((SearchCriteria)cri).getSearchType())
							.queryParam("keyword",((SearchCriteria)cri).getKeyword())
							.queryParam("cateType", ((ProductSearchCriteria)cri).getCateType())
							.queryParam("ConditionType", ((ProductSearchCriteria)cri).getConditionType())
							.build()
							.toUriString();
				}

		//others getter,setter
		public int getStartPage() {
			return startPage;
		}

		public void setStartPage(int startPage) {
			this.startPage = startPage;
		}

		public int getEndPage() {
			return endPage;
		}

		public void setEndPage(int endPage) {
			this.endPage = endPage;
		}

		public boolean isPrev() {
			return prev;
		}

		public void setPrev(boolean prev) {
			this.prev = prev;
		}

		public boolean isNext() {
			return next;
		}

		public void setNext(boolean next) {
			this.next = next;
		}

		public int getDisplayPageNum() {
			return displayPageNum;
		}

		public void setDisplayPageNum(int displayPageNum) {
			this.displayPageNum = displayPageNum;
		}

		public int getTotalCount() {
			return totalCount;
		}

		public Criteria getCri() {
			return cri;
		}
}
