Zq.Utility.RegisterNameSpace("solution.tree");
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
        callback: {
            onClick: treeClick
        }
    };

    /**
     * 树节点点击
     **/
    function treeClick(srcEvent, treeId, node, clickFlag){
        //替换svg中的元素
        console.log(node.filePath);
        $("#line_svg").attr("src" ,node.filePath);
    }

    ns.GetSelectedNode = function(){
        var nodes = zTree.getSelectedNodes();
        if(nodes && nodes.length>0){
            return nodes[0];
        }
        return null;
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
    //-------- 方案 ajax 交互  begin-----------------------
    ns.getList = function(callback){
        $.ajax({
            async: false,
            url: Zq.Utility.GetPath("/solution/getfiles"),
            data:{id : $("#hid_solutionId").val()},
            success:function(data){
                if(data!=null && data.length>0){
                    data[0].open=true;
                }
                console.log(data);
                callback(data);
            }
        });
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
})(solution.tree);

$(function () {
    solution.tree.init();
});