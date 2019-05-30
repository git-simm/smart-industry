Zq.Utility.RegisterNameSpace("check.grid");
//闭包引入命名空间
(function (ns, undefined) {
    ns.init = function () {
        //初始化一下excel表格
        $('#list').bootstrapTable({
            height: $(window).height() - 180,
            sortable:false,
            pagination: false, //分页
            silentSort: false, //自动记住排序项
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
            // onDblClickRow: function(tr,el) {
            //     ns.Edit(tr.id);
            // },
            //showToggle:true,
            // rowAttributes:function(row,index){
            //     return { state: row.state };
            // },
            columns: [
                {
                    field: 'number',
                    title: '序号',
                    width: 60,
                    align: 'center',
                    sortable: false,
                    // formatter: function (value, row, index) {
                    //     return index + 1;
                    // }
                },
                {
                    title: 'id',
                    field: 'id',
                    //align: 'center',
                    visible: false
                },
                {
                    title: '车型(Plant)',
                    field: 'Plant',
                    align: 'left',
                    width: "120px"
                },
                {
                    title: '位置(Location)',
                    field: 'Location',
                    align: 'left',
                    width: "120px"
                },
                {
                    title: '电缆等级(Wire_Grade)',
                    field: 'Wire_Grade',
                    align: 'left',
                    width: "150px"
                },
                {
                    title: '屏蔽代码(Cable)',
                    field: 'Cable',
                    align: 'left',
                    width: "120px"
                },
                {
                    title: '线芯(Core_Number)',
                    field: 'Core_Number',
                    align: 'left',
                    width: "140px"
                },
                {
                    title: '线号(Wire_Number)',
                    field: 'Wire_Number',
                    align: 'left',
                    width: "140px"
                },
                {
                    title: '电缆型号(Cable_type)',
                    field: 'Cable_type',
                    align: 'left',
                    width: "150px"
                },
                {
                    title: '版本(Revison_Modify)',
                    field: 'Revison_Modify',
                    align: 'left',
                    width: "150px"
                },
                {
                    title: '系统(Representation)',
                    field: 'Representation',
                    align: 'left',
                    width: "150px"
                },
                {
                    title: '始端车型(Dest_1_Plant)',
                    field: 'Dest_1_Plant',
                    align: 'left',
                    width: "170px"
                },
                {
                    title: '始端位置(Dest_1_Location)',
                    field: 'Dest_1_Location',
                    align: 'left',
                    width: "200px"
                },
                {
                    title: '始端名称(Dest_1_Item)',
                    field: 'Dest_1_Item',
                    align: 'left',
                    width: "150px"
                },
                {
                    title: '始端点位(Dest_1_Connector)',
                    field: 'Dest_1_Connector',
                    align: 'left',
                    width: "200px"
                },
                {
                    title: '始端分配点（Dest_1_Pin_assign）',
                    field: 'Dest_1_Pin_assign',
                    align: 'left',
                    width: "240px"
                },
                {
                    title: '始端连接线型号(Dest_1_Endwire_Type)',
                    field: 'Dest_1_Endwire_Type',
                    align: 'left',
                    width: "220px"
                },
                {
                    title: '末端车型(Dest_2_Plant)',
                    field: 'Dest_2_Plant',
                    align: 'left',
                    width: "180px"
                },
                {
                    title: '末端位置(Dest_2_Location)',
                    field: 'Dest_2_Location',
                    align: 'left',
                    width: "200px"
                },
                {
                    title: '末端名称(Dest_2_Item)',
                    field: 'Dest_2_Item',
                    align: 'left',
                    width: "150px"
                },
                {
                    title: '末端点位(Dest_2_Connector)',
                    field: 'Dest_2_Connector',
                    align: 'left',
                    width: "180px"
                },
                {
                    title: '末端分配点(Dest_2_Pin_assign)',
                    field: 'Dest_2_Pin_assign',
                    align: 'left',
                    width: "200px"
                },
                {
                    title: '末端连接线型号(Dest_2_Endwire_Type)',
                    field: 'Dest_2_Endwire_Type',
                    align: 'left',
                    width: "240px"
                },
                // {
                //     title: '状态',
                //     field: 'state',
                //     align: 'left',
                //     width: "15%",
                //     formatter: function (value, row, index) {
                //         if(value=="-1") return "缺失";
                //         if(value =="0") return "正常";
                //         if(value =="1") return "多出";
                //     }
                // }
            ]
        });
        //高度重置
        $(window).resize(function () {
            $('#list').bootstrapTable('resetView', { height: $(window).height() - 180 });
        });
    };
    //----------- excel清单交互方案 begin ---------------------
    ns.currExcel = null;
    ns.currCheckData = {};
    ns.onLoadCheckData = null;
    //获取excel数据
    ns.getExcelData = function(fileId,node,callback){
        ns.currExcel = node;
        $('#hid_fileId').val(fileId);
        $.ajax({
            async: false,
            url: Zq.Utility.GetPath("/solution/getCheckData"),
            data:{fileId : fileId, solutionId:$('#hid_solutionId').val()},
            success:function(data){
                ns.currCheckData = data;
                if(callback){
                   callback(data);
                }
                if (ns.onLoadCheckData){
                    ns.onLoadCheckData(data);
                }
            }
        });
    }
    /**
     * excel导出功能
     */
    ns.export = function(){
        var headers = ['Wire_Number','Dest_1_Item','Dest_1_Connector','Dest_2_Item','Dest_2_Connector','Dest_2_Pin_assign'];
        var data = $('#list').bootstrapTable('getData');
        var list = data.map(function(item){
            return headers.map(function(h){
                return item[h];
            });
        });
        exportUtil.export(headers,list,ns.currExcel.fileName);
    }
//----------- excel清单交互方案 end ---------------------
})(check.grid);

$(function () {
    check.grid.init();
});