Zq.Utility.RegisterNameSpace("exportUtil");
(function(ns){
    var export2Excel = new Export2Excel();
    ns.export = function (tHeader,data,title) {
        export2Excel.export_json_to_excel(tHeader, data, title,[]);
    };
    /**
     * 导出多sheet表格
     * @param tHeader
     * @param data
     * @param title
     */
    ns.exportSheets = function (sheets,title) {
        export2Excel.export_sheets_to_excel(sheets, title);
    };
})(exportUtil);