$.extend({
    namespace : function (path) {
        var parts = path.split("."), root = window, max, i;
        for (i = 0, max = parts.length; i < max; i++) {
            if (typeof root[parts[i]] === "undefined") {
                root[parts[i]] = {};
            }
            root = root[parts[i]];
        }
        return root;
    },

    define : function(className, options, isStatic){
        var classStr = className + (isStatic ? ' = {};' : ' = function(){};');
        eval(classStr);
        $.extend(isStatic ? eval(className) : eval(className).prototype, options);
    },
    /**
     * 定义类时，拆分命名空间与类名 分开处理
     * @param namespace
     * @param className
     * @param options
     * @param isStatic
     */

    class : function(namespace,className, options, isStatic){
        this.define(namespace + "." + className,options,isStatic);
    },
    /**
     * 获取系统上下文路径
     * @returns {*|jQuery}
     */
    getPath: function (path) {
        var contextPath = rootPath || '';
        return contextPath + path;
    }
});
