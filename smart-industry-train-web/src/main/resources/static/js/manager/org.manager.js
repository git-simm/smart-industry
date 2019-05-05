Zq.Utility.RegisterNameSpace("org.manager");
(function (ns, undefined) {
    var setting = {
        view: {
            expandSpeed:"fast",
            selectedMulti:false,
            dblClickExpand: function (treeId, treeNode) {
                return treeNode.level > 0;
            }
        },
        edit:{
            enable:true,
            drag:{
                isMove:true,
                prev:true,
                next:true,
                borderMax:10,
                borderMin:-5,
                minMoveSize:5,
                maxShowNodeNum:5,
                autoOpenTime:500
            },
            editNameSelectAll:true,
            renameTitle:"重命名",
            showRemoveBtn:false,
            showRenameBtn:true
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            beforeRightClick: zTreeBeforeRightClick,
            beforeDrag: zTreeBeforeDrag,
            beforeDrop: zTreeBeforeDrop,
            onDrop: zTreeOnDrop,
            onRename: zTreeOnRename
        }
    };

    ns.GetSelectedNode = function(){
        var nodes = zTree.getSelectedNodes();
        if(nodes && nodes.length>0){
            return nodes[0];
        }
        return null;
    }
    /**
     * 新增树节点
     * @param type
     */
    ns.addTreeNode = function(type) {
        var parent = zTree.getSelectedNodes()[0];
        if(parent){
            ns.add({ newType:type },function () {
                zTree.addNodes(zTree.getSelectedNodes()[0], {});
                ns.calcSort();
            });
        }else{
            layer.alert("请先选中一个父级节点");
        }
    }
    ns.removeTreeNode = function() {
        var nodes = zTree.getSelectedNodes();
        if (nodes && nodes.length>0) {
            var ids = [];
            if (nodes[0].children && nodes[0].children.length > 0) {
                var msg = "要删除的节点是父节点，如果删除将连同子节点一起删掉。\n\n请确认！";
                if (confirm(msg)==true){
                    var nodes = getChildNodes(nodes[0]);
                    ids = ids.concat(nodes);
                }
            } else {
                ids.push(nodes[0].id);
            }
            ns.remove(ids,function(){
                zTree.removeNode(nodes[0]);
            });
        }
    }
    ns.checkTreeNode = function(checked) {
        var nodes = zTree.getSelectedNodes();
        if (nodes && nodes.length>0) {
            zTree.checkNode(nodes[0], checked, true);
        }
    }
    ns.resetTree = function() {
        $.fn.zTree.init($("#soluTree"), setting, zNodes);
    }

    /*-------------- 打开弹层 begin ---------------*/
    //新增方法
    ns.add = function (param,callback) {
        var selected = $.extend(ns.GetSelectedNode(),param);
        SmartMonitor.Common.SetData(selected);
        Zq.Utility.OpenModal({
            title: "新增组织",
            maxmin: false,
            area: ['800px', '400px'],
            content: [('/org/addwin').geturl(), 'yes'],
            end: function () {
                if (SmartMonitor.Common.GetResult() === true) {
                    if(callback){
                        callback();
                    }
                }
            }
        });
    };

    //编辑方法
    ns.Edit = function (id) {
        Zq.Utility.OpenModal({
            title: "编辑组织",
            maxmin: false,
            area: ['800px', '400px'],
            content: [('/org/editwin?id=' + id).geturl(), 'yes'],
            end: function () {
                if (SmartMonitor.Common.GetResult() === true) {
                    ns.ReFresh();
                    Zq.Utility.Msg("保存成功");
                }
            }
        });
    };
    //删除
    ns.Delete = function (id) {
        //询问框
        layer.confirm('确定要删除？', function () {
            layer.closeAll('dialog');
            $.ajax({
                async: false,
                type: "Post",
                dataType:"json",
                url: ("/org/delete?id=" + id).geturl(),
                success: function (result) {
                    if (result > 0) {
                        //删除成功，刷新列表
                        ns.ReFresh();
                        Zq.Utility.Msg("删除成功");
                    }
                }
            });
        });
    };
    /*-------------- 打开弹层 end ---------------*/
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
    }

    //-------------------------
    //拖拽前校验事件
    function zTreeBeforeDrag(treeId, treeNodes) {
        //根节点不允许拖拽
        if(treeNodes[0].pId ==null){
            layer.alert("根节点不允许拖拽");
            return false;
        }
        return true;
    };

    /**
     * Drop前校验事件
     * @param treeId
     * @param treeNodes
     * @param targetNode
     * @param moveType
     * @returns {boolean}
     */
    function zTreeBeforeDrop(treeId, treeNodes, targetNode, moveType) {
        if(targetNode && targetNode.pId == null && moveType=="prev"){
            layer.alert("不允许新建根节点");
            return false;
        }
        return true;
    };

    /**
     * Drop完成
     * @param event
     * @param treeId
     * @param treeNodes
     * @param targetNode
     * @param moveType
     */
    function zTreeOnDrop(event, treeId, treeNodes, targetNode, moveType) {
        ns.calcSort();
        //alert(treeNodes.length + "," + (targetNode ? (targetNode.tId + ", " + targetNode.name) : "isRoot" ));
    };

    /**
     * 重命名完成
     * @param event
     * @param treeId
     * @param treeNode
     * @param isCancel
     */
    function zTreeOnRename(event, treeId, treeNode, isCancel) {
        $.ajax({
            async: false,
            type: "Post",
            url: ("/solucls/update").geturl(),
            data: {id:treeNode.id,name:treeNode.name},
            success:function(id){
                //entity.id = id;
                //callback(id);
            }
        });
    }

    /**
     * 右键菜单控制
     * @param treeId
     * @param treeNode
     */
    function zTreeBeforeRightClick(treeId,treeNode){
        console.log(treeNode);
    }
    //-------- 方案 ajax 交互  begin-----------------------
    ns.getList = function(callback){
        $.ajax({
            async: false,
            url: Zq.Utility.GetPath("/org/list"),
            success:function(data){
                if(data!=null && data.length>0){
                    data[0].open=true;
                }
                callback(data);
            }
        });
    }
    /**
     *删除事件
     * @param entity
     * @param callback
     */
    ns.remove = function(ids,callback) {
        $.ajax({
            async: false,
            type: "Post",
            url: Zq.Utility.GetPath("/solucls/del"),
            dataType:"json",
            contentType : 'application/json;charset=utf-8',
            data: JSON.stringify(ids),
            success:function(result){
                callback(result);
            }
        });
    }
    //重新计算排序码
    ns.calcSort = function(callback){
        //获取数控件序列
        var list = getNodes(zTree.getNodes()[0]);
        $.ajax({
            async: false,
            type: "Post",
            url: Zq.Utility.GetPath("/solucls/calcSort"),
            dataType:"json",
            data: JSON.stringify(list),
            contentType : 'application/json;charset=utf-8',
            success:function(result){
                if(callback){
                    callback(result);
                }
            }
        });
    }

    /**
     * 递归获取所有的数节点信息
     */
    function getNodes(node,i){
        var arr =[];
        if(i==undefined)i=0;
        if(node.sort !== i){
            node.sort = i;
            arr.push({id:node.id,sort:i});
        }
        if(node.children && node.children.length>0){
            $.each(node.children,function(index,obj){
                //获取子记录
                arr = arr.concat(getNodes(obj,index));
            });
        }
        return arr;
    }
    /**
     * 递归获取所有的数节点信息
     */
    function getChildNodes(node){
        var arr =[];
        arr.push(node.id);
        if(node.children && node.children.length>0){
            $.each(node.children,function(index,obj){
                //获取子记录
                arr = arr.concat(getChildNodes(obj));
            });
        }
        return arr;
    }
    //-------- 方案 ajax 交互  end-----------------------
    //右键菜单内容
    ns.contexts = {
        addComp:{text: '新增公司',action: function(e){
                e.preventDefault();
                ns.addTreeNode(0);
            }},
        addDept:{text: '新增部门', action: function(e){
                e.preventDefault();
                ns.addTreeNode(1);
            }},
        addGroup:{text: '新增工作组', action: function(e){
                e.preventDefault();
                ns.addTreeNode(2);
            }},
        del:{text: '删除组织', action: function(e){
                e.preventDefault();
                ns.removeTreeNode();
            }}
    };
})(org.manager);

$(function () {
    org.manager.init();

    context.init({preventDoubleContext: false});
    var contexts = org.manager.contexts;
    context.attach('#soluTree', [contexts.addComp,contexts.addDept,contexts.addGroup,contexts.del]);

    context.settings({compress: true});

    $(document).on('mouseover', '.me-codesta', function(){
        $('.finale h1:first').css({opacity:0});
        $('.finale h1:last').css({opacity:1});
    });

    $(document).on('mouseout', '.me-codesta', function(){
        $('.finale h1:last').css({opacity:0});
        $('.finale h1:first').css({opacity:1});
    });
});