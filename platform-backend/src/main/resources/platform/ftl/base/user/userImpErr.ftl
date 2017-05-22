<!DOCTYPE html>
<html>
	<head>
	    <title>失败详细信息</title>
	    <#include "../../include.ftl">
	    <script type="text/javascript" src="${webRoot}/statics/base/js/DateUtil.js"></script>
	</head>
	<style type="text/css">
		*{
			margin:0px;
			padding:0px;
		}
		.tablelist{ 
			width:100%; 
			border-left:1px solid #DFE0E5; 
			border-top:1px solid #DFE0E5; 
			font-size:12px; color:#333
		}
		.tablelist td{
			border-right:1px solid #DFE0E5;
			border-bottom:1px solid #DFE0E5;
			background:#fff;
		 }
		 
		 #scroll{
		 	border:none;
		 }
	</style>
	<body scroll="yes">
	<div id="p" class="easyui-panel"    
        style="padding:10px;background:#fafafa;"   
        data-options="closable:true,collapsible:true,minimizable:true,maximizable:true,fit:true">   
   	<p id = "contentData"></p> 
	</div>  
	<script>
	$(document).ready(
		function(){
			var temp = "${data}".split(',');
			for(var i = 0; i < temp.length ; i++){
				$('#contentData').append(temp[i]);
				$('#contentData').append("<br>");
			}
		}
	)
	</script>
	</body>
</html>