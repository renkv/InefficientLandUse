layui.config({
}).extend({
    tableMerge: 'tableMerge'
});
layui.use(['table', 'admin','tableMerge',  'laydate','ax', 'func','upload', 'util'], function () {
    var $ = layui.$;
    var table = layui.table;
    var $ax = layui.ax;
    var admin = layui.admin;
    var func = layui.func;
    var laydate = layui.laydate;
    var upload = layui.upload;
    var util = layui.util;
    var tableMerge = layui.tableMerge;

    /**
     * 文件政策管理
     */
    var busMainTable = {
        tableId: "busMainTable"
    };

    /**
     * 初始化表格的列
     */
    busMainTable.initColumn = function () {
        return [[
            {fixed: 'left',type: 'checkbox'},
            {field: 'id', hide: true,align: 'center',fixed: 'left', title: 'ID'},
            {field: 'landCode', hide:true,align: 'center',fixed: 'left', title: '地块编码'},
            {field: 'planCode', hide:true,align: 'center',fixed: 'left', title: '计划编码'},
            {field: 'countyName',width:150,align: 'center',merge:true,fixed: 'left', title: '区县'},
            /*{field: 'xmmc',width:150,align: 'center',merge:true,fixed: 'left', title: '地块项目名称'},*/
            {field: 'planName',hide:true, sort: false, align: 'center',fixed: 'left',title: '计划名称'},
            {field: 'disYear', sort: false, align: 'center',fixed: 'left',title: '年份'},
            {field: 'busName', sort: false, align: 'center',fixed: 'left',title: '企业名称',templet:function (d){
                    var html = '<div><a rel="nofollow"  style="color:#1E9FFF" href="javascript:void(0);" lay-event="showDetail">' + d.busName+ '</a></div>';
                    return html;
                }
                },
            {field: 'zoneName', sort: false, align: 'center',title: '开发区名称',edit: 'text'},
            {field: 'creditCode',width:150, sort: false, align: 'center',title: '社会统一信用代码',edit: 'text'},
            {field: 'occupyArea', sort: false, align: 'center',title: '占地面积',edit: 'text'},
            {field: 'located', sort: false, align: 'center',title: '坐落',edit: 'text'},
            {field: 'preYearTax',width:120, sort: false, align: 'center',title: '上年度亩均税收',edit: 'text'},
            {field: 'upYearTax',width:150, sort: false, align: 'center',title: '上上年度亩均税收',edit: 'text'},
            /*{field: 'isPlanBus', width:120,sort: false, align: 'center',title: '是否规上企业'},*/
            {field: 'busStatus', sort: false, align: 'center',title: '经营状态',edit: 'text'},
            {field: 'useStatus', sort: false, align: 'center',title: '现状用途',edit: 'text'},
            /*{field: 'disType', sort: false, align: 'center',title: '处置类型'},*/
            {field: 'conStandard', sort: false, align: 'center',title: '低效类型',edit: 'text'},
            {field: 'disStandard', sort: false, align: 'center',title: '处置方式',edit: 'text'},
            {field: 'curStatus', sort: false, align: 'center',title: '当前状态',edit: 'text'},
            {field: 'curProgress', sort: false, align: 'center',title: '详细进展',edit: 'text'},
            /*{field: 'planStartTime',width:120, sort: false, align: 'center',title: '计划开始时间'},
            {field: 'planEndTime', sort: false,hide:true, align: 'center',title: '计划完成时间'},
            {field: 'actStartTime', sort: false,hide:true, align: 'center',title: '实际开始时间'},
            {field: 'actEndTime', sort: false,hide:true, align: 'center',title: '实际完成时间'},
            {field: 'planArea',width:120, sort: false, align: 'center',title: '计划完成面积'},*/
            {field: 'currentArea', width:120,sort: false, align: 'center',title: '处置面积',edit: 'text'},
            {field: 'remArea', sort: false, align: 'center',title: '剩余面积'},
            {field: 'planUnit',width:120, sort: false, align: 'center',title: '计划实施单位',edit: 'text'},
            /*{field: 'actUnit', sort: false,hide:true, align: 'center',title: '实际实施单位'},*/
            {field: 'reasonsType', sort: false, align: 'center',title: '问题类型'},
            {field: 'reasons', sort: false, align: 'center',title: '问题详细描述',edit: 'text'},
            {field: 'remark', sort: false, align: 'center',title: '备注',edit: 'text'}
        ]];
    };


    // 渲染表格
    var tableResult = table.render({
        elem: '#' + busMainTable.tableId,
        url: Feng.ctxPath + '/plan/selectList?planType=1',
        page: true,
        height: "full-158",
        cellMinWidth: 100,
        cols: busMainTable.initColumn(),
        done:function(res, curr, count){
            tableMerge.render(this);
            $("[data-field = 'reasonsType']").children().each(function(){
                if($(this).text() == '1'){
                    $(this).text("困难原因1");
                }else if($(this).text() == '2'){
                    $(this).text("困难原因2");
                }else if($(this).text() == '3'){
                    $(this).text("困难原因3");
                }else if($(this).text() == '4'){
                    $(this).text("困难原因4");
                }else if($(this).text() == '5'){
                    $(this).text("其他");
                }
            });
        }
    });
    //渲染时间选择框
    laydate.render({
        elem: '#timeLimit',
        range: true,
        max: Feng.currentDate()
    });
    //监听单元格编辑
    table.on('edit(busMainTable)', function(obj){
        var value = obj.value //得到修改后的值
            ,data = obj.data //得到所在行所有键值
            ,field = obj.field; //得到字段
        if(value.replace(/\s/g,'' ) === ''){
            layer.msg('修改值不能为空！');
            return obj.reedit();
        }
        if(field == "occupyArea" || field == "currentArea"){
            value = numLimit(value,4);
            obj.value = value;
            if(field == "occupyArea"){
                var currentArea = obj.currentArea;
                if(parseFloat(value) < parseFloat(currentArea)){
                    layer.msg('占地面积小于处置面积，请重新修改！');
                    return obj.reedit();
                }
            }else{
                var occupyArea = obj.occupyArea;
                if(parseFloat(value) < parseFloat(occupyArea)){
                    layer.msg('占地面积小于处置面积，请重新修改！');
                    return obj.reedit();
                }
            }
        }
        if(field == "preYearTax" || field == "upYearTax"){
            value = numLimit(value,6);
            obj.value = value;
        }
        // 编辑后续操作，如提交更新请求，以完成真实的数据更新
        var index = top.layer.msg('正在将新数据写入数据库，请稍候...', {icon: 16, time: false, shade: 0.8});
        var msg;
        setTimeout(function () {
            $.ajax({
                type: "POST",
                url: Feng.ctxPath + "/plan/savePlanById",
                data: {
                    id: data.id, // 获取当前修改数据的id
                    field: field,// 得到修改的字段
                    value: value,// 得到修改后的值
                },
                success: function (d) {
                    if (d.code == 200) {
                        msg = d.message;
                    } else {
                        msg = d.message;
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    layer.msg("获取数据失败！");
                }
            }).done(function () {
                top.layer.close(index);
                top.layer.msg(msg);
                layer.closeAll("iframe");
                setTimeout(function () {
                    parent.location.reload();
                }, 1000);
            });
        }, 2000);
        //layer.msg('[ID: '+ data.id +'] ' + field + ' 字段更改值为：'+ util.escape(value));
    });

    /**
     * 点击查询按钮
     */
    busMainTable.search = function () {
        var queryData = {};

        /*queryData['policyType'] = $('select[name="policyType"]').next().find('.layui-this').attr('lay-value');*/
        queryData['xmmc'] = $('#xmmc').val();
        queryData['planName'] = $('#planName').val();
        queryData['busName'] = $('#busName').val();
        queryData['timeLimit'] = $('#timeLimit').val();

        table.reload(busMainTable.tableId, {
            where: queryData, page: {curr: 1}
        });
    };

    table.on('tool(busMainTable)', function (obj) {
        var data = obj.data;//获取监听点击当前行的所有信息[object，object]
        var layEvent = obj.event;
        if (layEvent === 'showDetail') {
            func.open({
                width:  "1800px",
                height: 1500,
                title: '低效企业详情',
                content: Feng.ctxPath + '/plan/detail?id=' + data.id,
                tableId: busMainTable.tableId,
                endCallback: function () {
                    //table.reload(busMainTable.tableId);
                }
            });
        }else if(layEvent === 'showPre'){
            window.open(Feng.ctxPath+"/policy/showPre?fileId="+data.fileId);
        }
    });

    /**
     * 弹出添加
     */
    busMainTable.openAddPage = function () {
        func.open({
            width:  "1800px",
            height: 1500,
            title: '新增低效企业信息',
            content: Feng.ctxPath + '/plan/add?planType=1',
            tableId: busMainTable.tableId,
            endCallback: function () {
                table.reload(busMainTable.tableId);
            }
        });
    };

    /**
     * 跳转到编辑页面
     *
     * @param data 点击按钮时候的行数据
     */
    busMainTable.jumpEditPage = function (data) {
        func.open({
            width:  "1800px",
            height: 1500,
            title: '编辑',
            content: Feng.ctxPath + '/plan/edit?id=' + data.id,
            tableId: busMainTable.tableId,
            endCallback: function () {
                //table.reload(busMainTable.tableId);
            }
        });
    };
    //btnEdt 编辑按钮点击事件
    $('#btnEdt').click(function () {
        var checkStatus = table.checkStatus(busMainTable.tableId);
        var ids = [];
        $(checkStatus.data).each(function (i, o) {//o即为表格中一行的数据
            ids.push(o.id);
        });
        if (ids.length < 1) {
            layer.msg('请选择一条数据');
            return false;
        }
        busMainTable.jumpEditPage(checkStatus.data[0]);
    });
    /**
     * 跳转到详情页面
     *
     * @param data 点击按钮时候的行数据
     */
    busMainTable.jumpDetailPage = function (data) {
        func.open({
            width:  "1800px",
            height: 1500,
            title: '低效企业详情',
            content: Feng.ctxPath + '/plan/detail?id=' + data.id,
            tableId: busMainTable.tableId,
            endCallback: function () {
                //table.reload(busMainTable.tableId);
            }
        });
    };
    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        busMainTable.search();
    });

    // 添加按钮点击事件
    $('#btnAdd').click(function () {
        busMainTable.openAddPage();
    });
    //删除按钮点击事件
    $('#btnDel').click(function () {
        var checkStatus = table.checkStatus(busMainTable.tableId);
        var ids = [];
        $(checkStatus.data).each(function (i, o) {//o即为表格中一行的数据
            ids.push(o.id);
        });
        if (ids.length < 1) {
            layer.msg('请选择一条数据');
            return false;
        }
        ids = ids.join(",");
        layer.confirm('您确定要删除该数据吗？', function (index) {
            $.ajax({
                url: Feng.ctxPath + '/plan/delete',
                type: "POST",
                dataType:"json",
                data:{ids:ids},
                success: function (res) {
                    if(res.code == "500"){
                        Feng.error(res.message);
                    }else{
                        Feng.success("删除成功！");
                        table.reload(busMainTable.tableId);
                    }
                },
                error: function (err) {
                    console.log('err', err);
                }
            });
            layer.close(index);
        });
    });

    //执行实例
    var uploadInst = upload.render({
        elem: '#btnImp'
        , url: '/plan/uploadExcel?planType=1'
        ,accept: 'file'
        , done: function (res) {
            if (res.code==500){
                Feng.error("导入失败!" + res.message);
            }else{
                Feng.success("导入成功！");
                $('#btnSearch').click()
            }
        }
        , error: function () {
            Feng.error("导入失败!");
            //请求异常回调
        }
    });

});

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
