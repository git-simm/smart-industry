<#include "../base/_base.ftl"/>
<@layout;section>
    <#if section="title">测试方案列表
    <#elseif section="css">
        <@cssRef url="/static/_resources/ztree/zTreeStyle/zTreeStyle.css"/>
    <style>
    </style>
    <#elseif section="content">
    <div style="position: relative;" class="col-12">
        <#assign ar=1..10/>
        <#list ar as a>
            <div class="col-xs-4 col-sm-2 pricing-box">
                <div class="widget-box widget-color-dark">
                    <div class="widget-header">
                        <h5 class="widget-title bigger lighter">车门开关测试</h5>
                        <div class="pull-right action-buttons">
                            <a class="red" href="javasript:void(0);" style="margin-top: 10px;">
                                <i class="icon-trash bigger-130"></i>
                            </a>
                        </div>
                    </div>

                    <div class="widget-body">
                        <div class="widget-main" style="height: 200px;overflow:auto;">
                            <ul class="list-unstyled spaced2">
                                <li>
                                    <i class="ace-icon fa fa-check green"></i>
                                    10 GB Disk Space
                                </li>

                                <li>
                                    <i class="ace-icon fa fa-check green"></i>
                                    200 GB Bandwidth
                                </li>

                                <li>
                                    <i class="ace-icon fa fa-check green"></i>
                                    100 Email Accounts
                                </li>

                                <li>
                                    <i class="ace-icon fa fa-check green"></i>
                                    10 MySQL Databases
                                </li>
                            </ul>
                        </div>

                        <div>
                            <a href="javascript:void(0)" class="btn btn-block btn-inverse">
                                <i class="ace-icon fa fa-shopping-cart bigger-110"></i>
                                <span>运行</span>
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </#list>
        <div class="col-xs-4 col-sm-2 pricing-box">
            <button class="btn btn-light marginTop5" title="点击添加新的测试方案">
                <img src="${request.contextPath}/static/images/svg-add.svg" alt="">
            </button>
        </div>
    </div>
    <#elseif section="scripts">
    </#if>
</@layout>