Zq.Utility.RegisterNameSpace("svg.resolve");
//闭包引入命名空间
(function (ns, undefined) {
    var actionEnum = {
        start: 1, //开始
        normal: 2, //正常流动
        stop: 3, //停止
    };
    /**
     * 元器件行为配置
     */
    var symbolConfig = [
        {
            type: actionEnum.start,
            list: ['CD_POT_PRSW_W1RR', 'CD_W_PR_PANTO', 'CD_G_PRSW_BATT_']
        },
        {
            type: actionEnum.normal,
            list: ['CD_R_PRSW_01', 'CD_V_PRSW_D', 'CD_V_PRSW_IL', 'CD_L_PRSW_01', 'CD_F_PRSW_F1', 'CD_F_PRSW_ARR',
                'CD_W_PR_JUMPER', 'CD_R_PRSW_06_N', 'CD_G_PRSW_DC_DC', 'CD_V_PRSW_SD', 'CD_H_PRSW_ANT', 'CD_X_PR_PLUG230V',
                'CD_P_PRSW_VOL', 'CD_P_PRSW_CUR', 'CD_T_PRSW_VT_L', 'CD_W_PR_GRD1', 'CD_W_PR_GRD2', 'CD_W_PR_SHIELD',
                'CD_W_PR_SH_POINT', 'CD_W_PR_COUPLER1', 'CD_R_PRSW_PT100','CD_K_PRSW_SMDD', 'CD_POT_PRSW_W1RL']
        },
        {
            type: actionEnum.stop,
            list: ['CD_C_PRSW_01', 'CD_E_PRSW_L_INV', 'CD_H_PRSW_IL01', 'CD_H_PRSW_AC3',
                'CD_H_PRSW_AC1', 'CD_S_PRSW_SM12', 'CD_S_PRSW_PS_10', 'CD_S_PRSW_ASNC', 'CD_S_PRSW_ASNO',
                'CD_Q_PRSW_D1', 'CD_S_PRSW_01_1L', 'CD_S_PRSW_01_1L', 'CD_H_PRSW_AC2', 'CD_H_PRSW_MIC',
                'CD_M_PRSW_3', 'CD_S_PRSW_CO_3', 'CD_B_PRSW_PS_01', 'CD_K_PRSW_SMVAR',
                'CD_K_PRSW_ACO', 'CD_K_PRSW_ANO_WD', 'CD_K_PRSW_MNO1', 'CD_Y_PRSW_EMV_NC', 'CD_S_PRSW_PS11_',
                'CD_S_PRSW_SM07', 'CD_S_PRSW_10', 'CD_S_PRSW_SM10', 'CD_S_PRSW_PS11', 'CD_S_PRSW_SM11',
                'CD_K_PRSW_ACOD', 'CD_K_PRSW_ACO_D', 'CD_S_PRSW_ISS2', 'CD_K_PRSW_ACO4_S', 'CD_K_PRSW_ACO4_D']
        }];
    /**
     * 入口排序数组
     * @type {Array}
     */
    ns.entrySortArr = [];
//----------------------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------
    ns.wirePath = function (color) {
        var group = getLongestGroup();
        if (group == null) return;
        changeFill(group, color);
    }
    /**
     * 改变svg的填充颜色
     * @param svg
     * @param color
     */
    var waitCount = 0;
    // waitNodes 是等待执行的节点队列
    // startNodes 是起始的节点队列
    // stopKeys 是当前被阻塞的key值列表
    var waitNodes = [], startNodes = [], stopNodes = [], stopKeys = [];
    var waitInterval = null;
    var wireIndex = 0;
    var fillColor = "red";

    /**
     * 初始化wait队列
     */
    function initWaitNodes() {
        waitNodes = [], startNodes = [], stopNodes = [], stopKeys = [];
        wireIndex = 0;
    }

    /**
     * 启动电流处理线程
     */
    function wireThread() {
        if (waitInterval != null) {
            clearInterval(waitInterval);
            initWaitNodes();
        }
        var svg = document.getElementById("line_svg").getSVGDocument();
        var map = Snap(svg.getElementsByTagName("svg")[0]);
        waitInterval = setInterval(function () {
            if (waitNodes.length == 0) {
                if(stopNodes.length > 0){
                    var first = stopNodes[0];
                    showStartBtn(true,first.x1 - 10,first.y1-10);
                }else{
                    $('#btn_run').prop('disabled',false);
                    showStartBtn(false);
                }
                return;
            }
            $('#btn_run').prop('disabled',true);
            wireIndex = waitNodes[0].sort;
            //选中元素
            var selected = waitNodes.filter(function (node) {
                return node.sort == wireIndex;
            });
            //移除数组元素
            waitNodes = waitNodes.filter(function (node) {
                return node.sort != wireIndex;
            });
            var normalList = getNormalNodes(selected);
            //变色
            normalList.forEach(function (node) {
                changeColor(node, map);
            });
        }, 400);
    }

    /**
     * 处理运行的节点信息
     * @param selected
     * @returns {Array}
     */
    function getNormalNodes(selected) {
        //TODO:直接运行
        //return selected;
        //TODO:半路停止
        //选中节点分类，分成正常运行，与终止运行两种
        var normalList = [];
        selected.forEach(function (node) {
            if (node.action == actionEnum.stop) {
                if (startNodes.includes(node.key)) {
                    normalList.push(node);
                } else {
                    stopNodes.push(node);
                    stopKeys.push(node.key);
                }
            } else {
                if (stopKeys.includes(node.parent)) {
                    stopNodes.push(node);
                    stopKeys.push(node.key);
                } else {
                    normalList.push(node);
                }
            }
        });
        if (normalList.length == 0) {
            //本节点全部终止,则提前结束逻辑，将未处理完的节点全部移入终止节点中
            stopNodes = stopNodes.concat(waitNodes);
            waitNodes = [];
        }
        return normalList;
    }

    /**
     * 变更颜色
     * @param node
     */
    function changeColor(node, map) {
        var key = node.key;
        var el = map.select(node.type + '[entity-key="' + key + '"]');
        if (el == null) return;
        var len = el.getTotalLength();
        el.attr({
            stroke: fillColor,
            strokeWidth: 1,
            "stroke-dasharray": len + " " + len,
            "stroke-dashoffset": len
        }).animate({
            "stroke-dashoffset": 0,
            strokeWidth: 0.2
        }, 100, mina.easeinout);
    }

    /**
     * 电流处理
     * @param group
     * @param color
     */
    function changeFill(group, color) {
        //TODO:需要完善 运行按钮的 可用控制逻辑
        if (stopNodes.length > 0) {
            //说明电流逻辑已经中断
            startNodes = [stopNodes[0].key];//开通第一个中断节点
            waitNodes = stopNodes;
            fillColor = "red";
            stopNodes = [], stopKeys = [];
        } else {
            //初始化
            initWaitNodes();
            fillColor = color;
            waitNodes = group.nodes;
        }
    }


//----------------------------------------------------------------------------------------------------------
// ----------------------------------------------------------------------------------------------------------
    //--------- 启动按钮处理逻辑 begin ---------------------------------
    var startBtn = null;
    var btnIsShow = false;
    /**
     * 创建一个推进按钮
     * @param map
     * @constructor
     */
    function createStartBtn(map){

        //1.添加一个播放按钮
        startBtn = map.paper.image(Zq.Utility.GetPath('/static/svg/play_button.svg'),100,200,10,10).attr({
            opacity: 0.3,
            display:'none',
        });
        startBtn.mouseover(function(e){
            this.animate({
                opacity: 1,
                //x:300
            }, 100, mina.easeinout);
        }).mouseout(function(e){
            this.animate({
                opacity: 0.3
            }, 100, mina.easeinout);
        }).click(function(e){
            //运行未执行完的节点
            changeFill(null,null);
        });
        var draft = map.select("g[id=\"draft\"]");
        draft.append(startBtn);
    }

    /**
     * 启动按钮的处理逻辑
     * @param isShow
     */
    function showStartBtn(isShow,x,y){
        if(!isShow && btnIsShow == isShow) return;
        btnIsShow = isShow;
        if(isShow){
            startBtn.attr({
                display: 'block',
                x:x,
                y:y,
            });
        }else{
            startBtn.attr({
                display: 'none',
            });
        }
    }
    //--------- 启动按钮处理逻辑 end -----------------------------------
    /**
     * 获取最长的分组
     * @returns {*}
     */
    function getLongestGroup() {
        if (ns.entrySortArr.length == 0) return null;
        var longItem = ns.entrySortArr[0];
        var len = longItem.nodes.length;
        ns.entrySortArr.forEach(function (group) {
            if (group.nodes.length > len) {
                longItem = group;
                len = group.nodes.length;
            }
        });
        return longItem;
    }

    /**
     * 为svg元素排序
     * @param svg
     */
    ns.sort = function (svg) {
        ns.entrySortArr = [];
        var map = Snap(svg.getElementsByTagName("svg")[0]);
        createStartBtn(map);
        //整理 实体列表
        var entities = getEntities(map);
        //为实体信息排序
        //----- 1.查找入口 begin ----------------------------------
        var entryList = entities.filter(function(entity){
           return (entity.name=="CD_POT_PRSW_W1RR");
        });
        //2.入口循环组装排序
        if (entryList.length == 0) {
            //没有找到带箭头的图标，则以左上角的图标为起始点进行运算
            entryList = entities.filter(function (entity) {
                return (entity.name=="CD_W_PR_JUMPER");
            }).sort(function(a,b){return Number(a.x1)-Number(b.x1)});
            if(entryList.length>0) {
                var start = Number(entryList[0].x1);
                entryList = entryList.filter(function(entry){
                    return Number(entry.x1) == start;
                })
            }
        }
        if(entryList.length==0){
            entryList = entities.filter(function (entity) {
                return (entity.name=="CD_POT_PRSW_W1LR");
            });
        }
        //----- 查找入口 end ----------------------------------
        if(entryList.length==0) return;
        var startNodes = [];
        entryList.forEach(function (entry, index) {
            var key = entry.key;
            var entity = entities.find(function (a, i) {
                return a.key == key;
            });
            var node = JSON.parse(JSON.stringify(entity));
            node.sort = 1;
            startNodes.push(node);
        });
        //改造成所组起始点同时运行
        var group = {
            key: 'root',
            parent: 'root',
            nodes: startNodes
        };
        //设置异步执行
        setTimeout(function () {
            wrapNodes(group, startNodes, entities);
            ns.entrySortArr.push(group);
            console.log(ns.entrySortArr);
        }, 0);
        //排序结果
        //启动变色执行线程
        wireThread();
    }

    /**
     * 组装节点数据
     * @param group
     * @param currList
     * @param nodes
     */
    function wrapNodes(group, currList, nodes) {
        var nextList = [];
        currList.forEach(function (curr) {
            var nList = getNextList(group, curr, nodes);
            if (nList == null) return;
            //如果包含元器件信息，需要将4条边都包含进系统内
            //按序号排列好
            nList.forEach(function (next) {
                var temp = {};
                for (var p in next) {
                    temp[p] = next[p];
                }
                temp.sort = curr.sort + 1;
                temp.parent = curr.key;
                group.nodes.push(temp);
                nextList.push(temp);
            });
        })
        if (nextList.length == 0) return;
        //递归组装
        wrapNodes(group, nextList, nodes);
    }

    /**
     * 获取接下来的节点列表
     * @param curr
     * @param nodes
     * @returns {*}
     */
    function getNextList(group, curr, nodes) {
        var threshold = 2;
        var rad = curr.type == "use" ? threshold : 0;
        var cX1 = Number(curr.x1), cX2 = Number(curr.x2);
        var cY1 = Number(curr.y1), cY2 = Number(curr.y2);
        var cXMin = Math.min(cX1, cX2) - rad, cXMax = Math.max(cX1, cX2) + rad;
        var cYMin = Math.min(cY1, cY2) - rad, cYMax = Math.max(cY1, cY2) + rad;
        //处理有效的记录
        var nextList = nodes.filter(function (node) {
            if (node.key == curr.key) {
                return false;
            }
            var bExists = group.nodes.some(function (item) {
                return item.key == node.key;
            });
            if (bExists) return false;
            var nX1 = Number(node.x1), nX2 = Number(node.x2);
            var nY1 = Number(node.y1), nY2 = Number(node.y2);
            rad = node.type == "use" ? threshold : 0;
            var nXMin = Math.min(nX1, nX2) - rad, nXMax = Math.max(nX1, nX2) + rad;
            var nYMin = Math.min(nY1, nY2) - rad, nYMax = Math.max(nY1, nY2) + rad;
            var xIn = (nXMin >= cXMin && nXMin <= cXMax) || (nXMax >= cXMin && nXMax <= cXMax)
                || (nXMin <= cXMin && nXMax >= cXMax);
            var yIn = (nYMin >= cYMin && nYMin <= cYMax) || (nYMax >= cYMin && nYMax <= cYMax)
                || (nYMin <= cYMin && nYMax >= cYMax);
            return xIn && yIn;
        });
        return nextList;
    }

    /**
     * 整理实体列表
     * @param svg
     * @returns {*}
     */
    function getEntities(map) {
        var entities = [];
        var draft = map.select("g[id=\"draft\"]");
        if (!draft) return;
        var set = draft.selectAll('line');
        // 遍历填色
        set.forEach(function (element, index) {
            var key = guid();
            if (element.attr("entity-key") != null) {
                element.attr({"entity-key": key});
            } else {
                element.attr("entity-key", key);
            }
            entities.push({
                key: key,
                type: "line",
                x1: Number(element.attr("x1")),
                y1: Number(element.attr("y1")),
                x2: Number(element.attr("x2")),
                y2: Number(element.attr("y2"))
            });
        });
        var symbols = map.selectAll('use[entity-key]');
        symbols.forEach(function (element, index) {
            try {
                var key = element.attr("entity-key");
                if (!key) return;
                var trans = element.attr("transform").localMatrix;
                if (!trans) return;
                var x = trans.e;
                var y = trans.f;
                var box = element.original.getBBox();
                var config = getSymbolInfo(element);
                entities.push($.extend({
                    key: key, type: "use", x1: Number(x), y1: Number(y),
                    x2: Number(x) + box.width,
                    y2: Number(y) - box.height,
                }, config));
            }
            catch (e) {
                console.log(e);
            }
        });
        return entities;
    }

    /**
     * 获取图标信息
     * @param element
     * @returns {{name: null, action: number}}
     */
    function getSymbolInfo(element) {
        var el = {name: null, action: actionEnum.normal};
        var name = element.attr("xlink:href");
        if (name != null) {
            el.name = name.replace('#', '');
            //解析其行为
            for (var i = 0; i < symbolConfig.length; i++) {
                var config = symbolConfig[i];
                if (config.list.includes(el.name)) {
                    el.action = config.type;
                    return el;
                }
            }
        }
        return el;
    }

    //用于生成uuid
    function S4() {
        return (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
    }

    function guid() {
        return (S4() + S4() + "-" + S4() + "-" + S4() + "-" + S4() + "-" + S4() + S4() + S4());
    }
})(svg.resolve);

$(function () {
});