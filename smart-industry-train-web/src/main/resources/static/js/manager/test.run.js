Zq.Utility.RegisterNameSpace("test.run");
//闭包引入命名空间
(function (ns, undefined) {
    ns.init = function () {
        thumbnailViewer({mainViewId: 'line_svg',thumbViewId: 'thumbView'});
    };
})(test.run);

$(function () {
    test.run.init();
});