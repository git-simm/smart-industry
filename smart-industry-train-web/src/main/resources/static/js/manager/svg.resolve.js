Zq.Utility.RegisterNameSpace("svg.resolve");
//闭包引入命名空间
(function (ns, undefined) {
    /**
     * 实体信息存储
     * @type {Array}
     */
    var entities = [];
    var timeoutList = [];
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
            timeoutList.push(setTimeout(function () {
                var len = el.getTotalLength();
                el.attr({
                    stroke: color,
                    strokeWidth: 0.2,
                    "stroke-dasharray": len + " " + len,
                    "stroke-dashoffset": len
                }).animate({"stroke-dashoffset": 0}, 50,mina.easeinout);
            }, 500 * node.sort));
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
        timeoutList.forEach(function(a){
            clearTimeout(a);
        });
        timeoutList = [];
        var map = Snap(svg.getElementsByTagName("svg")[0]);
        //整理 实体列表
        var entities = getEntities(map);
        console.log(entities);
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
            wrapNodes(group,node,entities);
            ns.entrySortArr.push(group);
        });
        //排序结果
        console.log(ns.entrySortArr);
    }

    /**
     * 组装节点数据
     * @param group
     * @param curr
     * @param nodes
     */
    function wrapNodes(group,curr,nodes){
        var x = curr.x1,y = curr.y1;
        if(curr.direction==1){
            x = curr.x2;
            y =curr.y2;
        }else if(curr.direction==2){
            x = curr.x1;
            y =curr.y1;
        }
        x = Number(x),y = Number(y);
        //处理有效的记录
        var nextList = nodes.filter(function(node){
            if(node.key==curr.key){
                return false;
            }
            var bExists = group.nodes.some(function(item){
                return item.key == node.key;
            });
            if(bExists) return false;
            //1.正向验证
            var bX1 = rangeValid(node.x1,x);
            var bY1 = rangeValid(node.y1,y);
            var r = bX1 && bY1;
            if(r){
                //正方向
                node.direction = 1;
                return r;
            }
            //2.逆向验证
            var bX2 = rangeValid(node.x2,x);
            var bY2 = rangeValid(node.y2,y);
            r = bX2 && bY2;
            if(r){
                //反方向
                node.direction = 2;
                return r;
            }
            //3.中间连接验证
            // r = centerLink(node,x,y,curr);
            // if(r){
            //     return r;
            // }
            //4.挂载多元素的关系查找
            return multiLink(node,curr);
        });
        if(nextList==null) return;
        //如果包含元器件信息，需要将4条边都包含进系统内
        var newList = [],useArr =[];
        for(var i=0;i<nextList.length;i++){
            var node = nextList[i];
            if(node.type=="use"){
                //已经处理过了，则退出
                if(useArr.includes(node.key)) continue;
                useArr.push(node.key);
                //得到关于元素组的所有信息
                var list = nodes.filter(function(n){
                    return n.key==node.key;
                });
                newList = newList.concat(list);
            }else{
                newList.push(node);
            }
        }
        //按序号排列好
        var tempList = [];
        newList.forEach(function(next){
            var temp = {};
            for(var p in next){
                temp[p] = next[p];
            }
            temp.sort = curr.sort + 1;
            group.nodes.push(temp);
            tempList.push(temp);
        });
        //递归组装
        tempList.forEach(function(next){
            wrapNodes(group,next,nodes);
        })
    }

    /**
     * 是否为中间连接
     */
    function centerLink(node,x,y,curr,cycle){
        if(curr.type=="use" && !cycle){
            var result = centerLink(node,Number(curr.x1),Number(curr.y1),curr,true);
            if(result){
                node.direction = 2;
            }else{
                node.direction = 1;
                result = centerLink(node,Number(curr.x2),Number(curr.y2),curr,true);
            }
            return result;
        }
        var x1 = Number(node.x1),x2= Number(node.x2),y1= Number(node.y1),y2= Number(node.y2);
        var max=0,min=0,r = false;
        if(x1==x2 && x1==x){
            max = Math.max(y1,y2);
            min = Math.min(y1,y2);
            r = (max>=y && min<=y);
        }else if(y1==y2 && y1==y){
            max = Math.max(x1,x2);
            min = Math.min(x1,x2);
            r = (max>=x && min<=x);
        }
        if(r){
            node.direction = 3;
        }
        return r;
    }

    /**
     * 一个元素上搭载多个连接点
     * @param node
     * @param x
     * @param y
     * @param curr
     * @param cycle
     */
    function multiLink(node,curr,cycle){
        var x1 = Number(curr.x1),x2= Number(curr.x2),y1= Number(curr.y1),y2= Number(curr.y2);
        var nx1 = Number(node.x1),nx2= Number(node.x2),ny1= Number(node.y1),ny2= Number(node.y2);
        var r = false,direction=1;
        var maxX = Math.max(x1,x2),minX = Math.min(x1,x2);
        var maxY = Math.max(y1,y2),minY = Math.min(y1,y2);
        var maxNX = Math.max(nx1,nx2),minNX = Math.min(nx1,nx2);
        var maxNY = Math.max(ny1,ny2),minNY = Math.min(ny1,ny2);
        if((x1==x2) && (nx1==nx2) && (x1==nx1)){
            //1.判断x轴上的重叠
            //正向 y轴挂载多节点
            r = (maxY>= ny1 && minY<= ny1);
            //正向挂载
            direction =1;
            if(!r){
                //反向挂载
                r = (maxY>= ny2 && minY<= ny2);
                direction =2;
            }
            if(!r){
                //反向包含
                r = (maxNY>= y1 && minNY<= y1);
                direction =1;
            }
            if(!r){
                //反向包含
                r = (maxNY>= y2 && minNY<= y2);
                direction =2;
            }
        }else if((y1==y2) && (ny1==ny2) && (y1==ny1)){
            //2.判断y轴上的重叠
            //正向 y轴挂载多节点
            r = (maxX>= nx1 && minX<= nx1);
            //正向挂载
            direction =1;
            if(!r){
                //反向挂载
                r = (maxX>= nx2 && minX<= nx2);
                direction =2;
            }
            if(!r){
                //反向包含
                r = (maxNX>= x1 && minNX<= x1);
                direction =1;
            }
            if(!r){
                //反向包含
                r = (maxNX>= x2 && minNX<= x2);
                direction =2;
            }
        }//3.垂直挂接
        else if(x1==x2 && x1==nx1){
            //正向 y轴挂载多节点
            r = (maxY>= ny1 && minY<= ny1);
            direction =1;
        }else if(y1==y2 && y1==ny1){
            //正向 x轴挂载多节点
            r = (maxX>= nx1 && minX<= nx1);
            direction =1;
        }else if(x1==x2 && x1 ==nx2){
            //逆向 y轴挂载多节点
            r = (maxY>= ny2 && minY<= ny2);
            direction =2;
        }else if(y1==y2 && y1==ny2){
            //逆向 x轴挂载多节点
            r = (maxX>= nx2 && minX<= nx2);
            direction =2;
        }
        if(r){
            node.direction = direction;
        }
        return r;
    }
    /**
     * 范围验证
     * @param val
     * @param base
     * @returns {boolean}
     */
    function rangeValid(val,base){
        val = Number(val);
        base = Number(base);
        var rad = 2;
        return (val -(base-rad) >0 ) && (val - (base + rad)<= 0);
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
                var box = element.original.getBBox();
                //y轴扩展
                entities.push({
                    key:key,
                    type:"use",
                    x1:arr[0],
                    y1:arr[1],
                    x2:arr[0],
                    y2:Number(arr[1])-box.height,
                });
                //x轴扩展
                entities.push({
                    key:key,
                    type:"use",
                    x1:arr[0],
                    y1:arr[1],
                    x2:Number(arr[0])+ box.width,
                    y2:arr[1],
                });
                entities.push({
                    key:key,
                    type:"use",
                    x1:Number(arr[0])+ box.width,
                    y1:arr[1],
                    x2:Number(arr[0])+ box.width,
                    y2:Number(arr[1])-box.height,
                });
                entities.push({
                    key:key,
                    type:"use",
                    x1:arr[0],
                    y1:Number(arr[1])-box.height,
                    x2:Number(arr[0])+ box.width,
                    y2:Number(arr[1])-box.height,
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