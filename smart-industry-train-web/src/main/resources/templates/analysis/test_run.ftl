<#include "../base/_base.ftl"/>
<@layout;section>
    <#if section="title">运行测试
    <#elseif section="css">
        <@cssRef "/static/_resources/layout/layout-default-latest.css"/>
        <@cssRef "/static/_resources/ztree/zTreeStyle/zTreeStyle.css"/>
        <@cssRef "/static/css/eagleEye.css"/>
      <style type="text/css">
          #mainViewContainer, .svg_position {
              position: absolute;
              top: 0;
              left: 0;
              bottom: 0;
              right: 0;
              height: 100%;
              width: 100%;
          }

          .treeContainer {
              position: absolute;
              bottom: 0;
              top: 25px;
              left: 0;
              right: 0;
              border: 1px solid #ccc;
              border-width: 0 1px 0 0;
              background-color: ghostwhite;
              overflow: auto;
              overflow-y: auto;
          }

          .ztreeMenu {
              height: 25px;
              background: whitesmoke;
              position: absolute;
              right: 0;
              left: 0;
              padding-left: 10px;
              padding-top: 3px;
          }
          tr[state='-1']{
            background: #aaaaaa;
            color: white;
          }
          tr[state='1']{
              background: red;
              color: white;
          }
      </style>
    <#elseif section="content">
    <input type="hidden" id="hid_solutionId" value="${solutionId!}"/>
    <div class="ui-layout-west" style="padding: 0 !important">
        <div style="position: relative;height: 100%;">
            <div class="ztreeMenu">解决方案列表</div>
            <div class="treeContainer">
                <ul id="soluTree" class="ztree"></ul>
            </div>
        </div>
    </div>
    <div class="ui-layout-center" style="background:beige;">
    <#--主窗口-->
        <div id="mainViewContainer" style="position: relative">
            <!--<embed id="bg_svg" src="${request.contextPath}/static/svg/new.svg" class="col-md-12 svg_position" type="image/svg+xml"/>-->
            <embed id="line_svg" src="${request.contextPath}/static/svg/new.svg" class="col-md-12 svg_position" type="image/svg+xml"/>
        </div>
    <#--鹰眼窗口-->
        <div id="thumbViewContainer">
            <svg id="scopeContainer" class="thumbViewClass">
                <g>
                    <rect id="scope" fill="red" fill-opacity="0.1" stroke="red" stroke-width="2px" x="0" y="0" width="0"
                          height="0"/>
                    <line id="line1" stroke="red" stroke-width="2px" x1="0" y1="0" x2="0" y2="0"/>
                    <line id="line2" stroke="red" stroke-width="2px" x1="0" y1="0" x2="0" y2="0"/>
                </g>
            </svg>
            <embed id="thumbView" type="image/svg+xml" src="${request.contextPath}/static/svg/new.svg"
                   class="thumbViewClass"/>
        </div>
    <#--excel信息展示窗口-->
        <div class="col-12 col-md-12 col-sm-12 marginBottom0" id="excelList" style="display: none;">
            <input id="hid_fileId" type="hidden" value="-1"/>
            <div class="mini-model">
                <table class="table table-bordered table-hover table-striped marginBottom0">
                    <caption>
                        <div class="pullLeft marginTop5">
                            <label class="inline-block textRight">清单列表</label>
                        </div>
                        <div class="pullRight marginRight10">
                            <button class="btn btn-primary btn-sm" onclick=""><i class="glyphicon glyphicon-export"></i>&nbsp;导出比对清单</button>
                        </div>
                    </caption>
                </table>
                <table id="list" class="table table-bordered table-hover col-12" style="background: white;"></table>
            </div>
        </div>
    </div>
    <div class="ui-layout-south">
        <div style="position: absolute;top: 0 ;left:0; bottom: 0;width:50px; background: grey;padding: 4px;">
            <button class="btn btn-primary btn-sm width-100" onclick="svgAnimal();">
                <span class="icon icon-rocket"/>
            </button>
            <button class="btn btn-primary btn-sm marginTop10 width-100">
                <span class="glyphicon glyphicon-export"></span>
            </button>
        </div>
        <div style="position: absolute;top: 0 ;left:50px; bottom: 0;right: 30%; background: black;">
            <p class="light-green">继电器规则校验：校验时长10s，元器件yuan2正常...</p>
            <p class="light-green">继电器规则校验：校验时长10s，元器件yuan3正常...</p>
            <p class="light-green">继电器规则校验：校验时长10s，元器件yuan4正常...</p>
            <p class="light-green">继电器规则校验：校验时长10s，元器件yuan5正常...</p>
            <p class="red">继电器规则校验：校验时长10s，元器件yuan6 布线错误...</p>
        </div>
        <iframe id="train" style="position: absolute;top: 0 ;right:0; bottom: 0;width: 30%;height: 100%;"
                src="${request.contextPath}/moni/train"></iframe>
    </div>
    <#elseif section="scripts">
        <@jsRef "/static/_resources/layout/jquery-ui-latest.js"/>
        <@jsRef "/static/_resources/layout/jquery.layout-latest.js"/>
        <@jsRef "/static/_resources/snap/snap.svg-min.js"/>
        <@jsRef "/static/js/common/svg-pan-zoom.js"/>
        <@jsRef "/static/js/common/thumbnailViewer.js"/>
        <@jsRef "/static/_resources/ztree/jquery.ztree.all.js"/>
        <@jsRef "/static/js/manager/svg.resolve.js"/>
        <@jsRef "/static/js/manager/solution.tree.js"/>
        <@jsRef "/static/js/manager/test.run.js"/>
<script type="application/javascript">
    //--------------------
    var i = 0;

    function svgAnimal() {
        var svgDoc = document.getElementById("line_svg").getSVGDocument();
        /* map = Snap(svgDoc.getElementsByTagName("svg")[0]);
        var paths = map.selectAll("path");
        $.each(paths,function(i,obj){
            var len = obj.getTotalLength();
            obj.attr({
                stroke: '#31ff42',
                strokeWidth: 10,
                "stroke-dasharray": len + " " + len,
                "stroke-dashoffset": len
            }).animate({"stroke-dashoffset": 10}, 2500,mina.easeinout);
        }); */
        //开灯，关灯
        i++;
        if (i % 2) {
            changeFill(svgDoc, "#ff0000");
            document.getElementById("train").contentWindow.train.lines.openLight();
        } else {
            changeFill(svgDoc, "gray");
            document.getElementById("train").contentWindow.train.lines.closeLight();
        }
    }

    /**
     * 改变svg的填充颜色
     * @param svg
     * @param color
     */
    var waitCount = 0;

    function changeFill(svg, color) {
        if (svg.id == "CD_A3L_PRSC_SHH") return;
        //对use元素 进行统一处理
        if (svg.tagName == "use") {
            //console.log($(svg).attr("xlink:href"));
            if($(svg).attr("xlink:href")=="#CD_A3L_PRSC_SHH"){
                return;
            }
            $(svg).attr("color","#31ff42");
            return;
        }
        if (svg.children && svg.children.length > 0) {
            $.each(svg.children, function (i, svgItem) {
                changeFill(svgItem, color);
            });
        }
        waitCount++;
        if (svg.tagName == "path" || svg.tagName == "line" || svg.tagName == "circle") {
            setTimeout(function () {
                var len = svg.getTotalLength();
                $(svg).css({
                    stroke: '#31ff42',
                    strokeWidth: 0.2,
                    "stroke-dasharray": len + " " + len,
                    "stroke-dashoffset": len
                }).animate({"stroke-dashoffset": 0}, 50, mina.easeinout);
            }, 5 * waitCount);
        }
    }

    function sleep(numberMillis) {
        var now = new Date();
        var exitTime = now.getTime() + numberMillis;
        while (true) {
            now = new Date();
            if (now.getTime() > exitTime)
                return;
        }
    }

    //----------------------
    var map;
    $(function () {
        var myLayout = $("body").layout(
                {
                    west__size: 300,
                    south__Size: 300,
                    west__initClosed: false,
                    //west__enableCursorHotkey: false,
                    //south__enableCursorHotkey: false,
                    //west__spacing_open: 0,
                    //west__spacing_closed: 0,
                    stateManagement__enabled: true,
                    spacing_open: 6, // ALL panes
                    spacing_closed: 30, // ALL panes
                    //south__spacing_open: 6,
                    //south__spacing_closed: 20,
                    togglerContent_open: "<div><</div>", //pane打开时，边框按钮中需要显示的内容可以是符号"<"等。需要加入默认css样式.ui-layout-toggler .content
                    togglerContent_closed: "<div>>></div>", //pane关闭时，同上。
                    south__minSize: 300,
                    south__maxSize: 300,
                    west__minSize: 200,
                    west__maxSize: 300,
                    onresize: function () {
                        //$('#list').bootstrapTable('resetWidth');
                        svgPanZoom.resize();
                    }
                }
        );
    });
</script>
    </#if>
</@layout>