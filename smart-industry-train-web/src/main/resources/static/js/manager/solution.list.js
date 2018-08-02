Zq.Utility.RegisterNameSpace("solution.list");
(function (ns, undefined) {
    var setting = {
        view: {
            dblClickExpand: function (treeId, treeNode) {
                return treeNode.level > 0;
            }
        },
        data: {
            simpleData: {
                enable: true
            }
        },
        callback: {
            //onRightClick: OnRightClick
        }
    };

    var zNodes =[
        { id:1, pId:0, name:"解决方案中心", open:true},
        { id:11, pId:1, name:"父节点 1-1", open:true},
        { id:111, pId:11, name:"叶子节点 1-1-1"},
        { id:112, pId:11, name:"叶子节点 1-1-2"},
        { id:113, pId:11, name:"叶子节点 1-1-3"},
        { id:114, pId:11, name:"叶子节点 1-1-4"},
        { id:12, pId:1, name:"父节点 1-2", open:true},
        { id:121, pId:12, name:"叶子节点 1-2-1"},
        { id:122, pId:12, name:"叶子节点 1-2-2"},
        { id:123, pId:12, name:"叶子节点 1-2-3"},
        { id:124, pId:12, name:"叶子节点 1-2-4"},
        { id:13, pId:1, name:"父节点 1-3", open:true},
        { id:131, pId:13, name:"叶子节点 1-3-1"},
        { id:132, pId:13, name:"叶子节点 1-3-2"},
        { id:133, pId:13, name:"叶子节点 1-3-3"},
        { id:134, pId:13, name:"叶子节点 1-3-4"}
    ];
    var addCount = 1;
    ns.addTreeNode = function() {
        var newNode = { name:"增加" + (addCount++)};
        if (zTree.getSelectedNodes()[0]) {
            newNode.checked = zTree.getSelectedNodes()[0].checked;
            zTree.addNodes(zTree.getSelectedNodes()[0], newNode);
        } else {
            zTree.addNodes(null, newNode);
        }
    }
    ns.removeTreeNode = function() {
        var nodes = zTree.getSelectedNodes();
        if (nodes && nodes.length>0) {
            if (nodes[0].children && nodes[0].children.length > 0) {
                var msg = "要删除的节点是父节点，如果删除将连同子节点一起删掉。\n\n请确认！";
                if (confirm(msg)==true){
                    zTree.removeNode(nodes[0]);
                }
            } else {
                zTree.removeNode(nodes[0]);
            }
        }
    }
    ns.checkTreeNode = function(checked) {
        var nodes = zTree.getSelectedNodes();
        if (nodes && nodes.length>0) {
            zTree.checkNode(nodes[0], checked, true);
        }
    }
    ns.resetTree = function() {
        $.fn.zTree.init($("#treeDemo"), setting, zNodes);
    }

    var zTree, rMenu;
    /**
     * 初始化ztree控件
     */
    ns.init = function () {
        zTree = $.fn.zTree.init($("#soluTree"), setting, zNodes);
        rMenu = $("#rMenu");
    }
})(solution.list);

$(function () {
    solution.list.init();

    context.init({preventDoubleContext: false});
    context.attach('#soluTree', [
        {text: '增加节点',action: function(e){
                e.preventDefault();
                solution.list.addTreeNode();
            }},
        {text: '修改节点',action: function(e){
                e.preventDefault();
                solution.list.resetTree();
            }},
        {text: '删除节点', action: function(e){
                e.preventDefault();
                solution.list.removeTreeNode();
            }}
    ]);

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