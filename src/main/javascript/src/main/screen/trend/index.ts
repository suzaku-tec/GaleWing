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

      // 初期化
      clearFeedList();
      const popKeyword = document.getElementById("popKeyword") as HTMLInputElement;
      popKeyword.value = link.innerText || '';

      await GaleWingApi.getInstance().keywordFeed(link.innerText || '').then(res => {
        if (res.status != 200) {
          return;
        }

        const keyword = link.innerText || '';
        const feeds: { title: string; link: string }[] = res.data;
        addFeedItem(keyword, feeds);
      });
      popup.style.display = 'flex';
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

  const sendSummary = document.getElementById("sendSummary")!;
  sendSummary.addEventListener('click', () => {
    const linkList = Array.from(document.querySelectorAll<HTMLInputElement>('input[name="feedCheckList"]:checked')).map(checkbox => {
      const link = checkbox.dataset.link ? checkbox.dataset.link : null;
      return {
        link
      };
    });
    const popKeyword = document.getElementById("popKeyword") as HTMLInputElement;
    GaleWingApi.getInstance().keywordLinkSummaryReport(popKeyword.value, linkList);

    const popup = document.getElementById('feedPopup')!;
    popup.style.display = 'none';
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
function addFeedItem(keyword: string, feeds: { title: string; link: string; translateTitle?: string }[]) {
  const feedList = document.getElementById('feedList')!;
  feeds.forEach(feed => {
    const divElement = document.createElement('div');
    const checkbox = document.createElement("input");
    checkbox.type = "checkbox";
    checkbox.name = "feedCheckList";
    checkbox.dataset.link = feed.link;
    const linkElement = document.createElement('a');
    linkElement.href = feed.link;
    linkElement.textContent = feed.translateTitle ? "【翻訳】" + feed.translateTitle : feed.title;
    // checkbox.appendChild(linkElement);
    divElement.appendChild(checkbox);
    divElement.appendChild(linkElement);
    divElement.classList.add('m-1');
    feedList.appendChild(divElement);
  });
}
