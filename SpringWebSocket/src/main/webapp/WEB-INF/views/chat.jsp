<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<script src="https://code.jquery.com/jquery-2.1.3.min.js"></script>
<link rel="stylesheet" href="/bootstrap/css/bootstrap.min.css" />
<link rel="stylesheet" href="/bootstrap/css/chat.css" />
<script src="/bootstrap/js/bootstrap.min.js" ></script>
<title>CHAT</title>
<script type="text/javascript">

	var ws;
	
	function connect() {
		try{
			if( $("#pwd").val() == "9845" || $("#pwd").val() == "1234" ){
				ws = new WebSocket('ws://222.107.254.93:8080/echo.do?userid=' + $("#userid").val());
			    ws.onopen = function (event) {
			        console.log('websocket opened');
			        console.log(event);
			    };
			    ws.onmessage = function (message) {
			    	var jsonObj = JSON.parse(message.data);
			    	
			    	$('#messages').append(jsonObj.html);
			    	document.getElementById('bodyArea').scrollTop = document.getElementById('bodyArea').scrollHeight;

			    	// 데스크탑 알림 요청
			    	var options = {
		                body: jsonObj.msg
		            }
		            var notification = new Notification("메시지가 도착했습니다.", options);
		            setTimeout(function () {
		                notification.close();
		            }, 5000);
		            
				};
				ws.onclose = function(event) {
					console.log('websocket closed');
					console.log(event);
				};
				ws.onerror = function(event) {
					console.log(event);
					console.log(event.data);
	            };
				
				Notification.requestPermission(function (result) {
			        if (result === 'denied') {
			        	// 요청 거절
			        	alert("알림을 허용해야 합니다.\n크롬 > 설정 > 고급 설정 표시 > 콘텐츠 설정 > 알림 > 예외관리 > [http://222.107.254.93:8080] > 허용");
			            return;
			        } else {
			        	//요청 허용
			            return;
			        }
			    });
				
			} else {
				alert("invalid password");
				location.reload();
			}
		}catch(e){
			console.log(e);
		}
	}

	function disconnect() {
		if (ws) {
			ws.close();
			ws = null;
		}
	}

	$(function() {
		var bodyArea = $(document).height() - ($("#headArea").height() + $("#footerArea").height()) - 75;
		$("#bodyArea").height(bodyArea);

		$('#btn-chat').click(function() {
			if ($("#message").val() != "") {
				ws.send($("#message").val());
				$("#message").val('');
			}
		});

		$('#btn-connect').click(function() {
			if ($("#userid").val() != "") {
				connect();
				$('#myModal').modal('hide');
			}
		});

		$('#message').keydown(event, function() {
			if (event.keyCode === 13) {
				ws.send($(this).val());
				$(this).val('');
			}
		});

		$("#myModal").modal();
	});
</script>
</head>
<body>

	<div class="container" style="height: 100%;">
		
		<div class="row" style="height: 100%;">
			<div class="panel panel-primary" style="margin-bottom: 0px;">
				<div class="panel-heading" id="headArea">
					<span class="glyphicon glyphicon-comment"></span> Chat
				</div>
				<div class="panel-body" id="bodyArea">
					<ul class="chat" id="messages"></ul>
				</div>
				<div class="panel-footer" id="footerArea">
					<div class="input-group">
						<input id="message" type="text" class="form-control input-sm" placeholder="Type your message here...">
						<span class="input-group-btn">
							<button class="btn btn-warning btn-sm" id="btn-chat">Send</button>
						</span>
					</div>
				</div>
			</div>
		</div>

		<div class="modal fade" id="myModal" role="dialog" data-backdrop="static" data-keyboard="false">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-body text-center">
						<p class="text-left"><input type="text" id="userid" name="userid" class="form-control" placeholder="Username" maxlength="8" /></p>
						<p class="text-left"><input type="password" id="pwd" name="pwd" class="form-control" placeholder="Password" maxlength="8" /></p>
						<p><button type="button" id="btn-connect" class="btn btn-success" style="width:100%;">CONNECT</button></p>
					</div>
				</div>
			</div>
		</div>
		
	</div>

</body>
</html>