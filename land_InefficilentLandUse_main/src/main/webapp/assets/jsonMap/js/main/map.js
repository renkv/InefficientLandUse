/*配置气泡窗口模板匹配字段信息*/
function MapConfig() { };
MapConfig.fields = {
    //阀门配置信息
    "famen": {
        //简单信息模板
        simple: [
                    { field: "NAME", alias: "阀门名称" },
                    { field: "PHONE", alias: "联系电话" }
        ],
        //详情信息模板
        detail: [
                    { field: "NAME", alias: "阀门名称" },
                    { field: "PHONE", alias: "联系电话" },
                    { field: "PERSON", alias: "负责人" },
                    { field: "ROUTETIME", alias: "上次巡检时间" },
                    { field: "INSDATE", alias: "安装日期" }
                ]
    },
    //监测站配置信息
    "jiancezhan": {
        //简单信息模板
        simple: [
                    { field: "NAME", alias: "监测站名称" },
                    { field: "ADDRESS", alias: "监测站地址" }
        ],
        //详情信息模板
        detail: [
                    { field: "NAME", alias: "监测站名称" },
                    { field: "PHONE", alias: "联系电话" },
                    { field: "ADDRESS", alias: "监测站地址" },
                    { field: "PERSON", alias: "负责人" },
                    { field: "TIME", alias: "监测时间" }
        ]
    }
}

dojo.addOnLoad(function () {
    load2DMap();//初始化地图加载部分
});
(function () {
    dojo.require("esri.layers.FeatureLayer");
    dojo.require("esri.tasks.FeatureSet");
    dojo.require("esri.renderers.SimpleRenderer");
})();
/**
 * 初始化地图加载
*/
function load2DMap() {
    //创建地图map对象
    map = new esri.Map("map", {
        logo: false
    });
    debugger;
    graphicsSHPlayer = new esri.layers.GraphicsLayer();//创建绘制SHP图层
    map.addLayer(graphicsSHPlayer);//地图添加图层
    //设置地图初始范围
    var initExtent = new esri.geometry.Extent({ xmin: 113.23782432948647, ymin: 37.3346, xmax: 115.8843752444488, ymax: 38.9647322423371, spatialReference: map.spatialReference });
    map.setExtent(initExtent);
   /* //设置地图初始范围
    var initExtent = new esri.geometry.Extent({ xmin: 110.97802432948647, ymin: 27.531235202509322, xmax: 113.8843752444488, ymax: 28.9647322423371, spatialReference: map.spatialReference });
    map.setExtent(initExtent);*/
    //叠加压缩SHP图层
    loadSHPLayer();

    /*JsonFlayer = null;//创建绘制Json阀门图层
    //叠加Json数据源图层
    loadJsonFlayer();
    //阀门勾选点击事件监听
    $("#famen input").bind("click", function () {
        if (this.checked) {
            if (JsonFlayer) {
                JsonFlayer.show();
            } else {
                //叠加Json数据源图层
                loadJsonFlayer();
            }
        }
        else {
            if (JsonFlayer)
                JsonFlayer.hide();
            map.infoWindow.hide();
        }
    })*/

    /*JsonJlayer = null;//创建绘制Json监测站图层
    //叠加Json数据源图层
    loadJsonJlayer();
    //阀门勾选点击事件监听
    $("#jiancezhan input").bind("click", function () {
        if (this.checked) {
            if (JsonJlayer) {
                JsonJlayer.show();
            } else {
                //叠加Json数据源图层
                loadJsonJlayer();
            }
        }
        else {
            if (JsonJlayer)
                JsonJlayer.hide();
            map.infoWindow.hide();
        }
    })*/

}
/**
 * 加载阀门Json
*/
function loadJsonFlayer() {
    $.ajax({
        type: "GET",
        url: "/assets/jsonMap/js/main/json/famen.json",
        dataType: "json",
        async: false,
        success: function (data) {
            var featureSet = new esri.tasks.FeatureSet(data);//模拟数据源json
            var layerDefinition = {
                "geometryType": "esriGeometryPoint",
                "fields": [
                  {
                      "name": "FID",
                      "type": "esriFieldTypeOID",
                      "alias": "FID"
                  },
                  {
                      "name": "NAME",
                      "type": "esriFieldTypeString",
                      "alias": "NAME",
                      "length": 100
                  },
                  {
                      "name": "PHONE",
                      "type": "esriFieldTypeString",
                      "alias": "PHONE",
                      "length": 50
                  },
                  {
                      "name": "PERSON",
                      "type": "esriFieldTypeString",
                      "alias": "PERSON",
                      "length": 50
                  },
                  {
                      "name": "ROUTETIME",
                      "type": "esriFieldTypeString",
                      "alias": "ROUTETIME",
                      "length": 50
                  },
                  {
                      "name": "INSDATE",
                      "type": "esriFieldTypeString",
                      "alias": "INSDATE",
                      "length": 50
                  }
                ]
            }
            var featureCollection = {
                layerDefinition: layerDefinition,
                featureSet: featureSet
            };
            JsonFlayer = new esri.layers.FeatureLayer(featureCollection);//创建要素图层FeatureLayer
            map.addLayer(JsonFlayer);
            //定义要素图层样式符号
            var symbol =  new esri.symbol.PictureMarkerSymbol("/assets/jsonMap/images/plot/point1.png", 22, 27);
            var renderer = new esri.renderer.SimpleRenderer(symbol);
            JsonFlayer.setRenderer(renderer);
            //监听JsonFlayer图层的点击事件
            JsonFlayer.on("click", function (evt) {
                isShow = true;
                var graphic = evt.graphic;
                var content = getQueryWinContent(graphic.attributes, "famen", "detail");//自定义阀门气泡窗口内容
                var zoompoint = null;
                zoompoint = graphic.geometry;
                map.infoWindow.resize(250, 650);
                map.infoWindow.setTitle("");
                map.infoWindow.setContent(content);
                map.infoWindow.show(zoompoint);
				//map.infoWindow.hide();
				//var graphic = evt.graphic;
				//alert("名称:" + graphic.attributes.NAME)
            });
            //监听JsonFlayer图层的鼠标移进事件
            JsonFlayer.on("mouse-over", function (evt) {
                isShow = false;
                var graphic = evt.graphic;
                var content = getQueryWinContent(graphic.attributes, "famen", "simple");//自定义阀门气泡窗口内容
                var zoompoint = null;
                zoompoint = graphic.geometry;
                map.infoWindow.resize(250, 650);
                map.infoWindow.setTitle("");
                map.infoWindow.setContent(content);
                map.infoWindow.show(zoompoint);
            });
            //监听JsonFlayer图层的鼠标移开事件
            JsonFlayer.on("mouse-out", function (evt) {
                if (!isShow)
                     map.infoWindow.hide();
            });

        }
    });
}
/**
 * 加载监测站Json
*/
function loadJsonJlayer() {
    $.ajax({
        type: "GET",
        url: "/assets/jsonMap/js/main/json/jiancezhan.json",
        dataType: "json",
        async: false,
        success: function (data) {
            var featureSet = new esri.tasks.FeatureSet(data);//模拟数据源json
            var layerDefinition = {
                "geometryType": "esriGeometryPoint",
                "fields": [
                    {
                        "name": "FID",
                        "type": "esriFieldTypeOID",
                        "alias": "FID"
                    },
                    {
                        "name": "NAME",
                        "type": "esriFieldTypeString",
                        "alias": "NAME",
                        "length": 100
                    },
                    {
                        "name": "ADDRESS",
                        "type": "esriFieldTypeString",
                        "alias": "ADDRESS",
                        "length": 50
                    },
                    {
                        "name": "PERSON",
                        "type": "esriFieldTypeString",
                        "alias": "PERSON",
                        "length": 50
                    },
                    {
                        "name": "PHONE",
                        "type": "esriFieldTypeString",
                        "alias": "PHONE",
                        "length": 50
                    },
                    {
                        "name": "TIME",
                        "type": "esriFieldTypeString",
                        "alias": "TIME",
                        "length": 50
                    }
                ]
            }
            var featureCollection = {
                layerDefinition: layerDefinition,
                featureSet: featureSet
            };
            JsonJlayer = new esri.layers.FeatureLayer(featureCollection);//创建要素图层FeatureLayer
            map.addLayer(JsonJlayer);
            //定义要素图层样式符号
            var symbol = new esri.symbol.PictureMarkerSymbol("/assets/jsonMap/images/plot/point2.png", 22, 27);
            var renderer = new esri.renderer.SimpleRenderer(symbol);
            JsonJlayer.setRenderer(renderer);
            //监听JsonJlayer图层的点击事件
            JsonJlayer.on("click", function (evt) {
                isShow = true;
                var graphic = evt.graphic;
                var content = getQueryWinContent(graphic.attributes, "jiancezhan", "detail");//自定义监测站气泡窗口内容
                var zoompoint = null;
                zoompoint = graphic.geometry;
                map.infoWindow.resize(250, 650);
                map.infoWindow.setTitle("");
                map.infoWindow.setContent(content);
                map.infoWindow.show(zoompoint);
				//map.infoWindow.hide();
                //var graphic = evt.graphic;
				//alert("名称:" + graphic.attributes.NAME)
            });
            //监听JsonJlayer图层的鼠标移进事件
            JsonJlayer.on("mouse-over", function (evt) {
                isShow = false;
                var graphic = evt.graphic;
                var content = getQueryWinContent(graphic.attributes, "jiancezhan", "simple");//自定义监测站气泡窗口内容
                var zoompoint = null;
                zoompoint = graphic.geometry;
                map.infoWindow.resize(250, 650);
                map.infoWindow.setTitle("");
                map.infoWindow.setContent(content);
                map.infoWindow.show(zoompoint);
            });
            //监听JsonJlayer图层的鼠标移开事件
            JsonJlayer.on("mouse-out", function (evt) {
                if (!isShow)
                    map.infoWindow.hide();
            });

        }
    });
}
/**
 * 气泡窗口信息详情模板
*/
function getQueryWinContent (json, pointType,infoType) {
    var html = "";
    if (MapConfig.fields[pointType])
        var fields = MapConfig.fields[pointType][infoType];//根据参数来匹配选择的是哪个图层配置的信息模板
    else {
        return html;
    }
    html = "<div style='width:230px;border: 0px solid #ABADCE;'>" +
    "<div style='margin: 0;'>" +
    "<table>";
    if (fields.length > 0) {
        for (var i = 0; i < fields.length; i++) {
            html += "<tr>" +
                    "<td><label>" + fields[i].alias + ":</label></td>" +
                    "<td><label>" + json[fields[i].field] + "</label></td>" +
                    "</tr>";
        }
    }
    html += "</table>" +
   "</div>";
    html += "</div>";
    return html;
}
//叠加压缩SHP图层，批量加载显示
function loadSHPLayer() {
    shp("/assets/jsonMap/js/main/shp/XZQ.zip").then(function (data) {
        debugger;
        if (data && data.length > 0) {
            graphicsSHPlayer.clear();
            for (var i = 0; i < data.length; i++) {
                var features = data[i].features;
                for (var j = 0; j < features.length; j++) {
                    feature = features[j];
                    var graphic = getGraphic(feature);
                    graphicsSHPlayer.add(graphic);
                }
            }
        }
    });
    //根据feature几何类型动态构造对应的要素
    function getGraphic(feature) {
        var graphic = null;
        var symbol = null;
        debugger;
        switch (feature.geometry.type) {
            case "Point"://构造点要素
                symbol = new esri.symbol.PictureMarkerSymbol(getRootPath() + "/assets/jsonMap/images/plot/point1.png", 22, 27);
                var mappoint = new esri.geometry.Point(feature.geometry.coordinates[0], feature.geometry.coordinates[1], new esri.SpatialReference(map.spatialReference.wkid));
                graphic = new esri.Graphic(mappoint, symbol);
                break;
            case "LineString"://构造线要素
                symbol = new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID, new dojo.Color([255, 0, 0]), 6);
                var polyline = new esri.geometry.Polyline(feature.geometry.coordinates);
                graphic = new esri.Graphic(polyline, symbol);
                break;
            case "Polygon"://构造面要素
                //测试加载的shp是面要素，为了区别县界和水库不同样式颜色，定制不同颜色面样式
                if (feature.properties && feature.properties.ADMATYPE) {//县界图层
                    symbol = new esri.symbol.SimpleFillSymbol(esri.symbol.SimpleFillSymbol.STYLE_SOLID, new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID, new esri.Color([211, 211, 211]), 2),
                                                     new esri.Color([125, 125, 125, 0]));
                }
                else {//水库图层
                    symbol = new esri.symbol.SimpleFillSymbol(esri.symbol.SimpleFillSymbol.STYLE_SOLID, new esri.symbol.SimpleLineSymbol(esri.symbol.SimpleLineSymbol.STYLE_SOLID, new esri.Color([0, 191, 255]), 1),
                                                    new esri.Color([0, 191, 255, 0.8]));
                }
                var polygon = new esri.geometry.Polygon(feature.geometry.coordinates);
                graphic = new esri.Graphic(polygon, symbol);
                break;
        }

        return graphic;
    }
}
