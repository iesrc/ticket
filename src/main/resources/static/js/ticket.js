// 在HTML页面上添加Layui的JavaScript代码
layui.use('laydate', function(){
    var laydate = layui.laydate;
    //执行一个laydate实例
    // 清空时间输入框的值
    // $('#departureTimeInput').val('');
    laydate.render({
        elem: '#departureTimeInput', //绑定的元素
        type: 'date' ,// 设置时间格式为：年月日
        // min: 0,// 设置可选的最小日期时间，这里是最小值为当前时间（0代表当前时间）。
        trigger: 'click',// 设置弹出时间选择器的事件为点击
    });
});