import 'bootstrap/dist/css/bootstrap.min.css';
import hideModifier from '@popperjs/core/lib/modifiers/hide';

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

import ElementEvent from '../../events/elementEvent';
import UpdateEvent from '../../events/updateEvent';
import GaleWingApi from '../../api/galeWingApi';

library.add(faBars, faCheck, faSyncAlt, faPlus, faWrench, faTh, faIdCard);
dom.watch();

window.onload = function () {

  const checkCollection = document.getElementsByClassName("form-check-input");
  let api = GaleWingApi.getInstance();
  Array.from(checkCollection).forEach(checkEl => {
    checkEl.addEventListener("click", () => {
      const val = (<HTMLInputElement>checkEl).checked ? "1": "0";
      api.functionCtrlUpdate(checkEl.id, val);
    });
  })
};

export default { hideModifier };
