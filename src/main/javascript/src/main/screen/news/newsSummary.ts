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
import AxiosSetting from '../../setting/AxiosSetting';
import { Modal } from 'bootstrap';
import { marked } from "marked";

marked.use({
  async: false,
  pedantic: false,
  gfm: true,
});

window.onload = async () => {
  // axiosのヘッダー設定
  axios.defaults.headers.common = AxiosSetting.header;

  let api = GaleWingApi.getInstance();

  let summaryBtnList = document.getElementsByName("summary");
  summaryBtnList.forEach(summaryBtn => {
    let modal = new Modal(document.getElementById('exampleModal')!);

    summaryBtn?.addEventListener("click", async () => {
      let summary = summaryBtn.dataset.summary!;
      let modalBody = document.getElementById("modal-body");
      const html: string = await marked(summary);
      modalBody!.innerHTML = html;
    });
  });

  let delBtnList = document.getElementsByName("del");
  delBtnList.forEach(delBtn => {
    delBtn?.addEventListener("click", (e) => {
      let uuid = delBtn.dataset.id!;
      api.deleteSummary(uuid).then(() => delBtn.parentElement?.parentElement?.classList.add("grid-card-read"));
    });
  });
};

export default { hideModifier };
