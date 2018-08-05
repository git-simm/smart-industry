Zq.Utility.RegisterNameSpace("train.lines");
(function (ns, undefined) {
    ns.AddLine = function () {
        var subdivisions = 6;
        var recursion = 1;
        var points = hilbert3D(new THREE.Vector3(300, 0, 0), 125.0, recursion, 0, 1, 2, 3, 4, 5, 6, 7);
        var spline = new THREE.CatmullRomCurve3(points);
        var samples = spline.getPoints(points.length * subdivisions);
        var geometrySpline = new THREE.Geometry().setFromPoints(samples);
        var object = new THREE.Line(geometrySpline, new THREE.LineDashedMaterial({
            color: 0xffffff,
            dashSize: 5,
            gapSize: 0.5
        }));
        return object;
    }

    ns.AddTrain2 = function () {
        var sphere_geometry_1 = new THREE.SphereGeometry(50, 64, 8);
        var sphere_bsp_1 = new ThreeBSP(sphere_geometry_1);

        var sphere_geometry_2 = new THREE.SphereGeometry(50, 8, 32);
        var sphere_mesh_2 = new THREE.Mesh(sphere_geometry_2);
        sphere_mesh_2.position.x = 20;
        var sphere_bsp_2 = new ThreeBSP(sphere_mesh_2);

        var intersect_bsp = sphere_bsp_1.subtract(sphere_bsp_2);

        var result = intersect_bsp.toMesh(new THREE.MeshLambertMaterial({
            shading: THREE.SmoothShading,
            map: THREE.ImageUtils.loadTexture('../textures/floor.jpg')
        }));
        result.position.x = 6;
        result.geometry.computeVertexNormals();
        return result;
    }

    ns.AddTrain3 = function () {
        var geometry = new THREE.CylinderGeometry(75, 75, 500, 100);
        // Create a mesh based on tubeGeometry and tubeMaterial
        var mytrain = new THREE.Mesh(geometry);
        //var geometry = new THREE.TorusKnotBufferGeometry( 100, 3, 100, 16 );
        //var mytrain = new THREE.Mesh( geometry, train.textures.TrainMaterial );
        //geometry.name = "列车";
        mytrain.rotateX(-Math.PI / 2);
        mytrain.rotateZ(-Math.PI / 2);
        var bsp1 = new ThreeBSP(mytrain);

        var geometry2 = new THREE.CylinderGeometry(70, 70, 500, 100);
        var mytrain2 = new THREE.Mesh(geometry2);
        mytrain2.rotateX(-Math.PI / 2);
        mytrain2.rotateZ(-Math.PI / 2);
        var bsp12 = new ThreeBSP(mytrain2);

        var body = new THREE.BoxGeometry(500, 100, 200)
        var bottom = body.clone();
        //绘制3d图形
        cube = new THREE.Mesh(body);
        cube.position.y -= 0;
        var bsp2 = new ThreeBSP(cube);

        bCube = new THREE.Mesh(bottom);
        bCube.position.y -= 50;
        var bsp3 = new ThreeBSP(bCube);

        var union_bsp = bsp1.subtract(bsp12).subtract(bsp2).subtract(bsp3);
        var result = union_bsp.toMesh(train.textures.TrainMaterial);
        //group.add(union_bsp);
        result.geometry.computeVertexNormals();
        return result;
    };
    /**
     * 加载3D模型图
     * @constructor
     */
    ns.AddTrain = function (callback) {
        //3ds files dont store normal maps
        var loader = new THREE.TDSLoader();
        loader.setPath(Zq.Utility.GetPath('/const/objs/train/'));
        var material = train.textures.TrainMaterial.clone();
        material.transparent = true;
        material.opacity = 0.8;
        loader.load(Zq.Utility.GetPath('/const/objs/train/AVEENG_L.3DS'), function (object) {
            object.traverse(function (child) {
                if (child instanceof THREE.Mesh) {
                    child.material = material;
                }
            });
            object.scale.set(0.02, 0.02, 0.02);
            object.rotateX(-Math.PI / 2);
            object.rotateZ(-Math.PI / 2);
            object.name = "train-outer";
            object.position.z -= 42;
            object.castShadow = true;
            callback(object);
        });
    }

    ns.Lights = [];
    ns.openLight = function(){
        $.each(ns.Lights,function(i,light){
            light.open();
        });
    }
    ns.closeLight = function(){
        $.each(ns.Lights,function(i,light){
            light.close();
        });
    }

    ns.AddLight = function (group) {
        //车头大灯
        ns.Lights.push(ThreeSimm.AddLight({
            group: group,
            lightPosition: new THREE.Vector3(-250, -25, 0),
            targetPosition: new THREE.Vector3(-10000, -35, 0)
        }));
        //车尾灯1
        ns.Lights.push(ThreeSimm.AddLight({
            group: group,
            lightPosition: new THREE.Vector3(195, -20, -22),
            targetPosition: new THREE.Vector3(10000, -35, -22),
            color: 0xff0000
        }));
        //车尾灯2
        ns.Lights.push(ThreeSimm.AddLight({
            group: group,
            lightPosition: new THREE.Vector3(195, -20, 22),
            targetPosition: new THREE.Vector3(10000, -35, 22),
            color: 0xff0000
        }));
    }

    /**
     * obj格式导入
     * @param callback
     * @constructor
     */
    ns.AddTrain4 = function (callback) {
        var path = Zq.Utility.GetPath("/const/objs/train/");
        var mtlLoader = new THREE.MTLLoader();
        mtlLoader.setPath(path);
        mtlLoader.load('AVEENG_L.mtl', function (materials) {
            materials.preload();
            var objLoader = new THREE.OBJLoader();
            //objLoader.setMaterials( train.textures.TrainMaterial );
            objLoader.setMaterials(materials);
            objLoader.setPath(path);
            objLoader.load('AVEENG_L.obj', function (object) {
                object.scale.set(0.02, 0.02, 0.02);
                object.rotateX(-Math.PI / 2);
                object.rotateZ(-Math.PI / 2);
                object.name = "train-outer";
                callback(object);
            });
        });
    }


    /**
     * 创建火车
     * @returns {*}
     * @constructor
     */
    ns.AddTrain1 = function () {
        var group = new THREE.Object3D();
        var train1 = extrudeTrain();
        train1.rotateY(-Math.PI / 2);
        train1.position.x += 500 / 2;
        group.add(train1);
        group.add(ns.trainHead());
        return group;

        /*train2.scale.set(0.9,0.9,1);
        train2.position.y+=10;
        var bsp11 = new ThreeBSP(train1);
        var bsp12 = new ThreeBSP(train2);

        var union_bsp = bsp11.subtract(bsp12);
        var result = union_bsp.toMesh(train.textures.TrainMaterial);
        result.geometry.computeVertexNormals();*/

        return result;
    };

    ns.trainHead = function () {
        var points = [];
        var width = 200;
        points.push(new THREE.Vector3(50, 0, 0));
        points.push(new THREE.Vector3(-50, 0, 0));
        points.push(new THREE.Vector3(48, 50, 0));
        points.push(new THREE.Vector3(-48, 50, 0));

        points.push(new THREE.Vector3(25, 0, 0));
        points.push(new THREE.Vector3(-25, 0, 0));
        points.push(new THREE.Vector3(24, 20, 0));
        points.push(new THREE.Vector3(-24, 20, 0));

        var group = new THREE.Object3D();
        points.forEach(function (point) {
            var geom = new THREE.SphereGeometry(200);
            var mesh = new THREE.Mesh(geom, train.textures.TrainMaterial);
            mesh.position = point;
            group.add(mesh);
        })
        return group;
    };

    /**
     * 拉出一个车厢
     * @returns {Raycaster.params.Mesh|*}
     */
    function extrudeTrain() {
        var options = {
            amount: 500,
            bevelThickness: 10,
            bevelSize: 1,
            bevelSegments: 30,
            curveSegments: 12,
            steps: 1
        };
        var mytrain2 = new THREE.Mesh(new THREE.ExtrudeGeometry(drawShape(), options), train.textures.TrainMaterial);
        return mytrain2;
    }

    function drawShape() {
        var shape = new THREE.Shape();
        shape.moveTo(-50, 0);//开始点
        //shape.lineTo(-50,0);//直线
        //shape.lineTo(50,0);//直线
        //shape.lineTo(50,100);//直线
        //shape.bezierCurveTo(45, 110, -45, 110, -50, 100);//在(-50, 100)这个点结束
        shape.bezierCurveTo(-45, 10, 45, 10, 50, 0);//在(50, 0)这个点结束
        shape.lineTo(50, -2);//直线
        shape.bezierCurveTo(45, 8, -45, 8, -50, -2);//在(-50, -2)这个点结束
        /*shape.splineThru([
            new THREE.Vector2(10,210),
            new THREE.Vector2(190,210)
        ]);*/
        //为子路径添加贝塞尔曲线
        //shape.quadraticCurveTo(20,15,10,10);
        /*(var hole1 = new THREE.Path();
        hole1.absellipse(16,24,2,3,0,Math.PI*2,true);
        shape.holes.push(hole1);*/
        return shape;
    }
})(train.lines);