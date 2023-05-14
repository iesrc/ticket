
    layui.use([ 'layer'], function () {
        var layer = layui.layer;
        var $ = layui.jquery;

            $('#cancelOrderBtn').on('click', function () {
                layer.confirm('确定要取消订单吗？', function (index) {
                    window.history.back();
                    layer.close(index);
                });
            });

            // 点击确认支付按钮
            $('button[lay-filter="confirmsOrder"]').on('click', function() {
                var scheduleId = $(this).data('id');
                console.log("scheduleId的类型" + typeof scheduleId);
                var passengerIds = $(this).data('ids');
                console.log("passengerIds的类型" + typeof passengerIds);
                var totalPrice = $(this).data('totalprice');
                console.log("totalPrice的类型" + typeof totalPrice);
                console.log("scheduleId"+scheduleId);
                console.log("passengerIds"+passengerIds);
                console.log("totalPrice"+totalPrice);
                // 将 passengerIds 对象转换成 JSON 字符串
                var passengerIdsJson = JSON.stringify(passengerIds);
                console.log("passengerIds的 JSON 字符串的类型为：" +typeof passengerIdsJson);
                // 弹出确认支付提示框，并显示订单总金额
                layer.confirm('您的订单总金额为' + totalPrice + '元，请确认是否支付?', {icon: 3, title:'支付确认'}, function(index){
                // 模拟支付操作
                $.ajax({
                    type: 'POST',
                    url: '/ticket/order/pay',
                    data: {scheduleId: scheduleId, passengerIds: passengerIdsJson, totalPrice: totalPrice},
                    success: function(data){
                    // 更新订单状态
                        if (data == 'success') {
                            layer.msg('支付成功，感谢您的使用！', {icon: 1, time: 3000}, function(){
                                window.location.replace('http://localhost:8080/ticket/order/list');
                                //将刷新页面的语句从 window.location.href 改成了 window.location.replace，
                                // 这是因为 window.location.replace 可以在不产生历史记录的情况下跳转到新页面，
                                // 而 window.location.href 则会产生历史记录。在实际应用中，根据具体情况选择合适的方式是比较好的习惯。
                            });
                        }
                        else {
                    layer.msg('支付失败，请稍后再试', {icon: 2,time : 2000});
                }
                },
                    error: function(){
                    layer.msg('支付失败，请稍后再试', {icon: 2,time : 2000});
                }
            });
                layer.close(index);
            });
        });

});
