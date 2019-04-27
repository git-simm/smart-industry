/**
 * svg_node管理
 */
var svgNode = function (node) {
    this.key = node.name,
        this.options = {
            container: $('#mainViewContainer'),
            eyeContainer: $('#thumbViewContainer'),
            iconContainer: $(".img-menu"),
            svgResolver: null,
            main: null,
            mainId: null,
            eyeId: null,
            scopeId: null,
            eyeWin: null,
            thumbnailViewer: null,
            icon: null,
            node: node,
            path: Zq.Utility.GetPath(node.filePath),
        };
    /**
     * 创建节点
     */
    this.create = function () {
        //loading层
        var index = layer.load(1, {
            shade: [0.1, '#fff'] //0.3透明度的白色背景
        });
        try {
            var key = 'main_' + this.options.node.id;
            this.options.mainId = key;
            //1.添加一个主界面信息
            var $main = $('<embed id="' + key + '" src="' + this.options.path + '"' +
                ' class="svg-node col-md-12 svg_position" type="image/svg+xml"/>');
            var $mainContainer = $('<div></div>');
            var $menu = $("#topContainer_template").clone();
            $menu.show();
            $mainContainer.append($menu);
            $mainContainer.append($main);
            this.options.main = $mainContainer;
            this.options.container.append($mainContainer);
            //2.添加鹰眼图
            this.createEyeWin();
            //3.添加底部导航信息
            this.createMenuIcon(this.options.node);
            //滚动到最右边
            $(".img-menu").scrollLeft(10000);
            //4.主界面逻辑处理
            mainProcess(this.options, function (options) {
                //5.开始计算排序码
                options.svgResolver = new svgNodeResolve(options);
                options.svgResolver.init();
            });
        } catch (ex) {
            console.error(ex);
        } finally {
            layer.close(index);
        }
    }

    /**
     * 创建主界面
     * @param options
     */
    function mainProcess(options, callback) {
        var node = options.node;
        //创建一个新的主界面
        var linkMap = node.linkMap;
        if (linkMap == null) return;
        setTimeout(function (args) {
            //console.log("开始计算链接")
            setLink(node, linkMap, options);
            if (callback) {
                callback(options);
            }
        }, 500);
    }

    /**
     * 设置link-map
     * @param linkMap
     * @constructor
     */
    function setLink(node, linkMap, options) {
        var svgDoc = document.getElementById(options.mainId).getSVGDocument();
        if (svgDoc == null) return;
        var map = Snap(svgDoc.getElementsByTagName("svg")[0]);
        var gList = [];
        for (var key in linkMap) {
            //var set = map.selectAll('g#'+key);
            var set = map.selectAll('use[entity-key="' + key + '"]');
            // 遍历填色
            set.forEach(function (element, index) {
                var g = element.node.href.baseVal;
                if (gList.indexOf(g) == -1) {
                    gList.push(g);
                    var set2 = map.selectAll('g' + g);
                    set2.forEach(function (eg, i) {
                        var box = eg.getBBox();
                        eg.append(createLink(box, map));
                    });
                }
                element.key = key;
                element.linkName = linkMap[key];
                element.attr("style", 'stroke:#f0f;').click(function () {
                    selectLinkNode(node, this.linkName);
                });
            });
        }
    }

    /**
     * 创建一个链接
     * @param map
     * @constructor
     */
    function createLink(box, map) {
        var link = map.paper.rect(box.x, box.y, box.width, box.height, 2).attr({
            "smart-type": "link",
            fill: "#0b1e66",
            "fill-opacity": 0.5,
            stroke: "#0b1e66",
            transform: "scale(1.1)"
        });
        return link;
    }

    /**
     * 设置跳转节点
     * @param node
     * @param name
     */
    function selectLinkNode(node, name) {
        //跳转到具体的node节点
        console.log("准备跳转");
        var zTree = solution.tree.zTree;
        if (node.projPath == null) return;
        var path = node.projPath.replace(node.fileName, "");
        var index = path.lastIndexOf("|");
        path = path.substr(0, index + 1) + name;
        var linkNode = zTree.getNodesByFilter(function (item) {
            var projPath = item.fileName + path;
            return (item.projPath == projPath);
        }, true); // 仅查找一个节点
        solution.tree.treeClick(null, null, linkNode);
        zTree.selectNode(linkNode);
    }

    /**
     * 创建鹰眼图
     * @param options
     */
    this.createEyeWin = function () {
        var key = 'eye_' + this.options.node.id;
        var scopeId = 'eye_scope_' + this.options.node.id;
        this.options.eyeId = key;
        this.options.scopeId = scopeId;
        var $eyeWin = $('#scopeContainer-template').eq(0).clone().show();
        $eyeWin.prop('id', key + '_eye__container');
        var $eyeRect = $('.thumb_rect', $eyeWin).eq(0);
        $eyeRect.prop("id", scopeId);

        var $scope = $('#scope', $eyeRect).eq(0);
        $scope.prop('id', 'scope' + this.key);
        var $line1 = $('#line1', $eyeRect).eq(0);
        $line1.prop('id', 'line1' + this.key);
        var $line2 = $('#line2', $eyeRect).eq(0);
        $line2.prop('id', 'line2' + this.key);

        var $eyeSvg = $('.thumb_svg', $eyeWin).eq(0);
        $eyeSvg.prop("id", key);
        $eyeSvg.prop("src", this.options.path);

        this.options.eyeWin = $eyeWin;
        this.options.eyeContainer.append($eyeWin);
        /**
         * 初始化缩略图
         */
        this.options.thumbnailViewer = new thumbnailViewer({
            mainViewId: this.options.mainId,
            thumbViewId: this.options.eyeId,
            scopeContainer: this.options.scopeId,
            key: this.key,
            onMainViewShown: function (svg, main) {
                main.zoom(2);
            }
        });
    }

    /**
     * 创建下方的导航图标
     * @param node
     */
    this.createMenuIcon = function () {
        var name = this.options.node.name;
        var $icon = $('<li key="' + name + '_icon"><a>' +
            '<embed type="image/svg+xml" style="pointer-events: none;" src="' + this.options.path + '" class="view-card"/>' +
            '<p class="view-text">' + name + '</p>' +
            '</a></li>');
        this.options.icon = $icon;
        this.options.iconContainer.append($icon);
        //图标点击事件
        var selected = this.selected;
        var node = this.options.node;
        $icon.on("click", function () {
            selected(node);
        });
    }
    /**
     * 选中
     */
    this.selected = function (node) {
        if (!node) {
            node = this.options.node;
        }
        var zTree = solution.tree.zTree;
        var linkNode = zTree.getNodesByFilter(function (item) {
            return (item.id == node.id);
        }, true); // 仅查找一个节点
        solution.tree.treeClick(null, null, linkNode);
        zTree.selectNode(linkNode);
    }
    /**
     * 窗口显示
     */
    this.show = function () {
        this.options.eyeWin.css('visibility', 'visible');
        this.options.main.css('visibility', 'visible');
        this.options.icon.addClass('selected');
    }
    //tip:(display:none 将元素直接卸载)(visible:hidden 隐藏，看不到摸得着)
    /**
     * 窗口隐藏
     */
    this.hide = function () {
        this.options.eyeWin.css('visibility', 'hidden');
        this.options.main.css('visibility', 'hidden');
        this.options.icon.removeClass('selected');
    }
    /**
     * 关闭窗口
     */
    this.close = function () {
        this.options.main.remove();
        this.options.eyeWin.remove();
        this.options.icon.remove();
    }
    //----------------------------------------------------------------------
    var runTimes = 0;
    /**
     * 运行
     */
    this.run = function () {
        //开灯，关灯
        runTimes++;
        if (runTimes % 2) {
            this.options.svgResolver.wirePath("#ff0000");
            //document.getElementById("train").contentWindow.train.lines.openLight();
        } else {
            this.options.svgResolver.wirePath("gray");
            //document.getElementById("train").contentWindow.train.lines.closeLight();
        }
    }
    //----------------------------------------------------------------------
    /**
     * 执行对象创建方法
     */
    this.create();
}
/**
 * svg-node 管理
 */
Zq.Utility.RegisterNameSpace("svg.node");
//闭包引入命名空间
(function (ns, undefined) {
    ns.nodes = [];
    ns.currentNode = null;
    var runBtn = $("#btn_run");
    /**
     * 打开窗口
     * @param node
     */
    ns.open = function (node) {
        var nodeWin = ns.nodes.find(function (n) {
            return n.key == node.name;
        });
        if (nodeWin == null) {
            //窗口不存在，则需要创建
            var vnode = new svgNode(node);
            ns.nodes.push(vnode);
            nodeWin = vnode;
        }
        ns.nodes.forEach(function (n) {
            n.hide();
        })
        ns.currentNode = nodeWin;
        nodeWin.show();
    }
    /**
     * 运行
     */
    ns.run = function () {
        ns.currentNode.run();
    }
    /**
     * 关闭窗口
     */
    ns.close = function () {
        ns.currentNode.close();
        //数组删除
        ns.nodes.splice($.inArray(ns.currentNode, ns.nodes), 1);
        if (ns.nodes.length > 0) {
            ns.currentNode = ns.nodes[ns.nodes.length - 1];
            ns.currentNode.selected();
        }
    }
})(svg.node);