;(function ($) {
    var _root_data = "json/data.json";
    var _root_search = "json/search.json";
    
    var _default = {
        format: "list", // 列表或者树 list, tree
        title: "选择", // 标题
        multi: true, // 是否多选
        initItems: [], // 初始选中的数据 {name:'xx', id:'xx'}
        webRoot: window.webRoot,
        apiPath: "/user/data/choose", // 数据接口
        onConfirm: null // 确认事件
    };
    var uichoose = {};
    uichoose.openChooseDialog = function (config) {
    	var _templateOpt = $.extend(true, {}, _default);
        var opt = $.extend(true, _templateOpt, config);
        
        var _root = opt.webRoot || "";
        if(opt.apiPath.indexOf('?') > 0) {
        	_root_data = _root + opt.apiPath + "&format=" + opt.format;
        }else{
        	_root_data = _root + opt.apiPath + "?format=" + opt.format;
        }
        _root_search = _root_data + "&keyword=";
        
        _initDialogDom(function(){
        	_initChooseLeft()
        	_initChooseRight();
        	_initSearchBox();
        	_initEvents();
        	$('#ui-choose-dlg').dialog('open');
        });
        
        function _initDialogDom(callback) {
        	var html = '<div id="ui-choose-dlg" closed="true" class="easyui-dialog" title="' + opt.title + '" style="width:500px;height:400px;padding:10px" data-options="onClose: function(){uichoose.destroyDom();}" toolbar="#ui-choose-dlg-toolbar" buttons="#ui-choose-dlg-buttons">';
        	html += '<div class="easyui-layout" fit="true">';
        	html += '<div region="center" border="false" style="overflow-x:hidden;">';
        	html += '<div id="ui-choose-data-left"></div>';
        	html += '</div>';
        	html += '<div region="east" border="false" style="overflow-x:hidden;width:120px;padding-left:6px;">';
        	html += '<div id="ui-choose-data-right"></div>';
        	html += '</div>';
        	html += '</div>';
        	html += '</div>';
        	html += '<div id="ui-choose-dlg-toolbar">';
        	html += '<input id="ui-choose-search-box" style="width:180px"></input>';
        	html += '</div>';
        	html += '<div id="ui-choose-dlg-buttons">';
        	html += '<a href="#" id="ui-choose-btn-ok" class="easyui-linkbutton" iconCls="icon-ok">确定</a>';
        	html += '<a href="#" id="ui-choose-btn-cancel" class="easyui-linkbutton" iconCls="icon-cancel">取消</a>';
        	html += '</div>';
        	var container = $('<div id="ui-choose-container" style="display: none;"></div>');
        	container.append(html);
        	$('body').append(container);
        	
        	$.parser.onComplete = function(){
        		callback && callback();
        	};
        	$.parser.parse($('#ui-choose-container'));
        };
        function _initChooseLeft() {
        	$.get(_root_data, function (result) {
            	if (opt.format === 'tree') {
            		// 树形选择
            		if ($.isArray(result) && result.length > 0) {
            			var _initData = [];
    	    			if ($.isArray(result) && result.length > 0) {
    	    				_initData = _initData.concat(result);
    	    			}
                		$("#ui-choose-data-left").tree({
            				checkbox: true,
            				cascadeCheck: false,
            				onlyLeafCheck: true,
            				data: _initData,
            				animate: false,
            				line: false,
            				onClick: function(node) {
            					if (node.checked) {            						
            						$("#ui-choose-data-left").tree("uncheck", node.target);
            					} else {            						
            						$("#ui-choose-data-left").tree("check", node.target);
            					}
            				},
            				onCheck: function(node, checked) {
            					if (checked) {            						
            						addChosenToRight(node);
            					} else {
            						delChosenFromRight(node);
            					}
            				},
            				onBeforeExpand: function(node, param) {
            					// 展开应该是班级到学生。树：院系/班级/学生 三层结构
            					$("#ui-choose-data-left").tree("options").url = _root_data + "&parentId=" + node.id;
            				},
            				filter: function(q, node) {
            					var isLeaf = $("#ui-choose-data-left").tree("isLeaf", node.target);
            					var hasText = node.text.toLowerCase().indexOf(q.toLowerCase()) > -1;
            					return !isLeaf || hasText;
            				},
            	    	    onLoadSuccess: function (data) {
            	    	    	// 初始化已选择的数据
            	    	    	initChosen(data);
            	    	    }
            			});
            		} else {
            			$('#ui-choose-data-left').html('暂无数据');
            		}
            		
            	} else {
            		// 列表选择
            		if ($.isArray(result) && result.length > 0) {
            			var _initData = [];
    	    			if ($.isArray(result) && result.length > 0) {
    	    				_initData = _initData.concat(result);
    	    			}
                    	$('#ui-choose-data-left').datalist({
                    		width: '100%',
            	    	    checkbox: true,
            	    	    singleSelect: false,
            	    	    lines: false,
            	    	    border: false,
            	    	    data: _initData,
            	    	    textField: 'name',
            	    	    valueField: 'id',
            	    	    idField: 'id',
            	    	    onSelect: function (index, row) {
            	    	    	addChosenToRight(row);
            	    	    },
            	    	    onUnselect: function (index, row) {
            	    	    	delChosenFromRight(row);
            	    	    },
            	    	    onLoadSuccess: function (data) {
            	    	    	// 初始化已选择的数据
            	    	    	initChosen(data);
            	    	    }
            	    	});
            		} else {
            			$('#ui-choose-data-left').html('暂无数据');
            		}
            	}
        	});
        };
        function _initChooseRight() {
        	var _initData = [{name: '已选择：', id: ''}];
        	$('#ui-choose-data-right').datalist({
        		width: '100%',
        	    checkbox: false,
        	    lines: false,
        	    border: false,
        	    selectOnCheck: false,
        	    onBeforeSelect: function() { return false; },
        	    data: _initData,
	    	    textField: 'name',
	    	    valueField: 'id',
	    	    idField: 'id',
	    	    textFormatter: function(value, row, index) {
	    	    	if (index == 0) {
	    	    		return value;
	    	    	}
	    	    	return '<div title="单击以取消选择" style="cursor: pointer;"><a href="#" style="text-decoration: none;"> ' + value  + ' </a><div>';
	    	    },
	    	    onClickRow: function (index, row) {
	    	    	if (index > 0) {
	    	    		cancelChosen(row);
	    	    	}
	    	    },
	    	    onDblClickRow: function (index, row) {
	    	    	if (index > 0) {
	    	    		scrollToChosen(row);
	    	    	}
	    	    }
        	});
        };
        function _initSearchBox() { // 搜索框
        	$('#ui-choose-search-box').searchbox({
        	    searcher: function(value, name) {
        	    	if (opt.format === 'tree') {
        	    		// 树形选择，过滤本地数据，不从服务器请求
        	    		$('#ui-choose-data-left').tree('doFilter', value || '');
        	    	} else {
        	    		$.get(_root_search + value, function (result) {
        	    			// 列表选择，重新加载过滤后的数据
        	    			var _initData = [];
        	    			if ($.isArray(result) && result.length > 0) {
        	    				_initData = _initData.concat(result);
        	    			}
        	    			$('#ui-choose-data-left').datalist('loadData', _initData);
        	    		});
        	    	}
        	    },
        	    prompt: '输入名称以过滤'
        	});
        };
        function _initEvents() {
        	// 确定按钮
        	$('#ui-choose-btn-ok').click(function(){
        		// 获取数据
        		var _result = [];
        		$.each($('#ui-choose-data-right').datalist('getRows'), function(index, item) {
        			if (item.id) {
        				_result.push({id: item.id, name: item.name});
        			}
        		});
        		opt.onConfirm && opt.onConfirm(_result);
        		$('#ui-choose-dlg').dialog('close');
        		uichoose.destroyDom();
        	});
        	// 取消按钮
        	$('#ui-choose-btn-cancel').click(function(){
        		$('#ui-choose-dlg').dialog('close');
        		uichoose.destroyDom();
        	});
        };
        function addChosenToRight(data) { // 添加已选择
        	var rightIndex = $('#ui-choose-data-right').datalist('getRowIndex', data.id);
        	if (rightIndex < 0) {
        		var _row = {id: data.id, name: data.name || data.text};
        		$('#ui-choose-data-right').datalist('appendRow', _row);
        	}
        };
        function delChosenFromRight(data) { // 删除已选择
        	var rightIndex = $('#ui-choose-data-right').datalist('getRowIndex', data.id);
        	if (rightIndex > -1) {
        		$('#ui-choose-data-right').datalist('deleteRow', rightIndex);
        	}
        };
        function cancelChosen(row) { // 取消已选择
        	// 先取消左侧选择区域
        	if (opt.format === 'tree') {
        		// 树形选择
        		var node = $('#ui-choose-data-left').tree('find', row.id);
        		if (node) {
        			$('#ui-choose-data-left').tree('uncheck', node.target);
        		}
        	} else {
        		// 列表选择
            	var leftIndex = $('#ui-choose-data-left').datalist('getRowIndex', row.id);
            	if (leftIndex > -1) {
            		$('#ui-choose-data-left').datalist('unselectRow', leftIndex);
            	}
        	}
        	// 后取消右侧已选中区域
        	delChosenFromRight(row);
        };
        function scrollToChosen(row) { // 滚动到已选择
        	if (opt.format === 'tree') {
        		// 树形选择
        		var node = $('#ui-choose-data-left').tree('find', row.id);
        		if (node) {
        			$('#ui-choose-data-left').tree('expandTo', node.target);
        			// $('#ui-choose-data-left').tree('scrollTo', node.target);
        		}
        	} else {
        		// 列表选择
        		var leftIndex = $('#ui-choose-data-left').datalist('getRowIndex', row.id);
        		if (leftIndex > -1) {
        			$('#ui-choose-data-left').datalist('scrollTo', leftIndex);
        		}
        	}
        };
        function initChosen(allData) { // 初始化已选择
        	if ($.isArray(opt.initItems) && opt.initItems.length > 0) {
        		// 先初始化左侧选择区域
        		$.each(opt.initItems, function (index, item) {
        			if (opt.format === 'tree') {
                		// 树形选择: 一般情况下因为人员数据是叶子结点，点击才会异步加载，这里通常用不上
        				var node = $('#ui-choose-data-left').tree('find', item.id);
                		if (node) {
                			$('#ui-choose-data-left').tree('check', node.target);
                		}
                	} else {
                		// 列表选择
            			var leftIndex = $('#ui-choose-data-left').datalist('getRowIndex', item.id);
            			if (leftIndex > -1) {
            				$('#ui-choose-data-left').datalist('selectRow', leftIndex);
                		}
                	}
        		});
        		// 后初始化右侧已选中区域
        		$.each(opt.initItems, function (index, item) {
        			addChosenToRight(item)
        		});
        	}
        };
        this.destroyDom = function() {
        	$('#ui-choose-dlg').dialog('destroy');
        	$('#ui-choose-container').remove();
        };
    };
    
    window.uichoose = uichoose;
    
})(jQuery);