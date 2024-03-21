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
    var files;
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
                    var receiveUserName = $("#receiveUserName").val();
                    var receiveUser = $("#receiveUser").val();
                    var shareTitle = $("#shareTitle").val();
                    var businessKey = $("#businessKey").val();
                    var shareId = $("#shareId").val();
                    var remark = $("#remark").val();
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
                    $("#receiveUserName").val(receiveUserName);
                    $("#receiveUser").val(receiveUser);
                    $("#shareTitle").val(shareTitle);
                    $("#businessKey").val(businessKey);
                    $("#shareId").val(shareId);
                    $("#remark").val(remark);
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
                    var receiveUserName = $("#receiveUserName").val();
                    var receiveUser = $("#receiveUser").val();
                    var shareTitle = $("#shareTitle").val();
                    var businessKey = $("#businessKey").val();
                    var shareId = $("#shareId").val();
                    var remark = $("#remark").val();
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
                    $("#receiveUserName").val(receiveUserName);
                    $("#receiveUser").val(receiveUser);
                    $("#shareTitle").val(shareTitle);
                    $("#businessKey").val(businessKey);
                    $("#shareId").val(shareId);
                    $("#remark").val(remark);
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
        var ajax = new $ax(Feng.ctxPath + "/fileShare/saveFileShare", function (data) {
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
