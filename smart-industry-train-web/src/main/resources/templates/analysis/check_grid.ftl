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
                    <button class="btn btn-primary btn-sm" onclick="checkbox.grid.export()">
                        <i class="glyphicon glyphicon-export"></i>&nbsp;导出比对清单
                    </button>
                </div>
            </caption>
        </table>
        <table id="list" class="table table-bordered table-hover col-12" style="background: white;"></table>
    </div>
</div>
<script type="text/javascript">
    var app = new Vue({
        el: '#excelList',
        data: {
            checkData:{},
        },
        mounted:function(){
            var me = this;
            Smart.Common.retry(10,function () {
                check.grid.onLoadCheckData = me.onLoadCheckData;
            });
        },
        methods: {
            /**
             * 加载excel检查数据
             * @param data
             */
            onLoadCheckData:function (data) {
                this.checkData = data;
                console.log("onLoadCheckData");
                console.log(data);
                var show = {total:2,rows:[
                        {"Wire_Number":1},
                        {"Wire_Number":2},
                    ]};
                $('#list').bootstrapTable('load',show);
            }
        }
    });
</script>