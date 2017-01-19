<!DOCTYPE html>
<html>
	<head>
	    <title>图标选择</title>
	    <#include "../../include.ftl">
	</head>
	<body scroll="yes" onload="init()">
	    <script>
	  	 function init(){
	  	 	var data = ${icons};
			for(var i in data){
				listIcon(data[i])
			}
	  	}
	  	function listIcon(data){
		  	$('<img>',{
	        		src:'${webRoot}/statics/base/icons/'+data,
	        		name:data.replace('.png',''),
	        		height:20,
	        		wdith:20,
	        		style:'margin:5px;',
	        		click:function(){  
	        		parent.testExcel = this.name;
	        		parent.window.frames["menuEditFrame"].$("#_img")[0].src = "${webRoot}/statics/base/icons/"+this.name+".png";
	        		parent.closeDialog("iconFrame");
        	}  
			    }).appendTo('body');
		  	}
	    </script>
	</body>
</html>