/*
 * @Author: CK
 * @Date:   2016-03-24 13:19:45
 * @Last Modified by:   CK
 * @Last Modified time: 2017-03-16 21:39:18
 */
$(function() {
	'use strict';
	
	
	//消息
	$(document).on("pageInit", "#Router-message-index", function(e, id, page) {
		RouterMessageIndex(page);
	});
	$(document).on("pageInit", "#Router-message-send", function(e, id, page) {
		RouterMessageSend(page);
	});
	
	
	
	
	//联系人
	$(document).on("pageInit", "#Router-friends-index", function(e, id, page) {
		RouterFriendsIndex(page);
	});
	$(document).on("pageInit", "#Router-friends-add", function(e, id, page) {
		RouterFriendsAdd(page);
	});
	$(document).on("pageInit", "#Router-friends-info", function(e, id, page) {
		RouterFriendsInfo(page);
	});
	
	
	
	//动态
	$(document).on("pageInit", "#Router-star-index", function(e, id, page) {
		RouterIndex(page);
	});
	
	
	
	//我的
	$(document).on("pageInit", "#Router-user-index", function(e, id, page) {
		RouterUserIndex(page);
	});
	$(document).on("pageInit", "#Router-user-my", function(e, id, page) {
		RouterUserMyInfo(page);
	});
	
	
	
	//注册
	$(document).on("pageInit", "#Router-register", function(e, id, page) {
		RouterRegister(page);
	});
	
	
	//tab 页
	$(window).on('pageInit', function(e, id, page) {
		//首页不显示消息
		var foot_pathname = window.location.pathname;
		var foot_index = 0;
		if (foot_pathname.indexOf('/message') !== -1) {
			foot_index = 0;
		} else if (foot_pathname.indexOf('/friends') !== -1) {
			foot_index = 1;
		} else if (foot_pathname.indexOf('/user') !== -1) {
			foot_index = 2;
		} else {
			foot_index = 0;
		}

		$('#J_site_footer').find('.tab-item').eq(foot_index).addClass('active').siblings('.tab-item').removeClass('active');

	});
    

	$.init();
});