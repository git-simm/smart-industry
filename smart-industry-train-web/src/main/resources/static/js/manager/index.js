Zq.Utility.RegisterNameSpace("smart.train.index");
//闭包引入命名空间
(function (ns, undefined) {
    //设置的实体
    ns.entity = null;

    ns.init = function () {
        //窗口缩放
        window.onresize = function () {
            var height = document.body.clientHeight - 75;
            $(".page_iframe").height(height);
        }

        //绑定tabs的右键菜单
        $("#tabs").tabs({
            onContextMenu: function (e, title) {
                e.preventDefault();
                $('#tabsMenu').menu('show', {
                    left: e.pageX,
                    top: e.pageY
                }).data("tabTitle", title);
            }
        });

        //实例化menu的onClick事件
        $("#tabsMenu").menu({
            onClick: function (item) {
                ns.CloseTab(this, item.name);
            }
        });
    };

    //在右边center区域打开菜单，新增tab
    ns.open = function(text, url) {
        if ($("#tabs").tabs('exists', text)) {
            if(text)
            $('#tabs').tabs('select', text);
        } else {
            var height = document.body.clientHeight - 75;
            var path = Zq.Utility.GetPath(url);
            var content = '<iframe class="page_iframe" scrolling="auto" frameborder="0"  src="' + path + '" style="width:100%;height:' + height + 'px;"></iframe>';
            $('#tabs').tabs('add', {
                title: text,
                closable: true,
                content: content
            });
        }
    }
    

    //几个关闭事件的实现
    ns.closeTab = function(menu, type) {
        var curTabTitle = $(menu).data("tabTitle");
        var tabs = $("#tabs");

        if (type === "close") {
            tabs.tabs("close", curTabTitle);
            return;
        }

        var allTabs = tabs.tabs("tabs");
        var closeTabsTitle = [];

        $.each(allTabs, function () {
            var opt = $(this).panel("options");
            if (opt.closable && opt.title != curTabTitle && type === "Other") {
                closeTabsTitle.push(opt.title);
            } else if (opt.closable && type === "All") {
                closeTabsTitle.push(opt.title);
            }
        });

        for (var i = 0; i < closeTabsTitle.length; i++) {
            tabs.tabs("close", closeTabsTitle[i]);
        }
    }
    
})(smart.train.index);

$(function () {
    smart.train.index.init();
});