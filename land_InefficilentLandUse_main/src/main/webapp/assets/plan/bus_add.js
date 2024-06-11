/**
 * 用户详情对话框
 */
var LandInfoDlg = {
    data: {
        landCode: "",
        xmmc: "",
        dkmj:""
    }
};
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
    if (data.indexOf(".") < 0 && num != "") {//以上已经过滤，此处控制的是如果没有小数点，首位不能为类似于 01、02的金额
        data = parseFloat(data);
    }
    return data;
}

layui.use(['table','layer', 'form', 'admin', 'laydate', 'ax', 'formSelects'], function () {
    var $ = layui.$;
    var table = layui.table;
    var form = layui.form;
    var $ax = layui.ax;
    var admin = layui.admin;
    var laydate = layui.laydate;
    var layer = layui.layer;
    var formSelects = layui.formSelects;
// 点击项目名称
    $('#xmmcSelect').click(function () {
        var deptName =$("#deptName").val();
        layer.open({
            type: 2,
            title: '选择地块',
            area: ['800px', '600px'],
            content: Feng.ctxPath + '/landdetail/commonSelect?deptName=' + deptName,
            end: function () {
                $("#landCode").val(LandInfoDlg.data.landCode);
                $("#xmmcSelect").val(LandInfoDlg.data.xmmc);
                $("#busName").val(LandInfoDlg.data.xmmc);
                $("#dkmj").val(LandInfoDlg.data.dkmj);
                $("#occupyArea").val(LandInfoDlg.data.dkmj);
            }
        });
    });




    //渲染时间选择框
    laydate.render({
        elem: '#planStartTime',
        range: false,
        min: Feng.currentDate()
    });



    var activeDlSelect = function () {
        var deptName = $("#deptName").val();
        //初始化区县
        $("#xdm").html('<option value="">请选择区县</option>');
        var qxAjax = new $ax(Feng.ctxPath + "/dict/listDictsByCode?dictTypeCode=sjzqx", function (data) {
            for (var i = 0; i < data.data.length; i++) {
                var name = data.data[i].name;
                var code = data.data[i].code;
                if(data.data[i].parentId === 0){
                    if(deptName != "" ){
                        if(deptName.indexOf(name) >= 0){
                            $("#xdm").append('<option value="' + code + '">'  + name + '</option>');
                        }
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
                    $("#dldm").append('<option value="' + code + '">'  + name + '</option>');
                }
            }
        }, function (data) {
        });

        //初始化行业
        $("#hydm").html('<option value="">请选择行业</option>');
        var hyajax = new $ax(Feng.ctxPath + "/dict/listDictsByCode?dictTypeCode=INDUSTRY", function (data) {
            for (var i = 0; i < data.data.length; i++) {
                var name = data.data[i].name;
                var code = data.data[i].code;
                if(data.data[i].parentId === 0){
                    $("#hydm").append('<option value="' + code + '">'  + name + '</option>');
                }
            }
        }, function (data) {
        });
        qxAjax.start();
        ajax.start();
        hyajax.start();
        form.render();
    };
    activeDlSelect();

    laydate.render({
        elem: '#crsj',
        range: false
    });
    form.on('select(typeSelect)',function (data){
        var value = data.value;
        if(value  == '1'){
            $("#readyDiv").css("display","block");
            $("#landDiv").css("display","none");
            $("#div2").css("display","none");
            $("#moreShow").css("display","none");
        }else {
            $("#readyDiv").css("display","none");
            $("#landDiv").css("display","block");
            $("#div2").css("display","block");
            $("#moreShow").css("display","block");
        }
        form.render();
    });
    var isShow = 0;
    $('#moreShow').click(function () {
        if(isShow == 0){
            $("#cyDiv").css("display","block");
            isShow =1;
        }else{
            $("#cyDiv").css("display","none");
            isShow =0;
        }
    });

    $('#dkmj').on('blur', function(){
        // 在这里编写失去焦点时的逻辑
        var dkmj = $("#dkmj").val();
        $("#occupyArea").val(dkmj);
    });


    /*form.on('select(categorySelect)',function (data){
        var value = data.value;
        var e = data.elem;
        if(value != "" && value == 'industries'){
            $("#cyDiv").css("display","block");
        }else {
            $("#cyDiv").css("display","none");
        }
        form.render();
    });*/

    form.on('select(dldmSelect)',function (data){
        var value = data.value;
        var e = data.elem;
        var text = e[e.selectedIndex].text;
        $("#dlmc").val(text);
        $("#xldm").html('<option value="">请选择小类</option>');
        if(value != "" && category.length >= 0){
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
    form.on('select(hydmSelect)',function (data){
        var e = data.elem;
        var text = e[e.selectedIndex].text;
        $("#hymc").val(text);
    });
    form.on('select(xdmSelect)',function (data){
        var e = data.elem;
        var text = e[e.selectedIndex].text;
        $("#xmc").val(text);
    });

    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var dkmj = $("#dkmj").val();
        $("#occupyArea").val(dkmj);
        var type = $('select[name="type"]').next().find('.layui-this').attr('lay-value');
        var landCode = $("#landCode").val();
        if(type == "1"){
            if(landCode == ""){
                layer.msg('请填写关联项目名称');
                return false;
            }
        }else{
            var xmc = $("#xmc").val();
            var xmmc = $("#xmmc").val();
            var dkmj = $("#dkmj").val();
            if(xmc == "" || xmmc== "" || dkmj == ""){
                layer.msg('必填项不能为空!!!');
                return false;
            }
        }
        var ajax = new $ax(Feng.ctxPath + "/plan/savePlan?landCode="+landCode, function (data) {
            Feng.success("保存成功！");
            //传给上个页面，刷新table用
            admin.putTempData('formOk', true);
            //关掉对话框
            admin.closeThisDialog();
        }, function (data) {
            Feng.error("保存失败！" + data.responseJSON.message)
        });
        ajax.set(data.field);
        ajax.start();
        //添加 return false 可成功跳转页面
        return false;
    });
});



