<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="layout/header.jsp"%>
<div class="container">

	<!-- 요청정보가 넘어올때 JSTL의 EL 표현식: boards를 받을 수 있음 -->
	<c:forEach var="board" items="${boards.content}">
		<!-- profile card -->
		<div class="card m-2">
			<!--  <img class="card-img-top" src="img_avatar1.png" alt="Card image"> -->
			<div class="card-body">
				<h4 class="card-title">${board.title}</h4>
				<!-- board.getTitle() 이 호출되는것과 같은 원리 -->
				<a href="#" class="btn btn-primary">See Post</a>
			</div>
		</div>
	</c:forEach>


	<ul class="pagination justify-content-center">
		<!-- 이전 페이지 -->
		<c:choose>
			<c:when test="${boards.first}">
				<li class="page-item disabled"><a class="page-link" href="?page=${boards.number-1 }">Previous</a></li>
			</c:when>
			<c:otherwise>
				<li class="page-item"><a class="page-link" href="?page=${boards.number-1 }">Previous</a></li>
			</c:otherwise>
		</c:choose>

		<!-- 페이지 번호 -->
		<c:forEach var="i" begin="1" end="${boards.totalPages}">
			<li class="page-item"><a class="page-link" href="?page=${i-1}">${i}</a></li>
		</c:forEach>

		<!--  다음 페이지 -->
		<c:choose>
			<c:when test="${boards.last}">
				<li class="page-item disabled"><a class="page-link" href="?page=${boards.number+1 }">Next</a></li>
			</c:when>
			<c:otherwise>
				<li class="page-item"><a class="page-link" href="?page=${boards.number+1 }">Next</a></li>
			</c:otherwise>
		</c:choose>

	</ul>
</div>

<%@ include file="layout/footer.jsp"%>


<!-- <!-- profile card
	<div class="card m-2">
		 <img class="card-img-top" src="img_avatar1.png" alt="Card image">
		<div class="card-body">
			<h4 class="card-title">Title</h4>
			<a href="#" class="btn btn-primary">See Post</a>
		</div>
	</div>

	profile card
	<div class="card m-2">
		 <img class="card-img-top" src="img_avatar1.png" alt="Card image">
		<div class="card-body">
			<h4 class="card-title">Title</h4>
			<a href="#" class="btn btn-primary">See Post</a>
		</div>
	</div>  -->



