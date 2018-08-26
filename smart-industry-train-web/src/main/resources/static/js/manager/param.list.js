Zq.Utility.RegisterNameSpace("param.list");
//闭包引入命名空间
(function (ns, undefined) {
    //设置的实体
    ns.Entity = null;
    ns.CurrentGroupId = null;
    ns.Init = function () {
        //选项列表
        $('#optionlist').bootstrapTable({
            url: ('/param/getOptionList').geturl(),
            method: 'post',
            dataType: "json",
            contentType:"application/x-www-form-urlencoded",
            height: 200,
            pagination: false, //分页
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
            sortName: "sort",
            sortOrder: "asc",
            queryParams: function (params) {
                $.extend(params, { searchKey: ns.CurrentGroupId});
                return params;
            },
            onDblClickRow: function(tr,el) {
                ns.EditOption(tr.id);
            },
            //showToggle:true,
            columns: [
                {
                    field: 'number',
                    title: '序号',
                    width: 60,
                    align: 'center',
                    sortable: false,
                    formatter: function (value, row, index) {
                        return index + 1;
                    }
                },
                {title: '主键',field: 'id',visible: false},
                {title: '值',field: 'value',align: 'left',width: "30%"},
                {title: '显示',field: 'showVal',align: 'left',width: "30%"},
                {title: '排序码',field: 'sort',align: 'center',width: "15%",titleTooltip:"用于标识字典项在列表中的顺序"},
                {
                    title: '操作',
                    align: 'center',
                    width: "25%",
                    sortable: false,
                    formatter: function (value, row, index) {
                        var content = $("#list_opr_template").html();
                        content = content.replace("{edit}", "param.list.EditOption('" + row.id + "')");
                        content = content.replace("{del}", "param.list.DelOption('" + row.id + "')");
                        return content;
                    }
                    //width: "30%"
                }
            ]
        });
        //分组列表
        $('#list').bootstrapTable({
            url: ('/param/getlist').geturl(),
            method: 'post',
            dataType: "json",
            contentType:"application/x-www-form-urlencoded",
            height: $(window).height() - 300,
            pagination: false, //分页
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
            onClickRow: function(tr,el) {
                $("tbody>tr[class='selected']", '#list').removeClass("selected");
                $(el).addClass("selected");

            	$(".group-options").html("["+tr.name+"]值列表");
            	ns.ChoosedGroup(tr.id);
            },
            onLoadSuccess:function(data){
            	//首行选中
            	var $first = $('#list tbody>tr[data-uniqueid]:eq(0)');
            	if($first.length>0){
            		$(".group-options").html("["+$first.find("td:eq(3)").html()+"]值列表");
            		ns.ChoosedGroup($first.data("uniqueid"));
            		$first.click();
            	}else{
            		$(".group-options").html("值列表");
            		ns.ChoosedGroup(null);
            	}
            },
            //showToggle:true,
            columns: [
                {
                    field: 'number',
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
                    width: "10%",
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
                    width: "15%",
                    cellStyle: function (value, row, index, field) {
                        return {
                            title: value
                        };
                    }
                },
                {
                    title: '字典项',
                    field: 'name',
                    align: 'left',
                    width: "15%"
                },
                {
                    title: '类型',
                    field: 'paramType',
                    align: 'center',
                    width: "10%"
                },
                {
                    title: '备注',
                    field: 'remark',
                    align: 'left',
                    width: "20%"
                },
                {
                    title: '默认值',
                    field: 'defaultVal',
                    align: 'left',
                    width: "15%"
                },
                {
                    title: '操作',
                    align: 'center',
                    width: "150",
                    sortable: false,
                    formatter: function (value, row, index) {
                        var content = $("#list_opr_template").html();
                        content = content.replace("{edit}", "param.list.Edit('" + row.id + "')");
                        content = content.replace("{del}", "param.list.Delete('" + row.id + "')");
                        return content;
                    }
                    //width: "30%"
                }
            ]
        });
        //高度重置
        $(window).resize(function () {
            $('#list').bootstrapTable('resetView', { height: $(window).height() - 300 });
            $('#optionlist').bootstrapTable('resetView');
        });
        //注册回车事件
        Zq.Utility.EnterEventRegister($("input[name='searchkey']"), ns.ReFresh);
    };

    ns.ChoosedGroup = function(groupId){
    	if(groupId!=ns.CurrentGroupId){
    		ns.CurrentGroupId = groupId;
        	ns.ReFreshOption();
    	}
    }
    
    //列表刷新
    ns.ReFresh = function () {
        $('#list').bootstrapTable('refresh');
    }
    
    //新增方法
    ns.Add = function () {
        Zq.Utility.OpenModal({
            title: "新增组",
            maxmin: false,
            area: ['600px', '300px'],
            content: [('/param/addwin').geturl(), 'yes'],
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
            title: "组编辑",
            maxmin: false,
            area: ['600px', '300px'],
            content: [('/param/editwin?id=' + id).geturl(), 'yes'],
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
                type: "post",
                dataType:"json",	
                url: ("/param/delete?id=" + id).geturl(),
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
    
  //列表刷新
    ns.ReFreshOption = function () {
        $('#optionlist').bootstrapTable('refresh');
    }

    //新增方法
    ns.AddOption = function () {
    	if(!ns.CurrentGroupId>''){
    		layer.alert("请先选择一个分组项");
    		return;
    	}
        Zq.Utility.OpenModal({
            title: "新增项",
            maxmin: false,
            area: ['600px', '200px'],
            content: [('/param/addOptionWin?groupid='+ns.CurrentGroupId).geturl(), 'yes'],
            end: function () {
                if (SmartMonitor.Common.GetResult() === true) {
                    ns.ReFreshOption();
                    Zq.Utility.Msg("保存成功");
                }
            }
        });
    };

    //编辑方法
    ns.EditOption = function (id) {
        Zq.Utility.OpenModal({
            title: "项编辑",
            maxmin: false,
            area: ['600px', '200px'],
            content: [('/param/editOptionWin?id=' + id).geturl(), 'yes'],
            end: function () {
                if (SmartMonitor.Common.GetResult() === true) {
                    ns.ReFreshOption();
                    Zq.Utility.Msg("保存成功");
                }
            }
        });
    };
    //删除
    ns.DelOption = function (id) {
        //询问框
        layer.confirm('确定要删除？', function () {
            layer.closeAll('dialog');
            $.ajax({
                async: false,
                type: "post",
                dataType:"json",	
                url: ("/param/delOption?id=" + id).geturl(),
                success: function (result) {
                    if (result > 0) {
                        //删除成功，刷新列表
                        ns.ReFreshOption();
                        Zq.Utility.Msg("删除成功");
                    }
                }
            });
        });
    };
})(param.list);

$(function () {
    param.list.Init();
});