Zq.Utility.RegisterNameSpace("test.run");
//闭包引入命名空间
(function (ns, undefined) {
    ns.layout = null;
    ns.init = function () {
        ns.layout = $("body").layout(
            {
                west__size: 200,
                south__Size: 140,
                west__initClosed: false,
                //west__enableCursorHotkey: false,
                //south__enableCursorHotkey: false,
                //west__spacing_open: 0,
                //west__spacing_closed: 0,
                stateManagement__enabled: true,
                spacing_open: 10, // ALL panes
                spacing_closed: 20, // ALL panes
                south__spacing_open: 10,
                south__spacing_closed: 20,
                togglerContent_open: "<div><</div>", //pane打开时，边框按钮中需要显示的内容可以是符号"<"等。需要加入默认css样式.ui-layout-toggler .content
                togglerContent_closed: "<div>>></div>", //pane关闭时，同上。
                south__togglerContent_open: "<div>︾</div>", //pane打开时，边框按钮中需要显示的内容可以是符号"<"等。需要加入默认css样式.ui-layout-toggler .content
                south__togglerContent_closed: "<div>︽</div>", //pane关闭时，同上。
                south__minSize: 140,
                south__maxSize: 140,
                west__minSize: 200,
                west__maxSize: 300,
                onresize: function () {
                    //$('#list').bootstrapTable('resetWidth');
                    svgPanZoom.resize();
                }
            }
        );
    };
})(test.run);

$(function () {
    test.run.init();
});