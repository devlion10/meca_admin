const addLine = (addLineBtn, addBtnId, cloneId) => {
	let addNum = addLineBtn.previousElementSibling.value;
	let addBtns = document.querySelectorAll('#'+addBtnId);
	if (addBtns.length > 1) {
		for (let i = 0; i < addNum; i++) {
			addList(addBtns[addBtns.length - 1], cloneId)
		}
	} else {
		for (let i = 0; i < addNum; i++) {
			addList(document.getElementById(addBtnId), cloneId)
		}
	}
}