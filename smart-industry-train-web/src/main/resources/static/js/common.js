Zq.Utility.RegisterNameSpace("Cd.Common");
(function(ns,undefined){
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
    ns.FormValid = function(){
    	var $list = $("[required]","#form-main");
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
})(Cd.Common)

/**
 * 方法扩展，获取url
 */
String.prototype.geturl = function() {
	return Cd.Common.GetUrl(this);
}