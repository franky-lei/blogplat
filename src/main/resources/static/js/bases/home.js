"use strict";
//# sourceURL=blog.js

// DOM 加载完再执行
$(function () {


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
                    url: '/',
                    type: 'GET',
                    contentType: 'application/json',
                    data: {
                        "keyword": $(".top-search-input").val(),
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
                    url: '/',
                    type: 'GET',
                    contentType: 'application/json',
                    data: {
                        "keyword": $(".top-search-input").val(),
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

            }
        }
    });


    //最新查询
    $(".home-content-container").on("click", ".the-latest", function(){
        /**
         * 提交
         * TODO
         */
        querymethod = 1;
        pageIndex = 0;
        $.ajax({
            url: '/',
            type: 'GET',
            contentType: 'application/json',
            data: {
                "keyword": $(".top-search-input").val(),
                "async": true,
                "order": "latest",
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
    $(".home-content-container").on("click", ".the-hot", function(){
        /**
         * 提交
         * TODO
         */
        querymethod = 2;
        pageIndex = 0;
        $.ajax({
            url: '/',
            type: 'GET',
            contentType: 'application/json',
            data: {
                "keyword": $(".top-search-input").val(),
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

    //模糊查询，回车查询
    // $(".the-top-nav-container").on("keypress", "#the-top-search-input", function(){
    //     /**
    //      * 提交
    //      * TODO
    //      */
    //     var thehref = '?keyword=' + $("#the-top-search-input").val();
    //     window.open(thehref, '_blank');
    // });

    $("#the-top-search-input").keypress(function(e){
        if(e.which == 13){
            var thehref = '?keyword=' + $("#the-top-search-input").val();
            window.open(thehref, '_blank');
        }
    });

    //模糊查询,按键查询
    $(".the-top-nav-container").on("click", "#top-search-button", function(){
        /**
         * 提交
         * TODO
         */
        var thehref = '?keyword=' + $("#the-top-search-input").val();
        window.open(thehref, '_blank');
    });

    initalPerson();
});