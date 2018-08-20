<#include "../base/_base.ftl"/>
<@layout;section>
<#if section="title"> 用户编辑
<#elseif section="css">
<#elseif section="content">
<div class="page-content mini-model">
    <form id="form-main" onsubmit="javascript: return false;">
        <div class="mini-model-body bottom40">
            <div class="form-group padding10">
                <label class="col-3 textRight noPadding-right red">原密码：</label>
                <div class="col-9">
                 	<input type="password" name="psw" required />
                </div>
            </div>
            <div class="form-group padding10">
                <label class="col-3 textRight noPadding-right red">新密码：</label>
                <div class="col-9">
                    <input type="password" name="newpsw" required />
                </div>
            </div>
            <div class="form-group padding10">
                <label class="col-3 textRight noPadding-right red">确认密码：</label>
                <div class="col-9">
                    <input type="password" name="confirmpsw" required />
                </div>
            </div>
        </div>
        <div class="mini-model-footer">
            <div class="textRight padding5">
                <button class="btn btn-sm btn-primary btn-round" onclick="user.edit.OK()">保存</button>
                <a id="close" class="btn btn-sm btn-default btn-round" onclick="SmartMonitor.Common.Close()">取消</a>
            </div>
        </div>
    </form>
</div>
<#elseif section="scripts">
	 <script type="text/javascript">
	     Zq.Utility.RegisterNameSpace("user.edit");
		//闭包引入命名空间
		(function (ns, undefined) {
		    //新增方法
		    ns.OK = function () {
                if(!Smart.Common.FormValid()) return false;
		        var obj = $("#form-main").serializeObject();
		        if(obj.newpsw!=obj.confirmpsw){
		            layer.alert("新密码与确认密码不匹配，请重新输入");
		            $("input[name='newpsw']").focus();
		            return false;
                }
		        $.ajax({
		            async: false,
		            type: "Post",
		            dataType:"json",
		            url: "/updatepsw".geturl(),
		            data: obj,
		            success: function (result) {
		                SmartMonitor.Common.Close(true);
		            }
		        });
		    };
		})(user.edit);
	  </script>
</#if>
</@layout>
