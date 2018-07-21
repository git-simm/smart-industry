<#include "base/_base.ftl"/>
<@layout;section>
    <#if section="title">首页
    <#elseif section="css">
    <#elseif section="content">
<div class="mini-model">
    <h1>Hello,boy!这只是一个测试页面！！</h1>
    <p>${greeting!}</p>
</div>
    <#elseif section="scripts">
	 <script type="text/javascript">
         $(function () {
             alert("页面初始化完成");
         });
     </script>
    </#if>
</@layout>