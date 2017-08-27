<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>员工信息</title>
<% 
	pageContext.setAttribute("APP_PATH", request.getContextPath());
%>
<!--  
	web路径:
		不以/开头的相对路径 是以当前资源路径为基准[是当前浏览器URL的路径] 经常容易出问题  
		以/开头的相对路径  是以服务器路径为基准[即localhost:8080/为基准]  此时想要访问资源就需要加上项目名字
			一般项目名是不能写死的  所以使用 pageContext对象去获取
-->
<script type="text/javascript" src="${APP_PATH }/static/javascript/jquery-1.9.1.min.js"></script>
<link href="${APP_PATH }/static/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
<script type="${APP_PATH }/text/javascript" src="static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
<script type="text/javascript">
	$(function() {
		
		$(".first").click(function() {
			return false;
		});
		
	});
</script>
</head>
<body>
	<div class="container">
		<div class="row">
			<h1 class="col-md-12">员工信息</h1>
		</div>
		<div class="row">
			<div class="col-md-2 col-md-offset-10">
				<button class="btn btn-primary btn-sm">
					<span class="glyphicon glyphicon-plus-sign" aria-hidden="true"></span>
					<fmt:message key="appmsg.add"></fmt:message>
				</button>
				<button class="btn btn-danger btn-sm">
					<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
					<fmt:message key="appmsg.delete"></fmt:message>
				</button>
			</div>
		</div>
		<div class="row">
			<div class="col-md-2 col-md-offset-9">
				<h4>总记录数 <kbd>${requestScope.pageInfo.total }</kbd></h4>
			</div>
			<div class="col-md-1">
				<select class="form-control input-sm" id="pageSize">
					<option id="0" value="5">5</option>
					<option id="1" value="10">10</option>
					<option id="2" value="20">20</option>
				</select>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12">
				<table class="table table-hover">
					<thead>
						<tr>
							<th><fmt:message key="appmsg.empId"></fmt:message></th>
							<th><fmt:message key="appmsg.empName"></fmt:message></th>
							<th><fmt:message key="appmsg.empGender"></fmt:message></th>
							<th><fmt:message key="appmsg.empEmail"></fmt:message></th>
							<th><fmt:message key="appmsg.empDeptName"></fmt:message></th>
							<th><fmt:message key="appmsg.operation"></fmt:message></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${requestScope.pageInfo.list }" var="emp">
							<tr>
								<td>${emp.empId }</td>
								<td>${emp.empName }</td>
								<td>${emp.gender == 'M' ? '男' : '女' }</td>
								<td>${emp.email }</td>
								<td>${emp.department.deptName }</td>
								<td>
									<button class="btn btn-primary btn-sm">
										<span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
										<fmt:message key="appmsg.edit"></fmt:message>
									</button>																
									<button class="btn btn-danger btn-sm">
										<span class="glyphicon glyphicon-trash" aria-hidden="true"></span>
										<fmt:message key="appmsg.delete"></fmt:message>
									</button>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-8 col-md-offset-3">
				<nav aria-label="Page navigation">
					<ul class="pagination pagination-sm">
						<c:if test="${requestScope.pageInfo.pageNum != 1}">
							<li>
								<a href="${APP_PATH }/emps?pageNum=1"><fmt:message key="appmsg.firstPage"></fmt:message></a>
							</li>
						</c:if>
						<c:if test="${requestScope.pageInfo.hasPreviousPage }">
							<li>
								<a href="${APP_PATH }/emps?pageNum=${requestScope.pageInfo.pageNum - 1 }" aria-label="Previous">
								  <span aria-hidden="true">&laquo;</span>
								</a>
							</li>
						</c:if>
						<c:forEach items="${requestScope.pageInfo.navigatepageNums }" var="num">
							<li <c:if test="${requestScope.pageInfo.pageNum == num }">class="active"</c:if>>
								<a href="${APP_PATH }/emps?pageNum=${num }">${num }</a>
							</li>
						</c:forEach>
						<c:if test="${requestScope.pageInfo.hasNextPage }">
							<li>
								<a href="${APP_PATH }/emps?pageNum=${requestScope.pageInfo.pageNum + 1 }" aria-label="Next">
								  <span aria-hidden="true">&raquo;</span>
								</a>
							</li>
						</c:if>
						<c:if test="${requestScope.pageInfo.pages != requestScope.pageInfo.pageNum }">
							<li>
								<a href="${APP_PATH }/emps?pageNum=${requestScope.pageInfo.pages }"><fmt:message key="appmsg.lastPage"></fmt:message></a>
							</li>
						</c:if>
					</ul>
				</nav>
			</div>	
		</div>
	</div>
</body>
</html>