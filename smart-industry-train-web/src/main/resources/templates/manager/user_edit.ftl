<#include "../base/_base.ftl"/>
<@layout;section>
<#if section="title"> 用户编辑
<#elseif section="css">
<style type="text/css">
	.radio{
		display: inline;
		vertical-align: middle;
	}
    .radio + .radio, .checkbox + .checkbox {
        margin-top: 4px;
    }
</style>
<#elseif section="content">
<div class="page-content mini-model">
    <form id="form-main" onsubmit="javascript: return false;">
        <input type="hidden" name="id" value="${entity.id!}" />
        <div class="mini-model-body bottom40">
            <div class="form-group padding10">
                <label class="col-2 textRight noPadding-right red">姓名：</label>
                <div class="col-4">
                 	<input type="text" name="name" value="${entity.name!}" required />
                </div>
                <label class="col-2 textRight noPadding-right red">登录名：</label>
                <div class="col-4">
                    <input type="text" name="code" value="${entity.code!}" required />
                </div>
            </div>
            <div class="form-group padding10">
                <label class="col-2 textRight noPadding-right red">密码：</label>
                <div class="col-4">
                <#if entity.sex??>
                    <input type="password" name="psw" value="${entity.psw!}" required />
                <#else>
                    <input type="text" name="psw" value="123456" required />
                </#if>
                </div>
                <label class="col-2 textRight noPadding-right red">性别：</label>
                <div class="col-4">
					<#if entity.sex??>
                    	<input class="radio width-20" type="radio" name="sex" value="true" <#if entity.sex>checked="checked"</#if> />男
                    	<input class="radio width-20" type="radio" name="sex" value="false" <#if !entity.sex>checked="checked"</#if>/>女
					<#else>
						<input class="radio width-20" type="radio" name="sex" value="true" checked="checked" />男
                    	<input class="radio width-20" type="radio" name="sex" value="false" />女
					</#if>
                </div>
            </div>
            <div class="form-group padding10">
                <label class="col-2 textRight noPadding-right">备注：</label>
                <div class="col-10">
                	 <textarea style="width:100%" rows="3" type="text" name="remark">${entity.remark!}</textarea>
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
		        var url = "/user/edit";
		        if (SmartMonitor.Common.GetMode($("#form-main")) === "add") {
		            url = "/user/add";
		        }
                if(!Smart.Common.FormValid()) return false;
		
		        var obj = $("#form-main").serializeObject();
		        $.ajax({
		            async: false,
		            type: "Post",
		            dataType:"json",
					contentType: 'application/json',
		            url: url.geturl(),
		            data: JSON.stringify(obj),
		            success: function (result) {
		                SmartMonitor.Common.Close(true);
		            }
		        });
		    };
		})(user.edit);
	  </script>
</#if>
</@layout>
