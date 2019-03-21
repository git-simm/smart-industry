Zq.Utility.RegisterNameSpace("exportUtil");
(function(ns){
    var export2Excel = new Export2Excel();
    ns.export = function (tHeader,data,title) {
        export2Excel.export_json_to_excel(tHeader, data, title,[]);
    };
})(exportUtil);