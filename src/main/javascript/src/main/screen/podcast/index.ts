import 'bootstrap/dist/css/bootstrap.min.css';

// fortawesome
import { library, dom } from '@fortawesome/fontawesome-svg-core';
import {
  faBars,
  faSyncAlt,
  faPlus,
} from '@fortawesome/free-solid-svg-icons/index';

library.add(
  faBars,
  faSyncAlt,
  faPlus,
);
dom.watch();

import 'gridjs/dist/theme/mermaid.css';

import hideModifier from '@popperjs/core/lib/modifiers/hide';
import ElementEvent from '../../events/elementEvent';
import AddPodcastEvent from '../../events/modal/addPodcastEvent';
import UpdatePodcatFeedEvent from '../../events/updatePodcatFeedEvent';
import Grid, { Row, html, h } from 'gridjs';
import SettingApi from '../../api/settingApi';
import GaleWingApi from '../../api/galeWingApi';
import { read } from 'fs';

const gridHeaderIndex = {
  id: 0,
  url: 1,
  title: 2,
  read: 3,
}

window.onload = async () => {
  // サイドバー初期化
  setupSidebar();

  // init event
  setupEvent();

  const setting = new SettingApi();
  await setting.init();
  const limit = Number(setting.get('feed_rows'));

  GaleWingApi.getInstance().podcastNotReadFeed().then(async (res: { data: any; }) => {
    const data = res.data;
    const grid = new Grid({
      columns: [
        { name: 'id', hidden: true },
        { name: 'url', hidden: true },
        {
          name: 'title',
          hidden: false,
          formatter: (cell: any, row: any) => {
            console.log(row);
            return html(`<a href="${row.cells[gridHeaderIndex.url].data}" target="_blank">${row.cells[gridHeaderIndex.title].data}</a>`);
          }
        },
        { name: 'read', hidden: true },
        { name: 'publishedDate', hidden: false },
      ],
      pagination: {
        limit: Number(limit),
      },
      sort: true,
      search: false,
      data: data,
    }).render(<HTMLInputElement>document.getElementById('wrapper'));

    grid.on('cellClick', (event: Event, ...columns: any[]) => {
      const col = columns[0];
      const colConfig = columns[1];
      const row = columns[2]!;

      if (colConfig.name === 'title') {
        GaleWingApi.getInstance().podcastMarkRead(<string>row.cells[gridHeaderIndex.url].data).catch((error: any) => {
          alert('Failed to mark as read.');
        });
      }
    });
  });

};

function setupEvent() {
  new ElementEvent(new AddPodcastEvent()).setup('click', document.getElementById('addPodcast'));
  new ElementEvent(new UpdatePodcatFeedEvent()).setup('click', document.getElementById('updateFeed'));
}


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

export default { hideModifier };
