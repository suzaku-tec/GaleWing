import 'bootstrap/dist/css/bootstrap.min.css';

import { library, dom } from '@fortawesome/fontawesome-svg-core';

import {
  faRss,
  faChartSimple,
  faGear,
  faScrewdriverWrench,
} from '@fortawesome/free-solid-svg-icons/index';

library.add(
  faRss,
  faChartSimple,
  faGear,
  faScrewdriverWrench,
);
dom.watch();

window.addEventListener('load', () => {
  const toggleBtn = document.getElementById('toggleBtn');
  const workspace = document.getElementById('workspace');

  // ボタンが押されたら 'open' クラスを付け外しする
  toggleBtn.addEventListener('click', () => {
    workspace.classList.toggle('open');
  });
});
