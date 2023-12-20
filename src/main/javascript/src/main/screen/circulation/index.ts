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
import GaleWingApi from '../../api/galeWingApi';
import { Grid } from 'gridjs';
import AxiosSetting from '../../setting/AxiosSetting';

window.onload = async () => {
  // axiosのヘッダー設定
  axios.defaults.headers.common = AxiosSetting.header;

  let api = GaleWingApi.getInstance();
  const circulationList = await api.circulationList();

  new Grid({
    columns: [
      { name: 'title', hidden: false },
      { name: 'status', hidden: false },
    ],
    sort: true,
    search: true,
    data: circulationList.data,
    }).render(<HTMLInputElement>document.getElementById('wrapper'));
};


export default { hideModifier };
