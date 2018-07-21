Zq.Utility.RegisterNameSpace("boot.test.index");
//闭包引入命名空间
(function (ns, undefined) {
    //设置的实体
    ns.Entity = null;
    ns.Init = function () {
        alert("初始化成功");
    }
})(boot.test.index);

$(function () {
    boot.test.index.Init();
});