"use strict";
//# sourceURL=blogedit.js

// DOM 加载完再执行
$(function () {

    // 初始化 md 编辑器
    editormd("my-editormd", {//注意1：这里的就是上面的DIV的id属性值
        width   : "100%",
        height  : 700,
        syncScrolling : "single",
        path    : "../../lib/",//注意2：你的路径
        theme: "dark",//工具栏主题
        previewTheme: "dark",//预览主题
        editorTheme: "pastel-on-dark",//编辑主题
        saveHTMLToTextarea : true//注意3：这个配置，方便post提交表单
    });

    //$('.form-control-chosen').chosen();


    $("#uploadImage").click(function () {
        $.ajax({
            url: fileServerUrl,
            type: 'POST',
            cache: false,
            data: new FormData($('#uploadformid')[0]),
            processData: false,
            contentType: false,
            success: function (data) {
                var mdcontent = $("#md").val();
                $("#md").val(mdcontent + "\n![](" + data + ") \n");

            }
        }).done(function (res) {
            $('#file').val('');
        }).fail(function (res) {
        });
    })

    // 发布博客
    $("#submitBlog").click(function () {

        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: '/writer/' + $(this).attr("userName"),
            type: 'POST',
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify({
                "id": $('#blogId').val(),
                "title": $('#title').val(),
                "summary": $('#summary').val(),
                "content": $('.editormd-markdown-textarea').val(),
                "parsedContent": $('.editormd-html-textarea').val(),
                "classification": {"id": $('#catalogSelect').val()},
                "tags": $('#form-control-tags').val()
            }),
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
            },
            success: function (data) {
                if (data.success) {
                    // 成功后，重定向
                    window.location = data.body;
                } else {
                    toastr.error("error!" + data.message);
                }

            },
            error: function () {
                toastr.error("error!");
            }
        })
    })

    $('#form-control-tags').tagsInput({
        'defaultText': '输入标签',
        'height':'100px',
        'width':'210px',
    });
});