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
  faSearch,
  faListCheck,
  faLeftLong,
  faRightLong,
);
dom.watch();

import 'gridjs/dist/theme/mermaid.css';
import 'gridjs/dist/theme/mermaid.css';
import hideModifier from '@popperjs/core/lib/modifiers/hide';
import axios from 'axios';
import { VNode } from 'preact';
import ElementEvent from '../../events/elementEvent';
import CategorySelectEvent from '../../events/categorySelectEvent';
import AxiosSetting from '../../setting/AxiosSetting';
import SettingApi from '../../api/settingApi';
import { Grid } from "gridjs";
import { html } from 'gridjs';
import GaleWingApi from '../../api/galeWingApi';

export enum HeaderIndex {
  title,
  link,
  uri,
  type,
  author,
  comments,
  publishedDate,
  uuid,
  imageUrl,
  chkSts,
};

window.onload = async () => {
  // サイドバー初期化
  setupSidebar();

  // axiosのヘッダー設定
  axios.defaults.headers.common = AxiosSetting.header;

  // grid初期化
  const setting = new SettingApi();
  await setting.init();
  setting.outputLog();
  const limit = Number(setting.get('feed_rows'));
  const grid = createGrid([], limit);


  // init event
  setupEvent(grid);
};

/**
 * サイドバー初期化
 */
function setupSidebar() {
  // toggleボタンをセレクト
  const sidebarToggler = document.getElementById('sidebarToggler');

  // 表示状態用の変数
  let showSidebar = true;
  const sidemenu = document.getElementById('sidemenu');
  const mainContent = document.getElementById('mainContent');

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

function createGrid(data: any, limit: any) {

  const grid = new Grid({
    columns: createGridColumn(),
    pagination: {
      limit: Number(limit),
    },
    sort: true,
    search: false,
    data: data,
  });

  grid.render(<HTMLInputElement>document.getElementById('wrapper'));

  grid.on('cellClick', (event: Event, ...columns: any[]) => {
    const col = columns[0];
    const colConfig = columns[1];
    const row = columns[2];

    if (colConfig.name === 'title') {
      const link = row.cells[HeaderIndex.link].data!.toLocaleString();

      // リンク押下の場合はリンクの遷移を実施。それ以外は個別にリンクを表示
      if ((event.target as any).localName !== 'a') {
        window.open(link, '_blank');
      }

      const api = GaleWingApi.getInstance();
      api
        .readCategoryFeed(link)
        .then(() => {

          // リンク押下の場合はリンクの遷移を実施。それ以外は個別にリンクを表示
          const targetElement: HTMLElement | null = event.target as HTMLElement;
          if (targetElement.tagName.toLowerCase() === 'a') {
            targetElement.classList = 'rss-read-link';
          } else {
            targetElement.getElementsByTagName('a')[0].classList = 'rss-read-link';
          }

          row.cells[HeaderIndex.chkSts].data = '1';
        }).catch((error) => {
          console.error(error);
        });
    }
  });

  return grid;
}

function createGridColumn() {
  return [
    {
      name: 'title', hidden: false, formatter: (
        cell: any,
        row: {
          cells: {
            data: string | number | bigint | boolean | object | VNode<any> | null | undefined;
          }[];
        },
      ) =>
        html(
          createTitleLinkText(
            row.cells[HeaderIndex.title].data,
            row.cells[HeaderIndex.link].data,
            row.cells[HeaderIndex.imageUrl].data,
            row.cells[HeaderIndex.chkSts].data,
          ),
        ),
    },
    { name: 'link', hidden: true },
    { name: 'uri', hidden: true },
    { name: 'type', hidden: true },
    { name: 'author', hidden: true },
    { name: 'comments', hidden: true },
    { name: 'publishedDate', hidden: false },
    { name: 'uuid', hidden: true },
    { name: 'imageUrl', hidden: true },
    { name: 'chkSts', hidden: true },
  ];
}

function createTitleLinkText(
  title: string | number | bigint | boolean | object | VNode<any> | null | undefined,
  link: string | number | bigint | boolean | object | VNode<any> | null | undefined,
  imageUrl: string | number | bigint | boolean | object | VNode<any> | null | undefined,
  chkSts: string | number | bigint | boolean | object | VNode<any> | null | undefined,
) {
  return (
    "<div style='display: flex;'>" +
    `<a href='${link}' target="_blank" rel="noopener" class="${chkSts ? 'rss-read-link' : 'rss-link'
    }" data-origin-txt="${title}" data-translation-jp-txt="">${title}</a>` +
    '</div>'
  );
}

function setupEvent(grid: Grid) {
  new ElementEvent(new CategorySelectEvent(grid)).setup('change', document.getElementById('categoryId'));
}

export default { hideModifier };
