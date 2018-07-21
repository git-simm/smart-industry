<#include "../base/_base.ftl"/>
<@layout;section>
    <#if section="title">动车模型
    <#elseif section="css">
        <@cssRef url="/static/css/train.css"/>
    <#elseif section="content">
    <div id="container"></div>
    <#elseif section="scripts">
        <@jsRef "/static/libs/three.js"/>
        <@jsRef "/static/libs/ThreeBSP.js"/>
        <@jsRef "/static/libs/loaders/MTLLoader.js"/>
        <@jsRef "/static/libs/loaders/TDSLoader.js"/>
        <@jsRef "/static/libs/loaders/OBJLoader.js"/>
        <@jsRef "/static/libs/BokehShader2.js"/>
        <@jsRef "/static/libs/CinematicCamera.js"/>
        <@jsRef "/static/libs/dat.gui.min.js"/>
        <@jsRef "/static/libs/LoadFont.js"/>
        <@jsRef "/static/libs/hilbert3D.js"/>
        <@jsRef "/static/libs/stats.min.js"/>
        <@jsRef "/static/libs/three.simm.js"/>
        <@jsRef "/static/libs/controls/OrbitControls.js"/>
        <@jsRef "/static/js/train.selection.js"/>
        <@jsRef "/static/js/train.textures.js"/>
        <@jsRef "/static/js/train.js"/>
        <@jsRef "/static/js/train.lines.js"/>
    </#if>
</@layout>