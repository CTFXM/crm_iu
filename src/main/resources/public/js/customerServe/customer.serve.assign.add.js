layui.use(['form', 'layer'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;

    /**
     * 监听submit事件
     * 实现流失客户的添加与更新
     */
    form.on("submit(addOrUpdateCustomerServe)", function (data) {
        var index = layer.msg("数据提交中，请稍后...", {
            icon:16, // 图标
            time:false, // 不关闭
            shade:0.8 // 设置遮罩的透明度
        });
        // 请求的地址
        var url = ctx + "/customer_serve/update";    // 服务分配操作

        // 发送ajax请求
        $.post(url, data.field, function (result) {
            // 操作成功
            if (result.code == 200) {
                // 提示成功
                top.layer.msg("操作成功！", {icon: 6});
                // 关闭加载层
                top.layer.close(index)
                // 关闭弹出层
                layer.closeAll("iframe");
                // 刷新父页面，重新渲染表格数据
                parent.location.reload();
            } else {
                layer.msg(result.msg, {icon: 5});
            }
        });
        return false; // 阻止提交
    });


    /**
     * 关闭弹出层
     */
    $("#closeBtn").click(function () {
        // 先得到当前iframe层的索引
        var index = parent.layer.getFrameIndex(window.name);
        // 在执行关闭
        parent.layer.close(index);
    });

    /**
     * 加载指派人的下拉框
     */
    $.ajax({
        type:"get",
        url :ctx + "/user/queryAllCustomerManagers",
        data:{},
        success:function (data) {
            // console.log(data);
            // 判断返回的数据是否为空
            if (data != null) {

                // 遍历返回的数据
                for (var i = 0; i<data.length; i++) {
                    var opt = "<option value='" + data[i].id + "'>"+data[i].uname + "</option>";
                    // 将下拉项设置到下拉框中
                    $("#assigner").append(opt);
                }
            }
            // 重新渲染下拉框的内容
            layui.form.render("select");
        }
    });


});