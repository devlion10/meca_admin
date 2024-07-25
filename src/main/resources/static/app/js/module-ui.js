/**
 * JavaScript UI Library
 * @author
 * @copyright
 */

"use strict";

PUB.createNs("ui");



/**
 * UI 기능 코드
 * @namespace ui
 * @memberOf PUB
 */
PUB.ui = (function() {
	// dependency
	var util = PUB.util;

	return {
		/**
		 * 스케일 값
		 * @memberOf PUB.ui
		 */
		scaleValue: 1,
		/**
		 * 화면 스케일
		 * @memberOf PUB.ui
		 */
		setScale: function() {
		},
		/**
		 * 화면 스케일
		 * @memberOf PUB.ui
		 */
		initEvent: function() {
			
			$('.ui-accordmenu').accordionMenu();

			if($(".ui-scroll").exists()){
				$(".ui-scroll").each(function(){
					$(this).mCustomScrollbar();
				});
			}
		},
		/**
		 * 화면 스케일
		 * @memberOf PUB.ui
		 */
		ModalController:function(){
			$.fn.openModal = function (options) {
				this.each(function(index){
					if($(this).data('modalController') === undefined){
						var modalController = new ModalController(this, options);
						$(this).data('modalController', modalController);
					} else {
						var modalController = $(this).data('modalController');
					}
		
					if(modalController)
						modalController.show($(this));
				})
				return this;
			}
		
			$.fn.closeModal = function () {
				this.each(function(index){
					var modalController = $(this).data('modalController');
					if(modalController)
						modalController.hide($(this));
				})
		
				return this;
			}
		
			$.fn.layoutModal = function () {
				this.each(function(index){
					var modalController = $(this).data('modalController');
					if(modalController)
						modalController.center($(this));
				})
		
				return this;
			}
		
			function ModalController(selector, options){
				this.$modal = null;
				this.$openBtn = null;	// 열기 selector
				this.$closeBtn = null;	// 닫기 selector
				this.$targetBtn = null;
				this.$scroller = null;
				this.$opener = null;
		
				this._init(selector);
				this._initOptions(options);
				this._initEvent();
			}
		
			/* 요소 초기화 */
			ModalController.prototype._init=function(selector){
				this.$modal = $(selector);
				this.$closeBtn = this.$modal.find('.pop-close');
				//this.$header = this.$modal.find('.pop_head');
				//this.$content = this.$modal.find('.pop_cont');
				//this.$buttonWrap = this.$modal.find('.pop_foot');
		
				this.isShown = false;
			}
		
			// 옵션 초기화
			ModalController.prototype._initOptions=function(options){
				this.options = jQuery.extend({}, ModalController.defaultOptions, options);
			}
		
			ModalController.prototype.center=function(options){
				this.layout();
			}
		
			ModalController.prototype._initEvent=function(){
				var self = this;
		
				this.$closeBtn.on('click', function(e){
					e.preventDefault();
					e.stopPropagation();
					self.hide();
		
					if (self.options.opener) {
						$(self.options.opener).focus();
					}
				});
		
				$(window).on('resize resizeend', function (e) {
					if(self.$modal.is(':visible')){
						self.layout();
					}
				});
			}
		
			ModalController.prototype._createHolder=function(target){
				var me = this;
				this.$holder = $('<span class="ui-modal-holder" style="display:none;"></span>').insertAfter(this.$modal);
			}
		
			ModalController.prototype._replaceHolder=function(target){
				var me = this;
		
				if (me.$holder) {
					me.$modal.insertBefore(me.$holder);
					me.$holder.remove();
				}
			}
		
			ModalController.prototype._createModalContainer=function(){
				var me = this;
		
				me.scrollTop = $('html,body').scrollTop();
				me.$container = $('<div class="ui-modal-container" />');
		
				me.$container.css({
					'position': 'fixed',
					'top': 0,
					'left': 0,
					'right': 0,
					'bottom':0
					//'height': '100%'
				}).append(me.$modal.css({
					'zIndex': 2
				})).appendTo('body');
			}
		
			ModalController.prototype.layout=function(){
				var me = this,
					opts = me.options,width,
					height, attr,
					winHeight = $(window).height();
				
				
				me.$modal.css({
					'display': 'block',
					'visibility': 'hidden',
					'top': '',
					'left': '',
					'height': '',
					'width': ''
				});
		
				width = me.$modal.width();
				height = me.$modal.height();
		
				attr = {
					visibility: '',
					display: 'block'
				};
		
				if (height > winHeight) {
					attr.top = 0;
					attr.marginTop = opts.offsetTop
					attr.marginBottom = opts.offsetTop;
				} else {
					attr.top = (opts.forceTop > 0 ? opts.forceTop : (winHeight - height) / 2);
					attr.height = '';
				}
		
				me.$modal.stop().css(attr);
			}
		
			ModalController.prototype._removeModalContainer = function () {
				var me = this;
				me._replaceHolder();
				me.$container.remove();
				me.$dim = null;
				me.$container = null;
			}
		
			ModalController.prototype.show=function(){
				var me = this;
		
				if (this.isShown) {
					return;
				}
		
				this.isShown = true;
				this._createHolder();
				this._createModalContainer();
				this.layout();
		
				
		
				$('html,body').css({'overflow':'hidden'});
				// $('body').css({'position':'fixed', 'top': -me.scrollTop + 'px'});
			}
		
			ModalController.prototype.hide=function(e){
				var me = this;
		
				if(!this.isShown){
					return;
				}
		
				this.isShown = false;
		
				me.$modal.css({
					'position': '',
					'top': '',
					'left': '',
					'outline': '',
					'marginLeft': '',
					'marginTop': '',
					'backgroundClip': '',
					'zIndex': '',
					'display': ''
				});
		
				me._removeModalContainer();
				$('html,body').css({'overflow':''});
				// $('html,body').css({'position':'', 'top': ''});
				// $(window).scrollTop(me.scrollTop+'');
			}
		
			// 기본 옵션값
			ModalController.defaultOptions = {
				overlay: true,
				forceTop: 0,
				offsetTop: 50,
				offsetLeft: 208
			}

			$('[data-control="modal"]').click(function(){
				var $target = $('#' + $(this).attr('data-handler'));
				$target.openModal();
			});
		},
		accordionMenu:function(){
			// accordionMenu 플러그인
			$.fn.accordionMenu=function(){
				// 선택자에 해당하는 요소 개수 만큼 AccordionMenu 객체 생성
				this.each(function(index){
					var accordionMenu = new AccordionMenu(this);
					$(this).data("accordionMenu", accordionMenu);
				   
				});
				return this;
			}
			
			// accordionMenu 선택처리 플러그인
			$.fn.selectAccordionMenuAt=function(selectIndex, animation){
				this.each(function(index){
					var accordionMenu = $(this).data("accordionMenu");
					if(accordionMenu)
						accordionMenu.setSelectMenuItemAt(selectIndex,animation);
				
				});
				return this;
			}

			function AccordionMenu(selector){
				// 프로퍼티 생성하기 
				this.$accordionMenu = null;
				this._$menuBody  = null;
				this._$menuItems = null;
				
				// 선택 메뉴 아이템
				this._$selectItem = null;
				this._selectIdx = null;
				
				this._init(selector);
				this._initEvent();   
			}
			
			// 요소 초기화 
			AccordionMenu.prototype._init=function(selector){
				this.$accordionMenu = $(selector);
				this._$menuBody = this.$accordionMenu.find(">li");
				this._$menuItems = this._$menuBody.find('.accord-head a')
			}
			
			// 이벤트 초기화 
			AccordionMenu.prototype._initEvent=function(){
				var objThis = this;
				
				// 선택 메뉴 아이템 처리
				this._$menuItems.click(function(e){
					var idx = $(this).parents('li').index();
					// 선택 메뉴 아이템 처리
					objThis.setSelectMenuItem(idx);
				})
			}
			
			/*
			 * 선택 메뉴 아이템 처리
			 * $item : 선택 메뉴 아이템
			 */
			AccordionMenu.prototype.setSelectMenuItem=function(index, animation){
				// 선택 메뉴 아이템 스타일 처리
				if(this._$selectItem){
					if(this._selectIdx!= index){ // 토글
						this._$selectItem.removeClass("open");
						this._$selectItem.find('.accord-contents').stop().slideUp(250);
					}
				}

				if(this._$menuBody.eq(index).hasClass("open")){
					this._$menuBody.eq(index).removeClass("open");
					this._$menuBody.eq(index).find('.accord-contents').stop().slideUp(250);
				} else {
					this._$menuBody.eq(index).addClass("open");
					this._$menuBody.eq(index).find('.accord-contents').stop().slideDown(250);
				}
				
				this._$selectItem = this._$menuBody.eq(index);
				this._selectIdx = index;
			}
			
			/* 
			 * animation : 애니메이션 이동 여부
			 */
			AccordionMenu.prototype.setSelectMenuItemAt=function(index, animation){
				this.setSelectMenuItem(index, animation);
			}
		},
		tabPanel: function () {
			$.fn.tabPanel=function(options){
				this.each(function(index){
					var tabPanel = new TabPanel(this, options);
					 $(this).data("tabPanel", tabPanel);
				})

				return this;
			}

			$.fn.selectTabPanel=function(tabIndex){
				this.each(function(index){
					var tabPanel =$(this).data("tabPanel");
					if(tabPanel)
						tabPanel.setSelectTabMenuItemAt(tabIndex);
				})

				return this;
			}

			function TabPanel(selector, options){
				this._$tabPanel = null;
				this._$tabMenu = null;
				this._$tabMenuItems = null;
				this._$selectTabMenuItem = null;

				this._$tabContents = null;
				this._$selectTabContent = null;

				this._options = null;

				this._init(selector);
				this._initEvent();
				this._initOptions(options);

				this.setSelectTabMenuItemAt(this._options.startIndex,false);
			}

			// 요소 초기화
			TabPanel.prototype._init=function(selector){
				var me = this;

				this._$tabPanel = $(selector);
				this._$tabMenu = this._$tabPanel.find(".js_tab_menu");

				this._$tabMenuItems = this._$tabMenu.find("li");
				this._$tabContents = this._$tabPanel.find(".js_tab_contents .js_tab_item");

			}


			// 옵션 초기화
			TabPanel.prototype._initOptions=function(options){
				this._options = jQuery.extend({}, TabPanel.defaultOptions, options);
			}

			// 이벤트 초기화
			TabPanel.prototype._initEvent=function(){
				var self = this;
				this._$tabMenuItems.on("click",function(e){
					e.preventDefault();

					self.setSelectTabMenuItem($(this));
				})
			}

			// 탭 메뉴  아이템 선택
			TabPanel.prototype.setSelectTabMenuItem = function ($item) {
				this._$tabMenuItems.removeClass("is_active");
				if(this._$selectTabMenuItem){
					this._$selectTabMenuItem.removeClass("is_active");
				}
				this._$selectTabMenuItem = $item;
				this._$selectTabMenuItem.addClass("is_active");

				var newIndex = this._$tabMenuItems.index(this._$selectTabMenuItem);
				this._showContentAt(newIndex);
			}

			// index 번째 탭메뉴 아이템 선택
			TabPanel.prototype.setSelectTabMenuItemAt=function(index){
				this.setSelectTabMenuItem(this._$tabMenuItems.eq(index));
			}

			// index에 맞는 탭 내용 활성화
			TabPanel.prototype._showContentAt=function(index){
				// 1. 활성화/비활성화 탭 내용 찾기
				var $hideContent = this._$selectTabContent;
				var $showContent =  this._$tabContents.eq(index);

				TabPanel.normalEffect.effect({
					$hideContent:$hideContent,
					$showContent:$showContent
				});

				// 4. 선택 탭 내용 업데이트
				this._$selectTabContent = $showContent;
			}

			// 일반 출력 효과
			TabPanel.normalEffect={
				effect:function(params){
					if(params.$hideContent){
						params.$hideContent.hide();
					}

					params.$showContent.show();
				}
			}

			// 기본 옵션값
			TabPanel.defaultOptions = {
				startIndex:0
			}
		}
	}
}());

/**
 * Common Module
 */
let CommonUI = {};

(function(_globalInstance){
	//초기화
	if( !_globalInstance ) _globalInstance = {};


	/**
	 * 데이터 picker 적용
	 * @param el
	 * @param callback
	 * @private
	 */
	function _elDatePicker(el, callback){
		el = el instanceof jQuery ? el : $(el);

		//초기화
		if(el.hasClass('hasDatepicker') == true) {
            el.siblings('.ui-datepicker-trigger,.ui-datepicker-apply').remove();
            el
            .removeAttr('id')
            .removeClass('hasDatepicker')
            .removeData('datepicker')
            .unbind();
		}

		let param = {};
		if (el.data("yearrange")) {
			param.yearRange = el.data("yearrange");
		} else {
			param.yearRange = 'c-50:c+20';
		}
		
		el.datepicker({
			dateFormat: "yy-mm-dd",
			yearRange: param.yearRange,

			prevText: '이전 달',
			nextText: '다음 달',

			changeYear: true, // 년 셀렉트박스 표시
			changeMonth: true, // 월 셀렉트박스 표시

			monthNames: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"], //달력의 월 부분 Tooltip 텍스트
			monthNamesShort: ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"], //달력의 월 부분 텍스트

			dayNames: [ "일", "월", "화", "수", "목", "금", "토" ], //달력의 요일 부분 Tooltip 텍스트
			dayNamesShort: [ "일", "월", "화", "수", "목", "금", "토" ], //달력의 요일 부분 텍스트
			dayNamesMin: ["일", "월", "화", "수", "목", "금", "토"], //달력의 요일 부분 텍스트

			firstDay: 0,  // 시작 요일: 1 - 월요일, 0 - 일요일
			showMonthAfterYear: true, //년도 먼저 나오고, 뒤에 월 표시
			yearSuffix: '년', //달력의 년도 부분 뒤에 붙는 텍스트

			// 선택한 데이터 추출
			onSelect: function(date) {
				// let date = $.datepicker.formatDate("yymmdd", $(".el_datePicker").datepicker("getDate"));
				//
				// date = $(".el_datePicker").val();
				// date = date.split('.'); // 데이터 값 년, 월, 일로 쪼개기
				//
				// let year, month;
				// year = date[0];
				// month = date[1];
				//
				// // 년, 월 데이터 넣기
				// let cal_year = document.querySelector('.cal_year');
				// let cal_month = document.querySelector('.cal_month');
				// if (cal_year != null) {
				// 	cal_year.innerText = year;
				// }
				// if (cal_month != null) {
				// 	cal_month.innerText = month;
				// }
				if( typeof clickDate == 'function'){
					// 함수가 선언이 되어있을 경우에만 메인에서만 씀
					if( closePopCalList && typeof closePopCalList == "function") closePopCalList();

					clickDate(date.split('-')[0]+date.split('-')[1]+date.split('-')[2])
				}
				if( callback && typeof callback == "function") callback();
			}
		});

		// sDate - eDate 보완
		el.find('.sdate').datepicker();
		el.find('.sdate').datepicker("option", "maxDate",el.find(".edate").val());
		el.find('.sdate').datepicker("option", "onClose", function (selectedDate) { 
			el.find('.edate').datepicker( "option", "minDate", selectedDate );
		});
		el.find('.edate').datepicker();
		el.find('.edate').datepicker("option", "minDate",el.find(".sdate").val());
		el.find('.edate').datepicker("option", "onClose", function (selectedDate) { 
			el.find('.sdate').datepicker( "option", "maxDate", selectedDate );
		});

		//datepicker 자동완성 기능 해제
		el.attr('autocomplete', 'off');
	}

	function laypopup(options) {
		let opt = Object.assign({
			layerId : "meca_layer",
			title : "홈페이지 안내",
			content : "",
			img: "",
			link: "",
			width: "",
			height: "",
			startTop: 0,
			startLeft: 0,
			cache : true
		},options);

		let html = $(`
		<div class="layerPopup" id="layer_popup" style="visibility: visible;">
			<div class="layerBox">`
				+(opt.title ? `<h4 class="title">${opt.title}</h4>` : '')+
				`<div class="cont">
					<div>
						${opt.content}
					</div>`
					+ (opt.link ? `<a href="${opt.link}" target="_blank">` : '')
					+ (opt.img ? `<img src="/api/homepage/popup/download?attachFilePath=${opt.img}" style="width:100%; height: auto" />` : '')
					+ (opt.link ? `</a>` : '') +
				`</div>
				<form class="pop_form" name="pop_form">
					<div id="check" ${opt.cache == true ? 'style="display:inline-block;"':'style="display:none;"'}><input type="checkbox" name="chkbox" value="checkbox" id='${opt.layerId}_chk' ><label for="${opt.layerId}_chk">&nbsp&nbsp오늘 하루 동안 보지 않기</label></div>
					<div id="close" ><a class='close-button-layerpopup' href="#">닫기</a></div>
				</form>
			</div>
		</div>
		`);

		//닫기 버튼 클릭 시
		html.find('.close-button-layerpopup').click(function(e){
			e.preventDefault();
			let form = html.find(".pop_form").get(0);
			if ( form.chkbox.checked ){
				setCookie( opt.layerId, "done" , 1 );
			}
			html.css({visibility : "hidden" });
			html.remove();

		});
		html.find(".layerBox").css({ left: opt.startLeft, top: opt.startTop }).draggable();
		html.find(".layerBox").css({width:opt.width ? opt.width : '', height:opt.height ? opt.height : 'auto'});

		// if (document.cookie.indexOf(opt.layerId + "=done") < 0) {      // 쿠키 저장여부 체크
			$('body').append(html);
			html.show();
		// }else {
		// 	html.hide();
		// }
		return html;
	}

	//data-upload="image" data-target="profile_img"
	/*	function fileUploadImage(el){
		let _this = this;
		this.el = el instanceof jQuery ? el[0] : typeof el =="string" ? document.querySelector(el) : el;

		this.options = {
			target : this.el.dataset.target,
			wrap : undefined
		}
		if( document.querySelector(this.options.target).tagName != "IMG" ){
			this.options.wrap = document.querySelector(this.options.target);
			this.previewImage = new Image();
		}else{
			this.previewImage = document.querySelector(this.options.target)
		}

		this.el.addEventListener("change", function(e){
			if (e.target.files && e.target.files[0]) {
				var reader = new FileReader();
				reader.onload = function(evt) {
					_this.setImage( evt.target.result );
				};
				reader.readAsDataURL(e.target.files[0]);
			} else {
				_this.previewImage.style.display = "none";
			}
		});
	}
	fileUploadImage.prototype.setImage = function(src){
		let _this = this;
		_this.previewImage.src = src;
		_this.previewImage.onload = function(){
			if( _this.options.wrap ) {
				let ratio = _this.previewImage.width/_this.previewImage.height > 1 ? "가로" : "세로";
				$(_this.options.wrap).css({
					"background" : "none",
					"display": "flex",
					"justify-content": "center",
				});
				$(_this.previewImage).css({
					"max-width" : "100%",
					"max-height" : "100%",
					"object-fit" : ratio == "가로" ? "contain" : "contain"
				});
				$(_this.options.wrap).html(_this.previewImage);
			}
		}
	}*/


	// _globalInstance.fileUploadImage = fileUploadImage;
	_globalInstance.datePicker = _elDatePicker;
	// _globalInstance.searchMediaCompany = searchMediaCompany;
	_globalInstance.sendSMS = sendSMSController;

	_globalInstance.ComponentFile = function(element, urlPath){
		let url="/api/common/upload/download?attachFilePath="+urlPath;
		let input = $(`<input type="text" id="thumbnailFileName" class="bl_upload_path js_path" readonly />`);
		let html = $(`<span></span>`).append(input);
		input.val(urlPath);
		$(element).parent().prepend(html);
	}

	_globalInstance.Laypopup = laypopup;
})(CommonUI);


function paginationAjax(bl_paginate,pageInfo){
	// const SIZE = pageInfo.size;
	// 페이징 관련
	const maxPage = pageInfo.totalPages; // 최대 페이지
	const blockPage = pageInfo.blockSize || 10; // 한 블럭의 페이지

	// 페이지 블록 구하기
	const currentBlock = parseInt(pageInfo.page / blockPage) + 1;
	const totalBlock = parseInt(pageInfo.totalPages / blockPage) + 1;
	const startBlock = (currentBlock-1) * blockPage + 1;
	const endBlock = ((currentBlock * blockPage)) > maxPage ? maxPage : currentBlock * blockPage;

	let pageMove = function(i){
		pageInfo.onClick && typeof pageInfo.onClick == "function" && pageInfo.onClick(i);
	}

	const bl_paginate_more = document.createElement('div');
	bl_paginate_more.setAttribute('class','bl_paginate_more')
	const bl_pageNums = document.createElement('div');
	bl_pageNums.setAttribute('class', 'bl_pageNums');

	// 맨 앞으로 버튼
	let firstItem = document.createElement('span');
	firstItem.setAttribute('class', 'bl_paginate_first bl_paginate_btn');
	pageInfo.first && firstItem.classList.add('disabled');
	firstItem.innerHTML = '<img src="/assets/images/icons/page_first.png"/>'
	bl_pageNums.appendChild(firstItem)

	// 앞 블록 버튼
	let prevItem = document.createElement('span');
	prevItem.setAttribute('class', 'bl_paginate_prev bl_paginate_btn')
	currentBlock === 1 && prevItem.classList.add('disabled');
	prevItem.innerHTML = '<img src="/assets/images/icons/page_prev.png"/>'
	bl_pageNums.appendChild(prevItem)

	// 페이지 랜더링
	for(let i = startBlock; i<endBlock+1; i++){
		let pageItem = document.createElement('a');
		pageItem.innerText = i;
		$(pageItem).click(function(e){
			e.preventDefault();
			pageMove(i);
		});
		(i-1) === pageInfo.page ? pageItem.setAttribute('class', 'is_active') : '';
		bl_pageNums.appendChild(pageItem)
	}

	// 뒷 블록 버튼
	let nextItem = document.createElement('span');
	nextItem.setAttribute('class', 'bl_paginate_next bl_paginate_btn')
	currentBlock === totalBlock && nextItem.classList.add('disabled');
	nextItem.innerHTML = '<img src="/assets/images/icons/page_next.png"/>'
	bl_pageNums.appendChild(nextItem)

	// 마지막 버튼
	let lastItem = document.createElement('span');
	lastItem.setAttribute('class', 'bl_paginate_last bl_paginate_btn')
	pageInfo.last && lastItem.classList.add('disabled');
	lastItem.innerHTML = '<img src="/assets/images/icons/page_last.png"/>'
	bl_pageNums.appendChild(lastItem)

	bl_paginate_more.appendChild(bl_pageNums);
	bl_paginate.innerHTML = "";
	bl_paginate.appendChild(bl_paginate_more);

	// 페이징 숫자 버튼을 제외한 버튼
	const firstBtn = bl_paginate.querySelector(".bl_paginate .bl_paginate_first");
	const prevBtn = bl_paginate.querySelector(".bl_paginate .bl_paginate_prev");
	const nextBtn = bl_paginate.querySelector(".bl_paginate .bl_paginate_next");
	const lastBtn = bl_paginate.querySelector(".bl_paginate .bl_paginate_last");

	firstBtn.addEventListener("click", () => { pageMove(1); })
	prevBtn.addEventListener("click", () => { pageMove( (startBlock-blockPage) ); })
	nextBtn.addEventListener("click", () => { pageMove( (startBlock+blockPage) ); })
	lastBtn.addEventListener("click", () => { pageMove( maxPage ); })
}


/**
 * 강사검색
 * @param type 미디어강사/언론인연수강사 선택
 * @returns {Promise<unknown>}
 */
function searchLecture(type){
	return new Promise((resolve, reject) =>{
		let modalWrap = $(`
			<article class="pop_wrapper ui-modal" id="popInstructorSelection">
				<div class="pop_wrap pop_fixed pop_secondary">
					<div class="pop_cont">
						<form class="sch_box">
							<select id="search_lecture_type" name="type" class="common_select">
								<option value="">권한 전체</option>
								<option value="READER">언론인연수강사</option>
								<option value="LECTURER">미디어교육강사</option>
								<option value="INSTR">미디어강사</option>
							</select>
							<select id="search_lecture_txt_type" name="text" class="common_select">
								<option value="1">이름+아이디</option>
								<option value="2">이름</option>
								<option value="3">아이디</option>
							</select>
							<input id="search_lecture_txt" type="text" class="hp_w200">
							<button class="btn btnDefault btnSearch">검색</button>
						</form>
	
						<div class="tableResponsive overflowMulti">
							<table class="table">
								<thead>
									<tr>
										<th scope="col" style="width:200px;">권한</th>
										<th scope="col">이름(아이디)</th>
										<th scope="col">소속회사</th>
										<th scope="col">직위</th>
										<th scope="col" style="width:80px;">상태</th>
										<th scope="col" style="width:80px;">선택</th>
									</tr>
								</thead>
								<tbody>
									
								</tbody>
							</table>
						</div>
	
						<div class="bl_paginate"></div>
					</div>
	
					<button type="button" class="pop_close pop-close">닫기</button>
				</div>
			</article>`);

		//페이지 기본 정보
		let pageInfo = {
			page: 0,
			size:10
		}
		let instrCtgr = type;
		let searchBox = modalWrap.find(".sch_box");
		let searchButton = modalWrap.find(".btnSearch");
		let closeBtn = modalWrap.find(".pop_close");
		let itemWrap = modalWrap.find("tbody");

		//닫기 버튼
		closeBtn.click(function(){
			_closeModal();
		});

		//검색폼
		searchBox.submit(function(e){
			e.preventDefault();
			let type = searchBox.find("#search_lecture_type").val();
			let searchTxt = searchBox.find("#search_lecture_txt").val().trim();
			let searchTxtType = searchBox.find("#search_lecture_txt_type").val();
			pageInfo.searchTxt = searchTxt;
			pageInfo.instrCtgr = type;
			callAjax(1);
		});

		pageInfo.onClick = function(pageIndex){
			callAjax(pageIndex);
		}
		let callAjax = function(pageIndex){
			if(pageIndex != undefined){
				pageInfo.page = pageIndex-1;
			}
			itemWrap.css({visibility:"hidden"});
			let url = `/api/user/instructor/page?page=${pageInfo.page}&instrStts=1`;
			if( pageInfo.instrCtgr ){
				url += "&instrCtgr="+pageInfo.instrCtgr;
			}
			if(pageInfo.searchTxt){
				url += "&instrNm="+pageInfo.searchTxt;
			}
			$.get(url).then(res=>{
				itemWrap.css({visibility:"visible"});
				if( res && res.content && res.content.length ){
					itemWrap.empty();
					res.content.forEach(generalItem);
				}
				pageInfo = Object.assign(pageInfo, res);
				paginationAjax( modalWrap.find(".bl_paginate").get(0), pageInfo);
			});
		}

		//아이템을 만들기
		let generalItem = function(item){
			let _itemHtml = $(`<tr>
				<td>
					`+
					(function(ii) {
						var typeText = '';
						if (ii.instrCtgr.indexOf('READER') != -1){
							typeText = typeText + `<div>언론인연수강사</div>`;
						}
						if(ii.instrCtgr.indexOf('LECTURER') != -1){
							typeText = typeText + `<div>미디어교육강사</div>`;
						}
						if(ii.instrCtgr.indexOf('INSTR') != -1){
							typeText = typeText + `<div>미디어강사</div>`;
						}
						return typeText;
					})(item)
					+`
				</td>
				<td>${item.instrNm}${item.userId ? '('+item.userId+')':''}</td>
				<td>${item.orgName ? item.orgName : ""}</td>
				<td>${item.department ? item.department : ""}</td>
				<td>
					`+
					(function(ii) {
						if (ii.instrStts == 1){
							return `<span class="state state__green">사용</span>`;
						}else{
							return `<span className="state state__red">미사용</span>`;
						}
					})(item)
					+`
				</td>
				<td>
					<div class="btn btnDefault">선택</div>
				</td>
			</tr>`
			);
			_itemHtml.find(".btn").click(function(){
				resolve({
					seq : item.instrSerialNo,
					username : item.instrNm,
				});
				_closeModal();
			});
			_itemHtml.appendTo(itemWrap);
		}

		//닫기 기능
		let _closeModal = function(){
			modal.closeModal();
			modalWrap.remove();
		}


		//모달 노출
		modalWrap.appendTo(document.body);
		let modal = modalWrap.openModal({
			removeComponent:true,
			onHide: function(){},
			onShow: function(){}
		});
		callAjax();
	});
}

/**
 * 강사검색, 일괄 선택
 * @param type 언론인연수강사, 미디어교육강사 선택
 * @returns {Promise<unknown>}
 */
function searchLecturers(){
	return new Promise((resolve, reject) =>{
		let modalWrap = $(`
			<article class="pop_wrapper ui-modal" id="popInstructorSelection">
				<div class="pop_wrap pop_fixed pop_secondary">
					<div class="pop_cont">
						<form class="sch_box">
							<select id="search_lecture_type" name="type" class="common_select">
								<option value="">권한 전체</option>
								<option value="READER">언론인연수강사</option>
								<option value="LECTURER">미디어교육강사</option>
								<option value="INSTR">미디어강사</option>
							</select>
							<select id="search_lecture_txt_type" name="text" class="common_select">
								<option value="1">이름+아이디</option>
								<option value="2">이름</option>
								<option value="3">아이디</option>
							</select>
							<input id="search_lecture_txt" type="text" class="hp_w200">
							<button class="btn btnDefault btnSearch">검색</button>
						</form>
	
						<div class="tableResponsive overflowMulti">
							<table class="table">
								<thead>
									<tr>
										<th scope="col" style="width:200px;">권한</th>
										<th scope="col">이름(아이디)</th>
										<th scope="col">소속회사</th>
										<th scope="col">직위</th>
										<th scope="col" style="width:80px;">상태</th>
										<th scope="col" style="width:80px;">선택</th>
									</tr>
								</thead>
								<tbody>
									
								</tbody>
							</table>
						</div>
						<div class="d-flex-between">
							<div class="bl_paginate"></div>
							<div class="btn btnDefault selects">선택</div>
						</div>
					</div>
					<button type="button" class="pop_close pop-close">닫기</button>
				</div>
			</article>`);

		//페이지 기본 정보
		let pageInfo = {
			page: 0,
			size:10
		}
		var values = [];
		let searchBox = modalWrap.find(".sch_box");
		let selectBtn = modalWrap.find(".btn.btnDefault.selects");
		let closeBtn = modalWrap.find(".pop_close");
		let itemWrap = modalWrap.find("tbody");

		//선택 버튼
		selectBtn.click(function() {
			resolve({values});
			_closeModal();
		});

		//닫기 버튼
		closeBtn.click(function() {
			_closeModal();
		});

		//검색폼
		searchBox.submit(function(e){
			e.preventDefault();
			let type = searchBox.find("#search_lecture_type").val();
			let searchTxt = searchBox.find("#search_lecture_txt").val().trim();
			let searchTxtType = searchBox.find("#search_lecture_txt_type").val();
			pageInfo.searchTxt = searchTxt;
			pageInfo.instrCtgr = type;
			callAjax(1);
		});

		pageInfo.onClick = function(pageIndex){
			callAjax(pageIndex);
		}
		let callAjax = function(pageIndex){
			if(pageIndex != undefined){
				pageInfo.page = pageIndex-1;
			}
			itemWrap.css({visibility:"hidden"});
			let url = `/api/user/instructor/page?page=${pageInfo.page}&instrStts=1`;
			if( pageInfo.instrCtgr ){
				url += "&instrCtgr="+pageInfo.instrCtgr;
			}
			if(pageInfo.searchTxt){
				url += "&instrNm="+pageInfo.searchTxt;
			}
			$.get(url).then(res=>{
				itemWrap.css({visibility:"visible"});
				if( res && res.content && res.content.length ){
					itemWrap.empty();
					res.content.forEach(generalItem);
				}
				pageInfo = Object.assign(pageInfo, res);
				let paginated = modalWrap.find('.bl_paginate').get(0)
				paginationAjax( paginated, pageInfo);
				// paginated.innerHTML += `<div class='btn btnDefault'>선택</div>`;
			});
		}

		//아이템을 만들기
		let generalItem = function(item){
			let _itemHtml = $(`<tr>
										<td>
											`+
				(function(ii) {
					var typeText = '';
					if (ii.instrCtgr.indexOf('READER') != -1){
						typeText = typeText + `<div>언론인연수강사</div>`;
					}
					if(ii.instrCtgr.indexOf('LECTURER') != -1){
						typeText = typeText + `<div>미디어교육강사</div>`;
					}
					if(ii.instrCtgr.indexOf('INSTR') != -1){
						typeText = typeText + `<div>미디어강사</div>`;
					}
					return typeText;
				})(item)
				+`
										</td>
										<td>${item.instrNm}${item.userId ? '('+item.userId+')':''}</td>
										<td>${item.orgName ? item.orgName : ""}</td>
										<td>${item.department ? item.department : ""}</td>
										<td>
											`+
				(function(ii) {
					if (ii.instrStts == 1){
						return `<span class="state state__green">사용</span>`;
					}else{
						return `<span class="state state__red">미사용</span>`;
					}
				})(item)
				+`
										</td>
										<td>
											<input class="select" type="checkbox"/>
										</td>
									</tr>`
			);
			_itemHtml.find(".select").click(function(){
				let num = values.findIndex(data => data.seq == item.instrSerialNo)
				if (num > -1) {
					values.splice(num, 1)
				} else {
					var selected = {
						seq : item.instrSerialNo,
						username : item.instrNm
					}
					values.push(selected)
				}
			});
			_itemHtml.appendTo(itemWrap);
		}

		//닫기 기능
		let _closeModal = function(){
			modal.closeModal();
			modalWrap.remove();
		}

		//모달 노출
		modalWrap.appendTo(document.body);
		let modal = modalWrap.openModal({
			removeComponent:true,
			onHide: function(){},
			onShow: function(){}
		});
		callAjax();
	});
}




/**
 * 과정선택 팝업
 * @param hidden
 */
function searchCurriculum(eduType,hidden,commonCode) {
	return new Promise(function(resolve, reject){
		//페이지 기본 정보
		let pageInfo = {
			page: 0,
			size:10
		}
		let modalWrap = $(`
			<article class="pop_wrapper ui-modal" id="modalOfCurriculum">
            <div class="pop_wrap pop_secondary">
                <div class="pop_cont">
                    <form class="sch_box">
                        <select class="common_select" id="educationType">
                            <option value="" selected="true">전체</option>
                            `+(function () {
								let list = _.find(commonCode, {code : "EDU_TYPE"});
								let optionsHtml = "";
								list?.subCode.forEach(function(categoryCode){
									optionsHtml += `<option value="${categoryCode.code}">${categoryCode.codeName}</option>`;
								});
								return optionsHtml;
							})()+`
                        </select>
                       	<select class="common_select" id="categoryCode">
                       		<option value="" selected="true">전체</option>
							`+(function () {
								let list = _.find(commonCode, {code : "CTS_CTGR"});
								let optionsHtml = "";
								list?.subCode.forEach(function(categoryCode){
									optionsHtml += `<option value="${categoryCode.code}">${categoryCode.codeName}</option>`;
							});
							return optionsHtml;
							})() + `
                        </select>
                        <input id="search_txt_type" type="text" class="hp_w200" placeholder="과정명을 입력해주세요.">
                        <button class="btn btnDefault btnSearch">검색</button>
                    </form>

                    <div class="tableResponsive overflowMulti">
                        <table class="table">
                            <thead>
                            <tr>
                                <th scope="col">번호</th>
                                <th scope="col">유형</th>
                                <th scope="col">카테고리</th>
                                <th scope="col">과정명</th>
                                <th scope="col">등록일</th>
                                <th scope="col">선택</th>
                            </tr>
                            </thead>
                            <tbody id="modalBodyOfCurriculum">
                            </tbody>
                        </table>
                    </div>
                    <div class="bl_paginate"></div>
                </div>
                <button type="button" class="pop_close pop-close">닫기</button>
            </div>
        </article>	
		`);
		let itemWrap = modalWrap.find("tbody");
		let closeBtn = modalWrap.find(".pop-close");

		if(eduType != undefined){
			modalWrap.find("#educationType").val(eduType).attr("disabled",true);
		}
		pageInfo.onClick = function(pageIndex){
			callAjax(pageIndex);
		}
		let callAjax = function(pageIndex){
			if(pageIndex != undefined){
				pageInfo.page = pageIndex-1;
			}
			itemWrap.css({visibility:"hidden"});
			let url = `/api/education/curriculum?page=${pageInfo.page}&size=10&isUsable=true${eduType!=null && eduType != undefined && eduType.length > 0 ? '&educationType='+eduType : ''}`
			//검색조건
			if( pageInfo.categoryCode ){
				url += "&categoryCode="+pageInfo.categoryCode;
			}
			if(pageInfo.searchTxt){
				url += "&curriculumName="+pageInfo.searchTxt;
			}

			$.ajax({
				url:url,
				type:"get", //http method
				dataType:"json", //html, xml, text, script, json, jsonp 등 다양하게 쓸 수 있음
			}).then(res=>{
				itemWrap.css({visibility:"visible"});
				if( res && res.content && res.content.length ){
					itemWrap.empty();
					res.content.forEach(generalItem);
				}
				pageInfo = Object.assign(pageInfo, res.pageable);
				paginationAjax( modalWrap.get(0).querySelector(".bl_paginate"), pageInfo);
			});
		}//function end

		let generalItem = function(curInfo, index, list){
			let _itemHtml = $(
				"<tr>" +
				"<td>" + (index + 1) + "</td>" +
				"<td>" + commonCode.filter(code => code.code === "EDU_TYPE").map(filterCode => filterCode.subCode.filter(seleted => seleted.code == curInfo.educationType))[0][0].codeName + "</td>" +
				"<td>" + commonCode.filter(code => code.code === "CTS_CTGR").map(filterCode => filterCode.subCode.filter(seleted => seleted.code == curInfo.categoryCode))[0][0].codeName + "</td>" +
				"<td>" + curInfo.curriculumName + "</td>" +
				"<td>" + curInfo.createDateTime.substr(0, 10) + "</td>" +
				"<td><a class='btn btnDefault'>선택</a></td>" +
				"</tr>"
			);
			_itemHtml.find(".btn").click(function(){
				resolve(curInfo);
				_closeModal();
			});
			_itemHtml.appendTo(itemWrap);
		}

		//닫기 기능
		let _closeModal = function(){
			modal.closeModal();
			modalWrap.remove();
		}

		//검색폼
		let searchBox = $(modalWrap).find(".sch_box");//검색폼
		searchBox.submit(function(e){
			e.preventDefault();
			let categoryCode = searchBox.find("#categoryCode").val();
			let educationType = searchBox.find("#educationType").val().trim();
			let searchTxtType = searchBox.find("#search_txt_type").val();
			pageInfo.categoryCode = categoryCode;
			pageInfo.educationType = educationType;
			pageInfo.searchTxt = searchTxtType;
			callAjax(1);
		});

		//닫기 버튼
		closeBtn.click(function(){
			_closeModal();
		});
		//모달 노출
		$(modalWrap).appendTo(document.body);
		let modal = $(modalWrap).openModal({
			removeComponent:true,
			onHide: function(){},
			onShow: function(){}
		});
		callAjax(1);
	});
}



/**
 * 사용자 검색
 * @param type
 * @returns {Promise<unknown>}
 */
function searchUser(type){

	let hasDuplicateId = function (array, object) {
		return _.some(array, function(item) {
			return item.userSerialNo === object.userSerialNo;
		});
	}

	let removeById = function (array, userSerialNo) {
		_.each(array, function(item, index) {
			if (item.userSerialNo === userSerialNo) {
				array.splice(index, 1);
			}
		});
	}

	// 배열 내에서 id 속성이 일치하는 객체가 있는지 검사하는 함수
	let hasId = function (array, userSerialNo) {
		return _.some(array, function(item) {
			return item.userSerialNo === userSerialNo;
		});
	}

	return new Promise((resolve, reject) =>{
		let _SELECT_LIST = [];
		let modalWrap = $(`
			<article class="pop_wrapper ui-modal" id="popInstructorSelection">
				<div class="pop_wrap pop_fixed pop_secondary">
					<div class="pop_cont">
						<form class="sch_box">
							<input id="search_user_id" type="text" class="hp_w200" placeholder="아이디">
							<input id="search_user_name" type="text" class="hp_w200" placeholder="이름">
							<button class="btn btnDefault btnSearch">검색</button>
						</form>
	
						<div class="tableResponsive overflowMulti">
							<table class="table">
								<thead>
									<tr>
										<th scope="col"><input type="checkbox" id="_all_lmsuser_check"></th>
										<th scope="col">권한</th>
										<th scope="col">회원유형</th>
										<th scope="col">이름</th>
										<th scope="col">아이디</th>
										<th scope="col">휴대전화</th>
										<th scope="col">생년월일</th>
									</tr>
								</thead>
								<tbody>
									
								</tbody>
							</table>
						</div>
	
						<div class="bl_paginate"></div>
						<div style="text-align: right;border-top: 1px solid #ccc;padding-top:20px;">
							<button class="btn btn-primary" id="complete-btn">불러오기</button>
						</div>
					</div>
	
					<button type="button" class="pop_close pop-close">닫기</button>
				</div>
			</article>`);
		//페이지 기본 정보
		let pageInfo = {
			page: 0,
			size: 20
		}
		let instrCtgr = type;
		let searchBox = modalWrap.find(".sch_box");
		let searchButton = modalWrap.find(".btnSearch");
		let closeBtn = modalWrap.find(".pop_close");
		let pageBox = modalWrap.find(".bl_paginate");
		let itemWrap = modalWrap.find("tbody");
		let allUserCheck = modalWrap.find("#_all_lmsuser_check");

		allUserCheck.change(function(e){
			if( e.target.checked ){
				modalWrap.find("[scope=user-item] input[name=USER_NO]").each(function(){
					$(this).prop("checked", true).change();
				});
			}else{
				modalWrap.find("[scope=user-item] input[name=USER_NO]").each(function(){
					$(this).prop("checked", false).change();
				});
			}
		})

		//닫기 버튼
		closeBtn.click(function(){
			_closeModal();
		});

		//검색폼
		searchBox.submit(function(e){
			e.preventDefault();
			//let type = searchBox.find("#search_lecture_type").val();
			let userId = searchBox.find("#search_user_id").val().trim();
			let userNm = searchBox.find("#search_user_name").val();
			pageInfo.userId = userId;
			pageInfo.userName = userNm;
			callAjax(1);
		});

		pageInfo.onClick = function(pageIndex){
			callAjax(pageIndex);
		}
		let callAjax = function(pageIndex){
			if(pageIndex != undefined){
				pageInfo.page = pageIndex-1;
			}
			itemWrap.css({visibility:"hidden"});
			let url = `/api/user/web-user?size=${pageInfo.size}&page=${pageInfo.page}`;
			if( pageInfo.userId ){
				url += "&userId="+pageInfo.userId;
			}
			if(pageInfo.userName){
				url += "&userName="+pageInfo.userName;
			}
			$.getJSON(url).then(res=>{
				itemWrap.css({visibility:"visible"});
				if( res && res.content && res.content.length ){
					itemWrap.empty();
					res.content.forEach(generalItem);
				}
				pageInfo = Object.assign(pageInfo, res.pageable);
				paginationAjax(pageBox.get(0), res.pageable)
			});
		}

		//아이템을 만들기
		let generalItem = function(item){
			/** item { name, seq } */
			let _itemHtml = $(`<tr scope="user-item">
						<td>
							<input id="chk${item.userSerialNo}" type="checkbox" name="USER_NO" value="${item.userSerialNo}" ${hasId(_SELECT_LIST,item.userSerialNo) ? 'checked="true"': ""} />
							<label for="chk${item.userSerialNo}"><span class="me-0"></span></label>
						</td>
						<td>
							`+
				(function(ii) {
					if (ii.roleGroup == 'JOURNALIST'){
						return `<span>언론인</span>`;
					}else if(ii.roleGroup == 'TEACHER'){
						return `<span>교원</span>`;
					}else if(ii.roleGroup == 'STUDENT'){
						return `<span>학생</span>`;
					}else if(ii.roleGroup == 'PARENTS'){
						return `<span>학부모</span>`;
					}else {
						return `<span>일반인</span>`;
					}
				})(item)
				+`
						</td>
						<td>`+
						(function(ii) {
							if (ii.businessAuthority == 'AGENCY'){
								return `<span>기관담당자</span>`;
							}else if(ii.businessAuthority == 'SCHOOL'){
								return `<span>학교담당자</span>`;
							}else if(ii.businessAuthority == 'INSTR'){
								return `<span>미디어강사</span>`;
							}else {
								return `<span></span>`;
							}
						})(item)
						+`</td>
						<td>${item.userId}</td>
						<td>${item.userName}</td>
						<td>${item.phone}</td>
						<td>${item.birthDay}</td>
					</tr>`
			);
			_itemHtml.find("[name=USER_NO]").change(function(){
				if (this.checked ){
					if (!hasDuplicateId(_SELECT_LIST, item)) {
						_SELECT_LIST.push(item);
					}
				} else {
					removeById(_SELECT_LIST, item.userSerialNo);
				}
			});
			_itemHtml.appendTo(itemWrap);
		}

		//닫기 기능
		let _closeModal = function(isResolve){
			modal.closeModal();
			modalWrap.remove();
			if( !isResolve ) reject();
		}

		//전송
		modalWrap.find("#complete-btn").click(function(e){
			e.preventDefault();
			resolve(_SELECT_LIST);
			_closeModal(true);
		});

		//모달 노출
		modalWrap.appendTo(document.body);
		let modal = modalWrap.openModal({
			removeComponent:true,
			onHide: function(){},
			onShow: function(){}
		});
		callAjax();
	});
}



/**
 * SMS 전송 공통 팝업
 * @param options 옵션
 * @returns {Promise<void>}
 */
async function sendSMSController(userList, options) {
	// 팝업 HTML 생성
	const _POPUP = $(`
                    <article class="pop_wrapper ui-modal" id="mobileMessagePop">
                        <div class="pop_wrap pop_secondary" style="max-width: 1000px;">
                            <div class="pop_cont">
                                <div class="d-flex" style="gap: 20px">
                                    <div class="tableResponsive" style="width: 290px;">
                                        <div class="bl_gridBtns d-flex mb-2">
                                            <div class="fs-6 bl_gridBtns_lgt">
                                                수신자 <strong id="_sms_popup_text_selectMember" class="text-primary">0</strong> 명
                                            </div>
                                            <div class="bl_gridBtns_rgt">
                                                <button id="_sms_popup_button_callMember" class="btn btnDefault">주소록</button>
                                            </div>
                                        </div>
                                        <div class="cts-grid-table">
                                            <table class="table cts-sms-table">
                                                <colgroup>
                                                    <col style="width:110px" />
                                                    <col style="width:120px" />
                                                    <col style="width:60px" />
                                                </colgroup>
                                                <thead>
                                                    <tr>
                                                        <th scope="col">이름</th>
                                                        <th scope="col">번호</th>
                                                        <th scope="col">관리</th>
                                                    </tr>
                                                </thead>
                                                <tbody class="__tbody" id="_sms_popup_content_userList"><tr scope="empty"><td colspan="3">사용자를 선택해주세요.</td></tr></tbody>
                                            </table>
                                        </div>
                                    </div>
									<div style="width: calc(100% - 310px)">
										<div class="tableLeftResponsive">
											<table class="tableLeft">
												<colgroup>
													<col width="60px" />
													<col width="100px" />
													<col width="auto" />
												</colgroup>
												<tbody>
													<tr>
														<th scope="row" rowspan="3">SMS</th>
														<th scope="row">발신번호</th>
														<td>
															<select id="_sms_popup_input_telNum" class="common_select" data-list="telNums" />
														</td>
													</tr>
													<tr>
														<th scope="row">제목</th>
														<td>
															<input type="text" id="_sms_popup_input_title" />
														</td>
													</tr>
													<tr>
														<th scope="row">내용</th>
														<td>
															<textarea id="_sms_popup_input_content"></textarea>
															<span id="_sms_popup_text_maxlength"></span>
														</td>
													</tr>
												</tbody>
											</table>
										</div>
                                    </div>
								</div>
								<div class="pop_foot text-end">
									<button id="_sms_popup_button_cancel" class="btn btnDefault">취소</button>
									<button id="_sms_popup_button_send" class="btn btn-primary ms-2">전송하기</button>
								</div>
							</div>
                            <button id="_sms_popup_button_close" type="button" class="pop_close pop-close">닫기</button>
                        </div>
                    </article>`
	).get(0);

	// PREFIX
	const prefix = "_sms_popup";
	// PREFIX DOM ID 설정
	const prefixEl = "#"+prefix+"_";
	// 문자 입력 제한
	const maxLength = 1000;
	// 기능에서 사용될 ELEMENT DOM
	const Element = {
		wrap : _POPUP,  //전체
		text : {        //텍스트 관련
			selectMember : _POPUP.querySelector(prefixEl+"text_selectMember"),  // 선택인원
			maxlength : _POPUP.querySelector(prefixEl+"text_maxlength"),        // 글자제한
		},
		button  : {     //버튼
			callMember : _POPUP.querySelector(prefixEl+"button_callMember"),    // 주소록
			callTemplate : _POPUP.querySelector(prefixEl+"button_callTemplate"),//템플릿
			cancel : _POPUP.querySelector(prefixEl+"button_cancel"),            // 취소
			close : _POPUP.querySelector(prefixEl+"button_close"),              // 닫기
			send : _POPUP.querySelector(prefixEl+"button_send")                 // 전송
		},
		input : {       //입력폼
			telNum : _POPUP.querySelector(prefixEl+"input_telNum"),               // 발신번호
			title : _POPUP.querySelector(prefixEl+"input_title"),               // 제목
			content : _POPUP.querySelector(prefixEl+"input_content")            // 내용
		},
		content : {     //콘텐츠
			userList : _POPUP.querySelector(prefixEl+"content_userList")        //선택된 회원
		}
	};
	CommonUI.dataListSelectBox(Element.input.telNum)

	/** 이벤트 바인딩 **/
	// 내용(TEXTAREA)글자길이제한
	Element.input.content.addEventListener("keyup", function(e){
		cut_80(e.target);
	});
	// [주소록] 버튼
	Element.button.callMember.addEventListener("click", async function(e){
		e.preventDefault();
		// 회원정보를 팝업 호출
		let userList = await searchUser();
		userContoller.renderList(userList);
	});
	if (userList) {
		Element.button.callMember.style.display = 'none';
	}
	// [템플릿 불러오기] 버튼
	// Element.button.callTemplate.addEventListener("click", function(e){
	// 	e.preventDefault();
	// });
	// [취소] 버튼
	Element.button.cancel.addEventListener("click", function(e){
		e.preventDefault();
		closeModal();
	});
	// [X(닫기)] 버튼
	Element.button.close.addEventListener("click", function(e){
		e.preventDefault();
		closeModal();
	});
	// [전송] 버튼
	Element.button.send.addEventListener("click", function(e){
		e.preventDefault();
		let userList = userContoller.getUserList().map((userData)=>userData.phone);
		let sendNum = Element.input.telNum.value.trim();
		let sendMessageTitle = Element.input.title.value.trim();
		let sendMessageContent = Element.input.content.value.trim();
		if (userList.length > 0 && sendNum && sendMessageTitle && sendMessageContent) {
			if(confirm('전송하시겠습니까?')){
				// 전송 API 호출
				let params = {};
				params.sender = sendNum;
				params.receiver = userList;
				params.subject = sendMessageTitle;
				params.content = sendMessageContent;
				$.ajax({
					type: "post", //http method
					url: `/api/bizppurio/send-sms`, //값을 가져올 경로
					//url: `/api/bizppurio/send-lms`, //값을 가져올 경로
					headers: {'Content-Type': 'application/json'},
					data: JSON.stringify(params),
					dataType: "json",
					success: function(data) {
						window.location.reload();
					},
					error: function(e) { }
				})
			}
		} else alert("모든 입력칸에 값이 있는지 확인해주세요.")
	});

	// 닫기
	let closeModal = function(){
		$(Element.wrap).closeModal();
		$(Element.wrap).remove();
	}
	/** 유저 정보 Controller **/
	let userContoller = {
		list: [],
		countUser:function(){
			let selectMemberCount = Element.content.userList.querySelectorAll("[scope=sms-rciv-user]").length;
			Element.text.selectMember.innerText = selectMemberCount;
		},
		emptyCheck:function(){
			let selectMemberCount = Element.content.userList.querySelectorAll("[scope=sms-rciv-user]").length;
			if( !selectMemberCount ){
				$(Element.content.userList).append('<tr scope="empty"><td colspan="3">사용자를 선택해주세요.</td></tr>');
			}else{
				$(Element.content.userList).find("[scope=empty]").remove();
			}
		},
		renderList:function(userList = [], isEmpty){
			if( isEmpty ){
				userContoller.list = [];
				$(Element.content.userList).empty();
			}
			(userList || []).forEach(function(user){
				userContoller.addUser(user);
			});
			userContoller.emptyCheck();
			userContoller.countUser();
		},
		addUser:function(user){
			console.warn('유저리스트를 추가합니다.')
			let userItem = $(`<tr scope="sms-rciv-user"><td key="name">${user.userName}</td><td key="phone">${ctsUtil.phoneFomatter(user.phone)}</td><td><button class="btn btnDefault">삭제</button></td></tr>`)
			userItem.data("user",user);
			userItem.find("button").click(this.removeUser);
			Element.content.userList.appendChild(userItem.get(0));
			userContoller.countUser();
		},
		removeUser:function(e){
			e.target.parentElement.parentElement.remove();
			userContoller.emptyCheck();
			console.warn('유저리스트를 삭제합니다.');
			userContoller.countUser();
		},

		getUserList:function(e){
			let result = [];
			Element.content.userList.querySelectorAll("[scope=sms-rciv-user]").forEach(function(user){
				result.push($(user).data("user"));
			});
			return result;
		}
	}

	/** 문자 길이 제한 UTIL 함수**/
	let cut_80 = function (obj){
		var text = $(obj).val();
		var leng = text.length;
		while(getTextLength(text) > 1000){
			leng--;
			text = text.substring(0, leng);
		}/**/
		$(obj).val(text);
		$(Element.text.maxlength).text(`${getTextLength(text)}/${maxLength}`);
	}
	let getTextLength = function(str) {
		var len = 0;
		for (var i = 0; i < str.length; i++) {
			if (escape(str.charAt(i)).length == 6) {
				len++;
			}
			len++;
		}
		return len;
	}

	/** 메뉴 리스트 유저 정보 불러오기 **/
	if (userList && userList.length) {
		userList.forEach(user => {
			console.log(user.phone)
			userContoller.addUser(user);
			userContoller.emptyCheck();
		})
	}

	/** 실행 **/
	$(Element.wrap).appendTo(document.body).openModal();
}