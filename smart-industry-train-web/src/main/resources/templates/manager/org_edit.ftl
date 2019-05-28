<#include "../base/_base.ftl"/>
<@layout;section>
    <#if section="title"> 组织架构编辑
    <#elseif section="css">
<style type="text/css">
    .radio {
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
        <input type="hidden" name="id" value="${entity.id!}"/>
        <div class="mini-model-body bottom40">
            <div class="form-group padding10">
                <label class="col-2 textRight noPadding-right red">名称：</label>
                <div class="col-4">
                    <input type="text" name="name" value="${entity.name!}" onKeyUp="org.edit.getCode(this)" required/>
                </div>
                <label class="col-2 textRight noPadding-right red">编码：</label>
                <div class="col-4">
                    <input type="text" name="code" value="${entity.code!}" required/>
                </div>
            </div>
            <div class="form-group padding10">
                <label class="col-2 textRight noPadding-right red">部门：</label>
                <div class="col-4">
                    <div class="input-group">
                        <input type="text" name="pName" value="${entity.pName!}" readonly required/>
                        <input type="hidden" name="pId" value="${entity.pId!}"/>
                        <span class="input-group-btn" onclick="org.edit.selectOrg()">
                            <i class="icon-search"></i>
                        </span>
                    </div>
                </div>
                <label class="col-2 textRight noPadding-right red">排序码：</label>
                <div class="col-4">
                    <input type="text" name="sort" value="${entity.sort!}" required/>
                </div>
            </div>
            <div class="form-group padding10">
                <label class="col-2 textRight noPadding-right red">类型：</label>
                <div class="col-5">
					<#if entity.orgType??>
                        <input class="radio width-20" type="radio" name="orgType" value="0"
                               <#if entity.orgType==0>checked="checked"</#if> />公司
                        <input class="radio width-20" type="radio" name="orgType" value="1"
                               <#if entity.orgType==1>checked="checked"</#if>/>部门
                        <input class="radio width-20" type="radio" name="orgType" value="2"
                               <#if entity.orgType==2>checked="checked"</#if>/>工作组
                    <#else>
						<input class="radio width-20" type="radio" name="orgType" value="0" checked="checked"/>公司
                    	<input class="radio width-20" type="radio" name="orgType" value="1"/>部门
                        <input class="radio width-20" type="radio" name="orgType" value="2"/>工作组
                    </#if>
                </div>
            </div>
            <div class="form-group padding10">
                <label class="col-2 textRight noPadding-right">备注：</label>
                <div class="col-10">
                    <input type="text" name="remark" value="${entity.remark!}"/>
                </div>
            </div>
        </div>
        <div class="mini-model-footer">
            <div class="textRight padding5">
                <button class="btn btn-sm btn-primary btn-round" onclick="org.edit.OK()">保存</button>
                <a id="close" class="btn btn-sm btn-default btn-round" onclick="SmartMonitor.Common.Close()">取消</a>
            </div>
        </div>
    </form>
</div>
    <#elseif section="scripts">
     <script type="text/javascript" charset="GBK"
             src="${request.contextPath}${urls.getForLookupPath("/static/_resources/common/pinying.js")}"></script>
	 <script type="text/javascript">
         Zq.Utility.RegisterNameSpace("org.edit");
         //闭包引入命名空间
         (function (ns, undefined) {
             ns.parent = {};
             //选择分类
             ns.selectOrg = function () {
                 Smart.Common.selectOrg(function (node) {
                     $("input[name='pName']").val(node.name);
                     $("input[name='pId']").val(node.id);
                 }, ns.parent);
             },
                     //获取编码
                     ns.getCode = function (obj) {
                         var codes = makePy($(obj).val());
                         $("input[name='code']").val(codes[0]);
                     },
                     //新增方法
                     ns.OK = function () {
                         var url = "/org/edit";
                         if (SmartMonitor.Common.GetMode($("#form-main")) === "add") {
                             url = "/org/add";
                         }
                         if (!Smart.Common.FormValid()) return false;

                         var obj = $("#form-main").serializeObject();
                         $.ajax({
                             async: false,
                             type: "Post",
                             dataType: "json",
                             //contentType: 'application/json',
                             url: url.geturl(),
                             data: obj,
                             success: function (result) {
                                 SmartMonitor.Common.Close(true);
                             }
                         });
                     };
         })(org.edit);
         /**
          * 初始化方法
          */
         $(function () {
             var selected = SmartMonitor.Common.GetData();
             if (selected && selected.name) {
                 $("input[name='pName']").val(selected.name);
                 $("input[name='pId']").val(selected.id);
                 $("input[name='orgType']").removeAttr("checked");
                 $("input[name='orgType'][value=" + selected.newType + "]").prop("checked", 'checked');
             }
         });
     </script>
    </#if>
</@layout>
