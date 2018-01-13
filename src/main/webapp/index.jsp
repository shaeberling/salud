<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%@ page import="com.s13g.salud.SubmitValueServlet" %>
<%@ page import="com.s13g.salud.SubmitValueServlet.ExerciseEntry" %>
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
  <link rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons">
  <link rel="stylesheet" href="https://code.getmdl.io/1.3.0/material.indigo-pink.min.css">
  <script defer src="https://code.getmdl.io/1.3.0/material.min.js"></script>
  <title>Salud (by @saschah)</title>
</head>
<body>
  Salud here. Enter activity...
  <c:forEach items="${SubmitValueServlet.getEntries()}" var="entry">
    <form action="/submit" method="post">
      <div class="mdl-textfield mdl-js-textfield">
        <input name="${entry.paramName}" class="mdl-textfield__input" type="text" pattern="-?[0-9]*(\.[0-9]+)?">
        <label class="mdl-textfield__label" for="sample2">${entry.label}</label>
        <span class="mdl-textfield__error">Input is not a number! (calories)</span>
      </div>
    </form>
  </c:forEach>
</body>
</html>
