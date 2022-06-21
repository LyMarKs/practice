/*判断是否存储了焦点信息*/
$(function () {
    /*监听页面显示*/
    if(performance.navigation.type === 0) {
        //删除焦点源
        localStorage.removeItem('this');
    }
    //获取焦点的id值
    let id = localStorage.getItem('this');
    //判断id值
    if (id === 'undefined' || id === null) {
        //设置默认焦点
        $('#seeNews').addClass('layui-this');
    } else {
        $('#seeNews').removeClass('layui-this');
        //设置焦点
        $('#'+id).addClass('layui-this');
    }
});

/*/!*判断是否存储了管理员数据*!/
$(function () {
    //获取管理员数据
    let adminInfo = JSON.parse(localStorage.getItem('adminData'));
    //判断数据
    if (adminInfo === null) {
        location.href = '/news/Login.html';
        return;
    }
    //设置界面内容
    $('#adminName').html(adminInfo.name);
    //根据管理员身份修改界面
    switch (parseInt(adminInfo.identity)) {
        //管理员
        case 1:
            $('#classify').remove();
            $('#admin').remove();
            $('#audit').remove();
            break;
        //超级管理员
        case 2:
            $('#addNews').remove();
            $('#myNews').remove();
            break;
    }
});
/!*换个账号点击事件*!/
function cutAccount() {
    //删除本地数据
    localStorage.removeItem('adminData');
}*/

/*监听页面刷新*/
window.addEventListener("beforeunload", function () {
    //存储当前页面的焦点的id值
    localStorage.setItem("this", $('.layui-this').attr("id"));
    //刷新页面，保留页面
    location.reload();
});
