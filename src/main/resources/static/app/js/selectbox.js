(function(_globalInstance){
    //초기화
    if( !_globalInstance ) _globalInstance = {};

    /* 공통 select data ----------- */
    //년도
    const yearList = [];
    const yearListValue = [];
    const startYear = 2010;
    const currentYear = new Date().getFullYear();
    yearList.push('년도');
    yearListValue.push("");
    for (let i = currentYear + 1; i >= startYear; i--) {
        yearList.push(i+'년');
        yearListValue.push(i);
    }

    //월
    const monthList = [];
    const monthListValue = [];
    monthList.push('월');
    monthListValue.push("");
    for (let i = 1; i <= 12; i++) {
        monthList.push(i+'월');
        let item = String(i);
        item = item.padStart(2, "0");
        monthListValue.push(item);
    }

    //차수
    const numList = [];
    const numListValue = [];
    const listNum = 20;
    numList.push('차수');
    numListValue.push("");
    for (let i = 1; i <= listNum; i++) {
        numList.push(i+'차');
        numListValue.push(i);
    }

    //기수
    const numList2 = [];
    const numList2Value = [];
    const listNum2 = 20;
    numList2.push('기수');
    numList2Value.push("");
    for (let i = 1; i <= listNum2; i++) {
        let item = String(i);
        item = item.padStart(2, "0");
        numList2.push(item);
        numList2Value.push(item);
    }

    //시
    const hourList = [];
    const hourListValue = [];
    hourList.push('시');
    hourListValue.push("");
    for (let i = 0; i <= 23; i++) {
        let hour = String(i);
        hour = hour.padStart(2, "0");
        hourList.push(hour);
        hourListValue.push(hour);
    }

    //분
    const minuteList = [];
    const minuteListValue = [];
    minuteList.push('분');
    minuteListValue.push("");
    for (let i = 0; i <= 11; i++) {
        let minute = String(i*5);
        minute = minute.padStart(2, "0");
        minuteList.push(minute);
        minuteListValue.push(minute);
    }

    //정렬순 - 리스트 개수
    const countSortList = ['10개씩','20개씩','30개씩','50개씩','100개씩'];
    const countSortListValue = [10,20,30,50,100];

    //제출처
    const submissionList = ['제출처','서울','세종','경남','인천','대전','부산','대구','울산','광주','경기','강원','충북','충남','전북','전남','경북','제주','헤외'];

    //검색(제목, 작성자)
    const schList = ['전체','제목','작성자'];

    //지역
    const regionList = ['지역','서울','세종','경남','인천','대전','부산','대구','울산','광주','경기','강원','충북','충남','전북','전남','경북','제주','해외'];

    //지사
    const branchList = ['본사','세종대전지사','부산지사','광주지사','대구지사'];
    const branchListValue = ['HEAD','SEDA','BU','GW','DA'];

    //이메일
    const emailList = ['선택','naver.com','daum.net','hanmail.net','gmail.com','nate.com','hotmail.com','직접입력'];

    //전화번호
    const telList = ['02','031','032','033','041','042','043','044','051','052','053','054','055','061','062','063','064','070','0504','0505'];

    //핸드폰번호
    const phoneList = ['010','011','016','017','018','019'];

    //은행
    const bankList = ['선택','기업은행','국민은행','농협중앙회','단위농협','우리은행','대구은행','외환은행','SC제일은행','부산은행','새마을금고','한국씨티은행','광주은행','경남은행','수협','신협','전북은행','제주은행','산림조합','우체국','하나은행','신한은행','동양종금증권','한국투자증권','삼성증권','미래에셋','우리투자증권','현대증권','SK증권','신한금융투자','하이증권','HMC증권','대신증권','하나대투증권','동부증권','유진증권','메리츠증권','신영증권','대우증권'];

    //사업 템플릿 유형
    //id: bizPbancType
    const businessTemp = ['사업 유형','기본형','미디어교육 평생교실','미디어교육 운영학교','자유학기제 미디어교육지원','팩트체크 교실','자유형'];
    const businessTempValue = ['','0','1','2','3','4','5'];

    //매체사 대분류
    const mediaType1 = [];
    const mediaType2 = ["방송","케이블SO","신문","지역일간","인터넷","인터넷신문","일반주간","전문주간","온라인신문","기타","인터넷","잡지","잡지","케이블PP","지상파-공영","지역주간","지상파-민영","통신","통신","지상파-특수방송","경제일간","전국종합일간","기타","전문일간","영자일간"];
    const mediaArea = ["서울특별시","부산광역시","대구광역시","인천광역시","광주광역시","대전광역시","울산광역시","경기도","강원도","충청북도","충청남도","전라북도","전라남도","경상북도","경상남도","제주도","세종특별자치시","전국"];
    //사업 검색 포함 유형
    //id: containTextType
    const businessContainTextType = ['전체', '사업명', '사업내용'];
    const businessContainTextTypeValue = ['1', '2', '3'];

    //비즈뿌리오 발송 번호
    const sendTelItems = ['선택', '7211', '7212', '7214', '7215', '7216', '7217', '7218', '7221', '7223', '7231', '7232', '7233', '7234', '7235', '7237', '7238', '7239', '7242', '7243', '7222', '7236', '4713', '4718', '4719', '2116', '2117', '2373', '2849'];
    const sendTelNums = ['', '0220017211', '0220017212', '0220017214', '0220017215', '0220017216', '0220017217', '0220017218', '0220017221', '0220017223', '0220017231', '0220017232', '0220017233', '0220017234', '0220017235', '0220017237', '0220017238', '0220017239', '0220017242', '0220017243', '0220017222', '0220017236', '0425274713', '0425274718', '0425274719', '0537632117', '0537632116', '0626522373', '0517592849'];

    function _dataListSelectBox(item){
        if (item.getAttribute('data-list') != null) {
            let dataList = item.getAttribute('data-list');
            let dataListValue;
            switch(dataList) {
                case "yearList":
                    dataList = yearList;
                    dataListValue = yearListValue;
                    break;
                case "monthList":
                    dataList = monthList;
                    dataListValue = monthListValue;
                    break;
                case "numList":
                    dataList = numList;
                    dataListValue = numListValue;
                    break;
                case "numList2":
                    dataList = numList2;
                    dataListValue = numList2Value;
                    break;
                case "hourList":
                    dataList = hourList;
                    dataListValue = hourListValue;
                    break;
                case "minuteList":
                    dataList = minuteList;
                    dataListValue = minuteListValue;
                    break;
                case "countSortList":
                    dataList = countSortList;
                    dataListValue = countSortListValue;
                    break;
                case "submissionList":
                    dataList = submissionList;
                    dataListValue = submissionList;
                    break;
                case "schList":
                    dataList = schList;
                    dataListValue = schList;
                    break;
                case "regionList":
                    dataList = regionList;
                    dataListValue = JSON.parse(JSON.stringify(regionList));
                    dataListValue[0] = "";
                    break;
                case "branchList":
                    dataList = branchList;
                    dataListValue = branchListValue;
                    break;
                case "emailList":
                    dataList = emailList;
                    dataListValue = emailList;
                    break;
                case "telList":
                    dataList = telList;
                    dataListValue = telList;
                    break;
                case "phoneList":
                    dataList = phoneList;
                    dataListValue = phoneList;
                    break;
                case "bankList":
                    dataList = bankList;
                    dataListValue = bankList;
                    break;
                case "businessTemp":
                    dataList = businessTemp;
                    dataListValue = businessTempValue;
                    break;
                case "businessContainTextType":
                    dataList = businessContainTextType;
                    dataListValue = businessContainTextTypeValue;
                    break;
                case "mediaArea":
                    dataList = mediaArea;
                    dataListValue = mediaArea;
                    break;
                case "telNums":
                    dataList = sendTelItems;
                    dataListValue = sendTelNums;
                    break;
            }
        
            //select option 맨 처음 값 지정
            // let dataListStart = item.getAttribute('data-list-start');
            // if (dataListStart != null) {
            //     if (flag) {
            //         //같은 select 2개 이상일 경우 맨 처음 값 삭제
            //         dataList.shift();
            //     } else {
            //         //select 별 맨 처음 지정 값 삭제
            //         switch(dataList) {
            //             case regionList:
            //                 dataList.shift();
            //         }
            //     }
            //     //맨 처음 값 추가
            //     dataList.unshift(dataListStart);
            //     flag = true;
            // }
        
            for (let i = 0; i < dataList.length; i++) { 
                let option = document.createElement("option");
                option.value = dataListValue[i];
                option.text = dataList[i];
                item.appendChild(option); 
            }
        }

    }

    $(document).ready(function () {
        /* select option 생성 ----------- */
        let __select = document.querySelectorAll('select[class*="common_select"]');
        let flag = false;
        if (__select.length > 0) {
            __select.forEach(item => {
                _dataListSelectBox(item);
            });
        }
    });
    
    _globalInstance.dataListSelectBox = _dataListSelectBox;
})(CommonUI)

/* select 렌더링 후 selected 처리 ----------- */
function setSelected(val, targetId) {
    const target = document.getElementById(targetId);
    const len = target.options.length;

    for (let i = 0; i < len; i++) {
        //option value가 입력 받은 value 값과 일치할 경우 selected
        if (target.options[i].value == val){
            target.options[i].selected = true;
        }
    }  
}

/* 이메일 select option '직접입력' 처리 ----------- */
function domainWriteChk() {
    if (document.querySelector('#email3').value == '직접입력') {
        document.querySelector('#email2').value = '';
        document.querySelector('#email2').setAttribute('placeholder', '직접입력');
    } else {
        document.querySelector('#email2').removeAttribute('placeholder');
    }
}