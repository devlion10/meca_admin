'use strict';


$.fn.serializableList = function(options){
    let defaultOptions = {
        targetEl:"input",
        targetName:"name",
        targetValue:"value",
    }
    let _opt = Object.assign(defaultOptions, options);
    let result = [];
    $(this).children().each(function(idx, row){
        let item = {};
        let _inputs = $(row).find(_opt.targetEl);
        _inputs.each(function(idx,input){
            let name = input.getAttribute(_opt.targetName);
            let value = input.tagName == "INPUT" ? input[_opt.targetValue] : input.getAttribute(_opt.targetValue);
            if( name ) item[name] = value;
        });
        result.push(item);
    })
}


let ctsUtil = (function(){
    let util = {}
    const sleep = (milliseconds) => {
        const date = Date.now();
        let currentDate = null;
        do {
            currentDate = Date.now();
        } while (currentDate - date < milliseconds);
    };


    const asyncForEach = async (array, callback, delay) => {
        for (let index = 0; index < array.length; index++) {
            await callback(array[index], index, array);
        }
    }

    function download(req, res, next){
        const filePath = `${appRoot}/uploads/${req.params.filename}`;
        const fileName = encodeURI(req.originalFile.original_filename);
        res.setHeader("Content-Disposition", "attachment;filename=" + encodeURI(fileName));
        res.setHeader("Content-Type","binary/octet-stream");
        var fileStream = fs.createReadStream(filePath);
        fileStream.pipe(res);
    }

//날짜인지 아닌지 검사
    function isDate(date) {
        return (new Date(date) !== "Invalid Date") && !isNaN(new Date(date));
    }
//날짜 유효값넘기기
    function getDateWithNull(date){
        if(isDate(date)){
            return date;
        }else{
            return undefined;
        }
    }

    //문자열의 콤마를 제거하고 숫자로 변환
    function getNumString(s) {
        var rtn = s;
        if(typeof rtn ==="string"){
            rtn = parseFloat(s.replace(/,/gi, ""));
        }else if(typeof rtn ==="number"){
            rtn = parseFloat(s);
        }
        if (isNaN(rtn)) {
            return 0;
        }
        else {
            return rtn;
        }
    }


//문자열 반환
    function getString(val , defaultValue){
        return val ? val : defaultValue ? defaultValue : "";
    }


//숫자 천단위 콤마표시
    function numberWithCommas(x){
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    }

//mimetype으로 이미지 판별
    function mimeDetectImage(mimetype){
        return mimetype.includes("image");
    }
//파일용량 구하기
    function getfileSize(x) {
        var s = ['bytes', 'kB', 'MB', 'GB', 'TB', 'PB'];
        var e = Math.floor(Math.log(x) / Math.log(1024));
        return (x / Math.pow(1024, e)).toFixed(2) + " " + s[e];
    };

//하이픈 제거
    function removeHypen(str){
        try{
            return str.replace(/-/g,"");
        }catch(e){
            console.error(str, e);
        }
    }

    function removeSpace(str){
        try{
            return str.replace(/(\s*)/g,'');
        }catch(e){
            console.error(e);
        }
    }

    function removeHypenMobile(str){
        if( typeof str == "number"){
            if( str.toString().length == 10 ){
                return "0"+str;
            }else if( str.toString().length == 9 ){
                return "01"+str;
            }else if( str.toString().length == 8 ){
                return "010"+str;
            }
        }else{
            return removeHypen(removeSpace(str));
        }
    }


//개행문자 변환
    function nl2br(str){
        if (str == undefined || str == null){
            return "";
        }
        str = str.replace(/\r\n/ig, '<br>');
        str = str.replace(/\\n/ig, '<br>');
        str = str.replace(/\n/ig, '<br>');
        return str;
    }

    function mathAbs(num){
        if(Math.abs(num) === 0){
            return `<span class='math-abs abs-zero'>0</span>`
        }else if(num > 0){
            return `<span class='math-abs abs-plus'>${Math.abs(num)}</span>`
        }else{
            return `<span class='math-abs abs-minus'>${Math.abs(num)}</span>`
        }
    }
    function mathAbsNumberWithCommas(num){
        if(Math.abs(num) === 0){
            return `<span class='math-abs abs-zero'>0</span>`
        }else if(num > 0){
            return `<span class='math-abs abs-plus'>${numberWithCommas(Math.abs(num))}</span>`
        }else{
            return `<span class='math-abs abs-minus'>${numberWithCommas(Math.abs(num))}</span>`
        }
    }

// 파일읽기
    async function getFileJson(jsonPath){
        return new Promise((resolve, reject)=>{
            fs.readFile(__dirname+"/"+jsonPath, (err, data) => { // 파일 읽기
                if (err) throw err;
                resolve( JSON.parse(data) )
            });
        })
    }


    /**
     * 폴더 생성
     * @param {*} dirPath
     */
    function mkdir( dirPath ) {
        const isExists = fs.existsSync( dirPath );
        if( !isExists ) {
            fs.mkdirSync( dirPath, { recursive: true } );
        }
    }

    /**
     * 해당경로가 파일인지 폴더인지
     * @param {*} path
     * @returns
     */
    function isFile(path) {
        var stats = fs.statSync(path);
        return stats.isFile();
    }


    /**
     * 유효한 사업자 등록번호인지 검사
     * @param {*} number
     * @returns
     */
    function checkCorporateRegiNumber(number){
        var numberMap = number.replace(/-/gi, '').split('').map(function (d){
            return parseInt(d, 10);
        });
        if(numberMap.length == 10){
            var keyArr = [1, 3, 7, 1, 3, 7, 1, 3, 5];
            var chk = 0;
            keyArr.forEach(function(d, i){
                chk += d * numberMap[i];
            });
            chk += parseInt((keyArr[8] * numberMap[8])/ 10, 10);
            return Math.floor(numberMap[9]) === ( (10 - (chk % 10) ) % 10);
        }
        return false;
    }


    /**
     * 사업자등록번호 하이픈 붙이기
     * @param {*} num 사업자등록번호
     * @param {*} type 0 : 123-45-*****, 1: 123-45-67890
     * @returns
     */
    function bizNoFormatter(num, type) {
        var formatNum = '';
        try{
            if (num.length == 10) {
                if (type == 0) {
                    formatNum = num.replace(/(\d{3})(\d{2})(\d{5})/, '$1-$2-*****');
                }else{
                    formatNum = num.replace(/(\d{3})(\d{2})(\d{5})/, '$1-$2-$3');
                }
            }
        }catch(e){
            formatNum = num;
            console.log(e);
        }
        return formatNum;
    }


    /**
     * (주) 제거 법인명 변환
     * @param {법인명} nm
     */
    function getCompanyName(nm){
        return nm.replace('(주)', '').replace('㈜', '').trim();
    }



// 법인등록번호 유효성 체크
    function isBubinNo(no) {

        // -제거
        no = no.split('-').join('');

        var as_Biz_no = String(no);
        var isNum = true;
        var I_TEMP_SUM = 0 ;
        var I_TEMP = 0;
        var S_TEMP;
        var I_CHK_DIGIT = 0;

        if (no.length != 13) {
            return false;
        }

        for (var index01 = 1; index01 < 13; index01++) {
            var i = index01 % 2;
            var j = 0;

            if(i == 1) j = 1;
            else if( i == 0) j = 2;

            I_TEMP_SUM = I_TEMP_SUM + parseInt(as_Biz_no.substring(index01-1, index01),10) * j;
        }

        I_CHK_DIGIT= I_TEMP_SUM%10;
        if (I_CHK_DIGIT != 0 ) I_CHK_DIGIT = 10 - I_CHK_DIGIT;

        if (as_Biz_no.substring(12, 13) != String(I_CHK_DIGIT)) return false;

        return true;

    }

// 사업자등록번호 유효성 체크
    function isCorporateRegiNo(no) {

        var numberMap = no.replace(/-/gi, '').split('').map(function (d){
            return parseInt(d, 10);
        });

        if (numberMap.length == 10) {

            var keyArr = [1, 3, 7, 1, 3, 7, 1, 3, 5];
            var chk = 0;

            keyArr.forEach(function(d, i){
                chk += d * numberMap[i];
            });

            chk += parseInt((keyArr[8] * numberMap[8])/ 10, 10);
            return Math.floor(numberMap[9]) === ( (10 - (chk % 10) ) % 10);

        }

        return false;

    }


    function checkNull(str, defaultValue = null){
        return ( str ) ? str : defaultValue;
    }
    function checkNullDate(str , format ="YYYY/MM/DD hh:mm:ss"){
        return ( str ) ? moment(str).format(format) : null;
    }


    function csvToJSON(csv_string){

        // 1. 문자열을 줄바꿈으로 구분 => 배열에 저장
        const rows = csv_string.split("\r\n");

        // 줄바꿈을 \n으로만 구분해야하는 경우, 아래 코드 사용
        // const rows = csv_string.split("\n");


        // 2. 빈 배열 생성: CSV의 각 행을 담을 JSON 객체임
        const jsonArray = [];


        // 3. 제목 행 추출 후, 콤마로 구분 => 배열에 저장
        const header = rows[0].split(",");


        // 4. 내용 행 전체를 객체로 만들어, jsonArray에 담기
        for(let i = 1; i < rows.length; i++){

            // 빈 객체 생성: 각 내용 행을 객체로 만들어 담아둘 객체임
            let obj = {};

            // 각 내용 행을 콤마로 구분
            let row = rows[i].split(",");

            // 각 내용행을 {제목1:내용1, 제목2:내용2, ...} 형태의 객체로 생성
            for(let j=0; j < header.length; j++){
                obj[header[j]] = row[j];
            }

            // 각 내용 행의 객체를 jsonArray배열에 담기
            jsonArray.push(obj);

        }

        // 5. 완성된 JSON 객체 배열 반환
        return jsonArray;

        // 문자열 형태의 JSON으로 반환할 경우, 아래 코드 사용
        // return JSON.stringify(jsonArray);
    }



    function phoneFomatter(num,type){
        var formatNum = '';
        if(num.length==11){
            if(type==0){
                formatNum = num.replace(/(\d{3})(\d{4})(\d{4})/, '$1-****-$3');
            }else{
                formatNum = num.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3');
            }
        }else if(num.length==8){
            formatNum = num.replace(/(\d{4})(\d{4})/, '$1-$2');
        }else{
            if(num.indexOf('02')==0){
                if(type==0){
                    formatNum = num.replace(/(\d{2})(\d{4})(\d{4})/, '$1-****-$3');
                }else{
                    formatNum = num.replace(/(\d{2})(\d{4})(\d{4})/, '$1-$2-$3');
                }
            }else{
                if(type==0){
                    formatNum = num.replace(/(\d{3})(\d{3})(\d{4})/, '$1-***-$3');
                }else{
                    formatNum = num.replace(/(\d{3})(\d{3})(\d{4})/, '$1-$2-$3');
                }
            }
        }
        return formatNum;
    }


    function convertUTF8(filename){
        return Buffer.from(filename, 'latin1').toString('utf8');
    }


    function windowOpen(url, title, width=800, height=600){
        return window.open(url, title, `menubar=no, toolbar=no, width=${width}, height=${height}`);
    }
    util.windowOpen = windowOpen;
    util.phoneFomatter = phoneFomatter;
    util.convertUTF8 = convertUTF8;
    util.csvToJSON = csvToJSON;
    util.removeHypenMobile = removeHypenMobile;
    util.removeSpace = removeSpace;
    util.checkNull = checkNull;
    util.checkNullDate = checkNullDate;
    util.isBubinNo = isBubinNo;
    util.isCorporateRegiNo = isCorporateRegiNo;
    util.getCompanyName = getCompanyName;
    util.bizNoFormatter = bizNoFormatter;
    util.checkCorporateRegiNumber = checkCorporateRegiNumber;
    util.mkdir = mkdir;
    util.isFile = isFile;
    util.sleep = sleep;
    util.getFileJson = getFileJson;
    util.nl2br = nl2br;
    util.mathAbs = mathAbs;
    util.mathAbsNumberWithCommas = mathAbsNumberWithCommas;
    util.numberWithCommas = numberWithCommas;
    util.mimeDetectImage = mimeDetectImage;
    util.getfileSize = getfileSize;
    util.getDateWithNull = getDateWithNull;
    util.getNumString = getNumString;
    util.isDate = isDate;
    util.download = download;
    util.getString = getString;
    util.asyncForEach = asyncForEach;
    util.removeHypen = removeHypen;
    return util;
})()



