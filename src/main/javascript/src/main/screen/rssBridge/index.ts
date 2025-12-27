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

library.add(faBars, faCheck, faSyncAlt, faPlus, faWrench, faTh, faIdCard);
dom.watch();

import GaleWingApi from '../../api/galeWingApi';

window.onload = function () {

  const connectSelect = document.getElementById('connectSelect') as HTMLSelectElement;
  const connectSelectValue = connectSelect.options[connectSelect.selectedIndex].value;
  connectSelect.addEventListener('change', () => {
    const keySelect = document.getElementById('keySelect') as HTMLSelectElement;
    Array.from(keySelect.options).forEach((option) => {
      if (option.value.startsWith(connectSelectValue + ":")) {
        option.style.display = '';
      } else {
        option.style.display = 'none';
      }
    });
  });

  const keySelect = document.getElementById('keySelect') as HTMLSelectElement;
  Array.from(keySelect.options).forEach((option) => {
    if (option.value.startsWith(connectSelectValue + ":")) {
      option.style.display = '';
    } else {
      option.style.display = 'none';
    }
  });

  document.getElementById('executeBtn')?.addEventListener('click', () => {
    let api = GaleWingApi.getInstance();

    const contentsListDiv = document.getElementById('contentsList');

    const connectSelect = document.getElementById('connectSelect') as HTMLSelectElement;
    const connectSelectValue = connectSelect.options[connectSelect.selectedIndex].value;

    if (connectSelectValue === 'instagram') {
      const keySelect = document.getElementById('keySelect') as HTMLSelectElement;
      const keySelectValue = keySelect.options[keySelect.selectedIndex].value;
      const colonIndex: number = keySelectValue.indexOf(":");
      const key: string = colonIndex !== -1 ? keySelectValue.slice(colonIndex + 1) : "";

      api.instantiateRssBridge(key).then((res) => {
        res.data.forEach((item: any) => {
          const div = document.createElement('div');
          div.innerHTML = item;
          contentsListDiv?.appendChild(div);
        });
      });
    }

  });
};

export default { hideModifier };
