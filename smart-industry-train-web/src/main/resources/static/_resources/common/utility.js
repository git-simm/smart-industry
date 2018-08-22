window.Zq = window.Zq || {};
window.Zq.Utility = window.Zq.Utility || {};

//Jquery serializeArray()方法扩充，将FORM内容拼成json字符串
//用法：var formJson = $("#"+formId).serializeObject();
(function ($) {
    $.fn.serializeObject = function (strSplit) {
        var o = {};
        var a = this.serializeArray();
        $.each(a, function () {
            if (strSplit != null) {
                var nameArr = this.name.split(strSplit);
                if (nameArr.length > 1) {
                    this.name = nameArr[1];
                }
            }
            //if ($.trim(this.value) != '') {//空值也需要收集
            if (o[this.name]) {
                if (!o[this.name].push) {
                    o[this.name] = [o[this.name]];
                }
                o[this.name].push(this.value || '');
            } else {
                o[this.name] = this.value || '';
            }
            //}
        });
        return o;
    };
})(jQuery);

(function (ns, undefined) {
    ns.RegisterNameSpace = function (nameSpace) {
        /// <summary>注册命名空间</summary>
        /// <param name="nameSpace" type="String">需要注册的命名空间字符串，比如 Mysoft.IT.Utility</param>
        if (!nameSpace) {
            return window;
        }

        var arr = nameSpace.split('.');
        var ns = window;
        for (var i = 0; i < arr.length; ++i) {
            var name = arr[i];
            if (i == 0 && name === 'window') {
                // 对 window.window 赋值会导致 IE8 下出错
                continue;
            }
            ns[name] = ns[name] || {};
            ns = ns[name];
        }
        return ns;
    };

    //added by simm 2014.09.02  回车事件
    ns.EnterEventRegister = function (obj, callback, preCallBack) {
        $(obj).on("keyup", function (e) {
            var ev = document.all ? window.event : e;
            if (ev.keyCode == 13) {
                if (preCallBack && typeof preCallBack == "function") {
                    preCallBack();
                }
                if (callback) {
                    callback();
                }
            }
        });
    }

    //数据加载中 遮罩处理
    ns.MaskBegin = function (options) {
        var title = "加载中,请稍候...";
        if (options && options.title) {
            title = options.title;
        }
        if ($("#maskLayer").length == 0) {
            $('<div id="maskLayer"></div>').appendTo("body");
        } else {
            $("#maskLayer").fadeIn(0);
        }
        $('<div class="loading"><h5>' + title + '</h5></div>').appendTo("body");
        var docE = document.documentElement;
        var b = docE ? docE : document.body,
            height = b.scrollHeight > b.clientHeight ? b.scrollHeight : b.clientHeight,
            width = b.scrollWidth > b.clientWidth ? b.scrollWidth : b.clientWidth;
        $("#maskLayer").css({
            "height": height,
            "width": width,
            "background": "#000",
            "opacity": ".3",
            "top": 0,
            "left": 0,
            "position": "absolute",
            "zIndex": "8000"
        });
        if (options && options.callback) {
            options.callback();
        }
    };
    //遮罩移除
    ns.MaskEnd = function () {
        $(".loading").remove();
        $("#maskLayer").fadeOut(300);
    }

    ns.OpenModal = function (param) {
        var parameter = $.extend({
            type: 2,
            title: "模态对话框",
            maxmin: true, //开启最大化最小化按钮
            shade: 0.3,
            end: null,
            success: null
        }, param);
        //使用layer进行弹层
        if (parent && parent.layer) {
            parent.layer.open(parameter);
        } else {
            layer.open(parameter);
        }
    }
    ns.Msg = function (msg, callback) {
        layer.msg(msg, {time: 1000}, function () {
            if (callback) {
                callback();
            }
        });
    }
    //行选中事件
    ns.RowChoosed = function (obj) {
        $(obj).on("click", "tbody>tr", function () {
            $(this).closest("tbody").find("tr.active").removeClass("active");
            $(this).addClass("active");
        });
    }
    ns.AppendNoData = function (objs) {
        var $list = $(objs);
        $list.each(function (i, obj) {
            var isShow = $(obj).attr("nodata");
            if (isShow == "false") return;
            var $childrens = $(obj).children();
            if ($childrens.length === 0) {
                $(obj).append("<div class='no-data'></div>");
            }
        });
    }
    //说话
    ns.Speak = function (text) {
        try {
            var msg = new SpeechSynthesisUtterance();
            //var voices = window.speechSynthesis.getVoices();
            //msg.voice = voices[0];
            msg.volume = 1; // 0 to 1
            msg.text = text;
            msg.lang = 'zh-CN';
            // 添加到队列
            window.speechSynthesis.speak(msg);
        } catch (e) {
            console.log("我去，无法播放声音");
            console.log(e);
        }
    }

    //下载文件
    ns.DownLoadExcel = function (obj, ajaxUrl) {
        var form = $("<form>");
        form.attr('style', 'display:none');
        form.attr('target', '');
        form.attr('method', 'post');
        form.attr('action', ajaxUrl);

        for (var prop in obj) {
            var temp = $('<input>');
            temp.attr('type', 'hidden');
            temp.attr('name', prop);
            temp.attr('value', obj[prop]);
            form.append(temp);
        }
        $('body').append(form);
        form.submit();
        form.remove();
    }

    ns.GetPath = function (path) {
        return $("#hid_contextpath").val() + path;
    }
})(Zq.Utility);


// 初始化ajax请求参数
$.ajaxSetup({
    cache: false,
    type: "post"
});

/**
 * 时间对象的格式化;
 */
Date.prototype.format = function (format) {
    /* 
     * eg:format="yyyy-MM-dd hh:mm:ss"; 
     */
    var o = {
        "M+": this.getMonth() + 1, // month  
        "d+": this.getDate(), // day  
        "h+": this.getHours(), // hour  
        "m+": this.getMinutes(), // minute  
        "s+": this.getSeconds(), // second  
        "q+": Math.floor((this.getMonth() + 3) / 3), // quarter  
        "S": this.getMilliseconds()
        // millisecond  
    }

    if (/(y+)/.test(format)) {
        format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4
            - RegExp.$1.length));
    }

    for (var k in o) {
        if (new RegExp("(" + k + ")").test(format)) {
            format = format.replace(RegExp.$1, RegExp.$1.length == 1
                ? o[k]
                : ("00" + o[k]).substr(("" + o[k]).length));
        }
    }
    return format;
}

// 统一的错误处理
$(document).ajaxError(function (event, XMLHttpRequest, ajaxOptions, thrownError) {
    if (typeof (thrownError) != "undefined") {
        if(XMLHttpRequest.responseJSON!=undefined && XMLHttpRequest.responseJSON.message!=undefined){
            layer.alert(XMLHttpRequest.responseJSON.message);
            return;
        }

        var pattern = "";
        if (/msie/.test(navigator.userAgent.toLowerCase())) {
            pattern = new RegExp("<b>Message</b> (.+)</p><p><b>Description</b>");
        } else {
            pattern = new RegExp("<b>message</b> <u>(.+)</u></p><p><b>description</b>");
        }

        if (XMLHttpRequest.responseText == undefined) {
            layer.alert(XMLHttpRequest.responseText);
        } else if (XMLHttpRequest.responseText.match(pattern) == null) {
            if (XMLHttpRequest.responseText.indexOf("用户session过期") >= 0) {
                layer.alert(XMLHttpRequest.responseText, function () {
                    window.location.reload();
                });
            } else {
                layer.alert(XMLHttpRequest.responseText);
            }
        } else {
            layer.alert(XMLHttpRequest.responseText.match(pattern)[1]);
        }
    } else {
        var error = "<b style='color: #f00'>" + XMLHttpRequest.status + "    " + XMLHttpRequest.statusText + "</b>";
        var start = XMLHttpRequest.responseText.indexOf("<title>");
        var end = XMLHttpRequest.responseText.indexOf("</title>");
        if (start > 0 && end > start)
            error += "<br /><br />" + XMLHttpRequest.responseText.substring(start + 7, end);
        layer.alert("调用服务器失败。<br />" + error);
    }
});
$(document).ajaxComplete(function () {
    Zq.Utility.AppendNoData($(".datatable"));
});