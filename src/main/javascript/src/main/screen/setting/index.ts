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

library.add(faBars, faCheck, faSyncAlt, faPlus, faWrench, faTh, faIdCard);
dom.watch();

window.onload = function () {
  new ElementEvent(new UpdateEvent()).setup('click', document.getElementById('update'));
};

export default { hideModifier };
