// postCode 필드 스코프 관리
const postCode = document.getElementById('comment_list').getAttribute('data-post-code');

// 모든 댓글 코드 필드로 관리
let selectedCommentCode;
// 정렬 기능
let align = 1;

document.addEventListener('DOMContentLoaded', function () {
    console.log(postCode);

// ====================== 댓글 조회! ==========================
    function loadComments(align) {

        fetch(`/board/comments?code=${postCode}&align=${align}`)
            .then(res => {
                if (!res.ok) {
                    throw new Error('Network response was not ok');
                }
                return res.json();
            })
            .then(data => {
                console.log(data);
                const commentList = document.querySelector('.comment_list');
                commentList.innerHTML = ''; // 기존 댓글 목록을 초기화

                const currentNickname = data.curNickname;
                const comments = data.comments;

                comments.forEach(cmt => {
                    let commentCode = cmt.commentCode;
                    console.log(cmt);
                    const li = document.createElement('li');
                    li.innerHTML = `
                            <div class="comment_inner_box">
                                <span class="comment_profile_img">
                                    <img src="/images/common/icon_user_profile.svg" alt="user profile icon">
                                </span>
                                <div class="comment_txt_box">
                                    <div class="comment_txt_top">
                                        <p>
                                            <span class="comment_nickname">${cmt.member.nickname}</span>
                                            <span class="comment_date">${new Date(cmt.commentDate).toLocaleString()}</span>
                                        </p>
                                        <div class="auth_box" style="display: none;">
                                            <ul class="comment_btn_list">
                                                <li class="modify_comment_btn" data-comment-modify-code="${commentCode}">수정</li>
                                                <li>|</li>
                                                <li class="delete_comment_btn" data-comment-delete-code="${commentCode}">삭제</li>
                                            </ul>
                                        </div>
                                    </div>
                                    <p class="comment_context">${cmt.commentContext}</p>
                                </div>
                            </div>
                            `;
                    const authBox = li.querySelector('.auth_box');
                    if (cmt.member.nickname === currentNickname) {
                        authBox.style.display = 'block';
                    }
                    commentList.appendChild(li);
                })
            })
            .catch(error => {
                console.error('There was a problem with the fetch operation:', error);
            });
    }

    // 페이지 요청시 상시 댓글 목록 노출
    loadComments(align);

// ====================== 댓글 등록! ==========================
    $(".register_comment_btn").click((e) => {

        const context = $(e.target).siblings('.schedule_tit_box').find('input').val();

        if ($(e.target).siblings('.schedule_tit_box').find('input').val() == null || $(e.target).siblings('.schedule_tit_box').find('input').val() === '') { // 댓글 입력내용이 빈칸이거나 null일경우
            $(".pop_bg, .pop_cont.no_text").addClass("active");
            $('html, body').addClass('fixed');
        } else { // 입력 내용이 있는 경우
            console.log(context);
            console.log(postCode);
            const data = {
                commentContext: context,
                postCode: postCode,
            }
            fetch("/board/comment-regist", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(data)
            })
                .then(response => {
                    if (!response.ok) {
                        throw new Error("Network response was not ok");
                    }
                    return response.json();
                })
                .then(data => {
                    console.log(data);
                    console.log(typeof data);
                    if (data > 0) {
                        $(e.target).siblings('.schedule_tit_box').find('input').val(null); // 등록 후 input 영역 내용 초기화
                        $(e.target).siblings('.schedule_tit_box').find('.tit_counted').text(0); // 댓글 등록 후 글자수 초기화
                        // $(".comment_list").prepend(commentListItem('스드메의문단속', today, context)); // 닉네임, 오늘날짜, 댓글내용 매개변수 설정
                        // 댓글 등록 시 댓글 목록 새로고침
                        loadComments(align);
                    }
                })

        }
    })

// ====================== 댓글 삭제! ==========================

    // 최초로 삭제 클릭 시 댓글 코드를 가져와 필드 스코프에 담는다.
    $(document).on('click', ".delete_comment_btn", (e) => {
        const commentCode = $(e.target).closest('li').data('commentDeleteCode'); // 클릭된 댓글의 commentCode 가져오기
        console.log(commentCode); // 가져온 commentCode 확인 (디버깅용)

        $(".pop_bg, .pop_cont.selective").addClass("active");
        $('html, body').addClass('fixed');

        // 이제 commentCode를 어딘가에 저장하여 후속 처리에서 사용 가능하도록 합니다.
        // 예를 들어, 전역 변수에 저장하거나, 다른 함수의 매개변수로 전달하여 활용할 수 있습니다.
        // 여기서는 이후의 처리를 위해 전역 변수에 저장하도록 하겠습니다.
        selectedCommentCode = commentCode;
    });

    // 필드 스코프에 저장된 값을 활용해서 모듈창으로 값 전달하고 서버로 발사 히히
    $(".pop_btn_list > li, .pop_btn").click(function (e) {
        const scrollTop = $(window).scrollTop(); // 스크롤 위치
        if ($(e.target).closest("li").hasClass("cancel_btn")) {//댓글 삭제 버튼을 클릭시
            console.log(selectedCommentCode);

            fetch("/board/comment-delete", {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(selectedCommentCode)
            })
                .then(res => {
                    if (!res.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return res.json();
                })
                .then(data => {
                    console.log(data);
                    if (data > 0) {
                        console.log("댓글 삭제 성공:", data);
                        // 댓글 삭제 성공 시 UI에서 해당 댓글 제거
                        // $(this).closest('li').remove();
                        loadComments(align);

                        $(".pop_bg, .pop_cont").removeClass("active"); // 팝업창/팝업배경 비활성화
                        $(window).scrollTop(scrollTop);
                        $('html, body').removeClass('fixed');
                    }
                })
                .catch(error => {
                    console.error('댓글 삭제 실패:', error);
                });
        } else {//댓글 삭제 취소 버튼을 클릭시
            console.log("댓글 삭제 취소");
        }
        $(".pop_bg, .pop_cont").removeClass("active"); // 팝업창/팝업배경 비활성화
        $(window).scrollTop(scrollTop);
        $('html, body').removeClass('fixed');
    })

// ====================== 댓글 수정! ==========================

    $(document).on("click", ".modify_comment_btn", function (e) { // arrow function 대신 function 키워드 사용
        let $commentBox = $(this).closest('.comment_inner_box'); // 클릭된 수정 버튼이 속한 댓글 박스를 찾음
        let registeredComment = $commentBox.find(".comment_context").text(); // 등록된 댓글 내용을 가져옴
        let registeredCommentLength = registeredComment.length;

        // 등록된 댓글 내용을 textarea에 입력
        $commentBox.find('.comment_context').replaceWith(`
                <div class="comment_input_area" style="width:100%;">
                    <div class="schedule_tit_box"><input type="text" class="schedule_tit" id="modifyContext" maxLength="100"
                                                         placeholder="내용을 입력하세요" value="${registeredComment}" style="padding-right:180px;"/>
                        <span class="tit_count eng" style="right: 140px;"><span class="tit_counted">${registeredCommentLength}</span>/100</span>
                    </div>
                    <div style="display: flex; align-content: center;">
                        <span class="register_btn register_comment_btn" id="modify_comment_btn" data-type="modify">등록</span>
                        <span class="register_btn register_comment_btn" id="cancel_comment_btn" data-type="cancel" style="margin-left: -125px;">취소</span>
                    </div>                   
                </div>
                `);

        // 수정된 textarea를 보이게 함
        $commentBox.find('.comment_context_edit').addClass('active');
        const commentCode = $(e.target).closest('li').data('commentModifyCode'); // 클릭된 댓글의 commentCode 가져오기
        selectedCommentCode = commentCode;
    });

    $(document).on("click", "#cancel_comment_btn", function (e){
        loadComments(align);
    })

    $(document).on("click", "#modify_comment_btn", function (e) {
        let $commentBox = $(this).closest('.comment_inner_box');
        let modifiedComment = $commentBox.find('#modifyContext').val(); // 수정된 댓글 내용을 가져옴

        console.log("수정된 댓글 내용:", modifiedComment);

        console.log(selectedCommentCode);
        console.log(modifiedComment);

        const data = {
            commentCode: selectedCommentCode,
            commentContext: modifiedComment,
        }

        fetch("/board/comment-modify", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(data)
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                return response.json();
            })
            .then(data => {
                console.log(data);
                console.log(typeof data);
                if (data > 0) {
                    $(e.target).siblings('.schedule_tit_box').find('input').val(null); // 등록 후 input 영역 내용 초기화
                    $(e.target).siblings('.schedule_tit_box').find('.tit_counted').text(0); // 댓글 등록 후 글자수 초기화
                    // $(".comment_list").prepend(commentListItem('스드메의문단속', today, context)); // 닉네임, 오늘날짜, 댓글내용 매개변수 설정
                    // 댓글 등록 시 댓글 목록 새로고침
                    loadComments(align);
                }
            })
    })

    //수정된 댓글 저장 변수
    let modifiedComment = '';

    // 정렬 버튼 클릭시,
    $('.filter_btn').click(() => {
        $(".filter_btn").toggleClass('active').find(".filter_list").stop().slideToggle(); // 클릭할때마다 활성/비활성화 자동설정
    })

    // 정렬 리스트 내 버튼 클릭시, 해당 정렬 내용으로 변경
    $(".filter_list li").click((e) => {
        const filterSelected = $(e.target).text();
        $(".filter_txt").text(filterSelected);

        if (filterSelected === '최신순') {
            align = 1;
            loadComments(align);
        } else {
            align = 2;
            loadComments(align);
        }
    })


    // 댓글 수정 버튼 클릭시,
    // $(document).on("click", ".modify_comment_btn", (e) => {
    //     let registeredComment = $(e.target).parents(".comment_txt_box").find(".comment_context").text(); // 등록된 댓글
    //     let registeredCommentLength = registeredComment.length; //등록된 댓글 길이
    //
    //     $(e.target).parents(".comment_txt_box").find('.schedule_contxt_box').text(registeredComment);//해당 댓글의 입력된 내용으로 textarea 내용 변경
    //     $(e.target).parents(".comment_txt_box").find('.txt_counted').text(registeredCommentLength);//해당 댓글 텍스트 길이로 내용 변경
    //     $(e.target).parents(".comment_txt_box").find('.schedule_txt_box').toggleClass('active');
    // });

    // 댓글 수정 취소 버튼 클릭시,
    $(document).on("click", ".cancel_register", (e) => {
        let registeredComment = $(e.target).parents(".comment_txt_box").find(".comment_context").text(); // 등록된 댓글
        let registeredCommentLength = registeredComment.length; //등록된 댓글 길이
        $(e.target).parents(".comment_txt_box").find('.schedule_contxt_box').val(registeredComment);//해당 댓글의 입력된 내용으로 textarea 내용 변경
        $(e.target).parents(".comment_txt_box").find('.txt_counted').text(registeredCommentLength);//해당 댓글 텍스트 길이로 내용 변경
        $(e.target).parents(".comment_txt_box").find('.schedule_txt_box').toggleClass('active');
    });


    // 수정 후 댓글 등록 버튼 클릭시,
    $(document).on("click", '.register_modified_comment_btn', (e) => {
        countedText = $(e.target).parents(".comment_txt_box").find('.txt_counted').text();

        if (countedText != 0) { // 수정 내용의 입력 길이가 0인 아니라면 - 입력 내용 존재
            $(e.target).parents(".comment_txt_box").find('.comment_context').text(modifiedComment);
            $(e.target).parents(".comment_txt_box").find('.txt_counted').text('0');
            $(e.target).parents(".comment_txt_box").find('.schedule_txt_box').removeClass('active');
        } else { // 수정 내용의 입력 길이가 0이라면 - 입력 내용 없음
            $(".pop_bg, .pop_cont.no_text").addClass("active"); // 입력 내용 없다는 팝업창 알림 활성화
            $('html, body').addClass('fixed'); // 웹 페이지 스크롤 기능 제거
        }

    })

    //댓글 입력시 입력시 텍스트 길이 실시간 반영
    $(document).on('input', '.schedule_tit', (e) => {
        $(".tit_counted").text(e.target.value.length);
    })

    //스케쥴 내용 입력시 텍스트 길이 실시간 반영
    $(document).on('input', '.schedule_contxt_box', (e) => {
        $(".txt_counted").text(e.target.value.length);
    })

})
//댓글 삭제 버튼 클릭시,
// $(document).on('click', '.execute_delete_btn', () => {
//     const index = $(".execute_delete_btn").attr('data-comment-index');
//     $(".comment_list > li").eq(index).remove();
// })