function checkBox(){

    const all_check = document.getElementById("all_check");
    const checkItem = document.querySelectorAll(".chk_box.item_chk input");

    let checkList = [];

    // 전체 선택 해제
    all_check.addEventListener('click',function (){
        checkList = [];
        userList = [];
        if(this.checked){
            // 체크
            checkItem.forEach((item) => {
                checkList.push(item.id)
                if (item.dataset.userdata) userList.push(JSON.parse(item.dataset.userdata))
                item.checked = true;
            })
        } else {
            // 체크 해제
            checkItem.forEach((item) => {
                item.checked = false;
            })
        }
    })

    // 개별 아이템 선택
    checkItem.forEach((item) => {
        item.addEventListener('click', function (){
            if(this.checked){
                // 체크
                checkList.push(this.id);
                if (item.dataset.userdata) userList.push(JSON.parse(this.dataset.userdata));
                checkList.length === checkItem.length && (all_check.checked = true);
            } else {
                // 체크 해제
                checkList = checkList.filter((el) => el !== this.id)
                if (item.dataset.userdata) { 
                    userList = userList.filter((el) => { 
                        el !== this.dataset.userdata;
                    })
                } 
                all_check.checked = false;
            }
        })
    })
}