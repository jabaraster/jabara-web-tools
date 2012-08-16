<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Index.</title>
  <style>
    form {
      margin: 0.5em;
      border: solid 1px black;
    }
  </style>
</head>

<body>

  <fieldset>
    <legeng>画像ファイルリサイズ</legeng>
    <form action="${pageContext.request.contextPath}/rest/image/resized" method="post" enctype="multipart/form-data">
      <ul>
        <li>画像ファイル：<input type="file" name="image" /></li>
        <li>リサイズ後の幅：<input type="text" name="width" /></li>
        <li>リサイズ後の高さ：<input type="text" name="height" /></li>
        <li> <input type="radio" name="format" value="PNG" checked/>png <input type="radio" name="format" value="JPEG" disabled/>jpeg  </li>
        <li><input type="submit" /></li>
      </ul>
    </form>
  </fieldset>

  <ul>
    <li><a href="${pageContext.request.contextPath}/blackout_ext.jsp">計画停電スケジュール処理画面へ</a></li>
  </ul>

</body>

</html>
