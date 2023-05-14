function deletePassenger(passengerId) {
    layer.confirm('确定要删除吗？', {
        btn: ['确认', '取消'] //可以无限个按钮
        ,btn3: function(index, layero){
            layer.close(index);
        }
    }, function(index, layero){
        layer.close(index);
        $.ajax({
            url:'http://localhost:8080/ticket/passenger/deletePassengerById',
            type:'post',
            data:{"passengerId":passengerId},
            success:function (result){
                if(result == "SUCCESS"){
                    layer.alert("删除成功",{icon:1});
                    setTimeout(function (){
                        $("#myForm").submit();
                    },1500)
                }else {
                    layer.alert("删除失败", {icon: 2});
                }
            }
        })
        return false;
    });
}



