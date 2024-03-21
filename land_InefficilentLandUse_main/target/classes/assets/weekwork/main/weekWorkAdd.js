layui.use(['table', 'admin', 'ax', 'func', 'laydate', 'formSelects'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var func = layui.func;
    var laydate = layui.laydate;
    var formSelects = layui.formSelects;

    //渲染时间选择框
    var laydate = layui.laydate;
    /**
     * 进行中表格
     */
    var weekWorkIngTable = {
        tableId: "weekWorkIngTable"
    };
    //暂停中
    var weekWorkStopTable = {
        tableId: "weekWorkStopTable"
    };
    //跟进中
    var weekWorkGJTable = {
        tableId: "weekWorkGJTable"
    };
    //已完成
    var weekWorkFinishTable = {
        tableId: "weekWorkFinishTable"
    };
    //计划跟进中
    var weekWorkJHGJTable = {
        tableId: "weekWorkJHGJTable"
    };
    //培训
    var weekWorkPXTable = {
        tableId: "weekWorkPXTable"
    };
    //会议
    var weekWorkHYTable = {
        tableId: "weekWorkHYTable"
    };
    //
    var weekWorkRemarkTable = {
        tableId: "weekWorkRemarkTable"
    };
    // 进行中按钮添加
    $('#jxBtn').click(function () {
        weekWorkIngTable.addTr();
    });
    // 暂停中新增
    $('#ztBtn').click(function () {
        weekWorkStopTable.addTr();
    });
    // 跟进中新增
    $('#gjjBtn').click(function () {
        weekWorkGJTable.addTr();
    });
    // 已完成新增
    $('#ywnBtn').click(function () {
        weekWorkFinishTable.addTr();
    });
    // 计划跟进中新增
    $('#jhgjBtn').click(function () {
        weekWorkJHGJTable.addTr();
    });
    // 培训新增
    $('#pxBtn').click(function () {
        weekWorkPXTable.addTr();
    });
    // 会议新增
    $('#hyBtn').click(function () {
        weekWorkHYTable.addTr();
    });
    var show_count = 200;   //要显示的条数
    var count = 1;    //递增的开始值，这里是你的ID
    //点击新增
    weekWorkIngTable.addTr = function () {
        var length = $("#weekWorkIngTable tbody tr").length;
        if (length < show_count)    //点击时候，如果当前的数字小于递增结束的条件
        {
            $("#tab11 tbody tr").clone().appendTo("#weekWorkIngTable tbody");   //在表格后面添加一行
            weekWorkIngTable.changeIndex();//更新行号
        }
    };
    weekWorkStopTable.addTr = function () {
        var length = $("#weekWorkStopTable tbody tr").length;
        if (length < show_count)    //点击时候，如果当前的数字小于递增结束的条件
        {
            $("#tab11 tbody tr").clone().appendTo("#weekWorkStopTable tbody");   //在表格后面添加一行
            weekWorkIngTable.changeIndex();//更新行号
        }
    };
    weekWorkGJTable.addTr = function () {
        var length = $("#weekWorkGJTable tbody tr").length;
        if (length < show_count)    //点击时候，如果当前的数字小于递增结束的条件
        {
            $("#tab11 tbody tr").clone().appendTo("#weekWorkGJTable tbody");   //在表格后面添加一行
            weekWorkIngTable.changeIndex();//更新行号
        }
    };
    weekWorkFinishTable.addTr = function () {
        var length = $("#weekWorkFinishTable tbody tr").length;
        if (length < show_count)    //点击时候，如果当前的数字小于递增结束的条件
        {
            $("#tab11 tbody tr").clone().appendTo("#weekWorkFinishTable tbody");   //在表格后面添加一行
            weekWorkIngTable.changeIndex();//更新行号
        }
    };
    weekWorkJHGJTable.addTr = function () {
        var length = $("#weekWorkJHGJTable tbody tr").length;
        if (length < show_count)    //点击时候，如果当前的数字小于递增结束的条件
        {
            $("#tab11 tbody tr").clone().appendTo("#weekWorkJHGJTable tbody");   //在表格后面添加一行
            weekWorkIngTable.changeIndex();//更新行号
        }
    };
    weekWorkPXTable.addTr = function () {
        var length = $("#weekWorkPXTable tbody tr").length;
        if (length < show_count)    //点击时候，如果当前的数字小于递增结束的条件
        {
            $("#tab11 tbody tr").clone().appendTo("#weekWorkPXTable tbody");   //在表格后面添加一行
            weekWorkIngTable.changeIndex();//更新行号
        }
    };
    weekWorkHYTable.addTr = function () {
        var length = $("#weekWorkHYTable tbody tr").length;
        if (length < show_count)    //点击时候，如果当前的数字小于递增结束的条件
        {
            $("#tab11 tbody tr").clone().appendTo("#weekWorkHYTable tbody");   //在表格后面添加一行
            weekWorkIngTable.changeIndex();//更新行号
        }
    };
    //更新行号
    weekWorkIngTable.changeIndex = function changeIndex() {
        var i = 1;
        $("#weekWorkIngTable tbody tr").each(function () { //循环tab tbody下的tr
            $(this).find("input[name='serialNo']").val(i++);//更新行号
            $(this).find("input[name='Button1']").bind('click',function () {
                $(this).parent().parent().remove();//移除当前行
                weekWorkIngTable.changeIndex();
            });//更新行号
        });
        $("#weekWorkStopTable tbody tr").each(function () { //循环tab tbody下的tr
            $(this).find("input[name='serialNo']").val(i++);//更新行号
            $(this).find("input[name='Button1']").bind('click',function () {
                $(this).parent().parent().remove();//移除当前行
                weekWorkIngTable.changeIndex();
            });//更新行号
        });
        $("#weekWorkGJTable tbody tr").each(function () { //循环tab tbody下的tr
            $(this).find("input[name='serialNo']").val(i++);//更新行号
            $(this).find("input[name='Button1']").bind('click',function () {
                $(this).parent().parent().remove();//移除当前行
                weekWorkIngTable.changeIndex();
            });//更新行号
        });
        $("#weekWorkFinishTable tbody tr").each(function () { //循环tab tbody下的tr
            $(this).find("input[name='serialNo']").val(i++);//更新行号
            $(this).find("input[name='Button1']").bind('click',function () {
                $(this).parent().parent().remove();//移除当前行
                weekWorkIngTable.changeIndex();
            });//更新行号
        });
        $("#weekWorkJHGJTable tbody tr").each(function () { //循环tab tbody下的tr
            $(this).find("input[name='serialNo']").val(i++);//更新行号
            $(this).find("input[name='Button1']").bind('click',function () {
                $(this).parent().parent().remove();//移除当前行
                weekWorkIngTable.changeIndex();
            });//更新行号
        });
        $("#weekWorkPXTable tbody tr").each(function () { //循环tab tbody下的tr
            $(this).find("input[name='serialNo']").val(i++);//更新行号
            $(this).find("input[name='Button1']").bind('click',function () {
                $(this).parent().parent().remove();//移除当前行
                weekWorkIngTable.changeIndex();
            });//更新行号
        });
        $("#weekWorkHYTable tbody tr").each(function () { //循环tab tbody下的tr
            $(this).find("input[name='serialNo']").val(i++);//更新行号
            $(this).find("input[name='Button1']").bind('click',function () {
                $(this).parent().parent().remove();//移除当前行
                weekWorkIngTable.changeIndex();
            });//更新行号
        });
    };

    $('#cancel').click(function(){
        window.location.href = Feng.ctxPath + '/weekWork';
    });

    $("#btnSubmit").click(function () {
        var weekWorkIngList = [];
        var weekWorkStopList = [];
        var weekWorkGJList = [];
        var weekWorkFinishList = [];
        var weekWorkJHGJList = [];
        var weekWorkPXList = [];
        var weekWorkHYList = [];
        var mainRemark = "";
        $("#weekWorkRemarkTable tbody tr").each(function () {
            mainRemark = $(this).find("input[name='remark']").val();
        });
        $("#weekWorkIngTable tbody tr").each(function () { //循环tab tbody下的tr
            var serialNo = $(this).find("input[name='serialNo']").val();
            var taskName = $(this).find("input[name='taskName']").val();
            var implementUser = $(this).find("input[name='implementUser']").val();
            var assignTime = $(this).find("input[name='assignTime']").val();
            var planFinishTime = $(this).find("input[name='planFinishTime']").val();
            var actualProgress = $(this).find("input[name='actualProgress']").val();
            var leader = $(this).find("input[name='leader']").val();
            var supervision = $(this).find("#supervision").val();
            var superviseTime = $(this).find("input[name='superviseTime']").val();
            /*var remark = $(this).find("input[name='remark']").val();*/
            var ingObj = {
                "serialNo":serialNo,
                "taskName":taskName,
                "implementUser":implementUser,
                "assignTime":assignTime,
                "planFinishTime":planFinishTime,
                "actualProgress":actualProgress,
                "leader":leader,
                "supervision":supervision,
                "superviseTime":superviseTime,
                /*"remark":remark,*/
                "status":"0"
            };
            weekWorkIngList.push(ingObj);
        });
        $("#weekWorkStopTable tbody tr").each(function () { //循环tab tbody下的tr
            var serialNo = $(this).find("input[name='serialNo']").val();
            var taskName = $(this).find("input[name='taskName']").val();
            var implementUser = $(this).find("input[name='implementUser']").val();
            var assignTime = $(this).find("input[name='assignTime']").val();
            var planFinishTime = $(this).find("input[name='planFinishTime']").val();
            var actualProgress = $(this).find("input[name='actualProgress']").val();
            var leader = $(this).find("input[name='leader']").val();
            var supervision = $(this).find("#supervision").val();
            var superviseTime = $(this).find("input[name='superviseTime']").val();
            /*var remark = $(this).find("input[name='remark']").val();*/
            var ingObj = {
                "serialNo":serialNo,
                "taskName":taskName,
                "implementUser":implementUser,
                "assignTime":assignTime,
                "planFinishTime":planFinishTime,
                "actualProgress":actualProgress,
                "leader":leader,
                "supervision":supervision,
                "superviseTime":superviseTime,
                /*"remark":remark,*/
                "status":"1"
            };
            weekWorkStopList.push(ingObj);
        });
        $("#weekWorkGJTable tbody tr").each(function () { //循环tab tbody下的tr
            var serialNo = $(this).find("input[name='serialNo']").val();
            var taskName = $(this).find("input[name='taskName']").val();
            var implementUser = $(this).find("input[name='implementUser']").val();
            var assignTime = $(this).find("input[name='assignTime']").val();
            var planFinishTime = $(this).find("input[name='planFinishTime']").val();
            var actualProgress = $(this).find("input[name='actualProgress']").val();
            var leader = $(this).find("input[name='leader']").val();
            var supervision = $(this).find("#supervision").val();
            var superviseTime = $(this).find("input[name='superviseTime']").val();
            /*var remark = $(this).find("input[name='remark']").val();*/
            var ingObj = {
                "serialNo":serialNo,
                "taskName":taskName,
                "implementUser":implementUser,
                "assignTime":assignTime,
                "planFinishTime":planFinishTime,
                "actualProgress":actualProgress,
                "leader":leader,
                "supervision":supervision,
                "superviseTime":superviseTime,
                /*"remark":remark,*/
                "status":"2"
            };
            weekWorkGJList.push(ingObj);
        });
        $("#weekWorkFinishTable tbody tr").each(function () { //循环tab tbody下的tr
            var serialNo = $(this).find("input[name='serialNo']").val();
            var taskName = $(this).find("input[name='taskName']").val();
            var implementUser = $(this).find("input[name='implementUser']").val();
            var assignTime = $(this).find("input[name='assignTime']").val();
            var planFinishTime = $(this).find("input[name='planFinishTime']").val();
            var actualProgress = $(this).find("input[name='actualProgress']").val();
            var leader = $(this).find("input[name='leader']").val();
            var supervision = $(this).find("#supervision").val();
            var superviseTime = $(this).find("input[name='superviseTime']").val();
            /*var remark = $(this).find("input[name='remark']").val();*/
            var ingObj = {
                "serialNo":serialNo,
                "taskName":taskName,
                "implementUser":implementUser,
                "assignTime":assignTime,
                "planFinishTime":planFinishTime,
                "actualProgress":actualProgress,
                "leader":leader,
                "supervision":supervision,
                "superviseTime":superviseTime,
                /*"remark":remark,*/
                "status":"3"
            };
            weekWorkFinishList.push(ingObj);
        });
        $("#weekWorkJHGJTable tbody tr").each(function () { //循环tab tbody下的tr
            var serialNo = $(this).find("input[name='serialNo']").val();
            var taskName = $(this).find("input[name='taskName']").val();
            var implementUser = $(this).find("input[name='implementUser']").val();
            var assignTime = $(this).find("input[name='assignTime']").val();
            var planFinishTime = $(this).find("input[name='planFinishTime']").val();
            var actualProgress = $(this).find("input[name='actualProgress']").val();
            var leader = $(this).find("input[name='leader']").val();
            var supervision = $(this).find("#supervision").val();
            var superviseTime = $(this).find("input[name='superviseTime']").val();
            /*var remark = $(this).find("input[name='remark']").val();*/
            var ingObj = {
                "serialNo":serialNo,
                "taskName":taskName,
                "implementUser":implementUser,
                "assignTime":assignTime,
                "planFinishTime":planFinishTime,
                "actualProgress":actualProgress,
                "leader":leader,
                "supervision":supervision,
                "superviseTime":superviseTime,
                /*"remark":remark,*/
                "status":"4"
            };
            weekWorkJHGJList.push(ingObj);
        });
        $("#weekWorkPXTable tbody tr").each(function () { //循环tab tbody下的tr
            var serialNo = $(this).find("input[name='serialNo']").val();
            var taskName = $(this).find("input[name='taskName']").val();
            var implementUser = $(this).find("input[name='implementUser']").val();
            var assignTime = $(this).find("input[name='assignTime']").val();
            var planFinishTime = $(this).find("input[name='planFinishTime']").val();
            var actualProgress = $(this).find("input[name='actualProgress']").val();
            var leader = $(this).find("input[name='leader']").val();
            var supervision = $(this).find("#supervision").val();
            var superviseTime = $(this).find("input[name='superviseTime']").val();
            /*var remark = $(this).find("input[name='remark']").val();*/
            var ingObj = {
                "serialNo":serialNo,
                "taskName":taskName,
                "implementUser":implementUser,
                "assignTime":assignTime,
                "planFinishTime":planFinishTime,
                "actualProgress":actualProgress,
                "leader":leader,
                "supervision":supervision,
                "superviseTime":superviseTime,
                /*"remark":remark,*/
                "status":"5"
            };
            weekWorkPXList.push(ingObj);
        });
        $("#weekWorkHYTable tbody tr").each(function () { //循环tab tbody下的tr
            var serialNo = $(this).find("input[name='serialNo']").val();
            var taskName = $(this).find("input[name='taskName']").val();
            var implementUser = $(this).find("input[name='implementUser']").val();
            var assignTime = $(this).find("input[name='assignTime']").val();
            var planFinishTime = $(this).find("input[name='planFinishTime']").val();
            var actualProgress = $(this).find("input[name='actualProgress']").val();
            var leader = $(this).find("input[name='leader']").val();
            var supervision = $(this).find("#supervision").val();
            var superviseTime = $(this).find("input[name='superviseTime']").val();
            /*var remark = $(this).find("input[name='remark']").val();*/
            var ingObj = {
                "serialNo":serialNo,
                "taskName":taskName,
                "implementUser":implementUser,
                "assignTime":assignTime,
                "planFinishTime":planFinishTime,
                "actualProgress":actualProgress,
                "leader":leader,
                "supervision":supervision,
                "superviseTime":superviseTime,
                /*"remark":remark,*/
                "status":"6"
            };
            weekWorkHYList.push(ingObj);
        });
        $.ajax({
            url: Feng.ctxPath + '/weekWork/saveWeekWork',
            type: "POST",
            data: {"remark":mainRemark,"weekWorkIngJson":JSON.stringify(weekWorkIngList),"weekWorkStopJson":JSON.stringify(weekWorkStopList),
            "weekWorkGJJson":JSON.stringify(weekWorkGJList),"weekWorkFinishJson":JSON.stringify(weekWorkFinishList),
                "weekWorkJHGJJson":JSON.stringify(weekWorkJHGJList),"weekWorkPXJson":JSON.stringify(weekWorkPXList),"weekWorkHYJson":JSON.stringify(weekWorkHYList)},
            dataType:"json",
            success: function (res) {
                window.location.href = Feng.ctxPath + '/weekWork';
            },
            error: function (err) {
                console.log('err', err);
            }
        });
    });
});
