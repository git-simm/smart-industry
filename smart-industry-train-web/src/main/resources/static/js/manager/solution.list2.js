Zq.Utility.RegisterNameSpace("solution.list2");
//闭包引入命名空间
(function (ns, undefined) {
    //设置的实体
    ns.Entity = null;
    ns.Init = function () {
        $('#list').bootstrapTable({
            url: ('/solution/getlist').geturl(),
            method: 'post',
            dataType: "json",
            contentType: "application/x-www-form-urlencoded",
            height: $(window).height() - 60,
            pagination: true, //分页
            silentSort: true, //自动记住排序项
            onlyInfoPagination: false,
            showFooter: false,
            striped: false,
            buttonsClass: "sm",
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
                $.extend(params, {searchKey: $("input[name='searchkey']").val()});
                return params;
            },
            onClickRow: function (tr, el) {
                $("tbody>tr[class='selected']", '#list').removeClass("selected");
                $(el).addClass("selected");
            },
            onDblClickRow: function (tr, el) {
                ns.Edit(tr.id);
            },
            showToggle: false,
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
                    title: '创建人',
                    field: 'createBy',
                    align: 'left',
                    width: "20%",
                    cellStyle: function (value, row, index, field) {
                        return {
                            title: value
                        };
                    }
                },
                {
                    title: '创建时间',
                    field: 'createDate',
                    align: 'center',
                    width: "20%",
                    cellStyle: function (value, row, index, field) {
                        return {
                            title: value
                        };
                    }
                },
                {
                    title: '方案名',
                    field: 'name',
                    align: 'left',
                    width: "40%"
                },
                {
                    title: '基准文件',
                    field: 'IP',
                    width: "20%"
                },
                {
                    title: '设计文件',
                    field: 'DbName',
                    width: "20%"
                },
                {
                    title: '清单',
                    field: 'User',
                    width: "20%"
                },
                {
                    title: '操作',
                    align: 'center',
                    width: "150",
                    sortable: false,
                    formatter: function (value, row, index) {
                        var content = $("#list_opr_template").html();
                        content = content.replace("{edit}", "solution.list2.Edit('" + row.id + "')");
                        content = content.replace("{del}", "solution.list2.Delete('" + row.id + "')");
                        return content;
                    }
                    //width: "30%"
                }
            ]
        });
        //高度重置
        $(window).resize(function () {
            $('#list').bootstrapTable('resetView', {height: $(window).height() - 60});
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
        var node = solution.list.GetSelectedNode();
        if (node == null) {
            layer.alert("请选择一个方案分类!");
            return;
        };
        Zq.Utility.OpenModal({
            title: "新增设计方案",
            maxmin: false,
            area: ['700px', '500px'],
            content: [('/solution/addwin').geturl(), 'yes'],
            success: openSuc,
            end: function () {
                if (SmartMonitor.Common.GetResult() === true) {
                    ns.ReFresh();
                    Zq.Utility.Msg("保存成功");
                }
            }
        });
    };
    //打开成功，赋值到字界面
    function openSuc(layero) {
        var node = solution.list.GetSelectedNode();
        var child = layero.find("iframe")[0].contentWindow;
        if (!child) return;
        child.solution.edit.setData(node);
    }

    //编辑方法
    ns.Edit = function (id) {
        Zq.Utility.OpenModal({
            title: "编辑设计方案",
            maxmin: false,
            area: ['700px', '500px'],
            content: [('/solution/editwin?id=' + id).geturl(), 'yes'],
            success: openSuc,
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
                dataType: "json",
                url: ("/solution/del?id=" + id).geturl(),
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
})(solution.list2);

$(function () {
    solution.list2.Init();
});