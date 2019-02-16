Zq.Utility.RegisterNameSpace("svg.resolve");
//闭包引入命名空间
(function (ns, undefined) {
    /**
     * 实体信息存储
     * @type {Array}
     */
    var entities = [];
    //入口排序数组
    ns.entrySortArr = [];

    ns.wirePath = function(color){
        var group =getLongestGroup();
        if(group==null) return;
        changeFill(group,color);
    }
    /**
     * 改变svg的填充颜色
     * @param svg
     * @param color
     */
    var waitCount = 0;

    function changeFill(group, color) {
        var svg = document.getElementById("line_svg").getSVGDocument();
        var map = Snap(svg.getElementsByTagName("svg")[0]);
        group.nodes.forEach(function(node){
            var key = node.key;
            var el = map.select(node.type + '[entity-key="'+ key +'"]');
            if(el==null) return;
            waitCount++;
            setTimeout(function () {
                var len = el.getTotalLength();
                el.attr({
                    stroke: color,
                    strokeWidth: 0.2,
                    "stroke-dasharray": len + " " + len,
                    "stroke-dashoffset": len
                }).animate({"stroke-dashoffset": 0}, 50,mina.easeinout);
            }, 5 * waitCount);
        })
    }
    /**
     * 获取最长的分组
     * @returns {*}
     */
    function getLongestGroup(){
        if(ns.entrySortArr.length==0) return null;
        var longItem = ns.entrySortArr[0];
        var len = longItem.nodes.length;
        ns.entrySortArr.forEach(function(group){
            if(group.nodes.length> len){
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
    ns.sort = function(svg){
        ns.entrySortArr = [];
        var map = Snap(svg.getElementsByTagName("svg")[0]);
        //整理 实体列表
        var entities = getEntities(map);
        //为实体信息排序
        //1.查找入口
        var entryList = [];
        map.selectAll('use[entity-key]').forEach(function (use,index) {
            if(use.attr("xlink:href")=="#CD_POT_PRSW_W1RR"){
                entryList.push(use);
            }
        })

        //2.入口循环组装排序
        entryList.forEach(function(entry,index){
            var key =entry.attr("entity-key");
            var entity = entities.find(function(a,index){
                return a.key == key;
            });
            var node = JSON.parse(JSON.stringify(entity));
            node.sort = 1;
            var group = {
                key:key,
                nodes:[node]
            };
            //wrapNodes;
            wrapNodes(group,node,entities);
            ns.entrySortArr.push(group);
        });
    }

    /**
     * 组装节点数据
     * @param group
     * @param curr
     * @param nodes
     */
    function wrapNodes(group,curr,nodes){
        var x = curr.x1,y = curr.y1;
        if(curr.type=="line"){
            x = curr.x2;
            y =curr.y2;
        }
        //处理有效的记录
        var nextList = nodes.filter(function(node){
            var b1 = (node.x1 ==x) && (node.y1 ==y) && (node.key!=curr.key);
            var b2 = group.nodes.some(function(item){
                return item.key == node.key;
            });
            return b1 && !b2;
        });
        if(nextList==null) return;
        nextList.forEach(function(next){
            var temp = {};
            for(var p in next){
                temp[p] = next[p];
            }
            temp.sort = curr.sort + 1;
            group.nodes.push(temp);
            wrapNodes(group,temp,nodes);
        })
    }

    /**
     * 整理实体列表
     * @param svg
     * @returns {*}
     */
    function getEntities(map){
        var entities = [];
        var draft = map.select("g[id=\"draft\"]");
        if(!draft)return;
        var set = draft.selectAll('line');
        // 遍历填色
        set.forEach(function(element, index) {
            var key = guid();
            if(element.attr("entity-key")!=null){
                element.attr({"entity-key":key });
            }else{
                element.attr("entity-key",key);
            }
            entities.push({
                key:key,
                type:"line",
                x1:element.attr("x1"),
                y1:element.attr("y1"),
                x2:element.attr("x2"),
                y2:element.attr("y2")
            });
        });
        var symbols = map.selectAll('use[entity-key]');
        symbols.forEach(function(element, index) {
            try{
                var key = element.attr("entity-key");
                if(!key)return;
                var trans = element.attr("transform").local;
                if(!trans) return;
                var arr = trans.replace("t","").split(",");
                if(arr.length<2) return;
                entities.push({
                    key:key,
                    type:"use",
                    x1:arr[0],
                    y1:arr[1]
                });
            }
            catch(e){
                console.log(e);
            }
        });
        return entities;
    }

    //用于生成uuid
    function S4() {
        return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
    }
    function guid() {
        return (S4()+S4()+"-"+S4()+"-"+S4()+"-"+S4()+"-"+S4()+S4()+S4());
    }
})(svg.resolve);

$(function () {
});