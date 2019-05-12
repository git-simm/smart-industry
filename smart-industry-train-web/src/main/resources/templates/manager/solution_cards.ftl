<#include "../base/_base.ftl"/>
<@layout;section>
    <#if section="title">测试方案列表
    <#elseif section="css">
        <@cssRef url="/static/_resources/ztree/zTreeStyle/zTreeStyle.css"/>
    <style type="text/css">
        .spaced2>li {
            margin-top: 0px;
            margin-bottom: 5px;
        }
        .edit-control {
            font-size: 15px;
            background: ghostwhite;
        }
    </style>
    <#elseif section="content">
    <div style="position: relative;" class="col-12" id="app">
        <#list cards as card>
            <div class="col-xs-4 col-sm-2 pricing-box">
                <div class="widget-box widget-color-dark">
                    <div class="widget-header">
                        <h5 class="widget-title bigger" style="color: black;">${card.name!}</h5>
                    </div>

                    <div class="widget-body">
                        <div class="widget-main" style="height: 160px;overflow:auto;">
                            <ul class="list-unstyled spaced2">
                                <#list card.partionList as item>
                                    <li>
                                        <i class="ace-icon fa fa-check green"></i>
                                        ${item.showName!}
                                    </li>
                                </#list>
                            </ul>
                        </div>
                        <div class="textCenter edit-control">
                            <a href="javascript:void(0)"
                               @click="edit(${card.id!})">编辑</a>
                            <a href="javascript:void(0)" class="marginLeft10"
                               @click="del(${card.id!})">删除</a>
                        </div>
                        <div>
                            <a href="javascript:void(0)" @click="run(${card.id!})" class="btn btn-block btn-inverse">
                                <i class="icon-rocket"></i>
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
            el: '#app',
            data: {
                solutionId: '${solutionId!}'
            },
            methods: {
                /**
                 * 运行
                 */
                run: function (cardId) {
                    window.open(('/run/test?id=' + this.solutionId + "&cardId=" + cardId).geturl(), '_blank');
                },
                /**
                 * 删除方案
                 */
                del: function (cardId) {
                    //询问框
                    layer.confirm('确定要删除？', function () {
                        layer.closeAll('dialog');
                        $.ajax({
                            async: false,
                            type: "Post",
                            dataType:"json",
                            url: ("/partion/delete?id="+cardId).geturl(),
                            success: function (result) {
                                location.reload();
                            }
                        });
                    });
                },
                /**
                 * 添加方案
                 */
                addCard: function () {
                    Zq.Utility.OpenModal({
                        title: "新增测试方案",
                        maxmin: false,
                        area: ['400px', '600px'],
                        content: [('/partion/addwin?solutionId=' + this.solutionId).geturl(), 'yes'],
                        end: function () {
                            if (SmartMonitor.Common.GetResult() === true) {
                                Zq.Utility.Msg("保存成功");
                                location.reload();
                            }
                        }
                    });
                },
                edit: function (id) {
                    Zq.Utility.OpenModal({
                        title: "编辑测试方案",
                        maxmin: false,
                        area: ['400px', '600px'],
                        content: [('/partion/editwin?id=' + id).geturl(), 'yes'],
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