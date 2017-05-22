/**
  *身份证读取
  *json: {"status":"ok","address":"湖北十堰....","birthday":"19930919","office":"XXX公安局","":"startTime":"20120102","endTime":"20120112","name":"XXXX","minority":"汉","sex":"男","photo":""}
  *
  */

/**
 * 初始化
 */
 
var Tocx = document.getElementById("Ts");;
 

/**
 * 获取身份证信息
 */
function getIdCards(){
	var json = "";
	var statusDes = "";
	var idCards = Tocx.ReadUserID().replace(/\s+/g,"").split("|");
	if(idCards[0] == '-2'){
		statusDes ="链接设备未放卡或放错卡！";
		json = '{"status":'+"\""+idCards[0]+"\""+',"statusDes":'+"\""+statusDes+"\""+'}';
	//}else if(idCards[0] == '-11'){
		//statusDes ="无卡";
	//	json = '{"status":'+"\""+idCards[0]+"\""+',"statusDes":'+"\""+statusDes+"\""+'}';
	}else if(idCards[0] == '-11'){
		statusDes ="未连接设备！";
		json = '{"status":'+"\""+idCards[0]+"\""+',"statusDes":'+"\""+statusDes+"\""+'}';
	}else if(idCards[0] == '00'){
			json = '{"status":'+"\""+idCards[0]+"\""+',"name":'+"\""+idCards[1]+"\""+',"sex":'+"\""+idCards[2]+"\""+',"nation":'+"\""+idCards[3]+"\""+',"birthday":'+"\""+idCards[4]+"\""+',"address":'+"\""+idCards[5]+"\""+',"idCard":'+"\""+idCards[6]+"\""+',"office":'+"\""+idCards[7]+"\""+',"startTime":'+"\""+idCards[8]+"\""+',"endTime":'+"\""+idCards[9]+"\""+',"photo":'+"\""+idCards[10]+"\""+'}';
	}else{
    	statusDes ="其它错误！("+idCards[0]+")";
    	json = '{"status":'+"\""+idCards[0]+"\""+',"statusDes":'+"\""+statusDes+"\""+'}';
    } 
	return json;
}

/**
 * 获取ic卡的信息
 */ 	
function getIcCards(){
	var json = "";
	var statusDes = "";
	var icCards = Tocx.CardUserID().replace(/\s+/g,"").split("|");
	if(icCards[0] == '-2'){
		statusDes ="链接设备未放卡或放错卡！";
		json = '{"status":'+"\""+icCards[0]+"\""+',"statusDes":'+"\""+statusDes+"\""+'}';
	//}else if(icCards[0] == '-2'){
	//	statusDes ="无卡！";
	//	json = '{"status":'+"\""+icCards[0]+"\""+',"statusDes":'+"\""+statusDes+"\""+'}';
	}else if(icCards[0] == '-11'){
		statusDes ="未连接设备！";
		json = '{"status":'+"\""+icCards[0]+"\""+',"statusDes":'+"\""+statusDes+"\""+'}';
	}else if(icCards[0] == "00"){
    	json = '{"status":'+"\""+icCards[0]+"\""+',"idCode":'+"\""+icCards[1]+"\""+',"cardNo":'+"\""+icCards[2]+"\""+',"card":'+"\""+icCards[3]+"\""+'}';
    }else{
    	statusDes ="其它错误！("+icCards[0]+")";
    	json = '{"status":'+"\""+icCards[0]+"\""+',"statusDes":'+"\""+statusDes+"\""+'}';
    } 
	return json;
}

/**
 * sam卡信息
 * 
 */
function getSamCards(){
	var json = "";
	var statusDes = "";
	var samCards = Tocx.TerminalInfo().replace(/\s+/g,"").split("|");
	var terminalNo = Tocx.GetReaderSN().replace(/\s+/g,"").split("|")[1];
	if(samCards[0] == '-11'){
		statusDes ="未连接设备！";
		json = '{"status":'+"\""+samCards[0]+"\""+',"statusDes":'+"\""+statusDes+"\""+'}';
	}else if(samCards[0] == '-4'){
		statusDes ="未插入sam卡或sam卡插入错误卡座！";
		json = '{"status":'+"\""+samCards[0]+"\""+',"statusDes":'+"\""+statusDes+"\""+'}';
	//}else if(samCards[0] == '-11'){
		//statusDes ="读卡器链接错误";
		//json = '{"status":'+"\""+samCards[0]+"\""+',"statusDes":'+"\""+statusDes+"\""+'}';
	}else if(samCards[0] == "00"){
		//00|000000|3B6D00000081544C0086840807230005C0|01|01|0018|12345600000000000000000000|420100000001|359136323430380137313339|  
		//状态字（00成功，否则失败）|发行机构标识|SAM卡序列号(ATR)|版本号|密钥类型|FCI自定义数据|SAM卡编号|终端机编号|
		json = '{"status":'+"\""+samCards[0]+"\" "+',"issuerNo":'+"\""+samCards[1]+"\""+',"samNo":'+"\""+samCards[2]+"\""+',"samVersion":'+"\""+samCards[3]+"\""+',"secretType":'+"\""+samCards[4]+"\""+',"fciData":'+"\""+samCards[5]+"\""+',"samNum":'+"\""+samCards[6]+"\""+',"terminalNo":'+"\""+terminalNo+"\""+'}';
	}else{
    	statusDes ="其它错误！("+samCards[0]+")";
    	json = '{"status":'+"\""+samCards[0]+"\" "+',"statusDes":'+"\""+statusDes+"\""+'}';
    } 
	return json;
}

/**
 * 密码键盘返回密码
 * @param soundType
 * @param mode
 * @param time
 * @param type 1表示只有提示音
 * @returns {String}
 */
function getPassWord(soundType,mode,time,type){
	var json = "";
	var statusDes = "";
	var deviceState = openDevice();
	if(deviceState != '00'){
		statusDes ="打开读卡器失败！";
    	json = '{"status":'+"\""+deviceState+"\" "+',"statusDes":'+"\""+statusDes+"\""+'}';
	}
	if(type ==1){
		return getRetSoundTip(soundType,time);
	}else{
		var retPswd = Tocx.DeviceSoundKey(soundType, mode,time).replace(/\s+/g,"").split("|");
		if(retPswd[0] == '00'){
			json = '{"status":'+"\""+retPswd[0]+"\" "+',"password":'+"\""+retPswd[1]+"\""+'}';
		}else if(retPswd[0] == '-2'){
			statusDes ="长时间未输入密码或取消输入密码！";
	    	json = '{"status":'+"\""+retPswd[0]+"\" "+',"statusDes":'+"\""+statusDes+"\""+'}';
		}else{
	    	statusDes ="其它错误！("+retPswd[0]+")";
	    	json = '{"status":'+"\""+retPswd[0]+"\" "+',"statusDes":'+"\""+statusDes+"\""+'}';
	    } 
	}
	return json;
}

/**
 * 打开设备
 */
function openDevice(){
	return Tocx.DeviceOpen();
}

function getDeviceVersion(){
	//00|V1.0_6903
	var json = "";
	var statusDes = "";
	var ocxInfos = Tocx.CTDCardPersonVer().replace(/\s+/g,"").split("|");
    if(ocxInfos[0] =='00'){
		json = '{"status":'+"\""+ocxInfos[0]+"\" "+',"ocxVersion":'+"\""+ocxInfos[1]+'}';
	}else{
		statusDes ="其它错误！("+ocxInfos[0]+")";
    	json = '{"status":'+"\""+ocxInfos[0]+"\" "+',"statusDes":'+"\""+statusDes+"\""+'}';
	}
	return json;
}

function getOcxVersion(){
	var json = "";
	var statusDes = "";
	var ocxType ="";
	var ocxInfos = Tocx.CTDCardPersonVer().replace(/\s+/g,"").split("|");
	
    if(ocxInfos[0] =='00'){
    	ocxType= 1;
		json = '{"status":'+"\""+ocxInfos[0]+"\" "+',"ocxVersion":'+"\""+ocxInfos[1]+"\""+',"ocxType":'+"\""+ocxType+"\""+'}';
	}else{
		statusDes ="其它错误！("+ocxInfos[0]+")";
    	json = '{"status":'+"\""+ocxInfos[0]+"\" "+',"statusDes":'+"\""+statusDes+"\""+'}';
	}
	return json;
}

/**
 * 获取所有的ocx控件的版本
 */
function getAllOcxVersions(){
	var ocxVersions='';

	var ocxInfos = JSON.parse(getOcxVersion());
	if(ocxInfos.status != 0){
	 	parent.$.messager.alert('提示', ocxInfos.statusDes,'info');
	}else{
	   ocxVersions = ocxInfos.ocxVersion+",101"+";";
	}
	return ocxVersions;
}

/** 
 * 返回提示音
 * @param soundType
 * @param mode
 */
function getRetSoundTip(soundType,mode){
	 return Tocx.DeviceSound(soundType, mode);
}

/**
 * 
 */
function getLodopVersion(){
	try{ 
	     var LODOP=getLodop(); 
		if (LODOP.VERSION) {
			 if (LODOP.CVERSION)
			 alert("当前有C-Lodop云打印可用!\n C-Lodop版本:"+LODOP.CVERSION+"(内含Lodop"+LODOP.VERSION+")"); 
			 else
			 alert("本机已成功安装了Lodop控件！\n 版本号:"+LODOP.VERSION); 

		};
	 }catch(err){ 
	 } 
}

function checkOcx(){
    var flag;
	if(document.all.Ts.object == null) { 
		flag  = 1;
		
	}else{
		flag = 0;
	}
	return flag;
}


 