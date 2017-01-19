function exportToExcel(id,exportExcel){
	var rows = $("#"+id).datagrid('getRows');	
	var fields = $("#"+id).datagrid('getColumnFields');
	
	var data = [];
	var titles = new Array();
	
	// 获取每一行的内容
	for(var i in rows){
		for(var j in fields){
			data.push(rows[i][fields[j]]);
		}
	}
	// 获取表头
	for(var i in fields){
		titles.push($("#"+id).datagrid('getColumnOption',fields[i]).title);
	}
	
	var url = window.webRoot+"/component/excelImportTemplate/exportExcel?titles="+titles+"&rows="+data;
	$("#"+exportExcel).attr("href",url); 
	
//  	$.ajax({
//   		type: "post",
//        url:window.webRoot+"/excelImportTemplate/exportExcel?titles="+titles+"&rows="+data,
//        async: false,
//        dataType: "json",
//        success: function (data) {
//
//        }
//    });
}