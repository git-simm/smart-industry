<#include "../base/_base.ftl"/>
<@layout;section>
    <#if section="title">分类选择
    <#elseif section="css">
        <@cssRef url="/static/_resources/ztree/zTreeStyle/zTreeStyle.css"/>
        <@cssRef url="/static/_resources/context/context.standalone.css"/>
    <style>
        .treeContainer{
            position: fixed;
            bottom: 40px;
            top: 0;
            left: 0;
            border: 1px solid #ccc;
            border-width: 0 1px 0 0;
            background-color: ghostwhite;
            overflow: auto;
            overflow-y: auto;
        }
    </style>
    <#elseif section="content">
    <div class="page-content mini-model">
        <form id="form-main" onsubmit="javascript: return false;">
            <div class="mini-model-body bottom40">
                <div class="treeContainer col-12">
                    <ul id="soluTree" class="ztree"></ul>
                </div>
            </div>
            <div class="mini-model-footer">
                <div class="textRight padding5">
                    <button class="btn btn-sm btn-primary btn-round" onclick="select.class.ok()">保存</button>
                    <a id="close" class="btn btn-sm btn-default btn-round" onclick="SmartMonitor.Common.Close()">取消</a>
                </div>
            </div>
        </form>
    </div>
    <#elseif section="scripts">
        <@jsRef "/static/_resources/ztree/jquery.ztree.all.js"/>
        <@jsRef "/static/_resources/context/context.js"/>
        <script type="text/javascript">
            Zq.Utility.RegisterNameSpace("select.class");
            (function (ns, undefined) {
                var setting = {
                    view: {
                        expandSpeed:"fast",
                        selectedMulti:false,
                        dblClickExpand: function (treeId, treeNode) {
                            return treeNode.level > 0;
                        }
                    },
                    data: {
                        simpleData: {
                            enable: true
                        }
                    },
                    callback:{
                        onDblClick: function(){
                            ns.ok();
                        }
                    }
                };
                ns.GetSelectedNode = function(){
                    var nodes = zTree.getSelectedNodes();
                    if(nodes && nodes.length>0){
                        return nodes[0];
                    }
                    return null;
                }
                ns.resetTree = function() {
                    $.fn.zTree.init($("#treeDemo"), setting, zNodes);
                }

                var zTree;
                /**
                 * 初始化ztree控件
                 */
                ns.init = function () {
                    ns.getList(function(data){
                        zTree = $.fn.zTree.init($("#soluTree"), setting, data);
                        //选中第一个节点
                        var nodes = zTree.getNodes();
                        if (nodes.length>0) {
                            zTree.selectNode(nodes[0]);
                        }
                    });
                    var node = SmartMonitor.Common.GetData();
                    console.log(node);
                    /*$(".datatable").on("dblclick", "tbody>tr", function () {
                        ns.OK();
                    });*/
                }
                //新增方法
                ns.ok = function () {
                    var selected = ns.GetSelectedNode();
                    if(selected==null){
                        layer.alert("请选择一个数据库!");
                        return;
                    }
                    SmartMonitor.Common.Close(selected);
                };
                //-------- 方案 ajax 交互  begin-----------------------
                ns.getList = function(callback){
                    $.ajax({
                        async: false,
                        url: Zq.Utility.GetPath("/solucls/list"),
                        success:function(data){
                            if(data!=null && data.length>0){
                                data[0].open=true;
                            }
                            callback(data);
                        }
                    });
                }
                //-------- 方案 ajax 交互  end-----------------------
            })(select.class);
            $(function () {
                select.class.init();
            });
        </script>
    </#if>
</@layout>