Zq.Utility.RegisterNameSpace("train.selection");
(function (ns, undefined) {
    var raycaster, renderer, objects, camera, scene;
    var mouse = new THREE.Vector2(), INTERSECTED;
    ns.Selected = null;
    ns.Init = function (param) {
        renderer = param.render;
        objects = param.objs;
        camera = param.camara;
        scene = param.scene;
        raycaster = new THREE.Raycaster();
        OnFocus();
    }
    var effectController = {
        focalLength: 15
    }

    function OnFocus() {
        var matChanger = function () {
            for (var e in effectController) {
                if (e in camera.postprocessing.bokeh_uniforms)
                    camera.postprocessing.bokeh_uniforms[e].value = effectController[e];
            }
            camera.postprocessing.bokeh_uniforms['znear'].value = camera.near;
            camera.postprocessing.bokeh_uniforms['zfar'].value = camera.far;
            camera.setLens(effectController.focalLength);
            //effectController['focalDepth'] = camera.postprocessing.bokeh_uniforms["focalDepth"].value;
        };
        var gui = new dat.GUI();
        gui.add(effectController, "focalLength", 5, 100, 0.01).name('镜头推进').onChange(matChanger);
        matChanger();
    }

    ns.onSelected = function (event) {
        event.preventDefault();
        if (ns.Selected != null) {
            //oldMaterial.visible = true;
            //ns.Selected.material = oldMaterial;
            //renderer.render(scene,camera);
        }
        mouse.x = (event.clientX / renderer.domElement.clientWidth) * 2 - 1;
        mouse.y = -(event.clientY / renderer.domElement.clientHeight) * 2 + 1;
        raycaster.setFromCamera(mouse, camera);
        //true：包括子元素一起查询;false:不查询子元素
        var intersects = raycaster.intersectObjects(objects, true);
        if (intersects.length > 0) {
            var targetDistance = intersects[0].distance;
            //Using Cinematic camera focusAt method
            camera.focusAt(targetDistance);
            var obj = intersects[0].object;
            ns.Selected = obj;
            if (INTERSECTED != obj) {
                if (INTERSECTED) {
                    //实例还原
                    INTERSECTED.material.emissive.setHex(INTERSECTED.currentHex);
                    INTERSECTED = null;
                }
                if (obj.material.emissive && obj.material.emissive.getHex) {
                    INTERSECTED = intersects[0].object;
                    INTERSECTED.currentHex = INTERSECTED.material.emissive.getHex();
                    //色彩滤镜
                    INTERSECTED.material.emissive.setHex(0x995555);
                }
            }
        } else {
            if (INTERSECTED) INTERSECTED.material.emissive.setHex(INTERSECTED.currentHex);
            INTERSECTED = null;
            ns.Selected = null;
        }
        //scene.overrideMaterial = null;
        //renderer.clear();
        renderer.render(scene, camera);
    }

    function save() {
        //window.open( renderer.domElement.toDataURL('image/png'), 'mywindow' );
        //return false;
    }
})(train.selection);

