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

import SettingApi from '../../api/settingApi';
import GaleWingApi from '../../api/galeWingApi';
import axios from 'axios';

window.onload = async () => {
  var setting = new SettingApi();
  await setting.init();
  setting.outputLog();

  var uri = new URL(window.location.href);

  var api = GaleWingApi.getInstance();
  var target = <HTMLAnchorElement>document.getElementById('target')!;

  api.jaroWinklerDistance(target.innerText).then((res) => {
    var grid = new Grid({
      columns: [
        {
          name: 'title',
          hidden: false,
          formatter: (cell, row) =>
            html(
              `<a href='javascript:void(0)' target="_blank" rel="noopener" class="rss-link">${row.cells[0].data}</a>`,
            ),
        },
        { name: 'link', hidden: true },
        { name: 'uri', hidden: true },
        { name: 'author', hidden: true },
        { name: 'comments', hidden: true },
        { name: 'publishedDate', hidden: false },
        { name: 'uuid', hidden: true },
      ],
      pagination: {
        enabled: true,
        limit: Number(setting.get('feed_rows')),
      },
      sort: true,
      search: true,
      data: res.data,
    }).render(<HTMLInputElement>document.getElementById('wrapper'));

    grid.on('rowClick', (event, row) => {
      var link = row?.cell(1).data?.toLocaleString();
      if (link) {
        window.open(link);
        axios
          .post(uri.origin + '/readed', {
            link: link,
          })
          .then(() => {
            // 既読表示に変更
            if ((event.target as any).localName == 'a') {
              (event.target as HTMLElement).classList.remove('rss-link');
              (event.target as HTMLElement).classList.add('rss-readed-link');
            } else {
              var innerHTML = (event.target as HTMLElement).innerHTML;
              (event.target as any).innerHTML = innerHTML.replace(/rss-link/g, 'rss-readed-link');
            }
          })
          .catch((error: any) => {
            console.log(error);
          });
      }
    });
  });
};
