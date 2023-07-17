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

import hideModifier from '@popperjs/core/lib/modifiers/hide';

import axios from 'axios';

import ElementEvent from '../../events/elementEvent';
import AddSiteEvent from '../../events/modal/addSiteEvent';
import ExportOpml from '../../events/exportOpml';
import ImportOpml from '../../events/modal/importOpml';
import GridLayoutChgEvent from '../../events/gridLayoutChgEvent';
import CardLayoutChgEvent from '../../events/cardLayoutChgEvent';

import PlaySound from '../../events/playSound';
import TranslationEnJp from '../../events/translationEnJpEvent';
import ReadDispListEvent from '../../events/readDispListEvent';
import GaleWingGrid from './galeWingGrid';
import TitleListEvent from '../../events/markdown/titleListEvent';

window.onload = async () => {
  // サイドバー初期化
  setupSidebar();

  // axiosのヘッダー設定
  axios.defaults.headers.common = {
    'X-Requested-With': 'XMLHttpRequest',
    'X-CSRF-TOKEN': document.querySelector('meta[name="csrf-token"]')?.getAttribute('content'),
  };

  // grid初期化
  let grid = GaleWingGrid.getInstance();
  grid.setupGrid();

  // init event
  setupEvent();
};

function setupEvent() {
  new ElementEvent(new AddSiteEvent()).setup('click', document.getElementById('addSite'));
  new ElementEvent(new ExportOpml()).setup('click', document.getElementById('exportOpml'));
  new ElementEvent(new ImportOpml()).setup('click', document.getElementById('importOpml'));
  new ElementEvent(new GridLayoutChgEvent()).setup(
    'click',
    document.getElementById('gridLayoutItem'),
  );

  new ElementEvent(new TranslationEnJp()).setup(
    'click',
    document.getElementById('translationEnJp'),
  );

  new ElementEvent(new CardLayoutChgEvent()).setup(
    'click',
    document.getElementById('cardLayoutItem'),
  );

  new ElementEvent(new ReadDispListEvent()).setup('click', document.getElementById('checkList'));

  new ElementEvent(new TitleListEvent()).setup('click', document.getElementById('mdTitleList'));

  let ps = setupPlayer();
  new ElementEvent(ps).setup('click', document.getElementById('playTitle'));

  document.getElementById('subPrev')?.addEventListener('click', () => {
    (<HTMLElement>document.querySelector('button[title="Previous"]')).click();
  });
  document.getElementById('subNext')?.addEventListener('click', () => {
    (<HTMLElement>document.querySelector('button[title="Next"]')).click();
  });
}

function setupPlayer() {
  let ps = new PlaySound();
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

  return ps;
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
