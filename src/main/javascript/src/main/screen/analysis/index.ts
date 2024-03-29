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
import AnalysisReadAll from '../../events/analysisReadAll';

window.onload = async () => {
  let api = GaleWingApi.getInstance();
  let target = <HTMLAnchorElement>document.getElementById('target')!;

  await api.jaroWinklerDistance(target.innerText).then(async (res) => {
    let grid = await createGrid(res.data);
    initGridEvent(grid);

    new ElementEvent(new AnalysisReadAll()).setup('click', document.getElementById('playTitle'));
    return Promise.resolve();
  });
};

async function createGrid(data: any) {
  let setting = new SettingApi();
  await setting.init();
  setting.outputLog();

  return new Grid({
    columns: [
      {
        name: 'title',
        hidden: false,
        formatter: (cell, row) =>
          html(
            `<a href='javascript:void(0)' rel="noopener" class="rss-link" data-link="${row.cells[1].data}">${row.cells[0].data}</a>`,
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
      limit: Number(setting.get('feed_rows')),
    },
    sort: true,
    search: true,
    data: data,
  }).render(<HTMLInputElement>document.getElementById('wrapper'));
}

function initGridEvent(grid: Grid) {
  let api = GaleWingApi.getInstance();

  grid.on('rowClick', (event, row) => {
    let link = row?.cell(1).data?.toLocaleString();
    if (link) {
      window.open(link);
      api.read(link).catch((error: any) => {
        console.log(error);
      });
    }
  });
}
