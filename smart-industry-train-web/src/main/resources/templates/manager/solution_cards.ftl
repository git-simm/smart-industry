<#include "../base/_base.ftl"/>
<@layout;section>
    <#if section="title">测试方案列表
    <#elseif section="css">
        <@cssRef url="/static/_resources/ztree/zTreeStyle/zTreeStyle.css"/>
    <style>
    </style>
    <#elseif section="content">
    <div style="position: relative;" class="col-12" id="app">
        <#list cards as card>
            <div class="col-xs-4 col-sm-2 pricing-box">
                <div class="widget-box widget-color-dark">
                    <div class="widget-header">
                        <h5 class="widget-title bigger lighter" key="${card.id!}">${card.name!}</h5>
                        <div class="pull-right action-buttons">
                            <a class="red" href="javasript:void(0);" style="margin-top: 10px;">
                                <i class="icon-trash bigger-130"></i>
                            </a>
                        </div>
                    </div>

                    <div class="widget-body">
                        <div class="widget-main" style="height: 200px;overflow:auto;">
                            <ul class="list-unstyled spaced2">
                                <#list card.partionList as item>
                                <li>
                                    <i class="ace-icon fa fa-check green"></i>
                                    ${item.showName!}
                                </li>
                                </#list>
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
            <button class="btn btn-light marginTop5" title="点击添加新的测试方案" @click="addCard">
                <img src="${request.contextPath}/static/images/svg-add.svg" alt="">
            </button>
        </div>
    </div>
    <#elseif section="scripts">
    <script type="text/javascript">
        var app = new Vue({
            el:'#app',
            data:{
                solutionId: '${solutionId!}'
            },
            methods:{
                /**
                 * 添加方案
                 */
                addCard: function(){
                    Zq.Utility.OpenModal({
                        title: "新增测试方案",
                        maxmin: false,
                        area: ['400px', '600px'],
                        content: [('/partion/addwin?solutionId='+ this.solutionId ).geturl(), 'yes'],
                        end: function () {
                            if (SmartMonitor.Common.GetResult() === true) {
                                Zq.Utility.Msg("保存成功");
                                location.reload();
                            }
                        }
                    });
                },
                edit:function(id){
                    Zq.Utility.OpenModal({
                        title: "编辑测试方案",
                        maxmin: false,
                        area: ['400px', '600px'],
                        content: [('/partion/editwin?id='+ id ).geturl(), 'yes'],
                        end: function () {
                            if (SmartMonitor.Common.GetResult() === true) {
                                Zq.Utility.Msg("保存成功");
                                location.reload();
                            }
                        }
                    });
                }
            }
        });
    </script>
    </#if>
</@layout>