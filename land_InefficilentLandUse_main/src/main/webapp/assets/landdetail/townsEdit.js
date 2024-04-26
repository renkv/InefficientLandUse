
layui.use(['layer','upload', 'form', 'formSelects','admin', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var layer = layui.layer;
    var upload = layui.upload;
    var formSelects = layui.formSelects;
    var category;

    var activeDlSelect = function () {
        var dldmV = $("#dldmV").val();
        var xdmV = $("#xdmV").val();
        //初始化区县
        $("#xdm").html('<option value="">请选择区县</option>');
        var qxAjax = new $ax(Feng.ctxPath + "/dict/listDictsByCode?dictTypeCode=sjzqx", function (data) {
            for (var i = 0; i < data.data.length; i++) {
                var name = data.data[i].name;
                var code = data.data[i].code;
                if(data.data[i].parentId === 0){
                    if(code === xdmV){
                        $("#xdm").append('<option value="' + code + '" selected="selected">'  + name + '</option>');
                    }else{
                        $("#xdm").append('<option value="' + code + '">'  + name + '</option>');
                    }
                }
            }
        }, function (data) {
        });
        //初始化大类
        $("#dldm").html('<option value="">请选择大类</option>');
        var ajax = new $ax(Feng.ctxPath + "/dict/listDictsByCode?dictTypeCode=LAND_TYPE", function (data) {
            category = data.data;
            for (var i = 0; i < data.data.length; i++) {
                var name = data.data[i].name;
                var code = data.data[i].code;
                if(data.data[i].parentId === 0){
                    if(code === dldmV){
                        $("#dldm").append('<option value="' + code + '" selected="selected">'  + name + '</option>');
                    }else{
                        $("#dldm").append('<option value="' + code + '">'  + name + '</option>');
                    }
                }
            }
        }, function (data) {
        });
        qxAjax.start();
        ajax.start();
        form.render();
    };
    activeDlSelect();
    /**
     * 给select框赋值
     */
    var setDefaultValue = function (){
        var xdmV = $("#xdmV").val();
        var dldmV = $("#dldmV").val();
        var xldmV = $("#xldmV").val();
        formSelects.value("xdm", xdmV);
        /*form.render('select', {
            'select': '#xdm'
            ,'value': xdmV
        });
        form.render('select', {
            'select': '#dldm'
            ,'value': dldmV
        });*/
        if(xldmV != "" && category.length >= 0){
            for (var i = 0; i < category.length; i++) {
                var name = category[i].name;
                var code = category[i].code;
                if(code != dldmV && code.includes(dldmV)){
                    if(code === xldmV){
                        $("#xldm").append('<option value="' + code + '" selected="selected">'  + name + '</option>');
                    }else{
                        $("#xldm").append('<option value="' + code + '">'  + name + '</option>');
                    }
                }
            }
        }
        form.render();
    }
    setDefaultValue();


    form.on('select(dldmSelect)',function (data){
        var value = data.value;
        var e = data.elem;
        var text = e[e.selectedIndex].text;
        $("#dlmc").val(text);
        $("#xldm").html('<option value="">请选择小类</option>');
        if(value !="" && category.length >= 0){
            for (var i = 0; i < category.length; i++) {
                var name = category[i].name;
                var code = category[i].code;
                if(code != value && code.includes(value)){
                    $("#xldm").append('<option value="' + code + '">'  + name + '</option>');
                }
            }
        }
        form.render();
    });
    form.on('select(xldmSelect)',function (data){
        var e = data.elem;
        var text = e[e.selectedIndex].text;
        $("#xlmc").val(text);
    });
    form.on('select(xdmSelect)',function (data){
        var e = data.elem;
        var text = e[e.selectedIndex].text;
        $("#xmc").val(text);
    });

    /**
     *
     * @param data 需要限制小数位数的数字
     * @param num 保留小数位数
     * @returns {*}
     */



        //执行实例
    var uploadInst = upload.render({
            elem: '#areaBtn'
            , url: Feng.ctxPath + '/landInfo/areaUpload'
            ,accept: 'file'
            ,acceptMime:'file/txt'
            ,size: 50 //最大允许上传的文件大小
            ,done: function (res) {
                if (res.code==500){
                    Feng.error("上传失败!" + res.message);
                }else{
                    $("#areaInfo").val(res.data.areaInfo);
                    $("#areaFileName").val(res.data.originalFilename);
                    $("#landArea").val(res.data.area);
                }
            }
            ,error: function () {
                Feng.error("上传失败!");
                //请求异常回调
            }
        });
    //多文件列表示例
    var demoListView = $('#demoList')
        ,uploadListIns = upload.render({
        elem: '#testList'
        ,url: Feng.ctxPath + '/system/upload?businessKey='+$("#businessKey").val() //改成您自己的上传接口
        ,accept: 'file'
        ,multiple: true
        ,auto: false
        ,bindAction: '#testListAction'
        ,choose: function(obj){
            files = this.files = obj.pushFile(); //将每次选择的文件追加到文件队列
            //读取本地文件
            obj.preview(function(index, file, result){
                var tr = $(['<tr id="upload-'+ index +'">'
                    ,'<td style="display:none"></td>'
                    ,'<td>'+ file.name +'</td>'
                    ,'<td>'+ (file.size/1024).toFixed(1) +'kb</td>'
                    ,'<td>等待上传</td>'
                    ,'<td>'
                    ,'<button class="layui-btn layui-btn-xs demo-reload layui-hide">重传</button>'
                    ,'<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>'
                    ,'</td>'
                    ,'</tr>'].join(''));

                //单个重传
                tr.find('.demo-reload').on('click', function(){
                    obj.upload(index, file);
                });

                //删除
                tr.find('.demo-delete').on('click', function(){
                    var tds = tr.children();
                    var fileID = tds.eq(0).html();
                    /*var receiveUserName = $("#receiveUserName").val();
                    var receiveUser = $("#receiveUser").val();
                    var shareTitle = $("#shareTitle").val();
                    var businessKey = $("#businessKey").val();
                    var shareId = $("#shareId").val();
                    var remark = $("#remark").val();*/
                    if(fileID != ""){
                        $.ajax({
                            url: Feng.ctxPath + '/system/deleteFiles',
                            type: "POST",
                            data: {"ids":fileID},
                            dataType:"json",
                            success: function (res) {
                                delete files[index]; //删除对应的文件
                                tr.remove();
                                uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                            },
                            error: function (err) {
                                console.log('err', err);
                            }
                        });
                    }else{
                        delete files[index]; //删除对应的文件
                        tr.remove();
                        uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                    }
                    /* $("#receiveUserName").val(receiveUserName);
                     $("#receiveUser").val(receiveUser);
                     $("#shareTitle").val(shareTitle);
                     $("#businessKey").val(businessKey);
                     $("#shareId").val(shareId);
                     $("#remark").val(remark);*/
                });
                demoListView.append(tr);
            });
        }
        ,done: function(res, index, upload){
            if(res.success){ //上传成功
                var tr = demoListView.find('tr#upload-'+ index)
                    ,tds = tr.children();
                tds.eq(0).html(res.data.fileId);
                tds.eq(3).html('<span style="color: #5FB878;">上传成功</span>');
                tds.eq(4).html('<button class="layui-btn layui-btn-xs layui-btn-danger demo-delete">删除</button>'); //清空操作
                //删除
                tr.find('.demo-delete').on('click', function(){
                    var fileID = tds.eq(0).html();
                    /* var receiveUserName = $("#receiveUserName").val();
                     var receiveUser = $("#receiveUser").val();
                     var shareTitle = $("#shareTitle").val();
                     var businessKey = $("#businessKey").val();
                     var shareId = $("#shareId").val();
                     var remark = $("#remark").val();*/
                    //debugger;
                    if(fileID != ""){
                        $.ajax({
                            url: Feng.ctxPath + '/system/deleteFiles',
                            type: "POST",
                            data: {"ids":fileID},
                            dataType:"json",
                            success: function (res) {
                                delete files[index]; //删除对应的文件
                                tr.remove();
                                uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                            },
                            error: function (err) {
                                console.log('err', err);
                            }
                        });
                    }else{
                        delete files[index]; //删除对应的文件
                        tr.remove();
                        uploadListIns.config.elem.next()[0].value = ''; //清空 input file 值，以免删除后出现同名文件不可选
                    }
                    /*   $("#receiveUserName").val(receiveUserName);
                       $("#receiveUser").val(receiveUser);
                       $("#shareTitle").val(shareTitle);
                       $("#businessKey").val(businessKey);
                       $("#shareId").val(shareId);
                       $("#remark").val(remark);*/
                });
                return delete this.files[index]; //删除文件队列已经上传成功的文件
            }
            this.error(index, upload);
        }
        ,error: function(index, upload){
            var tr = demoListView.find('tr#upload-'+ index)
                ,tds = tr.children();
            tds.eq(3).html('<span style="color: #FF5722;">上传失败</span>');
            tds.eq(4).find('.demo-reload').removeClass('layui-hide'); //显示重传
        }
    });
    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/landdetail/saveLandDetail", function (data) {
            Feng.success("提交成功！");
            //传给上个页面，刷新table用
            admin.putTempData('formOk', true);
            //关掉对话框
            admin.closeThisDialog();
        }, function (data) {
            Feng.error("提交失败！" + data.responseJSON.message)
        });
        ajax.set(data.field);
        ajax.start();
        return false;
    });
});

function deleteFile(obj,fileId){
    var td = $(obj).parent();
    var tr = td.parent("tr");
    if(fileId != ""){
        $.ajax({
            url: Feng.ctxPath + '/system/deleteFiles',
            type: "POST",
            data: {"ids":fileId},
            dataType:"json",
            success: function (res) {
                tr.remove();
            },
            error: function (err) {
                console.log('err', err);
            }
        });
    }else{
        tr.remove();
    }
}

function numLimit(data,num){
    // 先把非数字的都替换掉(空)，除了数字和.
    data = data.replace(/[^\d.]/g, "");
    // 必须保证第一个为数字而不是.
    data = data.replace(/^\./g, "");
    // 保证只有出现一个.而没有多个.
    data = data.replace(/\.{3,}/g, "");
    // 保证.只出现一次，而不能出现两次以上
    data =data
        .replace(".", "$#$")
        .replace(/\./g, "")
        .replace("$#$", ".");
    // 限制几位小数
    let subscript = -1;
    for (let i in data) {
        if (data[i] === ".") {
            subscript = i;
        }
        if (subscript !== -1) {
            if (i - subscript > num) {
                data = data.substring(0, data.length - 1);
            }
        }
    }
    return data;
}
