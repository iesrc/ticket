layui.use(['layer'], function(){
    var layer = layui.layer;

    // 绑定退票按钮的点击事件
    $('button[lay-event="refund"]').on('click', function(){
        var ticketId = $(this).data('id');
        var price = $(this).data('price');
        var departureTime = $(this).data('departure');
        var now = new Date();
        var departure = new Date(departureTime);
        var timeDiff = departure.getTime() - now.getTime(); // 计算时间差（单位：毫秒）

        if (timeDiff < 0) {
            layer.msg('已过退票时间');
            return;
        }

        var refundFee = 0;
        if (timeDiff < 30 * 60 * 1000) { // 离发车时间小于30分钟，手续费率50%
            refundFee = price * 0.5;
        } else if (timeDiff < 24 * 60 * 60 * 1000) { // 离发车时间小于一天，手续费率20%
            refundFee = price * 0.2;
        } else if (timeDiff < 3 * 24 * 60 * 60 * 1000) { // 离发车时间小于三天，手续费率10%
            refundFee = price * 0.1;
        }

        var refundAmount = price - refundFee;
        //refundFee.toFixed(2) 是将 refundFee 变量保留两位小数后的字符串表示形式。它的作用是将 refundFee
        // 四舍五入到小数点后两位，并转化为字符串，方便拼接到确认退票的提示信息中。在这里，它的作用是告诉用户本次退票的手续费金额。
        layer.confirm('原票价为：<b>' + price + '元</b><br>本次退票手续费为：<b>' + refundFee.toFixed(2) + '元</b><br>您将获得：<b>' +
            refundAmount.toFixed(2) + '元</b> 的退票金额。<br>是否确定要退票？<br><br>（离发车时间小于30分钟，手续费率50%；<br>离发车时间小于一天，手续费率20%；<br>离发车时间小于三天，手续费率10%；<br>大于三天不收取手续费。）', {
            btn: ['确定', '取消']
        }, function(){
            $.ajax({
                url: '/ticket/ticket/refundTicket',
                type: 'post',
                data: {
                    ticketId: ticketId
                },
                success: function(response) {
                    if (response.success) {
                        layer.msg(response.msg, {icon: 1}, function(){
                            window.location.reload();
                        });
                    } else {
                        layer.msg(response.msg, {icon: 2});
                        console.log("退票失败");
                    }
                },
                error: function() {
                    layer.msg('退票失败', {icon: 2});
                }
            });

        });
    });
});