/**
 * 格式化数据字典
 * @param key 配置的父级configKey
 * @param value 当前的configValue
 * @returns
 */
var storage = null;
var flag = null;
function format(key, value) {
	if (flag != key) {
		$.ajax({
			type : "post",
			url : window.webRoot + "/config/format?configKey=" + key,
			async : false,
			dataType : "json",
			success : function(data) {
				storage = data;
				flag = key;
			}
		});
	}
	
	for ( var i in storage) {
		if (storage[i].configValue == value) {
			return storage[i].name;
		}
	}
}
