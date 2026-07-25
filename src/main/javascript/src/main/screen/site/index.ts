import 'bootstrap/dist/css/bootstrap.min.css';
import 'gridjs/dist/theme/mermaid.css';

import hideModifier from '@popperjs/core/lib/modifiers/hide';

import GaleWingApi from '../../api/galeWingApi';

function deleteSite(uuid: string) {
  const api = GaleWingApi.getInstance();
  const url = window.location.href;
  return api.deleteSite(url, uuid).then(() => {
    location.reload();
  });
}

function siteCategory(uuid: string) {
  location.href = location.origin + `/siteCategory?uuid=${uuid}`;
}

async function fixSiteIcon(uuid: string) {
  const api = GaleWingApi.getInstance();
  await api.updateIcon(uuid);
  location.reload();
}

window.onload = () => {
  const feedListBtn = document.getElementById('feedList')!;
  feedListBtn.addEventListener('click', () => {
    const url = new URL(location.href);
    location.href = url.origin;
  });

  const feedUpdate = document.getElementById('feedUpdate')!;
  feedUpdate.addEventListener('click', () => {
    window.location.href = '/site/lastUpdateDate';
  });
};

export default { hideModifier, deleteSite, siteCategory, fixSiteIcon };
