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
import ElementEvent from '../../events/elementEvent';
import UpdateFeed from '../../events/updateFeed';

window.onload = async () => {
  var setting = new SettingApi();
  await setting.init();
  setting.outputLog();

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
              `<a href='${row.cells[1].data}' target="_blank" rel="noopener" class="rss-link">${row.cells[0].data}</a>`,
            ),
        },
        { name: 'link', hidden: true },
        { name: 'uri', hidden: true },
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
  });
};
