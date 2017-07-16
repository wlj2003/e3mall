<html>
<head>
	<meta charset="utf-8" />
	<title>freemarker测试</title>
</head>
<body>
	学生信息：<br>
	学号：${student.id}<br>
	姓名：${student.name}<br>
	年龄：${student.age}<br>
	家庭住址：${student.address}<br>
	学生列表<br>
	<table border="1">
		<tr>
			<th>下标</th>
			<th>学号</th>
			<th>姓名</th>
			<th>年龄</th>
			<th>家庭住址</th>
		</tr>
		<#list stuList as stu>
		<#if stu_index % 2 == 0>
		<tr bgcolor="red">
		<#else>
		<tr bgcolor="blue">
		</#if>
			<td>${stu_index}</td>
			<td>${stu.id}</td>
			<td>${stu.name}</td>
			<td>${stu.age}</td>
			<td>${stu.address}</td>
		</tr>
		</#list>
	</table>
	<br>
	当前日期：${date?date}<br>
	当前时间：${date?time}<br>
	当前日期和时间：${date?datetime}<br>
	自定义日期显示格式：${date?string("yyyy-MM-dd HH:mm:ss")}
	<br>
	null值的处理：${val!"val的值为null"}<br>
	null值的处理2：${val!}<br>
	null值的判断 ：<br>
	<#if val??>
		val的值不为null
	<#else>
		val的值是null
	</#if>
	<br>
	include测试：<#include "hello.ftl">
</body>
</html>