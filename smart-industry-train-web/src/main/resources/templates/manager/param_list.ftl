<#include "../base/_base.ftl"/>
<@layout;section>
<#if section="title"> 字典项列表
<#elseif section="css">
<#elseif section="content">
<div class="mini-model">
    <table class="table table-bordered table-hover table-striped marginBottom0">
        <caption>
            <div class="pullLeft marginTop5">
                <label class="inline-block textRight">字典项列表</label>
            </div>
            <div class="pullRight">
                <div class="input-group width200">
                    <input class="form-control" placeholder="请输入字典项名称"
                           type="text" name="searchkey" value="" autocomplete="off"/>
                    <span class="input-group-addon" onclick="param.list.ReFresh()">
                        <i class="icon-search"></i>
                    </span>
                </div>
            </div>
            <div class="pullRight marginRight10">
                <button class="btn btn-primary btn-sm" onclick="param.list.Add()"><i class="icon icon-plus"></i>&nbsp;新增</button>
            </div>
        </caption>
    </table>
    <table id="list" class="table table-bordered table-hover"></table>
    <table class="table table-bordered table-hover table-striped marginBottom0">
        <caption>
            <div class="pullLeft marginTop5">
                <label class="inline-block textRight group-options">值列表</label>
            </div>
            <div class="pullRight marginRight10">
                <button class="btn btn-sm btn-primary btn-grid" onclick="param.list.AddOption()"><i class="icon icon-plus"></i>&nbsp;新增项</button>
            </div>
        </caption>
    </table>
    <table id="optionlist" class="table table-bordered table-hover"></table>
    <div style="display: none;" id="list_opr_template">
        <button class="btn btn-xs btn-info" title="编辑" onclick="{edit}">
            <i class="icon-edit"></i>
        </button>
        <button class="btn btn-xs btn-danger" title="删除" onclick="{del}">
            <i class="icon-trash"></i>
        </button>
    </div>
</div>
<#elseif section="scripts">
    <@jsRef "/static/js/manager/param.list.js"/>
	 <script type="text/javascript">
	      $(function () {
	      });
	  </script>
</#if>
</@layout>
