window.addEventListener('load', function () {
    // let fileList = [];
    let bl_uploadGrid2 = document.querySelectorAll('.bl_uploadGrid2');
    let fileWrap = document.querySelectorAll('.js_upload');

    if (bl_uploadGrid2.length > 0) {
        bl_uploadGrid2.forEach((item, idx) => {
            let fileTarget = item.querySelector('.js_file');
            let fileDelBtn = item.querySelector('.bl_uploadGrid2_del');
            if( fileTarget )fileTarget.addEventListener('change', fileUpload);
            if( fileDelBtn ) fileDelBtn.addEventListener('click', fileDelete);

            function fileUpload() {
                let fileName;
                if (fileTarget.files.length < 2) {
                    fileName = fileTarget.files[0].name;
                } else { 
                    fileName = '파일 ' + fileTarget.files.length + '개';
                }
                let uploadName = item.querySelector('.js_path');
                uploadName.value = fileName;
            }

            function fileDelete() {
                if (fileTarget.files[0] == undefined) { 
                    return false;
                }
                let uploadName = item.querySelector('.js_path');
                uploadName.value = '';
            }
        })
    }
    if (fileWrap.length > 0) { 
        fileWrap.forEach((item, idx) => {
            let fileTarget = item.querySelector('.js_file');
            let bl_uploadList__scroll = document.querySelectorAll('.bl_uploadList__scroll');
            if (bl_uploadList__scroll.length > 0) {
                fileTarget.addEventListener('change', fileUpload);
            } else {
                fileTarget.addEventListener('change', fileUpload2);
                initFileUpload2();
            }

            function fileUpload() { // 파일 리스트 upload
                let bl_uploadList = document.querySelector('.bl_uploadList');
                let fileName = fileTarget.files[0].name;
                let fileSize = getByteSize(fileTarget.files[0].size);

                let LI = document.createElement('li');
                let UPLOADNAME = document.createElement('a');
                UPLOADNAME.setAttribute('class','js_path')
                UPLOADNAME.innerText = fileName;
                let DIV = document.createElement('div');
                DIV.setAttribute('class','bl_uploadList_rgt');
                let FILESIZE = document.createElement('span');
                FILESIZE.setAttribute('class', 'font_roboto file_size');
                FILESIZE.innerText = fileSize;
                let DELBTN = document.createElement('div');
                DELBTN.setAttribute('class','del_btn');
                let I = document.createElement('i');
                I.setAttribute('class','fas fa-times color_red cursor-pointer');
                
                LI.appendChild(UPLOADNAME);
                LI.appendChild(DIV);
                DIV.appendChild(FILESIZE);
                DIV.appendChild(DELBTN);
                DELBTN.appendChild(I);
                bl_uploadList.appendChild(LI);

                $('.del_btn').click(function(){ // 파일 삭제
                    let li = $(this).parent().parent();
                    let fileName = $(li).find("a.js_path").text();
                    li.remove();
                    $("#formFile").val("");
                    if (typeof fileList !== "undefined") {
                        for(let i=0; i < fileList.length; i++){
                            if(fileList[i] && fileList[i].name == fileName){
                                let text= fileList[i].size;
                                text = parseInt(text / 1024);
                                text = parseFloat(text / 1024);
                                text = text.toFixed(2);
                                let now = $('#fileMB').text();
                                now = parseFloat(now)-parseFloat(text);
                                now = parseFloat(now).toFixed(2);
                                $('#fileMB').text(now);
                                fileList[i]=null;
                            }
                        }
                    }
                });

                if(UPLOADNAME){

                }
            }

            function fileUpload2() { // 파일 upload
                let fileName = fileTarget.files[0].name;
                let uploadName = item.querySelector('.js_path');
                uploadName.value = fileName;
            }

            function initFileUpload2(){
                let fileNamePath = $(item).find(".bl_upload_path");
                let uploadName = item.querySelector('.js_path')?.value;
                let downloadButton = $(`<a class="btn btnDefault bg-primary text-white bl_upload_btn btn-download">다운로드</a>`);
                $(fileTarget).after(downloadButton);
                if( fileNamePath.length && uploadName){
                    downloadButton.attr('href', '/api/common/upload/download?attachFilePath='+uploadName);
                    downloadButton.removeClass("btn-download-hidden");
                }else{
                    downloadButton.addClass("btn-download-hidden");
                }
            }

            function getByteSize(size) { // 파일 용량(사이즈) 변환
                const byteUnits = ["KB", "MB", "GB", "TB"];
                
                if (size < 1024 ** 2 * 5) { // 5MB 미만 체크
                    for (let i = 0; i < byteUnits.length; i++) {
                        if(i == 0){
                            size = parseInt(size / 1024);
                            if (size < 1024) return size + byteUnits[i];
                        }else{
                            size = parseFloat(size / 1024);
                            size = size.toFixed(2);
                            if (size < 1024) return size + byteUnits[i];

                        }
                    }
                }
            };
        })
    }
});