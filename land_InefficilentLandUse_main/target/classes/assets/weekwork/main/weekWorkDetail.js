layui.use(['table', 'admin', 'ax', 'func', 'laydate', 'formSelects'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var func = layui.func;
    var laydate = layui.laydate;
    var formSelects = layui.formSelects;
    $("textarea[name='instruction']").blur(function(){

        var instruction = $(this).val();
        var tr = $(this).parent().parent();
        var id = tr.find("td:first").text();
        console.log(id);
        $.ajax({
            url: Feng.ctxPath + '/weekWork/saveWeekWorkDetail',
            type: "POST",
            data: {"instruction":instruction,"id":id},
            dataType:"json",
            success: function (res) {
                //window.location.href = Feng.ctxPath + '/weekWork';
            },
            error: function (err) {
                console.log('err', err);
            }
        });
    });
});