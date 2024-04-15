/* 추가 ----------- */
let __list;

const addList = (addBtn, cloneId, key=[], numId) => {
	// node 선택
	let tmpObj;
	if(key.length > 0) {
		tmpObj = {};
		key.forEach(item => {
			tmpObj[item] = ''
		})

		__list.push(tmpObj)
	}

	let maxNum;
	//리스트 추가시 넘버링 (최댓값 찾기)
	if (numId) {
		const numList = document.querySelectorAll('#' + numId);
		const numValueList = [];
		numList.forEach(item => {
			numValueList.push(Number(item.value))
		});
		maxNum = Math.max.apply(null, numValueList);
	}

	let cloneDiv = document.querySelectorAll('#'+cloneId);
	let btnOnly = false;
	cloneDiv.forEach(item => {
		// 1. '추가' 버튼이 컬럼 내부에 있는 경우 (테이블의 각 컬럼마다 추가 버튼 존재)
		// 선택한 '추가' 버튼을 가지고 있는 cloneDiv(tr) 선택
		if (item.querySelector('.btn_add') == addBtn) {
			// 노드 복사하기 (deep copy)
			let newDiv = item.cloneNode(true);

			// input 값 초기화
			let input = newDiv.querySelectorAll('input');
			input.forEach(item => {
				item.setAttribute("value", "");
			})
		
			// select 값 초기화
			let select = newDiv.querySelectorAll('select');
			select.forEach(item => {
				for (let i = 0; i < item.options.length; i++) {
					item.options[i].removeAttribute('selected');
				}
		
				item.options[0].setAttribute('selected', true);
			})
		
			// 복사한 노드 붙여넣기
			item.after(newDiv);

			if (numId) {
				// 리스트 추가시 넘버링 (최댓값 + 1)
				const newNumId = newDiv.querySelector('#' + numId);
				newNumId.setAttribute("value", maxNum + 1);
			}
		}

		// 2. '추가' 버튼이 컬럼 외부에 있는 경우 (테이블 하나에 추가 버튼 1개 존재)
		if (item.querySelector('.btn_add') == null) {
			btnOnly = true;
		}
	})

	if (btnOnly) {
		// 노드 복사하기 (deep copy)
		let newDiv = cloneDiv[cloneDiv.length - 1].cloneNode(true);
	
		// input 값 초기화
		let input = newDiv.querySelectorAll('input');
		input.forEach(item => {
			item.value = '';
		})
	
		// select 값 초기화
		let select = newDiv.querySelectorAll('select');
		select.forEach(item => {
			for (let i = 0; i < item.options.length; i++) {
				item.options[i].removeAttribute('selected');
			}
	
			item.options[0].setAttribute('selected', true);
		})
	
		// 복사한 노드 붙여넣기
		cloneDiv[cloneDiv.length - 1].after(newDiv);

		
		// datepicker 이벤트 추가 함수
		function addDatePickerEvent(input) {
			CommonUI.datePicker($(input))
		}
		
		// 복사된 .el_datePicker 요소에 datepicker 이벤트 새로 추가
		let el = newDiv.querySelectorAll('.el_datePicker');
		el.forEach(item => {
			addDatePickerEvent(item);
		})
		
		if (numId) {
			// 리스트 추가시 넘버링 (최댓값 + 1)
			const newNumId = newDiv.querySelector('#' + numId);
			newNumId.setAttribute("value", maxNum + 1);
		}
	}
}

/* 삭제 ----------- */
const removeList = (delBtn, targetId, table) => {


	let btnList;
	let target = document.querySelectorAll('#'+targetId);
	
	if (table == null) {
		btnList = document.querySelectorAll('.btn_del');
	} else {
		btnList = document.getElementById(table).querySelectorAll('.btn_del');
	}

	if (btnList.length == 1) { // 삭제 버튼 1개일 때(테이블 리스트 1개일 때)
		return false;
	}

	target.forEach((item,index) => {
		if (item.querySelector('.btn_del') == delBtn) {
			item.remove();
			if(__list && __list.length > 0) {
				__list.splice(index, 1);
			}
		}
	})

	// 테이블 여러개인 경우 예외처리 필요
	// let table = delBtn.parentElement.parentElement.parentElement.parentElement;
}