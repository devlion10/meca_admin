// input에 not_null="true" alert_name="담당자 이름" 추가하면 자동으로 비어있는지 검사해서 담당자이름을 입력해주세요라고 안내창
function nullCheck(){
    // let emailReg = new RegExp('[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\.[a-zA-Z]{2,3}');
    let emailReg = new RegExp('[0-9a-zA-Z+-_.]+@[0-9a-zA-Z-_.]+.[a-zA-Z]{2,3}');
    let result = true ;
    $("[not_null]").each((index, item)=>{
        if($(item).val()==""){
            alert($(item).attr("alert_name") +"을(를) 입력해 주세요.");
            result = false
            return result;
            // 전화번호 정규식
        } else if($(item).attr("reg_type") =="number"){
            if($(item).val().replace(/(^02.{0}|^01.{1}|^050.{1}|^[0-9]{3})([0-9]{3,4})([0-9]{4})/, "$1-$2-$3").indexOf('-') == -1){
                alert($(item).attr("alert_name") +"가 올바르지 않은 형태입니다. ");
                result = false
                return result;
            }
            //이메일 정규식
        } else if($(item).attr("reg_type") =="email"){
            if(!emailReg.test($(item).val())){
                alert($(item).attr("alert_name") +"은 올바르지 않은 이메일 형태입니다.");
                result = false
                return result;
            }
            //날짜 정규식
        } else if($(item).attr("reg_type") =="date"){
            if(isNaN(Date.parse($(item).val()))){
                alert($(item).attr("alert_name") +"의 날짜가 올바르지 않은 형태입니다.");
                result = false;
                return result;
            }
            //숫자 정규식
        } else if($(item).attr("reg_type") =="int"){
            if(isNaN(parseInt($(item).val())) || ($(item).val().indexOf('.') !=-1)){
                alert($(item).attr("alert_name") +" 항목은 숫자만 입력해 주세요.");
                result = false;
                return result;
            }
        }
    })

    return result;
}



function maxCheck(item) {
    let maxlength = $(item).attr("maxlength")?$(item).attr("maxlength"):false;
    if (maxlength && item.value.length >= maxlength) {
        alert(`이 칸에 입력할 수 있는 최대 글자수는 ${maxlength}자 입니다.`);
    }
}