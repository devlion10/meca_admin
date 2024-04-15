var context = window,
	$win = $(context),
	$body = $('body'),
	$doc = $(document);

(function($) {
	"use strict";
	// off, on

	$.fn.serializeObject = function() {
		var obj = null;
		try {
			if (this[0].tagName && this[0].tagName.toUpperCase() == "FORM") {
				var arr = this.serializeArray();
				if (arr) {
					obj = {};
					jQuery.each(arr, function() {
						obj[this.name] = this.value;
					});
				}//if ( arr ) {
			}
		} catch (e) {
			alert(e.message);
		} finally {
		}

		return obj;
	};

	$.fn.offon = function(type, f) {
		return this.off(type).on(type, f);
	};
	
	// length
	$.fn.exists = function() {
		return this.length > 0;
	};
	
	// space ([margin, padding], [horizontal, vertical, top, right, bottom, left])
	$.fn.space = function(property, option) {
		var property = String(property),
			option = String(option),
			value = 0;
		
		if(option == "horizontal") {
			if($(this).css(property + "-left") && $(this).css(property + "-left").replace(/\D/g,"")) {
				value += Number($(this).css(property + "-left").replace(/\D/g, ""));
			}
			if($(this).css(property + "-right") && $(this).css(property + "-right").replace(/\D/g,"")) {
				value += Number($(this).css(property + "-right").replace(/\D/g, ""));
			}
		} else if(option == "vertical") {
			if($(this).css(property + "-top") && $(this).css(property + "-top").replace(/\D/g,"")) {
				value += Number($(this).css(property + "-top").replace(/\D/g, ""));
			}
			if($(this).css(property + "-bottom") && $(this).css(property + "-bottom").replace(/\D/g,"")) {
				value += Number($(this).css(property + "-bottom").replace(/\D/g, ""));
			}
		} else {
			if($(this).css(property + "-" + option) && $(this).css(property + "-" + option).replace(/\D/g,"")) {
				value = Number($(this).css(property + "-" + option).replace(/\D/g, ""));
			}
		}
		
		return value;
	};

	$.fn.noop = function () {
		return this;
	};

}(jQuery));


(function ($) {
	"use strict";
	var $root = $(document.documentElement).addClass('js'),
		isTouch = ('ontouchstart' in context),
		isMobile = ('orientation' in context) || isTouch || window.IS_MOBILE === true;
	
	isTouch && $root.addClass('touch');
	isMobile && $root.addClass('mobile');

	// 모바일 디바이스
	window.IS_MOBILE = window.IS_MOBILE;
	window.isTouch = isTouch;

	// 해상도별 사이즈 기준이 변하는 시점에 changemediasize라는 이벤트를 별도로 발생시킨다.
	$win.on('resize.changemediasize', (function () {
		var sizes = [{
			mode: 'mobile',
			min: 0,
			max: 767
		},
		{
			mode: 'tablet',
			min: 768,
			max: 1024
		},{
			mode: 'desktop',
			min: 1025,
			max: 100000
		}],
		handleChangeMediaSize;

		handleChangeMediaSize = function () {
			var w = $win.width();

			
			
			for (var i = 0, size; size = sizes[i]; i++) {
				if (w > size.min && w <= size.max) {
					size.width = w;
					// 반응형일 때만 body에 클래스 토글
					switch (size.mode) {
						case 'desktop':
							$('body').removeClass('mobile tablet').addClass('desktop');
							break;
						case 'tablet':
							$('body').removeClass('desktop mobile').addClass('tablet');
							break;
						case 'mobile':
							$('body').removeClass('desktop tablet').addClass('mobile');
							break;
					}

					$win.trigger('changemediasize', false);
					break;
				}
			}
		};

		// 초기에 한번 실행
		$(function() {
			handleChangeMediaSize();
		});

		
		return handleChangeMediaSize;
	})());

	window.consts = {
		MOBILE_SIZE: 767,
		TABLET_SIZE: 1024
	};

	window.isMobileMode = window.isMobileSize = function (w) {
		if (w === undefined) {
			w = $win.width();
		}

		return window.IS_MOBILE === true || w < window.consts.TABLET_SIZE;
	};

	window.isTouchMode = function () {
		var ag = (/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini|Mobi|mobi/i.test(navigator.userAgent));
		
		return window.isTouch === true && ag;
	};
})(jQuery);


$(document).ready(function(){
	var ui = PUB.ui;
	var layout = PUB.layout;

	layout.setLayout();
	layout.initEvent();
	
	ui.accordionMenu();
	ui.ModalController();
	ui.tabPanel();

	// tab
	if($('.js_tab').exists()){
		$('.js_tab', this).tabPanel({
			startIndex:0
		});
	}

	// tab scroll
	if($('#navTab2').exists()){
		var navTab = new IScroll('#navTab2', {
			scrollX: true,
			scrollY: false
		});
	}

	// popup bg
	$('#popQuesView').openModal();

	// datePicker
	if($(".el_datePicker").exists()){
		$(".el_datePicker").each(function() {
			let param = {};
			if ($(this).data("yearrange")) {
				param.yearRange = $(this).data("yearrange");
			} else {
				param.yearRange = 'c-50:c+20';
			}

			$(this).datepicker(Object.assign({
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
				onSelect: function (date, inst) {
					const selectedYear = $('.ui-datepicker-year').val();
					if (inst.selectedYear != selectedYear) {
						$(this).datepicker("setDate", new Date(selectedYear, inst.selectedMonth, inst.selectedDay));
						$(this).blur(); // focus 해제
					}

					if( typeof clickDate == 'function'){
						// 함수가 선언이 되어있을 경우에만 메인에서만 씀
						if( closePopCalList && typeof closePopCalList == "function") closePopCalList();
						clickDate(date.split('-')[0]+date.split('-')[1]+date.split('-')[2])
					}
				}
			}));
		});

		// sDate - eDate 보완
		for(let i=0; i<$('.sdate').length; i++){
			$('.sdate').eq(i).datepicker();
			$('.sdate').eq(i).datepicker("option", "maxDate",$(".edate").eq(i).val());
			$('.sdate').eq(i).datepicker("option", "onClose", function ( selectedDate ) {
				$(".edate").eq(i).datepicker( "option", "minDate", selectedDate );
				// $(".bizOrgAplyLsnDtlYmd").each((index,item) =>{
				// 	$(item).datepicker( "option", "minDate", selectedDate );
				// });
			});

			$('.edate').eq(i).datepicker();
			$('.edate').eq(i).datepicker("option", "minDate", $(".sdate").eq(i).val());
			$('.edate').eq(i).datepicker("option", "onClose", function ( selectedDate ) {
				$(".sdate").eq(i).datepicker( "option", "maxDate", selectedDate );
				// $(".bizOrgAplyLsnDtlYmd").each((index,item) =>{
				// 	$(item).datepicker( "option", "maxDate", selectedDate )
				// });
			});
		}

		//datepicker 자동완성 기능 해제
		for(let i=0; i<$('.el_datePicker').length; i++){
			$('.el_datePicker').eq(i).attr('autocomplete', 'off');
		}
	}
});

window.addEventListener('load', function () {

	function setActiveMenu(el){
		let childrenBtn;
		let parentSeq = el.data("parentseq");
		let parentEl = $("[data-seqmenu="+parentSeq+"]");
		el.attr("aria-expanded", true).removeClass("collapsed");

		if( parentEl ){
			parentEl.children("div, ul").addClass("show");
			parentEl.children("button, a").attr("aria-expanded", true).removeClass("collapsed");
			childrenBtn = parentEl.children("button, a");
			console.warn("childrenBtn : " , childrenBtn);
		}
		return childrenBtn;
	}


	// sidebar + gnb active
	let targetUri = window.location.pathname;
	let tempUri = targetUri.split("/");
	targetUri = tempUri.slice(0,3).join("/");
	let findMenu = $('.gnb-sidebar [href="' + targetUri + '"]');
	if( !findMenu.length ){
		console.warn("없습니다..")
		targetUri = tempUri.slice(0,4).join("/");
		findMenu = $('.gnb-sidebar [href="' + targetUri + '"]');
	}

	if( findMenu ){
		findMenu.addClass("active");
		let ret = setActiveMenu(findMenu);
		ret && setActiveMenu(ret);
	}



	// 숫자만 입력 가능하도록 하는 함수
	function validateNumberInput(e) {
		// 입력값에서 숫자를 제외한 모든 문자를 삭제
		const newValue = e.target.value.replace(/[^\d]/g, '');
		// 입력값이 변경되었으면 입력값을 갱신
		if (newValue !== e.target.value) {
			e.target.value = newValue;
		}
	}
	document.querySelectorAll(".numberInput").forEach(item => {
		item.addEventListener("input", validateNumberInput);
	})
});

// 빈 배열 체크 함수
function isEmptyArr(arr)  {
	if(Array.isArray(arr) && arr.length === 0)  {
		return true;
	}
	
	return false;
}