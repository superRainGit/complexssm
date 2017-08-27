<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>员工信息</title>
<% 
	pageContext.setAttribute("APP_PATH", request.getContextPath());
	Object APP_PATH = pageContext.getAttribute("APP_PATH");
%>
<!--  
	web路径:
		不以/开头的相对路径 是以当前资源路径为基准[是当前浏览器URL的路径] 经常容易出问题  
		以/开头的相对路径  是以服务器路径为基准[即localhost:8080/为基准]  此时想要访问资源就需要加上项目名字
			一般项目名是不能写死的  所以使用 pageContext对象去获取
-->
<script type="text/javascript" src="${APP_PATH }/static/javascript/jquery-1.9.1.min.js"></script>
<link href="${APP_PATH }/static/bootstrap-3.3.7-dist/css/bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="${APP_PATH }/static/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>
</head>
<body>
	<!-- 员工修改的模态框  -->
	<div class="modal fade" id="editEmpModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
				  <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				  <h4 class="modal-title" id="myModalLabel">修改员工</h4>
				</div>
				<div class="modal-body">
				  	<form class="form-horizontal">
				  		<div class="form-group">
				  			<input type="hidden" name="_method" value="PUT" />
				  			<input type="hidden" name="empId" id="empId" />
						</div>
						<div class="form-group">
							<label for="empName" class="col-sm-2 control-label">EmpName</label>
							<div class="col-sm-10">
								<p id="empEditName" class="form-control-static"></p>
							</div>
						</div>
						<div class="form-group">
							<label for="email" class="col-sm-2 control-label">Email</label>
							<div class="col-sm-10">
							  	<input type="text" name="email" class="form-control" id="emailEdit" placeholder="zhangsan@163.com">
							  	<span class="help-block"></span>
							</div>
						</div>
						<div class="form-group">
							<label for="gender" class="col-sm-2 control-label">Gender</label>
							<div class="col-sm-10">
							  	<label class="radio-inline">
								  	<input type="radio" name="gender" id="genderEdit1" value="M"> 男
								</label>
								<label class="radio-inline">
								  	<input type="radio" name="gender" id="genderEdit2" value="F"> 女
								</label>
							</div>
						</div>
						<div class="form-group">
							<label for="gender" class="col-sm-2 control-label">DeptName</label>
							<div class="col-sm-3">
								<select class="form-control" name="dId" id="dEditId"></select>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
				  <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				  <button type="button" class="btn btn-primary" id="empEditBtn">修改</button>
				</div>
			</div>
		</div>
	</div>
	<!-- 员工添加的模态框  -->
	<div class="modal fade" id="addEmpModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
				  <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
				  <h4 class="modal-title" id="myModalLabel">添加员工</h4>
				</div>
				<div class="modal-body">
				  	<form class="form-horizontal">
						<div class="form-group">
							<label for="empName" class="col-sm-2 control-label">EmpName</label>
							<div class="col-sm-10">
								<input type="text" name="empName" class="form-control" id="empName" placeholder="zhangsan">
								<span class="help-block"></span>
							</div>
						</div>
						<div class="form-group">
							<label for="email" class="col-sm-2 control-label">Email</label>
							<div class="col-sm-10">
							  	<input type="text" name="email" class="form-control" id="email" placeholder="zhangsan@163.com">
							  	<span class="help-block"></span>
							</div>
						</div>
						<div class="form-group">
							<label for="gender" class="col-sm-2 control-label">Gender</label>
							<div class="col-sm-10">
							  	<label class="radio-inline">
								  	<input type="radio" name="gender" id="gender1" value="M" checked> 男
								</label>
								<label class="radio-inline">
								  	<input type="radio" name="gender" id="gender2" value="F"> 女
								</label>
							</div>
						</div>
						<div class="form-group">
							<label for="gender" class="col-sm-2 control-label">DeptName</label>
							<div class="col-sm-3">
								<select class="form-control" name="dId" id="dId"></select>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
				  <button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
				  <button type="button" class="btn btn-primary" id="empSaveBtn">提交</button>
				</div>
			</div>
		</div>
	</div>
	<!-- 显示整个页面数据的总页面 -->
	<div class="container">
		<div class="row">
			<h1 class="col-md-12">员工信息</h1>
		</div>
		<div class="row">
			<div class="col-md-2 col-md-offset-10">
				<button class="btn btn-primary btn-sm" id="addEmpBtn">
					<span class="glyphicon glyphicon-plus-sign"></span>
					<fmt:message key="appmsg.add"></fmt:message>
				</button>
				<button class="btn btn-danger btn-sm" id="delEmpBtn">
					<span class="glyphicon glyphicon-trash"></span>
					<fmt:message key="appmsg.delete"></fmt:message>
				</button>
			</div>
		</div>
		<div class="row">
			<div class="col-md-2 col-md-offset-8" id="page_info">
			</div>
			<div class="col-md-1"><h5>每页大小</h5></div>
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
							<th><input type="checkbox" id="checkAll" /></th>
							<th><fmt:message key="appmsg.empId"></fmt:message></th>
							<th><fmt:message key="appmsg.empName"></fmt:message></th>
							<th><fmt:message key="appmsg.empGender"></fmt:message></th>
							<th><fmt:message key="appmsg.empEmail"></fmt:message></th>
							<th><fmt:message key="appmsg.empDeptName"></fmt:message></th>
							<th><fmt:message key="appmsg.operation"></fmt:message></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>
		<div class="row">
			<div class="col-md-8 col-md-offset-3" id="page_nav">
			</div>	
		</div>
	</div>
</body>
<script type="text/javascript">
var $$_APP_PATH_$$ = '<%= APP_PATH%>';
</script>
<script type="text/javascript" src="${APP_PATH }/static/javascript/mod.js"></script>
</html>