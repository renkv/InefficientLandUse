/**
 * 角色详情对话框
 */
var fileShareInfoDlg = {
    data: {
        receiveUser: "",
        receiveUserName: ""
    }
};

layui.use(['layer','upload', 'form', 'admin', 'ax'], function () {
    var $ = layui.jquery;
    var $ax = layui.ax;
    var form = layui.form;
    var admin = layui.admin;
    var layer = layui.layer;
    var upload = layui.upload;

    // 点击接收人员
    $('#receiveUserName').click(function () {
        var formName = encodeURIComponent("parent.fileShareInfoDlg.data.receiveUserName");
        var formId = encodeURIComponent("parent.fileShareInfoDlg.data.receiveUser");
        var userIds = "";

        layer.open({
            type: 2,
            title: '接收人员',
            area: ['600px', '600px'],
            content: Feng.ctxPath + '/mgr/userSelect?userIds=' + userIds+'&formName=' + formName + "&formId=" + formId ,
            end: function () {
                $("#receiveUser").val(fileShareInfoDlg.data.receiveUser);
                $("#receiveUserName").val(fileShareInfoDlg.data.receiveUserName);
            }
        });
    });

    // 表单提交事件
    form.on('submit(btnSubmit)', function (data) {
        var ajax = new $ax(Feng.ctxPath + "/fileReceive/saveFileForward", function (data) {
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

