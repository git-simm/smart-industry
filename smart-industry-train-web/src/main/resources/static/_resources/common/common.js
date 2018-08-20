Zq.Utility.RegisterNameSpace("SmartMonitor.Common");
//闭包引入命名空间
(function (ns, undefined) {
    ns.Data = null;
    ns.Result = null;
    ns.IsClear = false;
    //-- 弹出层 通用方法 begin -------------
    //新增方法
    ns.Clear = function () {
        parent.SmartMonitor.Common.IsClear = true;
        ns.Close(null);
    };
    //编辑方法
    ns.Close = function (obj) {
        //移动了一下位置
        var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
        parent.SmartMonitor.Common.Result = obj;
        parent.layer.close(index);
    };
    //-- 弹出层 通用方法 end -------------

    ns.GetResult = function () {
        //关闭窗口后的回调
        var result = ns.Result;
        if (result == null && parent.SmartMonitor.Common != null) {
            result = parent.SmartMonitor.Common.Result;
            //获取完值后，清理上次的结果
            parent.SmartMonitor.Common.Result = null;
        }
        //获取完值后，清理上次的结果
        ns.Result = null;
        return result;
    }

    ns.GetData = function () {
        //获取初始化数据
        var data = ns.Data;
        if (data == null && parent.SmartMonitor.Common != null) {
            data = parent.SmartMonitor.Common.Data;
        }
        return data;
    }
    ns.SetData = function (data) {
        if (parent != null && parent.SmartMonitor.Common != null) {
            parent.SmartMonitor.Common.Data = data;
        } else {
            ns.Data = data;
        }
    }

    ns.End = function (callback) {
        var result = ns.GetResult();
        if (callback) {
            if (result == null) {
                if (parent.SmartMonitor.Common.IsClear === true) {
                    //如果是置空操作，则允许返回值为空
                    callback(result);
                    parent.SmartMonitor.Common.IsClear = false;
                }
            } else {
                callback(result);
            }

        }
    }

    //选择存储
    ns.OpenModal = function (param,data) {
        ns.SetData(data);
        Zq.Utility.OpenModal(param);
    }

    /*======== 页面对象通用处理逻辑 begin ===============*/
    ns.GetMode = function (obj) {
        var mode = $(obj).closest("body").find("#_mode").val();
        //console.log(mode);
        return mode > "" ? mode.toLowerCase() : "";
    }
    /*======== 页面对象通用处理逻辑 end ===============*/
    //修改密码
    ns.ChangePsw = function () {
        Zq.Utility.OpenModal({
            title: "修改密码",
            maxmin: false,
            area: ['400px', '300px'],
            content: [ns.GetUrl('/changePsw'), 'yes'],
            end: function () {
                if (SmartMonitor.Common.GetResult() === true) {
                    alert("密码修改成功，请重新登录");
                    ns.LogOut();
                }
            }
        });
    }
    //退出系统
    ns.LogOut = function () {
        $.ajax({
            async: false,
            type: "Get",
            url: ns.GetUrl("/logout"),
            success: function () {
                //关闭窗口
                window.location = ns.GetUrl("/login");
            }
        });
    }
    ns.GetUrl = function(url){
        var contextPath = $("#hid_contextpath").val();
        return contextPath+url;
    }
})(SmartMonitor.Common);
