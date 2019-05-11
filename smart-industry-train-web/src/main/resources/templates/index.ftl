<#include "base/_base.ftl"/>
<@layout;section>
    <#if section="title">首页
    <#elseif section="css">
     <@cssRef "/static/_resources/easyui/themes/bootstrap/easyui.css"/>
     <style>
         .west {
             width: 190px;
             margin-top: 60px !important;
             /*background-color: #669acc;*/
         }
         .layout-panel-west .panel-header {
             top: 60px;
         }
         .layout-expand-west .panel-header {
             margin-top:60px;
         }

         .north {
             height: 60px;
             background-color: darkblue;
             color: white;
             position: fixed;
             top: 0;
             left: 0;
             right: 0;
             height: 60px;
             z-index: 1000;
         }
         .center {
             margin-top: 60px !important;
         }
         .menu-text {
             height: 20px;
             line-height: 20px;
             float: inherit;
             padding-left: 0;
         }

         .sidebar:before {
             top: 85px;
         }

         .menu-line {
             border-left: 0 solid #ccc;
             border-right: 0 solid #fff;
         }
     </style>
    <#elseif section="content">
<div class="north">
    <div class="pull-left" style="padding: 10px;">
        <h1 title="点击返回首页选择模式"  onclick="javascript: window.open('${request.contextPath}/index', '_parent');" >
            <img src="${request.contextPath}/static/favicon.ico" style="width: 40px;">&nbsp;轨道车辆功能动态模拟系统
        </h1>
    </div>
    <div class="navbar-header pull-right" role="navigation">
        <ul class="nav ace-nav">
            <li style="background: none;height:60px;line-height:60px;">
                <a data-toggle="dropdown" href="#" class="dropdown-toggle" style="background-color: transparent;">
                    <span class="user-info" style="line-height:20px; font-size: 16px;top:10px;">
                        <small>欢迎光临</small>${user!}
                    </span>
                    <i class="icon-caret-down"></i>
                </a>
                <ul class="user-menu pull-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
                    <li onclick="SmartMonitor.Common.ChangePsw()">
                        <a href="javascript:void(0)">
                            <i class="icon-cog"></i>
                            修改密码
                        </a>
                    </li>
                    <li class="divider"></li>
                    <li onclick="SmartMonitor.Common.LogOut()">
                        <a href="javascript:void(0)">
                            <i class="icon-off"></i>
                            退出
                        </a>
                    </li>
                </ul>
            </li>
        </ul>
    </div>
</div>
<div region="center" class="center">
    <div class="easyui-tabs" fit="true" border="false" id="tabs">
        <!--<div title="首页"></div>-->
    </div>
</div>
<div region="west" class="west" title="系统管理菜单">
    <div class="sidebar" id="sidebar">
        <script type="text/javascript">
            try {
                ace.settings.check('sidebar', 'fixed');
            } catch (e) {
            }
        </script>
        <ul class="nav nav-list">
            <li>
                <a href="#" class="dropdown-toggle">
                    <i class="icon icon-lock"></i>
                    <span class="menu-text"> 权限管理 </span>
                    <b class="arrow icon-angle-down"></b>
                </a>
                <ul class="submenu">
                    <li onclick="smart.train.index.open('用户管理', '/user/list')">
                        <a href="javascript:void(0)">
                            <i class="icon-double-angle-right"></i>
                            用户管理
                        </a>
                    </li>
                </ul>
            </li>
            <li>
                <a href="#" class="dropdown-toggle">
                    <i class="icon icon-bar-chart"></i>
                    <span class="menu-text"> 方案管理 </span>
                    <b class="arrow icon-angle-down"></b>
                </a>
                <ul class="submenu">
                    <li onclick="smart.train.index.open('方案列表', '/solution/list')">
                        <a href="javascript:void(0)">
                            <i class="icon-double-angle-right"></i>
                            方案列表
                        </a>
                    </li>
                    <#--<li onclick="smart.train.index.open('测试方案', '/solution/cards')">-->
                        <#--<a href="javascript:void(0)">-->
                            <#--<i class="icon-double-angle-right"></i>-->
                            <#--测试方案-->
                        <#--</a>-->
                    <#--</li>-->
                    <#--<li onclick="smart.train.index.open('执行测试', '/run/test')">-->
                        <#--<a href="javascript:void(0)">-->
                            <#--<i class="icon-double-angle-right"></i>-->
                            <#--执行测试-->
                        <#--</a>-->
                    <#--</li>-->
                    <!--<li onclick="smart.train.index.open('文件上传', '/file/uploader')">
                        <a href="javascript:void(0)">
                            <i class="icon-double-angle-right"></i>
                            文件上传
                        </a>
                    </li>-->
                </ul>
            </li>
            <li>
                <a href="#" class="dropdown-toggle">
                    <i class="icon icon-cog"></i>
                    <span class="menu-text"> 基础配置 </span>
                    <b class="arrow icon-angle-down"></b>
                </a>
                <ul class="submenu">
                    <li onclick="smart.train.index.open('字典项管理', '/param/list')">
                        <a href="javascript:void(0)">
                            <i class="icon-double-angle-right"></i>
                            字典项管理
                        </a>
                    </li>
                </ul>
            </li>
        </ul>
        <!--<div class="sidebar-collapse" id="sidebar-collapse">
            <i class="icon-double-angle-left" data-icon1="icon-double-angle-left" data-icon2="icon-double-angle-right"></i>
        </div>
        <script type="text/javascript">
            try {
                ace.settings.check('sidebar', 'collapsed');
            } catch (e) {
            }
        </script>-->
    </div>
</div>

<div id="tabsMenu" class="easyui-menu" style="width: 120px;">
    <div name="close">关闭</div>
    <div name="Other">关闭其他</div>
    <div name="All">关闭所有</div>
</div>
    <#elseif section="scripts">
        <@jsRef "/static/_resources/easyui/jquery.easyui.min.js"/>
        <@jsRef "/static/js/manager/index.js"/>
	 <script type="text/javascript">
         $(function () {
             $(".submenu li").click(function () {
                 $(".submenu li.active").removeClass("active");
                 $(this).addClass("active");
             });
             var $first = $(".nav-list .dropdown-toggle").eq(1);
             $first.click();
             $first.parent().find(".submenu li a:eq(0)").click();
         });
     </script>
    </#if>
</@layout>
