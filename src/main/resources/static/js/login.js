"use strict";

$(function () {
    $("#loginButton").click(function () {
        var username = $("#inputUsername").val();
        var password = $("#inputPassword").val();
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");
        $.ajax({
            type: 'POST',//方法类型
            url: '/login' ,//url
            data: "username="+username+"&password="+password,
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
            },
            success: function (data) {
                //console.log(result);//打印服务端返回的数据(调试用)
                if (data.success) {
                    toastr.error("SUCCESS");
                }else {
                    toastr.error("error!" + data.message);
                }

            },
            error : function() {
                alert("异常！");
            }
        });
    });
});