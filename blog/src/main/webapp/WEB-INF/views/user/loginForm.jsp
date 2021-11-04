<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%@ include file="../layout/header.jsp"%>
<div class="container">
	<!-- 로그인 폼 -->
	<form action="/auth/loginProc" method="post">
		<div class="form-group">
			<label for="username">Username</label>
			<input type="text"  name="username" class="form-control" placeholder="Enter username" id="username">
		</div>
		
		<div class="form-group">
			<label for="password">Password</label>
			<input type="password" name="password" class="form-control" placeholder="Enter password" id="password">
		</div>
		<!-- Remember check Box
		<div class="form-group form-check">
			<label class="form-check-label">
			<input name="remember" class="form-check-input" type="checkbox"> Remember me
			</label>
		</div> 
		-->
		<button id="btn-login" class="btn btn-primary">로그인</button>
		<a href="https://kauth.kakao.com/oauth/authorize?client_id=fc74244df9199821608aa59f9bfe298d&redirect_uri=http://localhost:8000/auth/kakao/callback&response_type=code"><img height="38px" src="/image/kakao_login_button.png"/></a>
	</form>
	

</div>
<%@ include file="../layout/footer.jsp"%>

