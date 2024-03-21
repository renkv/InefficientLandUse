layui.use(['layer','upload', 'form', 'admin', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var layer = layui.layer;
    var upload = layui.upload;
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
    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/landInfo/saveLandInfo", function (data) {
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
