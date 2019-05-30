$.namespace('smart.utils');

/**
 * layer插件工具类
 */
$.define('smart.utils.layerUtil', {
    title:"提示",
    /**
     * 打开新窗口
     * @param options
     * @param callback
     */
    openWin: function (options, callback) {
        var defaultParam = {
            title: "新页面",
            url: null,
            area: ["600px", "400px"],
            data: null,
            isParent:false,
        };
        options = $.extend(defaultParam, options);
        var ctrl = layer;
        if(options.isParent){
            ctrl = parent.layer;
        }
        //绑定传值
        if (options.data) {
            ctrl.componentData = options.data;
        }
        //iframe窗
        var index = ctrl.open({
            type: 2,
            title: options.title,
            shadeClose: false, //点击遮罩关闭层
            shade: 0.2,
            area: options.area,
            //iframe的url，no代表不显示滚动条
            content: [options.url, 'yes'],
            resize:false,
        });
    },
    /**
     * 关闭窗口
     */
    closeWin: function () {
        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    },
    /**
     * 获取控件数据
     * @returns {null|*}
     */
    getComponentData: function (clone) {
        clone = clone || false;
        var data = parent.layer.componentData || {};
        if (clone) {
            data = this.deepClone(data);
        }
        return data;
    },
    /**
     * 获取控件数据
     * @returns {null|*}
     */
    deepClone: function (data) {
        return JSON.parse(JSON.stringify(data));
    },
    /**
     * 重试
     * @param counter 重试次数
     * @param callback 回调
     * @param interval 间隔
     */
    retry: function (counter, callback,interval) {
        var me = this;
        interval = interval || 500;
        setTimeout(function () {
            try {
                counter--;
                if (callback) {
                    callback();
                }
            } catch (ex) {
                if (counter > 0) {
                    me.retry(counter,callback,interval);
                }
            }
        }, interval);
    },
    error :function (msg) {
        this.alert(msg);
        throw new Error(msg);
    },
    alert : function (msg,yes) {
        var index =  layer.alert(msg,{icon:0,title:this.title},function () {
            if (yes){
                yes(index);
            }else{
                layer.close(index);
            }
        });
    },
    success : function (msg,yes) {
        var index = layer.alert(msg,{icon:1,title:this.title},function () {
            if (yes){
                yes(index);
            }else{
                layer.close(index);
            }
        });
    },
    /**
     * 数据验证
     */
    dataValid: function (entity,rules) {
        var me = this;
        rules.forEach(function (rule) {
            if (rule.valid && !rule.valid(entity)) return;
            if (rule.type == "empty") {
                //空验证
                if (logistics.utils.validate.isNull(entity[rule.field])) {
                    me.error(rule.msg);
                }
            }
        })
    },
    formatTitle:function(value){
        return "<span title='" + value + "'>" + value + "</span>";
    },
    /**
     * post请求
     * @param url
     * @param data
     * @param success
     * @param error
     */
    post: function (options) {
        var me = this;
        var params = $.extend({
            url: null,
            data: null,
            success: null,
            error: null,
            shade: true,
            dataType: "json",
        }, options);
        var ajaxParam = {
            type: "post",
            url: rootPath + params.url,
            success: function (data) {
                if (data.success == null || data.success == "1") {
                    if (params.success) {
                        params.success(data);
                    }
                } else {
                    layer.alert(data.message,{icon:2,title:me.title});
                }
            },
            complete: function (XMLHttpRequest, textStatus) {
                if (index) {
                    layer.close(index);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                var err = {};
                err.XMLHttpRequest = XMLHttpRequest;
                if (XMLHttpRequest) {
                    switch (XMLHttpRequest.status) {
                        case 400:
                            err.message = '请求错误(400)';
                            break;
                        case 401:
                            err.message = '未授权，请重新登录(401)';
                            break;
                        case 403:
                            err.message = '拒绝访问(403)';
                            break;
                        case 404:
                            err.message = '请求出错(404)';
                            break;
                        case 408:
                            err.message = '请求超时(408)';
                            break;
                        case 500:
                            err.message = '服务器错误(500)';
                            break;
                        case 501:
                            err.message = '服务未实现(501)';
                            break;
                        case 502:
                            err.message = '网络错误(502)';
                            break;
                        case 503:
                            err.message = '服务不可用(503)';
                            break;
                        case 504:
                            err.message = '网络超时(504)';
                            break;
                        case 505:
                            err.message = 'HTTP版本不受支持(505)';
                            break;
                        default:
                            var msg = XMLHttpRequest.status;
                            err.message = '连接出错(' + msg +')!';
                    }
                } else {
                    err.message = '连接服务器失败!'
                }
                if (params.error) {
                    params.error(err);
                } else {
                    layer.alert(err.message,{icon:2,title: me.title});
                }
            }
        };
        if (params.dataType == "json") {
            $.extend(ajaxParam,{
                dataType: "json",
                contentType: 'application/json',
                data: JSON.stringify(params.data),
            });
        }else if (params.dataType == "formData") {
            $.extend(ajaxParam,{
                processData: false,
                contentType: false,
                cache: false,
                data: params.data,
            });
        }else {
            $.extend(ajaxParam,{
                contentType: 'application/x-www-form-urlencoded',
                data: params.data,
            });
        }
        var index = null;
        if (params.shade) {
            index = layer.load(1, {shade: 0.2});
        }
        $.ajax(ajaxParam);
    }
}, true);