/*!
 * blog.html 页面脚本.
 * 
 * @since: 1.0.0 2017-03-26
 * @author Way Lau <https://waylau.com>
 */
"use strict";
//# sourceURL=blog.js

// DOM 加载完再执行
$(function () {

    editormd.markdownToHTML("content-editormd", {
        //htmlDecode      : "style,script,iframe",  // you can filter tags decode
        emoji           : true,
        taskList        : true,
        tex             : true,  // 默认不解析
        flowChart       : true,  // 默认不解析
        sequenceDiagram : true,  // 默认不解析
        codeFold: true
    });

    //$.catalog("#catalog-list", "#htmlcontent-editormd");

    // new katelog({
    //     contentEl: 'htmlcontent-editormd',
    //     catelogEl: 'catalog-list',
    //     linkClass: 'k-catelog-link',
    //     linkActiveClass: 'k-catelog-link-active',
    //     supplyTop: 20,
    //     //selector: ['h2', 'h3'],
    //     active: function (el) {
    //         console.log(el);
    //     }
    // });

    // 处理删除博客事件
    $(".blog-content-container").on("click", ".delete-blog-action", function () {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");


        $.ajax({
            url: '/blog/'+blogUsername+'/'+blogId,
            type: 'DELETE',
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
            },
            success: function (data) {
                if (data.success) {
                    // 成功后，重定向
                    window.location = data.body;
                } else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                toastr.error("error!");
            }
        });
    });


    // 获取评论列表
    function getComment(blogId) {
        $.ajax({
            url: '/comments',
            type: 'GET',
            data: {"blogId": blogId},
            success: function (data) {
                $("#commentContainer").html(data);
            },
            error: function () {
                toastr.error("error!");
            }
        });
    }

    // $("#comment-text-area").focus(function () {
    //     $(".write-function-block").css("display","block");
    // });

    $(".blog-content-container").on('focus','#comment-text-area',function(){
        $(".write-function-block").css("display","block");  //获取焦点时，背景颜色变白色
    });

    // 提交评论
    $(".blog-content-container").on("click", "#submitComment", function () {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: '/comments',
            type: 'POST',
            data: {"blogId": blogId, "commentContent": $('#comment-text-area').val()},
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
            },
            success: function (data) {
                if (data.success) {
                    // 清空评论框
                    $('#comment-text-area').val('');
                    // 获取评论列表
                    getComment(blogId);
                } else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                toastr.error("error!");
            }
        });
    });

    // 删除评论
    $(".blog-content-container").on("click", ".blog-delete-comment", function () {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: '/comments/' + $(this).attr("commentId") + '?blogId=' + blogId,
            type: 'DELETE',
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
            },
            success: function (data) {
                if (data.success) {
                    // 获取评论列表
                    getComment(blogId);
                } else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                toastr.error("error!");
            }
        });
    });

    // 初始化 博客评论
    getComment(blogId);



    // 提交点赞
    $(".blog-content-container").on("click", "#submitVote", function () {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: '/votes',
            type: 'POST',
            data: {"blogId": blogId},
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
            },
            success: function (data) {
                if (data.success) {
                    toastr.info(data.message);
                    // 成功后，重定向
                    window.location = blogUrl + '#upvote-pos';
                } else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                toastr.error("error!");
            }
        });
    });

    // 取消点赞
    $(".blog-content-container").on("click", "#cancelVote", function () {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: '/votes/' + $(this).attr('upVoteId') + '?blogId=' + blogId,
            type: 'DELETE',
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
            },
            success: function (data) {
                if (data.success) {
                    toastr.info(data.message);
                    // 成功后，重定向
                    window.location = blogUrl + '#upvote-pos';
                } else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                toastr.error("error!");
            }
        });
    });

    //模糊查询,回车查询
    $("#the-top-search-input").keypress(function(e){
        if(e.which == 13){
            var thehref = '../../?keyword=' + $("#the-top-search-input").val();
            window.open(thehref, '_blank');
        }
    });

    //模糊查询,按键查询
    $(".the-top-nav-container").on("click", "#top-search-button", function(){
        /**
         * 提交
         * TODO
         */
        var thehref = '../../?keyword=' + $("#the-top-search-input").val();
        window.open(thehref, '_blank');
    });

});

// $(document).ready(function(){
//     $("#comment-text-area").focus(function () {
//         $(".write-function-block").css("display","block");
//     });
// });