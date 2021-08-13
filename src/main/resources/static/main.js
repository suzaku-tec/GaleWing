// ページが読み込まれた後に実行
window.onload = () => {
    // toggleボタンをセレクト
    let sidebarToggler = document.getElementById('sidebarToggler')
    // 全ページを囲む親要素をセレクト
    let page = document.getElementsByTagName('main')[0];
    // 表示状態用の変数
    let showSidebar = true;

    // イベント追加
    sidebarToggler.addEventListener('click', () => {

        // 表示状態判別
        if(showSidebar){

            page.style.cssText = 'margin-left: -250px'
            showSidebar = false;

        }else{

            page.style.cssText = 'margin-left: 0px'
            showSidebar = true;

        }
    })
}