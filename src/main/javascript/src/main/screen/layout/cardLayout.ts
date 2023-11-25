import 'bootstrap/dist/css/bootstrap.min.css';

// fortawesome
import { library, dom } from '@fortawesome/fontawesome-svg-core';
import {
  faBars,
  faCheck,
  faSyncAlt,
  faPlus,
  faWrench,
  faTh,
  faIdCard,
  faCogs,
  faStepBackward,
  faBookmark,
  faPlayCircle,
  faBackward,
  faSearch,
  faListCheck,
  faLeftLong,
  faRightLong,
} from '@fortawesome/free-solid-svg-icons/index';

import { faYoutube } from '@fortawesome/free-brands-svg-icons';

library.add(
  faBars,
  faCheck,
  faSyncAlt,
  faPlus,
  faWrench,
  faTh,
  faIdCard,
  faCogs,
  faStepBackward,
  faBookmark,
  faPlayCircle,
  faBackward,
  faYoutube,
  faSearch,
  faListCheck,
  faLeftLong,
  faRightLong,
);
dom.watch();

import 'gridjs/dist/theme/mermaid.css';

import SettingApi from '../../api/settingApi';
import GaleWingApi from '../../api/galeWingApi';
import axios from 'axios';

let cardLayout;

window.onload = async () => {
  // axiosのヘッダー設定
  axios.defaults.headers.common = {
    'X-Requested-With': 'XMLHttpRequest',
    'X-CSRF-TOKEN': document.querySelector('meta[name="csrf-token"]')?.getAttribute('content'),
  };

  let setting = new SettingApi();
  await setting.init();
  setting.outputLog();

  new CardLayout(setting);
};

export default class CardLayout {
  private feedJson: any;

  private displayCount;
  private startIndex;

  constructor(setting: SettingApi) {
    // サイドバー初期化
    this.setupSidebar();

    const rows = setting.get('feed_rows');
    this.displayCount = rows ? Number(rows) : 0;
    this.startIndex = 0;

    let api = GaleWingApi.getInstance();
    api.getFeedList(window.location.href).then((res) => {
      this.feedJson = res.data;

      const displayDataJson = this.feedJson.slice(this.startIndex, this.displayCount + 1);
      this.createList(displayDataJson);
    });

    // イベント紐づけ
    // 各種ボタン
    var previousLink = document.getElementById("previous-link");
    var nextLink = document.getElementById("next-link");

    previousLink?.addEventListener("click", () => {
      if(!previousLink?.parentElement?.classList.contains("disabled")) {
        var str = document.getElementById("pageIndexVal")?.innerText
        var page = Number(str) - 1;
        this.startIndex = page * this.displayCount
        const displayDataJson = this.feedJson.slice(this.startIndex, this.displayCount + 1);
        this.deleteDispList();
        this.createList(displayDataJson);
      }
    })

    nextLink?.addEventListener("click", () => {
      if(!nextLink?.parentElement?.classList.contains("disabled")) {
        var str = document.getElementById("pageIndexVal")?.innerText
        var page = Number(str);
        this.startIndex = page * this.displayCount
        const displayDataJson = this.feedJson.slice(this.startIndex, this.displayCount + 1);
        this.deleteDispList();
        this.createList(displayDataJson);
      }
    })

  }

  private deleteDispList() {
    var layout = document.getElementById('cardLayout');
    while(layout?.firstChild ){
      layout.removeChild( layout.firstChild );
    }
  }

  /**
   * サイドバー初期化
   */
  private setupSidebar() {
    // toggleボタンをセレクト
    let sidebarToggler = document.getElementById('sidebarToggler');

    // 表示状態用の変数
    let showSidebar = true;
    let sidemenu = document.getElementById('sidemenu');
    let mainContent = document.getElementById('mainContent');

    // イベント追加
    sidebarToggler?.addEventListener('click', () => {
      // 表示状態判別
      if (showSidebar) {
        sidemenu?.classList.add('is-close');
        sidemenu?.classList.remove('col-md-3');
        mainContent?.classList.add('wideMainContent');
        mainContent?.classList.remove('col-md-9');
        showSidebar = false;
      } else {
        sidemenu?.classList.remove('is-close');
        sidemenu?.classList.add('col-md-3');
        mainContent?.classList.remove('wideMainContent');
        mainContent?.classList.add('col-md-9');
        showSidebar = true;
      }
    });
  }

  /**
   * <ul class="card-list">
   *   <li class="card-item">
   *   <a href="" class="card-item-link">
   *     <div class="card-item-picture"><img src="img/01.jpeg" alt=""></div>
   *     <div class="card-item-body">
   *       <div class="card-item-title"></div>
   *       <div class="card-item-text"></div>
   *       <div class="card-item-date"></div>
   *     </div>
   *   </a>
   *   <div class="card-item-icom"></div>
   *   </li>
   * </ul>
   */
  private createList(displayDataJson: [any]) {
    const cardListElement = this.createCardListElement();

    displayDataJson
      .map((data) => {
        return this.createCardItem(data.link, data.imageUrl, data.title, '', data.publishedDate);
      })
      .forEach((nodeItem) => {
        cardListElement.appendChild(nodeItem);
      });

    document.getElementById('cardLayout')?.appendChild(cardListElement);
  }

  private createCardListElement() {
    const cardListElement = document.createElement('ul');
    cardListElement.classList.add('card-list');
    return cardListElement;
  }

  private createCardItem(
    url: string,
    imgUrl: string,
    title: string,
    decrypt: string,
    date: string,
  ) {
    const cardItem = document.createElement('li');
    cardItem.classList.add('card-item');
    cardItem.appendChild(this.createCardItemLink(url, imgUrl, title, decrypt, date));
    return cardItem;
  }

  private createCardItemLink(
    url: string,
    imgUrl: string,
    title: string,
    decrypt: string,
    date: string,
  ): HTMLAnchorElement {
    const cardItemLink: HTMLAnchorElement = document.createElement('a');
    cardItemLink.classList.add('card-item-link');
    cardItemLink.href = url;
    cardItemLink.appendChild(this.createCardItemPicture(imgUrl));
    cardItemLink.appendChild(this.createCardItemBody(title, decrypt, date));
    return cardItemLink;
  }

  private createCardItemPicture(imgUrl: string): HTMLDivElement {
    const cardItemPicture: HTMLDivElement = document.createElement('div');
    cardItemPicture.classList.add('card-item-picture');

    const imgEl: HTMLImageElement = document.createElement('img');
    imgEl.src = imgUrl;

    cardItemPicture.appendChild(imgEl);

    return cardItemPicture;
  }
  private createCardItemBody(title: string, decrypt: string, date: string): HTMLDivElement {
    const cardItemBody: HTMLDivElement = document.createElement('div');
    cardItemBody.classList.add('card-item-body');
    cardItemBody.appendChild(this.createCardItemTitle(title));
    cardItemBody.appendChild(this.createCardItemText(decrypt));
    cardItemBody.appendChild(this.createCardItemDate(date));
    return cardItemBody;
  }
  private createCardItemTitle(title: string): HTMLDivElement {
    const cardItemTitle: HTMLDivElement = document.createElement('div');
    cardItemTitle.classList.add('card-item-title');
    cardItemTitle.innerText = title;
    return cardItemTitle;
  }

  private createCardItemText(decrypt: string): HTMLDivElement {
    const cardItemText: HTMLDivElement = document.createElement('div');
    cardItemText.classList.add('card-item-text');
    cardItemText.innerText = decrypt;
    return cardItemText;
  }

  private createCardItemDate(date: string): HTMLDivElement {
    const cardItemDate: HTMLDivElement = document.createElement('div');
    cardItemDate.classList.add('card-item-text');
    cardItemDate.innerText = date;
    return cardItemDate;
  }
}
