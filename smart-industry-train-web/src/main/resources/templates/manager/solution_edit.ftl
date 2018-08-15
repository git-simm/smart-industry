<#include "../base/_base.ftl"/>
<@layout;section>
<#if section="title"> 设计方案编辑
<#elseif section="css">
    <@cssRef url="/static/_resources/uploader/webuploader.css"/>
    <@cssRef url="/static/css/myuploader.css"/>
<#elseif section="content">
<div class="page-content mini-model">
    <form id="form-main" onsubmit="javascript: return false;">
        <input type="hidden" name="id" value="${entity.id!}" />
        <div class="mini-model-body bottom40">
            <div class="form-group padding10">
                <label class="col-2 textRight noPadding-right red">名称：</label>
                <div class="col-10">
                 	<input type="text" name="name" value="${entity.name!}" required />
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
                            <div id="thelist1" class="uploader-list"></div>
                            <div class="btns">
                                <div id="picker1" class="mypicker">选择文件</div>
                            </div>
                        </div>
                    </div>
                    <div id="profile3" class="tab-pane">
                        <div id="uploader2" class="uploader wu-example">
                            <!--用来存放文件信息-->
                            <div id="thelist2" class="uploader-list"></div>
                            <div class="btns">
                                <div id="picker2" class="mypicker">选择文件</div>
                            </div>
                        </div>
                    </div>
                    <div id="dropdown13" class="tab-pane">
                        <div id="uploader3" class="uploader wu-example">
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
		        var url = "/solution/edit";
		        if (SmartMonitor.Common.GetMode($("#form-main")) === "add") {
		            url = "/solution/add";
		        }
		        var obj = $("#form-main").serializeObject();
		        $.ajax({
		            async: false,
		            type: "Post",
		            dataType:"json",
		            url: url.geturl(),
		            data: obj,
		            success: function (id) {
                        //文件上传
                        ns.upload(id);
		                //SmartMonitor.Common.Close(true);
		            }
		        });
		    };
            var accept = {
                title: 'intoTypes',
                extensions: 'rar,zip,doc,xls,docx,xlsx,pdf',
                mimeTypes: '.rar,.zip,.doc,.xls,.docx,.xlsx,.pdf'
            };
            var uploader1 = new myUploader({
                list: $('#thelist1'),
                btn: $('#ctlBtn1'),
                pick: '#picker1',
                fileType:1,
                accept: accept
            });
            var uploader2 = new myUploader({
                list: $('#thelist2'),
                btn: $('#ctlBtn2'),
                pick: '#picker2',
                fileType:2,
                accept: accept
            });
            var uploader3 = new myUploader({
                list: $('#thelist3'),
                btn: $('#ctlBtn3'),
                pick: '#picker3',
                fileType:3,
                accept: accept
            });
            /**
             * 触发文件上传
             */
            ns.upload = function(id){
                uploader1.upload(id);
                uploader2.upload(id);
                uploader3.upload(id);
            }
		})(solution.edit);

		var accept2 =  {
            title: 'dxf',
            extensions: 'dxf',
            mimeTypes: 'application/dxf'
        };

		$(function(){
            //调整按钮大小
            setTimeout(function(){
                $("div[id^='rt_rt_']").css({ width:72,height:29});
            },1000);
        });
	  </script>
</#if>
</@layout>
