<#include "../base/_base.ftl"/>
<@layout;section>
    <#if section="title">运行测试
    <#elseif section="css">
        <@cssRef "/static/_resources/layout/layout-default-latest.css"/>
        <@cssRef "/static/_resources/ztree/zTreeStyle/zTreeStyle.css"/>
        <@cssRef "/static/css/eagleEye.css"/>
      <style type="text/css">
          .svg_position {
              position: absolute;
              top: 40px !important;
              left: 0px;
              bottom: 0px;
              right: 0px;
              margin: 0px !important;
          }

          .svg_position {
              height: calc(100vh - 80px);
          }

          .topContainer {
              position: absolute;
              top: 0;
              left: 0;
              height: 40px;
              right: 0;
              background: transparent;
              padding: 4px;
              z-index: 10;
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

          tr[state='-1'] {
              background: #aaaaaa;
              color: white;
          }

          tr[state='1'] {
              background: red;
              color: white;
          }

          .img-menu, .img-menu li {
              list-style: none; /* 将默认的列表符号去掉 */
              padding: 0; /* 将默认的内边距去掉 */
              margin: 0; /* 将默认的外边距去掉 */
          }

          .img-menu {
              overflow-x: auto;
              overflow-y: hidden;
              height: 100%;
              width: 100%;
              display: block;
              white-space: nowrap;
          }

          .img-menu li {
              width: 100px;
              height: 80px;
              margin: 5px;
              border: 2px solid grey;
              opacity: 0.5;
              position: relative;
              display: -webkit-inline-box;
          }

          .img-menu .selected, .img-menu li:hover {
              border: 2px solid darkblue;
              opacity: 1;
          }

          .view-card {
              background: antiquewhite;
              width: 100%;
              height: 100%;
          }

          .view-text {
              position: absolute;
              z-index: 10;
              bottom: 0;
              left: 0;
              right: 0;
              text-align: center;
              color: darkblue;
              font-size: 15px;
              width: 100%;
          }
          .fixed-table-container{
              padding-bottom: 0 !important;
          }
          .fixed-table-body{
              background: white;
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
    <div class="ui-layout-center" style="background:beige;padding: 0 !important;">
        <div id="topContainer_template" class="topContainer" style="display: none;">
            <button class="btn btn-primary btn-sm width80 marginLeft10 radius-3 btn_run" onclick="svg.node.run()">
                <i class="icon icon-rocket" style="margin-right: 10px;"></i><span>运行</span>
            </button>
            <button class="btn btn-primary btn-sm width80 marginLeft10 radius-3 btn_close" onclick="svg.node.close()">
                <i class="icon icon-eye-close" style="margin-right: 10px;"></i><span>关闭</span>
            </button>
        </div>
    <#--主窗口-->
        <div id="mainViewContainer" style="position: relative;margin: 0px;">
            <!--<embed id="bg_svg" src="${request.contextPath}/static/svg/new.svg" class="col-md-12 svg_position" type="image/svg+xml"/>-->
        <#--<embed id="line_svg" src="${request.contextPath}/static/svg/new.svg"-->
        <#--class="svg-node col-md-12 svg_position" type="image/svg+xml"/>-->
        </div>
    <#--鹰眼窗口-->
        <div id="thumbViewContainer">
            <div id="scopeContainer-template" style="display: none">
                <svg class="thumb_rect thumbViewClass" style="background: rgba(255,255,255,0.7);">
                    <g>
                        <rect id="scope" fill="red" fill-opacity="0.1" stroke="red" stroke-width="2px" x="0" y="0"
                              width="0"
                              height="0"/>
                        <line id="line1" stroke="red" stroke-width="2px" x1="0" y1="0" x2="0" y2="0"/>
                        <line id="line2" stroke="red" stroke-width="2px" x1="0" y1="0" x2="0" y2="0"/>
                    </g>
                </svg>
                <embed type="image/svg+xml" src="" class="thumb_svg thumbViewClass"/>
            </div>
        </div>
    <#--excel信息展示窗口-->
        <div class="col-12 col-md-12 col-sm-12 marginBottom0" id="excelList"
             style="position: absolute;top: 0px;z-index: 100;display: none;">
            <input id="hid_fileId" type="hidden" value="-1"/>
            <div class="mini-model">
                <table class="table table-bordered table-hover table-striped marginBottom0">
                    <caption>
                        <div class="pullLeft marginTop5">
                            <label class="inline-block textRight">清单列表</label>
                        </div>
                        <div class="pullRight marginRight10">
                            <button class="btn btn-primary btn-sm" onclick="solution.tree.export()">
                                <i class="glyphicon glyphicon-export"></i>&nbsp;导出比对清单
                            </button>
                        </div>
                    </caption>
                </table>
                <table id="list" class="table table-bordered table-hover col-12" style="background: white;"></table>
            </div>
        </div>
    </div>
    <div class="ui-layout-south" style="background: black;">
        <div style="overflow:hidden;">
            <ul class="img-menu">
            </ul>
        </div>
    </div>
    <#--<div class="ui-layout-south">-->
    <#--<div style="position: absolute;top: 0 ;left:0; bottom: 0;width:50px; background: grey;padding: 4px;">-->
    <#--<button class="btn btn-primary btn-sm width-100" id="btn_run" onclick="svgAnimal();">-->
    <#--<span class="icon icon-rocket"/>-->
    <#--</button>-->
    <#--<button class="btn btn-primary btn-sm marginTop10 width-100">-->
    <#--<span class="glyphicon glyphicon-export"></span>-->
    <#--</button>-->
    <#--</div>-->
    <#--<div style="position: absolute;top: 0 ;left:50px; bottom: 0;right: 30%; background: black;">-->
    <#--<p class="light-green">继电器规则校验：校验时长10s，元器件yuan2正常...</p>-->
    <#--<p class="light-green">继电器规则校验：校验时长10s，元器件yuan3正常...</p>-->
    <#--<p class="light-green">继电器规则校验：校验时长10s，元器件yuan4正常...</p>-->
    <#--<p class="light-green">继电器规则校验：校验时长10s，元器件yuan5正常...</p>-->
    <#--<p class="red">继电器规则校验：校验时长10s，元器件yuan6 布线错误...</p>-->
    <#--</div>-->
    <#--<iframe id="train" style="position: absolute;top: 0 ;right:0; bottom: 0;width: 30%;height: 100%;"-->
    <#--src="${request.contextPath}/moni/train"></iframe>-->
    <#--</div>-->
    <#elseif section="scripts">
        <@jsRef "/static/_resources/excel/xlsx.full.min.js"/>
        <@jsRef "/static/_resources/layout/jquery-ui-latest.js"/>
        <@jsRef "/static/_resources/layout/jquery.layout-latest.js"/>
        <@jsRef "/static/_resources/snap/snap.svg-min.js"/>
        <@jsRef "/static/js/common/svg-pan-zoom.js"/>
        <@jsRef "/static/js/common/thumbnailViewer.js"/>
        <@jsRef "/static/_resources/ztree/jquery.ztree.all.js"/>
        <@jsRef "/static/_resources/excel/FileSaver.js"/>
        <@jsRef "/static/_resources/excel/Blob.js"/>
        <@jsRef "/static/_resources/excel/xlsx.core.min.js"/>
        <@jsRef "/static/_resources/excel/Export2Excel.js"/>
        <@jsRef "/static/js/common/exportUtil.js"/>
        <@jsRef "/static/js/manager/svg.node.js"/>
        <@jsRef "/static/js/manager/svg.node.resolve.js"/>
        <@jsRef "/static/js/manager/solution.tree.js"/>
        <@jsRef "/static/js/manager/test.run.js"/>
<script type="application/javascript">
    //--------------------
    //----------------------
    var map;
    $(function () {
        var myLayout = $("body").layout(
                {
                    west__size: 200,
                    south__Size: 140,
                    west__initClosed: false,
                    //west__enableCursorHotkey: false,
                    //south__enableCursorHotkey: false,
                    //west__spacing_open: 0,
                    //west__spacing_closed: 0,
                    stateManagement__enabled: true,
                    spacing_open: 10, // ALL panes
                    spacing_closed: 20, // ALL panes
                    south__spacing_open: 10,
                    south__spacing_closed: 30,
                    togglerContent_open: "<div><</div>", //pane打开时，边框按钮中需要显示的内容可以是符号"<"等。需要加入默认css样式.ui-layout-toggler .content
                    togglerContent_closed: "<div>>></div>", //pane关闭时，同上。
                    south__togglerContent_open: "<div>︾</div>", //pane打开时，边框按钮中需要显示的内容可以是符号"<"等。需要加入默认css样式.ui-layout-toggler .content
                    south__togglerContent_closed: "<div>︽</div>", //pane关闭时，同上。
                    south__minSize: 140,
                    south__maxSize: 140,
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