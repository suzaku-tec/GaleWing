import { TempusDominus } from "@eonasdan/tempus-dominus";
import "@eonasdan/tempus-dominus/dist/css/tempus-dominus.min.css";
import 'bootstrap/dist/css/bootstrap.min.css';
import GaleWingApi from "../../api/galeWingApi";

// 初期化処理
window.onload = () => {
  const pickerElement = document.getElementById("datePicker")!;
  const calendarElement = document.getElementById("calendar")!;
  const picker = new TempusDominus(pickerElement, {
    display: {
      viewMode: "calendar",
      components: {
        calendar: true,
        date: true,
        month: true,
        year: true,
        decades: true,
        clock: true,
        hours: true,
        minutes: true,
        seconds: false,
      },
    },
    container: calendarElement,
    localization: {
      locale: "ja",
      format: "yyyy/MM/dd",
    },
  });

  pickerElement.addEventListener("change.datetimepicker", () => {
    console.log(picker.dates.picked); // picker を使用
  });

  const popup = document.getElementById('feedPopup')!;
  // const itemListEl = document.getElementById('itemList')!;
  // const showLink = document.getElementById('showItemsLink')!;
  const showLinks = document.querySelectorAll('.show-items-link') as NodeListOf<HTMLTableCellElement>;
  const closeBtn = popup.querySelector('.close-btn')!;

  showLinks.forEach(link => {
    link.addEventListener('click', async (e) => {
      e.preventDefault();

      // リストの初期化
      clearFeedList();

      await GaleWingApi.getInstance().keywordFeed(link.innerText || '').then(res => {
        if (res.status != 200) {
          return;
        }

        const feeds: { title: string; url: string }[] = res.data;
        addFeedItem(feeds);
      });
      popup.style.display = 'block';
    });
  });

  // 閉じるボタン
  closeBtn.addEventListener('click', () => {
    popup.style.display = 'none';
  });

  // 背景クリックで閉じる（任意）
  popup.addEventListener('click', (e) => {
    if (e.target === popup) {
      popup.style.display = 'none';
    }
  });
}

/**
 * フィードリストのクリア
 */
function clearFeedList() {
  const feedList = document.getElementById('feedList')!;
  while (feedList.firstChild) {
    feedList.removeChild(feedList.firstChild);
  }
}

/**
 * フィードリストにアイテムを追加
 * 
 * @param feeds フィード情報
 */
function addFeedItem(feeds: { title: string; url: string }[]) {
  const feedList = document.getElementById('feedList')!;
  feeds.forEach(feed => {
    const listItem = document.createElement('li');
    const linkElement = document.createElement('a');
    linkElement.href = feed.url;
    linkElement.textContent = feed.title;
    listItem.appendChild(linkElement);
    feedList.appendChild(listItem);
  });
}
