<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>

<div class="container">
 <!-- 다른사용자가 수정경로로 접근할 때 수정하는 것을 막음 -->
	<c:choose>
		<c:when test="${board.user.id != principal.user.id}">
			<h1>잘못된 경로입니다.</h1>
		</c:when>
		<c:otherwise>
			<form>
				<input type="hidden" id="id" value="${board.id}" />
				<div class="form-group">
					<label for="title">Title</label> <input value="${board.title}" type="text" class="form-control" placeholder="Enter title" id="title">
				</div>

				<div class="form-group">
					<label for="content">Content</label>
					<textarea class="form-control summernote" rows="5" id="content">${board.content}</textarea>
				</div>

			</form>
		</c:otherwise>
	</c:choose>

	<c:if test="${board.user.id == principal.user.id}">
		<button id="btn-update" class="btn btn-primary">수정하기</button>
	</c:if>
</div>

<script>
	$('.summernote').summernote({
		tabsize : 2,
		height : 300
	});
</script>

<script src="/js/board.js"></script>
<%@ include file="../layout/footer.jsp"%>