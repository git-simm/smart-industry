<#include "../base/_base.ftl"/>
<@layout;section>
    <#if section="title"> 设计方案编辑
    <#elseif section="css">
        <@cssRef url="/static/_resources/uploader/webuploader.css"/>
        <@cssRef url="/static/css/myuploader.css"/>
    <#elseif section="content">
<div class="page-content mini-model">
    <form id="form-main" onsubmit="javascript: return false;">
        <input type="hidden" name="id" value="${entity.id!}"/>
        <div class="mini-model-body bottom40">
            <div class="form-group padding10">
                <label class="col-2 textRight noPadding-right red">名称：</label>
                <div class="col-4">
                    <input type="text" name="name" value="${entity.name!}" required/>
                </div>
                <label class="col-2 textRight noPadding-right red">分类：</label>
                <div class="col-4">
                    <div class="input-group">
                        <input type="hidden" name="classId" value="${entity.classId!}" />
                        <input type="text" id="className" value="${className!}" required/>
                        <span class="input-group-btn" onclick="solution.edit.selectClass()">
                            <i class="icon-search"></i>
                        </span>
                    </div>
                </div>
            </div>
            <div class="tabbable tabs-left marginTop30">
                <ul class="nav nav-tabs" id="myTab3">
                    <li class="active">
                        <a data-toggle="tab" href="#home3" aria-expanded="true">
                            <i class="pink ace-icon fa fa-tachometer bigger-110"></i>
                            基准
                        </a>
                    </li>
                    <li class="">
                        <a data-toggle="tab" href="#profile3" aria-expanded="false">
                            <i class="blue ace-icon fa fa-user bigger-110"></i>
                            设计
                        </a>
                    </li>
                    <li class="">
                        <a data-toggle="tab" href="#dropdown13" aria-expanded="false">
                            <i class="ace-icon fa fa-rocket"></i>
                            清单
                        </a>
                    </li>
                </ul>
                <div class="tab-content" style="height: 350px;">
                    <div id="home3" class="tab-pane active">
                        <div id="uploader1" class="uploader wu-example">
                            <!--用来存放文件信息-->
                            <div class="uploader-list">
                                <#if type1??>
                                    <#list type1 as file>
                                        <div class="dd-handle" fileid="${file.id}">
                                            ${file.name!}
                                            <div class="pull-right action-buttons">
                                                <a class="red" href="#" onclick="solution.edit.delfile(${file.id})">
                                                    <i class="icon-trash bigger-130"></i>
                                                </a>
                                            </div>
                                        </div>
                                    </#list>
                                </#if>
                            </div>
                            <div id="thelist1" class="uploader-list"></div>
                            <div class="btns">
                                <div id="picker1" class="mypicker">选择文件</div>
                            </div>
                        </div>
                    </div>
                    <div id="profile3" class="tab-pane">
                        <div id="uploader2" class="uploader wu-example">
                            <div class="uploader-list">
                                <#if type2??>
                                    <#list type2 as file>
                                    <div class="dd-handle" fileid="${file.id}">
                                        ${file.name!}
                                        <div class="pull-right action-buttons">
                                            <a class="red" href="#" onclick="solution.edit.delfile(${file.id})">
                                                <i class="icon-trash bigger-130"></i>
                                            </a>
                                        </div>
                                    </div>
                                    </#list>
                                </#if>
                            </div>
                            <!--用来存放文件信息-->
                            <div id="thelist2" class="uploader-list"></div>
                            <div class="btns">
                                <div id="picker2" class="mypicker">选择文件</div>
                            </div>
                        </div>
                    </div>
                    <div id="dropdown13" class="tab-pane">
                        <div id="uploader3" class="uploader wu-example">
                            <div class="uploader-list">
                                <#if type3??>
                                    <#list type3 as file>
                                    <div class="dd-handle" fileid="${file.id}">
                                        ${file.name!}
                                        <div class="pull-right action-buttons">
                                            <a class="red" href="#" onclick="solution.edit.delfile(${file.id})">
                                                <i class="icon-trash bigger-130"></i>
                                            </a>
                                        </div>
                                    </div>
                                    </#list>
                                </#if>
                            </div>
                            <!--用来存放文件信息-->
                            <div id="thelist3" class="uploader-list"></div>
                            <div class="btns">
                                <div id="picker3" class="mypicker">选择文件</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="mini-model-footer">
            <div class="textRight padding5">
                <button class="btn btn-sm btn-primary btn-round" onclick="solution.edit.OK()">保存</button>
                <a id="close" class="btn btn-sm btn-default btn-round" onclick="SmartMonitor.Common.Close()">取消</a>
            </div>
        </div>
    </form>
</div>
    <#elseif section="scripts">
        <@jsRef "/static/_resources/uploader/webuploader.js"/>
        <@jsRef "/static/js/common/Uploader.js"/>
	 <script type="text/javascript">
         Zq.Utility.RegisterNameSpace("solution.edit");
         //闭包引入命名空间
         (function (ns, undefined) {
             //新增方法
             ns.OK = function () {
                 if (!Smart.Common.FormValid()) return false;
                 var obj = $("#form-main").serializeObject();
                 if (SmartMonitor.Common.GetMode($("#form-main")) === "add") {
                     $.ajax({
                         async: false,
                         type: "Post",
                         dataType: "json",
                         url: "/solution/add".geturl(),
                         data: obj,
                         success: function (id) {
                             //文件上传
                             ns.upload(id);
                         }
                     });
                 } else {
                     var param = {solution: obj, fileIds: dels}
                     $.ajax({
                         async: false,
                         type: "Post",
                         dataType: "json",
                         contentType: 'application/json',
                         url: "/solution/edit".geturl(),
                         data: JSON.stringify(param),
                         success: function () {
                             //文件上传
                             ns.upload(obj.id,function () {
                                 Zq.Utility.Msg("保存成功",function(){
                                     SmartMonitor.Common.Close(true);
                                 });
                             });
                         }
                     });
                 }
             };

             var accept = {
                 title: 'intoTypes',
                 extensions: 'rar,zip,doc,xls,docx,xlsx,pdf,dxf',
                 mimeTypes: '.rar,.zip,.doc,.xls,.docx,.xlsx,.pdf,.dxf'
             };
             var uploader1 = new myUploader({
                 list: $('#thelist1'),
                 btn: $('#ctlBtn1'),
                 pick: '#picker1',
                 fileType: 1,
                 accept: accept
             });
             var uploader2 = new myUploader({
                 list: $('#thelist2'),
                 btn: $('#ctlBtn2'),
                 pick: '#picker2',
                 fileType: 2,
                 accept: accept
             });
             var uploader3 = new myUploader({
                 list: $('#thelist3'),
                 btn: $('#ctlBtn3'),
                 pick: '#picker3',
                 fileType: 3,
                 accept: accept
             });
             /**
              * 触发文件上传
              */
             ns.upload = function (id,callback) {
                 var r1 = false,r2 = false,r3 = false;
                 uploader1.upload(id,function(){
                     r1 = true;
                 });
                 uploader2.upload(id,function(){
                     r2 = true;
                 });
                 uploader3.upload(id,function(){
                     r3 = true;
                 });
                 //上传完毕
                 var interval = setInterval(function(){
                     if(r1 && r2 && r3){
                         //清理掉定时器
                         clearInterval(interval);
                         //发起请求,统计解决方案对应的文件信息
                         fileSummary(id,callback);
                     }
                 }, 500);
             }
             /**
             * 文件统计
             */
             function fileSummary(id,callback){
                 $.ajax({
                     async: false,
                     type: "Post",
                     dataType: "json",
                     url: "/solution/fileSummary".geturl(),
                     data: {id:id},
                     success: function () {
                        if(callback){
                            callback();
                        }
                     }
                 });
             }
             /**
              * 删除文件
              * @type {Array}
              */
             var dels = [];
             ns.delfile = function (id) {
                 //删除文件
                 $("div[fileid='" + id + "']").remove();
                 dels.push(id);
             }

             /**
              * 设置选中的节点信息
              * node
              */
             var classNode = {};
             ns.setData = function (node) {
                 if($("input[name='classId']").val()==""){
                     $("#className").val(classNode.name);
                     $("input[name='classId']").val(classNode.id);
                     classNode = node;
                 }else{
                     classNode = {id:$("input[name='classId']").val(),name:$("#className").val()};
                 }
             }

             //选择分类
             ns.selectClass = function(){
                 Smart.Common.selClass(function(node){
                     $("input[name='classId']").val(node.id);
                     $("#className").val(node.name);
                     classNode = node;
                 },classNode);
             }
         })(solution.edit);
         $(function () {
             //调整按钮大小
             setTimeout(function () {
                 $("div[id^='rt_rt_']").css({width: 72, height: 29});
             }, 1000);
         });
     </script>
    </#if>
</@layout>
