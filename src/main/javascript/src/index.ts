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
} from '@fortawesome/free-solid-svg-icons/index';

library.add(faBars, faCheck, faSyncAlt, faPlus, faWrench, faTh, faIdCard);
dom.watch();

import { Grid, html } from 'gridjs';
import 'gridjs/dist/theme/mermaid.css';

import hideModifier from '@popperjs/core/lib/modifiers/hide';

import axios from 'axios';

import ElementEvent from './event/elementEvent';
import UpdateFeed from './event/updateFeed';
import AddSiteEvent from './event/addSiteEvent';
import ExportOpml from './event/exportOpml';
import ImportOpml from './event/importOpml';
import GridLayoutChgEvent from './event/gridLayoutChgEvent';
import CardLayoutChgEvent from './event/cardLayoutChgEvent';

window.onload = function () {
  // toggleボタンをセレクト
  let sidebarToggler = document.getElementById('sidebarToggler');
  // 全ページを囲む親要素をセレクト
  let page = document.getElementsByTagName('main')[0];
  // 表示状態用の変数
  let showSidebar = true;
  var sidemenu = document.getElementById('sidemenu');
  var mainContent = document.getElementById('mainContent');

  // イベント追加
  sidebarToggler.addEventListener('click', () => {
    // 表示状態判別
    if (showSidebar) {
      // page.style.cssText = 'margin-left: -250px';
      sidemenu.classList.add('is-close');
      mainContent.classList.add('wideMainContent');
      showSidebar = false;
    } else {
      sidemenu.classList.remove('is-close');
      mainContent.classList.remove('wideMainContent');
      // page.style.cssText = 'margin-left: 0px';
      showSidebar = true;
    }
  });

  var uri = new URL(window.location.href);
  var ajaxUrl = uri.origin + '/feedlist' + location.search;

  const uuid = new URLSearchParams(window.location.search).get('uuid');

  var grid = new Grid({
    columns: [
      {
        name: 'title',
        hidden: false,
        formatter: (cell, row) =>
          html(
            `<a href='${row.cells[1].data}' target="_blank" rel="noopener" class="rss-link">${row.cells[0].data}</a>`,
          ),
      },
      { name: 'link', hidden: true },
      { name: 'uri', hidden: true },
      { name: 'type', hidden: true },
      { name: 'author', hidden: true },
      { name: 'comments', hidden: true },
      { name: 'publishedDate', hidden: false },
      { name: 'uuid', hidden: true },
    ],
    pagination: true,
    sort: true,
    server: {
      url: ajaxUrl,
      then: (data) => {
        if (uuid) {
          document.getElementById('identifier').nodeValue = uuid;
        }

        return data;
      },
    },
  }).render(document.getElementById('wrapper'));

  grid.on('rowClick', (event, row) => {
    var link = row.cell(1).data.toLocaleString();

    if ((event.target as any).localName != 'a') {
      window.open(link);
    }

    axios
      .post(uri.origin + '/readed', {
        link: link,
      })
      .then((response) => {
        console.log(response);

        // 未読数の更新
        response.data.forEach((element: { uuid: string; count: number }) => {
          var countElement = document.getElementById(element.uuid + '_count');
          countElement.innerText = element.count.toString();
        });

        // 既読表示に変更
        var innerHTML = (event.target as HTMLElement).innerHTML;
        (event.target as any).innerHTML = innerHTML.replace(/rss-link/g, 'rss-readed-link');
      })
      .catch((error) => {
        console.log(error);
      });
  });

  // init event
  new ElementEvent(new UpdateFeed('identifier', grid)).setup(
    'click',
    document.getElementById('updateFeed'),
  );

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
};

export default { hideModifier };
