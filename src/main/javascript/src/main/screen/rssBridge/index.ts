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
import axios from 'axios';
import { logger } from 'handlebars';

window.onload = function () {

  const connectSelect = document.getElementById('connectSelect') as HTMLSelectElement;
  connectSelect.addEventListener('change', () => {
    const connectSelectValue = connectSelect.options[connectSelect.selectedIndex].value;
    const keySelect = document.getElementById('keySelect') as HTMLSelectElement;
    Array.from(keySelect.options).forEach((option) => {
      if (option.value.startsWith(connectSelectValue + ":") || option.value.indexOf(":") === -1) {
        option.style.display = '';
      } else {
        option.style.display = 'none';
      }
    });
  });

  const keySelect = document.getElementById('keySelect') as HTMLSelectElement;
  const connectSelectValue = connectSelect.options[connectSelect.selectedIndex].value;
  Array.from(keySelect.options).forEach((option) => {
    if (option.value.startsWith(connectSelectValue + ":") || option.value.indexOf(":") === -1) {
      option.style.display = '';
    } else {
      option.style.display = 'none';
    }
  });

  document.getElementById('executeBtn')?.addEventListener('click', () => {
    let api = GaleWingApi.getInstance();

    const contentsListDiv = document.getElementById('contentsList');

    removeAllChildren(contentsListDiv!);

    const connectSelect = document.getElementById('connectSelect') as HTMLSelectElement;
    const connectSelectValue = connectSelect.options[connectSelect.selectedIndex].value;

    const keySelect = document.getElementById('keySelect') as HTMLSelectElement;
    const keySelectValue = keySelect.options[keySelect.selectedIndex].value;
    const colonIndex: number = keySelectValue.indexOf(":");
    const key: string = colonIndex !== -1 ? keySelectValue.slice(colonIndex + 1) : "";

    createApiStrategy(api)(connectSelectValue)(key).then((res) => {
      res.data.forEach((item: any) => {
        const div = document.createElement('div');
        div.innerHTML = item;
        contentsListDiv?.appendChild(div);
      });
    });

  });
};

type ApiFn = (key: string) => Promise<axios.AxiosResponse<any, any>>;

function createApiStrategy(api: GaleWingApi): (key: string) => ApiFn {
  const strategies: Record<string, ApiFn> = {
    instagram: (key: string) => api.instantiateRssBridge(key),
    reddit: (key: string) => api.redditContentsList(key),
    bluesky: (key: string) => api.blueskyContentsList(key),
    // 新規追加はここに記述
  };
  return (service: string): ApiFn => {
    const strategy = strategies[service];
    if (!strategy) {
      throw new Error(`Unsupported service: ${service}`);
    }
    return strategy;
  };
}

function removeAllChildren(parent: HTMLElement): void {
  while (parent.firstChild) {
    parent.removeChild(parent.firstChild);
  }
}


export default { hideModifier };
