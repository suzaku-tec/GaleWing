import 'bootstrap/dist/css/bootstrap.min.css';

import {dom, library} from '@fortawesome/fontawesome-svg-core';

import {faChartSimple, faGear, faRss, faScrewdriverWrench,} from '@fortawesome/free-solid-svg-icons';

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
