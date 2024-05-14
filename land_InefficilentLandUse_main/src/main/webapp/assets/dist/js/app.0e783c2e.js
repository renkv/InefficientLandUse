(function(e){function t(t){for(var o,r,l=t[0],a=t[1],c=t[2],u=0,p=[];u<l.length;u++)r=l[u],Object.prototype.hasOwnProperty.call(n,r)&&n[r]&&p.push(n[r][0]),n[r]=0;for(o in a)Object.prototype.hasOwnProperty.call(a,o)&&(e[o]=a[o]);h&&h(t);while(p.length)p.shift()();return s.push.apply(s,c||[]),i()}function i(){for(var e,t=0;t<s.length;t++){for(var i=s[t],o=!0,l=1;l<i.length;l++){var a=i[l];0!==n[a]&&(o=!1)}o&&(s.splice(t--,1),e=r(r.s=i[0]))}return e}var o={},n={app:0},s=[];function r(t){if(o[t])return o[t].exports;var i=o[t]={i:t,l:!1,exports:{}};return e[t].call(i.exports,i,i.exports,r),i.l=!0,i.exports}r.m=e,r.c=o,r.d=function(e,t,i){r.o(e,t)||Object.defineProperty(e,t,{enumerable:!0,get:i})},r.r=function(e){"undefined"!==typeof Symbol&&Symbol.toStringTag&&Object.defineProperty(e,Symbol.toStringTag,{value:"Module"}),Object.defineProperty(e,"__esModule",{value:!0})},r.t=function(e,t){if(1&t&&(e=r(e)),8&t)return e;if(4&t&&"object"===typeof e&&e&&e.__esModule)return e;var i=Object.create(null);if(r.r(i),Object.defineProperty(i,"default",{enumerable:!0,value:e}),2&t&&"string"!=typeof e)for(var o in e)r.d(i,o,function(t){return e[t]}.bind(null,o));return i},r.n=function(e){var t=e&&e.__esModule?function(){return e["default"]}:function(){return e};return r.d(t,"a",t),t},r.o=function(e,t){return Object.prototype.hasOwnProperty.call(e,t)},r.p="";var l=window["webpackJsonp"]=window["webpackJsonp"]||[],a=l.push.bind(l);l.push=t,l=l.slice();for(var c=0;c<l.length;c++)t(l[c]);var h=a;s.push([0,"chunk-vendors"]),i()})({0:function(e,t,i){e.exports=i("56d7")},"0c5f":function(e,t,i){},"56d7":function(e,t,i){"use strict";i.r(t);var o=i("a2e7"),n=function(){var e=this,t=e._self._c;return t("div",{attrs:{id:"app"}},[t("CesiumGeoJson")],1)},s=[],r=function(){var e=this,t=e._self._c;return t("el-container",{directives:[{name:"loading",rawName:"v-loading",value:e.loading,expression:"loading"}],staticStyle:{width:"100%",height:"100%"},attrs:{"element-loading-text":"","element-loading-background":"rgba(0, 0, 0, 0.75)"}},[t("el-header",{staticStyle:{"background-color":"#545c64",display:"flex","align-items":"center","justify-content":"space-between"}},[t("el-autocomplete",{attrs:{"fetch-suggestions":e.querySearch,placeholder:"搜索","trigger-on-focus":!1},on:{select:e.handleSelect},scopedSlots:e._u([{key:"default",fn:function({item:i}){return[t("div",{staticStyle:{"text-overflow":"ellipsis",overflow:"hidden","font-size":"12px",color:"#666"},attrs:{title:i.value}},[e._v(e._s(i.value))])]}}]),model:{value:e.keyword,callback:function(t){e.keyword=t},expression:"keyword"}},[e.keyword?t("i",{staticClass:"el-icon-close el-input__icon",staticStyle:{cursor:"pointer"},attrs:{slot:"suffix"},on:{click:e.handleIconClear},slot:"suffix"}):t("i",{staticClass:"el-icon-search el-input__icon",attrs:{slot:"suffix"},slot:"suffix"})]),t("div",{staticStyle:{"flex-grow":"1"}}),t("div",{staticStyle:{display:"flex","column-gap":"30px"}},[e._l(e.geo_list,(function(i,o){return t("el-link",{key:o,attrs:{icon:"el-icon-folder",type:e.current===o?"warning":"info"},on:{click:function(t){return e.handleGeoClick(i,o)}}},[e._v(e._s(i.name))])})),t("el-link",{attrs:{icon:"el-icon-setting",type:"info"},on:{click:e.handleHome}},[e._v("复位")]),t("el-link",{attrs:{disabled:!e.selectItem.properties,icon:"el-icon-setting",type:e.visible?"warning":"info"},on:{click:e.handleShow}},[e._v("属性")]),t("el-link",{attrs:{type:e.isDraw?"warning":"info",icon:"el-icon-mouse"},on:{click:e.handleDraw}},[e._v(e._s(e.isDraw?"取消框选":"框选"))]),t("el-link",{attrs:{type:"info",icon:"el-icon-delete"},on:{click:e.handleClear}},[e._v("清除")]),t("el-link",{attrs:{disabled:0===e.points.length,type:"info",icon:"el-icon-download"},on:{click:e.handleExport}},[e._v("导出")])],2)],1),t("el-main",{staticStyle:{padding:"0",position:"relative",overflow:"hidden"}},[t("div",{staticStyle:{width:"100%",height:"100%"},attrs:{id:"cesiumContainer"}}),e.isDraw?t("div",{staticStyle:{position:"absolute",left:"0",top:"0"}},[t("div",{staticStyle:{padding:"0 8px","font-size":"13px",color:"#eee","line-height":"1.7"}},[e._v("点击左键绘制，双击左键结束，点击右键删除")])]):e._e(),t("div",{staticStyle:{position:"absolute",top:"0",right:"0",width:"25%",height:"100%","background-color":"rgba(255, 255, 255, 1)","border-left":"1px solid #ccc",transition:"all .2s linear"},style:{transform:e.visible?"translateX(0)":"translateX(100%)"}},[t("div",{staticStyle:{width:"100%",height:"100%","overflow-y":"auto"}},[t("table",e._l(e.selectItem.properties,(function(i,o){return t("tr",[t("td",[e._v(e._s(i.key))]),t("td",[e._v(e._s(i.value))])])})),0)])])])],1)},l=[],a=(i("65a7"),i("37f2"));const c=window.selectBoxStyle.strokeWeight||3,h=window.selectBoxStyle.strokeColor||"#67C23A",u=window.selectBoxStyle.fillColor||"rgb(230, 162, 60, 0.5)";class p{constructor(e){Object(a["a"])(this,"doubleClickTimeout",null),Object(a["a"])(this,"viewer",null),Object(a["a"])(this,"handler",null),Object(a["a"])(this,"entityCollection",null),Object(a["a"])(this,"drawingMode","polygon"),Object(a["a"])(this,"activeShape",void 0),Object(a["a"])(this,"floatingPoint",void 0),Object(a["a"])(this,"activeShapePoints",[]),Object(a["a"])(this,"callback",null),Object(a["a"])(this,"pointOptions",{color:Cesium.Color.fromCssColorString("#E6A23C").withAlpha(0),pixelSize:8,outlineColor:Cesium.Color.fromCssColorString("#000").withAlpha(0),outlineWidth:2,heightReference:Cesium.HeightReference.CLAMP_TO_GROUND}),Object(a["a"])(this,"polylineOptions",{clampToGround:!0,width:c,material:Cesium.Color.fromCssColorString(h)}),Object(a["a"])(this,"polygonOptions",{material:new Cesium.ColorMaterialProperty(Cesium.Color.fromCssColorString(u))}),this.viewer=e.viewer,this.callback=e.callback,this.entityCollection=new Cesium.CustomDataSource("geojson-draw-layers"),this.viewer.dataSources.add(this.entityCollection)}create(){this.handler||(this.handler=new Cesium.ScreenSpaceEventHandler(this.viewer.canvas),this.handler.setInputAction(this.mouseLeft.bind(this),Cesium.ScreenSpaceEventType.LEFT_CLICK),this.handler.setInputAction(this.mouseMove.bind(this),Cesium.ScreenSpaceEventType.MOUSE_MOVE),this.handler.setInputAction(this.mouseDoubleClcik.bind(this),Cesium.ScreenSpaceEventType.LEFT_DOUBLE_CLICK),this.handler.setInputAction(this.mouseRight.bind(this),Cesium.ScreenSpaceEventType.RIGHT_CLICK),this.viewer.canvas.style.cursor="crosshair")}destroy(){this.handler&&(this.handler.removeInputAction(this.mouseLeft.bind(this),Cesium.ScreenSpaceEventType.LEFT_CLICK),this.handler.removeInputAction(this.mouseMove.bind(this),Cesium.ScreenSpaceEventType.MOUSE_MOVE),this.handler.removeInputAction(this.mouseDoubleClcik.bind(this),Cesium.ScreenSpaceEventType.LEFT_DOUBLE_CLICK),this.handler.removeInputAction(this.mouseRight.bind(this),Cesium.ScreenSpaceEventType.RIGHT_CLICK),this.handler.destroy(),this.handler=null),this.viewer.canvas.style.cursor="default"}mouseLeft(e){const t=this.viewer.camera.getPickRay(e.position),i=this.viewer.scene.globe.pick(t,this.viewer.scene);if(Cesium.defined(i)){if(0===this.activeShapePoints.length){this.floatingPoint=this.createPoint(i),this.activeShapePoints.push(i);const e=new Cesium.CallbackProperty(()=>new Cesium.PolygonHierarchy(this.activeShapePoints),!1),t=new Cesium.CallbackProperty(()=>this.activeShapePoints,!1);this.activeShape=this.drawShape(e,t)}this.activeShapePoints.push(i),this.createPoint(i)}}mouseMove(e){if(Cesium.defined(this.floatingPoint)){const t=this.viewer.camera.getPickRay(e.endPosition),i=this.viewer.scene.globe.pick(t,this.viewer.scene);Cesium.defined(i)&&(this.floatingPoint.position.setValue(i),this.activeShapePoints.pop(),this.activeShapePoints.push(i))}}mouseRight(e){this.activeShapePoints.length>2?this.activeShapePoints.splice(-2,1):this.activeShapePoints=[]}mouseDoubleClcik(e){if(this.activeShapePoints.splice(-3),this.activeShapePoints.length<3)this.activeShapePoints=[];else{const e=this.activeShapePoints;e.push(e[0]),"function"===typeof this.callback&&this.callback(this.activeShapePoints),this.terminateShape()}}createPoint(e){const t=this.entityCollection.entities.add({position:e,point:this.pointOptions});return t}drawShape(e,t){let i=this.entityCollection.entities.add({polyline:{positions:t,...this.polylineOptions},polygon:{hierarchy:e,...this.polygonOptions}});return i}terminateShape(){this.drawShape(this.activeShapePoints,this.activeShapePoints),this.entityCollection.entities.remove(this.floatingPoint),this.entityCollection.entities.remove(this.activeShape),this.floatingPoint=void 0,this.activeShape=void 0,this.activeShapePoints=[]}clear(){this.entityCollection.entities.removeAll()}}var d=p;function m(e){const t=Cesium.Ellipsoid.WGS84.cartesianToCartographic(e),i=Cesium.Math.toDegrees(t.longitude),o=Cesium.Math.toDegrees(t.latitude),n=t.height;return{longitude:i,latitude:o,height:n}}var v=null,y=null,C=null,f=new Cesium.Color(1,1,0,.39),g=new Cesium.Color(1,1,0,1),S=new Cesium.Color(1,0,0,.39),w=new Cesium.Color(1,0,0,1);function b(e){var t=document.createElement("a");t.href=e||window.location.href;var i=t.search.substring(1),o={};return i.split("&").forEach((function(e){var t=e.split("=");o[decodeURIComponent(t[0])]=decodeURIComponent(t[1]||"")})),o}var k={name:"CesiumGeoJson",data(){return{points:[],isDraw:!1,current:0,geo_list:geo_list,loading:!1,visible:!1,keyword:null,restaurants:[],selectItem:{}}},watch:{isDraw(e){e?(this.removeMapClick(),C.create()):(C.destroy(),this.addMapClick())}},mounted(){v=new Cesium.Viewer("cesiumContainer",{imageryProvider:new Cesium.SingleTileImageryProvider({url:"data:image/gif;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVQImWNgYGBgAAAABQABh6FO1AAAAABJRU5ErkJggg=="}),baseLayerPicker:!1,animation:!1,fullscreenButton:!1,vrButton:!1,geocoder:!1,homeButton:!1,infoBox:!1,sceneModePicker:!1,selectionIndicator:!1,timeline:!1,navigationHelpButton:!1,navigationInstructionsInitiallyVisible:!1,scene3DOnly:!0,shouldAnimate:!1,skyBox:!1}),window.viewer=v,v.cesiumWidget.creditContainer.style.display="none",this.initDraw(),v.imageryLayers.addImageryProvider(new Cesium.WebMapTileServiceImageryProvider({url:"http://t0.tianditu.gov.cn/img_w/wmts?tk="+td_key,layer:"img",style:"default",tileMatrixSetID:"w",format:"tiles",maximumLevel:18})),v.imageryLayers.addImageryProvider(new Cesium.WebMapTileServiceImageryProvider({url:"http://t0.tianditu.gov.cn/cia_w/wmts?tk="+td_key,layer:"cia",style:"default",tileMatrixSetID:"w",format:"tiles",maximumLevel:18}));const{path:e,key:t,value:i,style:o}=b();if(e&&t&&i)this.loadGeoJson({path:e},()=>{const e=this.restaurants.find(e=>{const{entity:o}=e;return o.properties[t]==i});this.selectItem=e,this.selectEntityColor(e.entity),this.visible=!0,v.flyTo(e.entity)});else{const{geo_list:e,current:t}=this,i=e[t];this.loadGeoJson(i)}this.addMapClick()},methods:{handleDraw(){this.isDraw=!this.isDraw},handleClear(){C.clear(),this.points=[]},handleExport(){const e=this.points.map(e=>{const t=e.map(e=>{const t=m(e);return[t.longitude,t.latitude]});return[t]}),t={type:"Feature",geometry:{type:"MultiPolygon",coordinates:e},properties:{}};window.exportCallback(t,e)},addMapClick(){y=new Cesium.ScreenSpaceEventHandler(v.canvas),y.setInputAction(this.handleMapClick.bind(this),Cesium.ScreenSpaceEventType.LEFT_CLICK)},removeMapClick(){y&&(y.removeInputAction(this.handleMapClick.bind(this),Cesium.ScreenSpaceEventType.LEFT_CLICK),y.destroy(),y=null)},handleMapClick(e){var t=v.scene.pick(e.position);if(Cesium.defined(t)&&t.primitive){const e=t.id,i=e.properties,o=i.propertyNames;this.selectItem={properties:o.map(e=>({key:e,value:i[e]._value}))},this.visible=!0,this.clearEntitysColor(this.restaurants),this.selectEntityColor(e)}else this.keyword||(this.visible=!1,this.selectItem={},this.clearEntitysColor(this.restaurants))},initDraw(){C=new d({viewer:v,callback:e=>{this.points.push(e)}})},querySearch(e,t){var i=this.restaurants,o=e?i.filter(this.createFilter(e)):i;t(o)},createFilter(e){return t=>t.value.toLowerCase().includes(e)},handleSelect(e){const{entity:t,properties:i}=e;this.selectItem=e,this.clearEntitysColor(this.restaurants),this.selectEntityColor(t),v.flyTo(t)},handleIconClear(){this.selectItem={},this.keyword=null,this.clearEntitysColor(this.restaurants)},handleGeoClick(e,t){v.dataSources.removeAll(),this.current=t,this.loadGeoJson(e)},handleHome(){v.dataSources._dataSources[1]?v.flyTo(v.dataSources._dataSources[1]):v.flyTo(v.entities)},handleShow(){this.visible=!this.visible},clearEntitysColor(e){const{geo_list:t,current:i}=this,{styles:o}=t[i];e.forEach(({entity:e})=>{e.polygon.extrudedHeight=0,e.polygon.outlineColor=o&&o.strokeColor?Cesium.Color.fromCssColorString(o.strokeColor):g,e.polygon.material=o&&o.fillColor?Cesium.Color.fromCssColorString(o.fillColor):f})},selectEntityColor(e){e.polygon.extrudedHeight=.1,e.polygon.outlineColor=w,e.polygon.material=S},loadGeoJson({path:e,styles:t},i){this.loading=!0,Cesium.GeoJsonDataSource.load(e,{markerSize:5,markerSymbol:"?"}).then(e=>{const o=e.entities.values;o.forEach(e=>{e.polygon.outline=!0,e.polygon.outlineWidth=3,e.polygon.outlineColor=t&&t.strokeColor?Cesium.Color.fromCssColorString(t.strokeColor):g,e.polygon.material=t&&t.fillColor?Cesium.Color.fromCssColorString(t.fillColor):f});const n=o.map(e=>{const t=e.properties,i=t.propertyNames;return{value:i.map(e=>t[e]).toString(),entity:e,properties:i.map(e=>({key:e,value:t[e]._value}))}});this.restaurants=n,v.dataSources.add(e),v.zoomTo(e).then(()=>{this.loading=!1}),"function"===typeof i&&i(n)})}}},_=k,P=(i("ef40"),i("2272")),A=Object(P["a"])(_,r,l,!1,null,null,null),E=A.exports,I={name:"App",components:{CesiumGeoJson:E}},O=I,x=(i("d964"),Object(P["a"])(O,n,s,!1,null,null,null)),T=x.exports,M=i("2e36"),L=i.n(M);i("5ba9"),i("03b0");o["a"].use(L.a),o["a"].config.productionTip=!1,new o["a"]({render:e=>e(T)}).$mount("#app")},"6e64":function(e,t,i){},d964:function(e,t,i){"use strict";i("0c5f")},ef40:function(e,t,i){"use strict";i("6e64")}});
//# sourceMappingURL=app.0e783c2e.js.map