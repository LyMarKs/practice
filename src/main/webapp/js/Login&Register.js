/*登录/注册的点击事件*/
$(".box .btn input[type='submit']").click(function () {
    //获取用户名和密码
    let username = $("#username").val().trim();
    let password = $("#password").val().trim();
    //判断用户名、密码非空
    if(username === "") {
        layer.msg('请输入用户名', {time:1000});
    } else if(password === "") {
        layer.msg('请输入密码', {time:1000});
    } else {
        //使用ajax请求Servlet，判断用户是否存在
        $.post({
            url: "admin/login",
            data: {
                'username':username,
                'password':password
            },
            success: function (isExist) {
                // 调用登录/注册的检验方法
                exam(username, password, isExist);
            }
        });
    }
});
