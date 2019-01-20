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
     * 树节点点击(图形控件需要加载对应的文件)
     **/
    function treeClick(srcEvent, treeId, node, clickFlag){
        //替换svg中的元素
        if(node==null || node.filePath == null) return;
        var path = Zq.Utility.GetPath(node.filePath);
        console.log(path);
        if(path.indexOf(".svg")>-1){
            $("#line_svg").attr("src" ,path);
        }
        var linkMap = node.linkMap;
        if(linkMap==null) return;
        setTimeout(function (args) {
            //console.log("开始计算链接")
            ns.setLink(node,linkMap);
        }, 1000);
    }

    /**
     * 设置link-map
     * @param linkMap
     * @constructor
     */
    ns.setLink = function(node,linkMap){
        var svgDoc = document.getElementById("line_svg").getSVGDocument();
        var map = Snap(svgDoc.getElementsByTagName("svg")[0]);
        for(var key in linkMap){
            //var set = map.selectAll('g#'+key);
            var set = map.selectAll('use[entity-key="'+ key +'"]');
            // 遍历填色
            set.forEach(function(element, index) {
                var box = element.getBBox();
                element.append(createLink(box,map));
                element.attr("style",'stroke:#f0f;').click(function(){
                    selectLinkNode(node,linkMap[key]);
                });
            });
        }
    }
    function selectLinkNode(node,name){
        //跳转到具体的node节点
        console.log("准备跳转");
        var path = node.projPath.replace(node.fileName,"");
        var index = path.lastIndexOf("|");
        path = path.substr(0,index+1)+name;
        var linkNode = zTree.getNodesByFilter(function(item){
            var projPath = item.fileName + path;
            return (item.projPath == projPath);
        }, true); // 仅查找一个节点
        treeClick(null,null,linkNode);
        zTree.selectNode(linkNode);
    }
    /**
     * 创建一个链接
     * @param map
     * @constructor
     */
    function createLink(box,map){
        var link = map.paper.rect(box.x,box.y,box.width,box.height,2).attr({
            "smart-type":"link",
            fill:"#0b1e66",
            "fill-opacity": 0.5,
            stroke:"#0b1e66",
            transform: "scale(1.1)"
        });
        // link.hover(function(e){
        //     // var rect = e.target;
        //     // rect.setAttributeNS(null, "fill", "#00ff21")
        //     //移入
        //     this.attr({
        //         //transform: "scale(1.1)",
        //         fill:"#00ff21"
        //     });
        // },function (e) {
        //     // var rect = e.target;
        //     // rect.setAttributeNS(null, "fill", "#0b1e66")
        //     //移除
        //     this.attr({
        //         //transform:"scale(0.9)",
        //         fill:"#0b1e66"
        //     });
        // });
        return link;
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