Zq.Utility.RegisterNameSpace("svg.resolve");
//闭包引入命名空间
(function (ns, undefined) {
    /**
     * 实体信息存储
     * @type {Array}
     */
    var entities = [];
    /**
     * 为svg元素排序
     * @param svg
     */
    ns.sort = function(svg){
        var map = Snap(svg.getElementsByTagName("svg")[0]);
        var draft = map.select("g[id=\"draft\"]");
        if(!draft)return;
        var set = draft.selectAll('line');
        // 遍历填色
        set.forEach(function(element, index) {
            var key = guid();
            element.attr("key",key);
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
                    type:"symbol",
                    x1:arr[0],
                    y1:arr[1]
                });
            }
            catch(e){
                console.log(e);
            }
        });
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