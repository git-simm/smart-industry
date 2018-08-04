<#include "../base/_base.ftl"/>
<@layout;section>
    <#if section="title">方案管理
    <#elseif section="css">
    <@cssRef url="/static/_resources/ztree/zTreeStyle/zTreeStyle.css"/>
    <@cssRef url="/static/_resources/context/context.standalone.css"/>
    <style>
        .treeContainer{
            position: fixed;
            bottom: 14px;
            top: 25px;
            left: 0;
            border: 1px solid #ccc;
            border-width: 0 1px 0 0;
            background-color: ghostwhite;
            overflow: auto;
            overflow-y: auto;
        }
        .ztreeMenu{
            height: 25px;
            background: whitesmoke;
            position: absolute;
            right: 0;
            left: 0;
            padding-left: 10px;
            padding-top: 3px;
        }
        /*固定第一个元素*/
        .ztree li span.button.switch.level0 {visibility:hidden; width:1px;}
        .ztree li ul.level0 {padding:0; background:none;}
    </style>
    <#elseif section="content">
    <div style="position: relative;" class="col-2">
        <div class="ztreeMenu">解决方案列表</div>
        <div class="treeContainer col-2">
            <ul id="soluTree" class="ztree"></ul>
        </div>
    </div>
    <#elseif section="scripts">
        <@jsRef "/static/_resources/ztree/jquery.ztree.all.js"/>
        <@jsRef "/static/_resources/context/context.js"/>
        <@jsRef "/static/js/manager/solution.list.js"/>
    </#if>
</@layout>