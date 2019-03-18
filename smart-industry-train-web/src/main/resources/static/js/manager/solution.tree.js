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
    ns.runTimes = 0;
    /**
     * 树节点点击(图形控件需要加载对应的文件)
     **/
    function treeClick(srcEvent, treeId, node, clickFlag){
        ns.runTimes = 0;
        //替换svg中的元素
        if(node==null || node.filePath == null) return;
        var path = Zq.Utility.GetPath(node.filePath);
        if(path.indexOf(".svg")>-1){
            $('#mainViewContainer').show();
            $('#thumbViewContainer').show();
            $('#topContainer').show();
            $('#excelList').hide();

            $("#line_svg").attr("src" ,path);
            var linkMap = node.linkMap;
            if(linkMap==null) return;
            setTimeout(function (args) {
                //console.log("开始计算链接")
                ns.setLink(node,linkMap);
                svg.resolve.sort(document.getElementById("line_svg").getSVGDocument());
            }, 1000);
        }else if(path.indexOf(".xls")>-1){
            $('#mainViewContainer').hide();
            $('#thumbViewContainer').hide();
            $('#topContainer').hide();
            $('#excelList').show();
            ns.getExcelData(node.fileId);
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
        //初始化一下excel表格
        $('#list').bootstrapTable({
            url: ('/solution/getExcelData').geturl(),
            method: 'post',
            dataType: "json",
            contentType:"application/x-www-form-urlencoded",
            height: $(window).height() - 100,
            pagination: false, //分页
            silentSort: true, //自动记住排序项
            onlyInfoPagination: false,
            showFooter:false,
            striped:false,
            buttonsClass:"sm",
            locale: "zh-CN", //表格汉化
            search: false, //显示搜索框
            checkboxHeader: true,
            maintainSelected: true,
            clickToSelect: true,
            sidePagination: "server", //服务端处理分页
            pageSize: 25,
            uniqueId: "id",
            sortName: "Item",
            sortOrder: "desc",
            queryParams: function (params) {
                $.extend(params, { fileId: $('#hid_fileId').val(),solutionId:$('#hid_solutionId').val()});
                return params;
            },
            // onDblClickRow: function(tr,el) {
            //     ns.Edit(tr.id);
            // },
            //showToggle:true,
            rowAttributes:function(row,index){
                return { state: row.state };
            },
            columns: [
                {
                    field: 'Number',
                    title: '序号',
                    width: 60,
                    align: 'center',
                    sortable: false,
                    formatter: function (value, row, index) {
                        return index + 1;
                    }
                },
                {
                    title: 'id',
                    field: 'id',
                    //align: 'center',
                    visible: false
                },
                // {
                //     title: 'Item',
                //     field: 'Item',
                //     align: 'left',
                //     width: "15%"
                // },
                {
                    title: "Representation",
                    field: "Representation",
                    align: "left",
                    width: "15%"
                },
                {
                    title: 'Wire_Number',
                    field: 'Wire_Number',
                    align: 'left',
                    width: "15%"
                },
                {
                    title: 'Dest_1_Item',
                    field: 'Dest_1_Item',
                    align: 'left',
                    width: "15%"
                },
                {
                    title: 'Dest_1_Connector',
                    field: 'Dest_1_Connector',
                    align: 'left',
                    width: "15%"
                },
                {
                    title: 'Dest_2_Item',
                    field: 'Dest_2_Item',
                    align: 'left',
                    width: "15%"
                },
                {
                    title: 'Dest_2_Connector',
                    field: 'Dest_2_Connector',
                    align: 'left',
                    width: "15%"
                },
                {
                    title: '状态',
                    field: 'state',
                    align: 'left',
                    width: "15%",
                    formatter: function (value, row, index) {
                        if(value=="-1") return "缺失";
                        if(value =="0") return "正常";
                        if(value =="1") return "多出";
                    }
                }
            ]
        });
        //高度重置
        $(window).resize(function () {
            $('#list').bootstrapTable('resetView', { height: $(window).height() - 100 });
        });
        ns.getList(function(data){
            zTree = $.fn.zTree.init($("#soluTree"), setting, data);
            //选中第一个有效节点
            var nodes = zTree.getNodes();
            if (nodes.length>0) {
                var first = getFirstNode(nodes);
                zTree.selectNode(first);
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
            data:{id : $("#hid_solutionId").val()},
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
//----------- excel清单交互方案 begin ---------------------
    //获取excel数据
    ns.getExcelData = function(fileId,callback){
        $('#hid_fileId').val(fileId);
        $('#list').bootstrapTable('refresh');
    }
//----------- excel清单交互方案 end ---------------------
})(solution.tree);

$(function () {
    solution.tree.init();
});