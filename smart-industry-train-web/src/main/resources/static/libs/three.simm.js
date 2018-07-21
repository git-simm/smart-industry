window.ThreeSimm = window.Zq || {};
(function (ns) {
    /**
     * 循环创建贴图纹理
     * @param width
     * @param height
     * @param widthSegments
     * @param heightSegments
     * @param path
     * @returns {Object3D|*}
     * @constructor
     */
    ns.CyclePlaneMaterial = function (width, height, widthSegments, heightSegments, path, material) {
        var floorMat = new THREE.MeshStandardMaterial({
            roughness: 0.8,
            color: 0xffffff,
            metalness: 0.2,
            bumpScale: 0.0005
        });
        if (material != null) {
            floorMat = material;
        } else {
            var textureLoader = new THREE.TextureLoader();
            textureLoader.load(path, function (map) {
                wrapMap(map, function (m) {
                    floorMat.map = m;
                    floorMat.needsUpdate = true;
                });
            });
            textureLoader.load(path, function (map) {
                wrapMap(map, function (m) {
                    floorMat.bumpMap = m;
                    floorMat.needsUpdate = true;
                });
            });
            textureLoader.load(path, function (map) {
                wrapMap(map, function (m) {
                    floorMat.roughnessMap = m;
                    floorMat.needsUpdate = true;
                });
            });
        }

        function wrapMap(map, callback) {
            map.wrapS = THREE.RepeatWrapping;
            map.wrapT = THREE.RepeatWrapping;
            map.anisotropy = 4;
            map.repeat.set(widthSegments, heightSegments);
            callback(map);
        }

        var floorGeometry = new THREE.PlaneBufferGeometry(width, height);
        var floorMesh = new THREE.Mesh(floorGeometry, floorMat);
        floorMesh.receiveShadow = true;
        floorMesh.rotation.x = -Math.PI / 2.0;
        return floorMesh;
    }
    /**
     * 添加灯光
     * @param param
     * @constructor
     */
    ns.AddLight = function (param) {
        //param{group,lightPosition,targetPosition}
        var defaultParam = {
            color: 0xffffee
        };
        var param = $.extend({}, defaultParam, param);
        var light = new THREE.SpotLight(param.color, 2, 500, Math.PI / 6, 2);
        //球体模型(半径为5)
        var bulbGeometry = new THREE.SphereGeometry(5, 20, 20);
        var bulbMat = new THREE.MeshStandardMaterial({
            emissive: param.color,
            emissiveIntensity: 1,
            color: param.color
        });
        var ball = new THREE.Mesh(bulbGeometry, bulbMat);
        light.add(ball);
        var bulbGeometry = new THREE.SphereGeometry(5, 16, 8);

        bulbMat = new THREE.MeshStandardMaterial({
            emissive: param.color,
            emissiveIntensity: 1,
            color: param.color
        });
        light.add(new THREE.Mesh(bulbGeometry, bulbMat));
        light.position.copy(param.lightPosition);

        light.castShadow = true;
        light.shadowCameraNear = 2;
        light.shadowCameraFar = 200;
        light.shadowCameraFov = 30;
        light.shadowCameraVisible = true;

        light.shadowMapWidth = 1024;
        light.shadowMapHeight = 1024;
        light.shadowDarkness = 0.3;

        param.group.add(light);

        //定义一个照射的目标
        var ballMat = new THREE.MeshStandardMaterial({
            color: 0xffffff,
            roughness: 0.5,
            metalness: 1.0
        });

        var ballGeometry = new THREE.SphereGeometry(0.01, 16, 16);
        var ballMesh = new THREE.Mesh(ballGeometry, train.textures.TrainMaterial);
        ballMesh.position.copy(param.targetPosition);
        ballMesh.rotation.y = Math.PI;
        ballMesh.castShadow = true;
        light.target = ballMesh;
        param.group.add(ballMesh);
    }
})(ThreeSimm);