<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<html>
    <link href="resources/css/chatStyles.css" rel="stylesheet"
          type="text/css" />
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Chat</title>
    </head>
    <body>
        <div id="chatContainer">
            <div class="header">
                <div class="headerMessage">
                    <sec:authorize access="isAuthenticated()">
                        <sec:authentication property="principal.username" var="username"/>
                        <c:out value="Hello, ${username}"/>
                        <a class ="sign_out" href="<c:url value="/logout"/>">(sign out)</a>
                    </sec:authorize>                    
                </div>
            </div>
            <textarea class="chatArea" readonly></textarea>
            <div class="sendMessageBlock">
                <sec:authorize access="isAuthenticated()">
                    <div class="sendMessageForm" >
                        <textarea class="messageArea" name="message" maxlength="300"></textarea>
                        <button id="sendMessage" type="submit">Send</button>
                    </div>
                </sec:authorize>
                <sec:authorize access="not isAuthenticated()">
                    <label class="textInfo">
                        You aren't logged in. Please 
                        <a href="SignIn">Sign In</a>
                        or
                        <a href="Register">Create account</a>
                    </label>
                </sec:authorize>
            </div>
        </div>	
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
        <script>
            $(document).ready(function () {

                update();
                function update() {
                    $.ajax({
                        url: "getMessages",
                        type: 'POST',
                        dataType: 'json',
                        success: function (data) {
                            var result = "";
                            for (var i in data) {
                                result += data[i].time + "  " + data[i].author.nickname + ": " + data[i].content + "\n";
                            }
                            $(".chatArea").text(result);
                        }
                    });
                }
                setInterval(update, 2000);

                $('#sendMessage').click(function () {
                    var message = $(".messageArea").val();
                    $.ajax({
                        url: "sendMessage",
                        type: 'POST',
                        data: {
                            message: message
                        },
                        dataType: 'json',
                        success: function (data) {
                            if (data === "success") {
                                $(".messageArea").val("");
                                $(".messageArea").removeAttr("placeholder");
                            } else {
                                $(".messageArea").attr("placeholder", data);
                            }
                        }
                    });
                });

            });
        </script>
    </body>
</html>