function searchJson(jsonQuery){
    if(Object.keys(jsonQuery).length > 0){
        let query = Object.entries(jsonQuery).map(e => e.join('=')).join('&');
        return window.location.assign(window.location.pathname+"?"+query)
    }else{
        window.location.assign(window.location.pathname)
    }
}

function searchKeyword(){
    if(window.location.search!=''){
        let searchParams = new URLSearchParams(window.location.search);
        let size = searchParams.get("size");
        if(size){
            $(`[data-list="countSortList"]`).val(size).prop("selected", true);
        } else {
            $(`[data-list="countSortList"]`).val(20).prop("selected", true);
        }
        searchParams.delete("size");
        searchParams.delete("page");
        for (const [key, value] of searchParams.entries()) {
            if( $(`input:text#${key}`).length>0){
                $(`input:text#${key}`).val(value);
            }else if($(`input:radio[name =${key}]`).length>0 ){
                $(`input:radio[name =${key}]:input[value=${value}]`).attr("checked", true);
            }else if($(`select#${key}`).length>0){
                $(`select#${key}`).val(value).prop("selected", true);
            }else if($(`input:checkbox[name =${key}]`).length>0){
                let temp = value.split(",");
                for(const e of temp){
                    $(`input:checkbox[name =${key}]:input[value=${e}]`).attr("checked", true);
                }
            }
        }
    } else {
        $(`[data-list="countSortList"]`).val(20).prop("selected", true);
    }
}

let PAGEABLE;
$(document).on("change",`[data-list="countSortList"]:not(.no-change-event)`, function (e){
    let newSize = $(this).val(); //변경할 리스트 size
    if (window.location.search=='') {
        return window.location.replace(window.location.pathname+`?size=${newSize}`);
    } else {
        let searchParams = new URLSearchParams(window.location.search);
        let originPage = searchParams.get("page");
        searchParams.delete("size");
        if ((originPage + 1) * newSize > PAGEABLE.totalElements) {
            searchParams.delete("page");
        }

        if (searchParams.size < 1) {
            return window.location.replace(window.location.pathname+`?size=${newSize}`);
        } else {
            return window.location.replace(window.location.pathname+`?size=${newSize}&`+searchParams.toString())
        }
    }
})

window.addEventListener('load', function () {searchKeyword()})
window.addEventListener('load', function () {
    $(".btnReset").bind('click',function (e){
        e.preventDefault();
        window.location.assign(window.location.pathname)
    })
    if($(".btnSearch").length >0){
        $("table").each((idx, table)=>{
            if($(table).hasClass("tableLeft")){
                $(table).find("input[type=text]").each((index, item)=>{
                    $(item).on("keyup",function(key){
                        if(key.keyCode==13) {
                            $(".btnSearch").trigger('click');
                        }
                    });
                })
            }
        })
        $(".sch_box").find("input[type=text]").each((index, item)=>{
            $(item).on("keyup",function(key){
                if(key.keyCode==13) {
                    $(".btnSearch").trigger('click');
                }
            });
        })
    }
})