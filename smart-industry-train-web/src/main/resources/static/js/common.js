Zq.Utility.RegisterNameSpace("Smart.Common");
(function(ns,common){
	ns.GetUrl = function(url){
    	var contextPath = $("#hid_contextpath").val();
    	return contextPath+url;
    }
	/**
     * 标题格式化
     */
    ns.TitleFormat = function (value, row, index, field) {
        return {
            title: value
        };
    }
    /**
     * Form表单验证
     */
    ns.FormValid = function(formId){
    	if(formId==null){
            formId = "#form-main";
		}
    	var $list = $("input[type='text'][required]",formId);
    	for(var i =0;i<$list.length;i++){
    		if($list.eq(i).val()==="") return false;
    	}
    	//校验最小值
    	$list = $('input[type="number"][min]');
    	for(var i =0;i<$list.length;i++){
    		var val = $list.eq(i).val();
    		if(val==="") continue;
    		if(val < $list.eq(i).prop("min")) return false;
    	}
    	//校验最大值
    	$list = $('input[type="number"][max]');
    	for(var i =0;i<$list.length;i++){
    		var val = $list.eq(i).val();
    		if(val==="") continue;
    		if(val > $list.eq(i).prop("max")) return false;
    	}
    	return true;
    }

    /**
	 * 获取公选页面
     */
	//选择岗位
    ns.selClass = function (callback,data) {
        common.SetData(data);
        Zq.Utility.OpenModal({
            title: "选择分类",
            maxmin: false,
            area: ['350px', '450px'],
            content: [ns.GetUrl('/common/selclass'), 'yes'],
            end: function () {
                common.End(callback);
            }
        });
    }
    ns.selectOrg = function (callback,data) {
    	common.SetData(data);
        Zq.Utility.OpenModal({
            title: "选择组织",
            maxmin: false,
            area: ['350px', '450px'],
            content: [ns.GetUrl('/common/selectOrg'), 'yes'],
            end: function () {
                common.End(callback);
            }
        });
    }
})(Smart.Common,SmartMonitor.Common)

/**
 * 方法扩展，获取url
 */
String.prototype.geturl = function() {
	return Smart.Common.GetUrl(this);
}

$(function(){
    /**
     * ajax遮罩层处理
     */
    $(document).ajaxStart(function(){
        SmartMonitor.Common.loadIndex = layer.load(0,{
            shade: [0.1,'#000'] //0.1透明度的白色背景
        });
    }).ajaxStop(function(){
        layer.close(SmartMonitor.Common.loadIndex)
    });
});