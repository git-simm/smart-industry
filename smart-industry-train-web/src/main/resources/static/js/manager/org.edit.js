Zq.Utility.RegisterNameSpace("org.edit");
//闭包引入命名空间
(function (ns, undefined) {
    //设置的实体
    ns.Entity = null;
    //新增方法
    ns.Add = function () {
        Zq.Utility.OpenModal({
            title: "新增组织",
            maxmin: false,
            area: ['700px', '400px'],
            content: [('/user/addwin').geturl(), 'yes'],
            end: function () {
                if (SmartMonitor.Common.GetResult() === true) {
                    ns.ReFresh();
                    Zq.Utility.Msg("保存成功");
                }
            }
        });
    };

    //编辑方法
    ns.Edit = function (id) {
        Zq.Utility.OpenModal({
            title: "编辑组织",
            maxmin: false,
            area: ['700px', '400px'],
            content: [('/user/editwin?id=' + id).geturl(), 'yes'],
            end: function () {
                if (SmartMonitor.Common.GetResult() === true) {
                    ns.ReFresh();
                    Zq.Utility.Msg("保存成功");
                }
            }
        });
    };
    //删除
    ns.Delete = function (id) {
        //询问框
        layer.confirm('确定要删除？', function () {
            layer.closeAll('dialog');
            $.ajax({
                async: false,
                type: "Post",
                dataType:"json",	
                url: ("/user/delete?id=" + id).geturl(),
                success: function (result) {
                    if (result > 0) {
                        //删除成功，刷新列表
                        ns.ReFresh();
                        Zq.Utility.Msg("删除成功");
                    }
                }
            });
        });
    };
})(org.edit);

$(function () {
    org.edit.Init();
    console.log(SmartMonitor.Common.GetData());
});