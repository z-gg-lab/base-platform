var groot={};
/*通用弹出框*/
(function () {
    var $css3Transform = function (element, attribute, value) {
        var arrPriex = ["O", "Ms", "Moz", "Webkit", ""], length = arrPriex.length;
        for (var i = 0; i < length; i += 1) {
            element.css(arrPriex[i] + attribute, value);
        }
    }
    var game = {};
    game.zIndex = 12;//背景z-index
    game.stack = [];//弹出框 z-index栈
    game.stackPop = [];//弹出框对象
    game.showPop = function (element, callback, iClose) {
        if (window.parent !== window) {
            window.parent.groot.pop.showPop(element, callback, iClose);
            return;
        }
        var divgame = $("body");
        if (game.stack.length == 0) {
            divgame.append("<div id='pop-bg' class='pop-bg no-select'></div>");
            game.zIndex = 12;
            $("#pop-bg").css("zIndex", game.zIndex);
        } else {
            game.zIndex = game.stack[game.stack.length - 1] + 1;
            $("#pop-bg").css("zIndex", game.zIndex);
        }
        var pop = $("<div class='pop-container no-select'></div>");
        divgame.append(pop);
        var zPop = game.zIndex + 1999;
        pop.css("zIndex", zPop);
        game.stack.push(zPop);
        game.stackPop.push(pop);
        element = $(element);
        pop.append(element);
        var move = element.find(".drag").css("cursor", "move");
        move.bind("mousedown", function (event) {
            var x0 = event.pageX;
            var y0 = event.pageY;
            var pop = $(this).parent().parent();
            var ex0 = pop.css("left").replace("px", "") * 1;
            var ey0 = pop.css("top").replace("px", "") * 1;
            move.bind("mousemove", function (e) {
                var x1 = e.pageX;
                var y1 = e.pageY;
                var mx = x1 - x0;
                var my = y1 - y0;
                var ex1 = ex0 + mx;
                var ey1 = ey0 + my;
                pop.css("left", ex1 + "px");
                pop.css("top", ey1 + "px");

            });
        });
        move.bind("mouseup", function () {
            move.unbind("mousemove");
        });
        move.bind("mouseleave", function () {
            move.unbind("mousemove");
        });
        if (typeof callback == "function") {
            if (callback(element[0], show) !== "wait") {
                show();
            }
            ;
        } else {
            show();
        }
        function show() {
            var w = pop.width();
            var h = pop.height();
            var wScreen = $(window).width();
            var hScreen = $(window).height();
            if (w > wScreen) {
                pop.width(wScreen);
                w = wScreen;
            }
            if (h > hScreen) {
                pop.height(hScreen);
                h = hScreen;
            }
            var x = (wScreen - w) / 2;
            var y = (hScreen - h) / 2;
            if ($(".navbar-collapse").length > 0) {
            	y = y - 46; // -46 是针对 minimal 框架布局的修正
            }
            pop.css("left", x + "px");
            pop.css("top", y + "px");
            pop.removeClass("pop-container-animate");
            setTimeout(function () {
                pop.addClass("pop-container-animate");
                $css3Transform(pop, "Transform", "scale(1, 1)");
                $css3Transform(pop, "opacity", "1");
                setTimeout(function () {
                    pop.removeClass("pop-container-animate");
                    if (!((typeof iClose != "undefined") && iClose === false)) {
                        pop.addClass("iClose");
                    }
                    $("#pop-bg").unbind("click").bind("click", function () {
                        var element = game.stackPop[game.stackPop.length - 1];
                        if (element.hasClass("iClose")) {
                            game.closePop();
                            var mid = element.attr("popmid");
                            if (mid !== undefined) {
                                if (window.parent !== window) {
                                    delete window.parent[mid];
                                } else {
                                    delete window[mid];
                                }
                            }
                        }
                    })

                }, 300);
            }, 30);
        }

    };
    game.closePop = function () {
        if (window.parent != window) {
            $("#pop-calendar", $(window.parent.document.body)).remove();
        } else {
            $("#pop-calendar").remove();
        }
        if (window.parent !== window) {
            window.parent.groot.pop.closePop();
            return;
        }
        var pop = game.stackPop.pop();
        game.stack.pop();
        if (game.stack.length == 0) {
            $("#pop-bg").remove();
            game.zIndex = 12;
        } else {
            game.zIndex = game.stack[game.stack.length - 1] - 1;
            $("#pop-bg").css("zIndex", game.zIndex);
        }
        pop.addClass("pop-container-animate");
        $css3Transform(pop, "opacity", "0");
        $css3Transform(pop, "Transform", "scale(0, 0)");
        setTimeout(function () {
            pop.remove();
        }, 300);
    }
    $(window).on("resize", function () {
        var pops = $(".pop-container");
        if (pops.length > 0) {
            pops.each(function () {
                var pop = $(this);
                var w = pop.width();
                var h = pop.height();
                var wScreen = $(window).width();
                var hScreen = $(window).height();
                var x = (wScreen - w) / 2;
                var y = (hScreen - h) / 2;
                pop.css("left", x + "px");
                pop.css("top", y + "px");
            });
        }
    });
    groot.pop = game;//弹出框通用组件

})()

;
(function () {
	var _root = "";
    var _root_data = "json/data.json";
    var _root_search = "json/search.json";
    
    var _default = {
        type: "user",
        typeName: "选人",
        multi: true,
        initItems: [], // name, id
        webRoot: window.webRoot,
        apiPath: "/api/user/data"
    };
    groot.openChooseDialog = function (config) {
    	var _templateOpt = $.extend(true, {}, _default);
        var opt = $.extend(true, _templateOpt, config);
        _root = opt.webRoot || "";
        if (opt.type === "user") {
        	opt.typeName = "选人";
        	_root_data = _root + "/api/user/data?format=tree";
            _root_search = _root_data + "&keyword=";
        }
        else if (opt.type === "org") {
        	opt.typeName = "选部门";
        	_root_data = _root + "/api/org/data?format=tree";
            _root_search = _root_data + "&keyword=";
        }
        else {
        	if(opt.apiPath.indexOf('?') > 0){
        		_root_data = _root + opt.apiPath + "&format=tree";
        	}else{
        		_root_data = _root + opt.apiPath + "?format=tree";
        	}
            _root_search = _root_data + "&keyword=";
        }
        var html = "";
        html += "<div class='dialog open'>";
        html += "<div class='dialog-head drag'>";
        html += "<span class='close' title='关闭'></span><strong>" + (opt.typeName || "选择") + "</strong>";
        html += "</div>";
        html += "<div class='dialog-body'>";
        html += "<div class='line tree' style='width: 480px;height: 300px'>";
        html += "<div class='x9'>";
        html += "<div class='input-group'>";
        html += "<input id='treesearch' type='text' class='input' placeholder='请输入关键字' size='30'>";
        html += "<span class='addon fa fa-search'></span>";
        html += "</div>";
        html += "<div id='slist' class='slist' style='height: 260px;display: none'></div>";
        html += "<div class='tree padding-top' id='treeleft'>";
        html += "</div>";
        html += "</div>";
        html += "<div class='x3' id='treeright'>";
        html += "</div>";
        html += "</div>";
        html += "</div>";
        html += "<div class='dialog-foot'>";
        html += "<button class='button cancel dialog-close'>取消</button>";
        html += "<button class='button clear dialog-close margin-left'>清空</button>";
        html += "<button class='button ok bg-sub margin-left'>确认</button>";
        html += "</div>";
        html += "</div>";
        html += "</div>";
        function hasid(id, name) {
            var ret = false;
            for (var i = 0; i < opt.initItems.length; i++) {
                if (opt.initItems[i].id === id) {
                    ret = true;
                    if (opt.initItems[i].name == null) {
                    	opt.initItems[i].name = name;
                    }
                    break;
                }
            }
            return ret;
        };

        var _div = $(html);
        if (opt["type"] === "org") { // 数据格式 name, id, pId
            $.get(_root_data, function (result) {
                function creat(element, pid, layer) {
                    var index = 0;
                    for (var i = 0; i < result.length; i++) {
                        var o = result[i];
                        if (o.pId === pid) {
                            index++;
                            var temp = "";
                            temp += "<div class='row'>";
                            temp += "<div class='name'>";
                            if (layer < 2) {
                                temp += "<span class='check icon-minus-square-o'></span>";
                            } else {
                                temp += "<span class='check icon-plus-square-o'></span>";
                            }
                            if (hasid(o.id, o.name)) {
                                if (opt.multi) {
                                    temp += "<input checked='true' data-name='" + o.name + "' data-id='" + o.id + "' type='checkbox'>";
                                } else {
                                    temp += "<input name='xr' checked='true' data-name='" + o.name + "' data-id='" + o.id + "' type='radio'>";
                                }
                            } else {
                                if (opt.multi) {
                                    temp += "<input data-name='" + o.name + "' data-id='" + o.id + "' type='checkbox'>";
                                } else {
                                    temp += "<input name='xr' data-name='" + o.name + "' data-id='" + o.id + "' type='radio'>";
                                }

                            }
                            temp += "<span class='icon icon-group'></span>";
                            temp += o.name;
                            temp += "</div>";
                            temp += "</div>";
                            temp = $(temp);
                            var root;
                            if (layer < 2) {
                                root = $("<div class='root'></div>");
                            } else {
                                root = $("<div class='root' style='display:none;'></div>");
                            }
                            temp.append(root);
                            $(element).append(temp);
                            if (creat(root, o.id, layer + 1) === 0) {
                                temp.find(".check").hide();
                            }
                        }
                    }
                    return index;
                }

                creat(_div.find("#treeleft"), 0, 1);
                for (var i = 0; i < opt.initItems.length; i++) {
                    if (!opt.multi && i > 0) {
                        return;
                    }
                    var o = opt.initItems[i];
                    var _intem = "<div data-id='" + o.id + "' data-name='" + o.name + "' class='selecte' id='sec" + o.id + "'>";
                    if (o.cascadeFlag === "1") {
                        _intem += "<input title='是否级联' checked='true' type='checkbox'>";
                    } else {
                        _intem += "<input title='是否级联' type='checkbox'>";
                    }
                    _intem += (o.name || o.id);
                    _intem += "<span class='fa fa-times' title='移除'></span>";
                    _intem += "</div>";
                    _div.find("#treeright").append(_intem);
                }
            })
            _div.find("#treeleft").on("click", ".check", function () {
                if ($(this).hasClass("icon-minus-square-o")) {
                    $(this).removeClass("icon-minus-square-o");
                    $(this).addClass("icon-plus-square-o");
                    $(this).parent().parent().find(">.root").slideUp();
                } else {
                    $(this).removeClass("icon-plus-square-o");
                    $(this).addClass("icon-minus-square-o");
                    $(this).parent().parent().find(">.root").slideDown();
                }
            })
            _div.find("#treeleft").on("click", "input[type='checkbox'],input[type='radio']", function () {
                var id = $(this).attr("data-id");
                if ($(this).is(':checked')) {
                    var id = $(this).attr("data-id");
                    var name = $(this).attr("data-name");
                    if (opt.multi) {
                        _div.find("#treeright").append("<div data-id='" + id + "' data-name='" + name + "' class='selecte' id='sec" + id + "'><input title='是否包含子部门?' type='checkbox'>" + name + "<span class='icon-times' title='移除'></span></div>");
                    } else {
                        _div.find("#treeright").html("<div data-id='" + id + "' data-name='" + name + "' class='selecte' id='sec" + id + "'><input title='是否包含子部门?' type='checkbox'>" + name + "<span class='icon-times' title='移除'></span></div>");
                    }
                } else {
                    _div.find("#sec" + id).remove();
                }
            })
        }
        else { // 包括 if (opt["type"] === "user") // 数据格式 name, id, icon, isParent
            function creat(element, id, name, callback) {
                $.post(_root_data, name === null ? {} : {id: id, name: name}, function (result) {
                    for (var i = 0; i < result.length; i++) {
                        var o = result[i];
                        var temp = "";
                        temp += "<div class='row'>";
                        temp += "<div class='name'>";
                        if (o.isParent) {
                            temp += "<span data-name='" + o.name + "' data-id='" + o.id + "' class='check icon-plus-square-o'></span>";
                        }
                        if (!o.isParent) {
                            if (hasid(o.id, o.name)) {
                                if (opt.multi) {
                                    temp += "<input data-icon='" + o.icon + "' checked='true' data-name='" + o.name + "' data-id='" + o.id + "' type='checkbox'>";
                                } else {
                                    temp += "<input data-icon='" + o.icon + "' name='xr' checked='true' data-name='" + o.name + "' data-id='" + o.id + "' type='radio'>";
                                }
                            } else {
                                if (opt.multi) {
                                    temp += "<input data-icon='" + o.icon + "' data-name='" + o.name + "' data-id='" + o.id + "' type='checkbox'>";
                                } else {
                                    temp += "<input data-icon='" + o.icon + "' name='xr' data-name='" + o.name + "' data-id='" + o.id + "' type='radio'>";
                                }

                            }
                        }
                        if (o.isParent) {
                            temp += "<span class='icon icon-group'></span>";
                        } else {
                            temp += "<span class='icon icon-user'></span>";
                        }
                        temp += o.name;
                        temp += "</div>";
                        temp += "</div>";
                        temp = $(temp);
                        var root = $("<div class='root' style='display:none;'></div>");
                        temp.append(root);
                        $(element).append(temp);
                    }
                    callback();
                });
            }

            creat(_div.find("#treeleft"), null, null, function () {
            	
            	// 先拼左侧树，再拼右侧选择区域
            	for (var i = 0; i < opt.initItems.length; i++) {
            		if (!opt.multi && i > 0) {
            			break;
            		}
            		var o = opt.initItems[i];
            		var _intem = "<div data-id='" + o.id + "' data-name='" + o.name + "' class='selecte' id='sec" + o.id + "'>";
            		_intem += (o.name || o.id);
            		_intem += "<span class='icon-times' title='移除'></span>";
            		_intem += "</div>";
            		_div.find("#treeright").append(_intem);
            	}
            });
            _div.find("#treeleft").on("click", ".check", function () {
                if ($(this).hasClass("icon-minus-square-o")) {
                    $(this).removeClass("icon-minus-square-o");
                    $(this).addClass("icon-plus-square-o");
                    $(this).parent().parent().find(">.root").slideUp();
                } else {
                    $(this).removeClass("icon-plus-square-o");
                    $(this).addClass("icon-minus-square-o");
                    var p = $(this).parent().parent().find(">.root");
                    if (p.find("div").length > 0) {
                        p.slideDown();
                    } else {
                        creat(p, $(this).attr("data-id"), $(this).attr("data-name"), function () {
                            p.slideDown();
                        })
                    }
                }
            })
            _div.find("#treeleft").on("click", "input[type='checkbox'],input[type='radio']", function () {
                var id = $(this).attr("data-id");
                if ($(this).is(':checked')) {
                    var id = $(this).attr("data-id");
                    var name = $(this).attr("data-name");
                    var icon = $(this).attr("data-icon");
                    if (opt.multi) {
                        _div.find("#treeright").append("<div data-icon='" + icon + "' data-id='" + id + "' data-name='" + name + "' class='selecte' id='sec" + id + "'>" + name + "<span class='icon-times' title='移除'></span></div>");
                    } else {
                        _div.find("#treeright").html("<div data-icon='" + icon + "' data-id='" + id + "' data-name='" + name + "' class='selecte' id='sec" + id + "'>" + name + "<span class='icon-times' title='移除'></span></div>");
                    }
                } else {
                    _div.find("#sec" + id).remove();
                }
            })
        }

        _div.find("#treeright").on("click", ".selecte span", function () {
            $(this).parent().remove();
            $("input[data-id='" + $(this).parent().attr("id").replace("sec", "") + "']", _div).removeAttr("checked");
        });
        _div.find("#treeright").on("dblclick", "div.selecte", function () {
            var selId = $(this).attr('data-id');
            _div.find("#treeleft .row input[data-id='"+selId+"']").focus();
        });
        groot.pop.showPop(_div, function (element) {
            $(element).find(".ok").on("click", function () {
                groot.pop.closePop();
                if (typeof opt.onchange == "function") {
                    var retlist = [];
                    _div.find("#treeright").find(".selecte").each(function () {
                        var id = $(this).attr("data-id");
                        var name = $(this).attr("data-name");
                        var icon = $(this).attr("data-icon");
                        var cascadeFlag = "0";
                        if ($(this).find("input").length > 0 && $(this).find("input").first().is(':checked')) {
                            cascadeFlag = "1";
                        }
                        if (opt.type === "org") {
                            retlist.push({id: id, name: name, cascadeFlag: cascadeFlag})
                        } else {
                            retlist.push({id: id, name: name})
                        }
                    });
                    opt.onchange(retlist);
                }
            });
            $(element).find(".clear").on("click", function () {
                $("#treeright").html("");
                $("#treeleft").find("input[type='checkbox'],input[type='radio']").removeAttr("checked");
            });
            $(element).find(".cancel").on("click", function () {
                groot.pop.closePop();
            });
            $(element).find(".close").on("click", function () {
                groot.pop.closePop();
            });
        }, false)
        /*搜索*/
        var timer;

        function keydown(event) {
            var code = event.keyCode;
            if (code === 13) {//enter
                var id = _div.find("#slist").find(".active").attr("data-id");
                var name = _div.find("#slist").find(".active").attr("data-name");
                var icon = _div.find("#slist").find(".active").attr("data-icon");
                if(!id){
                	return;
                }
                if (opt.type == "org") {
                    if (opt.multi) {
                        _div.find("#treeright").append("<div data-id='" + id + "' data-name='" + name + "' data-icon='" + icon + "' class='selecte' id='sec" + id + "'><input title='是否级联' type='checkbox'>" + name + "<span class='icon-times' title='移除'></span></div>");
                        _div.find("input[data-id='" + id + "']").attr("checked", true);
                    } else {
                        _div.find("#treeright").html("<div data-id='" + id + "' data-name='" + name + "' data-icon='" + icon + "' class='selecte' id='sec" + id + "'><input title='是否级联' type='checkbox'>" + name + "<span class='icon-times' title='移除'></span></div>");
                        _div.find("input").removeAttr("checked");
                        _div.find("input[data-id='" + id + "']").attr("checked", true);
                    }
                } else {
                    if (opt.multi) {
                        _div.find("#treeright").append("<div data-id='" + id + "' data-name='" + name + "' data-icon='" + icon + "' class='selecte' id='sec" + id + "'>" + name + "<span class='icon-times' title='移除'></span></div>");
                        _div.find("input[data-id='" + id + "']").attr("checked", true);
                    } else {
                        _div.find("#treeright").html("<div data-id='" + id + "' data-name='" + name + "' data-icon='" + icon + "' class='selecte' id='sec" + id + "'>" + name + "<span class='icon-times' title='移除'></span></div>");
                        _div.find("input").removeAttr("checked");
                        _div.find("input[data-id='" + id + "']").attr("checked", true);
                    }
                }
                _div.find("#treesearch").val("");
                _div.find("#slist").hide();
                _div.find("#slist").html("");
                _div.find("#treesearch").off("keydown", keydown);
            }
            if (code === 38) {//上
                var lenght = _div.find("#slist").find(".li").length - 1;
                var index = _div.find("#slist").find(".active").index();
                if (index == 0) {
                    index = lenght;
                } else {
                    index--;
                }
                _div.find("#slist").find(".li").removeClass("active");
                _div.find("#slist").find(".li").eq(index).addClass("active");
            }
            if (code === 40) {//下
                var lenght = _div.find("#slist").find(".li").length - 1;
                var index = _div.find("#slist").find(".active").index();
                if (index == lenght) {
                    index = 0;
                } else {
                    index++;
                }
                _div.find("#slist").find(".li").removeClass("active");
                _div.find("#slist").find(".li").eq(index).addClass("active");
            }
        }

        _div.find("#treesearch").on("input propertychange", function () {
            clearTimeout(timer);
            var val = $.trim($(this).val());
            if (val == "") {
                _div.find("#slist").hide();
                _div.find("#slist").html("");
                _div.find("#treesearch").off("keydown", keydown);
            } else {
                if (_div.find("#slist").is(":hidden")) {
                    _div.find("#slist").show();
                    _div.find("#treesearch").on("keydown", keydown);
                }
                timer = setTimeout(function () {
                    $.get(_root_search + val, function (result) {
                        var html = "";
                        for (var i = 0; i < result.length; i++) {
                            var o = result[i];
                            if (_div.find("#sec" + o.id).length == 0) {
                                if (html == "") {
                                    if (opt.type === "user") {
                                        html += "<div data-icon='" + o.icon + "' data-name='" + o.name + "' data-id='" + o.id + "' class='li active'>" + o.name + "</div>";
                                    } else {
                                        html += "<div data-name='" + o.name + "' data-id='" + o.id + "' class='li active'>" + o.name + "</div>";
                                    }
                                } else {
                                    if (opt.type === "user") {
                                        html += "<div data-icon='" + o.icon + "' data-name='" + o.name + "' data-id='" + o.id + "' class='li'>" + o.name + "</div>";
                                    } else {
                                        html += "<div data-name='" + o.name + "' data-id='" + o.id + "' class='li'>" + o.name + "</div>";
                                    }
                                }
                            }
                        }
                        _div.find("#slist").html(html);
                    })
                }, 300)
            }
        })
        _div.find("#slist").on("click", ".li", function () {
            var id = $(this).attr("data-id");
            var name = $(this).attr("data-name");
            var icon = $(this).attr("data-icon");
            if (opt.type == "org") {
                if (opt.multi) {
                    _div.find("#treeright").append("<div data-id='" + id + "' data-name='" + name + "' class='selecte' id='sec" + id + "'><input title='是否级联' type='checkbox'>" + name + "<span class='icon-times' title='移除'></span></div>");
                    _div.find("input[data-id='" + id + "']").attr("checked", true);
                } else {
                    _div.find("#treeright").html("<div data-id='" + id + "' data-name='" + name + "' class='selecte' id='sec" + id + "'><input title='是否级联' type='checkbox'>" + name + "<span class='icon-times' title='移除'></span></div>");
                    _div.find("input").removeAttr("checked");
                    _div.find("input[data-id='" + id + "']").attr("checked", true);
                }
            } else {
                if (opt.multi) {
                    _div.find("#treeright").append("<div data-id='" + id + "' data-name='" + name + "' data-icon='" + icon + "' class='selecte' id='sec" + id + "'>" + name + "<span class='icon-times' title='移除'></span></div>");
                    _div.find("input[data-id='" + id + "']").attr("checked", true);
                } else {
                    _div.find("#treeright").html("<div data-id='" + id + "' data-name='" + name + "' data-icon='" + icon + "' class='selecte' id='sec" + id + "'>" + name + "<span class='icon-times' title='移除'></span></div>");
                    _div.find("input").removeAttr("checked");
                    _div.find("input[data-id='" + id + "']").attr("checked", true);
                }
            }
            _div.find("#treesearch").val("");
            _div.find("#slist").hide();
            _div.find("#slist").html("");
            _div.find("#treesearch").off("keydown", keydown);
        })
    }
})();