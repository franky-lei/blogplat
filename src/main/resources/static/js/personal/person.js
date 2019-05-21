"use strict";
//# sourceURL=blog.js

// DOM 加载完再执行
$(function () {
    //加载文章分类目录
    function getClassification(username) {
        $.ajax({
            url: '/p/classification',
            type: 'GET',
            data: {"username": username},
            success: function (data) {
                $("#classifications-container").html(data);
            },
            error: function () {
                toastr.error("error!");
            }
        });
    }

    function initalPerson(){
        $(".trigger-menu li.the-latest").addClass("active");
        querymethod = 1; //方法设为最新查询
    }

    $('#the-pagination').jqPaginator({
        totalPages: 10,
        visiblePages: 5,
        currentPage: 1,
        first: '<li class="first"><a href="javascript:void(0);"><i class="fa fa-angle-double-left" style="font-size:24px"></i></a></li>',
        prev: '<li class="prev"><a href="javascript:void(0);"><i class="fa fa-angle-left" style="font-size:24px"></i></a></li>',
        next: '<li class="next"><a href="javascript:void(0);"><i class="fa fa-angle-right" style="font-size:24px"></i></a></li>',
        last: '<li class="last"><a href="javascript:void(0);"><i class="fa fa-angle-double-right" style="font-size:24px"></i></a></li>',
        page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',
        onPageChange: function (num) {
            $('#paginator-text').html('当前第' + num + '页');
            num = num-1;
            if(querymethod == 1){
                /*
                 *最新查询、分页式
                 */
                $.ajax({
                    url: '/p/' + userId,
                    type: 'GET',
                    contentType: 'application/json',
                    data: {
                        "async": true,
                        "pageIndex": num,
                        "pageSize": pageSize
                    },
                    success: function (data) {
                        $("#list-container").html(data);
                    },
                    error: function () {
                        toastr.error("error!");
                    }
                });


            }else if(querymethod == 2){
                /**
                 * 最热查询、分页式
                 */
                $.ajax({
                    url: '/p/' + userId,
                    type: 'GET',
                    contentType: 'application/json',
                    data: {
                        "async": true,
                        "order": "popular",
                        "pageIndex": num,
                        "pageSize": pageSize
                    },
                    success: function (data) {
                        $("#list-container").html(data);
                    },
                    error: function () {
                        toastr.error("error!");
                    }
                });

            }else if(querymethod == 3){
                /**
                 * 关键词查询、分页式
                 */
                $.ajax({
                    url: '/p/' + userId,
                    type: 'GET',
                    contentType: 'application/json',
                    data: {
                        "keyword": $(".search-input").val(),
                        "async": true,
                        "pageIndex": num,
                        "pageSize": pageSize
                    },
                    success: function (data) {
                        $("#list-container").html(data);
                    },
                    error: function () {
                        toastr.error("error!");
                    }
                });

            }
        }
    });

    //点击目录编辑，显示目录编辑框
    $(".person-content-container").on("click", ".function-btn", function(){
        $.ajax({
            url: '/p/classification/edit',
            type: 'GET',
            success: function (data) {
                $("#form-container").html(data);
                $(".catalog-edit").css("display", "block");
            },
            error: function () {
                toastr.error("error!");
            }
        });

    });

    //取消编辑
    $(".person-content-container").on("click", ".cancel-edit-intro", function(){
        // 清空编辑框
        $('#edited-classification-content').val('');
        $(".catalog-edit").css("display", "none");
    });

    //提交已编辑好的目录名
    $(".person-content-container").on("click", ".save-edit-classification", function(){
        /**
         * 提交
         * TODO
         */
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");

        $.ajax({
            url: '/p/classification',
            type: 'POST',
            contentType: "application/json; charset=utf-8",
            data: JSON.stringify({
                "username": username,
                "classification": {"name": $('#edited-classification-content').val()}
            }),
            beforeSend: function (request) {
                request.setRequestHeader(csrfHeader, csrfToken); // 添加  CSRF Token
            },
            success: function (data) {
                if (data.success) {
                    toastr.info(data.message + "+1");
                    // 成功后，刷新列表
                    // 清空编辑框
                    $('#edited-classification-content').val('');
                    getClassification(username);
                } else {
                    toastr.error(data.message);
                }
            },
            error: function () {
                toastr.error("error!");
            }
        });


        $('#edited-classification-content').val('');
        $(".catalog-edit").css("display", "none");
    });

    //最新查询
    $(".person-content-container").on("click", ".the-latest", function(){
        /**
         * 提交
         * TODO
         */
        querymethod = 1;
        pageIndex = 0;
        $.ajax({
            url: '/p/' + userId,
            type: 'GET',
            contentType: 'application/json',
            data: {
                "async": true,
                "pageIndex": pageIndex,
                "pageSize": pageSize
            },
            success: function (data) {
                $("#list-container").html(data);
            },
            error: function () {
                toastr.error("error!");
            }
        });
        if($(".trigger-menu li").hasClass("active")){
            $(".trigger-menu li").removeClass("active");
        }
        $(".trigger-menu li.the-latest").addClass("active");
        $('.pagination-display-control').css("display", "block");
    });

    //最热查询
    $(".person-content-container").on("click", ".the-hot", function(){
        /**
         * 提交
         * TODO
         */
        querymethod = 2;
        pageIndex = 0;
        $.ajax({
            url: '/p/' + userId,
            type: 'GET',
            contentType: 'application/json',
            data: {
                "async": true,
                "order": "popular",
                "pageIndex": pageIndex,
                "pageSize": pageSize
            },
            success: function (data) {
                $("#list-container").html(data);
            },
            error: function () {
                toastr.error("error!");
            }
        });
        if($(".trigger-menu li").hasClass("active")){
            $(".trigger-menu li").removeClass("active");
        }
        $(".trigger-menu li.the-hot").addClass("active");
        $('.pagination-display-control').css("display", "block");
    });

    //目录查询,取消最新最热的样式，display分页栏
    $(".person-content-container").on("click", ".the-classification-name", function(){
        $.ajax({
            url: '/p/' + userId + '?classification=' + $(this).attr("classificationId"),
            type: 'GET',
            contentType: 'application/json',
            data: {
                "async": true
            },
            success: function (data) {
                $("#list-container").html(data);
                $('.pagination-display-control').css("display", "none");
            },
            error: function () {
                toastr.error("error!");
            }
        });
    });

    //模糊查询，回车查询
    $(".person-content-container").on("onkeyup", ".search-input", function(){
        /**
         * 提交
         * TODO
         */
        querymethod = 3;
        pageIndex = 0;
        $.ajax({
            url: '/p/' + userId,
            type: 'GET',
            contentType: 'application/json',
            data: {
                "keyword": $(".search-input").val(),
                "async": true,
                "pageIndex": pageIndex,
                "pageSize": pageSize
            },
            success: function (data) {
                $("#list-container").html(data);
            },
            error: function () {
                toastr.error("error!");
            }
        });
        if($(".trigger-menu li").hasClass("active")){
            $(".trigger-menu li").removeClass("active");
        }
        $(".trigger-menu li.search").addClass("active");
        $('.pagination-display-control').css("display", "block");
    });

    //模糊查询,按键查询
    $(".person-content-container").on("click", ".search-btn", function(){
        /**
         * 提交
         * TODO
         */
        querymethod = 3;
        pageIndex = 0;
        $.ajax({
            url: '/p/' + userId,
            type: 'GET',
            contentType: 'application/json',
            data: {
                "keyword": $(".search-input").val(),
                "async": true,
                "pageIndex": pageIndex,
                "pageSize": pageSize
            },
            success: function (data) {
                $("#list-container").html(data);
            },
            error: function () {
                toastr.error("error!");
            }
        });
        if($(".trigger-menu li").hasClass("active")){
            $(".trigger-menu li").removeClass("active");
        }
        $(".trigger-menu li.search").addClass("active");
        $('.pagination-display-control').css("display", "block");
    });



    $(".blog-content-container").on("click", ".blog-delete-blog", function () {
        // 获取 CSRF Token
        var csrfToken = $("meta[name='_csrf']").attr("content");
        var csrfHeader = $("meta[name='_csrf_header']").attr("content");


        $.ajax({
            url: blogUrl,
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

    //模糊查询,按键查询
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

    getClassification(username);
    initalPerson();
});