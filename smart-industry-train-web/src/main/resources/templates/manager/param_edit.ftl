<#include "../base/_base.ftl"/>
<@layout;section>
<#if section="title"> 分组编辑
<#elseif section="css">
<#elseif section="content">
<div class="page-content mini-model">
    <form id="form-main" onsubmit="javascript: return false;">
        <input type="hidden" name="id" value="${db.id!}" />
        <div class="mini-model-body bottom40">
            <div class="form-group padding10">
                <label class="col-2 textRight noPadding-right red">分组名：</label>
                <div class="col-4">
                 	<input type="text" name="name" value="${db.name!}" required />
                </div>
                <label class="col-2 textRight noPadding-right red">类型：</label>
                <div class="col-4">
                    <#if db.Type??>
	                     <select class="form-control" name="paramType" required>
	                        <option value="String" <#if db.Type=="String">selected</#if>>字符串</option>
	                        <option value="Long" <#if db.Type=="Long">selected</#if>>整型</option>
	                        <option value="Double" <#if db.Type=="Double">selected</#if>>浮点型</option>
	                        <option value="Date" <#if db.Type=="Date">selected</#if>>日期</option>
                    	 </select>
	                 <#else>
	                	 <select class="form-control" name="paramType" required>
	                        <option value="String">字符串</option>
	                        <option value="Long">整型</option>
	                        <option value="Double">浮点型</option>
	                        <option value="Date">日期</option>
                    	 </select>
	                 </#if>
                </div>
            </div>
            <div class="form-group padding10">
            	<label class="col-2 textRight noPadding-right">默认值：</label>
                <div class="col-10">
                 	<input type="text" name="defaultVal" value="${db.defaultVal!}"/>
                </div>
            </div>
            <div class="form-group padding10">
                <label class="col-2 textRight noPadding-right">备注：</label>
                <div class="col-10">
                    <textarea style="width:100%;" rows="3" name="remark">${db.remark!}</textarea>
                </div>
            </div>
        </div>
        <div class="mini-model-footer">
            <div class="textRight padding5">
                <button class="btn btn-sm btn-primary btn-round" onclick="param.edit.OK()">保存</button>
                <a id="close" class="btn btn-sm btn-default btn-round" onclick="SmartMonitor.Common.Close()">取消</a>
            </div>
        </div>
    </form>
</div>
<#elseif section="scripts">
	 <script type="text/javascript">
	     Zq.Utility.RegisterNameSpace("param.edit");
		//闭包引入命名空间
		(function (ns, undefined) {
		    //新增方法
		    ns.OK = function () {
		        var url = "/param/edit";
		        if (SmartMonitor.Common.GetMode($("#form-main")) === "add") {
		            url = "/param/add";
		        }
                if (!Smart.Common.FormValid()) return false;
		
		        var obj = $("#form-main").serializeObject();
		        $.ajax({
		            async: false,
		            type: "post",
		            dataType:"json",
		            url: url.geturl(),
		            data: obj,
		            success: function (result) {
		                SmartMonitor.Common.Close(true);
		            }
		        });
		    };
		})(param.edit);
	  </script>
</#if>
</@layout>
