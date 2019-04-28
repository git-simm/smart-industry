<#macro layout>
    <#include "resouceimp.ftl"/>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xml:lang="zh-CN" xmlns="http://www.w3.org/1999/xhtml" lang="zh-CN">
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <title><#nested "title"/> - 轨道车辆功能动态模拟系统</title>
    <link rel="shortcut icon" type="image/ico" href="${request.contextPath!}/favicon.ico">
    <!-- basic styles -->
<@cssRef url="/static/_resources/assets/css/bootstrap.min.css"/>
<@cssRef url="/static/_resources/bootstrap/css/bootstrap-table.css"/>
<@cssRef url="/static/_resources/assets/css/font-awesome.min.css"/>
<@cssRef url="/static/_resources/bootstrap-select/css/bootstrap-select.min.css"/>
    <!--[if IE 7]>
        <@cssRef url="/static/_resources/assets/css/font-awesome-ie7.min.css"/>
    <![endif]-->
    <!-- page specific plugin styles -->
    <!-- ace styles -->
<@cssRef url="/static/_resources/assets/css/ace.min.css"/>
<@cssRef url="/static/_resources/assets/css/ace-rtl.min.css"/>
<@cssRef url="/static/_resources/assets/css/ace-skins.min.css"/>
<@cssRef url="/static/css/ace.conver.css"/>
<@cssRef url="/static/_resources/assets/css/jquery-ui.min.css"/>
<@cssRef url="/static/css/main.css"/>
    <!--[if lte IE 8]>
        <@cssRef url="/static/_resources/assets/css/ace-ie.min.css"/>
    <![endif]-->
<#nested "css" />
    <@jsRef url="/static/_resources/jquery/jquery-2.0.3.min.js"/>
    <@jsRef url="/static/_resources/assets/js/ace-extra.min.js"/>
    <@jsRef url="/static/_resources/common/utility.js"/>
    <@jsRef url="/static/_resources/common/common.js"/>
    <@jsRef url="/static/js/common.js"/>
</head>
<body class="easyui-layout layout panel-noscroll" style="background-color:white;">
    <#nested "content" />
<input type="hidden" id="hid_contextpath" value="${request.contextPath!}"/>
<input type="hidden" id="_mode" value="${formMode!}"/>
</body>
</html>
<!-- ace scripts -->
    <@jsRef "/static/_resources/bootstrap/js/bootstrap.min.js"/>
    <@jsRef "/static/_resources/bootstrap/js/bootstrap-table.js"/>
    <@jsRef "/static/_resources/bootstrap/js/bootstrap-table-zh-CN.js"/>
    <@jsRef "/static/_resources/layer/layer.js"/>
    <@jsRef url="/static/_resources/assets/js/jquery-ui.min.js"/>
    <@jsRef url="/static/_resources/assets/js/jquery.ui.touch-punch.min.js"/>
    <@jsRef url="/static/_resources/assets/js/jquery.slimscroll.min.js"/>
    <@jsRef url="/static/_resources/assets/js/jquery.easy-pie-chart.min.js"/>
    <@jsRef url="/static/_resources/assets/js/jquery.sparkline.min.js"/>
<!-- ace settings handler -->
    <@jsRef url="/static/_resources/assets/js/ace-elements.min.js"/>
    <@jsRef url="/static/_resources/assets/js/ace.min.js"/>
    <#nested "scripts"/>
</#macro>