Zq.Utility.RegisterNameSpace("user.list");
//闭包引入命名空间
(function (ns, undefined) {
    //设置的实体
    ns.Entity = null;
    ns.Init = function () {
        $('#list').bootstrapTable({
            url: ('/user/getlist').geturl(),
            method: 'post',
            dataType: "json",
            contentType:"application/x-www-form-urlencoded",
            height: $(window).height() - 60,
            pagination: true, //分页
            silentSort: true, //自动记住排序项
            onlyInfoPagination: false,
            showFooter:false,
            striped:false,
            buttonsClass:"sm",
            locale: "zh-CN", //表格汉化
            search: false, //显示搜索框
            checkboxHeader: true,
            maintainSelected: true,
            clickToSelect: true,
            sidePagination: "server", //服务端处理分页
            pageSize: 25,
            uniqueId: "id",
            sortName: "createDate",
            sortOrder: "desc",
            queryParams: function (params) {
                $.extend(params, { searchKey: $("input[name='searchkey']").val()});
                return params;
            },
            onDblClickRow: function(tr,el) {
                ns.Edit(tr.id);
            },
            //showToggle:true,
            columns: [
                {
                    field: 'Number',
                    title: '序号',
                    width: 60,
                    align: 'center',
                    sortable: false,
                    formatter: function (value, row, index) {
                        return index + 1;
                    }
                },
                {
                    title: '主键',
                    field: 'id',
                    //align: 'center',
                    visible: false
                },
                {
                    title: '创建时间',
                    field: 'createDate',
                    align: 'center',
                    width: "15%",
                    cellStyle: Smart.Common.TitleFormat
                },
                {
                    title: '用户名',
                    field: 'name',
                    align: 'left',
                    width: "15%"
                },
                {
                    title: '登录名',
                    field: 'code',
                    align: 'left',
                    width: "15%"
                },
                {
                    title: '性别',
                    field: 'sex',
                    align: 'center',
                    width: "80px",
                    formatter: function (value, row, index) {
                        if(value) return "男";
                        return "女";
                    }
                },
                {
                    title: '操作',
                    align: 'center',
                    width: "150",
                    sortable: false,
                    formatter: function (value, row, index) {
                        var content = $("#list_opr_template").html();
                        content = content.replace("{edit}", "user.list.Edit('" + row.id + "')");
                        content = content.replace("{del}", "user.list.Delete('" + row.id + "')");
                        return content;
                    }
                    //width: "30%"
                }
            ]
        });
        //高度重置
        $(window).resize(function () {
            $('#list').bootstrapTable('resetView', { height: $(window).height() - 60 });
        });
        //注册回车事件
        Zq.Utility.EnterEventRegister($("input[name='searchkey']"), ns.ReFresh);
    };

    //列表刷新
    ns.ReFresh = function () {
        $('#list').bootstrapTable('refresh');
    }

    //新增方法
    ns.Add = function () {
        Zq.Utility.OpenModal({
            title: "新增用户",
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
            title: "编辑用户",
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
                type: "Get",
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
})(user.list);

$(function () {
    user.list.Init();
});