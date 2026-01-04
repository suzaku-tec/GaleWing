import 'bootstrap/dist/css/bootstrap.min.css';
import hideModifier from '@popperjs/core/lib/modifiers/hide';
import Chart from 'chart.js/auto';
import StaticsApi from '../../api/staticsApi';
import 'gridjs/dist/theme/mermaid.css';
import axios from 'axios';
import AxiosSetting from '../../setting/AxiosSetting';

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
export default { hideModifier };

window.onload = function () {
  const main = document.getElementById('main')!;
  // axiosのヘッダー設定
  axios.defaults.headers.common = AxiosSetting.header;

  const readRateCanvas = document.createElement('canvas');

  main.appendChild(readRateCanvas);

  const staticsApi = StaticsApi.getInstance();
  staticsApi.getReadRate().then((response) => {
    new Chart(readRateCanvas, {
      type: 'pie',
      data: {
        labels: ['open', 'Read', 'Unread'],
        datasets: [
          {
            data: [response.data.opened, response.data.activeReads, response.data.totalDelivered - (response.data.activeReads + response.data.opened)],
            borderWidth: 1,
          }],
      },
      options: {},
    });
  });

}
