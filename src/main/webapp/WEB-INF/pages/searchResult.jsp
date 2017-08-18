<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style>
table, th, td {
	border: 1px solid black;
}


.textBoxContent {
	
	background: white;
	padding: 15px;
	width: 100%;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<h1>
		<font color=#0080ff>S</font><font color=#ff0000>E</font><font
			color=#ff9900>A</font><font color=#0080ff>R</font><font color=#339933>C</font><font
			color=#ff0000>H</font> <font color=#0080ff>M</font><font
			color=#ff0000>E</font> Results
	</h1>
	<form action="searchWords.htm" method="get">
		<div class="textBoxContent">
			<br> <input name="searchWord" type="text" size="35" required />
			<button type="submit">Search</button><br></br>
		</div>
		
	</form>

	<b>Number of Documents containing result:</b><cout>${fn:length(foundResults)}</cout><br>	
	<table style="width: 100%">
		<tr>
			<th><b>File Path:</b></th>
			<th><b>Score:</b></th>
			<th><b>Highlighted Text:</b></th>
			</br>
		</tr>
		<c:forEach var="data" items="${foundResults}">
			

			<c:forEach var="highlight" items="${data.searchHighlightTextResult}">
				<tr>
				</tr>
				<tr>
					<td><cout>${data.path}</cout><br></td>
					<td><cout>${data.docIndexScore}</cout><br></td>
					<td><cout>${highlight}</cout><br> <br></td>
				</tr>
			</c:forEach>
		</c:forEach>
	</table>
</body>
</html>