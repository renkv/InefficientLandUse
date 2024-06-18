layui.config({
}).extend({
    tableMerge: 'tableMerge'
});
layui.use(['table', 'admin','laydate','tableMerge', 'ax', 'func','upload'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var func = layui.func;
    var laydate = layui.laydate;
    var upload = layui.upload;
    var tableMerge = layui.tableMerge;
    var tips;


    /**
     * 文件发送管理
     */
    var detailMainTable = {
        tableId: "detailMainTable"
    };

    /**
     * 初始化表格的列
     */
    detailMainTable.initColumn = function () {
        return [[
            {field: 'xmc', sort: false,align: 'center', title: '县名称'},
            {field: 'year', sort: false,align: 'center', title: '年份'},
            {field: 'totalArea', sort: false,align: 'center', title: '上报面积'},
            {field: 'finishArea', sort: false,align: 'center', title: '完成面积'},
            {field: 'comratio', sort: false,align: 'center',title: '完成比例(%)'},
        ]];
    };

    // 渲染表格
    var tableResult = table.render({
        id: detailMainTable.tableId,
        elem: '#' + detailMainTable.tableId,
        url: Feng.ctxPath + '/statistics/inbusList',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: detailMainTable.initColumn(),
        done:function (){


        }
    });
    //渲染时间选择框
    laydate.render({
        elem: '#timeLimit',
        range: true,
        max: Feng.currentDate()
    });


    /**
     * 点击查询按钮
     */
    detailMainTable.search = function () {
        var queryData = {};
        queryData['timeLimit'] = $('#timeLimit').val();
        var value = $('select[name="xdm"]').next().find('.layui-this').attr('lay-value');
        if(value != undefined){
            queryData['xdm'] = value;
        }else{
            queryData['xdm'] = "";
        }
        table.reload(detailMainTable.tableId, {
            where: queryData, page: {curr: 1}
        });
    };


    /**
     * 导出按钮
     */
    detailMainTable.exportToExcel = function () {
        var limitTime = $('#timeLimit').val();
        var value = $('select[name="xdm"]').next().find('.layui-this').attr('lay-value');
        if(value == undefined){
           value = '';
        }
        var form=$("<form>");
        form.attr("style","display:none");
        form.attr("target","");
        form.attr("method","post");//提交方式为post
        form.attr("action",Feng.ctxPath + "/statistics/exportToBusExcel?xdm="+value+'&limitTime='+limitTime);//定义action
        $("body").append(form);
        form.submit();
    };
    // 导出按钮点击事件
    $('#btnOut').click(function () {
        detailMainTable.exportToExcel();
    });
    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        detailMainTable.search();
    });
});

