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
    var flag = 1;

    //年份
    var yearList = JSON.parse($('#yearList').val());
    var yearValue = $("#year").val();

    var demo1 = xmSelect.render({
        el: '#yearDiv',
        language: 'zn',
        /*initValue:[yearValue],*/
        tips:"请选择年份",
        data: yearList
    });



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
            {field: 'xmc',width:'14%', sort: false,align: 'center', title: '县名称'},
            {field: 'year',width:'14%', sort: false,align: 'center', title: '年份'},
            {field: 'totalArea',width:'24%', sort: false,align: 'center', title: '上报面积(亩)'},
            {field: 'finishArea', width:'24%',sort: false,align: 'center', title: '完成面积（亩）'},
            {field: 'comratio', width:'24%',sort: false,align: 'center',title: '完成比例(%)'},
        ]];
    };

    var selectArr1 = demo1.getValue();
    var year1 = "";
    if(selectArr1.length > 0){
        year1 = JSON.stringify(selectArr1, null, 2);
        year1 = encodeURIComponent(year1);
    }

    // 渲染表格
    var tableResult = table.render({
        id: detailMainTable.tableId,
        elem: '#' + detailMainTable.tableId,
        url: Feng.ctxPath + '/statistics/inbusList?year='+year1,
        page: false,
        height: "full-100",
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
        if(flag == 1){
            detailMainTable.searchMu();
        }else{
            detailMainTable.searchGq();
        }
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
        var selectArr = demo1.getValue();
        var year = "";
        if(selectArr.length > 0){
           year = JSON.stringify(selectArr, null, 2);
           year = encodeURIComponent(year);
        }
        var form=$("<form>");
        form.attr("style","display:none");
        form.attr("target","");
        form.attr("method","post");//提交方式为post
        form.attr("action",Feng.ctxPath + "/statistics/exportToBusExcel?xdm="+value+'&limitTime='+limitTime+'&year='+year+'&flag='+flag);//定义action
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
    //亩查询
    detailMainTable.searchMu  = function () {
        var limitTime = $('#timeLimit').val();
        var value = $('select[name="xdm"]').next().find('.layui-this').attr('lay-value');
        if(value == undefined){
            value = '';
        }
        var selectArr = demo1.getValue();
        var year = "";
        if(selectArr.length > 0){
            year = JSON.stringify(selectArr, null, 2);
            year = encodeURIComponent(year);
        }
        tableResult = table.render({
            id: detailMainTable.tableId,
            elem: '#' + detailMainTable.tableId,
            url: Feng.ctxPath + "/statistics/inbusList?xdm="+value+'&limitTime='+limitTime+'&year='+year+'&flag='+flag,
            page: false,
            height: "full-158",
            cellMinWidth: 100,
            cols: detailMainTable.initColumn(),
            done:function (){
            }
        });
    }
    //公顷查询
    detailMainTable.searchGq  = function () {
        var limitTime = $('#timeLimit').val();
        var value = $('select[name="xdm"]').next().find('.layui-this').attr('lay-value');
        if(value == undefined){
            value = '';
        }
        var selectArr = demo1.getValue();
        var year = "";
        if(selectArr.length > 0){
            year = JSON.stringify(selectArr, null, 2);
            year = encodeURIComponent(year);
        }
        tableResult = table.render({
            id: detailMainTable.tableId,
            elem: '#' + detailMainTable.tableId,
            url: Feng.ctxPath + "/statistics/inbusList?xdm="+value+'&limitTime='+limitTime+'&year='+year+'&flag='+flag,
            page: false,
            height: "full-158",
            cellMinWidth: 100,
            cols:[[
                {field: 'xmc',width:'14%', sort: false,align: 'center', title: '县名称'},
                {field: 'year',width:'14%', sort: false,align: 'center', title: '年份'},
                {field: 'totalArea',width:'24%', sort: false,align: 'center', title: '上报面积(公顷)'},
                {field: 'finishArea', width:'24%',sort: false,align: 'center', title: '完成面积（公顷）'},
                {field: 'comratio', width:'24%',sort: false,align: 'center',title: '完成比例(%)'},
            ]],
            done:function (){
            }
        });
    }
    // 按钮亩点击事件
    $('#btnMu').click(function () {
        flag = 1;
        $("#btnMu").removeClass("layui-btn-primary");
        $("#btnMu").addClass("layui-btn-normal");
        $("#btnGq").removeClass("layui-btn-normal");
        $("#btnGq").addClass( "layui-btn-primary");
        detailMainTable.searchMu();
    });
    // 按钮公顷点击事件
    $('#btnGq').click(function () {
        $("#btnGq").removeClass("layui-btn-primary");
        $("#btnGq").addClass("layui-btn-normal");
        $("#btnMu").removeClass("layui-btn-normal");
        $("#btnMu").addClass( "layui-btn-primary");
        flag = 2;
        detailMainTable.searchGq();
    });
});

