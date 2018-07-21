<#-- 静态资源版本控制 -->
<#-- <link rel="stylesheet" type="text/css" href="${request.contextPath}${urls.getForLookupPath(url)}" /> -->
<#macro cssRef url>
    <link rel="stylesheet" type="text/css" href="${request.contextPath}${urls.getForLookupPath(url)}"/>
</#macro>
<#macro jsRef url>
    <script type="text/javascript" src="${request.contextPath}${urls.getForLookupPath(url)}"></script>
</#macro>
<#-- 获取请求地址 带context -->
<#macro geturl url>
    ${request.contextPath}${url}
</#macro>