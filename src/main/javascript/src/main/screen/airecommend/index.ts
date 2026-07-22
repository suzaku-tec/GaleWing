import 'bootstrap/dist/css/bootstrap.min.css';
import GaleWingApi from '../../api/galeWingApi';

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

window.onload = () => {
  const elements = document.getElementsByClassName("readbtn");

  Array.from(elements).forEach((element) => {
    if (element instanceof HTMLElement) {
      element.addEventListener("click", async (event) => {
        let api = GaleWingApi.getInstance();
        await api.aiRecommendRead(element.dataset.recommendid).then((res) => {
          console.log(res.data);
        });
      });
    }
  });

}

