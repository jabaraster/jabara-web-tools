<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE HTML>

<html>
  <head>
    	<meta charset="UTF-8">
    	<title>Upload</title>
  </head>
  <body>
  
    <form action="${pageContext.request.contextPath}/upload" method="POST" enctype="multipart/form-data">
      <ul>
        <li><input type="text" name="userName" /></li>
        <li><input type="password" name="password" /></li>
        <li><input type="file" name="file" /></li>
        <li><input type="submit" /></li>
      </ul>    
    </form>
  
  </body>
</html> 
