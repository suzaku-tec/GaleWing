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
);
dom.watch();

import { Grid, html } from 'gridjs';
import 'gridjs/dist/theme/mermaid.css';

import hideModifier from '@popperjs/core/lib/modifiers/hide';

import axios from 'axios';

import ElementEvent from '../../events/elementEvent';
import UpdateFeed from '../../events/updateFeed';
import AddSiteEvent from '../../events/modal/addSiteEvent';
import ExportOpml from '../../events/exportOpml';
import ImportOpml from '../../events/modal/importOpml';
import GridLayoutChgEvent from '../../events/gridLayoutChgEvent';
import CardLayoutChgEvent from '../../events/cardLayoutChgEvent';
import ReadAllShowFeed from '../../events/readAllShowFeed';

import init from '../cardGridLayout';

import GaleWingApi from '../../api/galeWingApi';
import PlaySound from '../../events/playSound';
import SettingApi from '../../api/settingApi';

var setting: SettingApi;

window.onload = async () => {
  setting = new SettingApi();
  await setting.init();
  setting.outputLog();

  // サイドバー初期化
  setupSidebar();

  var uri = new URL(window.location.href);

  var api = GaleWingApi.getInstance();
  api
    .getFeedList(window.location.href)
    .then((res) => {
      var grid = new Grid({
        columns: [
          {
            name: 'title',
            hidden: false,
            formatter: (cell, row) =>
              html(
                `<a name="analysis" style="margin-right: 5px"  href='${
                  window.location.origin +
                  '/analysis/?targetLink=' +
                  encodeURIComponent(String(row.cells[1].data))
                }'><i class="fas fa-search"></i></a>
                <a href='${row.cells[1].data}' target="_blank" rel="noopener" class="rss-link">${
                  row.cells[0].data
                }</a>`,
              ),
          },
          { name: 'link', hidden: true },
          { name: 'uri', hidden: true },
          { name: 'type', hidden: true },
          { name: 'author', hidden: true },
          { name: 'comments', hidden: true },
          { name: 'publishedDate', hidden: false },
          { name: 'uuid', hidden: true },
          {
            name: '',
            hidden: false,
            formatter: (_, row) => html(`<i class="fas fa-bookmark" role="button"></i>`),
          },
        ],
        pagination: {
          enabled: true,
          limit: Number(setting.get('feed_rows')),
        },
        sort: true,
        search: true,
        data: res.data,
      }).render(<HTMLInputElement>document.getElementById('wrapper'));
      new ElementEvent(new UpdateFeed('identifier', grid)).setup(
        'click',
        document.getElementById('updateFeed'),
      );

      new ElementEvent(new ReadAllShowFeed(grid)).setup(
        'click',
        document.getElementById('readAllShowFeed'),
      );

      grid.on('rowClick', (event, row) => {
        var link: string | undefined = undefined;
        if ((event.target as any).localName == 'path') {
          var uuid = row?.cell(7).data?.toLocaleString();
          link = row?.cell(1).data?.toLocaleString();
          stack(uuid, link);
          return;
        }

        if (
          (event.target as any).name === 'analysis' ||
          ((event.target as Element).parentElement as HTMLInputElement).name === 'analysis'
        ) {
          return;
        }

        link = row?.cell(1).data?.toLocaleString();
        if ((event.target as any).localName != 'a') {
          window.open(link);
        }

        console.log('link:', link);

        axios
          .post(uri.origin + '/readed', {
            link: link,
          })
          .then((response) => {
            // 未読数の更新
            response.data.forEach((element: { uuid: string; count: number }) => {
              var countElement = <HTMLInputElement>document.getElementById(element.uuid + '_count');
              countElement.innerText = element.count.toString();
            });

            // 既読表示に変更
            if ((event.target as any).localName == 'a') {
              (event.target as HTMLElement).classList.remove('rss-link');
              (event.target as HTMLElement).classList.add('rss-readed-link');
            } else {
              var innerHTML = (event.target as HTMLElement).innerHTML;
              (event.target as any).innerHTML = innerHTML.replace(/rss-link/g, 'rss-readed-link');
            }
          })
          .catch((error) => {
            console.log(error);
          });
      });

      init(9, 3, res.data);
    })
    .catch((error) => {
      throw new Error(error);
    });

  // init event
  new ElementEvent(new AddSiteEvent()).setup('click', document.getElementById('addSite'));
  new ElementEvent(new ExportOpml()).setup('click', document.getElementById('exportOpml'));
  new ElementEvent(new ImportOpml()).setup('click', document.getElementById('importOpml'));
  new ElementEvent(new GridLayoutChgEvent()).setup(
    'click',
    document.getElementById('gridLayoutItem'),
  );

  new ElementEvent(new CardLayoutChgEvent()).setup(
    'click',
    document.getElementById('cardLayoutItem'),
  );

  var ps = new PlaySound();
  ps.setTalking(() => {
    // TODO 再生のコントロールを検討する
    var rssLinks = document.getElementsByClassName('rss-link');

    (async () => {
      await Array.from(rssLinks).reduce((promise, rssLink) => {
        return promise.then(async () => {
          await ps.genAudio(rssLink.textContent);
        });
      }, Promise.resolve());
    })();
  });
  new ElementEvent(ps).setup('click', document.getElementById('playTitle'));
};

function stack(uuid: string | null | undefined, link: string | undefined) {
  var api = GaleWingApi.getInstance();
  api.stackFeed(window.location.href, uuid, link);
}

/**
 * サイドバー初期化
 */
function setupSidebar() {
  // toggleボタンをセレクト
  let sidebarToggler = document.getElementById('sidebarToggler');

  // 表示状態用の変数
  let showSidebar = true;
  var sidemenu = document.getElementById('sidemenu');
  var mainContent = document.getElementById('mainContent');

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
