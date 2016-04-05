<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <link rel="icon" type="image/png" href="img/favicon.ico">

    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>

    <link rel="stylesheet"
          href="css/index.css">

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">

    <!-- Optional theme -->
    <link rel="stylesheet"
          href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap-theme.min.css">

    <!-- Latest compiled and minified JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

    <script src="https://cdnjs.cloudflare.com/ajax/libs/parsley.js/2.1.2/parsley.min.js"></script>

    <title>Survey Results</title>
</head>

<nav class="navbar navbar-default">
    <div class="container">
        <div class="navbar-header">
            <a class="navbar-brand" href="/">Survey Results</a>
        </div>
        <div>
            <ul class="nav navbar-nav">
                <li>
                    <a href="/">
                        Index
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>
<div class="container">
    <c:forEach var="survey"  items="${surveys}" >
        <h1>Results for survey: <c:out value="${survey.id}"/></h1>

                <ul class="list-unstyled">
                    <li>
                        <c:forEach var="question" items="${survey.questions}" >
                            <div class="panel panel-default">
                                <div class="panel-heading">
                                    Question: ${question.body}
                                </div>
                                <div class="panel-body">
                                    <ol class="list-group">
                                        <li class="list-group-item">Answer type: ${question.type}</li>
                                        <c:choose>
                                            <c:when test='${question.type == "voice" && question.answer.startsWith("http") }' >
                                                <audio controls>
                                                    <source src="${question.formatedAnswer}" type="audio/mpeg"/>
                                                    Your browser does not support the audio tag.
                                                </audio>
                                            </c:when>
                                            <c:otherwise>
                                                <li class="list-group-item">Answer: ${question.formatedAnswer}</li>
                                            </c:otherwise>
                                        </c:choose>
                                    </ol>
                                </div>
                            </div>
                        </c:forEach>
                    </li>
                </ul>

    </c:forEach>
</div>
</html>