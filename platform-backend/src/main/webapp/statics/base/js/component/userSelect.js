/**
 * userSelect(option,callback)
 *option属性，通过json传输,示例： json:{"isSingle":"false","excludeUserIds":"400XXX,400XXX","includeUserIds":"400XXX,400XXX",
 *									"ecludeType":"1001","includeType":"1002","isMerchant":"true"}
 *属性值名称：isSingle  		属性值类型：String    描述：是否多选，当为true时，禁止多选，默认为false
 *属性值名称：excludeUserIds   属性值类型：String    描述：当不为空时，显示用户为剔除传入值之后的用户 	 
 *属性值名称：includeUserIds   属性值类型：String    描述：当不为空时，显示用户仅为该传入值的用户
 *属性值名称：excludeType  	属性值类型：String    描述：当不为空时，显示用户为剔除传入类型之外的用户	
 *属性值名称：includeType  	属性值类型：String    描述：当不为空时，显示用户仅为该传入类型的用户
 *属性值名称：isMerchant		属性值类型：String    描述：是否显示商户用户，当为true时，显示商户用户，默认为true
 *callback回调函数，对控件返回的所选人员进行处理
 */

function createFrame4Component(url,userIds,type,singleSelect,methodOfUser,methodOfType) {
	var s = '<iframe id="userSelectFrame" name="userSelectFrame" scrolling="no" frameborder="0"  src="'+url+'" style="width:100%;height:98%;"></iframe>';
	return s;
}

function userSelect(options,callback ) {
	var type = null;
	var userIds = null;
	var isSingle = false;
	var methodOfType = null;
	var methodOfUser = null;
	var isMerchant = null;
	var excludeUserIds = null;
	var includeUserIds = null;
	//为isSingle赋值
	if(options.isSingle == "true"){
		isSingle = true;
	}
	//当includeType有值时，includeType同时也有值则报错
	if(options.excludeType != null && options.includeType != null){
		$.messager.alert('警告','Type传入值有问题','info');
		return false;
	}
	if(options.includeType != null){
		type = options.includeType;
		methodOfType = "true";
	}else{
		type = options.excludeType;
		methodOfType = "false";
	}
	//当includeUserIds有，excludeUserIds同时也有值时，userIds为其求集之后集合
	if(options.excludeUserIds != null && options.includeUserIds != null){
		excludeUserIds = options.excludeUserIds;
		includeUserIds = options.includeUserIds
		var tmp = new Array();
		tmp = excludeUserIds.split(",");
		for(var i = 0; i < tmp.length; i++){
			if(tmp[i]!="") {
				var replaceTmp = tmp[i] + ",";
				includeUserIds = includeUserIds.replace(replaceTmp,"");
				includeUserIds = includeUserIds.replace(tmp[i],"");
			}
		}
	}
	if(options.includeUserIds != null){
		methodOfUser = "true";
		userIds = includeUserIds;
	}else{
		methodOfUser = "false";
		userIds = options.excludeUserIds;
	}
	//判断是否显示商户用户
	if(options.isMerchant == "false"){
		isMerchant = "false";
	}else{
		isMerchant = "true";
	}
	//生成url
	var urlForUser = window.webRoot+"/component/userselect/userSelect?userIds="+userIds+"&type="+type+"&isSingle="+isSingle+"&methodOfUser="+methodOfUser+"&methodOfType="+methodOfType+"&isMerchant="+isMerchant;
	var s = createFrame4Component(urlForUser,userIds,type,isSingle,methodOfUser,methodOfType);
    parent.$("#userSelect4Component").dialog({
        title: "人员选择",
        content: s,
        modal: true,
        width: 750,
    	height:525,
		top : $(window).width()*0.2,
		left : $(window).height()*0.2,
        toolbar:[{
			text:'确定',
			iconCls:'icon-ok',
			handler:function(){
				callback(parent.window.frames["userSelectFrame"].backIds,parent.window.frames["userSelectFrame"].userArray);
				parent.$("#userSelect4Component").dialog("close");
			}
		},{
			text:'取消',
			iconCls:'icon-no',
			handler:function(){
				parent.$("#userSelect4Component").dialog("close");
			}
		}],
    });
    parent.$("#userSelect4Component").dialog('open');
}
