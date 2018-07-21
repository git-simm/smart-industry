Zq.Utility.RegisterNameSpace("train.textures");
(function (ns, undefined) {
    ns.Floor = null;
    //反光材质
    ns.TrainMaterial = new THREE.MeshPhongMaterial({color: "#afafaf"});

    //--------- 材质初始化 begin ---------------
    function Init() {
        SetFloorMaterial();
    }

    function SetFloorMaterial() {
        var texture = THREE.ImageUtils.loadTexture(Zq.Utility.GetPath("/static/const/textures/grid.png"));//加载纹理贴图
        var material = new THREE.MeshBasicMaterial({
            color: 0Xffffff, transparent: true,
            opacity: 0.5, map: texture
        });
        //地板材质初始化
        ns.Floor = material;
    }

    Init();
})(train.textures);