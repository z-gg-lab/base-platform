<!DOCTYPE html>
<html>
<head>
    <title>${systemName!''}</title>
<#include "include.ftl">
<#include "ocx.ftl">
    <link rel="stylesheet" type="text/css" href="${webRoot}/statics/base/css/index.css"/>
    <script type="text/javascript" src="${webRoot}/statics/base/js/component/ComponentOfDepartment.js"></script>
    <style type="text/css">
        #home {
            text-align: center;
            display: table-cell;
            vertiacal-align: middle;
            background-image: url(${webRoot}/statics/base/images/welcome_bg.png);
            background-repeat: repeat-x;
        }

        #img {
            display: inline-block;
            vertical-align: middle;
        }
    </style>
</head>
<body class="easyui-layout" style="overflow-y: hidden" scroll="no">
<div region="north" split="true" style="height:30px;border:none;">
    <div style="overflow: hidden;background: url(${webRoot}/statics/base/images/layout-browser-hd-bg.gif) #7f99be repeat-x center 50%;
            line-height: 20px;color: #fff; font-family: Verdana, 微软雅黑,黑体">
	        <span style="float:right; padding-right:20px;" class="head">
	        	欢迎
	        	<a href="#" id="editProfile" style="text-decoration: none;">${currentUser.userName!} </a>
	        	<a href="${webRoot}/logout" id="loginOut">安全退出</a>
	        </span>
        <span style="padding-left:10px; font-size: 16px; "><img src="${webRoot}/statics/base/images/blocks.gif"
                                                                width="20" height="20"
                                                                align="absmiddle"/>${systemName!''}</span>
        <div id="themes" style="float:right;padding-right:20px;">
            <span>更换皮肤：</span>
            <select class="easyui-combobox" panelHeight="auto" style="width:100px;height:20px" id="changeSkin"
                    data-options="editable:false,hasDownArrow:true">
                <option value="default">天空蓝</option>
                <option value="metro-green">魔法绿</option>
                <option value="metro-orange">魅惑橙</option>
                <option value="ui-pepper-grinder">温莎红</option>
                <option value="ui-cupertino">宝石蓝</option>
                <option value="ui-sunny">耀沙金</option>
                <option value="metro-gray">星河银</option>
                <option value="metro-red">魅力红</option>
            </select>
        </div>
    </div>
</div>
<div region="south" split="true" style="height: 30px;">
    <div class="footer">${copyrightInfo!''}</div>
</div>
<div region="west" split="true" title="导航菜单" iconCls="icon-house" style="width:180px;" id="west">
    <div id="nav">
        <!--  导航内容 -->
    </div>
</div>
<div id="mainPanle" region="center" style="overflow-y:hidden">
    <div id="tabs" class="easyui-tabs" fit="true" border="false">
        <div title="欢迎使用" data-options="iconCls:'icon-house'" style="overflow:hidden;" id="home">
            <div id="img">
                <img src="${webRoot}/statics/base/images/welcome.png"/>
            </div>
            <span style="display: inline-block; height: 50%;"></span>
        </div>
    </div>
</div>

<div id="mm" class="easyui-menu" style="width:150px;">
    <div id="mm-tabclose">关闭</div>
    <div id="mm-tabcloseall">全部关闭</div>
    <div id="mm-tabcloseother">除此之外全部关闭</div>
    <div class="menu-sep"></div>
    <div id="mm-tabcloseright">当前页右侧全部关闭</div>
    <div id="mm-tabcloseleft">当前页左侧全部关闭</div>
    <div class="menu-sep"></div>
    <div id="mm-exit">退出</div>
</div>
<div id="dialog" class="easyui-dialog" closed="true" data-options="modal:true"></div>
<div id="edittwo" class="easyui-dialog" closed="true" data-options="modal:true"></div>
<div id="win" class="easyui-dialog" closed="true" data-options="modal:true"></div>
<div id="iconFrame" class="easyui-dialog" closed="true" data-options="modal:true"></div>
<div id="departmentSelect" class="easyui-dialog" closed="true" data-options="modal:true"></div>
<div id="dg" class="easyui-dialog" closed="true" data-options="modal:true"></div>
<div id="userRole" class="easyui-dialog" closed="true" data-options="modal:true"></div>
<div id="userSelect4Component" class="easyui-dialog" closed="true" data-options="modal:true"></div>

<script type="text/javascript">

    function addTab(subtitle, url, icon) {

        if (!$('#tabs').tabs('exists', subtitle)) {
            $('#tabs').tabs('add', {
                title: subtitle,
                content: createFrame(url),
                closable: true,
                width: $('#mainPanle').width() - 10,
                height: $('#mainPanle').height() - 26,
                icon: icon
            });
        } else {
            $('#tabs').tabs('select', subtitle);
        }
        tabClose();
    }

    function createFrame(url) {
        var s = '<iframe name="mainFrame" id="mainFrame" scrolling="auto" frameborder="0"  src="' + url + '" style="width:100%;height:99%;"></iframe>';
        return s;
    }

    function tabClose() {
        /*双击关闭TAB选项卡*/
        $(".tabs-inner").dblclick(function () {
            var subtitle = $(this).children("span").text();
            if ($(this).siblings().is('.tabs-close')) {
                $('#tabs').tabs('close', subtitle);
            }
        })

        $(".tabs-inner").bind('contextmenu', function (e) {
            if ($(this).siblings().is('.tabs-close')) {
                $('#mm').menu('show', {
                    left: e.pageX,
                    top: e.pageY
                });

                var subtitle = $(this).children("span").text();
                $('#mm').data("currtab", subtitle);
            }
            return false;
        });
    }

    //绑定右键菜单事件
    function tabCloseEven() {
        var currtab_title = $('#mm').data("currtab");
        //关闭当前
        $('#mm-tabclose').click(function () {
            var currtab_title = $('#mm').data("currtab");
            $('#tabs').tabs('close', currtab_title);
        })
        //全部关闭
        $('#mm-tabcloseall').click(function () {
            $('.tabs-inner span').each(function (i, n) {
                if (i > 0) {
                    var t = $(n).text();
                    $('#tabs').tabs('close', t);
                }
            });
        });
        //关闭除当前之外的TAB
        $('#mm-tabcloseother').click(function () {
            var currtab_title = $('#mm').data("currtab");
            $('.tabs-inner span').each(function (i, n) {
                if ($(this).parent().siblings().is('.tabs-close')) {
                    var t = $(n).text();
                    if (t != currtab_title) {
                        $('#tabs').tabs('close', t);
                    }
                }
            });
            $('#tabs').tabs('select', currtab_title);
        });
        //关闭当前右侧的TAB
        $('#mm-tabcloseright').click(function () {
            var nextall = $('.tabs-selected').nextAll();
            if (nextall.length == 0) {
                //msgShow('系统提示','后边没有啦~~','error');
                alert('后边没有啦~~');
                return false;
            }
            nextall.each(function (i, n) {
                var t = $('a:eq(0) span', $(n)).text();
                $('#tabs').tabs('close', t);
            });
            $('#tabs').tabs('select', currtab_title);
            return false;
        });
        //关闭当前左侧的TAB
        $('#mm-tabcloseleft').click(function () {
            var prevall = $('.tabs-selected').prevAll();
            if (prevall.length == 1) {
                alert('到头了，前边没有啦~~');
                return false;
            }
            prevall.each(function (i, n) {
                if (i < prevall.length - 1) {
                    var t = $('a:eq(0) span', $(n)).text();
                    $('#tabs').tabs('close', t);
                }
            });
            $('#tabs').tabs('select', currtab_title);
            return false;
        });

        //退出
        $("#mm-exit").click(function () {
            $('#mm').menu('hide');
        })
    }

    //重新加载数据表格
    function reloadData(tableID) {
        var frame = $('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#' + tableID);
        frame.datagrid('reload');
    }
    //重新加载数据表格
    function loadData(tableID) {
        var frame = $('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#' + tableID);
        frame.datagrid('load');
    }

    //信息匹配
    function searchmatch(url, tableID, cardNo, recycleBtn, cancelCard, unbindBtn) {
        var frame = $('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#' + tableID);
        frame.datagrid({
            url: url,
            queryParams: {
                "cardNo": cardNo
            }
        });
        if (recycleBtn != "") {
            $('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#' + recycleBtn).linkbutton('enable');
        }
        if (unbindBtn != "") {
            $('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#' + unbindBtn).linkbutton('enable');
        }
        if (cancelCard != "") {
            $('#tabs').tabs('getSelected').find('iframe')[0].contentWindow.$('#' + cancelCard).linkbutton('disable');
        }
    }

    //主题切换
    window.onload = function () {
        var cookieToken = "default";
        if (getCookie1() != null) {
            cookieToken = getCookie1("110110")
        }
        $("#easyuiTheme").attr("href", "${webRoot}/statics/base/css/themes/" + cookieToken + "/easyui.css");

        $("#changeSkin").combobox({
            onChange: function (newVal, oldVal) {
                var $easyuiTheme = $('#easyuiTheme');
                var url = $easyuiTheme.attr('href');
                var href = url.substring(0, url.indexOf('themes')) + 'themes/' + newVal + '/easyui.css';
                $easyuiTheme.attr('href', href);
                var $iframe = $('iframe');
                if ($iframe.length > 0) {
                    for (var i = 0; i < $iframe.length; i++) {
                        var ifr = $iframe[i];
                        $(ifr).contents().find('#easyuiTheme').attr('href', href);
                    }
                }
                $.ajax({
                    url: "${webRoot}/changeTheme",
                    type: "POST",
                    data: {"theme": newVal},
                    dataType: "json",
                    success: function (jdata, stat) {
                    }
                });
            }
        });
        var a = $("#changeSkin").combobox('setValue', cookieToken);

        var testExcel, excelId;
        // 个人资料
        $('#editProfile').click(function () {
            addTab('个人资料', '${webRoot}/profile/edit?userId=${currentUser.userId!}');
        });

        //初始化菜单

        $('#nav').accordion({
            fit: true,
            border: false
        });

        $.ajax({
            type: 'POST',
            dataType: "json",
            contentType: "application/json;charset=utf-8",
            url: "${webRoot}/menu/menuDataByRole?id=-1",
            success: function (data) {
                $.each(data, function (i, n) {//加载父类节点即一级菜单
                    if (i == 0) {
                        $('#nav').accordion('add', {
                            title: n.menuName,
                            iconCls: n.iconCls,
                            selected: true,
                            content: '<div style="padding:5px"><ul id="' + n.menuId + '"></ul></div>'
                        });
                    } else {
                        $('#nav').accordion('add', {
                            title: n.menuName,
                            iconCls: n.iconCls,
                            selected: false,
                            content: '<div style="padding:5px"><ul id="' + n.menuId + '"></ul></div>'
                        });
                    }

                    $("ul[id='" + n.menuId + "']").tree({
                        url: "${webRoot}/menu/treeDataByRoleMenuId/" + n.menuId,
                        animate: true,
                        lines: true,//显示虚线效果
                        onClick: function (node) {
                            $.ajax({
                                method: "post",
                                url: "${webRoot}/preclickmenu?date=" + Date.parse(new Date()),
                                contentType: "application/x-www-form-urlencoded;charset=utf-8",
                                async: false,
                                success: function (data) {
                                    if (data == "true") {
                                        if (node.attributes.url && node.attributes.url != "") {
                                            if ($("#tabs").tabs("getTab", node.text) == null) {
                                                addTab(node.text, "${webRoot}" + node.attributes.url, node.iconCls);
                                            } else {
                                                $("#tabs").tabs("select", node.text, node.iconCls);
                                            }
                                        }
                                    } else {
                                        $.messager.alert('提示信息', "登陆超时！请重新登陆！", 'info', function () {
                                            var dialog = window.open("${webRoot}/loginajax", "", "width=350px,height=330px");
                                            dialog.moveTo(500, 300);
                                        });

                                    }
                                }
                            });

                        },
                        onBeforeExpand: function (node, param) {
                            $("ul[id='" + n.menuId + "']").tree("options").url = "${webRoot}/menu/treeDataByRole/" + node.id;
                        }
                    });

                });

            }
        });

        tabClose();
        tabCloseEven();

        //发送请求判断当前的ocx是否需要更新
        //checkOcxVersion();
        checkOcxVersion(checkOcx());

    }


</script>

</body>
</html>