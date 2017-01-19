<link id="easyuiTheme" rel="stylesheet" type="text/css" href="${webRoot}/statics/base/css/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${webRoot}/statics/base/css/themes/icon.css">
<link rel="stylesheet" type="text/css" href="${webRoot}/statics/base/css/common.css">
<link href="${webRoot}/statics/base/css/icon_custom.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="${webRoot}/statics/base/js/jquery.min.js"></script>
<script type="text/javascript" src="${webRoot}/statics/base/js/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${webRoot}/statics/base/js/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="${webRoot}/statics/base/js/windowShow.js"></script>
<script type="text/javascript">

    function getCookie1() {
        var strCookie = document.cookie;
        var arrCookie = strCookie.split(";");

        for (var i = 0; i < arrCookie.length; i++) {
            var arr = arrCookie[i].split("=");
            if (arr[0] == 110110) {
                return arr[1];
            }
        }

    }
    window.webRoot = "${webRoot}";

    $($.ajaxSetup({
        dataType: 'html',
        contentType: "application/x-www-form-urlencoded;charset=utf-8",
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            if (XMLHttpRequest.status == 403) {
                alert('您没有权限访问此资源或进行此操作');
                return false;
            }
        },
        complete: function (XMLHttpRequest, textStatus) {

            if (textStatus == "parsererror") {
                $.messager.alert('提示信息', "登陆超时！请重新登陆！", 'info', function () {
                    var dialog = window.open("${webRoot}/loginajax", "", "width=350px,height=330px");
                    dialog.moveTo(500, 300);
                });
            }
        }
    }));

    $(function () {
        $.extend($.fn.validatebox.defaults.rules, {
            english: {
                validator: function (value) {
                    return /^[A-Za-z]+$/i.test(value);
                },
                message: '请输入英文字母.'
            },
            noChinese: {// 验证不能输入中文
                validator: function (value) {
                    var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
                    return !reg.test(value);
                },
                message: '请勿输入中文.'
            },
            specialCharacters: {// 特殊字符验证
                validator: function (value) {
                    var reg = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：\"'”“。，、？ ]");
                    return !reg.test(value);
                },
                message: '请勿输入特殊字符.'
            },
            specialCharactersComma: {// 特殊字符验证
                validator: function (value) {
                    var reg = new RegExp("[\"'”“’‘’]");
                    return !reg.test(value);
                },
                message: '请勿输入特殊字符.'
            },
            nospace: {// 空格字符验证
                validator: function (value) {
                    var ori = value.length;
                    var comp = value.replace(/\s/g, "").length;
                    return ori == comp;
                },
                message: '请勿输入空格.'
            },
            lengthCharacter: {
                validator: function (value, param) {
                    var len = 0;
                    for (var i = 0; i < value.length; i++) {
                        var a = value.charAt(i);
                        if (a.match(/[^\x00-\xff]/ig) != null) {
                            len += 2;
                        }
                        else {
                            len += 1;
                        }
                    }
                    if (len <= param[1]) {
                        return true;
                    } else {
                        return false;
                    }
                },
                message: '输入内容最大<br>{0}汉字或{1}字符'
            }})
        })



        //关闭窗口
        function closeDialog(winId) {
            parent.$("#" + winId).dialog("close");
        }

</script>