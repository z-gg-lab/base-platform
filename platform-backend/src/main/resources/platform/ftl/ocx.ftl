<OBJECT classid="clsid:0d4d4239-f71f-47b9-82c5-258e8258b25b"  width="0" height="0" align="center"  id="Ts" HSPACE="0" VSPACE="0">
</OBJECT>
<script type="text/javascript" src="${webRoot}/statics/base/js/IdCardsUtils.js"></script>
<script type ="text/javascript">
 function checkOcxVersion(flag){
 			var widgetVersions;
 			var url;
 			var width;
			  if(flag ==0){
			  	//已注册
			 	  widgetVersions = getAllOcxVersions();
			 	  url = "${webRoot}/widgetmanager/check"
			 	  width = 500;
			  }else{
			  	//未注册
			  	  widgetVersions ='';
			  	  url = "${webRoot}/widgetmanager/widgetCheck";
			  	  width = 620;
			  }
			   	$.ajax({
			    	type:"post",
			    	url:"${webRoot}/widgetmanager/versionCheck",
			    	data:{
			    		"widgetVersions":widgetVersions
			   		},
			   		async:false,
			    	error:function(request) {
			    	},
			  		success:function(data) {
			    		if("true" == data){
			    				parent.$("#dialog").dialog({
					        	title:'控件下载',
					        	content:createFrame(url),
								modal:true,
								width:width,
    							height:200,
    							top:($(window).height()-340)*0.5,
    							left:($(window).width()-420)*0.5
						});
				        parent.$("#dialog").dialog("open");
			    		}	 
			   		}
		 		});
		}
		
		//创建视图
			function createFrame(url,ocxId) {
             var s = '<iframe name="widgetTable" scrolling="no" frameborder="0"  src="'+url+'" style="width:100%;height:98%;"></iframe>';
				return s;
			}
		</script>