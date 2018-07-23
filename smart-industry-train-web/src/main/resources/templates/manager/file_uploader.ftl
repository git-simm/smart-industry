<#include "../base/_base.ftl"/>
<@layout;section>
    <#if section="title">文件上传
    <#elseif section="css">
        <@cssRef url="/static/_resources/uploader/webuploader.css"/>
        <@cssRef url="/static/css/myuploader.css"/>
    <#elseif section="content">
    <div id="uploader" class="wu-example">
        <!--用来存放文件信息-->
        <div id="thelist" class="uploader-list"></div>
        <div class="btns">
            <div id="picker">选择文件</div>
            <button id="ctlBtn" class="btn btn-sm btn-default">开始上传</button>
        </div>
    </div>
    <#elseif section="scripts">
        <@jsRef "/static/_resources/uploader/webuploader.js"/>
        <@jsRef "/static/js/manager/file.uploader.js"/>
    </#if>
</@layout>