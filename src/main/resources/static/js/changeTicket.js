layui.use(['layer'], function(){
    var layer = layui.layer;

    // 绑定改签按钮的点击事件
    $('button[lay-event="change"]').on('click', function(){
        var ticketId = $(this).data('id');
        var price = $(this).data('price');
        var departureInput = $(this).data('departure');
        var destination = $(this).data('destination');
        var departureTime = $(this).data('departuretime');
        var now = new Date();
        var departure = new Date(departureTime);
        var timeDiff = departure.getTime() - now.getTime(); // 计算时间差（单位：毫秒）

        // 判断发车时间
        if (timeDiff < 24 * 60 * 60 * 1000) { // 离发车时间小于一天，不能改签
            layer.msg('距离发车时间小于24小时，不能改签！', {icon: 2});
            return;
        }

        // 弹出确认改签的提示框
        layer.confirm('原票价为：<b>' + price + '元</b><br>是否确定要改签？<br><br>(改签之后的车票只能退票，不能再次改签<br>票价多退少补)<br>', {
            btn: ['确定', '取消']
        }, function(){
            // 将数据填入表单中
            $('#change-form input[name="ticketId"]').val(ticketId);
            $('#change-form input[name="departure"]').val(departureInput);
            $('#change-form input[name="destination"]').val(destination);
            $('#change-form input[name="price"]').val(price);
            // 提交表单
            $('#change-form').submit();
        });
    });
});
