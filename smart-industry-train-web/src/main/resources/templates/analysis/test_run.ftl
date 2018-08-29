<#include "../base/_base.ftl"/>
<@layout;section>
    <#if section="title">运行测试
    <#elseif section="css">
      <@cssRef "/static/_resources/layout/layout-default-latest.css"/>
      <style type="text/css">
          .svg_position{
              position: absolute;top:0;left: 0;
          }
      </style>
    <#elseif section="content">
    <div class="ui-layout-center">
        <div style="position: relative">
            <embed id="bg_svg" src="${request.contextPath}/static/svg/TopView.svg" class="col-md-12 svg_position" type="image/svg+xml"/>
            <embed id="line_svg" src="${request.contextPath}/static/svg/TopView.svg" class="col-md-12 svg_position" type="image/svg+xml"/>
        </div>
    </div>
    <div class="ui-layout-west"></div>
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
        <iframe id="train" style="position: absolute;top: 0 ;right:0; bottom: 0;width: 30%;height: 100%;" src="${request.contextPath}/moni/train"></iframe>
    </div>
    <#elseif section="scripts">
        <@jsRef "/static/_resources/layout/jquery-ui-latest.js"/>
        <@jsRef "/static/_resources/layout/jquery.layout-latest.js"/>
        <@jsRef "/static/_resources/snap/snap.svg-min.js"/>
<script type="application/javascript">
    //--------------------
    var i =0 ;
    function svgAnimal(){
        //var svgDoc = document.getElementById("line_svg").getSVGDocument();
        //map = Snap(svgDoc.getElementsByTagName("svg")[0]);
        //var paths = map.selectAll("path");
        $.each(paths,function(i,obj){
            var len = obj.getTotalLength();
            obj.attr({
                stroke: '#31ff42',
                strokeWidth: 10,
                "stroke-dasharray": len + " " + len,
                "stroke-dashoffset": len
            }).animate({"stroke-dashoffset": 10}, 2500,mina.easeinout);
        });
        //开灯，关灯
        i++;
        if( i % 2 ){
            changeFill(svgDoc,"#ff0000");
            document.getElementById("train").contentWindow.train.lines.openLight();
        }else{
            changeFill(svgDoc,"gray");
            document.getElementById("train").contentWindow.train.lines.closeLight();
        }
    }

    /**
     * 改变svg的填充颜色
     * @param svg
     * @param color
     */
    function changeFill(svg,color){
        if(svg.children && svg.children.length>0){
            $.each(svg.children,function(i,svgItem){
                changeFill(svgItem,color);
            });
        }
        if(!(svg.tagName=="svg" || svg.tagName=="defs")){
            if(svg.tagName=="path") {
                var len = svg.getTotalLength();
                svg.attr({
                    stroke: '#31ff42',
                    strokeWidth: 10,
                    "stroke-dasharray": len + " " + len,
                    "stroke-dashoffset": len
                }).animate({"stroke-dashoffset": 10}, 2500,mina.easeinout);
            }
        }
    }
    //----------------------
    var map;
    $(function(){
        var myLayout =$("body").layout(
            {
                west__size: 300,
                south__Size: 300,
                west__initClosed: true,
                //west__enableCursorHotkey: false,
                //south__enableCursorHotkey: false,
                //west__spacing_open: 0,
                //west__spacing_closed: 0,
                stateManagement__enabled: true,
                spacing_open: 6, // ALL panes
                spacing_closed: 20, // ALL panes
                south__spacing_open: 6,
                south__spacing_closed: 20,
                togglerContent_open: "<div><</div>", //pane打开时，边框按钮中需要显示的内容可以是符号"<"等。需要加入默认css样式.ui-layout-toggler .content
                togglerContent_closed: "<div>>></div>", //pane关闭时，同上。
                south__minSize: 300,
                south__maxSize: 300,
                west__minSize: 200,
                west__maxSize: 300,
                onresize: function () {
                    //$('#list').bootstrapTable('resetWidth');
                }
            }
        );
    });
</script>
    </#if>
</@layout>