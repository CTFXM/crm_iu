layui.use(['table','layer'],function(){
    var layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        table = layui.table;



    /**
     * 加载数据表格
     */
    var  tableIns = table.render({
        elem: '#cusDevPlanList', // 表格绑定的ID
        url : ctx + '/cus_dev_plan/list?saleChanceId=' + $("[name='id']").val(), // 访问数据的地址
        cellMinWidth : 95,
        page : true, // 开启分⻚
        height : "full-125",
        limits : [10,15,20,25],
        limit : 10,
        toolbar: "#toolbarDemo",
        id : "cusDevPlanTable",
        cols : [[
            {type: "checkbox", fixed:"center"},
            {field: "id", title:'编号',fixed:"true"},
            {field: 'planItem', title: '计划项',align:"center"},
            {field: 'planDate', title: '计划时间', align:'center'},
            {field: 'exeAffect', title: '执行效果', align:'center'},
            {field: 'createDate', title: '创建时间', align:'center'},
            {field: 'updateDate', title: '修改时间', align:'center'},
            {title: '操作', templet:'#cusDevPlanListBar',fixed:"right",align:"center", minWidth:150}
        ]]
    });

    /**
     *  监听头部工具栏
     */
    table.on('toolbar(cusDevPlans)', function (data) {

        if (data.event == "add") {  // 添加计划项

            // 打开添加或修改计划项的页面
            openAddOrUpdateCusDevPlanDialog();

        } else if (data.event == "success") {   // 开发成功

            // 更新营销机会的开发状态
            updateSaleChanceDevResult(2);   // 开发成功

        } else if (data.event == "failed") {    // 开发失败

            // 更新营销机会的开发状态
            updateSaleChanceDevResult(3);   // 开发失败

        }

    });

    /**
     * 监听行工具栏
     */
    table.on('tool(cusDevPlans)', function (data) {
        if (data.event == "edit") { // 更新操作

            // 打开添加或修改计划项的页面
            openAddOrUpdateCusDevPlanDialog(data.data.id);

        } else if (data.event == "del") {   // 删除计划项
            // 删除计划项
            deleteCusDevPlan(data.data.id);
        }
    });

    /**
     * 打开添加或修改计划项的页面
     */
    function openAddOrUpdateCusDevPlanDialog(id) {
        var title= "计划项管理 - 添加计划项";
        var url = ctx + "/cus_dev_plan/toAddOrUpdateCusDevPlanPage?sId=" + $("[name='id']").val();

        // 判断计划项的ID是否为空（如果为空，则表示添加，不为空则表示更新操作）
        if (id != null && id != '') {
            // 更新计划项
            title = "计划项管理 - 更新计划项";
            url += "&id=" + id;
        }

        // iframe层
        layui.layer.open({
            // 类型
            type: 2,
            // 标题
            title: title,
            // 宽高
            area: ['500px', '300px'],
            // url地址
            content: url,
            // 可以最大化与最小化
            maxmin:true
        });
    }

    /**
     * 删除计划项
     * @param id
     */
    function deleteCusDevPlan(id){
        // 弹出确认框，询问用户是否确认删除
        layer.confirm("您确定要删除该记录吗？", {icon:3, title:'开发数据管理'}, function (index) {
            // 发送ajax请求，执行删除操作
            $.post(ctx + '/cus_dev_plan/delete', {id:id}, function (result) {
               // 判断删除结果
               if (result.code == 200) {
                   // 提示成功
                   layer.msg("删除成功！", {icon: 6});
                   // 刷新数据表格
                   tableIns.reload();
               } else {
                   // 提示失败原因
                   layer.msg(result.msg, {icon: 5});
               }
            });
        })
    }


    /**
     * 更新营销机会的开发状态
     * @param devResult
     */
    function updateSaleChanceDevResult(devResult) {
        // 弹出确认框，询问用户是否确认删除
        layer.confirm('您确定执行该操作吗？', {icon:3, title:"营销机会管理"}, function (index) {
            // 得到需要被更新的营销机会的ID （通过隐藏域获取）
            var sId = $("[name='id']").val();
            // 发送ajax请求，更新营销机会的开发状态
            $.post(ctx + '/sale_chance/updateSaleChanceDevResult', {id:sId,devResult:devResult}, function (result) {
                if (result.code == 200) {
                    layer.msg('更新成功！', {icon:6});
                    // 关闭窗口
                    layer.closeAll("iframe");
                    // 刷新父页面
                    parent.location.reload();
                } else {
                    layer.msg(result.msg, {icon:5});
                }
            });
        })

    }
});
