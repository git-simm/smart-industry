<#include "../base/_base.ftl"/>
<@layout;section>
<#if section="title"> 项编辑
<#elseif section="css">
<#elseif section="content">
<div class="page-content mini-model">
    <form id="form-main" onsubmit="javascript: return false;">
        <input type="hidden" name="id" value="${db.id!}" />
        <input type="hidden" name="groupid" value="${db.groupid!}" />
        <div class="mini-model-body bottom40">
            <div class="form-group padding10">
                <label class="col-2 textRight noPadding-right red">值：</label>
                <div class="col-10">
                 	<input type="text" style="line-height: 1!important;" name="value" value="${db.value!}" required />
                </div>
            </div>
            <div class="form-group padding10">
				<label class="col-2 textRight noPadding-right">显示：</label>
                <div class="col-4">
                    <input type="text" name="showVal" value="${db.showVal!}"/>
                </div>
                <label class="col-2 textRight noPadding-right red">排序码：</label>
                <div class="col-4">
                    <input type="text" name="sort" value="${db.sort!}" required />
                </div>
            </div>
        </div>
        <div class="mini-model-footer">
            <div class="textRight padding5">
                <button class="btn btn-sm btn-primary btn-round" onclick="option.edit.OK()">保存</button>
                <a id="close" class="btn btn-sm btn-default btn-round" onclick="SmartMonitor.Common.Close()">取消</a>
            </div>
        </div>
    </form>
</div>
<#elseif section="scripts">
	 <script type="text/javascript">
	     Zq.Utility.RegisterNameSpace("option.edit");
		//闭包引入命名空间
		(function (ns, undefined) {
		    //新增方法
		    ns.OK = function () {
                if (!Smart.Common.FormValid()) return false;
		        var url = "/param/editOption";
		        if (SmartMonitor.Common.GetMode($("#form-main")) === "add") {
		            url = "/param/addOption";
		        }
		
		        var obj = $("#form-main").serializeObject();
		        $.ajax({
		            async: false,
		            type: "Post",
		            dataType:"json",
		            url: url.geturl(),
		            data: obj,
		            success: function (result) {
		                SmartMonitor.Common.Close(true);
		            }
		        });
		    };
		})(option.edit);
	  </script>
</#if>
</@layout>