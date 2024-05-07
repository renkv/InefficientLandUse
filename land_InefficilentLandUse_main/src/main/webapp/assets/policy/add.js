
layui.use(['layer','upload', 'form', 'admin', 'formSelects','ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var upload = layui.upload;
    var formSelects = layui.formSelects;

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
        ,choose:function (obj){
            obj.preview(function (index,file,result){
                var filename = file.name;
                $("#checkFileName").val(filename);
            })
        }
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
    var setDefaultValue = function (){
        var policyType = $("#policyTypeV").val();
        formSelects.value("policyType", policyType);
        if(policyType != null && policyType != ""){
            if("1" === policyType){
                $("#policyType").append('<option value="1" selected="selected">国家级</option>');
                $("#policyType").append('<option value="2">省级</option>');
                $("#policyType").append('<option value="3">市级</option>');
                $("#policyType").append('<option value="4">县级</option>');
            }else if ("2" === policyType){
                $("#policyType").append('<option value="1">国家级</option>');
                $("#policyType").append('<option value="2" selected="selected">省级</option>');
                $("#policyType").append('<option value="3">市级</option>');
                $("#policyType").append('<option value="4">县级</option>');
            }else if ("3" === policyType){
                $("#policyType").append('<option value="1">国家级</option>');
                $("#policyType").append('<option value="2">省级</option>');
                $("#policyType").append('<option value="3" selected="selected">市级</option>');
                $("#policyType").append('<option value="4">县级</option>');
            }else if ("4" === policyType){
                $("#policyType").append('<option value="1">国家级</option>');
                $("#policyType").append('<option value="2">省级</option>');
                $("#policyType").append('<option value="3">市级</option>');
                $("#policyType").append('<option value="4" selected="selected">县级</option>');
            }else{
                $("#policyType").append('<option value="1">国家级</option>');
                $("#policyType").append('<option value="2">省级</option>');
                $("#policyType").append('<option value="3">市级</option>');
                $("#policyType").append('<option value="4">县级</option>');
            }
        }else{
            $("#policyType").append('<option value="1">国家级</option>');
            $("#policyType").append('<option value="2">省级</option>');
            $("#policyType").append('<option value="3">市级</option>');
            $("#policyType").append('<option value="4">县级</option>');
        }
        form.render();
    }
    setDefaultValue();

    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var formData = new FormData($("#policyAddForm")[0]);
        var file = formData.get("file");
        var fileId = $("#fileId").val();
        if(fileId == "" && file.size == 0){
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
    });
});


