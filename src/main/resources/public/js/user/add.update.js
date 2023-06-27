layui.use(['form', 'layer', 'formSelects'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery;
    var formSelects = layui.formSelects;

    /**
     * 监听submit事件
     * 实现用户的添加与更新
     */
    form.on("submit(addOrUpdateUser)", function (data) {
        var index = layer.msg("数据提交中，请稍后...", {
            icon:16, // 图标
            time:false, // 不关闭
            shade:0.8 // 设置遮罩的透明度
        });
        // 请求的地址
        var url = ctx + "/user/add";    // 添加操作

        // 判断用户ID是否为空，如果不为空则为更新操作
        if ($("[name='id']").val()) {
            // 更新操作
            var url = ctx + "/user/update"
        }

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
     * 加载角色下拉框
     *
     * 配置远程搜索，请求头，请求参数，请求类型等
     *
     * formSelects.config(ID, Options, isJson);
     *
     */
    var userId = $("[name='id']").val();
    formSelects.config("selectId", {
        type:"post", // 请求方式
        searchUrl:ctx + "/role/queryAllRoles?userId=" + userId, // 请求地址
        keyName: 'roleName', // 下拉框中的文本内容，要与返回的数据中对应key一致
        keyVal: 'id'
    }, true);


});