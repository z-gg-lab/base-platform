
(function ($, window) {
    var applicationPath = webRoot;
    function SuiJiNum() {
        return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
    }
 
    function initWebUpload(item, options) {
 
        if (!WebUploader.Uploader.support()) {
            var error = "上传控件不支持您的浏览器！请尝试升级flash版本或者使用Chrome引擎的浏览器。<a target='_blank' href='http://se.360.cn'>下载页面</a>";
            if (window.console) {
                window.console.log(error);
            }
            $(item).text(error);
            return;
        }
        //创建默认参数
        var defaults = {
            auto:true,
            hiddenInputId: "uploadifyHiddenInputId", // input hidden id
            onAllComplete: function (event) {}, // 当所有file都上传后执行的回调函数
            onComplete: function (event) {},// 每上传一个file的回调函数
            innerOptions: {
                onAllComplete: function (event) {},
                onComplete: function (event) {}
            },
            accept:{},//验证文件格式
            fileNumLimit: undefined,//验证文件总数量, 超出则不允许加入队列
            fileSizeLimit: undefined,//验证文件总大小是否超出限制, 超出则不允许加入队列。
            fileSingleSizeLimit: undefined,//验证单个文件大小是否超出限制, 超出则不允许加入队列
            PostbackHold: false
        };
        var opts = $.extend(defaults, options);
        var hdFileData = $("#" + opts.hiddenInputId);
        var target = $(item);//容器
        var pickerid = "";
        if (typeof guidGenerator36 != 'undefined')//给一个唯一ID
            pickerid = guidGenerator36();
        else
            pickerid = (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
        var uploaderStrdiv = '<div class="webuploader">'
        
        if (opts.auto) {
            uploaderStrdiv = 
            '<div id="Uploadthelist" class="uploader-list"></div>' +
            '<div class="btns">' +
            '<div id="' + pickerid + '" style="margin-top:5px;">选择文件</div>' +
            '</div>'
 
        } else {
            uploaderStrdiv = 
            '<div  class="uploader-list"></div>' +
            '<div class="btns">' +
            '<div id="' + pickerid + '" style="float:left;font-size:12px;margin-top:5px;">选择文件</div>' +
            '<button class="webuploadbtn" style="float:left;font-size:12px;font-weight:normal;margin:5px">开始上传</button>' +
            '</div>'
        }
        uploaderStrdiv += '<div style="display:none" class="UploadhiddenInput" >\
                         </div>'
        uploaderStrdiv+='</div>';
        target.append(uploaderStrdiv);
 
        var $list = target.find('.uploader-list'),
             $btn = target.find('.webuploadbtn'),//手动上传按钮备用
             state = 'pending',
             $hiddenInput = target.find('.UploadhiddenInput'),
             uploader;
        var jsonData = {
            fileList: []
        };
 
        var webuploaderoptions = $.extend({
 
            // swf文件路径
            swf: applicationPath+'/statics/js/uploader/Uploader.swf',
            // 文件接收服务端。
            server: applicationPath+'/file/fileupload',
            deleteServer: applicationPath+'/file/filedelete',
            // 选择文件的按钮。可选。
            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
            pick: '#' + pickerid,
            //不压缩image, 默认如果是jpeg，文件上传前会压缩一把再上传！
            resize: false,
            accept: opts.accept,
            fileNumLimit: opts.fileNumLimit,
            fileSizeLimit: opts.fileSizeLimit,
            fileSingleSizeLimit: opts.fileSingleSizeLimit
        },
        opts.innerOptions);
        var uploader = WebUploader.create(webuploaderoptions);
 
        //回发时还原hiddenfiled的保持数据
        var fileDataStr = hdFileData.val();
        if (fileDataStr && opts.PostbackHold) {
            jsonData = JSON.parse(fileDataStr);
            $.each(jsonData.fileList, function (index, fileData) {
                var newid = SuiJiNum();
                fileData.queueId = newid;
                $list.append('<div id="' + newid + '" class="item" style="float:left;padding:3px;margin_top:5px;">' +
                '<div class="info">' + fileData.name + '</div>' +
                '<div class="state">已上传</div>' +
                '<div class="del"></div>' +
                '</div>');
            });
            hdFileData.val(JSON.stringify(jsonData));
        }
 
 
 
        if (opts.auto) {
            
            uploader.on('fileQueued', function (file) {
                $list.append('<div id="' + $(item)[0].id + file.id + '" class="item" style="float:left;padding:3px;margin_top:5px;">' +
                   '<span class="webuploadinfo" style="font-size:12px">' + file.name + '</span>' +
                   '<span class="webuploadstate" style="font-size:12px">正在上传...</span>' +
                   '<div class="webuploadDelbtn">删除</div><br />' +
               '</div>');
                uploader.upload();
            });
        } else {
            uploader.on('fileQueued', function (file) {//队列事件
                $list.append('<div id="' + $(item)[0].id + file.id + '" class="item" style="float:left;padding:3px;margin_top:5px;">' +
                    '<span class="webuploadinfo" style="font-size:12px">' + file.name + '</span>' +
                    '<span class="webuploadstate" style="font-size:12px">等待上传...</span>' +
                    '<div class="webuploadDelbtn">删除</div><br />' +
                '</div>');
            });
        }
       
        
        uploader.on('uploadProgress', function (file, percentage) {//进度条事件
            var $li = target.find('#' + $(item)[0].id + file.id),
                $percent = $li.find('.progress .bar');
 
            // 避免重复创建
            if (!$percent.length) {
                $percent = $('<span class="progress">' +
                    '<span  class="percentage"><span class="text"></span>' +
                  '<span class="bar" role="progressbar" style="width: 0%">' +
                  '</span></span>' +
                '</span>').appendTo($li).find('.bar');
            }
 
            $li.find('span.webuploadstate').html('上传中');
            $li.find(".text").text(Math.round(percentage * 100) + '%');
            $percent.css('width', percentage * 100 + '%');
        });
        uploader.on('uploadSuccess', function (file, response) {//上传成功事件
            if (response.state == "error") {
                target.find('#' + $(item)[0].id + file.id).find('span.webuploadstate').html(response.message);
            } else {
                target.find('#' + $(item)[0].id + file.id).find('span.webuploadstate').html('已上传');
                $hiddenInput.append('<input type="text" id="hiddenInput'+$(item)[0].id + file.id + '" class="hiddenInput" value="' + 

response.fileId + '" />');
                uploader.options.onComplete($(item)[0]);
            }
 
 
        });
 
        uploader.on('uploadError', function (file) {
            target.find('#' + $(item)[0].id + file.id).find('span.webuploadstate').html('上传出错');
        });
 
        uploader.on('uploadComplete', function (file) {//全部完成事件
            target.find('#' + $(item)[0].id + file.id).find('.progress').fadeOut();
            uploader.options.onAllComplete($(item)[0]);
        });
 
        uploader.on('all', function (type) {
            if (type === 'startUpload') {
                state = 'uploading';
            } else if (type === 'stopUpload') {
                state = 'paused';
            } else if (type === 'uploadFinished') {
                state = 'done';
            }
 
            if (state === 'uploading') {
                $btn.text('暂停上传');
            } else {
                $btn.text('开始上传');
            }
        });
 
        //删除时执行的方法
        uploader.on('fileDequeued', function (file) {
            
            
            var fullName = $("#hiddenInput" + $(item)[0].id + file.id).val();
            if (fullName!=null) {
                $.post(webuploaderoptions.deleteServer, { fileId: fullName }, function (data) {
                    if(data == '1') {
                        $.messager.alert('提示','删除成功!','info');
                    }
                })
            }
            $("#"+ $(item)[0].id + file.id).remove();
            $("#hiddenInput" + $(item)[0].id + file.id).remove();
            
        })
 
        //多文件点击上传的方法
        $btn.on('click', function () {
            if (state === 'uploading') {
                uploader.stop();
            } else {
            	 if($(".item").text() == ""){
                 	$.messager.alert('提示',"请先选择所要上传的文件",'info');
                 }
                uploader.upload();
            }
        });
 
        //删除
        $list.on("click", ".webuploadDelbtn", function () {
 
            var $ele = $(this);
            var id = $ele.parent().attr("id");
            var id = id.replace($(item)[0].id, "");
 
            var file = uploader.getFile(id);
 
            if(file) {
                uploader.removeFile(file);
            } else {
                $ele.parent().remove();
            }
        });
 
    }
    $.fn.LoadFiles = function (fileName) {
        var $list = $(this).find('.uploader-list');
        $list.append('<div id="' + SuiJiNum() + '" class="item" style="float:left;padding:3px;margin_top:5px;">' +
                '<span class="webuploadinfo" style="font-size:12px">' + fileName + '</span>' +
                '<span class="webuploadstate" style="font-size:12px">已上传</span>' +
                '<div class="webuploadDelbtn">删除</div><br />' +
            '</div>');
    }
    $.fn.GetFilesAddress = function (options) {
        var ele = $(this);
        var filesdata = ele.find(".UploadhiddenInput");
        var filesAddress = [];
        filesdata.find(".hiddenInput").each(function () {
            filesAddress.push($(this).val());
        })
        return filesAddress;
        
    }
    $.fn.GetFileUrl = function (url) {
        var imgFileNetPath = $(window).data("imgFileNetPath");
       if (!imgFileNetPath && imgFileNetPath != '') {
           $.ajax({ url: applicationPath+"/file/getFileNetPath", async: false, success: function(){
               imgFileNetPath = data;
               $(window).data("imgFileNetPath", imgFileNetPath); 
             }});
       }
       
        var rootUrl = '';
        var urlProfix = '';
        if(imgFileNetPath == '') {
            urlProfix = window.applicationPath;
            urlProfix = urlProfix.substring(0, urlProfix.length-1);
        } else {
            urlProfix = imgFileNetPath;
        }
        rootUrl = urlProfix + url;
        return rootUrl;
    }
 
    $.fn.powerWebUpload = function (options) {
        var ele = this;
        if (typeof WebUploader == 'undefined') {
            var casspath = applicationPath + "/css/uploader/webuploader.css";
            $("<link>").attr({ rel: "stylesheet", type: "text/css", href: casspath }).appendTo("head");
            var jspath = applicationPath + "/js/uploader/webuploader.min.js";
            $.getScript(jspath) .done(function() {
                initWebUpload(ele, options);
            })
            .fail(function() {
                alert("请检查webuploader的路径是否正确!")
            });
           
        }
        else {
            initWebUpload(ele, options);
        }
    }
})(jQuery, window);