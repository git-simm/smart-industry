Zq.Utility.RegisterNameSpace("test.run");
//闭包引入命名空间
(function (ns, undefined) {
    ns.init = function () {
        thumbnailViewer({
            mainViewId: 'line_svg',
            thumbViewId: 'thumbView',
            onMainViewShown: function (svg, main) {
                main.zoom(2);
            }
        });
    };
})(test.run);

$(function () {
    test.run.init();
});