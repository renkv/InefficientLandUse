$(function(){
    $("input[type=file]").change(function(){
        $(this).parents(".uploader").find(".filename1").val($(this).val());
    });
    $("input[type=file]").each(function(){
        if($(this).val()==""){$(this).parents(".uploader").find(".filename1").val("No file selected...");}
    });
});


layui.use(['layer','upload', 'form', 'admin', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var upload = layui.upload;

//执行实例
    var uploadInst = upload.render({
        elem: '#fileBtn'
        , url: Feng.ctxPath + '/policy/savePolicy'
        ,accept: 'file'
        ,acceptMime:'application/pdf,application/msword,application/vnd.openxmlformats-officedocument.wordprocessingml.document,image/*'
        ,auto: false
   /*     ,bindAction: '#submitBtn'*/
        ,drag:true
        ,size: 50*1024 //最大允许上传的文件大小
        ,done: function (res) {
            if (res.code==500){
                Feng.error("上传失败!" + res.message);
            }
        }
        ,error: function () {
            Feng.error("上传失败!");
            //请求异常回调
        }
    });
    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var formData = new FormData($("#policyAddForm")[0]);
        var file = formData.get("file");
        if(file.size == 0){
            layer.alert('请选择文件', {
                icon: 2,
                title:"提示"
            });
            return false ;
        }
        $.ajax({
            cache : true,
            type : "post",
            url : Feng.ctxPath + "/policy/savePolicy",
            async : false,
            data : formData,  // 你的formid
            contentType: false,   //jax 中 contentType 设置为 false 是为了避免 JQuery 对其操作，从而失去分界符，而使服务器不能正常解析文件
            processData: false,   //当设置为true的时候,jquery ajax 提交的时候不会序列化 data，而是直接使用data
            error : function(request) {
                layer.alert('提交失败', {
                    icon: 2,
                    title:"提示"
                });
            },
            success : function(ret) {
                if (ret.success) {
                    Feng.success("提交成功！");
                    //传给上个页面，刷新table用
                    admin.putTempData('formOk', true);
                    //关掉对话框
                    admin.closeThisDialog();
                } else {
                    Feng.error("提交失败！" + ret.message)
                }
            }
        })
        /*var ajax = new $ax(Feng.ctxPath + "/policy/savePolicy", function (data) {
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
        return false;*/
    });
});


