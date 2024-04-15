function upRow(e, rowId){
    const allRow = document.querySelectorAll('#'+rowId);
    let id_x;
    allRow.forEach((item, index) => {
        if(item === e.parentElement.parentElement){
            id_x = index
        }
    })

    if(id_x === 0){
        alert("이미 맨 처음에 있는 항목입니다.")
    } else {
        let tmp = __list[id_x];

        __list[id_x] = __list[id_x-1];
        __list[id_x-1] = tmp;
        drawRow(rowId);
    }
}

function downRow(e, rowId){
    const allRow = document.querySelectorAll('#'+rowId);
    let id_x;
    allRow.forEach((item, index) => {
        if(item === e.parentElement.parentElement){
            id_x = index
        }
    })

    if(id_x === (allRow.length-1)){
        alert("이미 맨 마지막에 있는 항목입니다.")
    } else {
        let tmp = __list[id_x];

        __list[id_x] = __list[id_x+1];
        __list[id_x+1] = tmp;
        drawRow(rowId);
    }
}