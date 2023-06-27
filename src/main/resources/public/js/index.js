layui.use(['form','jquery','jquery_cookie'], function () {
    var form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        $ = layui.jquery_cookie($);

    /**
     * 表单submit提交
     *
     */
    form.on('submit(login)', function(data){
        /*console.log(data.elem) //被执行事件的元素DOM对象，一般为button对象
        console.log(data.form) //被执行提交的form对象，一般在存在form标签时才会返回*/
        // 获取表单元素的值 （用户名 + 密码）
        console.log(data);
        console.log(data.field) //当前容器的全部表单字段，名值对形式：{name: value}
        console.log(data.field.username) //当前容器的全部表单字段，名值对形式：{name: value}
        console.log(data.field.password) //当前容器的全部表单字段，名值对形式：{name: value}
        var fieldDate = data.field;

        // 判断参数是否为空
        if (fieldDate.username == "undefined" || fieldDate.username.trim() == "") {
            layer.msg("用户名称不能为空！！");
            return false;
        }
        if (fieldDate.password == "undefined" || fieldDate.password.trim() == "") {
            layer.msg("⽤户密码不能为空！");
            return false;
        }
        // 发送 ajax 请求，请求⽤户登录
        $.ajax({
            type:"post",
            url : ctx + "/user/login",
            data: {
                userName:fieldDate.username,
                userPwd :fieldDate.password
            },
            dataType:"json",
            success:function (data) {
                console.log(data);
                // 判断是否登录成功
                if (data.code == 200) {
                    layer.msg("登录成功！", function () {
                        // 将⽤户信息存到cookie中
                        var result = data.result;

                        // 如果⽤户选择"记住我"，则设置cookie的有效期为7天
                        if($("input[type='checkbox']").is(":checked")){
                            $.cookie("userIdStr", result.userIdStr, { expires: 7 });
                            $.cookie("userName", result.userName, { expires: 7 });
                            $.cookie("trueName", result.trueName, { expires: 7 });
                        } else {

                            $.cookie("userIdStr", result.userIdStr);
                            $.cookie("userName", result.userName);
                            $.cookie("trueName", result.trueName);
                        }

                        // 登录成功后，跳转到⾸⻚
                        window.location.href = ctx + "/main";
                    });
                } else {
                    layer.msg(data.msg);
                }
            }
        });


        return false; //阻止表单跳转。如果需要表单跳转，去掉这段即可。
    });
});