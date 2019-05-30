Zq.Utility.RegisterNameSpace("solution.tree");
(function (ns, undefined) {
    var setting = {
        view: {
            expandSpeed:"fast",
            selectedMulti:false,
            showTitle : true,
        },
        data: {
            simpleData: {
                enable: true
            },
            key: {
                //title: "projFile",
                name: "projFile"
            }
        },
        callback: {
            onClick: treeClick
        }
    };
    ns.runTimes = 0;
    ns.treeClick = treeClick;
    /**
     * 树节点点击(图形控件需要加载对应的文件)
     **/
    function treeClick(srcEvent, treeId, node, clickFlag){
        ns.runTimes = 0;
        //替换svg中的元素
        if(node==null || node.filePath == null) return;
        var path = Zq.Utility.GetPath(node.filePath);
        if(path.indexOf(".svg")>-1){
            //$('#mainViewContainer').show();
            //$('#thumbViewContainer').show();
            $('#excelList').hide();
            test.run.layout.open('south');
            //$("#line_svg").attr("src" ,path);
            //$("#thumbView").attr("src" ,path);
            svg.node.open(node);
            //card.view.treeClick(node);
        }else if(path.indexOf(".xls")>-1){
            //$('#mainViewContainer').hide();
            //$('#thumbViewContainer').hide();
            $('#excelList').show();
            test.run.layout.close('south');
            check.grid.getExcelData(node.fileId,node);
        }
    }

    /**
     * 设置link-map
     * @param linkMap
     * @constructor
     */
    ns.setLink = function(node,linkMap){
        var svgDoc = document.getElementById("line_svg").getSVGDocument();
        var map = Snap(svgDoc.getElementsByTagName("svg")[0]);
        var gList = [];
        for(var key in linkMap){
            //var set = map.selectAll('g#'+key);
            var set = map.selectAll('use[entity-key="'+ key +'"]');
            // 遍历填色
            set.forEach(function(element, index) {
                var g = element.node.href.baseVal;
                if(gList.indexOf(g)==-1){
                    gList.push(g);
                    var set2 = map.selectAll('g'+g);
                    set2.forEach(function(eg, i) {
                        var box = eg.getBBox();
                        eg.append(createLink(box,map));
                    });
                }
                element.key = key;
                element.linkName = linkMap[key];
                element.attr("style",'stroke:#f0f;').click(function(){
                    selectLinkNode(node,this.linkName);
                });
            });
        }
    }

    /**
     * 设置跳转节点
     * @param node
     * @param name
     */
    function selectLinkNode(node,name){
        //跳转到具体的node节点
        console.log("准备跳转");
        if(node.projPath==null) return;
        var path = node.projPath.replace(node.fileName,"");
        var index = path.lastIndexOf("|");
        path = path.substr(0,index+1)+name;
        var linkNode = ns.zTree.getNodesByFilter(function(item){
            var projPath = item.fileName + path;
            return (item.projPath == projPath);
        }, true); // 仅查找一个节点
        treeClick(null,null,linkNode);
        ns.zTree.selectNode(linkNode);
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
        return link;
    }

    ns.GetSelectedNode = function(){
        var nodes = ns.zTree.getSelectedNodes();
        if(nodes && nodes.length>0){
            return nodes[0];
        }
        return null;
    }
    ns.checkTreeNode = function(checked) {
        var nodes = ns.zTree.getSelectedNodes();
        if (nodes && nodes.length>0) {
            ns.zTree.checkNode(nodes[0], checked, true);
        }
    }
    ns.resetTree = function() {
        ns.zTree = $.fn.zTree.init($("#soluTree"), setting, zNodes);
    }

    ns.zTree = null;
    /**
     * 初始化ztree控件
     */
    ns.init = function () {
        ns.getList(function(data){
            ns.zTree = $.fn.zTree.init($("#soluTree"), setting, data);
            //选中第一个有效节点
            var nodes = ns.zTree.getNodes();
            if (nodes.length>0) {
                var first = getFirstNode(nodes);
                ns.zTree.selectNode(first);
                setTimeout(function (args) {
                    treeClick(null,null,first);
                }, 1000);
            }
        });
    }
    //-------- 方案 ajax 交互  begin-----------------------
    /**
     * 获取第一个有效节点
     * @param nodes
     * @returns {*}
     */
    function getFirstNode(nodes){
        for(var index in nodes){
            var node = nodes[index];
            if(node && node.filePath){
                return node;
            }
            if(node.children && node.children.length>0){
                var temp = getFirstNode(node.children);
                if(temp){
                    return temp;
                }
            }
        }
        return null;
    }
    ns.getList = function(callback){
        $.ajax({
            async: false,
            url: Zq.Utility.GetPath("/solution/getfiles"),
            data:{id : $("#hid_solutionId").val(), cardId:$('#hid_cardId').val()},
            success:function(data){
                if(data!=null && data.length>0){
                    data[0].open=true;
                }
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