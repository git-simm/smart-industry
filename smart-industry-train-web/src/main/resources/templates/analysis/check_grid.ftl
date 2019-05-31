<#--excel信息展示窗口-->
<style type="text/css">
    .fixed-table-body {
        height: 92%;
    }
</style>
<div id="excelList" style="position: absolute;background-color: white; top: 0;
        left: 0; right:0; bottom:0; z-index: 100;display: none;padding: 10px;">
    <input id="hid_fileId" type="hidden" value="-1"/>
    <div class="tabbable">
        <ul class="nav nav-tabs" id="myTab">
            <li v-for="(item,index) in checkData" @click="tabClick(item)" :count="item.count">
                <a data-toggle="tab" href="#result" aria-expanded="true">
                    {{item.name}}
                    <span class="badge" :class="item.count >0 ?'badge-danger':'badge-default'">
                        {{ item.count }}
                    </span>
                </a>
            </li>
        </ul>
        <div class="tab-content">
            <div class="tab-pane fade" id="result">
                <table class="table table-bordered table-hover table-striped marginBottom0">
                    <caption>
                        <div class="pullLeft">
                            <label class="inline-block textRight">{{currentTab.name}}-清单列表</label>
                        </div>
                        <div class="pullRight marginRight10">
                            <button class="btn btn-primary btn-sm" onclick="check.grid.export()">
                                <i class="glyphicon glyphicon-export"></i>&nbsp;导出检查清单
                            </button>
                        </div>
                    </caption>
                </table>
                <table id="list" class="table table-bordered table-hover col-12"></table>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    // Vue.filter("count", function(list) {
    //     return list.length;
    // });
    var app = new Vue({
        el: '#excelList',
        data: {
            checkData:{},
            currentTab:{},
        },
        mounted:function(){
            var me = this;
            Smart.Common.retry(10,function () {
                check.grid.onLoadCheckData = me.onLoadCheckData;
            });
        },
        filters: {
            count: function (list) {
                return list.length;
            }
        },
        methods: {
            tabClick:function(item){
                this.currentTab = item;
                console.log(item);
                var show = {total:item.count,rows:item.list};
                $('#list').bootstrapTable('load',show);
            },
            /**
             * 加载excel检查数据
             * @param data
             */
            onLoadCheckData:function (data) {
                data.forEach(function(item){
                    item.count = item.list.length;
                })
                this.checkData = data;
                //自动选中第一个tab
                setTimeout(function(){
                    var list = $('#myTab li[count!=0]');
                    if (list.length>0){
                        list.eq(0).trigger('click');
                        list.eq(0).find('a').trigger('click');
                    }
                },500);
            }
        }
    });
</script>