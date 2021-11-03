let index = {
	init: function() {
		//jQuery
		$("#btn-save").on("click", () => { // function() {}, ()=> {} this를 바인딩 하기 위해 사용
			this.save();
		});
		
			$("#btn-update").on("click", () => { // function() {}, ()=> {} this를 바인딩 하기 위해 사용
			this.update();
		});
		
		// 기존 로그인 방식 사용x
		/*$("#btn-login").on("click", () => { // function() {}, ()=> {} this를 바인딩 하기 위해 사용
			this.login();
		});*/
	},
	save: function() {
		//alert('user의 save함수 호출됨');
		//회원가입 정보를 id값으로 찾은 후 변수에 바인딩하여 js의 data에 저장
		let data = {
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val()
		};

		//console.log(data);

		// ajax 호출시 default가 비동기 호출
		// ajax 통신을 이용하여 3개의 데이터를 json으로 변경하여 insert 요청
		// ajax가 통신을 성공하고 서버가 json을 리턴해주면 자동으로 자바 오브젝트로 변환가능
		$.ajax({
			// 회원가입 수행 요청
			type: "POST",
			url: "/auth/joinProc",
			data: JSON.stringify(data), // (http body 데이터) JS의 데이터를 JAVA에게 보낼 때 JSON 문자열을 사용하여 보내야 호환가능
			contentType: "application/json; charset=utf-8", // http body데이터가 어떤 타입인지(MIME)
			dataType: "json" // 요청을 서버로 해서 응답이 왔을 때 생긴것이  Json이라면 java 오브젝트로 변경
		}).done(function() { // 정상일때
			alert("회원가입이 완료되었습니다.");
			//onsole.log(resp);
			location.href = "/";
		}).fail(function(error) { // 실패일때
			alert(JSON.stringify(error));
		});
	},
	
	update: function() {
		let data = {
			id: $("#id").val(),
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val()
		};

		$.ajax({
			type: "PUT",
			url: "/user",
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8", 
			dataType: "json"
		}).done(function() {
			alert("회원 수정이 완료되었습니다.");
			location.href = "/";
		}).fail(function(error) {
			alert(JSON.stringify(error));
		});
	},
	
}

index.init();

	
	// 기존 로그인 방식 사용x
	/*login: function() {
		//alert('user의 save함수 호출됨');
		//회원가입 정보를 id값으로 찾은 후 변수에 바인딩하여 js의 data에 저장
		let data = {
			username: $("#username").val(),
			password: $("#password").val(),
		};

		$.ajax({
			type: "POST", // GET 방식을 사용시 주소에 로그인정보가 남게됨
			url: "/api/user/login",
			data: JSON.stringify(data), // (http body 데이터) JS의 데이터를 JAVA에게 보낼 때 JSON 문자열을 사용하여 보내야 호환가능
			contentType: "application/json; charset=utf-8", // http body데이터가 어떤 타입인지(MIME)
			dataType: "json" // 요청을 서버로 해서 응답이 왔을 때 생긴것이  Json이라면 java 오브젝트로 변경
		}).done(function() { // 정상일때
			alert("로그인이 완료되었습니다.");
			//onsole.log(resp);
			location.href = "/";
		}).fail(function(error) { // 실패일때
			alert(JSON.stringify(error));
		});
	}*/
