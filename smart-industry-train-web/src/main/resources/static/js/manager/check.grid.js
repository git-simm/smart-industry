Zq.Utility.RegisterNameSpace("check.grid");
//闭包引入命名空间
(function (ns, undefined) {
    ns.columns = [
        {
            field: 'number',
            title: '行号',
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
            width: "120px",
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
    ];
    ns.ruleConfig = [
        {
            key:1,
            name:'线号缺失',
            requireds:['Wire_Number','Dest_1_Location','Dest_1_Item'],
            desc:'当【线号(Wire_Number)】为空时，【始端名称】必须都要“-TB”开头才是正常数据，其余的都是异常数据并展示。'
        },
        {
            key:2,
            name:'车型缺失',
            requireds:['Wire_Number','Plant'],
            desc:'当【线号(Wire_Number)】不为空时，【车型(Plant)】必须不为空才是正常数据，其余的都是异常数据并展示。'
        },
        {
            key:3,
            name:'电压等级缺失',
            requireds:['Wire_Number','Wire_Grade'],
            desc:'当【线号(Wire_Number)】不为空时，【电缆等级(Wire_Grade)】必须不为空才是正常数据，其余的都是异常数据并展示。'
        },
        {
            key:4,
            name:'屏蔽纤芯缺失',
            requireds:['Cable','Core_Number'],
            desc:'当【屏蔽代码(Cable)】不为空时，【线芯(Core_Number)】必须不为空才是正常数据，其余的都是异常数据并展示。'
        },
        {
            key:5,
            name:'电缆型号缺失',
            requireds:[],
            desc:''
        },
        {
            key:6,
            name:'始末端位置缺失',
            requireds:['Wire_Number','Dest_1_Location'],
            desc:'当【线号(Wire_Number)】不为空时，【始端位置(Dest_1_Location)】必须不为空才是正常数据，其余的都是异常数据并展示。'
        },
        {
            key:7,
            name:'始末端名称缺失',
            requireds:['Wire_Number','Dest_1_Item'],
            desc:'当【线号(Wire_Number)】不为空时，【始端名称(Dest_1_Item)】必须不为空才是正常数据，其余的都是异常数据并展示。'
        },
        {
            key:8,
            name:'线号重复',
            requireds:['Wire_Number','Plant'],
            desc:'当【车型(Plant)】相同时，相同的【线号(Wire_Number)】只能有两条记录才是正常数据，其余的都是异常数据并展示。' +
            '比如车型是“MP”，线号是“1387”只能是两条记录才是正确的，其余的都错误。'
        },
        {
            key:9,
            name:'整车车型错误',
            requireds:['Dest_1_Plant','Dest_2_Plant','Plant'],
            desc:'当【始端车型(Dest_1_Plant)】=【末端车型（Dest_2_Plant）】时，【车型(Plant)】必须跟【始端车型(Dest_1_Plant)】和【末端车型（Dest_2_Plant）】一致。' +
            '比如【始端车型(Dest_1_Plant)】和【末端车型（Dest_2_Plant）】都是"MP"，则【车型(Plant)】是“MP”才是正确的，其余的都错误。'
        },
        {
            key:10,
            name:'始末端名称的位置唯一',
            requireds:['Dest_1_Location','Dest_1_Item'],
            desc:'当【始端位置(Dest_1_Location)】与【始端名称(Dest_1_Item)】的值是一一对应。' +
            '比如【始端位置(Dest_1_Location)】的值为“+RO_END1”，则【始端名称(Dest_1_Item)】的值必须是唯一值（-04SACV1CN12）才是正确的，其余的都错误。'
        },
        {
            key:11,
            name:'始末端名称错误',
            requireds:['Cable','Dest_1_Item'],
            desc:'当【屏蔽代码(Cable)】不为空时，按照【屏蔽代码(Cable）】与【始端名称(Dest_1_Item)】的值分组的值小于等于“2”是正确的，' +
            '否则为错误的。比如【屏蔽代码(Cable)】的值为“BL0001”，则【始端名称(Dest_1_Item)】的值最多有两种（“-04JC21_1“）才是正确的，其余的都错误。'
        },
        {
            key:12,
            name:'短接片PIN错误',
            requireds:[],
            desc:''
        },
        {
            key:13,
            name:'屏蔽纤芯错误',
            requireds:[],
            desc:''
        },
        {
            key:14,
            name:'始末端连接件型号错误',
            requireds:['Dest_1_Endwire_Type','Cable_type'],
            desc:'当【始端连接线型号(Dest_1_Endwire_Type)】以“I”开头时，提供“I”后面的第一个分割项数字必须包含在【电缆型号(Cable_type)】里是正确的，否则为错误的。' +
            '比如【始端连接线型号(Dest_1_Endwire_Type)】的值为“I/0.5/8”，则“0.5”必须在其【电缆型号(Cable_type)】中有包含有。'
        },
        {
            key:15,
            name:'端子排过插',
            requireds:[],
            desc:''
        },
        {
            key:16,
            name:'连接器过插',
            requireds:[],
            desc:''
        },
        {
            key:17,
            name:'电缆线芯在端子排上过远',
            requireds:[],
            desc:''
        },
        {
            key:18,
            name:'始末点位缺失',
            requireds:[],
            desc:''
        },
    ];
    ns.init = function () {
        //初始化一下excel表格
        $('#list').bootstrapTable({
            height: $(window).height() - 220,
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
            columns: ns.columns
        });
        //高度重置
        $(window).resize(function () {
            $('#list').bootstrapTable('resetView', { height: $(window).height() - 220 });
        });
    };
    //----------- excel清单交互方案 begin ---------------------
    ns.currExcel = {};
    ns.currCheckData = {};
    ns.onLoadCheckData = null;
    ns.cache = new Map();
    //获取excel数据
    ns.getExcelData = function(fileId,node,callback){
        ns.currExcel = node;
        $('#hid_fileId').val(fileId);
        if (ns.cache.has(fileId)){
            var data = ns.cache.get(fileId);
            ns.currCheckData = data;
            if(callback){
                callback(data);
            }
            if (ns.onLoadCheckData){
                ns.onLoadCheckData(data);
            }
            return;
        }
        $.ajax({
            async: false,
            url: Zq.Utility.GetPath("/solution/getCheckData"),
            data:{fileId : fileId, solutionId:$('#hid_solutionId').val()},
            success:function(data){
                ns.cache.set(fileId,data);
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
        Smart.Common.confirm('将导出excel文件, 是否继续?',function () {
            //ns.currCheckData 转换成多sheet数据
            var showCols = ns.columns.filter(function (column) {
                return column.visible == null || column.visible;
            });
            var showFields = showCols.map(function (item) {
                return item.field;
            });
            var showTitles = showCols.map(function (item) {
                return item.title;
            });
            var sheets = ns.currCheckData.filter(function (check) {
                return check.list.length > 0;
            }).map(function (check) {
                //没有异常不用导出
                if (check.list.length == 0) return;
                var excelData = check.list.map(function(item){
                    return showFields.map(function(h){
                        return item[h];
                    });
                });
                return {
                    th: showTitles,
                    jsonData: excelData,
                    defaultTitle: check.name,
                    types:[]
                };
            });
            exportUtil.exportSheets(sheets,ns.currExcel.fileName+'检查清单');
        });
    }
//----------- excel清单交互方案 end ---------------------
})(check.grid);

$(function () {
    check.grid.init();
});