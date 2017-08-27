// 定义一个变量用于分页最大页码的保存
var TotalPageNum = null;
var CurrentPageNum = null;
var CurrentPageSize = 5;

$(function() {
	to_page(1, CurrentPageSize);
});
	
var to_page = function(pageNum, pageSize) {
	$.ajax({
		url : $$_APP_PATH_$$+"/emps",
		data : "pageNum=" + pageNum + "&pageSize=" + pageSize,
		type : "POST",
		success : function(success){
			// 构建用户信息
			build_emps_table(success);
			// 构建分页数据
			build_page_info(success);
			// 构建分页条
			buil_page_nav(success);
		}
	});
}
	
var buil_page_nav = function(result) {
	$("#page_nav").empty();
	var ul = $("<ul></ul>").addClass("pagination pagination-sm");
	var firstLi = $("<li></li>").append($("<a></a>").append("首页").attr("href", "#"));
	var lastLi = $("<li></li>").append($("<a></a>").append("尾页").attr("href", "#"));
	var previousLi = $("<li></li>").append($("<a></a>").attr("href", "#").append($("<span></span>").append("&laquo;")));
	var nextLi = $("<li></li>").append($("<a></a>").attr("href", "#").append($("<span></span>").append("&raquo;")));
	if(result.extendMap.pageInfo.hasPreviousPage == false) {
		firstLi.addClass("disabled");
		previousLi.addClass("disabled");
	} else {
		// 当上一页和首页的按钮可以点击的时候 才给它加上对应的点击事件
		// 这样可以避免当页面上效果的显示是不可点击的状态但是请求仍然发出去的漏洞
		firstLi.click(function() {
			to_page(1, CurrentPageSize);
		});
		previousLi.click(function() {
			to_page(result.extendMap.pageInfo.pageNum - 1, CurrentPageSize);
		});
	}
	if(result.extendMap.pageInfo.hasNextPage == false) {
		lastLi.addClass("disabled");
		nextLi.addClass("disabled");
	} else {
		// 同上面的上一页和首页的逻辑处理
		lastLi.click(function() {
			to_page(result.extendMap.pageInfo.pages, CurrentPageSize);
		});
		nextLi.click(function() {
			to_page(result.extendMap.pageInfo.pageNum + 1, CurrentPageSize);
		});
	}
	ul.append(firstLi).append(previousLi);
	var pageNavs = result.extendMap.pageInfo.navigatepageNums;
	$.each(pageNavs, function(index, item) {
		var tempLi = $("<li></li>").append($("<a></a>").append(item).attr("href", "#"));
		if(item == result.extendMap.pageInfo.pageNum) {
			tempLi.addClass("active");
		}
		tempLi.click(function() {
			to_page(item, CurrentPageSize);
		});
		ul.append(tempLi);
	});
	ul.append(nextLi).append(lastLi);
	$("<nav></nav>").append(ul).appendTo($("#page_nav"));
	// 将分页的数据放入到全局变量中进行后面数据的使用
	TotalPageNum = result.extendMap.pageInfo.pages;
	CurrentPageNum = result.extendMap.pageInfo.pageNum;
}

var build_page_info = function(result) {
	$("#page_info").empty();
	$("#page_info").append($("<h4></h4>").append("总记录数").append($("<kbd></kbd>").append(result.extendMap.pageInfo.total)));
}

var build_emps_table = function(result){
	$("table tbody").empty();
	var emps = result.extendMap.pageInfo.list;
	$.each(emps,function(index,item){
		var checkBoxTd = $("<td></td>").append("<input type='checkbox' class='checkItem' />");
		var empIdTd = $("<td></td>").append(item.empId);
		var empNameTd = $("<td></td>").append(item.empName);
		var genderTd = $("<td></td>").append(item.gender == 'M' ? "男" : "女");
		var emailTd = $("<td></td>").append(item.email);
		var deptNameTd = $("<td></td>").append(item.department.deptName);
		var editBtn = $("<button></button>").attr("id", "EditBtn").addClass("btn btn-primary btn-sm")
					.append($("<span></span>").addClass("glyphicon glyphicon-pencil")).attr("btnId", item.empId)
					.append("编辑");
		var delBtn =  $("<button></button>").attr("id", "DelBtn").addClass("btn btn-danger btn-sm")
					.append($("<span></span>").addClass("glyphicon glyphicon-trash")).attr("btnId", item.empId)
					.append("删除");
		var btnTd = $("<td></td>").append(editBtn).append(" ").append(delBtn);
		// append方法执行完成以后还是返回原来的元素 所以可以链式添加新的数据
		$("<tr></tr>").append(checkBoxTd)
			.append(empIdTd)
			.append(empNameTd)
			.append(genderTd)
			.append(emailTd)
			.append(deptNameTd)
			.append(btnTd)
			.appendTo($("table tbody"));
	});
}

// 为编辑按钮添加响应事件
// 直接向按钮绑定事件是无效的  因为整个页面是通过Ajax请求响应过来的
// 此时页面还没有进行对应的按钮 因此事件是绑定不上的
// 因此解决的方法是
// 			: 在使用ajax请求进行之后的成功的回调中进行事件的绑定
// 			: 使用jquery提供的live事件[但是1.7以后不再支持此方法], 
// 				改用 on(event, selector, function() {}) 为某个节点的所有子节点进行响应事件的绑定
$(document).on("click", "#EditBtn", function() {
	// 获取对应的员工的信息
	// 获取部门信息 显示到模态框中
	$.ajax({
		url : $$_APP_PATH_$$+"/depts",
		data : "",
		type : "POST",
		success : function(success) {
			build_dept_name("#dEditId" ,success);
		}
	});
	// 显示员工信息
	$.ajax({
		url : $$_APP_PATH_$$ + "/emp/" + $(this).attr("btnId"),
		type : "GET",
		success : function(success) {
			if(success.code == 200) {
				return;
			} else {
				var emp = success.extendMap.emp;
				$("#empEditName").text(emp.empName);
				$("#emailEdit").val(emp.email);
				$("#editEmpModal input[name=gender]").val([emp.gender]);
				$("#editEmpModal select").val([emp.dId]);
				$("#empId").val(emp.empId);
			}
		}
	});
	$('#editEmpModal').modal({
		backdrop : "static"
	});
})

$(document).on("click", "#DelBtn", function() {
	var empName = $(this).parents("tr").find("td:eq(2)").text();
	if(confirm("确定要删除[" + empName + "]吗?")) {
		// 发送ajax请求进行数据的清除
		var empId = $("#DelBtn").attr("btnId");
		$.ajax({
			url : $$_APP_PATH_$$ + "/emp/" + empId,
			type : "POST",
			data : "_method=DELETE",
			success : function(success) {
				// 成功的回调
				if(success.code == 100) {
					to_page(CurrentPageNum, CurrentPageSize);
				} else {
					alert("删除失败");
				}
			}
		});
	}
});

$("#empEditBtn").click(function() {
	var empId = $("#editEmpModal form").find("input[name=empId]").val();
	$.ajax({
		url : $$_APP_PATH_$$ + "/emp/" + empId,
		data : $("#editEmpModal form").serialize(),
		type : "POST",
		success : function(success) {
			if(success.code == 100) {
				alert("更新成功");
			} else {
				alert("更新失败");
			}
			$("#editEmpModal").modal('hide');
			to_page(CurrentPageNum, CurrentPageSize);
		}
	});
});

// 为添加按钮添加点击事件
$("#addEmpBtn").click(function() {
	transToBlank();
	// 发送Ajax请求  将部门信息请求回来
	$.ajax({
		url : $$_APP_PATH_$$+"/depts",
		data : "",
		type : "POST",
		success : function(success) {
			build_dept_name("#dId" ,success);
		}
	});
	$('#addEmpModal').modal({
		backdrop : "static"
	});
});

// 填充部门信息
var build_dept_name = function(selector, result) {
	$(selector).empty();
	var selectObj = $(selector);
	var depts = result.extendMap.depts;
	$.each(depts, function(index, item) {
		var option = $("<option></option>").attr("id", item.deptId).attr("value", item.deptId).append(item.deptName);
		selectObj.append(option);
	});
}

$("#empSaveBtn").click(function() {
	// 数据校验
	if($(this).attr("ajax-validate_name") == "false" || $(this).attr("ajax-validate_email") == "false"){
		return;
	}
	$.ajax({
		url : $$_APP_PATH_$$ + "/emp",
		data : $("#addEmpModal form").serialize(),
		type : "POST",
		success : function(success) {
			// 判断返回的结果是失败还是成功
			if(success.code == 100) {
				// 将模态框进行隐藏
				$("#addEmpModal").modal('hide');
				// 发送请求到最后一页进行分页数据的获取
				// 因为PageHelper插件会对超出页码的数据进行矫正
				// 如果因为某些原因获取不到分页最大页码的数据  可以自行设置一个非常大的数字代表页数
				to_page(TotalPageNum == null ? 99999 : TotalPageNum + 1, CurrentPageSize);
			} else {
				var errorMap = success.extendMap.errorMsg;
				if(undefined != errorMap.email) {
					// 显示邮箱错误信息
					show_validate_msg("#email", "error", errorMap.email);
				} else {
					show_validate_msg("#email", "success", "");
				}
				if(undefined != errorMap.empName) {
					// 显示用户名错误信息
					show_validate_msg("#empName", "error", errorMap.empName);
				} else {
					show_validate_msg("#empName", "success", "");
				}
			}
		}
	});
});

var show_validate_msg = function(ele, status, msg) {
	// 在进行数据校验结构显示之前进行数据的清空
	$(ele).parent().removeClass("has-success has-error");
	$(ele).next("span").text("");
	if("success" == status) {
		$(ele).parent().addClass("has-success");
		$(ele).next("span").text(msg);
	} else if("error" == status) {
		$(ele).parent().addClass("has-error");
		$(ele).next("span").text(msg);
	}
}

$("#email").blur(function() {
	var email = $("#email").val();
	var regEmail = /^([a-z0-9_\.-]+)@([\da-z\.-]+)\.([a-z\.]{2,6})$/;
	if(!regEmail.test(email)) {
		show_validate_msg("#email", "error", "邮箱的格式不正确");
		$("#empSaveBtn").attr("ajax-validate_email", false);
	} else {
		show_validate_msg("#email", "success", "");
		$("#empSaveBtn").attr("ajax-validate_email", true);
	}
});

$("#empName").blur(function() {
	var empName = $("#empName").val();
	var regName = /(^[\w_-]{6,12}$)|(^[\u2E80-\u9FFF]{2,5})/;
	if(!regName.test(empName)) {
		// BootStrap对错误信息的提示  直接在 输入框 对应的父元素上加上对应的 css样式
		show_validate_msg("#empName", "error", "用户名可以是2-5个中文字符或者6-12个英文字符和数字 _ -的组合");
		return;
	} else {
		show_validate_msg("#empName", "success", "");
	}
	$.ajax({
		url : $$_APP_PATH_$$ + "/emp",
		data : "empName=" + empName,
		type : "get",
		success : function(success) {
			if(success.code == 200) {
				show_validate_msg("#empName", "error", "该用户名已经存在");
				$("#empSaveBtn").attr("ajax-validate_name", false);
			} else if(success.code == 100) {
				show_validate_msg("#empName", "success", "");
				$("#empSaveBtn").attr("ajax-validate_name", true);
			}
		}
	});
});

var transToBlank = function() {
	// 在发送数据之前  进行表单信息的清空
	$("#addEmpModal form")[0].reset();
	// 将之前的错误信息进行重置
	$("#addEmpModal form span").text("");
	// 将之前的样式进行重置
	$("#addEmpModal form").find("*").removeClass("has-success has-error");
}

$("#checkAll").click(function() {
	// 此处有一个需要说明的地方: 使用attr获取对应的属性值的时候
	// 如果是dom原生的属性  那么当标签上没有显式对应属性的时候  返回的值永远是 undefined
	// 因此推荐使用 prop 获取dom原生的属性; 使用attr 获取自定义的属性
	var flag = $("#checkAll").prop("checked");
	$(".checkItem").prop("checked", flag);
});

$(document).on("click", ".checkItem", function() {
	var flag = $(".checkItem:checked").length == $(".checkItem").length;
	$("#checkAll").prop("checked", flag);
});

$("#delEmpBtn").click(function() {
	var empNames = "";
	var empIds = "";
	$.each($(".checkItem:checked"), function() {
		empNames += $(this).parents("tr").find("td:eq(2)").text() + ",";
		empIds += $(this).parents("tr").find("td:eq(1)").text() + ",";
	})
	if(empNames == "") {
		alert("请至少选中一个进行删除...");
		return;
	}
	empNames = empNames.substring(0, empNames.length - 1);
	empIds = empIds.substring(0, empIds.length - 1);
	if(confirm("你确定要删除[" + empNames + "]吗?")) {
		// 发送ajax请求删除
		$.ajax({
			url : $$_APP_PATH_$$ + "/emp/" + empIds,
			data : "_method=DELETE",
			type : "POST",
			success : function(success) {
				alert(success.msg);
				to_page(CurrentPageNum, CurrentPageSize);
			}
		});
	} else {
		$(".checkItem:checked").prop("checked", false);
	}
});

// 对每页大小的控制
$("#pageSize").change(function() {
	var pageSize = $(this).val();
	CurrentPageSize = pageSize;
	to_page(CurrentPageNum, pageSize);
});

//$("#toChina").click(function() {
//	$.ajax({
//		url : $$_APP_PATH_$$+"/emps",
//		data : "pageNum=" + CurrentPageNum + "&pageSize=" + CurrentPageSize + "&locale=zh_CN",
//		type : "POST",
//		success : function(success){
//			// 构建用户信息
//			build_emps_table(success);
//			// 构建分页数据
//			build_page_info(success);
//			// 构建分页条
//			buil_page_nav(success);
//		}
//	});
//});
//
//$("#toEnglish").click(function() {
//	$.ajax({
//		url : $$_APP_PATH_$$+"/emps",
//		data : "pageNum=" + CurrentPageNum + "&pageSize=" + CurrentPageSize + "&locale=en_US",
//		type : "POST",
//		success : function(success){
//			// 构建用户信息
//			build_emps_table(success);
//			// 构建分页数据
//			build_page_info(success);
//			// 构建分页条
//			buil_page_nav(success);
//		}
//	});
//});
