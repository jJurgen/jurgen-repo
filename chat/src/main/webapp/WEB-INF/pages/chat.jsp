<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
                    <c:choose>
                        <c:when test="${not empty currentUser}">                        
                            <c:out value="Hello, ${currentUser.nickname}"></c:out>
                                <a class ="sign_out" href="signOut">(sign out)</a>
                        </c:when>
                        <c:otherwise>

                        </c:otherwise>
                    </c:choose>
                </div>
            </div>

            <textarea class="chatArea" readonly></textarea>

            <div class="sendMessageBlock">
                <c:choose>
                    <c:when test="${not empty currentUser}">
                        <div class="sendMessageForm" >
                            <textarea class="messageArea" name="message" maxlength="300"></textarea>
                            <button id="sendMessage" type="submit">Send</button>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <label class="textInfo">
                            You aren't logged in. Please 
                            <a href="SignIn">Sign In</a>
                            or
                            <a href="Register">Create account</a>
                        </label>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>	
        <input  id="testInput" type="text" lang="30"> 
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
        <script>
            $(document).ready(function () {
                
               // $("#testInput").prop('value','123');
                $("#testInput").attr('value','123');
                update();                
                function update() {
                    $.ajax({
                        url: "getMessages",
                        type: 'POST',
                        dataType: 'json',
                        success: function (data) {
                            var result = "";
                            for (var i in data) {
                                result += data[i].time + ":" + data[i].author + ": " + data[i].message + "\n";
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
                           $(".messageArea").val("");
                        }
                    });
                });

            });
        </script>
    </body>
</html>